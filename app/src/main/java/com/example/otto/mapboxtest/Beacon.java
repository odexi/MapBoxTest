package com.example.otto.mapboxtest;

import android.graphics.Bitmap;

import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by Otto on 3.11.2017.
 */

public class Beacon {
    private String namespace = "not set";
    private int major = 0;
    private int minor = 0;
    private int id = 0;
    private Bitmap image = null;
    private LatLng position;
    private double floor = 0;
    private double RSSI = 0;
    private double height = 0;
    private String color = "not set";
    private String surface = "not set";

    public Beacon(int id, int minor, LatLng position, double floor, double height) {
        this.id = id;
        this.minor = minor;
        this.position = position;
        this.floor = floor;
        this.height = height;
    }

    public int getId(){
        return id;
    }

    public String getNamespace() {
        return namespace;
    }

    public int getMinor() {
        return minor;
    }

    public LatLng getPosition() {
        return position;
    }

    public double getFloor() {
        return floor;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setPosition(LatLng position){
        this.position = position;
    }
}
