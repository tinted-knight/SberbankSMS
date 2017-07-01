package ru.tinted_knight.sberbanksms.Message.MessageProcessor;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public abstract class ItemProcessor<T> {

    protected T mItem;
    private Context mContext;
    private Uri mUri;

    ItemProcessor(Context context, Uri uri, T m) {
        mContext = context;
        mUri = uri;
        mItem = m;
    }

    public long save() {
        Uri rowUri = mContext.getContentResolver().insert(mUri, prepare(mItem));
        if (rowUri != null) {
            return Long.parseLong(rowUri.getLastPathSegment());
        }
        return -1;
    }

    protected abstract ContentValues prepare(T item);
}
