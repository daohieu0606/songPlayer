package com.example.songplayer.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.songplayer.dao.AlbumDAORoom;
import com.example.songplayer.dao.ArtistDaoRoom;
import com.example.songplayer.dao.SongDAO;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.ArtistEntity;
import com.example.songplayer.db.entity.SongEntity;

@Database(entities = {ArtistEntity.class, AlbumEntity.class, SongEntity.class},version = 1,exportSchema = false)
public abstract class MusicAppRoomDatabase  extends RoomDatabase{
    private static MusicAppRoomDatabase INSTANCE = null;
    public abstract AlbumDAORoom albumDAORoom();
    public abstract ArtistDaoRoom artistDAORoom();
    public abstract SongDAO songDao();

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
    public static MusicAppRoomDatabase getDatabase(final Application app){
        if(INSTANCE == null){
            synchronized (MusicAppRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(app,MusicAppRoomDatabase.class,"MUSIC_APP")
                            .fallbackToDestructiveMigration().addCallback(callback).build();
                }
            }
        }
        return INSTANCE;
    }

}
