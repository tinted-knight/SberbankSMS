package ru.tinted_knight.sberbanksms.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashSet;
import java.util.Set;

import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;

public class Cards {

    public static void extractCards(Context context) {
        Uri uri = MessageContentProvider.UriCardsInitial;
        Set<String> cards = new HashSet<>();
        Cursor mCursor = context.getContentResolver().query(uri, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                cards.add(mCursor.getString(mCursor.getColumnIndex(DBHandler.MessagesTable.Card)));
            } while (mCursor.moveToNext());
            mCursor.close();
            save(context, cards);
        }
    }

    public static void save(Context context, Set<String> cards) {
        Uri uri = MessageContentProvider.UriCardsInitial;
        for (String card : cards) {
            ContentValues values = new ContentValues();
            values.put(DBHandler.CARDS.DEFAULT_NAME, card);
            context.getContentResolver().insert(uri, values);
        }
    }

    public static boolean isUnique(Context context, String card) {
        Cursor cursor = context.getContentResolver()
                .query(
                        MessageContentProvider.UriCardByName,
                        null,
                        DBHandler.CARDS.DEFAULT_NAME + " = ?",
                        new String[] { card },
                        null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        return true;
    }

    public static CardsList getCardsList(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(MessageContentProvider.UriCardsList, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            CardsList list = new CardsList();
            do {
                String card = cursor.getString(cursor.getColumnIndex(DBHandler.CARDS.DEFAULT_NAME));
                int id = cursor.getInt(cursor.getColumnIndex(DBHandler.CARDS.ID));
                list.add(new Card(card, id));
            } while (cursor.moveToNext());
            cursor.close();
            return list;
        }
        return null;
    }

}