package com.example.songplayer.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.songplayer.MyApplication;
import com.example.songplayer.R;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.fragment.RepeatMode;
import com.example.songplayer.notification.NotificationHelper;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicService
        extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, Serializable {

    private static final String TAG = "Music service";
    private static final int NOTIFY_ID = 1;

    private static MusicService INSTANCE;
    private MediaPlayer songPlayer;
    private List<SongEntity> songEntities;
    private SongEntity currentSong;

    boolean isShuffle;
    private RepeatMode repeatMode;
    private ExecutorService executorService;

    public MusicService() {
        if (INSTANCE == null) {
            INSTANCE = this;
        }
    }

    public static MusicService getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPlayer = new MediaPlayer();

        repeatMode = RepeatMode.NEVER;

        if (songEntities == null) {
            songEntities = new ArrayList<>();
        }
        initMusicPlayer();
    }

    private void initMusicPlayer() {
        songPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();
            songPlayer.setAudioAttributes(attr);
        } else {
            songPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        songPlayer.setOnPreparedListener(this::onPrepared);
        songPlayer.setOnCompletionListener(this::onCompletion);
        songPlayer.setOnErrorListener(this::onError);
    }

    public void setSongList(List<SongEntity> newSongEntities) {
        songEntities = newSongEntities;
    }

    public void setCurrentSong(SongEntity newSong) {
        currentSong = newSong;
    }

    public void setShuffle(boolean isNewShuffle) {
        isShuffle = isNewShuffle;
    }

    public void setRepeatMode(RepeatMode newRepeatMode) {
        repeatMode = newRepeatMode;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        songPlayer.stop();
        songPlayer.release();
        return super.onUnbind(intent);
    }

    public void preparePlayASyn() {
        executorService.execute(() -> {
            songPlayer.reset();
            try {
                if (currentSong.isOnline()) {
                    Log.d(TAG, "preparePlayASyn: "+currentSong.getUriString());
                    songPlayer.setDataSource(currentSong.getUriString());
                } else {
                    songPlayer.setDataSource(getApplicationContext(), Uri.parse(currentSong.getUriString()));
                }
                songPlayer.prepareAsync();
            } catch (Exception exception) {
                Log.d(TAG, "playSong: ", exception);
            }
        });
    }


    public int getPosn() {
        return songPlayer.getCurrentPosition();
    }

    public int getDur() {
        return songPlayer.getDuration();
    }

    public boolean isPng() {
        return songPlayer.isPlaying();
    }

    public void pausePlayer() {
        songPlayer.pause();
        Notification not = NotificationHelper.createNotification(getApplicationContext()
                , currentSong
                , songEntities.indexOf(currentSong)
                , songEntities.size()
                , isPng());
        startForeground(NOTIFY_ID, not);
    }

    public void seek(int posn) {
        songPlayer.seekTo(posn);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Object object = intent.getSerializableExtra(getString(R.string.SONG));
        executorService = Executors.newFixedThreadPool(2);
        if (object != null) {
            SongEntity song = (SongEntity) object;
            prepareSourceForPlaying(song);

            playMusic();

        } else {
            Object listSongObj = intent.getSerializableExtra(getString(R.string.list_song));
            if (listSongObj != null) {
                List<SongEntity> songs = (List) listSongObj;
                setSongList(songs);
                if (songs.size() > 0) {
                    prepareSourceForPlaying(songs.get(0));
                }
                playMusic();
            }
        }


        return Service.START_STICKY;
    }


    public void prepareSourceForPlaying(SongEntity song) {

        if (song.isOnline()) {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            storage.getReference().child(song.getSongName()).getDownloadUrl().addOnSuccessListener(uri -> {
                setCurrentSong(song) ;
            song.setUriString(uri.toString());
                preparePlayASyn();
            }).addOnFailureListener(e -> Log.d(TAG, "onFailure: "+e));

        } else {
            setCurrentSong(song);
            preparePlayASyn();
        }
    }

    public void playMusic() {
        try{
        songPlayer.start();

        Notification not = NotificationHelper.createNotification(getApplicationContext()
                , currentSong
                , songEntities.indexOf(currentSong)
                , songEntities.size()
                , isPng());

        startForeground(NOTIFY_ID, not);
        }catch (Exception e){
            Log.d(TAG, "playMusic: "+ e);
            Toast.makeText(MyApplication.getContext(), "Cannot play this song", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Notification not = NotificationHelper.createNotification(getApplicationContext()
                , currentSong
                , songEntities.indexOf(currentSong)
                , songEntities.size()
                , isPng());
        startForeground(NOTIFY_ID, not);
    }

    public boolean takePreSong() {
        if (!isHasAnySongEntity()) {
            return false;
        }

        if (repeatMode == RepeatMode.ONLY_ONE) {
            songPlayer.stop();
            preparePlayASyn();
            return true;
        } else if (repeatMode == RepeatMode.NEVER) {
            if (songEntities.indexOf(currentSong) == 0) {
                Toast.makeText(getApplicationContext(), "You have just played last song in the list", Toast.LENGTH_LONG).show();
                songPlayer.stop();
                return false;
            }
        }

        songPlayer.stop();
        int currentIndex = 0;
        if (isShuffle) {
            currentIndex = new Random().nextInt(songEntities.size());
        } else {
            currentIndex = songEntities.indexOf(currentSong);

            if (currentIndex <= 0) {
                currentIndex = songEntities.size() - 1;
            } else {
                currentIndex -= 1;
            }
        }
        currentSong = (songEntities.get(currentIndex));
        Log.d(TAG, "takePreSong: " + currentSong.getSongName());
        preparePlayASyn();

        return true;
    }

    public boolean takeNextSong() {

        if (!isHasAnySongEntity()) {
            return false;
        }

        if (repeatMode == RepeatMode.ONLY_ONE) {
            songPlayer.stop();
            preparePlayASyn();
            return true;
        } else if (repeatMode == RepeatMode.NEVER) {
            if (songEntities.indexOf(currentSong) + 1 == songEntities.size()) {
                Toast.makeText(getApplicationContext(), "You have just played last song in the list", Toast.LENGTH_LONG).show();
                songPlayer.stop();
                return false;
            }
        }

        songPlayer.stop();
        int currentIndex = 0;

        if (isShuffle) {
            currentIndex = new Random().nextInt(songEntities.size());
        } else {
            currentIndex = songEntities.indexOf(currentSong);
            if (currentIndex >= songEntities.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
        }

        currentSong = (songEntities.get(currentIndex));
        Log.d(TAG, "takeNextSong: " + currentSong.getSongName());
        preparePlayASyn();

        return true;
    }

    public SongEntity getCurrentSong() {
        return currentSong;
    }

    private boolean isHasAnySongEntity() {
        return songEntities.size() > 0;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        executorService.shutdown();

        super.onDestroy();
    }


}
