package com.example.songplayer.dao;

import android.app.Application;

import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.utils.AlbumDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {
    private List<AlbumEntity> albums;
    private AlbumDbHelper albumDbHelper;

    public AlbumDAO(Application app) {
        albums = new ArrayList<>();
        albumDbHelper = new AlbumDbHelper(app);
        scanDefaultAlbumsFromDisk();
    }

    public void scanDefaultAlbumsFromDisk() {
        albums.addAll(albumDbHelper.getAllAlbums());
    }

    public void updateAlbumList() {
        albums = albumDbHelper.getAllAlbums();
    }

    public List<AlbumEntity> getLoadedAlbums() {
        return albums;
    }
}
