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

        this.n=(TextView)findViewById(R.id.textViewName);
        this.a=(TextView)findViewById(R.id.textViewAddress);
        this.p=(TextView)findViewById(R.id.textViewPhoneNumber);
        this.w=(TextView)findViewById(R.id.textViewWebsite);
        this.r=(TextView)findViewById(R.id.textViewResume);
        this.e=(TextView)findViewById(R.id.textViewEvent);
        List<TextView> t=new ArrayList<TextView>();
        t.add(n);
        t.add(a);
        t.add(p);
        t.add(w);
        t.add(r);
        t.add(e);

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
                m.Consult_association(bottomSheetBehavior,t,ad,pn,web,ev,res,item,items);
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