package com.example.songplayer.dao.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.example.songplayer.db.entity.ListMusicOfAlbum;

@Dao
public interface ListMusicOfPlaylistDAORoom {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ListMusicOfAlbum genre);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ListMusicOfAlbum genre);

}
