package com.example.songplayer.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.adapter.AlbumAdapter;
import com.example.songplayer.adapter.ArtistAdapter;
import com.example.songplayer.adapter.HorizontalAdapter;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.ArtistEntity;
import com.example.songplayer.viewmodel.AlbumViewModel;
import com.example.songplayer.viewmodel.ArtistViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private MainActivity mainActivity;

    private RecyclerView lstArtists;
    private ArtistAdapter artistAdapter;
    private ArtistViewModel artistViewModel;

    private RecyclerView lstAlbums;
    private AlbumAdapter albumAdapter;
    private AlbumViewModel albumViewModel;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_dashboard, container, false);

        setUpSingerListView(result);
        setUpAlbumList(result);

        albumViewModel.getAllAlbums().observe(getActivity(), new Observer<List<AlbumEntity>>() {
            @Override
            public void onChanged(List<AlbumEntity> albumEntities) {
                albumAdapter.setAlbum(albumEntities);
            }
        });

        artistViewModel.getAllArtists().observe(getActivity(), new Observer<List<ArtistEntity>>() {
            @Override
            public void onChanged(List<ArtistEntity> artistEntities) {
                artistAdapter.setArtists(artistEntities);
            }
        });
        return result;
    }

    private void setUpAlbumList(View view) {
        lstAlbums = view.findViewById(R.id.lstAlbums);

        albumAdapter = new AlbumAdapter(getActivity(), new ArrayList<AlbumEntity>());
        lstAlbums.setAdapter(albumAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lstAlbums.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lstAlbums.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        lstAlbums.addItemDecoration(dividerItemDecoration);
        albumAdapter.notifyDataSetChanged();
    }

    private void setUpSingerListView(View view) {

        lstArtists = view.findViewById(R.id.lstArtist);
        artistAdapter = new ArtistAdapter(getActivity());
        lstArtists.setAdapter(artistAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        lstArtists.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lstArtists.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        lstArtists.addItemDecoration(dividerItemDecoration);

        artistAdapter.notifyDataSetChanged();
    }
}