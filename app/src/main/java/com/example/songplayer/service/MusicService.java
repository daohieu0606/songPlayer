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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.db.entity.SongEntity;

public class MusicService
        extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "Music service";
    SongEntity currentSong;
    private MediaPlayer songPlayer;
    private final IBinder musicBind = new MusicBinder();
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "my_chanel";
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
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        mp.start();

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
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
