package com.sparksfoundation.creditmanagementapp;

import android.app.Application;

import com.sparksfoundation.creditmanagementapp.Helper.DatabaseHelper;
import com.sparksfoundation.creditmanagementapp.Helper.DatabaseManager;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
    }
}
