package com.example.songplayer.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.songplayer.R;
import com.example.songplayer.db.MusicAppRoomDatabase;

public class TestRoomDatabase extends AppCompatActivity {
    String TAG = "TESST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_room_database);
        MusicAppRoomDatabase db = MusicAppRoomDatabase.getDatabase(getApplication());

        db.songDao().getAllMusicOfAlbum().observe(this,(albums)->{
            Log.d(TAG, "ALBUMS "+ albums);
        });
    }
}