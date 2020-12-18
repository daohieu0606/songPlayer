package com.example.songplayer.service;

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

import androidx.annotation.Nullable;

import com.example.songplayer.db.entity.SongEntity;

public class MusicService
        extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "Music service";
    SongEntity currentSong;
    private MediaPlayer songPlayer;
    private final IBinder musicBind = new MusicBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        songPlayer = new MediaPlayer();
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

    public void setCurrentSong(SongEntity newSong) {
        currentSong = newSong;
    }

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

    public void preparePlay(){
        songPlayer.reset();

        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSong.getId());

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
    }

    public void seek(int posn){
        songPlayer.seekTo(posn);
    }

    public void go(){
        songPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        mp.start();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
