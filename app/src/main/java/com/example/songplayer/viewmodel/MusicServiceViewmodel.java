package com.example.songplayer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.db.entity.SongEntity;

public class MusicServiceViewmodel extends AndroidViewModel {
    private MutableLiveData<SongEntity> currentSong = new MutableLiveData<>();

    public MusicServiceViewmodel(@NonNull Application application) {
        super(application);
        currentSong = null;
    }

    public MutableLiveData<SongEntity> getCurrentSong() {
        return currentSong;
    }

    public void changeCurrentSong(SongEntity newSong) {
        currentSong.setValue(newSong);
    }
}
