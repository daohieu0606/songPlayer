package com.example.songplayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.dao.AlbumDAO;
import com.example.songplayer.db.entity.AlbumEntity;

import java.util.ArrayList;
import java.util.List;

public class AlbumViewModel extends AndroidViewModel {

    AlbumDAO albumDAO;

    public AlbumViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<AlbumEntity>> getAllAlbums() {

        MutableLiveData<List<AlbumEntity>> albums = new MutableLiveData<>(new ArrayList<>());

        Thread thread = new Thread(() -> {
            albumDAO = new AlbumDAO(getApplication());
            albums.postValue(albumDAO.getLoadedAlbums());
        });

        thread.start();
        return albums;
    }
}
