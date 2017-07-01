package ru.tinted_knight.sberbanksms.Message.MessageProcessor;

import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;

abstract class CursorMessageProcessor<T> {

    final Cursor cursor;

    final Context context;

    public CursorMessageProcessor(Cursor cursor, Context context){
        this.cursor = cursor;
        this.context = context;
    }

    public abstract T process();

}
