package com.example.songplayer.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.adapter.HorizontalAdapter;
import com.example.songplayer.adapter.VerticalAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView singerListView;
    private List<String> singers;
    private HorizontalAdapter singerListAdapter;

    private RecyclerView catalogueListView;
    private List<String> catalogues;
    private VerticalAdapter catalogueAdapter;
    public DashboardFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_dashboard, container, false);

        setUpSingerListView(result);
        setUpCatalogueListView(result);

        return result;
    }

    private void setUpCatalogueListView(View view) {
        catalogueListView = view.findViewById(R.id.lstCatalogueList);
        catalogues = new ArrayList<>();
        catalogueAdapter = new VerticalAdapter(getActivity(), catalogues);
        catalogueListView.setAdapter(catalogueAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
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