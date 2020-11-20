package com.example.appunan;

import android.app.Activity;
import android.content.Context;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private MapView _map; //creation de la map

    public Associations _associations;
    private Settings _settings;

    String userCountry, userAddress;


    public Map(Associations associations, MapView map, Settings settings) {
        this._associations = associations;
        this._map = map;
        this._settings= settings;
    }


    public ArrayList<OverlayItem> displayItems(Context context, Activity activity) {
        List<Double[]> coordinates = _associations.getLocations(context);
        List<String> names =  _associations.getName();

        ArrayList<OverlayItem> items = new ArrayList<>();

        if (coordinates != null) {
            for (int i = 0; i < coordinates.size(); i++) {
                Double[] loc = coordinates.get(i);
                String n = names.get(i);
                GeoPoint point = new GeoPoint(loc[0], loc[1]);
                System.out.println("Latitude " + loc[1]);
                System.out.println("Longitude " + loc[1]);
                OverlayItem item = new OverlayItem(n, null, point);
                System.out.println("name " + n);
                items.add(item);
            }
        }

        return items;
    }








}
