package com.example.songplayer.sdcardobserver;

import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.Nullable;

public class SDCardObserver extends FileObserver {
    public SDCardObserver(String path){
        super(path);
    }
    @Override
    public void onEvent(int i, @Nullable String s) {
        switch (i){
            case FileObserver.ALL_EVENTS:
                Log.d("TESST", "Change" );
                break;
            case FileObserver.CREATE:
                Log.d("TESST", "onEvent: "+ "Create");
                break;
        }
    }


}
