package com.example.kostez.outputtesttask;

import android.app.Application;

/**
 * Created by Kostez on 10.09.2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocaleHelper.onCreate(this);
    }
}