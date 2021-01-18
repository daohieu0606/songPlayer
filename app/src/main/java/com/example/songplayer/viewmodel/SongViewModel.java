package com.example.songplayer.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.db.SongRepo;

import java.util.ArrayList;
import java.util.List;

public class SongViewModel extends AndroidViewModel {

    private SongRepo repository;
    private MutableLiveData<List<SongEntity>> allOfSongs;
    private MutableLiveData<List<SongEntity>> onlineSongs;

    public SongViewModel(@NonNull Application application) {
        super(application);
        repository = new SongRepo(application);
        allOfSongs = repository.getAllSongs();
        onlineSongs = repository.getAllOnlineSongs();
    }

    public void insert(SongEntity songEntity) {
        repository.insert(songEntity);
    }

    public void update(SongEntity songEntity) {
        repository.update(songEntity);
    }

    public void delete(int ID) {
        repository.delete(ID);
        allOfSongs = repository.getAllSongs();
    }

    public MutableLiveData<List<SongEntity>> getAllOfflineSongs() {
        MutableLiveData<List<SongEntity>> songs = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        return allOfSongs;
    }

    public LiveData<List<SongEntity>> getAllOnlineSongs() {
        return onlineSongs;
    }

    public LiveData<List<SongEntity>> getAllSongs(){
        List<SongEntity> songs = new ArrayList<>();
        songs.addAll(allOfSongs.getValue());
        songs.addAll(songs.size(),onlineSongs.getValue());

        return new MutableLiveData<>(songs);
    }
}
