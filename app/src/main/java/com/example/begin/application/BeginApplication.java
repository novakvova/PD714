package com.example.begin.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class BeginApplication extends Application {
    private static BeginApplication instance;
    private static Context appContext;

    public static BeginApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}