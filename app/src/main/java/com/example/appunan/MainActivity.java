package com.example.appunan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

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


    private ImageView resume;
    private ImageView IconLocation;
    private ImageView IconPhone;
    private ImageView IconWebsite;

    private Button close;

    private TextView n;
    private TextView a;
    private TextView p;
    private TextView w;

    private String associationsNames;
    private MapView map; //creation de la map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /// CREATION MAP ///

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);
        this.close=(Button)findViewById(R.id.close);

        this.resume=(ImageView)findViewById(R.id.resume);
        this.IconLocation=(ImageView)findViewById(R.id.ImageLocation);
        this.IconPhone=(ImageView)findViewById(R.id.ImagePhone);
        this.IconWebsite=(ImageView)findViewById(R.id.ImageWebsite);

        this.n=(TextView)findViewById(R.id.textViewName);
        this.a=(TextView)findViewById(R.id.textViewAddress);
        this.p=(TextView)findViewById(R.id.textViewPhoneNumber);
        this.w=(TextView)findViewById(R.id.textViewWebsite);

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



        ArrayList<OverlayItem> items= m.displayItems(getApplicationContext());
        List<String> na =  m._associations.getPhoneNumber();
        List<String> ad =  m._associations.getAddress();
        List<String> web =  m._associations.getWebsite();


        db.close();

        System.out.println("items "+ items);
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),  //associer les pastilles avec la map
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {   //reaction au clic
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                resume.setVisibility(View.VISIBLE);
                IconLocation.setVisibility(View.VISIBLE);
                IconPhone.setVisibility(View.VISIBLE);
                IconWebsite.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                n.setVisibility(View.VISIBLE);
                a.setVisibility(View.VISIBLE);
                p.setVisibility(View.VISIBLE);
                w.setVisibility(View.VISIBLE);
                n.setText(item.getTitle());
                for (int i=0;i<items.size();i++){
                    if (items.get(i).getTitle()==item.getTitle()){
                        a.setText(ad.get(i));
                        p.setText(na.get(i));
                        w.setText(web.get(i));
                    }

                }
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
     });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resume.setVisibility(View.INVISIBLE);
                IconLocation.setVisibility(View.INVISIBLE);
                IconPhone.setVisibility(View.INVISIBLE);
                IconWebsite.setVisibility(View.INVISIBLE);
                close.setVisibility(View.INVISIBLE);
                n.setVisibility(View.INVISIBLE);
                a.setVisibility(View.INVISIBLE);
                p.setVisibility(View.INVISIBLE);
                w.setVisibility(View.INVISIBLE);
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