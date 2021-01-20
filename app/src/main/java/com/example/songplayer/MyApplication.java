package com.example.songplayer;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.songplayer.db.MusicAppRoomDatabase;
import com.example.songplayer.db.OnlSongDatabase;
import com.example.songplayer.db.SongDatabase;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.ListMusicOfAlbum;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.utils.PlaylistRelatedDbHelper;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MyApplication extends Application {
    public static MusicAppRoomDatabase database;
    public static SongDatabase songDatabase;
    public static OnlSongDatabase onlSongDatabase;
    public static PlaylistRelatedDbHelper listDBHelper;
    public static Semaphore semaphore = new Semaphore(0);
    private static Context context;
    public String TAG = "TESST";

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        database = MusicAppRoomDatabase.getDatabase(this);
        songDatabase = SongDatabase.getInstance(this);
        onlSongDatabase = OnlSongDatabase.getInstance(this);
        listDBHelper = new PlaylistRelatedDbHelper(this);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.prev), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        final boolean firstLoad = preferences.getBoolean(getString(R.string.first_load), true);
        if (firstLoad) {

            fistLoadAction();
            editor.putBoolean(getString(R.string.first_load), false);
            editor.apply();
        }


    }


    public static Context getContext() {
        return MyApplication.context;
    }

    // Find all songs in sd card and insert information to local database OKE
    // Find all album and related album's song and insert to list
    // Find all genre and related genre's song and insert it to genre
    // Then load all data from room database

    public void fistLoadAction() {

        new Thread(() -> {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {

                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "fistLoadAction: continue run ");
            songDatabase.songDAO().getAllSongs().forEach(database.songDao()::insert);

            HashMap<AlbumEntity, List<SongEntity>> albums = listDBHelper.scanAllAlbums();
            albums.forEach((album, songs) -> {
                database.albumDAORoom().insert(album);

                songs.forEach((song) -> {
                    database.listMusicOfAlbumDAORoom().insert(new ListMusicOfAlbum(song.getId(), album.getId()));
                });

            });


//            HashMap<Genre, List<SongEntity>> genres = listDBHelper.scanAllGenres();
//            genres.forEach((genre, songs) -> {
//                database.genreDAORoom().insert(genre);
//                songs.forEach(database.songDao()::insert);
//            });
        }).start();


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        sdCardObserver.stopWatching();
        Log.d(TAG, "onTerminate: ");
    }

}
