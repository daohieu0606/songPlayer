package com.example.songplayer.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.MyApplication;
import com.example.songplayer.R;
import com.example.songplayer.adapter.DrawerAdapter;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.fragment.DashboardFragment;
import com.example.songplayer.service.Restarter;
import com.example.songplayer.service.YourService;
import com.example.songplayer.utils.DrawerCreater;
import com.example.songplayer.viewmodel.SongViewModel;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import static com.example.songplayer.utils.DrawerCreater.POS_CATEGORY;
import static com.example.songplayer.utils.DrawerCreater.POS_HOME;
import static com.example.songplayer.utils.DrawerCreater.POS_MUSIC;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener, DashboardFragment.DashboardCallback {

    //VIEW
    private SearchView searchView;
    private SongViewModel songViewModel;
    private SlidingRootNav slidingRootNav;
    private NavHostFragment navHostFragment;
    private NavController navController;
    //DATA
    private static final String TAG = "TESST";
    private Bundle savedInstance;
    private MutableLiveData<Boolean> menuClosed = new MutableLiveData<>(true);

    // File Observer
    Intent mServiceIntent;
    private YourService mYourService;


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.savedInstance = savedInstanceState;

        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            runIfHasPermission();
        }
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference songNode = FirebaseDatabase.getInstance().getReference().child("SongEntity");
////        for (int i = 0; i < 10; i++) {
////            DatabaseReference songPush = songNode.push();
////            String ID = songPush.getKey();
////            SongEntity songEntity = new SongEntity("SongName", "song-name", "uriString",
////                    "PathString", 1.2, "Artist", "Singer", "Genre", false, true);
////
////            songPush.setValue(songEntity);
////        }
//
//
//        DatabaseReference artistNode = FirebaseDatabase.getInstance().getReference().child("ArtistEntity");
//        DatabaseReference artistPush = artistNode.push();
//        artistPush.setValue(new ArtistEntity("Artist"));
//
//        DatabaseReference albumNode = FirebaseDatabase.getInstance().getReference().child("AlbumEntity");
//        DatabaseReference albumPush = albumNode.push();
//        albumPush.setValue(new AlbumEntity("Love and Heavy"));
//        List<SongEntity> songEntityList = new ArrayList<>();
//        ArrayList<SongEntity> songEntities = new ArrayList<>();
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SongEntity");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot ds : snapshot.getChildren()) {
//                        songEntities.add(ds.getValue(SongEntity.class));
//                    }
//                    songEntityList.addAll(songEntities);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        Query songDel = ref.orderByChild("id").equalTo(570734123);
//        songDel.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot ds : snapshot.getChildren()) {
//                        DatabaseReference a = ds.getRef();
//                        a.removeValue();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    private void runIfHasPermission() {

        mYourService = new YourService();
        mServiceIntent = new Intent(this, mYourService.getClass());

        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }

        MyApplication.semaphore.release();
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
    }

    private boolean checkAndRequestPermission() {

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK}, 7003);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    runIfHasPermission();
                } else {
                    Toast.makeText(MainActivity.this, "Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
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

        searchView = (SearchView) menu.findItem(R.id.mi_search).getActionView(); // get my MenuItem with placeholder submenu
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
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

    private void bindViews() {
        //Set up nav controller
        this.navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Creating Drawer
        final DrawerCreater drawerCreater = new DrawerCreater(this, toolbar);

        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_format_list_bulleted_24));
        this.slidingRootNav = drawerCreater.createDrawer();

        slidingRootNav.getLayout().setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                Log.d(TAG, "onDrag: ");
                return false;
            }
        });


    }

    @Override
    public void onItemSelected(int position) {

        switch (position) {
            case POS_HOME:
                navController.navigate(R.id.dashboardFragment);
                break;
            case POS_MUSIC:
                navController.navigate(R.id.musicPlayerFragment);
                break;
            case POS_CATEGORY:
                navController.navigate(R.id.categoryFragment);
                break;
        }

        if (slidingRootNav != null) {
            slidingRootNav.closeMenu();
        }


    }

    public Bundle getSavedInstance() {
        return this.savedInstance;
    }

    public RecyclerView getMenu() {
        return findViewById(R.id.list);
    }

    @Override
    public void play(SongEntity music) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.SONG), music);
        navController.navigate(R.id.musicPlayerFragment, bundle);

    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                ) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String... permissions) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                (dialog, which) -> ActivityCompat.requestPermissions((Activity) context,
                        permissions,
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE));
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}