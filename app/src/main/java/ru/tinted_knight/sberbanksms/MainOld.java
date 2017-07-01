package ru.tinted_knight.sberbanksms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import ru.tinted_knight.sberbanksms.Dialogs.BundleConstants;
import ru.tinted_knight.sberbanksms.Dialogs.CardsListDialog;
import ru.tinted_knight.sberbanksms.Message.Agents;
import ru.tinted_knight.sberbanksms.Message.Cards;
import ru.tinted_knight.sberbanksms.Message.CardsList;
import ru.tinted_knight.sberbanksms.Message.Message;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.MessageProcessor;
import ru.tinted_knight.sberbanksms.Message.MessageProcessor.RawProcessor;
import ru.tinted_knight.sberbanksms.Message.MessageReader.DeviceInboxCursorMessageReader;
import ru.tinted_knight.sberbanksms.RecyclerView.DividerItemDecoration;
import ru.tinted_knight.sberbanksms.RecyclerView.MessagesRecyclerViewAdapter;
import ru.tinted_knight.sberbanksms.Settings.Preferences;
import ru.tinted_knight.sberbanksms.Settings.Settings;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Constants.Flag;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;
import ru.tinted_knight.sberbanksms.Tools.DebugTimer;
import ru.tinted_knight.sberbanksms.Tools.Loader.MessageListLoader;
import ru.tinted_knight.sberbanksms.Tools.LoadersConst;
import ru.tinted_knight.sberbanksms.Tools.RoboErrorReporter;

import static ru.tinted_knight.sberbanksms.Tools.Constants.BroadcastIncomeSms;

public class MainOld extends AppCompatActivity
        implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<List<Message>>,
        DialogInterface.OnClickListener {

    private DebugTimer timer = new DebugTimer();
//    private BottomBar bBar;
    BottomNavigationView bbar;
    private RecyclerView rvMain;
    private TextView tvDummy;
    private ProgressDialog progressDialog;
    private MessagesRecyclerViewAdapter viewAdapter;
//    private MessagesRVAdapterMonths viewAdapter;
    private BroadcastReceiver broadcastReceiver;
    private Boolean filterOperation = false;
    private Boolean filterAgent = false;
    private CardsList cardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoboErrorReporter.bindReporter(this);
        setContentView(R.layout.main_v3);
        timer.start();
        bottomBarInit();


        boolean firstRun = Preferences.isFirstRun(this, "MainOld");
//        Slog.log("FirstRun = " + firstRun);

        rvMain = (RecyclerView) findViewById(R.id.rvMain);
        tvDummy = (TextView) findViewById(R.id.tvDummy);
        tvDummy.setVisibility(View.INVISIBLE);

        if (firstRun) {
//            Slog.log("Start first run");
            rvMain.setVisibility(View.GONE);
            checkPermissions();
        } else {
            loadMessagesFromDB();
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra("status", -1);
                if (status == 1) {
                    getSupportLoaderManager().restartLoader(LoadersConst.MainLoader, null, MainOld.this);
                }
            }
        };

        rvMain.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 50) {
                    bbar.setVisibility(View.GONE);
                } else if(dy < -50) {
                    bbar.setVisibility(View.VISIBLE);
                }
            }
        });

        IntentFilter incomeSmsIntentFilter = new IntentFilter(BroadcastIncomeSms);
        registerReceiver(broadcastReceiver, incomeSmsIntentFilter);
    }

    private void bottomBarInit() {
        bbar = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bbSettings:
                        item.setChecked(true);
                        Intent intent = new Intent(MainOld.this, Settings.class);
                        startActivity(intent);
                        break;
                    case R.id.bbFilter:
                        item.setChecked(true);
                        CardsListDialog cardsListDialog = new CardsListDialog();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(BundleConstants.CARDS_LIST, cardsList.getStringArrayList());
                        cardsListDialog.setArguments(bundle);
                        cardsListDialog.show(getSupportFragmentManager(), "cards_list");
                        break;
                    case R.id.bbHome:
                        item.setChecked(true);
                        clearFilter();
                        break;
                }
                return false;
            }
        });

/*
        final BottomBar bBar = BottomBar.attach(this, savedInstanceState);
        bBar.setItems(R.menu.bottombar_menu);
        bBar.mapColorForTab(0, "#ff0000");
        bBar.mapColorForTab(1, "#00ff00");
        bBar.mapColorForTab(2, "#0000ff");
        bBar.selectTabAtPosition(1, false);
        bBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.bbSettings:
                        Intent intent = new Intent(MainOld.this, Settings.class);
                        startActivity(intent);
                        bBar.selectTabAtPosition(1, false);
                        break;
                    case R.id.bbFilter:
                        bBar.selectTabAtPosition(1, false);
                        CardsListDialog cardsListDialog = new CardsListDialog();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(BundleConstants.CARDS_LIST, cardsList.getStringArrayList());
                        cardsListDialog.setArguments(bundle);
                        cardsListDialog.show(getSupportFragmentManager(), "cards_list");
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.bbSettings:
                        onMenuTabSelected(menuItemId);
                        break;
                }
            }
        });
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void clearFilter() {
        viewAdapter.clearFilter();
        viewAdapter.notifyDataSetChanged();
        filterOperation = false;
        filterAgent = false;
        rvMain.scrollToPosition(0);
    }

    @Override
    public void onBackPressed() {
        if (filterAgent || filterOperation) {
            clearFilter();
            Toast.makeText(this, "Filters cleared", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    private void checkPermissions() {
        int readSms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);
        int receiveSms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if (readSms != PackageManager.PERMISSION_GRANTED || receiveSms != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.READ_SMS},
                    Constants.RequestCodes.Permissions
            );
        } else {
            firstStart();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //TODO: Запилить дисклеймер о том, что я только читаю сообщения от номера 900 и не отправляю
        if (requestCode == Constants.RequestCodes.Permissions
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            firstStart();
        }
    }

    private void firstStart() {
        // TODO: async
        timer.start();
        Cursor cursor = new DeviceInboxCursorMessageReader(this).read();

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(cursor.getCount());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

//        AsyncSmsParser parser = new AsyncSmsParser(this);
//        parser.execute(cursor);
        AsyncSmsSQLiteWriter parser = new AsyncSmsSQLiteWriter(this);
        parser.execute(cursor);
    }

    private void loadMessagesFromDB() {
//        Slog.log("loadMessagesFromDB()");
        cardsList = Cards.getCardsList(MainOld.this);
        cardsList.setActive(0);
        Bundle bundle = new Bundle();
        bundle.putLong(Flag.CardFilter, cardsList.getActiveId());
        getSupportLoaderManager().initLoader(LoadersConst.MainLoader, bundle, this);
    }

    @Override
    public AsyncTaskLoader<List<Message>> onCreateLoader(int id, Bundle args) {
//        Slog.log("onCreateLoader()");
        if (args != null && args.getLong(Flag.CardFilter, -1) != -1) {
            return new MessageListLoader(MainOld.this, args.getLong(Flag.CardFilter));
        }
        return new MessageListLoader(MainOld.this);
    }

    @Override
    public void onLoadFinished(Loader<List<Message>> loader, final List<Message> data) {
        try {
            if (viewAdapter == null && loader.getId() == LoadersConst.MainLoader) {
                viewAdapter = new MessagesRecyclerViewAdapter(this, data);
                viewAdapter.setOnItemClickListener(new MessagesRecyclerViewAdapter.OnItemClickListener() {
//                viewAdapter.setOnItemClickListener(new MessagesRVAdapterMonths.OnItemClickListener() {
                    @Override
                    public void onItemClick(View item, int position) {
                        if (filterOperation && filterAgent) {
                            Toast.makeText(MainOld.this, "Posible filter combination alredy enabled." +
                                    " Clear before use another one.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        switch (item.getId()) {
                            case R.id.ivOperationIcon:
                                int type = viewAdapter.getType(position);
                                viewAdapter.addTypeFilter(type);
                                filterOperation = true;
                                rvMain.scrollToPosition(0);
                                break;
                            case R.id.tvSumma:
                                viewAdapter.addTypeFilter(viewAdapter.getType(position));
                                filterOperation = true;
                                rvMain.scrollToPosition(0);
                                break;
                            case R.id.tvAgent:
                                String agent = viewAdapter.getAgent(position);
                                viewAdapter.addAgentFilter(agent);
                                filterAgent = true;
                                rvMain.scrollToPosition(0);
                                break;
                            case R.id.tvDate:
                                String smth = viewAdapter.getDateFull(position);
                                Toast.makeText(MainOld.this, smth, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        viewAdapter.notifyDataSetChanged();
                    }
                });
                viewAdapter.setOnItemLongClickListener(new MessagesRecyclerViewAdapter.OnItemLongClickListener() {
//                viewAdapter.setOnItemLongClickListener(new MessagesRVAdapterMonths.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View item, int position) {
                        switch (item.getId()) {
                            case R.id.ivOperationIcon:
                                Uri uri = ContentUris.withAppendedId(
                                        MessageContentProvider.UriGetRawSms,
                                        viewAdapter.get_id(position));
                                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                                String raw = "oops";
                                if (cursor != null && cursor.moveToFirst()) {
                                    raw = cursor.getString(cursor.getColumnIndex(DBHandler.RawTable.Raw));
                                    cursor.close();
                                }
                                Toast.makeText(MainOld.this, raw, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tvDate:
                                Toast.makeText(MainOld.this, "sorry :(", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                rvMain.setAdapter(viewAdapter);
                rvMain.setLayoutManager(new LinearLayoutManager(this));
                rvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
                rvMain.setVerticalScrollBarEnabled(true);
//                if (viewAdapter.mMessages == null) {
//                    // TODO: 04.01.17 в этом случае показать симпатичную заглушку вместо пустого экрана
//                    Toast.makeText(this, "no mMessages", Toast.LENGTH_SHORT).show();
//                    tvDummy.setVisibility(View.VISIBLE);
//                }
                if (BuildConfig.DEBUG) {
                    Toast.makeText(this, "load time: " + timer.inSeconds(), Toast.LENGTH_SHORT).show();
                }
            } else {
                viewAdapter.swapCursor(data);
            }
            getSupportActionBar()
                    .setTitle("Баланс :  "
                            + String.format(Locale.getDefault(), "%1$,.2f", viewAdapter.getCurrentBalance()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Message>> loader) {
//        Slog.log("onLoaderReset");
//        loader.reset();
        viewAdapter.swapCursor(null);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which != DialogInterface.BUTTON_NEGATIVE) {
            cardsList.setActive(which);
            Bundle bundle = new Bundle();
            bundle.putLong(Flag.CardFilter, cardsList.getActiveId());
            getSupportLoaderManager().restartLoader(LoadersConst.MainLoader, bundle, MainOld.this);
        }
    }

    private class AsyncSmsSQLiteWriter extends AsyncTask<Cursor, Integer, Void> {

        private Cursor cursor;
        private int progress = 1;
        private final Context context;

        AsyncSmsSQLiteWriter(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Cursor... params) {
            this.cursor = params[0];
            try {
                if (cursor.moveToLast()) {
                    int indexBody = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
                    int indexDate = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE);
                    do {
                        MessageProcessor messageProcessor =
                                new MessageProcessor(context, MessageContentProvider.UriFullSms,
                                        new Message(cursor.getString(indexBody), cursor.getLong(indexDate)));
                        long rowId = messageProcessor.save();
                        if (rowId != -1 && rowId != 0) {
                            RawProcessor rawProcessor =
                                    new RawProcessor(context, MessageContentProvider.UriRawSms,
                                            cursor.getString(indexBody));
                            rawProcessor.save(rowId);
                        }
                        publishProgress(progress++);
                    } while (cursor.moveToPrevious());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Немного терпения - это не долго");
            progressDialog.setMessage("Запись в базу данных...");
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.setProgress(progressDialog.getMax());
            progressDialog.setTitle("Урааа!");
//            progressDialog.setMessage("Готово! Нам понадобилось примерно " + timer.inSeconds() + " секунд.");
            Toast.makeText(context, "time: " + timer.inSeconds(), Toast.LENGTH_SHORT).show();
//            progressDialog.setCancelable(true);
//            progressDialog.show();
            Preferences.setFirtsRun(context, "MainOld", false);
            AsyncAgentListWriter agentListBuilder = new AsyncAgentListWriter(context);
            agentListBuilder.execute();
//            loadMessagesFromDB();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

    }

    private class AsyncAgentListWriter extends AsyncTask<Void, Integer, Void> {

        private Context context;
        private Cursor cursor;
        private int progress = 1;

        AsyncAgentListWriter(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cursor = getContentResolver().query(MessageContentProvider.UriAgentsInitial, null, null, null, null);

            progressDialog.setTitle("Чуточку терпения - это не долго");
            progressDialog.setMessage("Строим список агентов");
            progressDialog.setMax(cursor.getCount());
            progressDialog.setProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (cursor.moveToFirst()) {
                Agents agents = new Agents(context, cursor.getCount());
                do {
                    agents.add(cursor.getString(cursor.getColumnIndex(DBHandler.MessagesTable.Agent)));
//                    SystemClock.sleep(1000);
                    publishProgress(progress++);
                } while (cursor.moveToNext());
                agents.save();
            }
            cursor.close();
            Cards.extractCards(context);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.setProgress(progressDialog.getMax());
            progressDialog.setTitle("Урааа!");
            progressDialog.setMessage("Готово! Нам понадобилось примерно " + timer.inSeconds() + " секунд.");
            progressDialog.dismiss();
            progressDialog.setCancelable(true);
            loadMessagesFromDB();
            rvMain.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }
    }
}
