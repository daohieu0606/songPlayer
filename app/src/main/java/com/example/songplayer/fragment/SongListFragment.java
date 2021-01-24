package com.example.songplayer.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.Data.DummyData;
import com.example.songplayer.MyApplication;
import com.example.songplayer.R;
import com.example.songplayer.adapter.SongAdapter2;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.Playlist;

public class SongListFragment extends Fragment {

    private RecyclerView rvListSong;
    private SongAdapter2 songAdapter;

    public SongListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewResult = inflater.inflate(R.layout.fragment_list, container, false);
        setUpUI(viewResult);

        final Object data = getArguments().getSerializable(getString(R.string.data));
        SongAdapter2.SongAdapterCallback2 callback2 = (SongAdapter2.SongAdapterCallback2) getActivity();
        if (data instanceof AlbumEntity) {
            MyApplication
                    .database
                    .songDao()
                    .getAllSongsOfAlbum(((AlbumEntity) data).getId())
                    .observe(getViewLifecycleOwner(), (songs) -> {

                        if (songAdapter == null) {
                            songAdapter = new SongAdapter2(songs, DummyData.gradients, callback2, data);
                            rvListSong.setAdapter(songAdapter);
                            rvListSong.setLayoutManager(new LinearLayoutManager(getContext()));

                            rvListSong.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                    outRect.bottom = 25;
                                }
                            });
                        } else {
                            songAdapter.setSongs(songs);
                        }
                    });
        } else if (data instanceof Playlist) {
            MyApplication
                    .database
                    .songDao()
                    .getAllSongsOfPlaylist(((Playlist) data)
                            .getPlaylistID())
                    .observe(getViewLifecycleOwner(), (songs) -> {

                        Log.d("BUGG", "onCreateView: "+ songs);
                        if (songAdapter == null) {
                            songAdapter = new SongAdapter2(songs, DummyData.gradients, callback2, data);
                            rvListSong.setAdapter(songAdapter);
                            rvListSong.setLayoutManager(new LinearLayoutManager(getContext()));

                            rvListSong.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                    outRect.bottom = 25;
                                }
                            });

                        } else {
                            songAdapter.setSongs(songs);
                        }
                    });

        }

        return viewResult;
    }

    private void setUpUI(View view) {
        rvListSong = view.findViewById(R.id.rvList);
    }

}