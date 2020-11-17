package com.example.appunan;

import java.util.List;

public class Settings {

    private Radius _r;
    private List<Category> _c;

    public Settings(Radius radius, List<Category> categories)
    {
        this._r = radius;
        this._c =categories;
    }

    public void changeSetting(Radius r, List<Category> c)
    {
        this._r = r;
        this._c = c;
    }
    public Object[] getSettings()
    {
        Object[] settings = new Object[3];
        settings[0]= this._r;
        settings[1]= this._c;
        return settings;
    }

}
