package ru.tinted_knight.sberbanksms.detail_screen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.databinding.ActivityDetailBinding;
import ru.tinted_knight.sberbanksms.detail_screen.db.MessageEntity;

public class DetailActivity extends AppCompatActivity {

    DetailViewModel mDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        long id = getIntent().getLongExtra("_id", -1);
        if (id != -1) {
            mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
            mDetailViewModel.init(binding, id);
            mDetailViewModel.loadData();
            registerMessageEntityObserver();
        } else
            //TODO: do not go back - fill with fake data or show error message
            finish();
    }

    private void registerMessageEntityObserver() {
        mDetailViewModel.getLiveData().observe(this, new Observer<MessageEntity>() {
            @Override
            public void onChanged(@Nullable MessageEntity entity) {
                mDetailViewModel.bindData(entity);
            }
        });
    }

}
