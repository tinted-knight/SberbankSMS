package ru.tinted_knight.sberbanksms.dao;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity;
import ru.tinted_knight.sberbanksms.dao.entities.AliasEntity;
import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;

@Database(entities = {FullMessageEntity.class, AgentEntity.class, AliasEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract MessageDao daoMessages();

    public abstract AgentDao daoAgents();

    public abstract AliasDao daoAlias();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "BankSMS")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build();
            return instance;
        }
        return instance;
    }

}
