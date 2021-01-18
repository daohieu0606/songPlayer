package com.example.songplayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.dao.ArtistDAO;
import com.example.songplayer.db.entity.ArtistEntity;

import java.util.ArrayList;
import java.util.List;

public class ArtistViewModel extends AndroidViewModel {
    ArtistDAO artistDAO;

    public ArtistViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<ArtistEntity>> getAllArtists() {
        MutableLiveData<List<ArtistEntity>> artists = new MutableLiveData<>(new ArrayList<>());

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (artistDAO == null) {
                    artistDAO = new ArtistDAO(getApplication());
                }
                artists.postValue(artistDAO.getLoadedArtists());
            }
        }).start();

        return artists;
    }

    public MutableLiveData<List<ArtistEntity>> getAllArtists(boolean updateNewList) {
        if (!updateNewList) {
            MutableLiveData<List<ArtistEntity>> artists = new MutableLiveData<>(new ArrayList<>());
            if (artistDAO != null) {
                artists.setValue(artistDAO.getLoadedArtists());
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        artistDAO = new ArtistDAO(getApplication());
                        artists.postValue(artistDAO.getLoadedArtists());
                    }
                }).start();
            }
            return artists;
        } else {
            return getAllArtists();
        }
    }


}
