package com.example.appunan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity{
    private MapView _map; //creation de la map


    public Associations _associations;
    private Settings _settings;
    //public List<Integer> listOfIds = this._associations.getID();


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


    public ArrayList<OverlayItem> filterItemsbySearch( String search,  List<Integer> ids, Context context) {
        ArrayList<OverlayItem> items = new ArrayList<>();

        if (ids !=null) {
            for (int id : ids) {
                String name =this._associations.getName(id);
                if(search.equalsIgnoreCase(name)) {
                    List<Double> loc = this._associations.getLocations(context,id);  //récupération de la liste des coordonnées

                    System.out.println("loc "+loc);
                    GeoPoint point = new GeoPoint(loc.get(0), loc.get(1)); //création des points en fonction des coordonnées
                    System.out.println("Latitude " + loc.get(0));
                    System.out.println("Longitude " + loc.get(1));
                    System.out.println("name " + name);
                    OverlayItem item = new OverlayItem(name, " ", point); //création de chaque item
                    items.add(item);
                    Drawable myDraw=context.getResources().getDrawable(R.drawable.location);
                    item.setMarker(myDraw);
                }
            }
        }
        return items;
    }

    public ArrayList<OverlayItem> filterItemsbyCategoryandRadius(List<String> category, List<Integer> ids, Context context, GeoPoint myLocation){
        ArrayList<OverlayItem> items=new ArrayList<>();
        if (ids!=null){
            for(int id :ids){
                String cat=this._associations.getCategory(id);
                System.out.println("cat " + cat);
                for(String s : category) {
                    if (cat.equals(s)) {
                        List<Double> loc = this._associations.getLocations(context, id);  //récupération de la liste des coordonnées

                        GeoPoint point = new GeoPoint(loc.get(0), loc.get(1)); //création des points en fonction des coordonnées
                        System.out.println("Latitude " + loc.get(0));
                        System.out.println("Longitude " + loc.get(1));

                        if (this._settings.checkRadius(myLocation, point)) {
                            String n = this._associations.getName(id);  //récupération de la liste des noms
                            System.out.println("name " + n);
                            OverlayItem item = new OverlayItem(n, " ", point); //création de chaque item
                            items.add(item);
                            Drawable myDraw=context.getResources().getDrawable(R.drawable.location);
                            item.setMarker(myDraw);
                        }
                    }
                }

            }
        }
        return items;
    }

    public void consultAssociation(BottomSheetBehavior bottomSheetBehavior,List<TextView>t,
                                    OverlayItem item,ArrayList<OverlayItem> items,List<Integer> ids){

        t.get(0).setText(item.getTitle());
        for (int i = 0; i < items.size(); i++) {
            String accurateTitle= items.get(i).getTitle();
            if (accurateTitle == item.getTitle()) {
                int id = this._associations.getIDbyName(accurateTitle);
                t.get(1).setText(this._associations.getAddress(id));
                t.get(2).setText(this._associations.getPhoneNumber(id));
                t.get(3).setText(this._associations.getWebsite(id));
                t.get(4).setText(this._associations.getResume(id));
                if ("".equals(this._associations.getEvent(id))) {
                    System.out.println("c'est bon" + this._associations.getEvent(2));
                    t.get(5).setText("Pas d'événement");
                    t.get(5).setTextColor(Color.rgb(255, 0, 0));
                } else {
                    t.get(5).setText(this._associations.getEvent(id));
                    t.get(5).setTextColor(Color.rgb(0, 0, 255));
                }
            }
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    public List<Integer> getID(){

        return this._associations.getID();
    }

    }

