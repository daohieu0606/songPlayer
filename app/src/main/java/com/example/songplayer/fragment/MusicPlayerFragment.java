package com.example.songplayer.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.songplayer.R;
import com.example.songplayer.controller.MusicController;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.service.MusicService;
import com.example.songplayer.service.MusicService.MusicBinder;
import com.example.songplayer.utils.SongDbHelper;
import com.example.songplayer.viewmodel.SongViewModel;

public class MusicPlayerFragment
        extends Fragment
        implements MediaController.MediaPlayerControl, View.OnClickListener, Runnable {

    private static final String TAG = "MUSIC_FRAG";
    private final int SEEK_FORWARD_TIME = 5 * 1000;
    private final int SEEK_BACKWARD_TIME = 5 * 1000;
    // VIEW
    private ToggleButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;

    private TextView txtSongTittle;
    private TextView txtArtist;
    private TextView txtDuration;
    private TextView txtCurrentPosition;

    private SeekBar sbSongProcess;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    private MusicController musicController;
    private SongViewModel songViewModel;
    private SongEntity currentSong;
    private int currentSongId;

    public MusicPlayerFragment() {
        // Required empty public constructor
    }
    public MusicPlayerFragment(int songId) {
        currentSongId = songId;
    }
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;

            musicService = binder.getService();
            musicBound = true;
            if (currentSongId != 0) {
                for (SongEntity song :
                        songViewModel.getAllSongs().getValue()) {
                    if (song.getId() == currentSongId) {
                        setCurrentSong(song);
                        break;
                    }
                }
            } else if(songViewModel.getAllSongs().getValue().size() > 0) {
                setCurrentSong(songViewModel.getAllSongs().getValue().get(0));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };
    private void setCurrentSong(SongEntity newSong) {
        currentSong = newSong;

        if (musicService != null) {
            Log.d(TAG, "setCurrentSong: faskfjlsfjs");

            musicService.setCurrentSong(currentSong);
            musicService.preparePlay();
            updateUIContent();
            musicBound = true;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songViewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SongViewModel(getActivity().getApplication());
            }
        }).get(SongViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        initUI(view);
        setMusicController(view);
        return view;
    }

    private void initUI(View view) {
        btnPlay = view.findViewById(R.id.btnPlay);
        btnForward = view.findViewById(R.id.btnForward);
        btnBackward = view.findViewById(R.id.btnBackward);

        btnBackward.setOnClickListener(this::onClick);
        btnForward.setOnClickListener(this::onClick);
        btnPlay.setOnClickListener(this::onClick);

        txtSongTittle = view.findViewById(R.id.txtSongTittle);
        txtArtist = view.findViewById(R.id.txtArtist);
        txtDuration = view.findViewById(R.id.txtDuration);
        txtCurrentPosition = view.findViewById(R.id.txtCurrentPosition);

        if (currentSong != null) {
            updateUIContent();
        }

        sbSongProcess = view.findViewById(R.id.sbSongProcess);
        sbSongProcess.setProgress(0);
        sbSongProcess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0 && musicService != null && !musicService.isPng()) {
                    //handle when progress = 0 here
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!isPlayable()) {
                    sbSongProcess.setProgress(0);
                    return;
                }
                seekTo(sbSongProcess.getProgress());
            }
        });
    }

    private void updateUIContent() {
        txtArtist.setText(String.valueOf((int) currentSong.getSize() / 1000000) + " MB");
        txtSongTittle.setText(currentSong.getSongName());

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
            playIntent = new Intent(getActivity(), MusicService.class);
            getContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }

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

    private void playSong() {
        if (!isPlayable()) {
            return;
        }
        musicService.go ();
        sbSongProcess.setMax(getDuration());
        String durationTimeStr = getTimeStringFromMilliSeconds(getDuration());
        txtDuration.setText(durationTimeStr);
        new Thread(this).start();
    }

    private boolean isPlayable() {
        boolean result = true;

        if (currentSong == null) {
            Toast.makeText(getContext(), "Your device has no song", Toast.LENGTH_LONG).show();
            btnPlay.setChecked(false);
            result = false;
        } else if (musicService == null){
            Toast.makeText(getContext(), "Your music service haven't connected yet", Toast.LENGTH_LONG).show();
            btnPlay.setChecked(false);
            result = false;
        }
        return  result;
    }

    @Override
    public int getDuration() {
        if(musicService!=null && musicBound && isPlaying())
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
        if (musicService != null && musicBound) {
            musicService.seek(pos);
        }
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

    private void handlePlaySong() {
        if (musicService != null) {
            if (btnPlay.isChecked()) {
                playSong();
            } else {
                musicService.pausePlayer();
            }
        }
    }

    private void processForward() {
        if (!isPlayable()) {
            return;
        }
        Log.d(TAG, "processForward: " + getCurrentPosition());
        int pos = getCurrentPosition() + SEEK_FORWARD_TIME;
        seekTo(pos);
        Log.d(TAG, "processForward: " + getCurrentPosition());
    }

    private void processBackward() {
        if (!isPlayable()) {
            return;
        }
        Log.d(TAG, "processBackward: " + getCurrentPosition());
        int pos = getCurrentPosition() - SEEK_BACKWARD_TIME;
        seekTo(pos);
        Log.d(TAG, "processBackward: " + getCurrentPosition());
    }

    String getTimeStringFromMilliSeconds(int durationInMillis) {
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        String time = "";
        if (hour <= 0) {
            time = String.format("%d:%d", minute, second);

        } else {
            time = String.format("%0d:%2d:%2d", hour, minute, second);
        }
        return time;
    }

    @Override
    public void run() {
        int curPos = getCurrentPosition();
        int songDuration = getDuration();

        while (musicService != null && musicService.isPng() && curPos < songDuration) {
            try {
                Thread.sleep(1000);
                curPos = getCurrentPosition();
                if (curPos == 0) {
                    break;
                }
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            final int process = curPos;
            final String curPosTimeString = getTimeStringFromMilliSeconds(curPos);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtCurrentPosition.setText(String.valueOf(curPosTimeString));
                    sbSongProcess.setProgress(process);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnBackward:
                processBackward();
                break;
            case R.id.btnDownload:
                break;
            case R.id.btnForward:
                processForward();
                break;
            case R.id.btnPlay:
                handlePlaySong();
                break;
        }
    }

}