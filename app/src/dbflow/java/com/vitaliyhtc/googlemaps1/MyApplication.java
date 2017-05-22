package com.vitaliyhtc.googlemaps1;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.DirectModelNotifier;
import com.vitaliyhtc.googlemaps1.data.dbflow.DBFlowDatabase;

public class MyApplication extends Application {
    private static MyApplication sMyApplication;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate() {
        super.onCreate();

        sMyApplication = this;

        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseConfig(new DatabaseConfig.Builder(DBFlowDatabase.class)
                        .modelNotifier(DirectModelNotifier.get())
                        .build()).build());
    }

    public static Context getContext() {
        return sMyApplication;
    }
}
