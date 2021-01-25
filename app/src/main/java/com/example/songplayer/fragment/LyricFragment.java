package com.example.songplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.songplayer.R;

public class LyricFragment extends Fragment {


    public LyricFragment() {

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
        View viewResult = inflater.inflate(R.layout.fragment_lyric, container, false);
        String lyric = getArguments().getString("LYRIC");
        TextView txtLyric = viewResult.findViewById(R.id.txtLyric);

        if(lyric!=null){
            txtLyric.setText(lyric);
        }

        return viewResult;
    }



}
