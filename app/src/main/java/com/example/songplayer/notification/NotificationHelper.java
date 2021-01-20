package com.example.songplayer.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.receiver.NotificationReceiver;

import java.util.HashMap;

public class NotificationHelper {

    private static final String CHANNEL_ID = "my_chanel";

    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_NEXT = "ACTION_NEXT";

    public static Notification createNotification(Context context, SongEntity currentSong, int pos, int size, boolean isPlaying) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");

        PendingIntent pendingIntentPrevious;
        int drw_previous;
        if (pos == 0) {
            pendingIntentPrevious = null;
            drw_previous = 0;
        } else {
            Intent intentPrevious = new Intent(context, NotificationReceiver.class)
                    .setAction(ACTION_PREVIOUS);
            pendingIntentPrevious = PendingIntent.getBroadcast(context, 0,
                    intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
            drw_previous = R.drawable.ic_baseline_skip_previous_24;
        }

        Intent intentPlay = new Intent(context, NotificationReceiver.class)
                .setAction(ACTION_PLAY);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0,
                intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntentNext;
        int drw_next;
        if (pos == size - 1) {
            pendingIntentNext = null;
            drw_next = 0;
        } else {
            Intent intentNext = new Intent(context, NotificationReceiver.class)
                    .setAction(ACTION_NEXT);
            pendingIntentNext = PendingIntent.getBroadcast(context, 0,
                    intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
            drw_next = R.drawable.ic_baseline_skip_next_24;
        }

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art;
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        if (currentSong.isOnline()) {
            Log.d("TESST", "createNotification: "+currentSong.getUriString());
            mmr.setDataSource(currentSong.getUriString(), new HashMap<String,String>());

        } else {
            mmr.setDataSource(context, Uri.parse(currentSong.getUriString()));
        }

        rawArt = mmr.getEmbeddedPicture();

        if (null != rawArt) {
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        } else {
            art = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_image);
        }

        int btnPlay;
        if (isPlaying) {
            btnPlay = R.drawable.ic_baseline_pause_circle_outline_24;
        } else {
            btnPlay = R.drawable.ic_baseline_play_circle_outline_24;
        }
        Intent notIntent = new Intent(context, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.selector_pause_resume)
                .setContentTitle(currentSong.getSongName())
                .setContentText(currentSong.getArtist())
                .setLargeIcon(art)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .addAction(drw_previous, "Previous", pendingIntentPrevious)
                .addAction(btnPlay, "Play", pendingIntentPlay)
                .addAction(drw_next, "Next", pendingIntentNext)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()));
        Notification not = builder.build();
        return not;
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
