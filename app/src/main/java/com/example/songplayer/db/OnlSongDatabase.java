package com.example.songplayer.db;

import android.app.Application;

import com.example.songplayer.dao.daoimpl.OnlSongDAOImp;
import com.example.songplayer.db.entity.SongEntity;

import java.util.List;

public class OnlSongDatabase {
    private static final String TAG = "TESST";
    private static OnlSongDatabase instance;
    private static OnlSongDAOImp onlSongDAOImp;

    public static synchronized OnlSongDatabase getInstance(Application newContext) {
        if (instance == null) {
            instance = new OnlSongDatabase();
            onlSongDAOImp = new OnlSongDAOImp();
        }
        return instance;
    }

    public OnlSongDAOImp onlSongDao() {
        return onlSongDAOImp;
    }

    public List<SongEntity> fetchOnlineSongs() {
        return onlSongDAOImp.fetchOnlineSongs();
    }
}
