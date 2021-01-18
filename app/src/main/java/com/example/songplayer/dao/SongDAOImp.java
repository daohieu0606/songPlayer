package com.example.songplayer.dao;

import android.app.Application;

import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.utils.SongDbHelper;

import java.util.ArrayList;
import java.util.List;

public class SongDAOImp implements SongDAO{
    private static final String TAG = "TESST";
    private List<SongEntity> listMutableLiveData;
    private Application context;
    private SongDbHelper songDbHelper;

    public SongDAOImp(Application newContext) {
        context = newContext;
        listMutableLiveData = new ArrayList<>();

        songDbHelper = new SongDbHelper(context);
        loadDefaultSongList();
    }

    private void loadDefaultSongList() {
        List<SongEntity> songEntityList = songDbHelper.getAllSongs();
        listMutableLiveData.addAll(songEntityList);
    }

    @Override
    public List<SongEntity> getAllSongs() {
        return listMutableLiveData;
    }

    @Override
    public void delete(int ID) {
        SongEntity delSong = null;
        for (SongEntity tmpSong :
                listMutableLiveData) {
            if (tmpSong.getId() == ID) {
                delSong = tmpSong;
                break;
            }
        }
        songDbHelper.delete(delSong);
        listMutableLiveData = songDbHelper.getAllSongs();

    }

    @Override
    public void insert(SongEntity songEntity) {
        songDbHelper.insert(songEntity);
        listMutableLiveData = songDbHelper.getAllSongs();
    }

    @Override
    public void update(SongEntity songEntity) {
        songDbHelper.updateSong(songEntity);
        listMutableLiveData = songDbHelper.getAllSongs();
    }
}
