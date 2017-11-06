package com.example.otto.mapboxtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.steerpath.sdk.common.SteerpathClient;
import com.steerpath.sdk.maps.SteerpathMapFragment;
import com.steerpath.sdk.maps.SteerpathMapView;

/**
 * Created by Miikka on 11/6/2017.
 */

public class FragmentScanner extends Fragment implements SteerpathMapFragment.MapViewListener, View.OnClickListener{

    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZXMiOiJiYXNlOnI7anVoYW5pdGVzdF9zdGF0aWM6cjtqdWhhbml0ZXN0X2R5bmFtaWM6ciIsImp0aSI6ImUyZjY1Mjc0LTY4YTgtNGM0ZS04MGY5LTUzNThkOTBiNTRkNSIsInN1YiI6Imp1aGFuaXRlc3QifQ.gAOJ1h7q43p65H3pXMYsZ2EGYCoCGUTcNhB5aX1s8j4";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the fragment, "register" the UI components & sets OnClickListeners
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Beacon Scanner");
        final View v = inflater.inflate(R.layout.fragment_scanner, container, false);
        Button instMap = (Button) v.findViewById(R.id.installationMapBtn);
        instMap.setOnClickListener(this);


        SteerpathClient.StartConfig config =  new SteerpathClient.StartConfig.Builder()
                // MANDATORY:
                .apiKey(apiKey)
                .monitor(true)
                .build();

        SteerpathClient.getInstance().start(this.getActivity(), config, new SteerpathClient.StartListener() {
            @Override
            public void onStarted() {
            }
        });

        if (savedInstanceState == null) {
            this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.buildingView, SteerpathMapFragment.newInstance(), "steerpath-map-fragment").commit();
        }
        return v;
    }
    @Override
    public void onMapViewReady(SteerpathMapView steerpathMapView) {
        //TODO! Get Beacons from database and place them on to the map.
    }

    @Override
    public void onClick(View view) {
        //TODO! Implement a way back to the main actvity.
    }
}
