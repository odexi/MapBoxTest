package com.example.otto.mapboxtest;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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

import static android.view.View.VISIBLE;


public class MainActivity extends AppCompatActivity implements SteerpathMapFragment.MapViewListener{

    private SteerpathMap map;
    private Marker marker;
    private Marker markerToRemove;
    Button removeMarker;
    private Button disableMarkerAdd;
    boolean addableMarkers = true;
    boolean popupWindowActive = false;
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZXMiOiJiYXNlOnI7anVoYW5pdGVzdF9zdGF0aWM6cjtqdWhhbml0ZXN0X2R5bmFtaWM6ciIsImp0aSI6ImUyZjY1Mjc0LTY4YTgtNGM0ZS04MGY5LTUzNThkOTBiNTRkNSIsInN1YiI6Imp1aGFuaXRlc3QifQ.gAOJ1h7q43p65H3pXMYsZ2EGYCoCGUTcNhB5aX1s8j4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        disableMarkerAdd = (Button)findViewById(R.id.disableMarkerAdd);

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
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final SteerpathMap smap) {
                map = smap;
                // enable positioning
                map.setMyLocationEnabled(true);

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(38.75909, -9.13687))
                        .zoom(18)
                        //.tilt(40)
                        .build();

                map.setCameraPosition(position);

                mapView.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        final BaseMarkerOptions baseMarkerOptions = new BaseMarkerOptions() {
                            @Override
                            public BaseMarkerOptions getThis() {
                                return this;
                            }

                            @Override
                            public Marker getMarker() {
                                return marker;
                            }

                            @Override
                            public int describeContents() {
                                return 0;
                            }

                            @Override
                            public void writeToParcel(Parcel dest, int flags) {

                            }
                        };

                        if (addableMarkers) {
                            marker = new Marker(baseMarkerOptions);
                            marker.setPosition(point);
                            marker.setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_accuracy_marker));

                            SteerpathAnnotationOptions.Builder builder = new SteerpathAnnotationOptions.Builder();

                            SteerpathAnnotationOptions steerpathAnnotationOptions = builder.userId("1").floor(0).withBaseMarkerOptions(baseMarkerOptions).build();

                            map.addAnnotation(steerpathAnnotationOptions);
                        }

                    }
                });
                mapView.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        markerToRemove = marker;

                        showPopupWindow(markerToRemove);

                        return false;
                    }
                });





                disableMarkerAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addableMarkers) {
                            addableMarkers = false;
                            disableMarkerAdd.setText("Enable marker add");
                        } else {
                            addableMarkers = true;
                            disableMarkerAdd.setText("Disable marker add");
                        }
                    }
                });

            }
        });
    }

    public void showPopupWindow(Marker marker) {
        this.marker = marker;
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.fragment_parent);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        final Button removeMarker = (Button)popupView.findViewById(R.id.deleteMarker);

        removeMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerToRemove.remove();
                popupWindow.dismiss();
            }
        });

    }
}

