package com.example.appunan;

import java.util.List;

public class Category {

    private List<String> _category;

    public Category()
    {

    }

    public List<String> get_categories() {
        return _category;
    }

    public void set_categories(List<String> category) {
        this._category = category;
    }
}
