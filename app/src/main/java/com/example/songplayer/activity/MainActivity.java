package com.example.songplayer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.songplayer.R;
import com.example.songplayer.adapter.DrawerAdapter;
import com.example.songplayer.adapter.HorizontalAdapter;
import com.example.songplayer.adapter.VerticalAdapter;
import com.example.songplayer.adapter.adaper_item.DrawerItem;
import com.example.songplayer.dao.AlbumDAO;
import com.example.songplayer.dao.ArtistDAO;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.fragment.DashboardFragment;
import com.example.songplayer.utils.AlbumDbHelper;
import com.example.songplayer.viewmodel.SongViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TESST";
    ImageView btnDrawer;
    View fragmentFullScreen;
    boolean btnDrawerClicked = false;
    private RecyclerView drawerMenu;

    private SearchView searchView;

    private SongViewModel songViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermission();
        setUp();
        bindViews();

        songViewModel = new ViewModelProvider(this,
                new ViewModelProvider.Factory() {
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                        return (T) new SongViewModel(getApplication());
                    }
                }).get(SongViewModel.class);
        songViewModel.getAllSongs().observe(this, new Observer<List<SongEntity>>() {
            @Override
            public void onChanged(List<SongEntity> songEntities) {
                Log.d(TAG, "onChanged: " +  songEntities.size());
            }
        });
    }

    private void checkAndRequestPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 7003);
        }
    }

    private void setUp() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        DashboardFragment dashboardFragment = new DashboardFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, dashboardFragment);
        transaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        searchView = (SearchView) menu.findItem( R.id.mi_search ).getActionView(); // get my MenuItem with placeholder submenu
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return  true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mi_search:
                handleSearch();
                break;
        }
        return true;
    }

    private void handleSearch() {
    }

    private void bindViews(){
        btnDrawer = findViewById(R.id.btnDrawer);

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