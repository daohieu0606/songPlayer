package com.example.songplayer.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;
import java.util.List;

public class SongDbHelper {
    private static final String TAG = "TESST";
    private Application application;

    public SongDbHelper(Application newApplication) {
        application = newApplication;
    }

    public List<SongEntity> getAllSongs() {
        List<SongEntity> songEntities = new ArrayList<>();

        String []songProjection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.RELATIVE_PATH,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATE_TAKEN,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Albums.ALBUM
               };
        ContentResolver resolver = application.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), songProjection,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SongEntity songEntity = new SongEntity();

            songEntity.setId(cursor.getInt(0));
            songEntity.setSongName(cursor.getString(1));
            songEntity.setPath(cursor.getString(2));
            songEntity.setSize(cursor.getInt(3));

            Uri contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songEntity.getId());
            songEntity.setUriString(contentUri.toString());

            songEntities.add(songEntity);
            cursor.moveToNext();
        }
        cursor.close();
        return songEntities;
    }

    public void insert(SongEntity songEntity) {

    }

    public void delete(SongEntity songEntity) {
        FileHelper.removeFile(application, songEntity.getUriString());
    }

    public void updateSong(SongEntity songEntity) {

    }
}
