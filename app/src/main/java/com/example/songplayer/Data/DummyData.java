package com.example.songplayer.Data;

import com.example.songplayer.R;
import com.example.songplayer.db.entity.SongEntity;

import java.util.Arrays;
import java.util.List;

public class DummyData {
//    public static final List<AlbumEntity> albums = Arrays.asList(
//            new AlbumEntity("Love"),
//            new AlbumEntity("Animals"),
//            new AlbumEntity("Bollywoods"),
//            new AlbumEntity("Electronics"),
//            new AlbumEntity("Latin"),
//            new AlbumEntity("Notification"),
//            new AlbumEntity("World")
//
//    );

    public static final List<SongEntity> songs = Arrays.asList(
            new SongEntity(1,"Bollywood", "", "", 0, "", "", false),
            new SongEntity(2,
                    "Turkish", "", "", 0, "", "", true),
            new SongEntity(3,"Worlds and the nations", "", "", 0, "", "", false),
            new SongEntity(4,"Alarm soft", "", "", 0, "", "", false),
            new SongEntity(5,"Love me like you do", "", "", 0, "", "", false),
            new SongEntity(6,"Latin hope", "", "", 0, "", "", false),
            new SongEntity(7,"Cool tone", "", "", 0, "", "", false),
            new SongEntity(8,"Animal sound", "", "", 0, "", "", false)
//            new SongEntity("Forest bird", "", "", 0, "", "", false),
//            new SongEntity("Willy willy", "", "", 0, "", "", false),
//            new SongEntity("Senorita", "", "", 0, "", "", false),
//            new SongEntity("I miss you", "", "", 0, "", "", false),
//            new SongEntity("Royal french", "", "", 0, "", "", false),
//            new SongEntity("Some thing like that", "", "", 0, "", "", false),
//            new SongEntity("Brazil", "", "", 0, "", "", false),
//            new SongEntity("Nokia tune", "", "", 0, "", "", false),
//            new SongEntity("Notification", "", "", 0, "", "", false)
            );

    public static List<Integer> gradients = Arrays.asList(
            R.drawable.gradient1,
            R.drawable.gradient2,
            R.drawable.gradient3,
            R.drawable.gradient4,
            R.drawable.gradient5,
            R.drawable.gradient6,
            R.drawable.gradient7,
            R.drawable.gradient8,
            R.drawable.gradient9,
            R.drawable.gradient10,
            R.drawable.gradientnative,
            R.drawable.gradientpro

    );

    
}
