package com.example.appunan;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class Settings {

    private Radius _r;
    private List<Category> _c;

    public Settings(Radius radius)
    {
        this._r = radius;
    }

    public void changeSetting(Radius r)
    {
        this._r = r;
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
