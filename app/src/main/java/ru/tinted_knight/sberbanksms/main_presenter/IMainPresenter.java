package ru.tinted_knight.sberbanksms.main_presenter;

import android.os.Bundle;
import android.support.v4.content.Loader;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Message;

public interface IMainPresenter {

    void onResume();

    void onCreate();

    void onDestroy();

    Loader<List<Message>> getLoader(Bundle args);

    void onLoaderReset();

    void showMessages(List<Message> data);

    boolean onBackPressed();

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
