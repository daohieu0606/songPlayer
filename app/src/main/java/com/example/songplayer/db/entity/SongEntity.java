package com.example.songplayer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.songplayer.model.MySong;

import java.util.Random;

@Entity
public class SongEntity implements MySong {

    public SongEntity() {
        Random random = new Random();
        songName = String.valueOf(random.nextDouble());
        id = random.nextInt();
    }
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "song_name")
    private String songName;

    @ColumnInfo(name = "URI")
    private String uriString;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "size")
    private double size;

    @ColumnInfo(name = "artist")
    private String artist;

    @ColumnInfo(name = "singer")
    private String singer;

    @ColumnInfo(name = "isFavorite")
    private boolean isFavorite;

    @Override
    public void setSongName(String newName) {
        songName = newName;
    }

    @Override
    public void setUriString(String newUriString) {
        uriString = newUriString;
    }

    @Override
    public String getSongName() {
        return songName;
    }

    @Override
    public String getUriString() {
        return uriString;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public double getSize() {
        return size;
    }

    @Override
    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String getSinger() {
        return singer;
    }

    @Override
    public void setSinger(String singer) {
        this.singer = singer;
    }

    @Override
    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
