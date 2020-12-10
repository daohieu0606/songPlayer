package com.example.songplayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.dao.ArtistDAO;
import com.example.songplayer.db.entity.ArtistEntity;

import java.util.List;

public class ArtistViewModel extends AndroidViewModel {
    ArtistDAO artistDAO;

    public ArtistViewModel(@NonNull Application application) {
        super(application);

        artistDAO = new ArtistDAO(application);
    }

    public MutableLiveData<List<ArtistEntity>> getAllArtists() {
        return artistDAO.getAllArtists();
    }
}
