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

    @Query("select _id, agent, summa, year, month, day, date, type, hour, minute" +
            " from messages" +
            " order by year desc, month desc, day desc, hour desc, minute desc")
    LiveData<List<SimpleEntity>> getAll();

    @Query("select aliases.alias as 'alias', messages.* from agents, messages" +
            " left join aliases on aliases._id = agents.aliasId" +
            " where agents.defaultText = messages.agent" +
            " order by messages.year desc, messages.month desc, messages.day desc," +
            " messages.hour desc, messages.minute desc")
    LiveData<List<SimpleEntity>> getAllAlias();

    @Query("select distinct agent from messages")
    List<String> getUniqueAgentsList();

    @Insert
    void insertMessage(FullMessageEntity entity);

    @Insert
    void insertBatch(FullMessageEntity... entity);

}
