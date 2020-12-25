package com.example.songplayer.utils;

import android.app.Activity;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHelper {
    private static final int REMOVE_TAG = 101;

    public static boolean removeFile(Context context, String uriStr) {

        boolean result = false;

        Uri uri = Uri.parse(uriStr);
        try{
            context.getContentResolver().delete(uri, null, null);
            result = true;
        } catch (SecurityException securityException) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                RecoverableSecurityException recoverableSecurityException;
                if (securityException instanceof RecoverableSecurityException) {
                    recoverableSecurityException =
                            (RecoverableSecurityException)securityException;
                } else {
                    throw new RuntimeException(
                            securityException.getMessage(), securityException);
                }
                IntentSender intentSender =recoverableSecurityException.getUserAction()
                        .getActionIntent().getIntentSender();
                try{
                    ((Activity)context).startIntentSenderForResult(intentSender, REMOVE_TAG,
                            null, 0, 0, 0, null);
                } catch (Exception e){
                    Toast.makeText(context.getApplicationContext(), "Co loi khi xoa file", Toast.LENGTH_LONG).show();
                }
            } else {                //it can be file is read-only mode
                Toast.makeText(context.getApplicationContext(), "Co loi khi xoa file", Toast.LENGTH_LONG).show();
            }
        }

        return result;
    }

    public static Uri getNewSongFileUri(Context context) {
        ContentResolver resolver = context.getApplicationContext()
                .getContentResolver();

        Uri audioCollection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            audioCollection = MediaStore.Audio.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            audioCollection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues newSongDetails = new ContentValues();
        newSongDetails.put(MediaStore.Audio.Media.DISPLAY_NAME,
                "My Song.mp3");

        Uri result = resolver.insert(audioCollection, newSongDetails);
        return  result;
    }
}
