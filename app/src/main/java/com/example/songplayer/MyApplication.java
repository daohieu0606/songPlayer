package com.example.songplayer;

import android.app.Application;
import android.util.Log;

class MyApplication extends Application {
    String TAG = "TESST";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }
}
