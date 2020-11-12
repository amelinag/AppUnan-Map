package com.example.appunan;

import android.content.Context;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private MapView _map; //creation de la map

    private Associations _associations;


    public Map(Associations associations, MapView map) {
        this._associations = associations;
        this._map = map;
    }


    public ArrayList<OverlayItem> displayItems(Context context) {
        List<Double[]> coordinates = _associations.getLocations(context);
        //System.out.println("\n\ncoordinates= " + coordinates );
        List<String> names =  _associations.getName();
        ArrayList<OverlayItem> items = new ArrayList<>();

        for (int i=0; i<coordinates.size();i++) {
                Double[] loc = coordinates.get(i);
                String n = names.get(i);
                GeoPoint point = new GeoPoint(loc[0], loc[1]);
                System.out.println("Latitude "+ loc[1]);
                System.out.println("Longitude "+ loc[1]);
                OverlayItem item = new OverlayItem(n, null, point);
                System.out.println("name "+ n);
                items.add(item);
        }
        return items;
    }


}
