package ru.tinted_knight.sberbanksms.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.tinted_knight.sberbanksms.dao.entities.AliasEntity;

@Dao
public interface AliasDao {

    @Query("select * from aliases")
    LiveData<List<AliasEntity>> getAll();

    @Query("select aliases.alias as alias from aliases, agents" +
            " where agents._id = :id and agents.aliasId = aliases._id")
    LiveData<String> getByAgentId(int id);

    @Query("select aliases._id as _id, aliases.alias as alias from aliases, agents" +
            " where agents._id = :id and agents.aliasId = aliases._id")
    AliasEntity getAliasEntityByAgentId(int id);

    @Insert
    long insert(AliasEntity entity);

    @Update()
    int update(AliasEntity entity);

}
