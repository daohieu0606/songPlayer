package com.example.songplayer.viewmodel;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.db.SongRepo;

import java.util.List;

public class SongViewModel extends AndroidViewModel {

    private SongRepo repository;
    private MutableLiveData<List<SongEntity>> allSongs;

    public SongViewModel(@NonNull Application application) {
        super(application);
        repository = new SongRepo(application);
        allSongs = repository.getAllSongs();
    }

    public void insert(SongEntity songEntity) {
        repository.insert(songEntity);
    }

    public void update(SongEntity songEntity) {
        repository.update(songEntity);
    }

    public void delete(int ID) {
        repository.delete(ID);
        allSongs = repository.getAllSongs();
    }

/*    public void deleteAllSongs() {
        repository.deleteAllSongs();
    }*/

    public MutableLiveData<List<SongEntity>> getAllSongs(){
        return allSongs;
    }
}
