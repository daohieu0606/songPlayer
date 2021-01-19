package com.example.songplayer.dao.daointerface;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.songplayer.model.FavoriteSong;

import java.util.List;

@Dao
public interface FavoriteSongDao {
    @Query("SELECT * FROM FavoriteSong")
    List<FavoriteSong> getAll();

   /* @Query("SELECT * FROM FavoriteSong WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    FavoriteSong findById(int id);

    @Insert
    void insertAll(FavoriteSong... favoriteSongs);

    @Delete
    void delete(FavoriteSong favoriteSong);*/
}
