package ru.tinted_knight.sberbanksms.dao.query_pojos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MessageEntity {

    public int _id;

    public String card;

    public String cardType;

    public int date;

    public int type;

    public String agent;

    public float summa;

    public float balance;

    public int year;

    public int month;

    public int day;

    public int hour;

    public int minute;

    public String getSummaString(){
        return String.format(Locale.getDefault(), "%,.2f", summa);
    }

    public String getBalanceString(){
        return String.format(Locale.getDefault(), "%,.2f", balance);
    }

    public String getCardSummary(){
        return String.format(Locale.getDefault(), "%1$s%2$s", cardType, card);
    }

    public String getDateString(){
        Calendar c = new GregorianCalendar(year, month-1, day);
        return String.format(Locale.getDefault(), "%1$te %1$tb %1$tY", c);
    }

    public String getTimeString(){
        Date dateObject = new Date(date * 1_000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());

        sdf.applyPattern("HH");
        String hour = sdf.format(dateObject);
        sdf.applyPattern("mm");
        String minute = sdf.format(dateObject);

        return new StringBuilder()
                .append(hour).append(":")
                .append(minute)
                .toString();
    }
}
