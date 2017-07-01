package ru.tinted_knight.sberbanksms.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;

public class Agents {

    private List<String> items;

    private Context context;

    public Agents(Context context, Cursor cursor) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    public Agents(Context context, int size) {
        this.items = new ArrayList<>(size);
        this.context = context;
    }

    public boolean add(String name) {
        return items.add(name);
    }

    public static void insert(Context context, String agent){
        ContentValues values = new ContentValues();
        values.put(DBHandler.Agents.DefaultText, agent);
        context.getContentResolver().insert(MessageContentProvider.UriAgents, values);
    }

    public void save() {
        for (String agent: items) {
            ContentValues values = new ContentValues();
            values.put(DBHandler.Agents.DefaultText, agent);
            context.getContentResolver().insert(MessageContentProvider.UriAgents, values);
        }
    }

    public static boolean isUnique(Context context, String agent){
        Cursor cursor = context.getContentResolver()
                .query(
                        MessageContentProvider.UriAgentByName,
                        new String[] { DBHandler.Agents.DefaultText },
                        DBHandler.Agents.DefaultText + " = ?",
                        new String[] { agent },
                        null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
