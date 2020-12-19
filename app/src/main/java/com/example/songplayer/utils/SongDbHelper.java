package com.example.songplayer.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.example.songplayer.db.entity.SongEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        ContentResolver resolver = application.getContentResolver();

        Uri audioCollection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            audioCollection = MediaStore.Audio.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            audioCollection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues songDetails = new ContentValues();
        songDetails.put(MediaStore.Audio.Media.DISPLAY_NAME,
                songEntity.getSongName());
        songDetails.put(MediaStore.Audio.Media.IS_PENDING, 1);

        Uri songContentUri = resolver
                .insert(audioCollection, songDetails);

        try (ParcelFileDescriptor pfd =
                     resolver.openFileDescriptor(songContentUri, "w", null)) {
            // Write data into the pending audio file.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

// Now that we're finished, release the "pending" status, and allow other apps
// to play the audio track.
        songDetails.clear();
        songDetails.put(MediaStore.Audio.Media.IS_PENDING, 0);
        resolver.update(songContentUri, songDetails, null, null);

    }

    public void delete(SongEntity songEntity) {
        FileHelper.removeFile(application, songEntity.getUriString());
    }

    public void updateSong(SongEntity songEntity) {

    }
}
