package com.example.appunan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Associations {


    //// CREATION, OUVERTURE, FERMETURE DE LA BASE DE DONNEES////

    private DataBaseManager baseManager;  //on declare le DataBaseOpenHelper
    private SQLiteDatabase db;  // objet sqlite db
    private static Associations instance;
    Cursor c = null;

    private Associations(Context context ){

        this.baseManager = new DataBaseManager(context);
    }

    public static Associations getInstance(Context context){
        if(instance == null){
            instance = new Associations(context);
        }
        return instance;
    }

    public void open() {
        this.db=baseManager.getWritableDatabase();
    }
    public void close() {
        if(db!=null){
            this.db.close();
        }
    }


    //// REQUETE VERS LA BASE DE DONNEES////

    /*public String getName(){
        c= db.rawQuery("SELECT address FROM association", null);
        f(c.getCount() !=1){
        return "error";
    }

        c.moveToFirst();
        return c.getString(0);
    }*/

    public List<Double[]> getLocations(Context context)
    {
        c= db.rawQuery("SELECT address FROM association", null);
        c.moveToFirst();
        System.out.println(c.getString(0));
        List<Double[]> location = new ArrayList<>();;
        while (!c.isAfterLast()){
            try {
                //String address1 = "11 rue Lanrédec, 29200 Brest";
                //System.out.println(c.getString(0));



                Geocoder geocoder = new Geocoder(context, Locale.FRANCE);


                int index = c.getColumnIndex("address");
                List<Address> addresses = geocoder.getFromLocationName(c.getString(index), 1);
                Address address = addresses.get(0);
                Double[] aLocation =  {address.getLatitude(),address.getLongitude()};
                System.out.println("result latitude" + address.getLatitude() );
                System.out.println("result longitude" + address.getLongitude() );
                location.add(aLocation);
                c.moveToNext();

            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        return location;

    }

    public List<String> getName()
    {
        c= db.rawQuery("SELECT name FROM association", null);
        c.moveToFirst();
        List<String> names = new ArrayList<>();
        while (!c.isAfterLast()){
            int index = c.getColumnIndex("name");
            names.add(c.getString(index));
            c.moveToNext();

        }
        return names;

    }

    public List<String> getPhoneNumber() {
        c = db.rawQuery("SELECT phoneNumber FROM association", null);
        c.moveToFirst();
        List<String> phoneNumbers  = new ArrayList<>();
        while (!c.isAfterLast()) {
            int index = c.getColumnIndex("phoneNumber");
            phoneNumbers.add(c.getString(index));
            c.moveToNext();
        }
        return phoneNumbers;
    }

    public List<String> getAddress() {
        c = db.rawQuery("SELECT address FROM association", null);
        c.moveToFirst();
        List<String> Address  = new ArrayList<>();
        while (!c.isAfterLast()) {
            int index = c.getColumnIndex("address");
            Address.add(c.getString(index));
            c.moveToNext();
        }
        return Address;
    }

    public List<String> getWebsite() {
        c = db.rawQuery("SELECT website FROM association", null);
        c.moveToFirst();
        List<String> Website  = new ArrayList<>();
        while (!c.isAfterLast()) {
            int index = c.getColumnIndex("website");
            Website.add(c.getString(index));
            c.moveToNext();
        }
        return Website;
    }





}
