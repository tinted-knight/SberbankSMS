package ru.tinted_knight.sberbanksms.detail_screen.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface MessageDao {

    @Query("select * from messages where _id = :id limit 1")
    LiveData<MessageEntity> getMessage(int id);

}
