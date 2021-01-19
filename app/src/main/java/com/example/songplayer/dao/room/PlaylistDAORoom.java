package com.example.songplayer.dao.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.songplayer.db.entity.Playlist;

import java.util.List;

@Dao
public interface PlaylistDAORoom {

    @Query("select * from playlists")
    LiveData<List<Playlist>> getAllPlaylist();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Playlist playlist);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Playlist playlist);

    @Query("delete from playlists where playlistID= :ID")
    void delete(int ID);
}
