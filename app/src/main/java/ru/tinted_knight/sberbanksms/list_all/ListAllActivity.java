package ru.tinted_knight.sberbanksms.list_all;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.RecyclerView.DividerItemDecoration;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;
import ru.tinted_knight.sberbanksms.list_all.ui.ListRecyclerViewAdapter;

public class ListAllActivity
        extends AppCompatActivity
        implements ListAllViewModel.IShowProgress {

    ListAllViewModel mViewModel;
    RecyclerView rvMain;
    ListRecyclerViewAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);

        mViewModel = ViewModelProviders.of(this).get(ListAllViewModel.class);

        rvMain = findViewById(R.id.rvMain);

        //TODO: onResume, onPause
        registerObservers();

//        mViewModel.firstStart();
        mViewModel.onCreate();
    }

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
                } else
                    Log.d("TAGG", ":: null");
            }
        });

        mViewModel.mFirstRun.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null && aBoolean)
                    popupMessage("first run");
                else
                    popupMessage("common run or null");
            }
        });

        mViewModel.setProgressListener(this);
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
                Log.d("TAGG", "progressUpdate: " + progress);
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
