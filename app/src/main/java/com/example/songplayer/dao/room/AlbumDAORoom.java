package com.example.songplayer.dao.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.songplayer.db.entity.AlbumEntity;

import java.util.List;

@Dao
public interface AlbumDAORoom {

    @Query("select * from albums")
    LiveData<List<AlbumEntity>> getAllAlbums();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AlbumEntity album);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(AlbumEntity albumEntity);

    @Query("delete from albums where id = :ID")
    void delete(int ID);

    @Query("delete from albums")
    void deleteAll();

}
