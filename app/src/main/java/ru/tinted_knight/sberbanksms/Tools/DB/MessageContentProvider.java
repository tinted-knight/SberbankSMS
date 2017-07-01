package ru.tinted_knight.sberbanksms.Tools.DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MessageContentProvider extends ContentProvider {

    private static DBHandler dbHandler;
    private static SQLiteDatabase db;

    private static final String content = "content://";
    private static final String Authority = "ru.tinted_knight.sberbanksms.content";
    private static final String FullSms = "full_sms";
    private static final String SmsByCard = "sms_by_card";
    private static final String RawSms = "raw_sms";
    private static final String GetRawSms = "get_raw_sms";
    private static final String Agents = "agents";
    private static final String AgentByName = "agentbyname";
    private static final String Alias = "alias";
    private static final String AgentsByAlias = "agents_by_alias";
    private static final String AgentsInitial = "agents_initial";
    private static final String CardsInitial = "cards_initial";
    private static final String CardByName = "cards_by_name";
    private static final String CardsList = "cards_list";
    private static final String YearsList = "years_list";

    public static final Uri UriFullSms = Uri.parse(content + Authority + "/" + FullSms);
    public static final Uri UriSmsByCard = Uri.parse(content + Authority + "/" + SmsByCard);
    public static final Uri UriRawSms = Uri.parse(content + Authority + "/" + RawSms);
    public static final Uri UriGetRawSms = Uri.parse(content + Authority + "/" + GetRawSms);
    public static final Uri UriAgents = Uri.parse(content + Authority + "/" + Agents);
    public static final Uri UriAgentByName = Uri.parse(content + Authority + "/" + AgentByName);
    public static final Uri UriAlias = Uri.parse(content + Authority + "/" + Alias);
    public static final Uri UriAgentsByAlias = Uri.parse(content + Authority + "/" + AgentsByAlias);
    public static final Uri UriAgentsInitial = Uri.parse(content + Authority + "/" + AgentsInitial);
    public static final Uri UriCardsInitial = Uri.parse(content + Authority + "/" + CardsInitial);
    public static final Uri UriCardByName = Uri.parse(content + Authority + "/" + CardByName);
    public static final Uri UriCardsList = Uri.parse(content + Authority + "/" + CardsList);
    public static final Uri UriYearsList = Uri.parse(content + Authority + "/" + YearsList);

    // types
    public static final String FullSmsType = "vnd.android.cursor.dir/vnd" +
            Authority + "." + FullSms;
    public static final String RawSmsType = "vnd.android.cursor.dir/vnd" +
            Authority + "." + RawSms;
    public static final String AgentsType = "vnd.android.cursor.dir/vnd" +
            Authority + "." + Agents;
    public static final String AgentSingleType = "vnd.android.cursor.dir/vnd" +
            Authority + "." + Agents;
    public static final String AliasType = "vnd.android.cursor.dir/vnd" +
            Authority + "." + Alias;
    public static final String AgentByAliasType = "vnd.android.cursor.dir/vnd" +
            Authority + "." + AgentsByAlias;
    public static final String AgentsInitialType = "vnd.android.cursor.dir/vnd" +
            Authority + "." + AgentsByAlias;

    // UriMatcher
    private static final int FullSmsId = 1;
    private static final int AgentsId = 2;
    private static final int AgentSingleId = 3;
    private static final int AgentByNameId = 4;
    private static final int AliasId = 5;
    private static final int AliasSingleId = 6;
    private static final int AgentsByAliasId = 7;
    private static final int RawSmsId = 8;
    private static final int AgentsInitialId = 9;
    private static final int GetRawSmsId = 10;
    private static final int CardsInitialId = 11;
    private static final int CardByNameId = 12;
    private static final int SmsByCardId = 13;
    private static final int CardsListId = 14;
    private static final int YearsListId = 15;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Authority, FullSms, FullSmsId);
        uriMatcher.addURI(Authority, RawSms, RawSmsId);
        uriMatcher.addURI(Authority, GetRawSms + "/#", GetRawSmsId);
        uriMatcher.addURI(Authority, Agents, AgentsId);
        uriMatcher.addURI(Authority, Agents + "/#", AgentSingleId);
        uriMatcher.addURI(Authority, AgentByName, AgentByNameId);
        uriMatcher.addURI(Authority, Alias, AliasId);
        uriMatcher.addURI(Authority, Alias + "/#", AliasSingleId);
        uriMatcher.addURI(Authority, AgentsByAlias + "/#", AgentsByAliasId);
        uriMatcher.addURI(Authority, AgentsInitial, AgentsInitialId);
        uriMatcher.addURI(Authority, CardsInitial, CardsInitialId);
        uriMatcher.addURI(Authority, CardByName, CardByNameId);
        uriMatcher.addURI(Authority, SmsByCard + "/#", SmsByCardId);
        uriMatcher.addURI(Authority, CardsList, CardsListId);
        uriMatcher.addURI(Authority, YearsList, YearsListId);
    }
    // end UriMatcher

    @Override
    public boolean onCreate() {
        dbHandler = new DBHandler(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        boolean distinct = false;
        String table = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        // may never be used, declared here because of odd scope in switch
        // and used also as flag for "raw query"
        String query = null;

        switch (uriMatcher.match(uri)) {
            case FullSmsId:
                query = "select messages.*, agent_aliases.alias as 'alias' from agents, messages left join" +
                        " agent_aliases on agent_aliases._id = agents.alias" +
                        " where agents.default_text = messages.agent" +
                        " order by messages.year desc, messages.month desc, messages.day desc," +
                        " messages.hour desc, messages.minute desc";
                break;
            case SmsByCardId:
                String cardId = uri.getLastPathSegment();
                query = "select messages.*, agent_aliases.alias as 'alias' from cards, agents, messages left join" +
                        " agent_aliases on agent_aliases._id = agents.alias" +
                        " where agents.default_text = messages.agent" +
                        " and messages.card = cards.default_name and cards._id = " + cardId +
                        " order by messages.year desc, messages.month desc, messages.day desc," +
                        " messages.hour desc, messages.minute desc";
                break;
            case CardsListId:
                table = DBHandler.CARDS.TABLE;
                projection = new String[]{DBHandler.CARDS.ID, DBHandler.CARDS.DEFAULT_NAME};
                break;
            case GetRawSmsId:
//                query = "select _id, raw_body from raw_messages where mess_id = " + uri.getLastPathSegment();
                table = DBHandler.RawTable.Table;
                projection = new String[]{DBHandler.RawTable.Id, DBHandler.RawTable.Raw};
                selection = DBHandler.RawTable.MessageId + " = " + uri.getLastPathSegment();
                break;
            case AgentsId:
                table = DBHandler.Agents.Table;
                orderBy = DBHandler.Agents.DefaultText + " asc";
                distinct = true;
                break;
            case AgentSingleId:
                table = DBHandler.Agents.Table;
                selection = "_id = " + uri.getLastPathSegment();
                break;
            case AgentByNameId:
                table = DBHandler.Agents.Table;
                break;
            case AliasSingleId:
                table = DBHandler.AgentsAliases.Table;
                selection = "_id = " + uri.getLastPathSegment();
                break;
            case AliasId:
                table = DBHandler.AgentsAliases.Table;
                break;
            case AgentsByAliasId:
                query = "select agents._id as _id, agents.default_text from agents, agent_aliases" +
                        " where agents.alias = agent_aliases._id and agent_aliases._id = " + uri.getLastPathSegment();
                break;
            case AgentsInitialId:
                table = DBHandler.MessagesTable.Table;
                projection = new String[]{DBHandler.MessagesTable.Agent};
                distinct = true;
                break;
            case CardsInitialId:
                table = DBHandler.MessagesTable.Table;
                projection = new String[]{DBHandler.MessagesTable.Card};
                distinct = true;
                break;
            case CardByNameId:
                table = DBHandler.CARDS.TABLE;
                projection = new String[]{DBHandler.CARDS.DEFAULT_NAME};
                break;
            case YearsListId:
                table = DBHandler.MessagesTable.Table;
                projection = new String[]{DBHandler.MessagesTable.Year};
                distinct = true;
                break;
        }
        if (query != null) {
            db = dbHandler.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
//            Cursor cursor = getReadableDatabase().rawQuery(query, null);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } else {
            db = dbHandler.getReadableDatabase();
            Cursor cursor = db.query(distinct, table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
//            Cursor cursor = getReadableDatabase()
//                    .query(distinct, table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case FullSmsId:
                return FullSmsType;
            case AgentsId:
                return AgentsType;
            case AgentSingleId:
                return AgentSingleType;
            case AliasId:
                return AliasType;
            case AgentsByAliasId:
                return AgentByAliasType;
            case AgentsInitialId:
                return AgentsInitialType;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long rowId;
        db = dbHandler.getWritableDatabase();
        Uri rowUri = null;
        switch (uriMatcher.match(uri)) {
            case FullSmsId:
                rowId = db.insert(DBHandler.MessagesTable.Table, null, values);
//                rowId = getWritableDatabase().insert(DBHandler.MessagesTable.Table, null, values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(UriFullSms, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
            case RawSmsId:
                rowId = db.insert(DBHandler.RawTable.Table, null, values);
//                rowId = getWritableDatabase().insert(DBHandler.RawTable.Table, null, values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(UriRawSms, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
            case AgentsId:
                rowId = db.insert(DBHandler.Agents.Table, null, values);
//                rowId = getWritableDatabase().insert(DBHandler.Agents.Table, null, values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(uri, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
            case AliasId:
                rowId = db.insert(DBHandler.AgentsAliases.Table, null, values);
//                rowId = getWritableDatabase().insert(DBHandler.AgentsAliases.Table, null, values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(uri, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
            case CardsInitialId:
                rowId = db.insert(DBHandler.CARDS.TABLE, null, values);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(uri, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
        }
        return rowUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String table = null;
        switch (uriMatcher.match(uri)) {
            case AliasSingleId:
                table = DBHandler.AgentsAliases.Table;
                selection = "_id = " + uri.getLastPathSegment();
                break;
        }
        db = dbHandler.getWritableDatabase();
        int rowId = db.delete(table, selection, selectionArgs);
//        int rowId = getWritableDatabase().delete(table, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowId;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table = null;
        switch (uriMatcher.match(uri)) {
            case AgentsId:
                table = DBHandler.Agents.Table;
                break;
            case AgentSingleId:
                table = DBHandler.Agents.Table;
                String id = uri.getLastPathSegment();
                selection = "_id = " + id;
                break;
            case AliasSingleId:
                table = DBHandler.AgentsAliases.Table;
                id = uri.getLastPathSegment();
                selection = "_id = " + id;
                break;
        }

        db = dbHandler.getWritableDatabase();
        int rows = db.update(table, values, selection, null);
//        int rows = getWritableDatabase().update(table, values, selection, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }
}
