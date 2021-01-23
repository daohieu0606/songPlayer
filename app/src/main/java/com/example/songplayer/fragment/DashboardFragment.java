package com.example.songplayer.fragment;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.songplayer.adapter.AlbumAdapter;
import com.example.songplayer.adapter.SongAdapter;
import com.example.songplayer.dao.daoimpl.OnlSongDAOImp;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.Playlist;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.utils.MyDialog;
import com.example.songplayer.viewmodel.AlbumViewModel;
import com.example.songplayer.viewmodel.ArtistViewModel;
import com.example.songplayer.viewmodel.SongViewModel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements SongAdapter.SongAdapterCallback {

    private static final String TAG = "TESST";
    private RecyclerView rvSongs;
    private SongAdapter songAdapter;
    private ArtistViewModel artistViewModel;

    private RecyclerView lstAlbums;
    private AlbumAdapter albumAdapter;
    private AlbumViewModel albumViewModel;
    private SongViewModel songViewModel;

    private MyDialog dialog;

    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setUpSongListView(result);
        setUpAlbumList(result);

        return result;
    }

    private void setUpAlbumList(View view) {
        lstAlbums = view.findViewById(R.id.rvAlbums);

//        albumAdapter = new AlbumAdapter(DummyData.albums);
        albumAdapter = new AlbumAdapter(new ArrayList<>(), this);

        lstAlbums.setAdapter(albumAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        lstAlbums.setLayoutManager(layoutManager);

        lstAlbums.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.right = 30;

            }
        });
        albumAdapter.notifyDataSetChanged();

        albumViewModel.getAllAlbums().observe(getViewLifecycleOwner(), albumEntities -> {
            if (albumEntities != null) {
                albumAdapter.setAlbum(albumEntities);
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
            if(songEntities!=null){
                songAdapter.setSongs(songEntities);
            }
        });

        songViewModel.getAllOnlineSongs().observe(getViewLifecycleOwner(), (songs) -> {
            if (songs != null && songs.size() > 0) {
                Log.d(TAG, "Onl songs" + songs);
                songAdapter.appendWithOnlineSongs(songs);
            }
        });
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

                    dialog = new MyDialog(getContext(), "Hello", song, (ArrayList<Playlist>) playlists);
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
        songViewModel.getAllSongOfAlbum(album).observe(getViewLifecycleOwner(), new Observer<List<SongEntity>>() {
            @Override
            public void onChanged(List<SongEntity> songEntities) {
                songAdapter.setSongs(songEntities);
            }
        });

    }

}