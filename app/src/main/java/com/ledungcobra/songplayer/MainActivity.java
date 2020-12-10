package com.ledungcobra.songplayer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.songplayer.fragment.MusicPlayerFragment;
import com.ledungcobra.songplayer.ui_adaper.DrawerAdapter;
import com.ledungcobra.songplayer.ui_adaper.adaper_item.DrawerItem;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    ImageView btnSearch;
    ImageView btnDrawer;
    View fragmentFullScreen;
    boolean btnDrawerClicked = false;

    String TAG = "DEBUG";

    private RecyclerView drawerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.fragment_container ,MusicPlayerFragment.newInstance());
        ft.commit();


        bindViews();


    }

    private void bindViews(){
        btnSearch = findViewById(R.id.btn_search);
        btnDrawer = findViewById(R.id.btn_drawer);

        drawerMenu = findViewById(R.id.rv_drawer_menu);

        drawerMenu.setLayoutManager(new LinearLayoutManager(this));
        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                new DrawerItem(R.drawable.ic_home,"Home"),
                new DrawerItem(R.drawable.ic_category,"Category"),
                new DrawerItem(R.drawable.ic_favorite,"Favorite"),
                new DrawerItem(R.drawable.downloadic,"Download"),
                new DrawerItem(R.drawable.ic_share, "Share"),
                new DrawerItem(R.drawable.ic_rate, "Rate App"),
                new DrawerItem(R.drawable.ic_more_app,"More App")
        ));

        adapter.setOnItemClickListener(new DrawerAdapter.OnDrawerItemClickListener() {
            @Override
            public void onClick(DrawerItem item) {

            }
        });
        drawerMenu.setAdapter(adapter);








        fragmentFullScreen = findViewById(R.id.fragment_full_screen);
        final float scaleRatio = 0.65f;

        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!btnDrawerClicked) {
                    doAnimate(scaleRatio);
                }else{
                    reverseAnimate(scaleRatio);
                }

                btnDrawerClicked = !btnDrawerClicked;
            }
        });

        fragmentFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnDrawerClicked){
                    reverseAnimate(scaleRatio);
                    btnDrawerClicked = !btnDrawerClicked;
                }
            }
        });
    }

    private void doAnimate(float scaleRatioX){

        fragmentFullScreen.animate().scaleY(scaleRatioX).scaleX(scaleRatioX)
                .setDuration(500)
                .translationXBy(fragmentFullScreen.getMeasuredWidth()*(1-scaleRatioX)/1.5f)
                .start();
    }
    private void reverseAnimate(float scaleRatioX){
        fragmentFullScreen.animate().scaleY(1f).scaleX(1f)
                .setDuration(500)
                .translationXBy(-fragmentFullScreen.getMeasuredWidth()*(1-scaleRatioX)/1.5f)
                .start();
    }

}