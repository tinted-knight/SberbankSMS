package ru.tinted_knight.sberbanksms.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity;

@Dao
public interface AgentDao {

    @Query("select * from agents where defaultText != ''")
    LiveData<List<AgentEntity>> getAll();

    @Query("select * from agents where _id = :id")
    LiveData<AgentEntity> getById(int id);

    @Query("select count(defaultText) from agents where defaultText like :name")
    int countByName(String name);

    @Insert
    void insert(AgentEntity... entities);

    @Query("update agents set aliasId = :aliasId where _id = :agentId")
    void update(int agentId, long aliasId);
}
