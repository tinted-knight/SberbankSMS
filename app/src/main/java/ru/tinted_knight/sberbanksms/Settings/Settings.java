package ru.tinted_knight.sberbanksms.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Settings.Agents.AgentsActivity;
import ru.tinted_knight.sberbanksms.Settings.Agents.AliasesActivity;
import ru.tinted_knight.sberbanksms.Settings.Cards.CardsListActivity;

public class Settings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvMain;

    private String[] list;

    private final static class AppMenuItems {
        public static final int Cards = 0;
        public static final int Agents = 1;
        public static final int Aliases = 2;
        public static final int Operations = 3;
        public static final int RawSms = 4;
        public static final int AppSettings = 5;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(R.string.settings_activity_actionbar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvMain = (ListView) findViewById(R.id.lvMain);
        list = new String[] { "Карты", "Агенты", "Псевдонимы", "Операции", "Исходные СМС", "Настройки приложения"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case AppMenuItems.Cards:
                startActivity(new Intent(this, CardsListActivity.class));
//                Toast.makeText(this, "cards", Toast.LENGTH_SHORT).show();
                break;
            case AppMenuItems.Agents:
                startActivity(new Intent(this, AgentsActivity.class));
                break;
            case AppMenuItems.Aliases:
                startActivity(new Intent(this, AliasesActivity.class));
//                Toast.makeText(this, "aliases", Toast.LENGTH_SHORT).show();
                break;
            case AppMenuItems.Operations:
                Toast.makeText(this, "operations", Toast.LENGTH_SHORT).show();
                break;
            case AppMenuItems.RawSms:
                Toast.makeText(this, "rawsms", Toast.LENGTH_SHORT).show();
                break;
            case AppMenuItems.AppSettings:
                Toast.makeText(this, "appsettings", Toast.LENGTH_SHORT).show();
                break;
        }
//        Toast.makeText(this, list[position], Toast.LENGTH_SHORT).show();
    }

}
