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
    public List<Integer> listOfNewIds = new ArrayList<>();

    String userCountry, userAddress;


    public Map(Associations associations, MapView map, Settings settings) {
        this._associations = associations;
        this._map = map;
        this._settings= settings;
    }


    public ArrayList<OverlayItem> displayItems(Context context, Activity activity, GeoPoint myLocation) {
        List<Double[]> coordinates = _associations.getLocations(context);
        List<String> names =  _associations.getName();
        System.out.println("name " +names );
        ArrayList<OverlayItem> items = new ArrayList<>();

        if (coordinates != null) {
            for (int i = 0; i < coordinates.size(); i++) {
                Double[] loc = coordinates.get(i);  //récupération de la liste des coordonnées

                GeoPoint point = new GeoPoint(loc[0], loc[1]); //création des points en fonction des coordonnées
                System.out.println("Latitude " + loc[1]);
                System.out.println("Longitude " + loc[1]);
                //System.out.println("name " + n);
                if(this._settings.checkRadius(myLocation, point))
                {
                    String n = names.get(i);  //récupération de la liste des noms
                    OverlayItem item = new OverlayItem(n, null, point); //création de chaque item
                    items.add(item);
                    Integer id =  _associations.getID(n);  //récupération de l'id en fonction du nom
                    listOfNewIds.add(id);
                    System.out.println("ids " +id );

                }

            }
        }
        System.out.println("listIds" + listOfNewIds);

        return items;
    }














}
