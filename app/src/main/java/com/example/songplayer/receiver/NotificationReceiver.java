package com.example.songplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "receiver";
    public static final String INTENT_FILTER_ACTION = "SONG_CONTROL";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(INTENT_FILTER_ACTION)) {
            return;
        }
        String action = intent.getAction();
        Log.d(TAG, "onReceive: "+ action);

        Intent intent1 = new Intent();
        intent1.setAction(INTENT_FILTER_ACTION);
        intent1.putExtra(INTENT_FILTER_ACTION, action);
        context.sendBroadcast(intent1);
    }
}
