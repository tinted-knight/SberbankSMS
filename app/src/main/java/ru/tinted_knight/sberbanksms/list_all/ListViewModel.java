package ru.tinted_knight.sberbanksms.list_all;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.tinted_knight.sberbanksms.dao.AppDatabase;

public class ListViewModel extends AndroidViewModel {

    private AppDatabase mDatabase;

    private LiveData<List<SimpleEntity>> mLiveData;

    public ListViewModel(@NonNull Application application) {
        super(application);
        mDatabase = AppDatabase.getInstance(application);
    }

    public void init(){
        mLiveData = mDatabase.dao().getAll();
    }

    public LiveData<List<SimpleEntity>> getData() {
        if (mLiveData != null)
            return mLiveData;
        else
            throw new NullPointerException("getData(): LiveData is null");
    }
}
