package ru.tinted_knight.sberbanksms.dao.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "agents")
public class AgentEntity {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String defaultText;

    public int aliasId;

}
