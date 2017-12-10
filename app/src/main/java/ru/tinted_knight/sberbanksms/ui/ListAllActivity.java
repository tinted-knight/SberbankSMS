package ru.tinted_knight.sberbanksms.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Settings.Settings;

public class ListAllActivity
        extends AppCompatActivity
        implements ListAllFragment.OnItemClickListener, DetailFragment.OnDetailInteraction {

    private static final int REQUEST_CODE_GET_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO maybe should show some loading stuff on slower devices
        initNormalView();
        checkPermissions();
    }

    private void initNormalView() {
        setContentView(R.layout.activity_list_all);
        initBottomBar();
        ListAllFragment fragment = new ListAllFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flMain, fragment, ListAllFragment.TAG).commit();
    }

    private void initNoPermissionsView() {
        setContentView(R.layout.activity_list_all_permission_denied);
        Button btnTryAgain = findViewById(R.id.btnTryAgain);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
    }

    private void checkPermissions() {
        int readSms = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_SMS);
        int receiveSms = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.RECEIVE_SMS);

        if (readSms != PackageManager.PERMISSION_GRANTED || receiveSms != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                    REQUEST_CODE_GET_PERMISSIONS
            );
        }
    }

    private void initBottomBar() {
        BottomNavigationView bbar = findViewById(R.id.bottomNavigationView);
        bbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bbSettings:
                        item.setChecked(true);
                        Intent intent = new Intent(ListAllActivity.this, Settings.class);
                        startActivity(intent);
                        break;
                    case R.id.bbCards:
                        break;
                    case R.id.bbFilter:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GET_PERMISSIONS) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                initNoPermissionsView();
            }
        }
    }

    @Override
    public void onListItemClick(int id) {
        DetailFragment detailFragment = DetailFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack("detail")
                .replace(R.id.flMain, detailFragment, DetailFragment.TAG).commit();
    }

    @Override
    public void onFragmentInteraction(String message) {

    }
}
