package ru.tinted_knight.sberbanksms.Message.MessageReader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import ru.tinted_knight.sberbanksms.Tools.Constants;

public class DeviceInboxCursorMessageReader extends CursorMessageReader {

    public DeviceInboxCursorMessageReader(Context context) {
        super(context);
    }

    public Cursor read() {
        Uri uri = Uri.parse(Constants.DeviceSmsUri);
        String selection = Telephony.TextBasedSmsColumns.ADDRESS + " = '" + Constants.SBER_PHONE_NUMBER + "'";
        return context.getContentResolver().query(uri, null, selection, null, null);
    }

}
