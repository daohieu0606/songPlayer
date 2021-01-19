package com.example.songplayer.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.MyApplication;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;
import java.util.List;

public class SongViewModel extends AndroidViewModel {

    private LiveData<List<SongEntity>> allOfSongs = new MutableLiveData<>(new ArrayList<>());
    private LiveData<List<SongEntity>> onlineSongs = new MutableLiveData<>(new ArrayList<>());


    public SongViewModel(@NonNull Application application) {
        super(application);
    }

    public void insert(SongEntity songEntity) {

    }

    public void update(SongEntity songEntity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplication.database.songDao().update(songEntity);
            }
        }).start();
    }

    public void delete(int ID) {

    }

    public LiveData<List<SongEntity>> getAllOfflineSongs() {
        return MyApplication.database.songDao().getAllSongs();
    }

    public LiveData<List<SongEntity>> getAllOnlineSongs() {
        return onlineSongs;
    }

    public LiveData<List<SongEntity>> getAllSongs() {
        return MyApplication.database.songDao().getAllSongs();
    }

    public LiveData<List<SongEntity>> getAllSongOfAlbum(AlbumEntity album){
        return MyApplication.database.songDao().getAllSongsOfAlbum();
    }
}
