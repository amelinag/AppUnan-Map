package com.example.appunan;

import org.osmdroid.util.GeoPoint;

public class Settings {

    private Radius _r;
    private Category _c;

    public Settings(Radius radius, Category c)
    {
        this._r = radius;
        this._c= c;
    }

    public void changeSetting(Radius r, Category c)
    {
        this._r = r;
        this._c= c;
    }
    public Object[] getSettings()
    {
        Object[] settings = new Object[3];
        settings[0]= this._r;
        settings[1]= this._c;
        return settings;
    }

    public double getRadiusDouble() {

        return this._r.getRadius();
    }

    public Radius getRadius() {

        return this._r;
    }

    public boolean checkRadius(GeoPoint myLocation, GeoPoint association)
    {
        double distance = myLocation.distanceToAsDouble(association);
        if (distance <getRadiusDouble())
        {
            return true;
        }
        return false;
    }

}
