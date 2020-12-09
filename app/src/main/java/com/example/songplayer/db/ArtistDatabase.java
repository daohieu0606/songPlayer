package com.example.songplayer.db;

import android.app.Application;

import com.example.songplayer.dao.ArtistDAO;

public class ArtistDatabase {
    private static final String TAG = "TESST";

    private static ArtistDAO artistDAO;
    private static ArtistDatabase instance;

    public static synchronized ArtistDatabase getInstance(Application newContext) {
        if (instance ==null) {
            instance = new ArtistDatabase();
            artistDAO = new ArtistDAO(newContext);
        }
        return  instance;
    }

    public ArtistDAO artistDAO() {
        return artistDAO;
    }
}
