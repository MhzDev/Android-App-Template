package com.mhzdev.apptemplate.Controller.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.mhzdev.apptemplate.Controller.BaseActivity;
import com.mhzdev.apptemplate.Controller.Login.Activity.LoginActivity;
import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Session.UserSessionManager;

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
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);

        openHomeFragment();
        setUpNavDrawer();
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
                    case R.id.navigation_item_2:
                        //Already Nothing
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
     * Open home Fragment
     */
    private void openHomeFragment(){
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setListener(new HomeFragmentListener());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment, HomeFragment.TAG).commit();
        setTitle(R.string.title_activity_home);
    }

    private class HomeFragmentListener implements HomeFragment.HomeFragmentListener {
    }
}