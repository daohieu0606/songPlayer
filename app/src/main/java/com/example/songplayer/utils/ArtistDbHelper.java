package com.example.songplayer.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.songplayer.db.entity.ArtistEntity;

import java.util.ArrayList;
import java.util.List;

public class ArtistDbHelper {
    private static final String TAG = "TESST";
    private Application application;

    public ArtistDbHelper(Application newApplication) {
        application = newApplication;
    }

    public List<ArtistEntity> getAllArtists() {
        List<ArtistEntity> artistEntities = new ArrayList<>();
        String [] artistProjection = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
        };
        ContentResolver resolver = application.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Artists.getContentUri(MediaStore.VOLUME_EXTERNAL), artistProjection,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            /*Log.d(TAG, "getAllArtists: " + cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)));
            Log.d(TAG, "getAllArtists: " + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));*/

            ArtistEntity artistEntity = new ArtistEntity();
            artistEntity.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)));
            artistEntity.setComposerName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));

            artistEntities.add(artistEntity);
            cursor.moveToNext();
        }
        cursor.close();
        return artistEntities;
    }

    //don't create insert - delete - update function
}
