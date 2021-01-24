package com.example.songplayer.dao.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.example.songplayer.db.entity.ListMusicOfAlbum;

@Dao
public interface ListMusicOfAlbumDAORoom {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ListMusicOfAlbum album);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ListMusicOfAlbum album);

}
