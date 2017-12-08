package ru.tinted_knight.sberbanksms.list_all;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import ru.tinted_knight.sberbanksms.Message.MessageReader.DeviceInboxCursorMessageReader;
import ru.tinted_knight.sberbanksms.Settings.Preferences;
import ru.tinted_knight.sberbanksms.dao.AppDatabase;
import ru.tinted_knight.sberbanksms.dao.ParseUtils;
import ru.tinted_knight.sberbanksms.dao.entities.AgentEntity;
import ru.tinted_knight.sberbanksms.dao.entities.FullMessageEntity;
import ru.tinted_knight.sberbanksms.dao.query_pojos.SimpleEntity;

public class ListAllViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;

    private IShowProgress listener;

    private LiveData<List<SimpleEntity>> liveData;

    public MutableLiveData<Integer> progress;

    public MutableLiveData<String> popupMessage;

    public ListAllViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application);
        liveData = appDatabase.daoMessages().getAll();
        progress = new MutableLiveData<>();
    }

    public void setProgressListener(IShowProgress listener) {
        this.listener = listener;
    }

    public LiveData<List<SimpleEntity>> getData() {
        liveData = appDatabase.daoMessages().getAll();
        if (liveData != null)
            return liveData;
        else
            throw new NullPointerException("== getData(): LiveData is null");
    }

    public void onCreate() {
        if (Preferences.isFirstRun2(getApplication())) {
            //TODO check permissions
            this.firstStart();
        }
    }

    private void firstStart() {
        Cursor cursor = new DeviceInboxCursorMessageReader(this.getApplication()).read();
        if (cursor == null || cursor.getCount() == 0) {
            // TODO show popupmessage
        } else {
            // TODO progress update listener
            listener.onProgressStart("Анализ СМС-сообщений", "Это займет немного времени...", cursor.getCount());
            AsyncSmsRoomWriter writer = new AsyncSmsRoomWriter();
            writer.execute(cursor);
        }
    }

    private void setFirstRunPref(boolean value) {
        Preferences.setFirstRun2(this.getApplication(), !value);
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
                List<FullMessageEntity> entityList = new LinkedList<>();
                do {
                    FullMessageEntity entity = ParseUtils.fromStringToEntity(cursor.getString(indexBody));
                    if (entity != null)
                        entityList.add(entity);
                    if (progress++ % 10 == 0) {
                        appDatabase.daoMessages().insertBatch(entityList.toArray(new FullMessageEntity[]{}));
                        entityList.clear();
                        ListAllViewModel.this.progress.postValue(progress);
                    }
                } while (cursor.moveToPrevious());
                if (entityList.size() > 0)
                    appDatabase.daoMessages().insertBatch(entityList.toArray(new FullMessageEntity[]{}));

                List<String> agents = appDatabase.daoMessages().getUniqueAgentsList();
                List<AgentEntity> entities = new LinkedList<>();
                for (String agentName : agents) {
                    AgentEntity e = new AgentEntity();
                    e.aliasId = -1;
                    e.defaultText = agentName;
                    entities.add(e);
                }
                appDatabase.daoAgents().insert(entities.toArray(new AgentEntity[]{}));
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            setFirstRunPref(aBoolean);
            //TODO maybe should check listener != null
            listener.onProgressHide();
        }
    }

    public interface IShowProgress {
        void onProgressStart(String title, String text, int max);
        void onProgressHide();
    }
}
