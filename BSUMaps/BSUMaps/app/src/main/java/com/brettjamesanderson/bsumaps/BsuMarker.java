package com.brettjamesanderson.bsumaps;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Brettness on 2/26/15.
 */
public class BsuMarker {

    private String name;
    private LatLng location;
    private String description;
    private Bitmap picture;

    public BsuMarker(String name, LatLng location, String description, Bitmap picture){
        this.name = name;
        this.location = location;
        this.description = description;
        this.picture = picture;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }
}
