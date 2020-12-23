package com.example.songplayer.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteSong {
    @PrimaryKey
    @ColumnInfo(name = "song_id")
    public int SongId;
}
