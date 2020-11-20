package com.example.appunan;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataBaseManager extends SQLiteAssetHelper {

    public static final int DATA_VERSION = 9;
    private static final String DATABASE_NAME = "associations.db";

    public DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATA_VERSION);
        this.setForcedUpgrade();
    }

}
