package com.example.appunan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity{
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

    public void setSettings(Settings settings) {
        this._settings = settings;
    }
    public Settings getSettings(){
        return this._settings;
    }



    public ArrayList<GeoPoint> getPoints(Context context) {
        List<Double[]> coordinates = _associations.getLocations(context);
        ArrayList<GeoPoint> pointsAssociations = new ArrayList<>();

        if (coordinates != null) {
            for (int i = 0; i < coordinates.size(); i++) {
                Double[] loc = coordinates.get(i);  //récupération de la liste des coordonnées

                GeoPoint point = new GeoPoint(loc[0], loc[1]); //création des points en fonction des coordonnées
                System.out.println("Latitude " + loc[1]);
                System.out.println("Longitude " + loc[1]);

                //System.out.println("name " + n);
                pointsAssociations.add(point);
            }
        }


        return pointsAssociations;
    }

    public List<String> getNames()
    {
        List<String> names = _associations.getName();
        List<String> namesMap = new ArrayList<>();
        System.out.println("name " + names);
        if (names != null) {
            for (String n: names) {
                namesMap.add(n);  //récupération de la liste des noms
                Integer id = _associations.getID(n);  //récupération de l'id en fonction du nom
                listOfNewIds.add(id);
                }
            }
            System.out.println("listIds" + listOfNewIds);
        return namesMap;

    }

    public ArrayList<OverlayItem> displayItems(GeoPoint myLocation, List<GeoPoint> pointsAssociations, List<String> names) {


        ArrayList<OverlayItem> items = new ArrayList<>();

        if (pointsAssociations != null) {
            for (int i = 0; i < pointsAssociations.size(); i++) {
                GeoPoint point = pointsAssociations.get(i);
                if (this._settings.checkRadius(myLocation, point)) {
                    String n = names.get(i);  //récupération de la liste des noms
                    OverlayItem item = new OverlayItem(n, null, point); //création de chaque item
                    items.add(item);

                }
            }
        }
        return items;
    }

    public void Consult_association(BottomSheetBehavior bottomSheetBehavior,List<TextView>t,List<String> address,List<String> phone,List<String> website,List<String> event,List<String> resume,OverlayItem item,ArrayList<OverlayItem> items){

        t.get(0).setText(item.getTitle());
        for (int i=0;i<items.size();i++){
            if (items.get(i).getTitle()==item.getTitle()){
                t.get(1).setText(address.get(i));
                t.get(2).setText(phone.get(i));
                t.get(3).setText(website.get(i));
                t.get(4).setText(resume.get(i));
                if("".equals(event.get(i))){
                    System.out.println("c'est bon"+ event.get(2));
                    t.get(5).setText("Pas d'événement");
                    t.get(5).setTextColor(Color.rgb(255,0,0));
                }
                else{
                    t.get(5).setText(event.get(i));
                    t.get(5).setTextColor(Color.rgb(0,0,255));
                }
            }
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    public List<String> getPhone(){
        return _associations.getPhoneNumber();
    }

    public List<String> getAddres(){
        return _associations.getAddress();
    }

    public List<String> getWebsite(){
        return _associations.getWebsite();
    }

    public List<String> getResume(){
        return _associations.getResume();
    }

    public List<String> getEvent(){
        return _associations.getEvent();
    }



    }

