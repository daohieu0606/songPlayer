package com.example.songplayer.db.entity;

import android.net.Uri;

import java.util.List;
import java.util.Random;

public class PlaylistEntity {
    private int id;
    private String playlistName;
    private Uri playlistUri;
    private List<Integer> listSongID;

    public PlaylistEntity(String playlistName, Uri playlistUri, List<Integer> listSongID) {
        Random random = new Random();
        this.id = Math.abs(random.nextInt());
        this.playlistName = playlistName;
        this.playlistUri = playlistUri;
        this.listSongID = listSongID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Uri getPlaylistUri() {
        return playlistUri;
    }

    public void setPlaylistUri(Uri playlistUri) {
        this.playlistUri = playlistUri;
    }

    public List<Integer> getListSong() {
        return listSongID;
    }

    public void setListSong(List<Integer> listSong) {
        this.listSongID = listSongID;
    }
}
