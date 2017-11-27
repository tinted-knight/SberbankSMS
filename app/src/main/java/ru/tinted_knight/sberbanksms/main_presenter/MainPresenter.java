package ru.tinted_knight.sberbanksms.main_presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.View;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.RecyclerView.MessagesRecyclerViewAdapter;
import ru.tinted_knight.sberbanksms.Settings.Preferences;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Loader.MessageListLoader;
import ru.tinted_knight.sberbanksms.main_model.ISimpleModel;
import ru.tinted_knight.sberbanksms.main_model.SimpleModel;
import ru.tinted_knight.sberbanksms.main_view.IMainView;

public class MainPresenter implements
        IMainPresenter, ISimpleModel.OnProgressUpdateListener {

    private IMainView mView;
    private ISimpleModel mModel;

    private MessagesRecyclerViewAdapter mAdapter;

    private Context mContext;
    private boolean mFilterOperation;
    private boolean mFilterAgent;

    public MainPresenter(IMainView view) {
        mView = view;
        mContext = mView.getContext();
        mView.initBottomBar();
        mView.initBroadcastReceiver();

//        mModel = new SimpleModel(mContext);
    }

    private boolean firstRun() {
        return Preferences.isFirstRun(mContext, "Main2");
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onCreate() {
        if (firstRun()) {
            checkPermissions();
        } else {
            mModel = new SimpleModel(mContext);
            doAfterLoad();
//            mView.initLoader();
        }
    }

    private void doAfterLoad(){
        mView.initLoader();
    }

    @Override
    public void onDestroy() {
        mView.unregisterBroadcastReceiver();
    }

    private void checkPermissions() {
        int readSms = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_SMS);
        int receiveSms = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.RECEIVE_SMS);

        if (readSms != PackageManager.PERMISSION_GRANTED || receiveSms != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    mView.getActivity(), new String[]{Manifest.permission.READ_SMS},
                    Constants.RequestCodes.Permissions
            );
        } else {
            firstStart();
        }
    }

    private void firstStart() {
        mModel = new SimpleModel();
        int count = mModel.loadDeviceSms(mContext, this);
        if (count == -1) {
            mView.popupMessage("No SMS from 900 found");
            return;
        }
        mView.showProgress("Анализ СМС-сообщений", "Это займет немного времени...", count);
    }

    @Override
    public void showMessages(List<Message> data) {
        try {
            mAdapter = new MessagesRecyclerViewAdapter(mContext, data);
            initAdapterClickListeners();
            mView.showItems(data, mAdapter);
            mView.showActiveCard(mModel.getActiveCardName());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> getCardsList() {
        return mModel.getCardsList();
    }

    @Override
    public void setActiveCard(int id) {
        mModel.setActiveCard(id);
    }

    @Override
    public int getActiveCard() {
        return mModel.getActiveCardId();
    }

    @Override
    public boolean onBackPressed() {
        if (mFilterAgent || mFilterOperation) {
            clearFilter();
            mView.popupMessage("Filters cleared");
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.RequestCodes.Permissions
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            firstStart();
        }
    }

    private void clearFilter() {
        mAdapter.clearFilter();
        mAdapter.notifyDataSetChanged();
        mFilterOperation = false;
        mFilterAgent = false;
        mView.scrollToFirst();
    }

    private void initAdapterClickListeners() {
        mAdapter.setOnItemClickListener(new MessagesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View item, long position) {
                if (mFilterOperation && mFilterAgent) {
                    mView.popupMessage("Posible filter combination alredy enabled. Clear before use another one.");
                    return;
                }
                switch (item.getId()) {
                    case R.id.tvSumma:
                        mAdapter.addTypeFilter(mAdapter.getType((int) position));
                        mFilterOperation = true;
                        mView.scrollToFirst();
                        break;
                    case R.id.tvAgent:
                        String agent = mAdapter.getAgent((int) position);
                        mAdapter.addAgentFilter(agent);
                        mFilterAgent = true;
                        mView.scrollToFirst();
                        break;
                    case R.id.tvDate:
                        String smth = mAdapter.getDateFull((int) position);
                        mView.popupMessage(smth);
                        break;
                    case R.id.tvDay:
                        mView.showDetailActivity(position);
                        break;
                    case R.id.tvMonth:
                        mView.showDetailActivity(position);
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
//        mAdapter.setOnItemLongClickListener(new MessagesRecyclerViewAdapter.OnItemLongClickListener() {
//            @Override
//            public void onLongClick(Long id) {
//                mView.popupMessage("Item tag is: " + id);
//                mView.showDetailActivity(id);
//            }
//        });
    }

    @Override
    public void onProgressUpdate(int progress) {
        mView.progressUpdate(progress);
    }

    @Override
    public void onProgressUpdate(String title, String message, int max) {
        mView.progressUpdate(title, message, max);
    }

    @Override
    public void onProgressDone() {
        mView.hideProgress();
        doAfterLoad();
//        mView.initLoader();
//        mView.showActiveCard(mModel.getActiveCardName());
    }

    @Override
    public Loader<List<Message>> onCreateLoader(int id, Bundle args) {
        return new MessageListLoader(mContext, mModel.getActiveCardId());
    }

    @Override
    public void onLoadFinished(Loader<List<Message>> loader, List<Message> data) {
        this.showMessages(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Message>> loader) {
        mAdapter.swapCursor(null);
    }
}
