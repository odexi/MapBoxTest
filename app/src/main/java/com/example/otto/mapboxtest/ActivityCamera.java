package com.example.otto.mapboxtest;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ActivityCamera extends AppCompatActivity implements FragmentCamera.OnPreviewSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, FragmentCamera.newInstance())
                    .commit();
        }
    }

    public void onPreviewSelected () {
        FragmentPreview preview = new FragmentPreview();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, preview );
        transaction.addToBackStack(null);
        transaction.commit();
    }
}