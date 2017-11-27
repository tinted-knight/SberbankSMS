package ru.tinted_knight.sberbanksms.detail_screen;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        long id = getIntent().getLongExtra("_id", -1);
        if (id != -1){
            DetailContract.IDetailViewModel detailViewModel = new DetailViewModel(getApplicationContext(), mBinding, id);
            detailViewModel.bindData();
        }
        else
            //TODO: do not go back - fill with fake data or show error message
            finish();
    }

}
