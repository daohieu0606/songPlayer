package com.example.songplayer.sdcardobserver;

import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.songplayer.MyApplication;

public class SDCardObserver extends FileObserver {
    String path;
    public SDCardObserver(String path){
        super(path);
        this.path = path;
    }
    String TAG = "TESST";


    @Override
    public void onEvent(int i, @Nullable String file) {

        Log.d(TAG, "onEvent: "+file);

        if(i == 1073741840){
            new Thread(() -> {
                MyApplication.songDatabase.scanOfflineSongs()
                        .forEach(MyApplication.database.songDao()::insert);
            }).start();
        }
    }





}
