package com.example.otto.mapboxtest;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ActivityCamera extends AppCompatActivity implements FragmentCamera.OnPreviewSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_WRITE_PERMISSION = 1;
    private static final int REQUEST_READ_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
                return;
             }

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

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(
                   Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new FragmentCamera.ConfirmationDialog().show(getSupportFragmentManager(),
                       "dialog");
                } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                       REQUEST_WRITE_PERMISSION);
                }
        }
    }

    @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length != 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                FragmentCamera.ErrorDialog.newInstance("what")
                        .show(getSupportFragmentManager(), "dialog");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        }
    }
}