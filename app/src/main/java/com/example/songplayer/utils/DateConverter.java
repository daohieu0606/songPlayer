package com.example.songplayer.utils;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class DateConverter {
    @TypeConverter
    public static String a(Date date){
        return date.toString();
    }
    @TypeConverter
    public static Date toDate(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(date);
    }
}
