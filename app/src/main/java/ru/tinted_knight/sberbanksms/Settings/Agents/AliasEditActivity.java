package ru.tinted_knight.sberbanksms.Settings.Agents;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import ru.tinted_knight.sberbanksms.Message.Aliases;
import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Tools.Constants;
import ru.tinted_knight.sberbanksms.Tools.Constants.Flag;
import ru.tinted_knight.sberbanksms.Tools.DB.DBHandler;
import ru.tinted_knight.sberbanksms.Tools.DB.MessageContentProvider;
import ru.tinted_knight.sberbanksms.Tools.LoadersConst;

public class AliasEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter mCursorAdapter;

    long mAliasId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alias_edit);

        mAliasId = getIntent().getLongExtra("aliasId", 0);
        getSupportActionBar().setTitle("aliasId = " + String.valueOf(mAliasId));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Uri uri = ContentUris.withAppendedId(MessageContentProvider.UriAlias, mAliasId);
        String[] projection = new String[] {DBHandler.AgentsAliases.Alias};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String alias = cursor.getString(cursor.getColumnIndex(DBHandler.AgentsAliases.Alias));
            EditText etAlias = (EditText) findViewById(R.id.etAlias);
            etAlias.setText(alias);
        }

        String[] from = new String[] {DBHandler.Agents.DefaultText};
        int[] to = new int[] {android.R.id.text1};
        mCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(mCursorAdapter);
        lvMain.requestFocus();

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AliasEditActivity.this, Agents2Activity.class);
                intent.putExtra(Flag.AliasFilter, true);
                intent.putExtra(Flag.AliasId, mAliasId);
                startActivityForResult(intent, Constants.RequestCodes.AddAgent, null);
            }
        });

        getLoaderManager().initLoader(LoadersConst.AliasSingle, null, this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.RequestCodes.AddAgent) {
            Aliases.addAgent(this, data.getLongExtra(Flag.AgentId, -1), mAliasId);
            getLoaderManager().restartLoader(LoadersConst.AliasSingle, null, this);
//            Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "bad", Toast.LENGTH_SHORT).show();
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                ContentUris.withAppendedId(MessageContentProvider.UriAgentsByAlias, mAliasId),
                null, null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
