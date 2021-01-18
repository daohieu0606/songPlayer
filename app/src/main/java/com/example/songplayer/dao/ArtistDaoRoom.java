package com.example.songplayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.songplayer.db.entity.ArtistEntity;

import java.util.List;

@Dao
public interface ArtistDaoRoom {

    @Query("select * from artists")
    LiveData<List<ArtistEntity>> getAllArtist();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ArtistEntity album);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ArtistEntity albumEntity);

    @Query("delete from albums where id = :ID")
    void delete(int ID);

}
