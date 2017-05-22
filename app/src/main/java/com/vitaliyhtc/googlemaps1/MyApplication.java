package com.vitaliyhtc.googlemaps1;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.runtime.DirectModelNotifier;

import io.realm.Realm;

public class MyApplication extends Application {
    private static MyApplication sMyApplication;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate() {
        super.onCreate();

        sMyApplication = this;
        // TODO: 22.05.17 put this selection code into two separate classes in different builds that extends MyApplication class.
        if (ConfigBuildFlavors.BUILD_FLAVOR_CURRENT == Config.BUILD_FLAVOR_DB_REALM) {
            Realm.init(this);
        }
        if (ConfigBuildFlavors.BUILD_FLAVOR_CURRENT == Config.BUILD_FLAVOR_DB_DBFLOW) {
            FlowManager.init(new FlowConfig.Builder(this)
                    .addDatabaseConfig(new DatabaseConfig.Builder(DBFlowDatabase.class)
                            .modelNotifier(DirectModelNotifier.get())
                            .build()).build());
        }
    }

    public static Context getContext() {
        return sMyApplication;
    }
}
