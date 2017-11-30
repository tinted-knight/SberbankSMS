package ru.tinted_knight.sberbanksms.Tools.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.tinted_knight.sberbanksms.Tools.Slog;

public class DBHandler extends SQLiteOpenHelper{

    private static final String DbName = "BankSMS";
    public static final int Version = 2;

    public static final class CARDS {

        public static final String TABLE = "cards";
        public static final String ID = "_id";
        public static final String DEFAULT_NAME = "default_name";
        public static final String ALIAS = "alias";
        public static final String TYPE = "type"; // Visa or MasterCard
        static final String CREATE = "create table " + TABLE + "(" +
                ID + " integer primary key autoincrement, " +
                DEFAULT_NAME + " text, " +
                ALIAS + " text, " +
                TYPE + " integer);";

    }

    public static final class Agents {

        public static final String Table = "agents";
        public static final String Id = "_id";
        public static final String DefaultText = "default_text";
        public static final String AliasId = "alias";
        static final String Create = "create table " + Table + "(" +
                Id + " integer primary key autoincrement, " +
                DefaultText + " text, " +
                AliasId + " integer);";
    }

    public static final class AgentsAliases {

        public static final String Table = "agent_aliases";
        public static final String Id = "_id";
        public static final String Alias = "alias";
        static final String Create = "create table " + Table + "(" +
                Id + " integer primary key autoincrement, " +
                Alias + " text);";
    }

    public static final class MessagesTable {

        public static final String Table = "messages";
        public static final String Id = "_id";
        public static final String Card = "card";
        public static final String CardType = "cardType";
        public static final String Date = "date";
        public static final String Type = "type";
        public static final String Agent = "agent";
        public static final String Summa = "summa";
        public static final String Balance = "balance";
//        public static final String Raw = "raw";
        public static final String Year = "year";
        public static final String Month = "month";
        public static final String Day = "day";
        public static final String Hour = "hour";
        public static final String Minute = "minute";

        static final String Create = "create table " + Table + "(" +
                Id + " integer primary key autoincrement not null, " +
                Card + " text, " +
                CardType + " text, " +
                Date + " integer not null, " +
                Type + " integer not null, " +
                Agent + " text, " +
                Summa + " real not null, " +
                Balance + " real not null, " +
//                Raw + " text, " +
                Year + " integer not null, " +
                Month + " integer not null, " +
                Day + " integer not null, " +
                Hour + " integer not null, " +
                Minute + " integer not null);";

    }

    public static class RawTable {
        public static final String Table = "raw_messages";
        public static final String Id = "_id";
        public static final String Raw = "raw_body";
        public static final String MessageId = "mess_id";

        static final String Create = "create table " + Table + "(" +
                Id + " integer primary key autoincrement, " +
                Raw + " text, " +
                MessageId + " integer);";
    }

    public DBHandler(Context context) {
        super(context, DbName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MessagesTable.Create);
        Slog.log("dbHandler: " + MessagesTable.Table + " created");
        db.execSQL(RawTable.Create);
        Slog.log("dbHandler: " + RawTable.Table + " created");
        db.execSQL(Agents.Create);
        Slog.log("dbHandler: " + Agents.Table + " created");
        db.execSQL(AgentsAliases.Create);
        Slog.log("dbHandler: " + AgentsAliases.Table + " created");
        db.execSQL(CARDS.CREATE);
        Slog.log("dbHandler: " + CARDS.TABLE + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion == 1 && newVersion == Version) {
//            try {
//                Slog.log("try to upgrade db");
//                db.beginTransaction();
//                db.execSQL(Agents.Create);
//                Slog.log("dbHandler: " + Agents.Table + " created");
//                db.execSQL(AgentsAliases.Create);
//                Slog.log("dbHandler: " + AgentsAliases.Table + " created");
//                db.setTransactionSuccessful();
//            } finally {
//                db.endTransaction();
//            }
//        }
    }
}
