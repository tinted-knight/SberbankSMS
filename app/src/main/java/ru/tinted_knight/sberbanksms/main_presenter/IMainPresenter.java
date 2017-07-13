package ru.tinted_knight.sberbanksms.main_presenter;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Message;

public interface IMainPresenter extends LoaderManager.LoaderCallbacks<List<Message>> {

    void onResume();

    void onCreate();

    void onDestroy();

    void showMessages(List<Message> data);

    ArrayList<String> getCardsList();

    void setActiveCard(int id);

    int getActiveCard();

    boolean onBackPressed();

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);

    @Override
    Loader<List<Message>> onCreateLoader(int id, Bundle args);

    @Override
    void onLoadFinished(Loader<List<Message>> loader, List<Message> data);

    @Override
    void onLoaderReset(Loader<List<Message>> loader);
}
