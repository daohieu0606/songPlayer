package com.example.songplayer.service;

import android.app.Notification;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.fragment.RepeatMode;
import com.example.songplayer.notification.NotificationHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService
        extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "Music service";
    private static final int NOTIFY_ID = 1;

    private MediaPlayer songPlayer;
    private final IBinder musicBind = new MusicBinder();

    private List<SongEntity> songEntities;
    private SongEntity currentSongLiveData;

    boolean isShuffle;
    private RepeatMode repeatMode;

    @Override
    public void onCreate() {
        super.onCreate();
        songPlayer = new MediaPlayer();

        repeatMode = RepeatMode.NEVER;

        if (songEntities == null) {
            songEntities = new ArrayList<>();
        }
        initMusicPlayer();
        Log.d(TAG, "onCreate: music service");


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
        currentSongLiveData = newSong;
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
        Log.d(TAG, "onBind: onbind()");
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        songPlayer.stop();
        songPlayer.release();
        return super.onUnbind(intent);
    }

    public void preparePlaySyn() {
        songPlayer.reset();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSongLiveData.getId());

        try {
            songPlayer.setDataSource(getApplicationContext(), trackUri);
            songPlayer.prepare();

        } catch (IllegalStateException e) {

        } catch (IOException e) {

        } catch (Exception exception) {
            Log.d(TAG, "playSong: ", exception);
        }
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
                , currentSongLiveData
                , songEntities.indexOf(currentSongLiveData)
                , songEntities.size()
                , isPng());
        startForeground(NOTIFY_ID, not);
    }

    public void seek(int posn) {
        songPlayer.seekTo(posn);
    }

    public void go() {
        songPlayer.start();
        Notification not = NotificationHelper.createNotification(getApplicationContext()
                , currentSongLiveData
                , songEntities.indexOf(currentSongLiveData)
                , songEntities.size()
                , isPng());
        startForeground(NOTIFY_ID, not);
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
                , currentSongLiveData
                , songEntities.indexOf(currentSongLiveData)
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
            preparePlaySyn();
            return true;
        } else if (repeatMode == RepeatMode.NEVER) {
            if (songEntities.indexOf(currentSongLiveData) == 0) {
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
            currentIndex = songEntities.indexOf(currentSongLiveData);

            if (currentIndex <= 0) {
                currentIndex = songEntities.size() - 1;
            } else {
                currentIndex -= 1;
            }
        }
        currentSongLiveData = (songEntities.get(currentIndex));
        Log.d(TAG, "takePreSong: " + currentSongLiveData.getSongName());
        preparePlaySyn();

        return true;
    }

    public boolean takeNextSong() {
        if (!isHasAnySongEntity()) {
            return false;
        }

        if (repeatMode == RepeatMode.ONLY_ONE) {
            songPlayer.stop();
            preparePlaySyn();
            return true;
        } else if (repeatMode == RepeatMode.NEVER) {
            if (songEntities.indexOf(currentSongLiveData) + 1 == songEntities.size()) {
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
            currentIndex = songEntities.indexOf(currentSongLiveData);
            if (currentIndex >= songEntities.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
        }
        currentSongLiveData = (songEntities.get(currentIndex));
        Log.d(TAG, "takeNextSong: " + currentSongLiveData.getSongName());
        preparePlaySyn();

        return true;
    }

    public SongEntity getCurrentSong() {
        return currentSongLiveData;
    }

    private boolean isHasAnySongEntity() {
        return songEntities.size() > 0;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
