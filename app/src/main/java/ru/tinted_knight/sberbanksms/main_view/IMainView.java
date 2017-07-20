package ru.tinted_knight.sberbanksms.main_view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Message;

public interface IMainView {

    Context getContext();

    Activity getActivity();

    void initBottomBar();

    void initBroadcastReceiver();

    void unregisterBroadcastReceiver();

    void initLoader();

    void showItems(List<Message> data, RecyclerView.Adapter adapter);

    void showActiveCard(String cardName);

    void scrollToFirst();

    void popupMessage(String text);

    void showProgress(String title, String message, int max);

    void progressUpdate(int progress);

    void progressUpdate(String title, String message, int max);

    void hideProgress();
}
