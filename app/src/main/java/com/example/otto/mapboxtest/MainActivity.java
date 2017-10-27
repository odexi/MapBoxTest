package com.example.otto.mapboxtest;

import android.content.Context;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/*import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.maps.MapView;*/
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.steerpath.sdk.common.SteerpathClient;
import com.steerpath.sdk.maps.OnMapReadyCallback;
import com.steerpath.sdk.maps.SteerpathAnnotation;
import com.steerpath.sdk.maps.SteerpathAnnotationOptions;
import com.steerpath.sdk.maps.SteerpathMap;
import com.steerpath.sdk.maps.SteerpathMapFragment;
import com.steerpath.sdk.maps.SteerpathMapView;
import com.steerpath.sdk.maps.internal.SteerpathIcon;
import com.steerpath.sdk.maps.internal.SteerpathMarkerViewOptions;
import com.steerpath.sdk.meta.MetaFeature;
import com.steerpath.sdk.telemetry.TelemetryConfig;

import java.util.ArrayList;
import java.util.List;
/*import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;*/



public class MainActivity extends AppCompatActivity implements SteerpathMapFragment.MapViewListener{

    private MapboxMap mbmap;
    private SteerpathMap map;
    private SteerpathMapView mapView;
    private List<SteerpathAnnotation> anns = new ArrayList<>();
    BaseMarkerOptions baseMarkerOptions;

    public static final String EXTRAS_HELPER_BUILDING = "com.steerpath.dev.helper.building";

    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZXMiOiJiYXNlOnI7anVoYW5pdGVzdF9zdGF0aWM6cjtqdWhhbml0ZXN0X2R5bmFtaWM6ciIsImp0aSI6ImUyZjY1Mjc0LTY4YTgtNGM0ZS04MGY5LTUzNThkOTBiNTRkNSIsInN1YiI6Imp1aGFuaXRlc3QifQ.gAOJ1h7q43p65H3pXMYsZ2EGYCoCGUTcNhB5aX1s8j4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mapboxMap.getInstance(this, "pk.eyJ1Ijoib2RleGkiLCJhIjoiY2o5NnJ4eG00MDdlMjJxcXNuOTJpYW5heiJ9.nsA86RhtKSpr503V5Y-zbQ");

       setContentView(R.layout.activity_main);


       /* mapView = (SteerpathMapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        // mapView.setStyleUrl(Style.MAPBOX_STREETS);
*/

        SteerpathClient.StartConfig config =  new SteerpathClient.StartConfig.Builder()
                // MANDATORY:
                .apiKey(apiKey)
                .monitor(true)

                .build();


        SteerpathClient.getInstance().start(this, config, new SteerpathClient.StartListener() {
            @Override
            public void onStarted() {
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_parent, SteerpathMapFragment.newInstance(), "steerpath-map-fragment").commit();
        }
    }

    @Override
    public void onMapViewReady(final SteerpathMapView mapView) {
        this.mapView = mapView;
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final SteerpathMap map) {

                // enable positioning
                map.setMyLocationEnabled(true);

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(38.75909, -9.13687))
                        .zoom(18)
                        //.tilt(40)
                        .build();

                map.setCameraPosition(position);

                map.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        MetaFeature feature = mapView.getRenderedMetaFeature(point);

                        map.addAnnotation(new BaseMarkerOptions<Marker, >().position(point));

                    }
                });
            }
        });
    }

  /*  @Override
    public void onMapReady(MapboxMap map) {
        Log.d("MENNÄÄKS", "TÄNNE");
        mapboxMap = map;

        mapboxMap.setStyle(Style.SATELLITE);
        LatLng koulu = new LatLng(60.221171,24.8057408);
        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koulu,16));
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(60.221171,24.8057408)));

    }*/

}
