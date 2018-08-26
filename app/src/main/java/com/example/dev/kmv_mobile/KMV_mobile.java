package com.example.dev.kmv_mobile;

import android.app.Application;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class KMV_mobile extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //Toast.makeText(getApplicationContext(),"created",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
       // Toast.makeText(getApplicationContext(),"terminated",Toast.LENGTH_SHORT).show();
    }
}
