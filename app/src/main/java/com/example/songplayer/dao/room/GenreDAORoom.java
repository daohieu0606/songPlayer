package com.example.songplayer.dao.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.songplayer.db.entity.Genre;

import java.util.List;

@Dao
public interface GenreDAORoom {
    @Query("select * from genres")
    LiveData<List<Genre>> getAllGenre();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Genre genre);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Genre genre);

    @Query("delete from genres where genreID= :ID")
    void delete(int ID);
}
