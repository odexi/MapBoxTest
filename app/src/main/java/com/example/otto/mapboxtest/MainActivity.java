package com.example.otto.mapboxtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class MainActivity extends AppCompatActivity implements SteerpathMapFragment.MapViewListener, View.OnClickListener {

    private SteerpathMap map;
    private Marker marker;
    private Marker markerToRemove;
    Button removeMarker;
    private Button disableMarkerAdd;
    private Button markerMove;
    private Button picView;
    boolean addableMarkers = true;
    boolean editOn = false;
    boolean markerDraggable = false;
    boolean firstClick = false;
    private Marker markerToMove;
    boolean popupWindowActive = false;
    private String apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZXMiOiJiYXNlOnI7anVoYW5pdGVzdF9zdGF0aWM6cjtqdWhhbml0ZXN0X2R5bmFtaWM6ciIsImp0aSI6ImUyZjY1Mjc0LTY4YTgtNGM0ZS04MGY5LTUzNThkOTBiNTRkNSIsInN1YiI6Imp1aGFuaXRlc3QifQ.gAOJ1h7q43p65H3pXMYsZ2EGYCoCGUTcNhB5aX1s8j4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        disableMarkerAdd = (Button)findViewById(R.id.disableMarkerAdd);
        markerMove = (Button)findViewById(R.id.markerMove);


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

                mapView.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {


                        if(addableMarkers && !markerDraggable){
                            drawMarker(point, baseMarkerOptions);
                        }

                        else if(firstClick){
                            drawMarker(point, baseMarkerOptions);
                            markerToMove.remove();
                            addableMarkers = false;
                            firstClick = false;
                        }

                 mapView.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                     @Override
                     public boolean onMarkerClick(@NonNull Marker marker) {

                         if(!markerDraggable){
                             markerToRemove = marker;
                             showPopupWindow(markerToRemove);
                         }
                         else if(markerDraggable){
                             firstClick = true;
                             addableMarkers = true;
                             markerToMove = marker;
                             markerToMove.setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_local_bar_black_24dp));
                         }

                         return false;
                     }
                 });




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

                markerMove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!markerDraggable) {
                            markerDraggable = true;
                            markerMove.setText("Disable marker moving");
                        }
                        else{
                            markerDraggable = false;
                            markerMove.setText("Enable marker moving");

                        }

                    }
                });

            }
        });
    }

    private void drawMarker(LatLng point, BaseMarkerOptions baseMarkerOptions){
        if (addableMarkers) {
            marker = new Marker(baseMarkerOptions);
            marker.setPosition(point);
            marker.setIcon(IconFactory.getInstance(getApplicationContext()).fromResource(R.drawable.ic_accuracy_marker));

            SteerpathAnnotationOptions.Builder builder = new SteerpathAnnotationOptions.Builder();

            SteerpathAnnotationOptions steerpathAnnotationOptions = builder.userId("1").floor(0).withBaseMarkerOptions(baseMarkerOptions).build();

            map.addAnnotation(steerpathAnnotationOptions);
        }
    }

    private void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);
    }

    public void showPopupWindow(Marker marker) {
        this.marker = marker;

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, (ViewGroup) findViewById(R.id.popup));

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        popupWindow.setHeight(popupWindow.getHeight()-100);
        dimBehind(popupWindow);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //popupWindow.dismiss();
                return true;
            }
        });

        final Button removeMarker = (Button)popupView.findViewById(R.id.deleteMarker);
        final Button closeBtn = (Button)popupView.findViewById(R.id.closeButton);
        final Button editBtn = (Button)popupView.findViewById(R.id.editButton);
        final EditText editMinor = (EditText)popupView.findViewById(R.id.editMinor);
        final EditText editHeight = (EditText)popupView.findViewById(R.id.editHeight);
        final EditText editSurface = (EditText)popupView.findViewById(R.id.editSurface);
        final EditText editFloor = (EditText)popupView.findViewById(R.id.editFloor);
        final ImageView picView = (ImageView)popupView.findViewById(R.id.imageView);



        picView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityCamera.class);
                startActivity(intent);
            }
        });

        removeMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerToRemove.remove();
                popupWindow.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editOn) {
                    editBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_done_black_24dp));
                    editMinor.setEnabled(true);
                    editHeight.setEnabled(true);
                    editSurface.setEnabled(true);
                    editFloor.setEnabled(true);
                    editOn = true;
                }
                else{
                    editBtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit_black_24dp));
                    editMinor.setEnabled(false);
                    editHeight.setEnabled(false);
                    editSurface.setEnabled(false);
                    editFloor.setEnabled(false);
                    editOn = false;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {


    }
}



