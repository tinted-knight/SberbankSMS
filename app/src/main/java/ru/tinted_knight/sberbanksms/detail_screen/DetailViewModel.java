package ru.tinted_knight.sberbanksms.detail_screen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.dao.AppDatabase;
import ru.tinted_knight.sberbanksms.dao.query_pojos.MessageEntity;
import ru.tinted_knight.sberbanksms.databinding.ActivityDetailBinding;

public class DetailViewModel extends AndroidViewModel {

    private ActivityDetailBinding mBinding;
    private Long _id;
    private LiveData<MessageEntity> mLiveData;

    private AppDatabase mDatabase;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        mDatabase = AppDatabase.getInstance(application);
    }

    public void init(ActivityDetailBinding binding, long id) {
        mBinding = binding;
        _id = id;
    }

    public void loadData(){
        mLiveData = mDatabase.daoMessages().getMessage(_id.intValue());
    }

    public void bindData(MessageEntity entity) {
        mBinding.tvAgent.setText(entity.agent);
        mBinding.tvSumma.setText(entity.getSummaString());
        mBinding.tvBalance.setText(entity.getBalanceString());
        mBinding.tvCard.setText(entity.getCardSummary());
        mBinding.tvDate.setText(entity.getDateString());
        mBinding.tvTime.setText(entity.getTimeString());

        switch (entity.type) {
            case Constants.OperationType.INCOME:
                mBinding.tvSumma.setTextColor(ContextCompat.getColor(
                        this.getApplication().getApplicationContext(), R.color.summa_income));
                break;
            case Constants.OperationType.OUTCOME:
                mBinding.tvSumma.setTextColor(ContextCompat.getColor(
                        this.getApplication().getApplicationContext(), R.color.summa_expense));
                break;
            case Constants.OperationType.ATM_OUT:
                mBinding.tvSumma.setTextColor(ContextCompat.getColor(
                        this.getApplication().getApplicationContext(), R.color.summa_atm));
                break;
        }
    }

    public LiveData<MessageEntity> getLiveData() {
        return mLiveData;
    }

}
