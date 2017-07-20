package ru.tinted_knight.sberbanksms.main_model;

import android.content.Context;

import java.util.ArrayList;

public interface ISimpleModel {

    interface OnProgressUpdateListener {
        void onProgressUpdate(int progress);
        void onProgressUpdate(String title, String message, int max);
        void onProgressDone();
    }

    int loadDeviceSms(Context context, OnProgressUpdateListener listener);

    int getActiveCardId();

    String getActiveCardName();

    ArrayList<String> getCardsList();

    void setActiveCard(int id);

//    List<Message> loadFromDatabase();

}
