package com.kolastudios;

import com.orm.SugarApp;

public class MyApp extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();

        new KSUtils.Builder()
                .setContext(this)
                .setLogTag(Constants.LOG_TAG)
                .setLogEnabled(Constants.LOG_ENABLED)
                .build();
    }
}
