package com.example.songplayer.dao.daointerface;

import android.app.Application;

import com.example.songplayer.db.entity.ArtistEntity;
import com.example.songplayer.utils.ArtistDbHelper;

import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {
    private List<ArtistEntity> artistMutableLiveData;
    private ArtistDbHelper artistDbHelper;
    public ArtistDAO(Application app) {
        artistMutableLiveData = new ArrayList<>();

        artistDbHelper = new ArtistDbHelper(app);
        loadDefaultArtistsFromDisk();
    }

    public void loadDefaultArtistsFromDisk() {
        artistMutableLiveData.addAll(artistDbHelper.getAllArtists());
    }

    public void updateArtistList() {
        artistMutableLiveData = artistDbHelper.getAllArtists();
    }

    public List<ArtistEntity> getLoadedArtists() {
        return artistMutableLiveData;
    }
}
