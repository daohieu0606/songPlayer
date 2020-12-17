package com.example.songplayer.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.adapter.DrawerAdapter;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.utils.AlbumDbHelper;
import com.example.songplayer.utils.ArtistDbHelper;
import com.example.songplayer.utils.DrawerCreater;
import com.example.songplayer.viewmodel.SongViewModel;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import java.util.List;

import static com.example.songplayer.utils.DrawerCreater.POS_HOME;
import static com.example.songplayer.utils.DrawerCreater.POS_MUSIC;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{

    //VIEW
    private SearchView searchView;
    private SongViewModel songViewModel;
    private SlidingRootNav slidingRootNav;
    private NavHostFragment navHostFragment ;
    private NavController navController;
    //DATA
    private static final String TAG = "TESST";
    private Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstance = savedInstanceState;

        checkAndRequestPermission();
        bindViews();
        setUp();


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
                Log.d(TAG, "onCreate: " +  songEntities.size());
            }
        });
        AlbumDbHelper albumDbHelper = new AlbumDbHelper(getApplication());
        Log.d(TAG, "onCreate: so luong album " + albumDbHelper.getAllAlbums().size());

        ArtistDbHelper artistDbHelper = new ArtistDbHelper(getApplication());
        Log.d(TAG, "onCreate: so luong ca si " + artistDbHelper.getAllArtists().size());
    }

    private void checkAndRequestPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 7003);
        }
    }

    private void setUp() {

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        navController.navigate(R.id.action_play_music);
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
        //Set up nav controller
        this.navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Creating Drawer
        final DrawerCreater drawerCreater = new DrawerCreater(this,toolbar);

        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_format_list_bulleted_24));       this.slidingRootNav = drawerCreater.createDrawer();

    }

    @Override
    public void onItemSelected(int position) {

        if (position == POS_HOME) {
            navController.navigate(R.id.dashboardFragment);
        } else if (position == POS_MUSIC){
            navController.navigate(R.id.musicPlayerFragment);
        }
        if(slidingRootNav!=null ){
            slidingRootNav.closeMenu();
        }

    }



    public Bundle getSavedInstance(){
        return this.savedInstance;
    }

    public RecyclerView getMenu(){
        return findViewById(R.id.list);
    }
}