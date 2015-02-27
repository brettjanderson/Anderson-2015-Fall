package com.brettjamesanderson.bsumaps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {
    private static boolean doneSetup = false;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng startPosition = new LatLng(43.60360, -116.20871);
    private static final String INITIAL_BUILDINGS_JSON_URL = "http://zstudiolabs.com";
    private Button addPinButton;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        addPinButton = (Button) findViewById(R.id.placeNewPinButton);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new BsuMapsLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        addPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPinIntent = new Intent(MapsActivity.this, AddPinActivity.class);
                startActivity(addPinIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        GetBuildingsTask getBuildingsTask = new GetBuildingsTask(INITIAL_BUILDINGS_JSON_URL);
        getBuildingsTask.execute("/labs/cs402/buildings.json");

        while(!doneSetup);

        ArrayList<BsuMarker> pins = PinsController.getAllSavedMarkers();

        for(BsuMarker marker: pins){
            BitmapDescriptor imageDrawable = null;
            String name = marker.getName();
            Bitmap image = marker.getPicture();
            String description = marker.getDescription();
            LatLng location = marker.getLocation();

            if(image != null)
                imageDrawable = BitmapDescriptorFactory.fromBitmap(image);

            if(imageDrawable != null)
                mMap.addMarker(new MarkerOptions().position(location).title(name).snippet(description).icon(imageDrawable));
            else
                mMap.addMarker(new MarkerOptions().position(location).title(name).snippet(description));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPosition, 13));
        mMap.setMyLocationEnabled(true);
    }

    public class GetBuildingsTask extends AsyncTask<String, Integer, Boolean> {
        public static final int TIMEOUT = 30000;
        String url;

        public GetBuildingsTask(String url) {
            super();
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String resourceURL = url + params[0];

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);

            HttpGet httpGet = new HttpGet(resourceURL);

            try {
                BasicResponseHandler responseHandler = new BasicResponseHandler();
                String response = httpClient.execute(httpGet, responseHandler);

                JSONArray buildingsArray = new JSONArray(response);
                JSONObject building;
                String name;
                JSONObject location;
                Float lat;
                Float lng;
                String description;

                for(int i = 0; i < buildingsArray.length(); i++) {
                    building = buildingsArray.getJSONObject(i);
                    location = building.getJSONObject("location");
                    lat = Float.parseFloat(location.getString("latitude"));
                    lng = Float.parseFloat(location.getString("longitude"));
                    name = building.getString("name");
                    description = building.getString("description");

                    PinsController.addMarker(new BsuMarker(name, new LatLng(lat, lng), description, null));
                }

                doneSetup = true;
            } catch (Exception exception) {
            }

            return true;
        }
    }

    public class BsuMapsLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }
    }
}
