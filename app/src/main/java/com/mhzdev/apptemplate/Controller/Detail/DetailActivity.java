package com.mhzdev.apptemplate.Controller.Detail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mhzdev.apptemplate.Controller.BaseActivity;
import com.mhzdev.apptemplate.Model.GenericModel;
import com.mhzdev.apptemplate.R;


public class DetailActivity extends BaseActivity {
    public static final String EXTRA_MODEL = "MODEL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        GenericModel model = (GenericModel) extras.getSerializable(EXTRA_MODEL);

        init(model);


    }

    private void init(GenericModel model) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (model != null) {
            toolbar.setTitle(model.getTitle());
        }
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Example Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
