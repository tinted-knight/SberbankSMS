package ru.tinted_knight.sberbanksms.Message.MessageProcessor;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.HashMap;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.ItemProcessor;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;

public class MessageProcessor extends ItemProcessor<Message> {

    public MessageProcessor(Context context, Uri uri, Message m) {
        super(context, uri, m);
    }

    @Override
    public long save() {
        if (mItem.getType() != Constants.OperationType.DEBUG && mItem.getType() != Constants.OperationType.ERRORCODE) {
            return super.save();
        }
        return -1;
    }

    @Override
    protected ContentValues prepare(Message m) {
        ContentValues values = new ContentValues();
        values.put(DBHandler.MessagesTable.Card, m.getCard());
        values.put(DBHandler.MessagesTable.CardType, m.getCardType().toString());
        values.put(DBHandler.MessagesTable.Date, m.getDate());
        values.put(DBHandler.MessagesTable.Type, m.getType());
        values.put(DBHandler.MessagesTable.Agent, m.getAgent());
        values.put(DBHandler.MessagesTable.Summa, m.getSumma());
        values.put(DBHandler.MessagesTable.Balance, m.getBalance());

        HashMap<String, Integer> map = m.getDateSplit();
        values.put(DBHandler.MessagesTable.Year, map.get(Constants.SmsMapKeys.YEAR));
        values.put(DBHandler.MessagesTable.Month, map.get(Constants.SmsMapKeys.MONTH));
        values.put(DBHandler.MessagesTable.Day, map.get(Constants.SmsMapKeys.DAY));
        values.put(DBHandler.MessagesTable.Hour, map.get(Constants.SmsMapKeys.HOUR));
        values.put(DBHandler.MessagesTable.Minute, map.get(Constants.SmsMapKeys.MINUTE));

        return values;
    }

}
