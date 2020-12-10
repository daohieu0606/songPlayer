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
import com.example.songplayer.adapter.HorizontalAdapter;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.viewmodel.AlbumViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView singerListView;
    private List<String> singers;
    private HorizontalAdapter singerListAdapter;

    private RecyclerView albumListView;
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
        return result;
    }

    private void setUpAlbumList(View view) {
        albumListView = view.findViewById(R.id.lstAlbums);

        albumAdapter = new AlbumAdapter(getActivity(), new ArrayList<AlbumEntity>());
        albumListView.setAdapter(albumAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        albumListView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(albumListView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        albumListView.addItemDecoration(dividerItemDecoration);
        albumAdapter.notifyDataSetChanged();
    }

    private void setUpSingerListView(View view) {
        singerListView = view.findViewById(R.id.lstSinger);
        singers = new ArrayList<>();
        singerListAdapter = new HorizontalAdapter(getActivity(), singers);
        singerListView.setAdapter(singerListAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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
}