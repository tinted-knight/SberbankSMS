package ru.tinted_knight.sberbanksms.Tools.DB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

// не нужен
public class AgentsContentProvider extends ContentProvider {

    private static DBHandler dbHandler;
//    private static SQLiteDatabase db;

    private static final String Content = "content://";
    private static final String Authority = "ru.tinted_knight.sberbanksms.agents";

    public static final String AgentsInitial = "initial";

    public static final Uri UriAgentsInitial = Uri.parse(Content + Authority + "/" + AgentsInitial);

    //types
    private static final String AgentsInitialType = "vnd.android.cursor.dir/vnd" + Authority + "." + AgentsInitial;

    //UriMatcher
    private static final int IdAgentsInitial = 0;
    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(Authority, AgentsInitial, IdAgentsInitial);
    }

    @Override
    public boolean onCreate() {
        dbHandler = new DBHandler(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        boolean distinct = false;
        String table = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        switch (mUriMatcher.match(uri)) {
            case IdAgentsInitial:
                table = DBHandler.MessagesTable.Table;
                projection = new String[]{DBHandler.MessagesTable.Id, DBHandler.MessagesTable.Agent};
                break;
        }

//        Cursor cursor = getReadableDatabase()
//                .query(distinct, table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
        Cursor cursor = dbHandler.getReadableDatabase()
                .query(distinct, table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        cursor.close();
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case IdAgentsInitial: return AgentsInitialType;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
