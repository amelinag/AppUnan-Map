package com.example.appunan;

import android.content.Context;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map {
    private MapView _map; //creation de la map

    public Associations _associations;


    public Map(Associations associations, MapView map) {
        this._associations = associations;
        this._map = map;
    }


    public ArrayList<OverlayItem> displayItems(Context context) {
        Double[] a={48.3994098,-4.4981507};
        Double[] b={48.3842446,-4.5016007};
        Double[] c={48.4059796,-4.4752021};
        Double[] d={48.4212329,-4.4674397};
        List<Double[]> coordinates = new ArrayList<Double[]>();
        coordinates.add(a);
        coordinates.add(b);
        coordinates.add(c);
        coordinates.add(d);
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