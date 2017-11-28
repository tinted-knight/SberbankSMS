package ru.tinted_knight.sberbanksms.detail_screen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.databinding.ActivityDetailBinding;
import ru.tinted_knight.sberbanksms.detail_screen.db.AppDatabse;
import ru.tinted_knight.sberbanksms.detail_screen.db.MessageEntity;

public class DetailViewModel extends AndroidViewModel {

    private ActivityDetailBinding mBinding;
    private Long _id;

    private AppDatabse mDatabase;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        mDatabase = AppDatabse.getInstance(application);
    }

    public void init(ActivityDetailBinding binding, long id) {
        mBinding = binding;
        _id = id;
    }

    public void bindData() {
        MessageEntity entity = mDatabase.messageDao().getMessage(_id.intValue());
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

}
