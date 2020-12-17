package com.example.songplayer.dao;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Update;

import com.example.songplayer.db.entity.ArtistEntity;
import com.example.songplayer.utils.ArtistDbHelper;

import java.util.ArrayList;
import java.util.List;

@Dao
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

    @Update
    public void updateArtistList() {
        artistMutableLiveData.postValue(artistDbHelper.getAllArtists());
    }

    public MutableLiveData<List<ArtistEntity>> getAllArtists() {
        return artistMutableLiveData;
    }
}
