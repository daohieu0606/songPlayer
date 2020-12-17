package com.example.songplayer.db.entity;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlbumEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "albumName")
    private String albumName;
    @ColumnInfo(name = "artUri")
    private Uri artUri;

    public Uri getArtUri() {
        return artUri;
    }

    public void setArtUri(Uri artUri) {
        this.artUri = artUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

}
