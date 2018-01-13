package ru.tinted_knight.sberbanksms.dao.query_pojos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.tinted_knight.sberbanksms.Tools.Constants;

public class SimpleEntity {

    public int _id;

    public String agent;

    public String alias;

    public float summa;

    public int year;

    public int month;

    public int day;

    public long date;

    public int type;

    public int hour;

    public int minute;

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
