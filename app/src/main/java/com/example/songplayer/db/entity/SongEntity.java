package com.example.songplayer.db.entity;

import com.example.songplayer.model.MySong;

import java.io.Serializable;
import java.util.Random;

public class SongEntity implements MySong, Serializable {

    private int id;
    private String songName;
    private String uriString;
    private String path;
    private double size;
    private String artist;
    private String singer;
    private boolean isFavorite;
    private boolean isOnline;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public SongEntity() {
        Random random = new Random();
        songName = String.valueOf(random.nextDouble());
        id = random.nextInt();
        isOnline = false;
    }

    public SongEntity(String songName, String uriString, String path, double size, String artist, String singer, boolean isFavorite) {
        Random random = new Random();
        this.id = random.nextInt();
        this.songName = songName;
        this.uriString = uriString;
        this.path = path;
        this.size = size;
        this.artist = artist;
        this.singer = singer;
        this.isFavorite = isFavorite;
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
                ", isFavorite=" + isFavorite +
                ", isOnline=" + isOnline +
                '}';
    }
}
