package com.example.songplayer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;

import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.fragment.RepeatMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService
        extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "Music service";
    private MediaPlayer songPlayer;
    private final IBinder musicBind = new MusicBinder();
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "my_chanel";

    private List<SongEntity> songEntities;
    private SongEntity currentSong;
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
        currentSong = newSong;
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
                currentSong.getId());

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
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        createNotificationChannel();

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.selector_pause_resume)
                .setTicker(currentSong.getSongName())
                .setOngoing(true)
                .setContentTitle(currentSong.getUriString())
                .setContentText(currentSong.getSongName());
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
            if (songEntities.indexOf(currentSong)  == 0) {
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
        currentSong = songEntities.get(currentIndex);
        Log.d(TAG, "takePreSong: " + currentSong.getSongName());
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
        currentSong = songEntities.get(currentIndex);
        Log.d(TAG, "takeNextSong: " + currentSong.getSongName());
        preparePlaySyn();

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
        super.onDestroy();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
