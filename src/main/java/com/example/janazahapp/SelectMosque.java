package com.example.janazahapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SelectMosque extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMarkerClickListener {

    private FusedLocationProviderClient clientLocation;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    private Button B_search;
    private ArrayList<Marker> mTripMarkers = new ArrayList<>();
    public static final int REQUEST_LOCATION_CODE = 99;
    private Marker marker;
    private double latitude = 0, longitude = 0;
    private int proximityRadius = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager lm = (LocationManager) SelectMosque.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        //we will see if location is enabled, if it isn't then we will
        //redirect the user to the location parameter so that he can enable it
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(SelectMosque.this)
                    .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            SelectMosque.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                            .setNegativeButton("no",null)
                            .show();
        }
               requestPermission();
        clientLocation=LocationServices.getFusedLocationProviderClient(this);
        //Toast.makeText(SelectMosque.this,"clientLocation:"+clientLocation.getLastLocation(),Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_select_mosque);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final EditText tf_location = (EditText) findViewById(R.id.TF_location);
        B_search=findViewById(R.id.B_search);
        ///////////
        //made it to click on enter to submit but its not working for now
        tf_location.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.KEYCODE_ENTER)){
                    onClick(B_search);
                }
                return false;
            }
        });
        ///////////
        ////////////
    }

    public boolean onMarkerClick(Marker marker){
        LatLng latLng=new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);


        double latitude;
        double longitude;
        latitude=latLng.latitude;
        longitude=latLng.longitude;
        //Toast.makeText(selectMosque.this,"latlng: "+latitude,Toast.LENGTH_SHORT).show();
        Intent returnIntent=new Intent();
        returnIntent.putExtra("latLng",latLng);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
        /*Intent intent=getIntent();
        intent.putExtra("latLng",latLng);
        setResult(RESULT_OK,intent);
        finish();*/
        return true;
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //we check if the location is enabled by the user
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        //DON'T FORGET TO ENABLE USB DEBUGGING IN THE PHONE -> see tutorial on youtube
        else{
            Toast.makeText(SelectMosque.this,"Get location permission not granted!",Toast.LENGTH_SHORT).show();
        }
        //for the next line, i had to implements OnMarkerClickListener method
        //otherwise it would have not worked
        mMap.setOnMarkerClickListener(this);
    }
    /*to remove the markers that are on the map
    public void removeMarker(){
        for(Marker marker: mTripMarkers){
            marker.remove();
        }
    }*/
    private String getUrl(double latitude,double longitude,String nearbyMosque){
        StringBuilder googleURL =new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location="+latitude+","+longitude);
        googleURL.append("&radius="+proximityRadius);
        googleURL.append("&sensor=true");
        googleURL.append("&type="+nearbyMosque);
        googleURL.append("&key="+"AIzaSyCDFWmCeMLYmJ50GOiAUA0HKFsxkSgwWeg");

        Log.d("SelectMosque","url= "+googleURL.toString());

        return googleURL.toString();
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }
    public void onClick(View v){
        String mosque="mosque";
        Object transferData[]=new Object[2];
        GetNearbyPlacesData getNearbyPlacesData=new GetNearbyPlacesData();

        if(v.getId() == R.id.B_search){
            mMap.clear();
            EditText tf_location = (EditText) findViewById(R.id.TF_location);
            String location=tf_location.getText().toString();
            List<Address> addressList = null;
            MarkerOptions mo=new MarkerOptions();
            if(!location.equals("")){
                Geocoder geocoder=new Geocoder(this);
                try {
                    addressList=geocoder.getFromLocationName(location, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //we put a marker on all the point that we find on the map that match the research
                for(int i=0;i<addressList.size();i++){
                    Address myAdress = addressList.get(i);
                    LatLng latLng=new LatLng(myAdress.getLatitude(),myAdress.getLongitude());
                    mo.position(latLng);
                    marker=mMap.addMarker(mo);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng) );
                }
            }
        }
        else if(v.getId() == R.id.mosqueBt){

            if(ActivityCompat.checkSelfPermission(SelectMosque.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                checkLocationPermission();
            }
            /*clientLocation.getLastLocation().addOnSuccessListener(SelectMosque.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        Toast.makeText(SelectMosque.this,"alztamrlachienne"+location,Toast.LENGTH_SHORT);
                    }
                }
            });*/
            Log.d("latLng","value "+latitude+" "+longitude);//we get the right location
            String url=getUrl( latitude, longitude,mosque);
            transferData[0]=mMap;
            transferData[1]=url;

            getNearbyPlacesData.execute(transferData);
        }

    }

    protected synchronized void buildGoogleApiClient(){
        //we build a google api client and then we connect it
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();

        lastLocation = location;

        //if a marker is setted in another place, we will remove it
        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("Current location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        currentLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest=new LocationRequest();//creaate the object

        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //to get curent location of user
        //first we check if the permission is granted(permission to get user's location
        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client,locationRequest,this);

    }

    public boolean checkLocationPermission(){
        //contextCompat allows us to see if we have been granted a permission (from google api).
        //so the if sees if we have access to the user's position
        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            //here we check if we have asked the user previously for the permission to get his location.
            //so if he has refused we enter into the else, if not then we enter the if statement
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

            }
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
