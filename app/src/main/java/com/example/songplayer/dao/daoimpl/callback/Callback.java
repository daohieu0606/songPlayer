package com.example.songplayer.dao.daoimpl.callback;

import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;

@FunctionalInterface
public  interface Callback{
    void done(ArrayList<SongEntity> data);
}