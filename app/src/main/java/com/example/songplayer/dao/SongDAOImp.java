package com.example.songplayer.dao;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.songplayer.utils.SongDbHelper;
import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;
import java.util.List;

public class SongDAOImp implements SongDAO{
    private static final String TAG = "TESST";
    private MutableLiveData<List<SongEntity>> listMutableLiveData;
    private Application context;
    private SongDbHelper songDbHelper;

    public SongDAOImp(Application newContext) {
        context = newContext;
        listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.setValue(new ArrayList<>());

        songDbHelper = new SongDbHelper(context);
        loadDefaultSongList();
    }

    private void loadDefaultSongList() {
        List<SongEntity> songEntityList = songDbHelper.getAllSongs();
        listMutableLiveData.getValue().addAll(songEntityList);
    }

    public MutableLiveData<List<SongEntity>> getAllSongs() {
        return listMutableLiveData;
    }

    @Delete
    public void delete(int ID) {
        SongEntity delSong = null;
        for (SongEntity tmpSong :
                listMutableLiveData.getValue()) {
            if (tmpSong.getId() == ID) {
                delSong = tmpSong;
                break;
            }
        }
        songDbHelper.delete(delSong);
        listMutableLiveData.postValue(songDbHelper.getAllSongs());
    }

    @Insert
    public void insert(SongEntity songEntity) {
        songDbHelper.insert(songEntity);
        listMutableLiveData.postValue(songDbHelper.getAllSongs());
    }

    @Update
    public void update(SongEntity songEntity) {
        songDbHelper.updateSong(songEntity);
        listMutableLiveData.postValue(songDbHelper.getAllSongs());
    }
}
