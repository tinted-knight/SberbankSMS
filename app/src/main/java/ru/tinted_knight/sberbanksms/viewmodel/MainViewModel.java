package ru.tinted_knight.sberbanksms.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
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

public class MainViewModel extends AndroidViewModel {

    private AppDatabase repo;

    private IShowProgress listener;

    private AsyncSmsRoomWriter writer;

    private int progressMax;

    public LiveData<List<SimpleEntity>> liveData;

    public MutableLiveData<Integer> progress;

    public MutableLiveData<String> popupMessage;

    public MainViewModel(@NonNull Application application, AppDatabase database, IShowProgress progressListener) {
        super(application);
        repo = database;
        listener = progressListener;

        liveData = repo.daoMessages().getAll();
        progress = new MutableLiveData<>();
        popupMessage = new MutableLiveData<>();
        onCreate();
//        if (Preferences.isFirstRun(getApplication())) {
//            //TODO check permissions
//            this.firstStart();
//        }
    }

//    public void setProgressListener(IShowProgress listener) {
//        this.listener = listener;
//    }

    private void getData() {
//        liveData = repo.daoMessages().getAll();
    }

    public void onCreate() {
        if (Preferences.isFirstRun(getApplication())) {
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
            progressMax = cursor.getCount();
            listener.onProgressStart("Анализ СМС-сообщений", "Это займет немного времени...", cursor.getCount());
//            AsyncSmsRoomWriter writer = new AsyncSmsRoomWriter();
            writer = new AsyncSmsRoomWriter();
            writer.execute(cursor);
        }
    }

    private void setFirstRunPref(boolean flag) {
        Preferences.setFirstRun(this.getApplication(), !flag);
        if (flag)
            getData();
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
                        repo.daoMessages().insertBatch(entityList.toArray(new FullMessageEntity[]{}));
                        entityList.clear();
                        MainViewModel.this.progress.postValue(progress);
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
                } while (cursor.moveToPrevious());
                if (entityList.size() > 0)
                    repo.daoMessages().insertBatch(entityList.toArray(new FullMessageEntity[]{}));

                List<String> agents = repo.daoMessages().getUniqueAgentsList();
                List<AgentEntity> entities = new LinkedList<>();
                for (String agentName : agents) {
                    AgentEntity e = new AgentEntity();
                    e.aliasId = -1;
                    e.defaultText = agentName;
                    entities.add(e);
                }
                repo.daoAgents().insert(entities.toArray(new AgentEntity[]{}));
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

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final Application application;

        private final AppDatabase database;

        private IShowProgress progressListener;

        public Factory(Application application, IShowProgress listener) {
            this.application = application;
            this.database = AppDatabase.getInstance(application);
            this.progressListener = listener;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainViewModel(application, database, progressListener);
        }
    }

    private class MainLifecycleObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume() {
//            Log.d("TAGG", "=========== LifeCycle: onResume");
        }
    }

    public MainLifecycleObserver lifecycleObserver = new MainLifecycleObserver();

}
