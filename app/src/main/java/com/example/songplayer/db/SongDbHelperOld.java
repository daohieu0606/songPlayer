package com.example.songplayer.db;


import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;
import java.util.List;

/*
* Khong dung` class nay`
* */
public class SongDbHelperOld extends SQLiteOpenHelper {
    private static final String TAG = "SongDbHelper";
    private static final String DATABASE_NAME = "song_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SONG = "song";
    private Application application;

    public SongDbHelperOld(@Nullable Application context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(TAG, "Create table");
        String queryCreateTable = "CREATE TABLE " + TABLE_SONG + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "song_name NVARCHAR (255) NOT NULL, " +
                "song_uri VARCHAR (255), " +
                "path VARCHAR (255), " +
                "artist NVARCHAR (255), " +
                "singer NVARCHAR (255), " +
                "size INTEGER" +
                ")";
        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        onCreate(db);
    }


    public List<SongEntity> getAllSongs() {

        List<SongEntity> songs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from song", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SongEntity songEntity = new SongEntity();

            songEntity.setId(cursor.getInt(0));
            songEntity.setSongName(cursor.getString(1));
            songEntity.setUriString(cursor.getString(2));
            songEntity.setPath(cursor.getString(3));
            songEntity.setArtist(cursor.getString(4));
            songEntity.setSinger(cursor.getString(5));
            songEntity.setSize(cursor.getInt(6));

            songs.add(songEntity);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return songs;
    }

    public void insert(SongEntity songEntity) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO song(id, song_name, song_uri, path, artist, singer, size) VALUES (?, ?, ?, ?, ?, ?, ?)",
                new String[]{String.valueOf(songEntity.getId()), songEntity.getSongName(), songEntity.getUriString(),
                        songEntity.getPath(), songEntity.getArtist(),
                        songEntity.getSinger(),
                        String.valueOf(songEntity.getSize())});
        db.close();
    }

    public SongEntity getSongByID(int ID) {
        SongEntity songEntity = null;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from song where id = ?",
                new String[]{String.valueOf(ID)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            songEntity = new SongEntity();

            songEntity.setId(cursor.getInt(0));
            songEntity.setSongName(cursor.getString(1));
            songEntity.setUriString(cursor.getString(2));
            songEntity.setPath(cursor.getString(3));
            songEntity.setArtist(cursor.getString(4));
            songEntity.setSinger(cursor.getString(5));
            songEntity.setSize(cursor.getInt(6));
        }
        cursor.close();
        db.close();
        return songEntity;
    }

    public void deleteSongByID(int ID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM song where id = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    public void updateSong(SongEntity songEntity) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE song SET song_name=?, song_uri = ?, path = ?, artist = ?, singer = ?, size = ?" +
                        " where id = ?",
                new String[]{songEntity.getSongName(), songEntity.getUriString(),
                        songEntity.getPath(), songEntity.getArtist(),
                        songEntity.getSinger(),
                        String.valueOf(songEntity.getSize()),
                        String.valueOf(songEntity.getId())});
        db.close();
    }
}