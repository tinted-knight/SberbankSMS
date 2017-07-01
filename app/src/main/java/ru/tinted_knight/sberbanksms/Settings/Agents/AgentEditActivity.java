package ru.tinted_knight.sberbanksms.Settings.Agents;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.tinted_knight.sberbanksms.Message.Aliases;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants.Flag;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;

public class AgentEditActivity extends AppCompatActivity  /* implements LoaderManager.LoaderCallbacks<Cursor> */{

    TextView tvAgentDefaultName;

    EditText etAlias;

    long agentId;

    long aliasId = -1;

    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editsingleagent);
        getSupportActionBar().setTitle("Агент");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        agentId = getIntent().getLongExtra(Flag.AgentId, 0);
        tvAgentDefaultName = (TextView) findViewById(R.id.tvAgentDefaultName);
        etAlias = (EditText) findViewById(R.id.etAlias);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
/*
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(DBHandler.AgentsAliases.Alias, etAlias.getText().toString());
                Uri uri = getContentResolver().insert(MessageContentProvider.UriAlias, values);
                int rowId = Integer.valueOf(uri.getLastPathSegment());

                if (rowId != -1) {
                    values = new ContentValues();
                    values.put(DBHandler.Agents.AliasId, rowId);
                    getContentResolver().update(
                            ContentUris.withAppendedId(MessageContentProvider.UriAgents, agentId),
                            values, null, null
                    );
                    finish();
                }
            }
        });
*/

        Uri uri = ContentUris.withAppendedId(MessageContentProvider.UriAgents, agentId);
        String[] projection = new String[] {
                DBHandler.Agents.DefaultText,
                DBHandler.Agents.AliasId,
        };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            String agentDefaultName = cursor.getString(cursor.getColumnIndex(DBHandler.Agents.DefaultText));
            String agentAlias = cursor.getString(cursor.getColumnIndex(DBHandler.AgentsAliases.Alias));
//            String agentAlias = cursor.getString(cursor.getColumnIndex(DBHandler.Agents.AliasId));
            tvAgentDefaultName.setText(agentDefaultName);
            if (agentAlias != null && !agentAlias.equals("0"))
                getAlias(agentAlias);
            getSupportActionBar().setTitle(agentDefaultName);
        }

        cursor.close();
    }

    /**
     * Расковыривает имя алиаса по известному id в таблице агентов
     * @param agentAlias id алиаса из таблицы агентов
     */
    private void getAlias(String agentAlias){
        Uri uri = ContentUris.withAppendedId(MessageContentProvider.UriAlias, Long.parseLong(agentAlias));
        String[] projection = new String[]{ DBHandler.AgentsAliases.Alias, DBHandler.AgentsAliases.Id };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            agentAlias = cursor.getString(cursor.getColumnIndex(DBHandler.AgentsAliases.Alias));
            etAlias.setText(agentAlias);
            flag = true;
            aliasId = cursor.getLong(cursor.getColumnIndex(DBHandler.AgentsAliases.Id));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.miSave:
                ContentValues values = new ContentValues();
                values.put(DBHandler.AgentsAliases.Alias, etAlias.getText().toString());
                if (!flag) {
                    Uri uri = getContentResolver().insert(MessageContentProvider.UriAlias, values);
                    int rowId = Integer.valueOf(uri.getLastPathSegment());

                    if (rowId != -1) {
                        values = new ContentValues();
                        values.put(DBHandler.Agents.AliasId, rowId);
                        getContentResolver().update(
                                ContentUris.withAppendedId(MessageContentProvider.UriAgents, agentId),
                                values, null, null
                        );
                    }
                }
                else {
                    Aliases.update(this, etAlias.getText().toString(), aliasId);
//                    Uri uri = ContentUris.withAppendedId(MessageContentProvider.UriAlias, aliasId);
//                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.agent_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ContentUris.withAppendedId(MessageContentProvider.UriAgents, agentId);
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        if (data.moveToFirst()) {
            tvAgentDefaultName.setText(data.getString(data.getColumnIndex(DBHandler.Agents.DefaultText)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
*/
}
