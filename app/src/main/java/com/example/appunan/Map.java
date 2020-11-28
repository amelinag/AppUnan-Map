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
import java.util.Arrays;
import java.util.List;

public class Map extends AppCompatActivity{
    private MapView _map; //creation de la map

    private Associations _associations;


    public Map(Associations associations, MapView map) {
        this._associations = associations;
        this._map = map;
    }


    public ArrayList<OverlayItem> displayItems(Context context) {
        Double[] a={48.3994098,-4.4981507};
        Double[] b={48.3842446,-4.5016007};
        Double[] c={48.4059796,-4.4752021};
        Double[] d={48.4212329,-4.4674397};
        ArrayList<Double[]> coordinates = new ArrayList<>();
        coordinates.add(a);
        coordinates.add(b);
        coordinates.add(c);
        coordinates.add(d);
        //System.out.println("\n\ncoordinates= " + coordinates );
        List<String> names =  _associations.getName();
        ArrayList<OverlayItem> items = new ArrayList<>();
        Marker startMarker = new Marker(_map);
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
