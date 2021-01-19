package com.example.songplayer.db;

import android.app.Application;

import com.example.songplayer.MyApplication;
import com.example.songplayer.dao.daoimpl.OnlSongDAOImp;
import com.example.songplayer.dao.daoimpl.callback.Callback;
import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;

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

    public OnlSongDAOImp songDAO() {
        return onlSongDAOImp;
    }

    public void fetchOnlineSongs() {
        onlSongDAOImp.fetchOnlineSongs(
                new Callback() {
                    @Override
                    public void done(ArrayList<SongEntity> songs) {
                        songs.forEach(MyApplication.database.songDao()::insert);
                    }
                }
        );
    }
}
