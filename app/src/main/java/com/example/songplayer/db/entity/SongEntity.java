package com.example.songplayer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.songplayer.model.MySong;

import java.io.Serializable;
import java.util.Random;

@Entity(tableName = "songs"
)
public class SongEntity implements MySong, Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "songName")
    private String songName;

    @ColumnInfo(name = "songNameURL")
    private String songNameURL;

    @ColumnInfo(name = "uriString")
    private String uriString;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "size")
    private double size;

    @ColumnInfo(name = "artist")
    private String artist;

    @ColumnInfo(name = "singer")
    private String singer;

    @ColumnInfo(name = "genre")
    private String genre;

    @ColumnInfo(name = "isFavorite",defaultValue = "0")
    private boolean isFavorite;

    @ColumnInfo(name = "isOnline", defaultValue = "0")
    private boolean isOnline;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }


    public SongEntity( String songName, String songNameURL, String uriString, String path,
                      double size, String artist, String singer, String genre,
                      boolean isFavorite, boolean isOnline) {
        this.id = Math.abs(new Random(System.currentTimeMillis()).nextInt());
        this.songName = songName;
        this.songNameURL = songNameURL;
        this.uriString = uriString;
        this.path = path;
        this.size = size;
        this.artist = artist;
        this.singer = singer;
        this.genre = genre;
        this.isFavorite = isFavorite;
        this.isOnline = isOnline;
    }

    public SongEntity() {
    }

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSongNameURL() {
        return songNameURL;
    }

    public void setSongNameURL(String songNameURL) {
        this.songNameURL = songNameURL;
    }
    @Override
    public String toString() {
        return "SongEntity{" +
                "id=" + id +
                ", songName='" + songName + '\'' +
                ", uriString='" + uriString + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", artist='" + artist + '\'' +
                ", singer='" + singer + '\'' +
                ", genre='" + genre + '\'' +
                ", isFavorite=" + isFavorite +
                ", isOnline=" + isOnline +
                '}';
    }
}
