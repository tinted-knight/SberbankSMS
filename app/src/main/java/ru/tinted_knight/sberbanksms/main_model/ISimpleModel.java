package ru.tinted_knight.sberbanksms.main_model;

import android.content.Context;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Message;

public interface ISimpleModel {

    interface OnProgressUpdateListener {
        void onProgressUpdate(int progress);
        void onProgressUpdate(String title, String message, int max);
        void onProgressDone();
    }

    int loadDeviceSms(Context context, OnProgressUpdateListener listener);

    List<Message> loadFromDatabase();

}
