package com.mhzdev.apptemplate.Controller.Home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.mhzdev.apptemplate.Controller.BaseActivity;
import com.mhzdev.apptemplate.Controller.Home.Fragment.Home.HomeFragment;
import com.mhzdev.apptemplate.Controller.Home.Fragment.List.ListFragment;
import com.mhzdev.apptemplate.Controller.Login.LoginActivity;
import com.mhzdev.apptemplate.Controller.Settings.SettingsActivity;
import com.mhzdev.apptemplate.Model.GenericModel;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Session.UserSessionManager;
import com.mhzdev.apptemplate.Utils.IntentUtils;

public class HomeActivity extends BaseActivity {

    //Tags
    @SuppressWarnings("unused")
    public static final String TAG = "HomeActivity";
    @SuppressWarnings("unused")
    private final String LOG_TAG = "HomeActivity";

    //State
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5);
        }
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);

        setUpNavDrawer();

        openListFragment();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            getSupportActionBar().setTitle(R.string.title_activity_home);
            return;
        }
        plainToCloseApp();
    }


    /**
     * Setup the navigation drawer
     */
    private void setUpNavDrawer() {
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar,
                R.string.empty, R.string.empty
        );

        drawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        //Already Nothing
                        break;
                    case R.id.navigation_item_settings:
                        Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);
                        break;
                    case R.id.navigation_item_logout:
                        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        UserSessionManager.getInstance(HomeActivity.this).logOut(HomeActivity.this);
                        finish();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }


    /**
     * Open list Fragment
     */
    private void openListFragment() {
        ListFragment listFragment = new ListFragment();
        listFragment.setListener(new ListFragment.ListFragmentListener() {
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment, ListFragment.TAG).commit();
        setTitle(R.string.title_activity_home);
    }

    /**
     * Open detail activity
     */
    private void openDetailActivity(GenericModel model) {

    }

    /**
     * Open home Fragment
     */
    private void openHomeFragment(){
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setListener(new HomeFragment.HomeFragmentListener() {
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment, HomeFragment.TAG).commit();
        setTitle(R.string.title_activity_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_github) {
            IntentUtils.openLink(this, "https://github.com/MhzDev/Android-App-Template");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}