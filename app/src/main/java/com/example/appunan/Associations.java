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


    //// CREATION, OUVERTURE, FERMETURE DE LA BASE DE DONNEES/////

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


    public List<Double> getLocations(Context context, int ids)
    {
        String req = "SELECT address FROM association WHERE id = "+Integer.toString(ids);
        c= db.rawQuery(req, null);
        c.moveToFirst();
        List<Double> location =new ArrayList<>();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.FRANCE);
            int index = c.getColumnIndex("address");
            List<Address> addresses = geocoder.getFromLocationName(c.getString(0),5);
            Address address = addresses.get(0);
            System.out.println("result latitude" + address.getLatitude() );
            System.out.println("result longitude" + address.getLongitude() );
            location.add(address.getLatitude());
            location.add(address.getLongitude());

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return location;

    }

    public String getName(int ids)
    {
        c= db.rawQuery("SELECT name FROM association WHERE id = " + Integer.toString(ids), null);
        c.moveToFirst();
        String names=c.getString(0);
        return names;
    }


    public String  getPhoneNumber(int ids)
    {
        c= db.rawQuery("SELECT phoneNumber FROM association WHERE id = " + Integer.toString(ids), null);
        c.moveToFirst();

        String phoneNumber=c.getString(0);
        return phoneNumber;
    }

    public String  getAddress(int ids)
    {
        c= db.rawQuery("SELECT address FROM association WHERE id = " + Integer.toString(ids), null);
        c.moveToFirst();

        String phoneNumber=c.getString(0);
        return phoneNumber;
    }

    public String  getWebsite(int ids)
    {
        c= db.rawQuery("SELECT website FROM association WHERE id = " + Integer.toString(ids), null);
        c.moveToFirst();

        String website=c.getString(0);
        return website;
    }

    public List<Integer> getID() {
        c = db.rawQuery("SELECT id FROM association", null);
        c.moveToFirst();
        List<Integer> ids  = new ArrayList<>();
        while (!c.isAfterLast()) {
            int index = c.getColumnIndex("id");
            ids.add(c.getInt(index));
            c.moveToNext();
        }
        return ids;
    }

    public Integer getIDbyName(String names)
    {
        c= db.rawQuery("SELECT id FROM association WHERE name=? ", new String[]{names});
        c.moveToFirst();
        int id=c.getInt(0);
        return id;
    }

    public String  getResume(int ids)
    {
        c= db.rawQuery("SELECT resume FROM association WHERE id = " + Integer.toString(ids), null);
        c.moveToFirst();
        String resume=c.getString(0);
        return resume;
    }

    public String  getEvent(int ids)
    {
        c= db.rawQuery("SELECT event FROM association WHERE " + ids, null);
        c.moveToFirst();
        String event=c.getString(0);
        return event;
    }

}
