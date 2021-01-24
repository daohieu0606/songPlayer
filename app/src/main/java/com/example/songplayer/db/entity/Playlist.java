package com.example.songplayer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Random;

@Entity(tableName = "playlists")
public class Playlist implements Serializable {

    @ColumnInfo(name = "playlistID")
    @PrimaryKey
    private int playlistID;

    @ColumnInfo(name = "playlistName")
    private String playListName;

    public Playlist(){}

    public Playlist(String name){
        playlistID = new Random(System.currentTimeMillis()).nextInt();
        this.playListName = name;

    }

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }
}
