package ru.tinted_knight.sberbanksms.list_all;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.annotation.NonNull;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.MessageReader.DeviceInboxCursorMessageReader;
import ru.tinted_knight.sberbanksms.Settings.Preferences;
import ru.tinted_knight.sberbanksms.dao.AppDatabase;
import ru.tinted_knight.sberbanksms.dao.ParseUtils;
import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;

public class ListAllViewModel extends AndroidViewModel {

    private AppDatabase mDatabase;

    private IShowProgress listener;

    private LiveData<List<SimpleEntity>> mLiveData;

    public MutableLiveData<Boolean> mFirstRun;

    public MutableLiveData<Integer> mProgress;

    public ListAllViewModel(@NonNull Application application) {
        super(application);
        mDatabase = AppDatabase.getInstance(application);
        mLiveData = mDatabase.dao().getAll();
        mFirstRun = new MutableLiveData<>();
        mProgress = new MutableLiveData<>();
    }

    public void setProgressListener(IShowProgress listener) {
        this.listener = listener;
    }

    public LiveData<List<SimpleEntity>> getData() {
        mLiveData = mDatabase.dao().getAll();
        if (mLiveData != null)
            return mLiveData;
        else
            throw new NullPointerException("== getData(): LiveData is null");
    }

    public void onCreate() {
        if (Preferences.isFirstRun2(this.getApplication())) {
            //TODO check permissions
            this.firstStart();
        } else
            mFirstRun.setValue(false);
    }

    private void firstStart() {
        Cursor cursor = new DeviceInboxCursorMessageReader(this.getApplication()).read();
        if (cursor == null || cursor.getCount() == 0) {
            // TODO show popupmessage
        } else {
            // TODO progress update listener
//            mView.showProgress("title", "text", cursor.getCount());
            listener.onProgressStart("Анализ СМС-сообщений", "Это займет немного времени...", cursor.getCount());
            AsyncSmsRoomWriter writer = new AsyncSmsRoomWriter();
            writer.execute(cursor);
        }
    }

    private void setFirstRunPref(boolean value) {
        Preferences.setFirstRun2(this.getApplication(), !value);
        this.mFirstRun.setValue(value);
    }

    private class AsyncSmsRoomWriter extends AsyncTask<Cursor, Integer, Boolean> {

        private int progress = 0;

        @Override
        protected Boolean doInBackground(Cursor... cursors) {
            Cursor cursor = cursors[0];
            if (cursor.moveToLast()) {
                // TODO min api level 19
                int indexBody = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
//                int indexDate = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE);
                do {
                    FullMessageEntity entity = ParseUtils.fromStringToEntity(cursor.getString(indexBody));
                    if (entity != null)
                        mDatabase.dao().insertMessage(entity);
                    mProgress.postValue(progress++);
                } while (cursor.moveToPrevious());
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            setFirstRunPref(aBoolean);
            listener.onProgressHide();
        }
    }

    public interface IShowProgress {
        void onProgressStart(String title, String text, int max);
        void onProgressHide();
    }
}
