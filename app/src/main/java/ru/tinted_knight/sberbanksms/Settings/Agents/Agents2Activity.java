package ru.tinted_knight.sberbanksms.Settings.Agents;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants.Flag;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;
import ru.tinted_knight.sberbanksms.Tools.LoadersConst;

public class Agents2Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //todo: в правом верхнем углу кнопка сортировки, возможно с диалогом с вариантами сортировки
    //todo: показывать количество операций по агенту

    ListView lvMain;

    SimpleCursorAdapter cursorAdapter;

    String mSelection;

    Boolean mFilterFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents);

        getSupportActionBar().setTitle(R.string.agentsactivity_actionbar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // check if need filter
        mFilterFlag = getIntent().getBooleanExtra(Flag.AliasFilter, false);
        if (mFilterFlag) {
            long aliasId = getIntent().getLongExtra(Flag.AliasId, -1);
            mSelection = DBHandler.Agents.AliasId + " != " + String.valueOf(aliasId) + " or " + DBHandler.Agents.AliasId + " is null";
        }

        lvMain = (ListView) findViewById(R.id.lvMain);
        String[] from = new String[] { DBHandler.Agents.DefaultText };
        int[] to = new int[] { android.R.id.text1 };
        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
        lvMain.setAdapter(cursorAdapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mFilterFlag) {
                    Intent intent = new Intent();
                    intent.putExtra(Flag.AgentId, id);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getBaseContext(), AgentEditActivity.class);
                    intent.putExtra(Flag.AgentId, id);
                    startActivity(intent);
                }
            }
        });

        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = ((TextView) view).getText().toString();
//                Toast.makeText(Agents2Activity.this, text + " = " + String.valueOf(id), Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(lvMain, text, Snackbar.LENGTH_SHORT);
                snackbar.show();
                return true;
            }
        });

        getLoaderManager().initLoader(LoadersConst.AgentsLoader, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this, MessageContentProvider.UriAgents, null,
                mSelection, null, DBHandler.Agents.DefaultText + " asc"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

}
