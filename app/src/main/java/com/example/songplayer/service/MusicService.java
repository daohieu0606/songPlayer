package com.example.songplayer.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.fragment.RepeatMode;
import com.example.songplayer.notification.NotificationHelper;
import com.example.songplayer.receiver.NotificationReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService
        extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    public static final String INTENT_FILTER_ACTION = "SONG_CONTROL";
    private static final String TAG = "Music service";
    private static final int NOTIFY_ID = 1;

    private MediaPlayer songPlayer;
    private final IBinder musicBind = new MusicBinder();

    private List<SongEntity> songEntities;
    private MutableLiveData<SongEntity> currentSongLiveData;
    private BroadcastReceiver songControlReceiver;

    boolean isShuffle;
    private RepeatMode repeatMode;

    @Override
    public void onCreate() {
        super.onCreate();
        songPlayer = new MediaPlayer();

        repeatMode = RepeatMode.NEVER;
        currentSongLiveData = new MutableLiveData<>();
        currentSongLiveData.setValue(null);

        if (songEntities == null) {
            songEntities = new ArrayList<>();
        }
        initMusicPlayer();
        Log.d(TAG, "onCreate: music service");

        NotificationHelper.createNotificationChannel(getApplicationContext());
        songControlReceiver = new NotificationReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                String message = intent.getStringExtra(INTENT_FILTER_ACTION);
                if (message.equals(NotificationHelper.ACTION_PREVIOUS)){
                    onTakePreSong();
                } else if (message.equals(NotificationHelper.ACTION_PLAY)){
                    onPauseResumeSong();
                } else if (message.equals(NotificationHelper.ACTION_NEXT)){
                    onTakeNextSong();
                }
            }
        };
        registerReceiver(songControlReceiver, new IntentFilter(INTENT_FILTER_ACTION));
    }

    private void onTakeNextSong() {
        takeNextSong();
        go();
    }

    private void onPauseResumeSong() {
        if (isPng()) {
            pausePlayer();
        } else {
            go();
        }
    }

    private void onTakePreSong() {
        takePreSong();
        go();
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
        currentSongLiveData.setValue(newSong);
    }
    public void setShuffle(boolean isNewShuffle) {
        isShuffle = isNewShuffle;
    }
    public void setRepeatMode(RepeatMode newRepeatMode) { repeatMode = newRepeatMode;}
    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        Log.d(TAG, "onBind: onbind()");
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        songPlayer.stop();
        songPlayer.release();
        return super.onUnbind(intent);
    }

    public void preparePlaySyn(){
        songPlayer.reset();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSongLiveData.getValue().getId());

        try {
            songPlayer.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception exception) {
            Log.d(TAG, "playSong: ", exception);
        }
        try{
            songPlayer.prepare();
        } catch (IllegalStateException e) {

        } catch (IOException e) {

        }
    }

    public void preparePlay(){
        songPlayer.reset();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSongLiveData.getValue().getId());

        try {
            songPlayer.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception exception) {
            Log.d(TAG, "playSong: ", exception);
        }
        songPlayer.prepareAsync();
    }

    public int getPosn(){
        return songPlayer.getCurrentPosition();
    }

    public int getDur(){
        return songPlayer.getDuration();
    }

    public boolean isPng(){
        return songPlayer.isPlaying();
    }

    public void pausePlayer(){
        songPlayer.pause();
        Notification not = NotificationHelper.createNotification(getApplicationContext()
                , currentSongLiveData.getValue()
                , songEntities.indexOf(currentSongLiveData.getValue())
                , songEntities.size()
                ,isPng());
        startForeground(NOTIFY_ID, not);
    }

    public void seek(int posn){
        songPlayer.seekTo(posn);
    }

    public void go(){
        songPlayer.start();
        Notification not = NotificationHelper.createNotification(getApplicationContext()
                , currentSongLiveData.getValue()
                , songEntities.indexOf(currentSongLiveData.getValue())
                , songEntities.size()
                ,isPng());
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
                , currentSongLiveData.getValue()
                , songEntities.indexOf(currentSongLiveData.getValue())
                , songEntities.size()
                ,isPng());
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
            if (songEntities.indexOf(currentSongLiveData.getValue())  == 0) {
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
            currentIndex = songEntities.indexOf(currentSongLiveData.getValue());

            if (currentIndex <= 0) {
                currentIndex = songEntities.size() - 1;
            } else {
                currentIndex -= 1;
            }
        }
        currentSongLiveData.setValue(songEntities.get(currentIndex));
        Log.d(TAG, "takePreSong: " + currentSongLiveData.getValue().getSongName());
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
            if (songEntities.indexOf(currentSongLiveData.getValue()) + 1 == songEntities.size()) {
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
            currentIndex = songEntities.indexOf(currentSongLiveData.getValue());
            if (currentIndex >= songEntities.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex += 1;
            }
        }
        currentSongLiveData.setValue(songEntities.get(currentIndex));
        Log.d(TAG, "takeNextSong: " + currentSongLiveData.getValue().getSongName());
        preparePlaySyn();

        return true;
    }

    public SongEntity getCurrentSong() {
        return currentSongLiveData.getValue();
    }
    private boolean isHasAnySongEntity() {
        return songEntities.size() > 0;
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
        unregisterReceiver(songControlReceiver);
        super.onDestroy();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
