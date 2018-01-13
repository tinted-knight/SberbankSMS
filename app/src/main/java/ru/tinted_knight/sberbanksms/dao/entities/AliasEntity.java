package ru.tinted_knight.sberbanksms.dao.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "aliases")
public class AliasEntity {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String alias;

}
