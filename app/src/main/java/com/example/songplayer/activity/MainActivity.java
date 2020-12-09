package com.example.songplayer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.example.songplayer.db.SongDbHelper;
import com.example.songplayer.db.SongEntity;
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

    private RecyclerView singerListView;
    private List<String> singers;
    private HorizontalAdapter singerListAdapter;

    private RecyclerView catalogueListView;
    private List<String> catalogues;
    private VerticalAdapter catalogueAdapter;

    private SongViewModel songViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissin();
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

    private void checkAndRequestPermissin() {
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
        setUpSingerListView();
        setUpCatalogueListView();
    }

    private void setUpCatalogueListView() {
        catalogueListView = findViewById(R.id.lstCatalogueList);
        catalogues = new ArrayList<>();
        catalogueAdapter = new VerticalAdapter(this, catalogues);
        catalogueListView.setAdapter(catalogueAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        catalogueListView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(catalogueListView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        catalogueListView.addItemDecoration(dividerItemDecoration);
        catalogues.add("The Kings");
        catalogues.add("DSK - Playlist #1");
        catalogues.add("DSK - Playlist #2");
        catalogues.add("Hoa Hải Đường");
        catalogues.add("Gặp lại nhưng không ở lại");
        catalogues.add("Tình khúc quê hương");
        catalogues.add("Sai lầm của anh");
        catalogueAdapter.notifyDataSetChanged();
    }

    private void setUpSingerListView() {
        singerListView = findViewById(R.id.lstSinger);
        singers = new ArrayList<>();
        singerListAdapter = new HorizontalAdapter(this, singers);
        singerListView.setAdapter(singerListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        singerListView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(singerListView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        singerListView.addItemDecoration(dividerItemDecoration);
        singers.add("Den Vau");
        singers.add("Kimmese");
        singers.add("Bich Phuong");
        singers.add("DSK");
        singers.add("Karik");
        singers.add("Phuong Ly");
        singers.add("Wowy");
        singerListAdapter.notifyDataSetChanged();
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