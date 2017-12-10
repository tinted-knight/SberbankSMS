package ru.tinted_knight.sberbanksms.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ru.tinted_knight.sberbanksms.dao.AppDatabase;
import ru.tinted_knight.sberbanksms.dao.query_pojos.MessageEntity;

public class DetailViewModel extends AndroidViewModel {

    private AppDatabase repo;

    private int itemId;

    public LiveData<MessageEntity> data;

    public DetailViewModel(@NonNull Application application, AppDatabase database, int itemId){
        super(application);
        this.repo = database;
        this.itemId = itemId;
        data = repo.daoMessages().getMessage(itemId);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{

        private final Application application;

        private final int itemId;

        private final AppDatabase database;

        public Factory(Application application, int itemId) {
            this.application = application;
            this.itemId = itemId;
            this.database = AppDatabase.getInstance(application);
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new DetailViewModel(application, database, itemId);
        }
    }

}
