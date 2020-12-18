package com.example.songplayer.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.songplayer.R;
import com.example.songplayer.controller.MusicController;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.service.MusicService;
import com.example.songplayer.service.MusicService.MusicBinder;
import com.example.songplayer.utils.SongDbHelper;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayerFragment extends Fragment implements MediaController.MediaPlayerControl {

    private static final String TAG = "MUSIC_FRAG";
    // VIEW
    private ToggleButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnDownload;
    private ImageButton btnPlay2;
    private ToggleButton btnLove;

    private TextView txtSongTittle;
    private TextView txtArtist;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    private MusicController musicController;
    private SongEntity currentSong;

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    public static MusicPlayerFragment newInstance() {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        /*following code for test*/
        SongDbHelper songDbHelper = new SongDbHelper(getActivity().getApplication());
        currentSong = songDbHelper.getAllSongs().get(0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        initUI(view);
        setMusicController(view);
        return view;
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;

            musicService = binder.getService();

            //test
            musicService.setCurrentSong(currentSong);
            musicService.preparePlay();

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void initUI(View view) {
        btnPlay = view.findViewById(R.id.btnPlay);
        btnForward = view.findViewById(R.id.btnForward);
        btnBackward = view.findViewById(R.id.btnBackward);
        btnDownload = view.findViewById(R.id.btnDownload);
        btnPlay2 = view.findViewById(R.id.btnPlay2);

        btnLove = view.findViewById(R.id.btnLove);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService != null) {
                    if (btnPlay.isChecked()) {
                        musicService.go();
                    } else {
                        musicService.pausePlayer();
                    }
                }
            }
        });

        txtSongTittle = view.findViewById(R.id.txtSongTittle);
        txtSongTittle.setText(currentSong.getSongName());

        txtArtist = view.findViewById(R.id.txtArtist);
        txtArtist.setText(String.valueOf((int) currentSong.getSize() / 1000000) + " MB");
    }

    private void setMusicController(View anchor) {
        musicController = new MusicController(getContext());

        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        musicController.setMediaPlayer(this);
        musicController.setAnchorView(anchor.findViewById(R.id.timeControlContainer));
        musicController.setEnabled(true);
    }

    private void playPrev() {

    }

    private void playNext() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            Log.d(TAG, "onStart: create playintent");
            playIntent = new Intent(getActivity(), MusicService.class);
            getContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
         //   getContext().startService(playIntent);
        }

        if (playIntent != null) {
            //musicService.setCurrentSong(songEntity);
            if (currentSong == null) {
                Log.d(TAG, "onCreate: song entity is null");
            } else {
                Log.d(TAG, "onCreate: song entity is not null");
            }
        }
        if (musicService == null) {
            Log.d(TAG, "ondestroy: service is null");
        } else {
            Log.d(TAG, "ondestroy: service is not null");
        }
        Log.d(TAG, "onStart: is playing" + isPlaying());
        Log.d(TAG, "onStart: is playing" + canPause());
        Log.d(TAG, "onStart: is playing" + canSeekBackward());
        Log.d(TAG, "onStart: is playing" + canSeekForward());
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {

        getActivity().stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        if(musicService!=null && musicBound && musicService.isPng())
            return musicService.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicService!=null &&  musicBound && musicService.isPng())
            return musicService.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        if(musicService!=null && musicBound)
            return musicService.isPng();
        else return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}