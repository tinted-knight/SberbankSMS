package ru.tinted_knight.sberbanksms.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity;

@Dao
public interface AgentDao {

    @Query("select * from agents")
    LiveData<AgentEntity> getAll();

    @Insert
    void insert(AgentEntity... entities);
}
