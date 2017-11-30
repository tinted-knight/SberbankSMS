package ru.tinted_knight.sberbanksms.list_all;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.tinted_knight.sberbanksms.Tools.Constants;

@Entity
public class SimpleEntity {

    @PrimaryKey
    public int _id;

    public String agent;

    public float summa;

    public int year;

    public int month;

    public int day;

    public long date;

    public int type;

    public String getSummaString() {
        return String.format(Locale.getDefault(), "%,.2f", summa);
    }

    public String getMonthString() {
        return new SimpleDateFormat(Constants.shortDateFormatMonth, Locale.getDefault())
                .format(new Date(date * 1_000));
    }

    public String getDayString() {
        return new SimpleDateFormat(Constants.shortDateFormatDay, Locale.getDefault())
                .format(new Date(date * 1_000));
    }

}
