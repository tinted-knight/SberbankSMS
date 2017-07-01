package ru.tinted_knight.sberbanksms.Message.MessageReader;

import android.content.Context;
import android.database.Cursor;

public abstract class CursorMessageReader {

    protected Context context;

    public CursorMessageReader(Context context) {
        this.context = context;
    }

    public abstract Cursor read();

/*
    public abstract Cursor read(int operationType);

    public abstract Cursor read(String operationType);
*/
}
