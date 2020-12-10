package com.example.songplayer.dao;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.utils.AlbumDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {
    private MutableLiveData<List<AlbumEntity>> albumMutableLiveData;
    private Application application;
    private AlbumDbHelper albumDbHelper;

    public AlbumDAO(Application newApplication) {
        application = newApplication;
        albumMutableLiveData = new MutableLiveData<>();
        albumMutableLiveData.setValue(new ArrayList<>());

        albumDbHelper = new AlbumDbHelper(application);
        loadDefaultAlbums();
    }

    private void loadDefaultAlbums() {
        albumMutableLiveData.getValue().addAll(albumDbHelper.getAllAlbums());
    }

    public void updateAlbumList() {
        albumMutableLiveData.postValue(albumDbHelper.getAllAlbums());
    }

    public MutableLiveData<List<AlbumEntity>> getAllAlbums() {
        return albumMutableLiveData;
    }
}
