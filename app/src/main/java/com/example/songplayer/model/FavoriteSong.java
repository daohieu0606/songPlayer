package com.example.songplayer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteSong {
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    public int songId;

    public FavoriteSong(int newId) {
        songId = newId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
