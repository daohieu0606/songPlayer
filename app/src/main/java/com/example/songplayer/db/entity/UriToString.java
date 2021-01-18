package com.example.songplayer.db.entity;

import android.net.Uri;

import androidx.room.TypeConverter;

public class UriToString {
    @TypeConverter
    public static String uriToString(Uri uri){
        return uri.toString();
    }

    @TypeConverter
    public static Uri uriToString(String data){
        return Uri.parse(data);
    }

}
