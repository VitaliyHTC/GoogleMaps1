package com.vitaliyhtc.googlemaps1;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

public class MyApplication extends Application {
    private static MyApplication sMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sMyApplication = this;

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
    }

    public static Context getContext() {
        return sMyApplication;
    }
}
