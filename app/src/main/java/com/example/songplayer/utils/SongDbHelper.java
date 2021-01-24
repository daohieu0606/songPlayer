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

        String[] songProjection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
//                MediaStore.Audio.Media.RELATIVE_PATH,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Albums.ALBUM,

        };
        ContentResolver resolver = application.getContentResolver();
//        Cursor cursor = resolver.query(MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY), songProjection, null, null);
//        Uri uri = Uri.parse("content://media/external/audio/media");
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songProjection, null, null);
//        Cursor cursor = resolver.query(uri, songProjection, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            if (cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).endsWith(".ogg")) {
                continue;
            }

            SongEntity songEntity = new SongEntity();

            songEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));

            songEntity.setSongName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
            songEntity.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
            songEntity.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));

            Uri contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songEntity.getId());
            songEntity.setUriString(contentUri.toString());

            songEntity.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));

            songEntities.add(songEntity);
            cursor.moveToNext();
        }
        cursor.close();
        return songEntities;
    }

    //    public String getRealPathFromURI(Context context, Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Audio.Media.DATA };
//            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        songDetails.clear();
        songDetails.put(MediaStore.Audio.Media.IS_PENDING, 0);
        resolver.update(songContentUri, songDetails, null, null);


    }

    public void delete(SongEntity songEntity) {

        FileHelper.removeFile(application, songEntity.getUriString());
    }

    public void updateSong(SongEntity songEntity) {

        songEntity.setFavorite(!songEntity.isFavorite());
    }


}
