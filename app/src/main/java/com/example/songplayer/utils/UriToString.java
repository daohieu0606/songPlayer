package com.example.songplayer.utils;

import android.net.Uri;

import androidx.room.TypeConverter;

public class UriToString {
    @TypeConverter
    public static String uriToString(Uri uri){
        if(uri == null ) return "";
        return uri.toString();
    }

    @TypeConverter
    public static Uri uriToString(String data){
        return Uri.parse(data);
    }

}
