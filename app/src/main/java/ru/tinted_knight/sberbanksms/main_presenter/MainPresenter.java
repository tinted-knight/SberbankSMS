package ru.tinted_knight.sberbanksms.main_presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.view.View;

import java.text.ParseException;
import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageReader.DeviceInboxCursorMessageReader;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.RecyclerView.MessagesRecyclerViewAdapter;
import ru.tinted_knight.sberbanksms.Settings.Preferences;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Constants.Flag;
import ru.tinted_knight.sberbanksms.Tools.Loader.MessageListLoader;
import ru.tinted_knight.sberbanksms.main_model.ISimpleModel;
import ru.tinted_knight.sberbanksms.main_model.SimpleModel;
import ru.tinted_knight.sberbanksms.main_view.IMainView;

public class MainPresenter implements IMainPresenter, ISimpleModel.OnProgressUpdateListener {

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

        mModel = new SimpleModel();

        if (firstRun()) {
            checkPermissions();
        }
        else {
            mView.initLoader();
        }
    }

    private boolean firstRun() {
        return Preferences.isFirstRun(mContext, "Main2");
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroy() {
        mView.unregisterBroadcastReceiver();
    }

    private void checkPermissions() {
        int readSms = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_SMS);
        int receiveSms = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECEIVE_SMS);

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
        int count = mModel.loadDeviceSms(mContext, this);
        mView.showProgress("title", "message", count);
    }

    @Override
    public Loader<List<Message>> getLoader(Bundle args) {
        if (args != null && args.getLong(Flag.CardFilter, -1) != -1) {
            return new MessageListLoader(mContext, args.getLong(Flag.CardFilter));
        }
        return new MessageListLoader(mContext);

    }

    @Override
    public void onLoaderReset() {
        mAdapter.swapCursor(null);
    }

    @Override
    public void showMessages(List<Message> data) {
        try {
            mAdapter = new MessagesRecyclerViewAdapter(mContext, data);
            initAdapterClickListeners();
            mView.showMessages(data, mAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    private void clearFilter() {
        mAdapter.clearFilter();
        mAdapter.notifyDataSetChanged();
        mFilterOperation = false;
        mFilterAgent = false;
        mView.scrollToFirst();
    }

    private void initAdapterClickListeners(){
        mAdapter.setOnItemClickListener(new MessagesRecyclerViewAdapter.OnItemClickListener() {
            //                viewAdapter.setOnItemClickListener(new MessagesRVAdapterMonths.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                if (mFilterOperation && mFilterAgent) {
                    mView.popupMessage("Posible filter combination alredy enabled. Clear before use another one.");
                    return;
                }
                switch (item.getId()) {
                    case R.id.ivOperationIcon:
                        int type = mAdapter.getType(position);
                        mAdapter.addTypeFilter(type);
                        mFilterOperation = true;
                        mView.scrollToFirst();
                        break;
                    case R.id.tvSumma:
                        mAdapter.addTypeFilter(mAdapter.getType(position));
                        mFilterOperation = true;
                        mView.scrollToFirst();
                        break;
                    case R.id.tvAgent:
                        String agent = mAdapter.getAgent(position);
                        mAdapter.addAgentFilter(agent);
                        mFilterAgent = true;
                        mView.scrollToFirst();
                        break;
                    case R.id.tvDate:
                        String smth = mAdapter.getDateFull(position);
                        mView.popupMessage(smth);
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
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
        mView.initLoader();
    }

}
