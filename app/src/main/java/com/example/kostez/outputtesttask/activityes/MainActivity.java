package com.example.kostez.outputtesttask.activityes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kostez.outputtesttask.R;
import com.example.kostez.outputtesttask.fragments.ListFragment;

import static com.example.kostez.outputtesttask.fragments.ListFragment.LIST_FRAGMENT_TAG;
import static com.example.kostez.outputtesttask.fragments.ListFragment.ADD_DIALOG_TAG;
import static com.example.kostez.outputtesttask.adapters.ListRecyclerViewAdapter.LAST_POSITION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isActivityStartted = false;
    private Menu savedMenu;
    private boolean isList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, new ListFragment(), LIST_FRAGMENT_TAG)
                .commit();
        isList = true;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            ((ListFragment)getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG)).showDialog(ADD_DIALOG_TAG, "", LAST_POSITION);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        setBooleansToFalse();

        int id = item.getItemId();

        if (id == R.id.nav_list) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new ListFragment(), LIST_FRAGMENT_TAG)
                    .commit();
            isList = true;
        } else if (id == R.id.nav_scaling) {

        } else if (id == R.id.nav_service) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        savedMenu.findItem(R.id.action_add).setVisible(isList);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isActivityStartted)
            isActivityStartted = true;
        else
            restartActivity();
    }

    private void restartActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        System.out.println("--- onPrepareOptionsMenu");
        menu.findItem(R.id.action_add).setVisible(isList);
        return true;
    }

    private void setBooleansToFalse() {
        isList = false;
    }
}
