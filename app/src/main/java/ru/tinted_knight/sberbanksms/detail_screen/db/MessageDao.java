package ru.tinted_knight.sberbanksms.detail_screen.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;

import static ru.tinted_knight.sberbanksms.Tools.DB.DBHandler.*;

@Dao
public interface MessageDao {

    @Query("select * from messages where _id = :id limit 1")
    MessageEntity getMessage(int id);

}
