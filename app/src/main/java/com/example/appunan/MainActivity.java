package com.example.appunan;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ImageView IconLocation;
    private ImageView IconPhone;
    private ImageView IconWebsite;


    private TextView n;
    private TextView a;
    private TextView p;
    private TextView w;
    private TextView r;
    private TextView e;

    private String associationsNames;
    private MapView map; //creation de la map
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /// CREATION MAP ///

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);


        this.IconLocation=(ImageView)findViewById(R.id.ImageLocation);
        this.IconPhone=(ImageView)findViewById(R.id.ImagePhone);
        this.IconWebsite=(ImageView)findViewById(R.id.ImageWebsite);

        this.n=(TextView)findViewById(R.id.textViewName);
        this.a=(TextView)findViewById(R.id.textViewAddress);
        this.p=(TextView)findViewById(R.id.textViewPhoneNumber);
        this.w=(TextView)findViewById(R.id.textViewWebsite);
        this.r=(TextView)findViewById(R.id.textViewResume);
        this.e=(TextView)findViewById(R.id.textViewEvent);

        LinearLayout linearLayout=findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior= BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        map= findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); //render
        map.setBuiltInZoomControls(true);  //pour le zoom
        GeoPoint startPoint = new GeoPoint(48.41090774536133,-4.491884231567383); //Position de depart
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);  //definir le zoom
        mapController.setCenter(startPoint);

        ///CREATION ET OUVERTURE BASE DE DONNEES ///

        final Associations db = Associations.getInstance(getApplicationContext());

        Map m = new Map(db,map);
        db.open();


        /// CREATION ET AFFICHAGE DES ITEMS EN FONCTION DE LA BDD ///
        List<String> ad=m.getAddres();
        List<String> pn=m.getPhone();
        List<String> web=m.getWebsite();
        List<String> res=m.getResume();
        List<String> ev=m.getEvent();
        ArrayList<OverlayItem> items= m.displayItems(getApplicationContext());
        db.close();
        System.out.println(ev.get(3));
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),  //associer les pastilles avec la map
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {   //reaction au clic
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                n.setText(item.getTitle());
                for (int i=0;i<items.size();i++){
                    if (items.get(i).getTitle()==item.getTitle()){
                        a.setText(ad.get(i));
                        p.setText(pn.get(i));
                        w.setText(web.get(i));
                        r.setText(res.get(i));
                        if("".equals(ev.get(i))){
                            System.out.println("c'est bon"+ ev.get(2));
                            e.setText("Pas d'événement");
                            e.setTextColor(Color.rgb(255,0,0));
                        }
                        else{
                            e.setText(ev.get(i));
                            e.setTextColor(Color.rgb(0,0,255));
                        }
                    }
                }
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }


            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
     });
        mOverlay.setFocusItemsOnTap(true);  // clique sur la pastille
        map.getOverlays().add(mOverlay);





    }
    @Override
    public void onPause() {   //mise en pause de l'activité
        super.onPause();
        map.onPause();  // map mise en pause
    }
    @Override
    public void onResume() {   //mise en route de l'activité
        super.onResume();
        map.onResume();
    }




}