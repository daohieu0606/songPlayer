package com.example.songplayer.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.songplayer.db.FavoriteSongDbHelper;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.Genre;
import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaylistRelatedDbHelper {
    private static final String TAG = "TESST";
    private Application application;
    private FavoriteSongDbHelper favoriteSongDbHelper;

    public PlaylistRelatedDbHelper(Application newApplication) {
        application = newApplication;
        favoriteSongDbHelper = new FavoriteSongDbHelper(newApplication);
    }

    public HashMap<Genre, List<SongEntity>> scanAllGenres() {
        HashMap<Genre, List<SongEntity>> result = new HashMap<>();

        List<SongEntity> songEntities = new ArrayList<>();
        String[] songProjection = {
                MediaStore.Audio.Media._ID, //0
                MediaStore.Audio.Media.DISPLAY_NAME, // 1
//                MediaStore.Audio.Media.RELATIVE_PATH, // 2
                MediaStore.Audio.Media.SIZE, // 3
                MediaStore.Audio.Artists.ARTIST, // 5
                MediaStore.Audio.Albums.ALBUM, //6
                MediaStore.Audio.Media.GENRE_ID,//7
                MediaStore.Audio.Media.GENRE,// 8

        };
        ContentResolver resolver = application.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), songProjection, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SongEntity songEntity = new SongEntity();
//            songEntity.setId(cursor.getInt(0));
//            songEntity.setSongName(cursor.getString(1));
//            songEntity.setPath(cursor.getString(2));
//            songEntity.setSize(cursor.getInt(3));
//            songEntity.setArtist(cursor.getString(5));

            songEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            songEntity.setSongName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
//            songEntity.setPath(cursor.getString(2));
            songEntity.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
            songEntity.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));


            if (favoriteSongDbHelper.isExistFavoriteSong(songEntity.getId())) {
                songEntity.setFavorite(true);
            }

            Uri contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songEntity.getId());
            songEntity.setUriString(contentUri.toString());

            songEntities.add(songEntity);

            Genre genre = new Genre(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)));

            if (!result.containsKey(genre)) {
                result.put(genre, new ArrayList<>());
            }

            result.get(genre).add(songEntity);

            cursor.moveToNext();
        }

        cursor.close();
        return result;
    }

    public HashMap<AlbumEntity, List<SongEntity>> scanAllAlbums() {

        HashMap<AlbumEntity, List<SongEntity>> result = new HashMap<>();
        List<SongEntity> songEntities = new ArrayList<>();
        String[] songProjection = {
                MediaStore.Audio.Media._ID, //0
                MediaStore.Audio.Media.DISPLAY_NAME, // 1
                MediaStore.Audio.Media.SIZE, // 3
                MediaStore.Audio.Artists.ARTIST, // 5
                MediaStore.Audio.Albums.ALBUM, //6,
                MediaStore.Audio.Albums._ID // 7

        };
        ContentResolver resolver = application.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), songProjection, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SongEntity songEntity = new SongEntity();

            songEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            songEntity.setSongName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
            songEntity.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
            songEntity.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));

            if (favoriteSongDbHelper.isExistFavoriteSong(songEntity.getId())) {
                songEntity.setFavorite(true);
            }

            Uri contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songEntity.getId());
            songEntity.setUriString(contentUri.toString());

            songEntities.add(songEntity);

            AlbumEntity album = new AlbumEntity(
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));

            if (!result.containsKey(album)) {
                result.put(album, new ArrayList<>());
            }

            result.get(album).add(songEntity);

            cursor.moveToNext();
        }

        cursor.close();
        return result;
    }


}
