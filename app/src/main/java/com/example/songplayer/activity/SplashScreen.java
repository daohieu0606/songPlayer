package com.example.songplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.example.songplayer.R;

public class SplashScreen extends AppCompatActivity {
    String TAG = "DEBUG";

    private View imgDot1,imgDot2,imgDot3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bindViews();
        doAnimate();
    }

    public void bindViews(){
        imgDot1 = findViewById(R.id.imgDot1);
        imgDot2 = findViewById(R.id.imgDot2);
        imgDot3 = findViewById(R.id.imgDot3);
    }
    public void doAnimate(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.up_and_down_anim);
        imgDot1.startAnimation(animation);

        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.up_and_down_anim_delay);
        imgDot2.startAnimation(animation1);

        Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.up_and_down_anim_delay2);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imgDot3.startAnimation(animation2);
        Log.d(TAG, "doAnimate: "+"Run");
    }
}