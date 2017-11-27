package ru.tinted_knight.sberbanksms.detail_screen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.Locale;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;
import ru.tinted_knight.sberbanksms.databinding.ActivityDetailBinding;

public class DetailViewModel extends AndroidViewModel {

    private ActivityDetailBinding mBinding;
    private long _id;
    private Cursor mCursor;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(ActivityDetailBinding binding, long id){
        mBinding = binding;
        _id = id;
    }

    private void loadDetails() {
        Uri uri = Uri.parse(MessageContentProvider.UriFullSms + "/" + _id);
        mCursor = getApplication().getApplicationContext().getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
    }

    public void bindData() {
        this.loadDetails();
        if (mCursor == null)
            return;
        mCursor.moveToFirst();

        mBinding.tvAgent.setText(
                mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.Agent)));
        mBinding.tvSumma.setText(String.format(Locale.getDefault(), "%,.2f",
                mCursor.getFloat(mCursor.getColumnIndex(DBHandler.MessagesTable.Summa))));
        mBinding.tvBalance.setText(String.format(Locale.getDefault(), "%,.2f",
                mCursor.getFloat(mCursor.getColumnIndex(DBHandler.MessagesTable.Balance))));
        String card = mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.CardType))
                + mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.Card));
        mBinding.tvCard.setText(card);
        String date = mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.Day)) + "."
                + mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.Month)) + "."
                + mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.Year));
        mBinding.tvDate.setText(date);
        String time = mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.Hour)) + ":"
                + mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.Minute));
        mBinding.tvTime.setText(time);

        int opType = mCursor.getInt(mCursor.getColumnIndex(DBHandler.MessagesTable.Type));
        switch (opType) {
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
