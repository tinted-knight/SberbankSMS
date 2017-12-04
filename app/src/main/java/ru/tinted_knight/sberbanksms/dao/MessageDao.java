package ru.tinted_knight.sberbanksms.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;
import ru.tinted_knight.sberbanksms.dao.query_pojos.MessageEntity;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;

@Dao
public interface MessageDao {

    @Query("select * from messages where _id = :id limit 1")
    LiveData<MessageEntity> getMessage(int id);

    @Query("select _id, agent, summa, year, month, day, date, type" +
            " from messages" +
            " order by year desc, month desc, day desc")
    LiveData<List<SimpleEntity>> getAll();

    @Insert
    void insertMessage(FullMessageEntity entity);
}
