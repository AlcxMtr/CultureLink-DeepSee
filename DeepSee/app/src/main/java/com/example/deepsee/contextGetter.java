package com.example.deepsee;

import android.app.Application;
import android.content.Context;

// This feels dirty and bad and it is dirty and bad and very inefficient and not good :(
public class contextGetter extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        contextGetter.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return contextGetter.context;
    }
}