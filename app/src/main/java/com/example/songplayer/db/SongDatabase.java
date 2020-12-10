package com.example.songplayer.db;

import android.app.Application;

import com.example.songplayer.dao.OnlSongDAOImp;
import com.example.songplayer.dao.SongDAOImp;

public class SongDatabase{
    private static final String TAG = "TESST";
    private static SongDatabase instance;
    private static SongDAOImp songDAOImp;
    private static OnlSongDAOImp onlSongDAOImp;

    public static synchronized SongDatabase getInstance(Application newContext) {
        if (instance ==null) {
            instance = new SongDatabase();
            songDAOImp = new SongDAOImp(newContext);
            onlSongDAOImp = new OnlSongDAOImp(newContext);
        }
        return  instance;
    }

    public SongDAOImp songDAO() {
        return songDAOImp;
    }
    public OnlSongDAOImp OnlSongDAO() {
        return onlSongDAOImp;
    }
}
