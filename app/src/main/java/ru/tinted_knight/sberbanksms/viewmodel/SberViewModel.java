package ru.tinted_knight.sberbanksms.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import ru.tinted_knight.sberbanksms.dao.AppDatabase;

public class SberViewModel extends AndroidViewModel {

    final AppDatabase repo = AppDatabase.getInstance(this.getApplication());

    public SberViewModel(@NonNull Application application) {
        super(application);
    }

}
