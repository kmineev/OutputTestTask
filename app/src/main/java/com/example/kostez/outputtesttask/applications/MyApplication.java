package com.example.kostez.outputtesttask.applications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.kostez.outputtesttask.LocaleHelper;
import com.example.kostez.outputtesttask.data.DatabaseHelperFactory;

/**
 * Created by Kostez on 10.09.2016.
 */
public class MyApplication extends Application {

    private static Context context;
    private static SharedPreferences preferences;
    private static final String PREFERENCES = "preferences";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        preferences = getSharedPreferences( getPackageName() + PREFERENCES, MODE_PRIVATE);
        LocaleHelper.onCreate(this);
        DatabaseHelperFactory.setHelper(getApplicationContext());
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    @Override
    public void onTerminate() {
        DatabaseHelperFactory.releaseHelper();
        super.onTerminate();
    }

}