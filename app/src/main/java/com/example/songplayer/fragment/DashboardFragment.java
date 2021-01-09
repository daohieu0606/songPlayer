package com.example.songplayer.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.Data.DummyData;
import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.adapter.AlbumAdapter;
import com.example.songplayer.adapter.SongAdapter;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.viewmodel.AlbumViewModel;
import com.example.songplayer.viewmodel.ArtistViewModel;
import com.example.songplayer.viewmodel.SongViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements SongAdapter.SongAdapterCallback {

    private static final String TAG = "DASHBOARD";
    private MainActivity mainActivity;

    private RecyclerView rvSongs;
    private SongAdapter songAdapter;
    private ArtistViewModel artistViewModel;

    private RecyclerView lstAlbums;
    private AlbumAdapter albumAdapter;
    private AlbumViewModel albumViewModel;
    private SongViewModel songViewModel;
    private List<SongEntity> songs = new ArrayList<>();

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

        songViewModel.getAllSongs().observe(getActivity(), new Observer<List<SongEntity>>() {
            @Override
            public void onChanged(List<SongEntity> songEntities) {
                DashboardFragment.this.songs = songEntities;
                Log.d("OKE", "onChanged: " + songEntities);
                setUpSongListView(result);
            }
        });


        return result;
    }

    private void setUpAlbumList(View view) {
        lstAlbums = view.findViewById(R.id.rvAlbums);

        albumAdapter = new AlbumAdapter(DummyData.albums);
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
    }

    private void setUpSongListView(View view) {

        rvSongs = view.findViewById(R.id.rvSongs);
        songAdapter = new SongAdapter(songs,DummyData.gradients,this);
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
    }


    @Override
    public void downloadASong(SongEntity song) {

    }

    @Override
    public void favoriteASong(SongEntity song) {

    }

    @Override
    public void setASongAsRingTone(SongEntity song) {

    }

    @Override
    public void setASongAsNotification(SongEntity song) {

    }

    @Override
    public void setASongAsAlarm(SongEntity song) {

    }

    @Override
    public void onClickPlay(SongEntity music) {
        ((DashboardCallback)getContext()).play(music);
    }

    public interface DashboardCallback{
        void play(SongEntity music);
    }

}