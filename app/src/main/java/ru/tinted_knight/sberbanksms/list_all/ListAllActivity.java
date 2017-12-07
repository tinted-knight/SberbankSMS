package ru.tinted_knight.sberbanksms.list_all;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;

import java.util.List;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.RecyclerView.DividerItemDecoration;
import ru.tinted_knight.sberbanksms.Settings.Settings;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;
import ru.tinted_knight.sberbanksms.list_all.ui.ListRecyclerViewAdapter;
import ru.tinted_knight.sberbanksms.main_view.Main2;

public class ListAllActivity
        extends AppCompatActivity
        implements ListAllViewModel.IShowProgress {

    private static final int REQUEST_CODE_GET_PERMISSIONS = 100;

    ListAllViewModel mViewModel;
    RecyclerView rvMain;
    ListRecyclerViewAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
    }

    private void initNormalView() {
        setContentView(R.layout.activity_list_all);
        initBottomBar();
    }

    private void initNoPermissionsView() {
        setContentView(R.layout.activity_list_all_permission_denied);
        Button btnTryAgain = findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }

    private void createViewModel() {
        mViewModel = ViewModelProviders.of(this).get(ListAllViewModel.class);
        rvMain = findViewById(R.id.rvMain);
        //TODO: onResume, onPause
        registerObservers();
        mViewModel.onCreate();
    }

    private void checkPermissions() {
        int readSms = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_SMS);
        int receiveSms = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.RECEIVE_SMS);

        if (readSms != PackageManager.PERMISSION_GRANTED || receiveSms != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                    REQUEST_CODE_GET_PERMISSIONS
            );
        } else {
            initNormalView();
            createViewModel();
        }
    }

    private void initBottomBar() {
        BottomNavigationView bbar = findViewById(R.id.bottomNavigationView);
        bbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bbSettings:
                        item.setChecked(true);
                        Intent intent = new Intent(ListAllActivity.this, Settings.class);
                        startActivity(intent);
                        break;
                    case R.id.bbCards:
                        break;
                    case R.id.bbFilter:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GET_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initNormalView();
                createViewModel();
            } else {
                initNoPermissionsView();
            }
        }
    }

    //TODO: onResume, onPause
    private void registerObservers() {
        mViewModel.getData().observe(this, new Observer<List<SimpleEntity>>() {
            @Override
            public void onChanged(@Nullable List<SimpleEntity> simpleEntities) {
                if (simpleEntities != null) {
                    if (adapter == null) {
                        adapter = new ListRecyclerViewAdapter(getApplicationContext(), simpleEntities);
                        rvMain.setAdapter(adapter);
                        rvMain.setLayoutManager(new LinearLayoutManager(ListAllActivity.this));
                        rvMain.addItemDecoration(
                                new DividerItemDecoration(
                                        ListAllActivity.this, DividerItemDecoration.VERTICAL_LIST));
                        rvMain.setVerticalScrollBarEnabled(true);
                    } else {
                        adapter.swapData(simpleEntities);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        mViewModel.setProgressListener(ListAllActivity.this);
    }

    public void popupMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressStart(String title, String text, int max) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(max);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(text);
        progressDialog.setProgress(0);
        progressDialog.show();

        mViewModel.mProgress.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer progress) {
                progressDialog.setProgress(progress);
            }
        });
    }

    @Override
    public void onProgressHide() {
//        progressDialog.dismiss();
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setTitle("Done");
        progressDialog.setMessage("Thank you for patience. Tap anywhere outside.");

        mViewModel.mProgress.removeObservers(this);
    }
}
