package com.example.songplayer.db;

import android.app.Application;

import com.example.songplayer.dao.OnlSongDAOImp;
import com.example.songplayer.dao.SongDAOImp;

public class OnlSongDatabase {
    private static final String TAG = "TESST";
    private static OnlSongDatabase instance;
    private static OnlSongDAOImp onlSongDAOImp;

    public static synchronized OnlSongDatabase getInstance(Application newContext) {
        if (instance ==null) {
            instance = new OnlSongDatabase();
            onlSongDAOImp = new OnlSongDAOImp(newContext);
        }
        return  instance;
    }

    public OnlSongDAOImp songDAO() {
        return onlSongDAOImp;
    }
}
