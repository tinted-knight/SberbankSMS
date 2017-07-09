package ru.tinted_knight.sberbanksms.Tools.Loader;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.ArrayListFullMessageProcessor;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;

public class MessageListLoader extends AsyncTaskLoader<List<Message>> {

    private List<Message> mData;
    private boolean mCardFilter = false;
    private long mCard;

    public MessageListLoader(Context context) {
        super(context);
    }

    public MessageListLoader(Context context, long card) {
        super(context);
        mCardFilter = true;
        mCard = card;
    }

    @Override
    public List<Message> loadInBackground() {
//        Slog.log("MLL.loadInBackgroud()");
        Uri uri;
        if (mCardFilter) {
            uri = ContentUris.withAppendedId(MessageContentProvider.UriSmsByCard, mCard);
        } else {
            uri = MessageContentProvider.UriFullSms;
        }
        Cursor cursor = getContext().getContentResolver()
                .query(uri, null, null, null, null);
        if (cursor != null) {
            mData = new ArrayListFullMessageProcessor(cursor, getContext()).process();
            cursor.close();
        }
        return mData;
    }


    @Override
    public void deliverResult(List<Message> data) {
//        Slog.log("MLL.deliverResult()");
        if (isReset()) {
            releaseResouces();
            return;
        }

        List<Message> oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseResouces();
        }
    }

    @Override
    protected void onStartLoading() {
//        Slog.log("MLL.onStartLoading()");
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
//        Slog.log("MLL.onStopLoading()");
        cancelLoad();
    }

    @Override
    protected void onReset() {
//        Slog.log("MLL.onReset()");
        onStopLoading();
        mData = null;
        mCardFilter = false;
    }

    private void releaseResouces() {
    }
}
