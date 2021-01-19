package com.example.songplayer.dao.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.songplayer.db.entity.SongEntity;

import java.util.List;
@Dao
public interface SongDAORoom {

    @Query("select * from songs")
    LiveData<List<SongEntity>> getAllSongs();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SongEntity songEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(SongEntity songEntity);

    @Query("delete from songs where id = :ID")
    void delete(int ID);

    @Query("delete from songs")
    void deleteAll();

    @Query("select s.* from listMusicOfPlaylist l join songs s on l.songID = s.id where playlistID=:playlistID")
    LiveData<List<SongEntity>> getAllSongsOfPlaylist(int playlistID);

    @Query("delete from listMusicOfPlaylist where songID= :songID and playlistID= :playlistID")
    void deleteFromPlayList(int songID,int playlistID);

    @Query("select s.* from listMusicOfAlbum l join songs s on l.songID = s.id where albumID=:albumID")
    LiveData<List<SongEntity>> getAllSongsOfAlbum(int albumID);



    @Query("delete from listMusicOfAlbum where songID= :songID and albumID=:albumID")
    void deleteFromAlbum(int songID,int albumID);
}
