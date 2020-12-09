package com.example.songplayer.db;

import android.app.Application;
import com.example.songplayer.dao.AlbumDAO;

public class AlbumDatabase {
    private static final String TAG = "TESST";

    private static AlbumDAO albumDAO;
    private static AlbumDatabase instance;

    public static synchronized AlbumDatabase getInstance(Application newContext) {
        if (instance ==null) {
            instance = new AlbumDatabase();
            albumDAO = new AlbumDAO(newContext);
        }
        return  instance;
    }

    public AlbumDAO albumDAO() {
        return albumDAO;
    }
}
