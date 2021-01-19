package com.example.songplayer.db;

import android.app.Application;
import android.os.AsyncTask;

import com.example.songplayer.MyApplication;
import com.example.songplayer.dao.daoimpl.OnlSongDAOImp;
import com.example.songplayer.dao.daoimpl.SongDAOImp;
import com.example.songplayer.db.entity.SongEntity;

import java.util.List;

public class SongRepo {
    private static final String TAG = "TESST";

    private SongDAOImp songDAO;
    private OnlSongDAOImp onlSongDAOImp;

    private List<SongEntity> allSongs;
    private List<SongEntity> allOnlineSongs;

    private MusicAppRoomDatabase roomDatabase;


    public SongRepo(Application newApplication) {
        SongDatabase localDatabase = MyApplication.songDatabase;
        roomDatabase = MyApplication.database;
        OnlSongDatabase onlSongDatabase = MyApplication.onlSongDatabase;
        songDAO = localDatabase.songDAO();

        allSongs = songDAO.getAllSongs();
        onlSongDAOImp = onlSongDatabase.songDAO();
        allOnlineSongs = onlSongDAOImp.getAllSongs();

    }

    public MusicAppRoomDatabase getRoomDatabase() {
        return roomDatabase;
    }

    public void insert(SongEntity songEntity) {
        new InsertSongAsyncTask(songDAO).execute(songEntity);
    }

    public void update(SongEntity songEntity) {
        new UpdateSongAsyncTask(songDAO).execute(songEntity);
    }

    public void delete(int songID) {
        new DeleteSongAsyncTask(songDAO).execute(songID);
        allSongs = songDAO.getAllSongs();
    }

    public void deleteAllSongs() {
        new DeleteAllSongsAsyncTask(songDAO).execute();
    }

    public List<SongEntity> getAllSongs() {
        return allSongs;
    }

    public List<SongEntity> getAllOnlineSongs() {
        return allOnlineSongs;
    }

    private static class InsertSongAsyncTask extends AsyncTask<SongEntity, Void, Void> {
        private SongDAOImp songDAO;

        private InsertSongAsyncTask(SongDAOImp songDAO) {
            this.songDAO = songDAO;
        }

        @Override
        protected Void doInBackground(SongEntity... songs) {
            songDAO.insert(songs[0]);
            return null;
        }
    }

    private static class UpdateSongAsyncTask extends AsyncTask<SongEntity, Void, Void> {
        private SongDAOImp songDAO;

        private UpdateSongAsyncTask(SongDAOImp songDAO) {
            this.songDAO = songDAO;
        }

        @Override
        protected Void doInBackground(SongEntity... songs) {
            songDAO.update(songs[0]);
            return null;
        }
    }

    private static class DeleteSongAsyncTask extends AsyncTask<Integer, Void, Void> {
        private SongDAOImp songDAO;

        private DeleteSongAsyncTask(SongDAOImp songDAO) {
            this.songDAO = songDAO;
        }

        @Override
        protected Void doInBackground(Integer... songs) {
/*            if (FileHelper.removeFile((Activity) context, songs[0].getUriString())) {

            }*/
            songDAO.delete(songs[0]);
            return null;
        }
    }

    private static class DeleteAllSongsAsyncTask extends AsyncTask<Void, Void, Void> {
        private SongDAOImp songDAO;

        private DeleteAllSongsAsyncTask(SongDAOImp songDAO) {
            this.songDAO = songDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //   songDAO.deleteAll();
            return null;
        }
    }
}
