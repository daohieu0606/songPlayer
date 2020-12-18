package com.example.songplayer.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.songplayer.R;
import com.example.songplayer.controller.MusicController;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.service.MusicService;

public class SongListFragment extends Fragment {



    public SongListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewResult = inflater.inflate(R.layout.fragment_song_list, container, false);

        setUpUI(viewResult);
        return viewResult;
    }

    private void setUpUI(View view) {

    }


}