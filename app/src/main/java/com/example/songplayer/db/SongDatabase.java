package com.example.songplayer.db;

import android.app.Application;

import com.example.songplayer.MyApplication;
import com.example.songplayer.dao.daoimpl.SongDAOImp;

public class SongDatabase {
    private static final String TAG = "TESST";
    private static SongDatabase instance;
    private static SongDAOImp songDAOImp;

    public static synchronized SongDatabase getInstance(Application newContext) {
        if (instance == null) {
            instance = new SongDatabase();
            songDAOImp = new SongDAOImp(newContext);
        }
        return instance;
    }

    public SongDAOImp songDAO() {
        return songDAOImp;
    }

    public void scanOfflineSong(boolean firstLoad) throws Exception {
        if (songDAOImp != null) {
            new Thread(() -> {
                if (firstLoad) {
                    songDAOImp.scanSongList(data -> {
                        data.forEach(MyApplication.database.songDao()::insert);
                    });

                } else {
                    songDAOImp.scanSongList();
                }
            }).start();
        } else {
            throw new Exception("not implement");
        }
    }
}
