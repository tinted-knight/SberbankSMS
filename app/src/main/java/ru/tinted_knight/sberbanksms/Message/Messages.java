package ru.tinted_knight.sberbanksms.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Constants.SmsMapKeys;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;

import static ru.tinted_knight.sberbanksms.Tools.DB.DBHandler.MessagesTable;


public class Messages {

    public static LinkedList<Integer> getYears(Context context) {
        Uri uri = MessageContentProvider.UriYearsList;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            LinkedList<Integer> years = new LinkedList<>();
            do {
                years.add(cursor.getInt(cursor.getColumnIndex(MessagesTable.Year)));
            } while (cursor.moveToNext());
            return years;
        }
        return null;
    }

    public static Uri save(final Context context, final Message message) {
        if (message.getType() != Constants.OperationType.ERRORCODE) {
            Uri uri = MessageContentProvider.UriFullSms;
            ContentValues values = new ContentValues();
            values.put(MessagesTable.Card, message.getCard());
            values.put(MessagesTable.Date, message.getDate());
            values.put(MessagesTable.Type, message.getType());
            values.put(MessagesTable.Agent, message.getAgent());
            values.put(MessagesTable.Summa, message.getSumma());
            values.put(MessagesTable.Balance, message.getBalance());
//            values.put(MessagesTable.Raw, message.getRaw());

            HashMap<String, Integer> map = message.getDateSplit();
            values.put(MessagesTable.Year, map.get(SmsMapKeys.YEAR));
            values.put(MessagesTable.Month, map.get(SmsMapKeys.MONTH));
            values.put(MessagesTable.Day, map.get(SmsMapKeys.DAY));
            values.put(MessagesTable.Hour, map.get(SmsMapKeys.HOUR));
            values.put(MessagesTable.Minute, map.get(SmsMapKeys.MINUTE));
            return context.getContentResolver().insert(uri, values);
        }
//        else Slog.log("opType = " + message.getTypeString());
        return null;
    }

}