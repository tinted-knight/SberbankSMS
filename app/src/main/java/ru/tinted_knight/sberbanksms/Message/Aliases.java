package ru.tinted_knight.sberbanksms.Message;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.List;

import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;

/**
 * Для работы с алиасами, статика
 */

public class Aliases {

    private static final String mTable = DBHandler.AgentsAliases.Table;

    public static boolean insert(Context context, String alias) {
        return false;
    }

    /**
     *
     * @param context текущий контекст для вызова контент-провайдера
     * @param newAlias строка с новым значением псевдонима
     * @param id id псевдонима из таблицы псевдонимов
     * @return возвращает true если изменена хоть одна строка в базе данных, иначе false
     */
    public static boolean update(Context context, String newAlias, long id) {
        Uri uri = ContentUris.withAppendedId(MessageContentProvider.UriAlias, id);
        ContentValues values = new ContentValues();
        values.put(DBHandler.AgentsAliases.Alias, newAlias);
        return context.getContentResolver().update(uri, values, null, null) > 0;
    }

    /**
     *
     * @param context Контекст для контент провайдера
     * @param id
     */
    public static void delete(Context context, long id){
        Uri uri = ContentUris.withAppendedId(MessageContentProvider.UriAlias, id);
        context.getContentResolver().delete(uri, null, null);

        // clear agents table
        uri = MessageContentProvider.UriAgents;
        ContentValues values = new ContentValues();
        values.put(DBHandler.Agents.AliasId, 0);
        String selection = DBHandler.Agents.AliasId + " = " + String.valueOf(id);
        context.getContentResolver().update(uri, values, selection, null);
    }

    public static void addAgent(Context context, long agentId, long aliasId){
        Uri uri = ContentUris.withAppendedId(MessageContentProvider.UriAgents, agentId);
        ContentValues values = new ContentValues();
        values.put(DBHandler.Agents.AliasId, aliasId);
        context.getContentResolver().update(uri, values, null, null);
    }

    public static List<String> getAgentList() {
        return null;
    }

}
