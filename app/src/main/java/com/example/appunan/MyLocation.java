package com.example.appunan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import org.osmdroid.util.GeoPoint;

public class MyLocation {

    Location gps_loc, network_loc, final_loc;
    LocationManager locationManager = null;
    private String fournisseur;
    double longitude;
    double latitude;

    public MyLocation() {
    }

    public GeoPoint getMyLocation(Activity activity, Context context) {
        GeoPoint myPoint;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return  null;
        }

        try {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
            myPoint = new GeoPoint(latitude, longitude); //Position de depart
            //myPoint = new GeoPoint(48.38547134399414, -4.5312371253967285);
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
            myPoint = new GeoPoint(latitude, longitude);
            //myPoint = new GeoPoint(48.38547134399414, -4.5312371253967285);
        }
        else {
            latitude = 0.0;
            longitude = 0.0;
            //myPoint = new GeoPoint(latitude, longitude);
            myPoint = new GeoPoint(48.38547134399414, -4.5312371253967285);
        }

        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        //myPoint = new GeoPoint(48.38547134399414, -4.5312371253967285);
        return myPoint;

    }
}
