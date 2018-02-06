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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof FullMessageEntity){
            FullMessageEntity e = (FullMessageEntity) obj;
            if (e.card.equals(this.card) && e.cardType.equals(this.cardType) && e.date == this.date
                    && e.type == this.type && e.agent.equals(this.agent) && e.summa == this.summa
                    && e.balance == this.balance && e.year == this.year && e.month == this.month
                    && e.day == this.day && e.hour == this.hour && e.minute == this.minute)
                return true;
        }
        return false;
    }
}
