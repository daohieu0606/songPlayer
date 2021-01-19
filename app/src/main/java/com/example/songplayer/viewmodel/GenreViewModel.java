package com.example.songplayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.songplayer.MyApplication;
import com.example.songplayer.db.entity.Genre;

import java.util.List;

public class GenreViewModel extends AndroidViewModel {

    public GenreViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Genre>> getAllGenres() {
        return MyApplication.database.genreDAORoom().getAllGenre();
    }
}