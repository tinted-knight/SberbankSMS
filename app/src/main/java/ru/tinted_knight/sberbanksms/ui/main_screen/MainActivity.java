package ru.tinted_knight.sberbanksms.ui.main_screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ru.tinted_knight.sberbanksms.R;
import ru.tinted_knight.sberbanksms.Settings.Settings;
import ru.tinted_knight.sberbanksms.ui.adapters.ListRecyclerViewAdapter;

public class MainActivity
        extends AppCompatActivity
        implements MainFragment.OnMainFragmentInteractionListener, DetailFragment.OnDetailInteraction {

    private static final int REQUEST_CODE_GET_PERMISSIONS = 100;

    private enum State {ALLOWED, DENIED, NONE}

    private State flagState = State.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_all);

        //TODO maybe should show some loading stuff on slower devices
        if (savedInstanceState == null)
            if (checkPermissions())
                initNormalView();
            else {
                getSupportFragmentManager().popBackStackImmediate();
            }
    }

    private void initNormalView() {
        setContentView(R.layout.activity_list_all);
        initBottomBar();
        MainFragment fragment = MainFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.flMain, fragment, MainFragment.TAG)
                .commit();
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

    private boolean checkPermissions() {
        int readSms = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_SMS);
        int receiveSms = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.RECEIVE_SMS);

        if (readSms == PackageManager.PERMISSION_GRANTED || receiveSms == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                REQUEST_CODE_GET_PERMISSIONS
        );
    }

    private void initBottomBar() {
        BottomNavigationView bbar = findViewById(R.id.bottomNavigationView);
        bbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bbSettings:
                        item.setChecked(true);
                        Intent intent = new Intent(MainActivity.this, Settings.class);
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

    private void setupActionBar(boolean value) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(value);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");

        // Don't make the call to super() on the saveInstanceState method
        // https://stackoverflow.com/questions/7575921/illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-wit

        // You should use commitAllowingStateLoss() instead of commit()

        // Bug in support package
        // http://code.google.com/p/android/issues/detail?id=19917
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GET_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                flagState = State.ALLOWED;
            } else {
                flagState = State.DENIED;
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        switch (flagState) {
            case ALLOWED:
                initNormalView();
                break;
            case DENIED:
                initNoPermissionsView();
                break;
            default:
                initNormalView();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            super.onBackPressed();
        return true;
    }

    @Override
    public void onListItemClick(int id, ListRecyclerViewAdapter.ViewHolder holder) {
        DetailFragment detailFragment = DetailFragment.newInstance(id);
        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.animator.slide_in_top, R.animator.fade_out)
                .addToBackStack("detail")
                .replace(R.id.flMain, detailFragment, DetailFragment.TAG)
                .commit();
        setupActionBar(true);

/*
        DetailFragment detailFragment = DetailFragment.newInstance(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
            Transition default_transition = TransitionInflater.from(this).inflateTransition(R.transition.default_transition);

            detailFragment.setSharedElementEnterTransition(default_transition);
            detailFragment.setEnterTransition(fade);

            Fragment listFragment = getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
            listFragment.setSharedElementReturnTransition(default_transition);
            detailFragment.setEnterTransition(fade);
        }
        getSupportFragmentManager().beginTransaction()
                .addSharedElement(holder.agent, "agent")
                .addSharedElement(holder.summa, "summa")
                .addToBackStack("detail")
                .replace(R.id.flMain, detailFragment, DetailFragment.TAG).commit();
        setupActionBar(true);
*/
    }

    @Override
    public void onListItemLongClick(int id) {
        Toast.makeText(this, "tag: " + String.valueOf(id), Toast.LENGTH_SHORT).show();
//        DetailFragment detailFragment = DetailFragment.newInstance(id, "none");
//        getSupportFragmentManager().beginTransaction()
//                .addToBackStack("detail")
//                .replace(R.id.flMain, detailFragment, DetailFragment.TAG).commit();
//        setupActionBar(true);
    }

    @Override
    public void onFragmentResume() {
        setupActionBar(false);
    }

    @Override
    public void onFragmentInteraction(String message) {

    }
}
