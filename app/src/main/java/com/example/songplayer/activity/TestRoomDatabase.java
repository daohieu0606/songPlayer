package com.example.songplayer.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.songplayer.Data.DummyData;
import com.example.songplayer.R;
import com.example.songplayer.db.MusicAppRoomDatabase;
import com.example.songplayer.db.entity.ListMusicOfAlbum;

public class TestRoomDatabase extends AppCompatActivity {
    String TAG = "TESST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_room_database);
        MusicAppRoomDatabase db = MusicAppRoomDatabase.getDatabase(getApplication());
        new Thread(() -> {
            DummyData.songs.forEach(song -> {
                db.songDao().insert(song);
            });

            db.listMusicOfAlbumDAORoom().insert(new ListMusicOfAlbum(1,67));
            db.listMusicOfAlbumDAORoom().insert(new ListMusicOfAlbum(2,67));


//            DummyData.albums.forEach(db.albumDAORoom()::insert);

        }).start();
        db.songDao().getAllSongs().observe(this, songEntities -> Log.d(TAG, "onChanged: HI " + songEntities));

        //        db.songDao().getAllSongsOfAlbum(67).observe(this, new Observer<List<SongEntity>>() {
//            @Override
//            public void onChanged(List<SongEntity> songEntities) {
//                Log.d(TAG, "Album "+songEntities);
//            }
//        });
    }
}