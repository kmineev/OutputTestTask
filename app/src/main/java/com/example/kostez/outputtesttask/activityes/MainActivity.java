package com.example.kostez.outputtesttask.activityes;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kostez.outputtesttask.R;
import com.example.kostez.outputtesttask.applications.MyApplication;
import com.example.kostez.outputtesttask.fragments.ListFragment;
import com.example.kostez.outputtesttask.fragments.MapFragment;
import com.example.kostez.outputtesttask.fragments.ScalingFragment;
import com.example.kostez.outputtesttask.fragments.ServiceFragment;

import static com.example.kostez.outputtesttask.adapters.ListRecyclerViewAdapter.LAST_POSITION;
import static com.example.kostez.outputtesttask.applications.MyApplication.getAppContext;
import static com.example.kostez.outputtesttask.applications.MyApplication.hasConnection;
import static com.example.kostez.outputtesttask.fragments.ListFragment.ADD_DIALOG_TAG;
import static com.example.kostez.outputtesttask.fragments.ListFragment.LIST_FRAGMENT_ID;
import static com.example.kostez.outputtesttask.fragments.ListFragment.LIST_FRAGMENT_TAG;
import static com.example.kostez.outputtesttask.fragments.MapFragment.MAP_FRAGMENT_ID;
import static com.example.kostez.outputtesttask.fragments.MapFragment.MAP_FRAGMENT_TAG;
import static com.example.kostez.outputtesttask.fragments.ScalingFragment.SCALING_FRAGMENT_ID;
import static com.example.kostez.outputtesttask.fragments.ScalingFragment.SCALING_FRAGMENT_TAG;
import static com.example.kostez.outputtesttask.fragments.ServiceFragment.DOWNLOAD_CODE;
import static com.example.kostez.outputtesttask.fragments.ServiceFragment.SERVICE_FRAGMENT_ID;
import static com.example.kostez.outputtesttask.fragments.ServiceFragment.SERVICE_FRAGMENT_TAG;
import static com.example.kostez.outputtesttask.fragments.SettingsFragment.LANGUAGE_IS_CHANGED;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Menu savedMenu;
    private boolean isList;
    private static final int REQUEST_SCALLING = 1;
    private static final int REQUEST_SERVICE = 2;
    private static final int REQUEST_MAP = 3;
    private int currentFragment;
    private static final String CURRENT_FRAGMENT = "current_fragment";
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("--- MA:onCreate");
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadLastFragment();
    }

    private void loadLastFragment() {
        int fragmentId = MyApplication.getPreferences().getInt(CURRENT_FRAGMENT, LIST_FRAGMENT_ID);
        switch (fragmentId) {
            case LIST_FRAGMENT_ID:
                navigationView.setCheckedItem(R.id.nav_list);
                swapToListFragment();
                break;
            case SCALING_FRAGMENT_ID:
                navigationView.setCheckedItem(R.id.nav_scaling);
                swapToScalingFragment();
                break;
            case SERVICE_FRAGMENT_ID:
                navigationView.setCheckedItem(R.id.nav_service);
                swapToServiceFragment();
                break;
            case MAP_FRAGMENT_ID:
                navigationView.setCheckedItem(R.id.nav_manage);
                swapToMapFragment();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        savedMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            ((ListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG)).showDialog(ADD_DIALOG_TAG, "", LAST_POSITION);
            return true;
        } else if (id == android.R.id.home) {
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        setBooleansToFalse();

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_list:
                swapToListFragment();
                break;
            case R.id.nav_scaling:
                swapToScalingFragment();
                break;
            case R.id.nav_service:
                swapToServiceFragment();

                break;
            case R.id.nav_manage:
                if (hasConnection(getAppContext())) {
                    swapToMapFragment();
                } else {
                    Toast.makeText(getAppContext(), getAppContext().getString(R.string.please_connect_to_internet), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_settings:

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        savedMenu.findItem(R.id.action_add).setVisible(isList);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isLangChanged = MyApplication.getPreferences().getBoolean(LANGUAGE_IS_CHANGED, false);

        if (isLangChanged) {
            SharedPreferences.Editor editor = MyApplication.getPreferences().edit();
            editor.putBoolean(LANGUAGE_IS_CHANGED, false);
            if (editor.commit()) {
                restartActivity();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_add).setVisible(isList);
        return true;
    }

    private void setBooleansToFalse() {
        isList = false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DOWNLOAD_CODE) {
            ((ServiceFragment) getFragmentManager().findFragmentByTag(SERVICE_FRAGMENT_TAG)).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_SCALLING:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startScallingFragment();
                } else {
                    //do nothing
                }
                break;
            case REQUEST_SERVICE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startServiceFragment();
                } else {
                    //do nothing
                }
                break;
            case REQUEST_MAP:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    startMapFragment();
                } else {
                    //do nothing
                }
                break;
        }
    }

    private void startScallingFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, new ScalingFragment(), SCALING_FRAGMENT_TAG)
                .commit();
    }

    private void startServiceFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, new ServiceFragment(), SERVICE_FRAGMENT_TAG)
                .commit();
    }


    private void startMapFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, new MapFragment(), MAP_FRAGMENT_TAG)
                .commit();
    }

    private void swapToListFragment() {

        currentFragment = LIST_FRAGMENT_ID;
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, new ListFragment(), LIST_FRAGMENT_TAG)
                .commit();
        isList = true;
    }

    private void swapToScalingFragment() {
        currentFragment = SCALING_FRAGMENT_ID;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_SCALLING);
        } else {
            startScallingFragment();
        }
    }

    private void swapToServiceFragment() {
        currentFragment = SERVICE_FRAGMENT_ID;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET
                            , Manifest.permission.ACCESS_NETWORK_STATE},
                    REQUEST_SERVICE);
        } else {
            startServiceFragment();
        }
    }

    private void swapToMapFragment() {
        currentFragment = MAP_FRAGMENT_ID;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_NETWORK_STATE
                            , Manifest.permission.INTERNET},
                    REQUEST_MAP);
        } else {
            startMapFragment();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = MyApplication.getPreferences().edit();
        editor.putInt(CURRENT_FRAGMENT, currentFragment);
        editor.apply();
    }
}
