package com.example.songplayer.db.entity;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.songplayer.utils.UriToString;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Random;

@Entity(tableName = "albums")
@TypeConverters({UriToString.class})
public class AlbumEntity  implements Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "albumName")
    private String albumName;

    @ColumnInfo(name = "artUri")
    private Uri artUri;

    public AlbumEntity() {
    }
//
//    public AlbumEntity(String albumName) {
//        this.albumName = albumName;
//    }

    public AlbumEntity(String albumName) {
        this.albumName = albumName;
        this.id = Math.abs(new Random(System.currentTimeMillis()).nextInt());;
    }
    public AlbumEntity(int id, String albumName) {
        this.albumName = albumName;
        this.id =id;
    }


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

    @NotNull
    @Override
    public String toString() {
        return "AlbumEntity{" +
                "id=" + id +
                ", albumName='" + albumName + '\'' +
                ", artUri=" + artUri +
                '}';
    }
}

