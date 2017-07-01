package ru.tinted_knight.sberbanksms.Message.MessageProcessor;

import android.content.Context;
import android.net.Uri;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.MessageProcessor;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.RawProcessor;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;

public class MessageListProcessor {

    private static final Uri MESSAGES_URI = MessageContentProvider.UriFullSms;
    private static final Uri RAW_URI = MessageContentProvider.UriRawSms;

    private List<MessageProcessor> mMessages;
    private List<RawProcessor> mRaws;
    private Context mContext;

    MessageListProcessor(Context context) {
        mMessages = new ArrayList<>();
        mRaws = new ArrayList<>();
        mContext = context;
    }

    public void add(String raw, long date) throws ParseException {
        Message m = new Message(raw, date);
        if (m.getType() != Constants.OperationType.DEBUG
                && m.getType() != Constants.OperationType.ERRORCODE) {
            mMessages.add(new MessageProcessor(mContext, MESSAGES_URI, m));
            mRaws.add(new RawProcessor(mContext, RAW_URI, raw));
        }
    }

    public void process() {
        for (int i = 0; i < mMessages.size(); i++) {
            long rowId = mMessages.get(i).save();
            if ( rowId != -1) {
                mRaws.get(i).save(rowId);
            }
        }
    }

}
