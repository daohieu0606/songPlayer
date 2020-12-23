package com.example.songplayer.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteSong {
    @PrimaryKey
    public int SongId;
}
