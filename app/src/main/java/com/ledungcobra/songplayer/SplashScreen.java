package com.ledungcobra.songplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    String TAG = "DEBUG";

    private View imgDot1,imgDot2,imgDot3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
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
        imgDot3.startAnimation(animation2);



        Log.d(TAG, "doAnimate: "+"Run");



    }
}