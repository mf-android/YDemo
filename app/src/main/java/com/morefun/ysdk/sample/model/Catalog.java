package com.morefun.ysdk.sample.model;


import android.app.Activity;

public class Catalog {

    public String name;
    public Class<? extends Activity> cls;

    public Catalog(String name, Class<? extends Activity> cls) {
        this.name = name;
        this.cls = cls;
    }

    @Override
    public String toString() {
        return name;
    }
}