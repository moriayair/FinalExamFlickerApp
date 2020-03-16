package com.moria.finalexamflickerapp;

import android.util.Log;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    static final String FLICKER_QUERY = "FLICKER QUERY";
    static final String PHOTO_TRANSFER = "PHOTO TRANSFER";

    void activateToolbar(boolean isEnabledHome){
        Log.d(TAG, "activateToolbar: starts");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null){
            Toolbar toolbar = findViewById(R.id.toolbar);

            if (toolbar != null){
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(isEnabledHome);
        }
    }

}
