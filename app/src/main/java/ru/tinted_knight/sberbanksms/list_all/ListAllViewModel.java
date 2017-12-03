package ru.tinted_knight.sberbanksms.list_all;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.MessageReader.DeviceInboxCursorMessageReader;
import ru.tinted_knight.sberbanksms.dao.AppDatabase;
import ru.tinted_knight.sberbanksms.dao.ParseUtils;
import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;

public class ListAllViewModel extends AndroidViewModel {

    private AppDatabase mDatabase;

    private LiveData<List<SimpleEntity>> mLiveData;

    public ListAllViewModel(@NonNull Application application) {
        super(application);
        mDatabase = AppDatabase.getInstance(application);
    }

    public void init() {
        mLiveData = mDatabase.dao().getAll();
    }

    public LiveData<List<SimpleEntity>> getData() {
        if (mLiveData != null)
            return mLiveData;
        else
            throw new NullPointerException("getData(): LiveData is null");
    }

    public void firstStart() {
        Cursor cursor = new DeviceInboxCursorMessageReader(this.getApplication()).read();
        if (cursor == null || cursor.getCount() == 0) {
            // TODO show popupmessage
        }
        // TODO progress update listener
        AsyncSmsRoomWriter writer = new AsyncSmsRoomWriter();
        writer.execute(cursor);
    }

    private class AsyncSmsRoomWriter extends AsyncTask<Cursor, Integer, Void> {

        private int progress = 0;

        @Override
        protected Void doInBackground(Cursor... cursors) {
            Cursor cursor = cursors[0];
            if (cursor.moveToLast()) {
                // TODO min api level 19
                int indexBody = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
//                int indexDate = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE);
                do {
                    FullMessageEntity entity = ParseUtils.fromStringToEntity(cursor.getString(indexBody));
                    if (entity != null)
                        mDatabase.dao().insertMessage(entity);
//                    if (entity.fillFromString(cursor.getString(indexBody)))
//                        mDatabase.dao().insertMessage(entity);
                    progress++;
                    Log.d("TAGG", "progress: " + progress);
                } while (cursor.moveToPrevious());
            }
            return null;
        }

    }
}
