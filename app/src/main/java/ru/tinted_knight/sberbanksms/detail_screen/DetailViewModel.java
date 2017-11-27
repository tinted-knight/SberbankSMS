package ru.tinted_knight.sberbanksms.detail_screen;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Locale;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageBuilder;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;
import ru.tinted_knight.sberbanksms.databinding.ActivityDetailBinding;

import static ru.tinted_knight.sberbanksms.Tools.DB.DBHandler.*;

public class DetailViewModel implements DetailContract.IDetailViewModel {

    private ActivityDetailBinding mBinding;

    private Context mContext;

    private long _id;

    private Cursor mCursor;

    public DetailViewModel(Context context, ActivityDetailBinding b, long id) {
        mBinding = b;
        this._id = id;
        this.mContext = context;
        loadDetails();
    }

    private void loadDetails() {
        Uri uri = Uri.parse(MessageContentProvider.UriFullSms + "/" + _id);
        mCursor = mContext.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void bindData() {
        if (mCursor == null)
            return;
        mCursor.moveToFirst();

        mBinding.tvAgent.setText(
                mCursor.getString(mCursor.getColumnIndex(MessagesTable.Agent)));
        mBinding.tvSumma.setText(String.format(Locale.getDefault(), "%,.2f",
                mCursor.getFloat(mCursor.getColumnIndex(MessagesTable.Summa))));
        mBinding.tvBalance.setText(String.format(Locale.getDefault(), "%,.2f",
                mCursor.getFloat(mCursor.getColumnIndex(MessagesTable.Balance))));
        String card = mCursor.getString(mCursor.getColumnIndex(MessagesTable.CardType))
                + mCursor.getString(mCursor.getColumnIndex(MessagesTable.Card));
        mBinding.tvCard.setText(card);
        String date = mCursor.getString(mCursor.getColumnIndex(MessagesTable.Day)) + "."
                + mCursor.getString(mCursor.getColumnIndex(MessagesTable.Month)) + "."
                + mCursor.getString(mCursor.getColumnIndex(MessagesTable.Year));
        mBinding.tvDate.setText(date);
        String time = mCursor.getString(mCursor.getColumnIndex(MessagesTable.Hour)) + ":"
                + mCursor.getString(mCursor.getColumnIndex(MessagesTable.Minute));
        mBinding.tvTime.setText(time);
    }
}
