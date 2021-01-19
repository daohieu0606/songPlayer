package com.example.songplayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.songplayer.MyApplication;
import com.example.songplayer.dao.room.AlbumDAORoom;
import com.example.songplayer.db.entity.AlbumEntity;

import java.util.List;

public class AlbumViewModel extends AndroidViewModel {

    AlbumDAORoom albumDAORoom = MyApplication.database.albumDAORoom();
    public AlbumViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<AlbumEntity>> getAllAlbums() {
        return albumDAORoom.getAllAlbums();
    }
}
