package com.ledungcobra.songplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.ledungcobra.songplayer.ui_adaper.MusicCategoryAdapter;

import java.util.ArrayList;
import java.util.Collections;


public class HomePageFragment extends Fragment {

    //View
    RecyclerView rcvListCategories;


    public HomePageFragment() {
        // Required empty public constructor
    }


    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_screen_holder, container, false);
        rcvListCategories = view.findViewById(R.id.rcvListCategories);

        rcvListCategories.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        ArrayList<String> categories = new ArrayList<>();
        Collections.addAll(categories, "Animal", "Ballad");
        rcvListCategories.setAdapter(new MusicCategoryAdapter(categories));

        return view;
    }
}