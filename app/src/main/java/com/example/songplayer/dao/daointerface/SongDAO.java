package com.example.songplayer.dao.daointerface;

import com.example.songplayer.db.entity.SongEntity;

import java.util.List;

public interface SongDAO {
    List<SongEntity> getAllSongs();
    void insert(SongEntity songEntity);
    void update(SongEntity songEntity);
    void delete(int ID);
}
