package com.example.appunan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private MapView _map; //creation de la map

    public Associations _associations;
    private Settings _settings;
    Location gps_loc, network_loc, final_loc;
    double longitude;
    double latitude;
    String userCountry, userAddress;


    public Map(Associations associations, MapView map, Settings settings) {
        this._associations = associations;
        this._map = map;
        this._settings= settings;
    }


    public ArrayList<OverlayItem> displayItems(Context context, Activity activity) {
        List<Double[]> coordinates = _associations.getLocations(context);
        //System.out.println("\n\ncoordinates= " + coordinates );
        List<String> names =  _associations.getName();

        ArrayList<OverlayItem> items = new ArrayList<>();

        GeoPoint myPoint;   // récupérer ma localisation

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
            //myPoint= new GeoPoint(latitude,longitude);
            myPoint= new GeoPoint(48.38547134399414,-4.5312371253967285);
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
            //myPoint= new GeoPoint(latitude,longitude);
            myPoint= new GeoPoint(48.38547134399414,-4.5312371253967285);
        }
        else {
            latitude = 0.0;
            longitude = 0.0;
            //myPoint= new GeoPoint(latitude,longitude);
            myPoint= new GeoPoint(48.38547134399414,-4.5312371253967285);
        }

        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
        OverlayItem myItem = new OverlayItem("myLocation",null,  myPoint);

        if (coordinates != null) {
            for (int i = 0; i < coordinates.size(); i++) {
                Double[] loc = coordinates.get(i);
                String n = names.get(i);
                GeoPoint point = new GeoPoint(loc[0], loc[1]);
                System.out.println("Latitude " + loc[1]);
                System.out.println("Longitude " + loc[1]);
                OverlayItem item = new OverlayItem(n, null, point);
                System.out.println("name " + n);

                if( this._settings.checkRadius(myPoint, point)== true)
                {
                    items.add(item);
                }

            }
        }

        items.add(myItem);
        return items;
    }








}
