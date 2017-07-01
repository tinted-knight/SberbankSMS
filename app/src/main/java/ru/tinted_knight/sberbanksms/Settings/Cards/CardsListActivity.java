package ru.tinted_knight.sberbanksms.Settings.Cards;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import ru.tinted_knight.sberbanksms.Message.Card;
import ru.tinted_knight.sberbanksms.Message.Cards;
import ru.tinted_knight.sberbanksms.R;

public class CardsListActivity
        extends AppCompatActivity
        implements CardsView, AdapterView.OnItemClickListener {

    private ListView lvMain;

    private CardsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_list);

        getSupportActionBar().setTitle("Список карт");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setOnItemClickListener(this);

        mPresenter = new SimpleCardPresenter(this, this);
    }

    @Override
    public void setItems(List<String> items) {
        lvMain.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }

    @Override
    public void showSnack(String text) {
        Snackbar.make(lvMain, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPresenter.onItemClicked(position);
    }
}
