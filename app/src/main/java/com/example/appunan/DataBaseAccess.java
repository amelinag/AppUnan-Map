package com.example.appunan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseAccess {


    //// CREATION, OUVERTURE, FERMETURE DE LA BASE DE DONNEES////

    private DataBaseManager baseManager;  //on declare le DataBaseOpenHelper
    private SQLiteDatabase db;  // objet sqlite db
    private static DataBaseAccess instance;
    Cursor c = null;

    private DataBaseAccess(Context context){
        this.baseManager = new DataBaseManager(context);
    }

    public static DataBaseAccess getInstance(Context context){
        if(instance == null){
            instance = new DataBaseAccess(context);
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

    public String getName(){
        c= db.rawQuery("SELECT address FROM association", null);
        /*f(c.getCount() !=1){
            return "error";
        }*/

        c.moveToFirst();
        return c.getString(0);

    }
}
