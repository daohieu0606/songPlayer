package com.example.songplayer.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.adapter.CategoryAdapter;
import com.example.songplayer.db.entity.Genre;
import com.example.songplayer.viewmodel.GenreViewModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment implements CategoryAdapter.CategoryCallback {


    private RecyclerView recyclerView;
    private GenreViewModel genreViewModel;
    private CategoryAdapter adapter;
    public CategoryFragment() {
        // Required empty public constructor
    }

    private String TAG = "TESST";

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        genreViewModel = new ViewModelProvider(this,
                new ViewModelProvider.Factory() {
                    @NonNull
                    @Override
                    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                        return (T) new GenreViewModel(getActivity().getApplication());
                    }
                }).get(GenreViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.rvCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GenreViewModel genreViewModel = new GenreViewModel(getActivity().getApplication());
        adapter= new CategoryAdapter(genreViewModel.getAllGenres().getValue()==null?new ArrayList():genreViewModel.getAllGenres().getValue(),CategoryFragment.this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 20;
            }
        });
        genreViewModel.getAllGenres().observe(getViewLifecycleOwner(), genres -> adapter.setCategories(genres));

        return view;

    }


    @Override
    public void click(Genre genre) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.data), (Serializable) genre);
        MainActivity.getNavController().navigate(R.id.songListFragment, bundle);
    }
}