package com.example.songplayer.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.songplayer.model.FavoriteSong;

import java.util.List;

@Dao
public interface FavoriteSongDao {
    /*@Query("SELECT * FROM FavoriteSong")
    List<FavoriteSong> getAll();

    @Query("SELECT * FROM FavoriteSong WHERE uid IN (:userIds)")
    List<FavoriteSong> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);*/
}
