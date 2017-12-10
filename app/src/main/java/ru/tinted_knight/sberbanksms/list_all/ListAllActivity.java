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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.RecyclerView.DividerItemDecoration;
import ru.tinted_knight.sberbanksms.Settings.Settings;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;
import ru.tinted_knight.sberbanksms.list_all.ui.ListRecyclerViewAdapter;

public class ListAllActivity
        extends AppCompatActivity
        implements ListAllFragment.OnItemClickListener {

    private static final int REQUEST_CODE_GET_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();
    }

    private void initNormalView() {
        setContentView(R.layout.activity_list_all);
        initBottomBar();
        ListAllFragment fragment = ListAllFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flMain, fragment, ListAllFragment.TAG).commit();
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
            } else {
                initNoPermissionsView();
            }
        }
    }

    @Override
    public void onItemClick(int id) {

    }
}
