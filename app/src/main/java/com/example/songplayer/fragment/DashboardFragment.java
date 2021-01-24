package com.example.songplayer.fragment;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.Data.DummyData;
import com.example.songplayer.MyApplication;
import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.adapter.AlbumHorizontalAdapter;
import com.example.songplayer.adapter.SongAdapter;
import com.example.songplayer.dao.daoimpl.OnlSongDAOImp;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.Playlist;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.utils.MyDialog;
import com.example.songplayer.viewmodel.AlbumViewModel;
import com.example.songplayer.viewmodel.ArtistViewModel;
import com.example.songplayer.viewmodel.SongViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.songplayer.utils.Constants.DASH_BOARD;
import static com.example.songplayer.utils.Constants.DOWNLOAD_SCREEN;
import static com.example.songplayer.utils.Constants.SCREEN_TYPE;

public class DashboardFragment extends Fragment implements SongAdapter.SongAdapterCallback {

    private static final String TAG = "TESST";
    private RecyclerView rvSongs;
    private SongAdapter songAdapter;
    private ArtistViewModel artistViewModel;

    private RecyclerView lstAlbums;
    private AlbumHorizontalAdapter albumHorizontalAdapter;
    private AlbumViewModel albumViewModel;
    private SongViewModel songViewModel;
    private View view;

    private MyDialog dialog;
    private String screenType;
    private TextView screenTitle;
    private FloatingActionButton btnPlayAll;

    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null) {
            setArguments(new Bundle());
        }

        albumViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AlbumViewModel(getActivity().getApplication());
            }
        }).get(AlbumViewModel.class);

        artistViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ArtistViewModel(getActivity().getApplication());
            }
        }).get(ArtistViewModel.class);

        songViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SongViewModel(getActivity().getApplication());
            }
        }).get(SongViewModel.class);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_dashboard, container, false);

        screenTitle = result.findViewById(R.id.screenTitle);
        btnPlayAll = result.findViewById(R.id.fab);

        screenType = getArguments().getString(SCREEN_TYPE) == null ? DASH_BOARD :
                getArguments().getString(SCREEN_TYPE);

        if (screenType.equals(DASH_BOARD)) {
            setUpAlbumList(result);
            screenTitle.setText("All music");
        } else if (screenType.equals(DOWNLOAD_SCREEN)) {
            screenTitle.setText("Downloads");
        }

        setUpSongListView(result);

        view = result;

        btnPlayAll.setOnClickListener((v)->{
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.list_song), (Serializable) songAdapter.getSongs());
            MainActivity.getNavController().navigate(R.id.musicPlayerFragment, bundle);
        });


        return result;
    }

    private void setUpAlbumList(View view) {
        lstAlbums = view.findViewById(R.id.rvAlbums);

//        albumAdapter = new AlbumAdapter(DummyData.albums);
        albumHorizontalAdapter = new AlbumHorizontalAdapter(new ArrayList<>(), this);

        lstAlbums.setAdapter(albumHorizontalAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        lstAlbums.setLayoutManager(layoutManager);

        lstAlbums.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.right = 30;

            }
        });
        albumHorizontalAdapter.notifyDataSetChanged();

        albumViewModel.getAllAlbums().observe(getViewLifecycleOwner(), albumEntities -> {
            if (albumEntities != null) {
                albumHorizontalAdapter.setAlbum(albumEntities);
            }
        });
    }

    private void setUpSongListView(View view) {

        rvSongs = view.findViewById(R.id.rvSongs);

        songAdapter = new SongAdapter(new ArrayList<>(), DummyData.gradients, this);
        rvSongs.setAdapter(songAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvSongs.setLayoutManager(layoutManager);

        rvSongs.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 25;
            }
        });

        songAdapter.notifyDataSetChanged();

        songViewModel.getAllOfflineSongs().observe(getViewLifecycleOwner(), songEntities -> {

            if (screenType.equals(DOWNLOAD_SCREEN)) {
                ArrayList<SongEntity> downloadSongs = new ArrayList<>();

                songEntities.forEach((song) -> {

                    if (song.getPath() != null &&
                            song.getPath()
                                    .toLowerCase()
                                    .contains(Environment.DIRECTORY_DOWNLOADS.toLowerCase())) {
                        downloadSongs.add(song);
                    }
                });
                songAdapter.appendWithOnlineSongs(downloadSongs);

            } else {
                if (songEntities != null) {
                    songAdapter.appendWithOnlineSongs(songEntities);
                }
            }

        });

        if (screenType.equals(DASH_BOARD)) {
            songViewModel.getAllOnlineSongs().observe(getViewLifecycleOwner(), (songs) -> {
                if (songs != null && songs.size() > 0) {
                    songAdapter.appendWithOnlineSongs(songs);
                }
            });
        }

    }


    @Override
    public void downloadASong(SongEntity song) {


        final ProgressDialog dialog = ProgressDialog.show(
                getContext(), "Downloading", "Loading...please wait");

        new Thread(() -> {

            try {
                MyApplication.onlSongDatabase.onlSongDao().downloadFile(song.getSongName(), new OnlSongDAOImp.UIHandler() {
                    @Override
                    public void updateProgress(int percent) {
                        dialog.setMessage("Downloaded " + percent + "%");
                    }

                    @Override
                    public void success() {
                        dialog.setMessage("Download completed");
                        try {
                            Thread.sleep(500);
                            setUpSongListView(view);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void favoriteASong(SongEntity song) {

    }

    @Override
    public void addToPlaylist(SongEntity song) {
        MyApplication.database.playlistDAORoom().getAllPlaylist().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                if (playlists != null) {
                    if (dialog != null) {
                        dialog.hide();
                    }

                    dialog = new MyDialog(getContext(), "Add song to playlist", song, (ArrayList<Playlist>) playlists);
                    dialog.show();
                } else {
                    Toast.makeText(getContext(), "There are no playlist in your phone", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public void setASongAsNotification(SongEntity song) {

    }

    @Override
    public void setASongAsAlarm(SongEntity song) {

    }

    @Override
    public void onClickPlay(SongEntity music) {
        ((DashboardCallback) getContext()).play(music);
    }

    public interface DashboardCallback {
        void play(SongEntity music);
    }

    public void displayListSongOfAlbum(AlbumEntity album) {
        songViewModel.getAllSongOfAlbum(album).observe(getViewLifecycleOwner(), songEntities -> songAdapter.setSongs(songEntities));

    }
}