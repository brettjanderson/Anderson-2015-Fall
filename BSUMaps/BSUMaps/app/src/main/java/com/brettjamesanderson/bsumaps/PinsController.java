package com.brettjamesanderson.bsumaps;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Brettness on 2/26/15.
 */
public class PinsController {

    private static ArrayList<BsuMarker> savedMarkers = new ArrayList<BsuMarker>();
    private static boolean doneWithGettingSavedBuildings = false;

    public static synchronized void addMarker(BsuMarker marker){
        savedMarkers.add(marker);
    }

    public static synchronized ArrayList<BsuMarker> getAllSavedMarkers(){
        return savedMarkers;
    }

    private static void initializeSavedMarkers() {

        savedMarkers = new ArrayList<BsuMarker>();

        while(savedMarkers.isEmpty());

    }

    public static void setDoneWithGettingSavedBuildings(boolean done) {
        doneWithGettingSavedBuildings = done;
    }
}
