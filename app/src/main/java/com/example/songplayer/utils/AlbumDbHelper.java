package com.example.songplayer.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import com.example.songplayer.db.entity.AlbumEntity;

import java.util.ArrayList;
import java.util.List;

public class AlbumDbHelper {
    private static final String TAG = "TESST";
    private Application application;

    public AlbumDbHelper(Application newApplication) {
        application = newApplication;
    }

    public List<AlbumEntity> getAllAlbums() {
        List<AlbumEntity> albumEntities = new ArrayList<>();
        String [] albumProjection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
        };
        ContentResolver resolver = application.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Albums.getContentUri(MediaStore.VOLUME_EXTERNAL), albumProjection,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.d(TAG, "getAllAlbums: " + cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
            Log.d(TAG, "getAllAlbums: " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));

            AlbumEntity albumEntity = new AlbumEntity();
            albumEntity.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
            albumEntity.setAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));

            albumEntities.add(albumEntity);
            cursor.moveToNext();
        }
        cursor.close();
        return albumEntities;
    }

    //don't create insert - delete - update function
}
