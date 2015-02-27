package com.brettjamesanderson.bsumaps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;


public class AddPinActivity extends ActionBarActivity {

    private Button takePictureButton;
    private Button savePinButton;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private ImageView pictureView;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new BsuMapsLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        takePictureButton = (Button) findViewById(R.id.takePictureButton);
        savePinButton = (Button) findViewById(R.id.savePinButton);
        nameEditText = (EditText) findViewById(R.id.nameEditField);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditField);
        pictureView = (ImageView) findViewById(R.id.pinPicture);

        if(savedInstanceState != null){
            nameEditText.setText(savedInstanceState.getString("name"));
            descriptionEditText.setText(savedInstanceState.getString("description"));
            pictureView.setImageBitmap((Bitmap)savedInstanceState.getParcelable("picture"));
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);
            }
        });

        savePinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                Bitmap picture = ((BitmapDrawable) pictureView.getDrawable()).getBitmap();
                LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                PinsController.addMarker(new BsuMarker(name,currentLatLng,description,picture));
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bitmap picture = ((BitmapDrawable)pictureView.getDrawable()).getBitmap();
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        outState.putParcelable("picture", picture);
        outState.putString("name", name);
        outState.putString("description", description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_pin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        Bitmap pinImage = null;

        if(extras != null)
            pinImage = (Bitmap) extras.get("data");

        if(pinImage != null)
            pictureView.setImageBitmap(pinImage);
    }

    public class BsuMapsLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            currentLocation = location;
        }

        @Override
        public void onProviderDisabled(String provider)
        {
        }

        @Override
        public void onProviderEnabled(String provider)
        {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }
    }
}
