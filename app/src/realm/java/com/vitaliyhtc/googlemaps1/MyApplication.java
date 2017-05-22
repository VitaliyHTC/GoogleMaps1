package com.vitaliyhtc.googlemaps1;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

public class MyApplication extends Application {
    private static MyApplication sMyApplication;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate() {
        super.onCreate();

        sMyApplication = this;

        Realm.init(this);
    }

    public static Context getContext() {
        return sMyApplication;
    }
}
