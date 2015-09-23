package com.mhzdev.apptemplate.Controller.Settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mhzdev.apptemplate.R;
import com.mhzdev.apptemplate.Controller.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
