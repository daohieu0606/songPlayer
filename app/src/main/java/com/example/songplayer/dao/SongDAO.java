package com.example.songplayer.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.songplayer.db.entity.SongEntity;

import java.util.List;
@Dao
public interface SongDAO {

    @Query("select * from songs")
    LiveData<List<SongEntity>> getAllSongs();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SongEntity songEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(SongEntity songEntity);

    @Query("delete from songs where id = :ID")
    void delete(int ID);
    public List<SongEntity> getAllSongs(boolean pure);
}
