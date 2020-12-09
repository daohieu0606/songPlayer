package com.example.songplayer.dao;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import com.example.songplayer.db.entity.ArtistEntity;
import com.example.songplayer.utils.ArtistDbHelper;

import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {
    private MutableLiveData<List<ArtistEntity>> artistMutableLiveData;
    private Application application;
    private ArtistDbHelper artistDbHelper;

    public ArtistDAO(Application newApplication) {
        application = newApplication;
        artistMutableLiveData = new MutableLiveData<>();
        artistMutableLiveData.setValue(new ArrayList<>());

        artistDbHelper = new ArtistDbHelper(application);
        loadDefaultArtists();
    }

    private void loadDefaultArtists() {
        artistMutableLiveData.getValue().addAll(artistDbHelper.getAllArtists());
    }

    public void updateArtistList() {
        artistMutableLiveData.postValue(artistDbHelper.getAllArtists());
    }

    public MutableLiveData<List<ArtistEntity>> getAllArtists() {
        return artistMutableLiveData;
    }
}
