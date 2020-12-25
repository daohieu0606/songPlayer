package com.example.songplayer.db;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.model.FavoriteSong;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSongDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "FavoriteSongDbHelper";
    private static final String DATABASE_NAME = "song.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SONG = "favorite_song";

    public FavoriteSongDbHelper(@Nullable Application context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(TAG, "Create table");
        String queryCreateTable = "CREATE TABLE " + TABLE_SONG + " ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT " +
                ")";
        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        onCreate(db);
    }


    public List<FavoriteSong> getAllFavoriteSongs() {

        List<FavoriteSong> favoriteSongs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from favorite_song", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            FavoriteSong favoriteSong = new FavoriteSong(cursor.getInt(0));
            favoriteSongs.add(favoriteSong);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return favoriteSongs;
    }

    public void insert(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO favorite_song(id) VALUES (?)",
                new String[]{String.valueOf(id)});
        db.close();
    }


    public void deleteFavoriteSongByID(int ID) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM favorite_song where id = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    public boolean isExistFavoriteSong(int ID) {
        boolean isExist = false;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from favorite_song where id = ?",
                new String[]{String.valueOf(ID)});

        if (cursor.getCount() > 0) {
            isExist = true;
        }
        cursor.close();
        db.close();
        return isExist;
    }
}