package com.example.songplayer.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.MyApplication;
import com.example.songplayer.R;
import com.example.songplayer.adapter.ListItemAdapter;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.Playlist;
import com.example.songplayer.utils.Constants;

public class ListFragment extends Fragment implements ListItemAdapter.AlbumVerticalAdapterCallback {
    private String listType;
    private RecyclerView rvList;
    private ListItemAdapter<Playlist> playlistListAdapter;
    private ListItemAdapter<AlbumEntity> albumEntityListAdapter;
    private ListFragmentCallback callback;

    public ListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
        callback = (ListFragmentCallback) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewResult = inflater.inflate(R.layout.fragment_list, container, false);

        setUpUI(viewResult);
        listType = getArguments().getString(getString(R.string.type));

        if (listType.equals(Constants.PLAYLIST)) {

            MyApplication.database.playlistDAORoom().getAllPlaylist().observe(getViewLifecycleOwner(), playlists -> {
                if (playlists != null) {

                    if (playlistListAdapter == null) {
                        playlistListAdapter = new ListItemAdapter<>(playlists, ListFragment.this);
                    } else {
                        playlistListAdapter.setListItems(playlists);
                    }
                    rvList.setAdapter(playlistListAdapter);
                    rvList.setLayoutManager(new LinearLayoutManager(getContext()));

                    rvList.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                            outRect.bottom = 25;
                        }
                    });

                }
            });

        } else if (listType.equals(Constants.ALBUM)) {
            MyApplication.database.albumDAORoom().getAllAlbums().observe(getViewLifecycleOwner(), albums -> {
                if (albums != null) {

                    if (albumEntityListAdapter == null) {
                        albumEntityListAdapter = new ListItemAdapter<>(albums, ListFragment.this);
                    } else {
                        albumEntityListAdapter.setListItems(albums);
                    }
                    rvList.setAdapter(albumEntityListAdapter);
                    rvList.setLayoutManager(new LinearLayoutManager(getContext()));

                    rvList.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                            outRect.bottom = 25;
                        }
                    });

                }
            });
        }

        return viewResult;
    }

    private void setUpUI(View view) {
        rvList = view.findViewById(R.id.rvList);
    }

    @Override
    public void playAllList(Object object) {
        if (Constants.ALBUM.equals(listType)) {
            callback.playAllList((AlbumEntity) object);
        } else if (Constants.PLAYLIST.equals(listType)) {
            callback.playAllList((Playlist) object);
        }
    }

    @Override
    public void viewAlbumDetail(Object object) {
        callback.viewAlbumDetail((AlbumEntity) object);
    }

    @Override
    public void viewPlaylistDetail(Object listItem) {
        callback.viewPlaylistDetail((Playlist) listItem);
    }

    public interface ListFragmentCallback {
        void playAllList(AlbumEntity albumEntity);
        void playAllList(Playlist playlist);
        void viewAlbumDetail(AlbumEntity albumEntity);
        void viewPlaylistDetail(Playlist playlist);
    }
}
