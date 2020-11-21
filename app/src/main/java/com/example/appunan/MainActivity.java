package com.example.appunan;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
    private MyLocation myLocation = new MyLocation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        Context context2 = getApplicationContext();
        /// CREATION MAP ///

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);
        this.close = (Button) findViewById(R.id.close);

        this.resume = (ImageView) findViewById(R.id.resume);
        this.IconLocation = (ImageView) findViewById(R.id.ImageLocation);
        this.IconPhone = (ImageView) findViewById(R.id.ImagePhone);
        this.IconWebsite = (ImageView) findViewById(R.id.ImageWebsite);

        this.n = (TextView) findViewById(R.id.textViewName);
        this.a = (TextView) findViewById(R.id.textViewAddress);
        this.p = (TextView) findViewById(R.id.textViewPhoneNumber);
        this.w = (TextView) findViewById(R.id.textViewWebsite);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK); //render
        map.setBuiltInZoomControls(true);  //pour le zoom
        GeoPoint startPoint = new GeoPoint(48.38547134399414, -4.5312371253967285); //Position de depart
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);  //definir le zoom
        mapController.setCenter(startPoint);

        Radius r = new Radius(4000.0);
        Settings setting = new Settings(r);
        final Associations db = Associations.getInstance(context);
        Map m = new Map(db, map, setting);
        db.open();
        GeoPoint myPoint= myLocation.getMyLocation( this,  context);
        /// CREATION ET AFFICHAGE DES ITEMS EN FONCTION DE LA BDD ///

        ArrayList<OverlayItem> items = m.displayItems(context, this,myPoint);
        List<String> na = m._associations.getPhoneNumber();
        List<String> ad = m._associations.getAddress();
        List<String> web = m._associations.getWebsite();


        db.close();

        System.out.println("items " + items);
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(context,  //associer les pastilles avec la map
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
                for (int i = 0; i < (items.size()); i++) {
                    if (items.get(i).getTitle() == item.getTitle()) {
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
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }



        ArrayList<OverlayItem> myItems = new ArrayList<>();
        OverlayItem myItem = new OverlayItem("My location", "my Location", myPoint);
        Drawable y = myItem.getMarker(3);
        myItems.add(myItem);
        System.out.println("item " + myItems);

        ItemizedOverlayWithFocus<OverlayItem> yLocationOverlay = new ItemizedOverlayWithFocus<OverlayItem>(context,  //associer les pastilles avec la map
                myItems, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {   //reaction au clic
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }

        });
        yLocationOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(yLocationOverlay);
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