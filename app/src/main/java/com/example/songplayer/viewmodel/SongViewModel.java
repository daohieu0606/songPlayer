package com.example.songplayer.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.db.SongRepo;
import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;
import java.util.List;

public class SongViewModel extends AndroidViewModel {

    private SongRepo repository;
    private LiveData<List<SongEntity>> allOfSongs = new MutableLiveData<>(new ArrayList<>());
    private LiveData<List<SongEntity>> onlineSongs = new MutableLiveData<>(new ArrayList<>());


    public SongViewModel(@NonNull Application application) {
        super(application);
        repository = new SongRepo(application);
        allOfSongs.getValue().addAll(repository.getAllSongs());
    }

    public void insert(SongEntity songEntity) {
        repository.insert(songEntity);
    }

    public void update(SongEntity songEntity) {
        repository.update(songEntity);
    }

    public void delete(int ID) {
        repository.delete(ID);
        ((MutableLiveData) allOfSongs).setValue(repository.getAllSongs());
    }

    public LiveData<List<SongEntity>> getAllOfflineSongs() {
        return allOfSongs;
    }

    public LiveData<List<SongEntity>> getAllOnlineSongs() {
        return onlineSongs;
    }

    public LiveData<List<SongEntity>> getAllSongs() {
        List<SongEntity> songs = new ArrayList<>();
        songs.addAll(allOfSongs.getValue());
        songs.addAll(songs.size(), onlineSongs.getValue());

        return new MutableLiveData<>(songs);
    }
}
