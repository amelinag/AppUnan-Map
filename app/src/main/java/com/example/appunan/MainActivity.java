package com.example.appunan;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity<radiusMeters> extends AppCompatActivity {


    private ImageView resume;
    private ImageView settings;
    private ImageView IconLocation;
    private ImageView IconPhone;
    private ImageView IconWebsite;

    private Button close;
    private Button closeSettings;
    private ImageButton openSettings;

    private TextView n;
    private TextView a;
    private TextView p;
    private TextView w;
    private TextView textRadius;
    private TextView titleSettings;

    private SeekBar chooseRadius;

    private String associationsNames;
    private MapView map; //creation de la map
    private MyLocation myLocation = new MyLocation();

    private List<GeoPoint> allPoints= new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private Radius radius = new Radius();
    private Settings setting = new Settings(radius);
    private ArrayList<OverlayItem> items= new ArrayList<>();


    public void setItems(ArrayList<OverlayItem> items) {
        this.items = items;
    }


    private ItemizedOverlayWithFocus<OverlayItem> mOverlay = null;
    public ArrayList<OverlayItem> getItems() {
        return items;
    }


    private SearchView searchView;
    private ListView listView;
    private ArrayList list;
    private ArrayAdapter adapter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();

        ListAdapter adapter;


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

        final Associations db = Associations.getInstance(context); // création de la bdd
        Map m = new Map(db, map, setting);
        System.out.println("radius " + radius.getRadius());
        db.open();


        /// CREATION DES ITEMS EN FONCTION DE LA BDD ///
        GeoPoint myPoint= myLocation.getMyLocation( this,  context);  // récupérer le point correspond à ma localisation
        this.allPoints = m.getPoints(context);
        this.names = m.getNames();

        List<String> na = m._associations.getPhoneNumber();
        List<String> ad = m._associations.getAddress();
        List<String> web = m._associations.getWebsite();
        db.close();


        //this.setItems(m.displayItems(myPoint, allPoints, names));



        /// CREATION ET AFFICHAGE  ITEM MYLOCATION ///

        ArrayList<OverlayItem> myItems = new ArrayList<>();
        OverlayItem myItem = new OverlayItem("My location", "my Location", myPoint);
        myItem.getMarker(3);
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

        map.getOverlays().add(yLocationOverlay);




        // interaction settings

        this.settings = (ImageView) findViewById(R.id.settings);

        this.openSettings = (ImageButton)findViewById(R.id.openSettings);
        this.closeSettings = (Button)findViewById(R.id.closeSettings);
        closeSettings.setVisibility(View.INVISIBLE);

        this.chooseRadius=(SeekBar)findViewById(R.id.choseRadius);
        this.textRadius= (TextView)findViewById(R.id.textRadius);
        this.titleSettings= (TextView)findViewById(R.id.titleSettings);


        openSettings.setOnClickListener(new View.OnClickListener() {
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
                settings.setVisibility(View.VISIBLE);
                closeSettings.setVisibility(View.VISIBLE);
                chooseRadius.setVisibility(View.VISIBLE);
                textRadius.setVisibility(View.VISIBLE);
                titleSettings.setVisibility(View.VISIBLE);

            }
        });
        closeSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                settings.setVisibility(View.INVISIBLE);
                map.setBuiltInZoomControls(true);
                closeSettings.setVisibility(View.INVISIBLE);
                chooseRadius.setVisibility(View.INVISIBLE);
                textRadius.setVisibility(View.INVISIBLE);
                titleSettings.setVisibility(View.INVISIBLE);

            }
        });



        // perform seek bar change listener event used for getting the progress value
        chooseRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;

                if(MainActivity.this.mOverlay !=null)
                {
                    for(int i = 0; i < map.getOverlays().size(); i++)
                    {
                        Overlay overlay = map.getOverlays().get(i);
                        map.getOverlays().remove(overlay);
                    }
                }
                map.getOverlays().add(yLocationOverlay);
                double radiusMeters = (double)progress*1000;
                System.out.println(radiusMeters);                                                                                               // MISE AA JOUR DES ITEMS
                MainActivity.this.radius.setRadius(radiusMeters);
                MainActivity.this.setItems(m.displayItems(myPoint,MainActivity.this.allPoints, MainActivity.this.names));
                System.out.println("items " + MainActivity.this.getItems());


                MainActivity.this.mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(context,  //associer les pastilles avec la map
                        MainActivity.this.getItems(), new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {   //reaction au clic
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

                MainActivity.this.mOverlay.setFocusItemsOnTap(true);  // clique sur la pastille
                map.getOverlays().add(MainActivity.this.mOverlay);
                map.refreshDrawableState();

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();



            }

        });
        /// AFFICHAGE ITEMS DES ASSOCIATIONS ///



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

        this.searchView = findViewById(R.id.search);
        this.listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,this.names);
        listView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(list.contains(query)){
                    ((ArrayAdapter) adapter).getFilter().filter(query);

                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                ((ArrayAdapter) adapter).getFilter().filter(newText);
                listView.setVisibility(View.VISIBLE);
                return false;
            }
        });

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