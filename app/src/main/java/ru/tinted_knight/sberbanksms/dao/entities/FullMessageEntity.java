package ru.tinted_knight.sberbanksms.dao.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "messages")
public class FullMessageEntity {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String card;

    //    @TypeConverters(CardTypeConverter.class)
    public String cardType;

    public long date;

    public int type;

    public String agent;

    public float summa;

    public float balance;

    public int year;

    public int month;

    public int day;

    public int hour;

    public int minute;

}
