package ru.tinted_knight.sberbanksms.main_view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Cards;
import ru.tinted_knight.sberbanksms.Message.CardsList;
import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.RecyclerView.DividerItemDecoration;
import ru.tinted_knight.sberbanksms.Settings.Settings;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.LoadersConst;
import ru.tinted_knight.sberbanksms.main_presenter.IMainPresenter;
import ru.tinted_knight.sberbanksms.main_presenter.MainPresenter;

import static ru.tinted_knight.sberbanksms.Tools.Constants.BroadcastIncomeSms;

public class Main2 extends AppCompatActivity
        implements IMainView, LoaderManager.LoaderCallbacks<List<Message>> {

    BottomNavigationView bbar;
    CardsList mCardsList;
    RecyclerView rvMain;
    ProgressDialog progressDialog;

    IMainPresenter mPresenter;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_v3);

        rvMain = (RecyclerView) findViewById(R.id.rvMain);

        mPresenter = new MainPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initBottomBar() {
        bbar = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bbSettings:
                        item.setChecked(true);
                        Intent intent = new Intent(Main2.this, Settings.class);
                        startActivity(intent);
                        break;
                    case R.id.bbFilter:
//                        item.setChecked(true);
//                        CardsListDialog cardsListDialog = new CardsListDialog();
//                        Bundle bundle = new Bundle();
//                        bundle.putStringArrayList(BundleConstants.CARDS_LIST, cardsList.getStringArrayList());
//                        cardsListDialog.setArguments(bundle);
//                        cardsListDialog.show(getSupportFragmentManager(), "cards_list");
                        break;
                    case R.id.bbHome:
                        item.setChecked(true);
//                        clearFilter();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void initBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra("status", -1);
                if (status == 1) {
                    getSupportLoaderManager().restartLoader(LoadersConst.MainLoader, null, Main2.this);
                }
            }
        };
        IntentFilter incomeSmsIntentFilter = new IntentFilter(BroadcastIncomeSms);
        registerReceiver(mBroadcastReceiver, incomeSmsIntentFilter);
    }

    @Override
    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void initLoader() {
        mCardsList = Cards.getCardsList(this);
        mCardsList.setActive(0);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.Flag.CardFilter, mCardsList.getActiveId());
        getSupportLoaderManager().initLoader(LoadersConst.MainLoader, bundle, this);
    }

    @Override
    public void showMessages(List<Message> data, RecyclerView.Adapter adapter) {
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rvMain.setVerticalScrollBarEnabled(true);
    }

    @Override
    public void scrollToFirst() {
        rvMain.scrollToPosition(0);
    }

    @Override
    public void popupMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void showProgress(String title, String message, int max) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(max);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    @Override
    public void progressUpdate(int progress) {
        progressDialog.setProgress(progress);
    }

    @Override
    public void progressUpdate(String title, String message, int max) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setMax(max);
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public Loader<List<Message>> onCreateLoader(int id, Bundle args) {
        return mPresenter.getLoader(args);
    }

    @Override
    public void onLoadFinished(Loader<List<Message>> loader, List<Message> data) {
        mPresenter.showMessages(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Message>> loader) {
        mPresenter.onLoaderReset();
    }

    @Override
    public void onBackPressed() {
        if (!mPresenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

}
