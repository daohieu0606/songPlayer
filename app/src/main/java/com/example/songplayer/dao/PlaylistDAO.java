package com.example.songplayer.dao;

import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.db.entity.PlaylistEntity;
import com.example.songplayer.db.entity.SongEntity;

import java.util.List;

public interface PlaylistDAO{
    //Get all playlist
    public MutableLiveData<List<PlaylistEntity>> getAllPlaylist();

    public void insert(PlaylistEntity playlistEntity);

    public void update(PlaylistEntity playlistEntity);

    //delete playlist
    public void delete(int ID);
}