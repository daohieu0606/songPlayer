package com.example.songplayer.dao;

import com.example.songplayer.db.entity.SongEntity;

import java.util.List;

public interface SongDAO {
    public List<SongEntity> getAllSongs();
    public void insert(SongEntity songEntity);
    public void update(SongEntity songEntity);
    public void delete(int ID);
}
