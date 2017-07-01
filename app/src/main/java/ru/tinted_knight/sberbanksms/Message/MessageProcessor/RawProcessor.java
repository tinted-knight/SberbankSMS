package ru.tinted_knight.sberbanksms.Message.MessageProcessor;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import ru.tinted_knight.sberbanksms.Message.MessageProcessor.ItemProcessor;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;


public class RawProcessor extends ItemProcessor<String> {

    private long mMessageId;

    public RawProcessor(Context context, Uri uri, String m) {
        super(context, uri, m);
    }

    public long save(long messageId) {
        mMessageId = messageId;
        return super.save();
    }

    @Override
    protected ContentValues prepare(String m) {
        ContentValues values = new ContentValues();
        values.put(DBHandler.RawTable.Raw, m);
        values.put(DBHandler.RawTable.MessageId, mMessageId);

        return values;
    }
}
