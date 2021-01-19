package com.example.songplayer.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.songplayer.dao.room.AlbumDAORoom;
import com.example.songplayer.dao.room.ArtistDaoRoom;
import com.example.songplayer.dao.room.GenreDAORoom;
import com.example.songplayer.dao.room.ListMusicOfAlbumDAORoom;
import com.example.songplayer.dao.room.ListMusicOfPlaylistDAORoom;
import com.example.songplayer.dao.room.PerformerDAORoom;
import com.example.songplayer.dao.room.PlaylistDAORoom;
import com.example.songplayer.dao.room.SongDAORoom;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.ArtistEntity;
import com.example.songplayer.db.entity.Genre;
import com.example.songplayer.db.entity.ListMusicOfAlbum;
import com.example.songplayer.db.entity.ListMusicOfPlaylist;
import com.example.songplayer.db.entity.Performer;
import com.example.songplayer.db.entity.Playlist;
import com.example.songplayer.db.entity.SongEntity;

@Database(entities = {
        ArtistEntity.class,
        AlbumEntity.class,
        SongEntity.class,
        Genre.class,
        ListMusicOfAlbum.class,
        ListMusicOfPlaylist.class,
        Performer.class,
        Playlist.class
},version = 3,exportSchema = false)
public abstract class MusicAppRoomDatabase  extends RoomDatabase{
    private static MusicAppRoomDatabase INSTANCE = null;
    public abstract AlbumDAORoom albumDAORoom();
    public abstract ArtistDaoRoom artistDAORoom();
    public abstract SongDAORoom songDao();
    public abstract GenreDAORoom genreDAORoom();
    public abstract ListMusicOfAlbumDAORoom listMusicOfAlbumDAORoom();
    public abstract ListMusicOfPlaylistDAORoom listMusicOfPlaylistDAORoom();
    public abstract PerformerDAORoom performerDAORoom();
    public abstract PlaylistDAORoom playlistDAORoom();

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
//            db.execSQL("DELETE from songs");
//            db.execSQL("DELETE from playlists");
//            db.execSQL("DELETE from albums");
//            db.execSQL("DELETE from performers");
//            db.execSQL("DELETE from genres");
//            db.execSQL("DELETE from artists");

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
