package com.example.songplayer;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.songplayer.db.MusicAppRoomDatabase;
import com.example.songplayer.db.OnlSongDatabase;
import com.example.songplayer.db.SongDatabase;

public class MyApplication extends Application {
    public static MusicAppRoomDatabase database;
    public static SongDatabase songDatabase;
    public static OnlSongDatabase onlSongDatabase;
    private static Context context;
    public String TAG = "TESST";
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        database = MusicAppRoomDatabase.getDatabase(this);
        songDatabase = SongDatabase.getInstance(this);
        onlSongDatabase = OnlSongDatabase.getInstance(this);

        try {
            songDatabase.scanOfflineSong(true);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        onlSongDatabase.fetchOnlineSongs();

    }

    public static Context getContext() {
        return MyApplication.context;
    }
}
