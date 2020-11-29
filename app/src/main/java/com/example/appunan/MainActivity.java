package com.example.appunan;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    

    private TextView r;
    private TextView e;

    private BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();

        ListAdapter adapter;


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


/// CREATION ET AFFICHAGE DES ITEMS EN FONCTION DE LA BDD ///
        List<String> ad=m.getAddres();
        List<String> pn=m.getPhone();
        List<String> web=m.getWebsite();
        List<String> res=m.getResume();
        List<String> ev=m.getEvent();

        db.close();
        System.out.println(ev.get(3));


        /*
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getApplicationContext(),  //associer les pastilles avec la map
                items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {   //reaction au clic
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                m.Consult_association(bottomSheetBehavior, t, ad, pn, web, ev, res, item, items);

                return true;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        });*/


        ///////////////////// CREATION ET AFFICHAGE  ITEM MYLOCATION ///////////////////////////////

        ArrayList<OverlayItem> myItems = new ArrayList<>();
        OverlayItem myItem = new OverlayItem("My location", "my Location", myPoint);
        myItem.getMarker(3);
        myItems.add(myItem);
        System.out.println("item " + myItems);

        ItemizedOverlayWithFocus<OverlayItem> yLocationOverlay = new ItemizedOverlayWithFocus<OverlayItem>(context,  //associer les pastilles avec la map
                myItems, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {   //reaction au clic
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                m.Consult_association(bottomSheetBehavior, t, ad, pn, web, ev, res, item, items);
                return true;

            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }


        });

        map.getOverlays().add(yLocationOverlay);

        ////////////////////////////////////////////////////////////////////////////////////////////


        ///////////////////// CREATION ET AFFICHAGE SETTINGS ///////////////////////////////////////

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
        ////////////////////////////////////////////////////////////////////////////////////////////


        //////////////////////////////////FILTRAGE PAR RADIUS //////////////////////////////////////

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
                        m.Consult_association(bottomSheetBehavior, t, ad, pn, web, ev, res, item, MainActivity.this.items);
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

        ////////////////////////////////////////////////////////////////////////////////////////////

        /*
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
        });*/
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