package com.example.songplayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.dao.AlbumDAO;
import com.example.songplayer.db.entity.AlbumEntity;

import java.util.List;

public class AlbumViewModel extends AndroidViewModel {

    AlbumDAO albumDAO;

    public AlbumViewModel(@NonNull Application application) {
        super(application);

        albumDAO = new AlbumDAO(application);
    }

    public MutableLiveData<List<AlbumEntity>> getAllAlbums() {
        return albumDAO.getAllAlbums();
    }
}
