package ru.tinted_knight.sberbanksms.list_all;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.RecyclerView.DividerItemDecoration;

public class ListAllActivity extends AppCompatActivity {

    ListViewModel mViewModel;
    RecyclerView rvMain;
    ListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);

        mViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        mViewModel.init();

        rvMain = findViewById(R.id.rvMain);

        registerObservers();
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
                    }
                    else {
                        adapter.swapData(simpleEntities);
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                    Log.d("TAGG", ":: null");
            }
        });
    }
}
