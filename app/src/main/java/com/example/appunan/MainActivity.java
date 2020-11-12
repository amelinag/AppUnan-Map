package com.example.appunan;

import android.graphics.drawable.Drawable;
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
    private Button close;


    private String associationsNames;
    private MapView map; //creation de la map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //// MAP ///


        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);
        this.close=(Button)findViewById(R.id.close);
        this.resume=(ImageView)findViewById(R.id.resume);
        map= findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); //render
        map.setBuiltInZoomControls(true);  //pour le zoom
        GeoPoint startPoint = new GeoPoint(48.41090774536133,-4.491884231567383); //Position de depart
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);  //definir le zoom
        mapController.setCenter(startPoint);

        ArrayList<OverlayItem> items = new ArrayList<>();  //creation des pastilles
        OverlayItem home = new OverlayItem("Secours Populaire Français","Association humanitaire, d'entraide, sociale",new GeoPoint(48.41090774536133,-4.491884231567383));  //creation de la pastille "home"
        //Il y a le nom et la position de la pastille
        Drawable m = home.getMarker(0);  //forme de la pastille (deja existante dans la librairie)
        items.add(home); //ajout de la pastille home dans la liste des pastilles
        items.add(new OverlayItem("ENIB", "Ecole National des Ingénieurs de Brest", new GeoPoint(48.36085891723633, -4.567451477050781)));  //creation d'une autre pastille en version plus compact

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),  //associer les pastilles avec la map
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {   //reaction au clic
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                resume.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
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
                close.setVisibility(View.INVISIBLE);
            }
        });
        mOverlay.setFocusItemsOnTap(true);  // clique sur la pastille
        map.getOverlays().add(mOverlay);


        //// DATABASE TEST ////

        final Association db = Association.getInstance(getApplicationContext());
        db.open();
        //associationsNames = db.getName();

        TextView textViewName = (TextView) findViewById(R.id.textView_name);
        textViewName.setText(associationsNames);
        List<Double[]> associationsAddress = db.getLocations(getApplicationContext());
        db.close();


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