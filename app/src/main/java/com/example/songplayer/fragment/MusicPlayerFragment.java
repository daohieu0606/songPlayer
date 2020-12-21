package com.example.songplayer.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.songplayer.R;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.service.MusicService;
import com.example.songplayer.service.MusicService.MusicBinder;
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
    private ImageButton btnPreSong;
    private ImageButton btnNextSong;
    private ToggleButton btnMarkFavorite;
    private ToggleButton btnShuffle;
    private ImageButton btnRepeat;

    private TextView txtSongTittle;
    private TextView txtArtist;
    private TextView txtDuration;
    private TextView txtCurrentPosition;
    private TextView txtSongName2;
    private TextView txtArtist2;

    private ImageView imgSongThumbnail2;
    private ImageView imgCenterSongThumbnail;

    private SeekBar sbSongProcess;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    private SongViewModel songViewModel;
    private int currentSongId;
    private RepeatMode currentRepeatMode;

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
            musicService.setSongList(songViewModel.getAllSongs().getValue());
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

        if (musicService != null) {
            musicService.setCurrentSong(newSong);
            musicService.preparePlay();
            updateUIContent();
            musicBound = true;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentRepeatMode = RepeatMode.NEVER;

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
        return view;
    }

    private void initUI(View view) {
        btnPlay = view.findViewById(R.id.btnPlay);
        btnForward = view.findViewById(R.id.btnForward);
        btnBackward = view.findViewById(R.id.btnBackward);
        btnPreSong = view.findViewById(R.id.btnPreSong);
        btnNextSong = view.findViewById(R.id.btnNextSong);
        btnMarkFavorite = view.findViewById(R.id.btnMarkFavorite);
        btnShuffle = view.findViewById(R.id.btnShuffle);
        btnRepeat = view.findViewById(R.id.btnRepeat);

        btnBackward.setOnClickListener(this::onClick);
        btnForward.setOnClickListener(this::onClick);
        btnPlay.setOnClickListener(this::onClick);
        btnPreSong.setOnClickListener(this::onClick);
        btnNextSong.setOnClickListener(this::onClick);
        btnMarkFavorite.setOnClickListener(this::onClick);
        btnShuffle.setOnClickListener(this::onClick);
        btnRepeat.setOnClickListener(this::onClick);

        txtSongTittle = view.findViewById(R.id.txtSongTittle);
        txtArtist = view.findViewById(R.id.txtArtist);
        txtDuration = view.findViewById(R.id.txtDuration);
        txtCurrentPosition = view.findViewById(R.id.txtCurrentPosition);
        txtSongName2 = view.findViewById(R.id.txtSongName2);
        txtArtist2 = view.findViewById(R.id.txtArtist2);

        imgSongThumbnail2 = view.findViewById(R.id.imgSongThumbnail2);
        imgCenterSongThumbnail = view.findViewById(R.id.imgCenterSongThumbnail);

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
        SongEntity currentSong = musicService.getCurrentSong();
        if (currentSong == null) {
            return;
        }
        txtSongTittle.setText(currentSong.getSongName());
        txtSongName2.setText(currentSong.getSongName());

        String artist = "Artist";
        if (!currentSong.getArtist().isEmpty()) {
            artist = currentSong.getArtist();
        }
        txtArtist.setText(artist);
        txtArtist2.setText(artist);

        if (currentSong.isFavorite()) {
            btnMarkFavorite.setChecked(true);
        }

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art;
        BitmapFactory.Options bfo=new BitmapFactory.Options();

        mmr.setDataSource(getContext(), Uri.parse(currentSong.getUriString()));
        rawArt = mmr.getEmbeddedPicture();

        if (null != rawArt){
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);

            Glide.with(getContext()).load(art).apply(RequestOptions.circleCropTransform()).into(imgCenterSongThumbnail);
            imgSongThumbnail2.setImageBitmap(art);
        } else {
            // do nothing
        }

    }

    private void playPrev() {
        if (musicService.takePreSong()) {
            updateUIContent();
            playSong();
        }
    }

    private void playNext() {
        if (musicService.takeNextSong()){
            updateUIContent();
            playSong();
        }
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
        musicService.go();
        sbSongProcess.setMax(getDuration());
        Log.d(TAG, "playSong: duration " + getDuration());
        String durationTimeStr = getTimeStringFromMilliSeconds(getDuration());
        txtDuration.setText(durationTimeStr);
        new Thread(this).start();
    }

    private boolean isPlayable() {
        boolean result = true;

        if (musicService.getCurrentSong() == null) {
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

    private void handleMarkFavorite() {
        SongEntity currentSong = musicService.getCurrentSong();
        currentSong.setFavorite(!currentSong.isFavorite());
        songViewModel.update(currentSong);
    }

    private void handleChangeShuffle() {
        musicService.setShuffle(btnShuffle.isChecked());
    }

    private void changeRepeatMode() {
        if (currentRepeatMode.equals(RepeatMode.NEVER)) {
            currentRepeatMode = RepeatMode.ONLY_ONE;
            btnRepeat.setBackgroundResource(R.drawable.ic_repeat_only_one);
        } else if (currentRepeatMode.equals(RepeatMode.ONLY_ONE)) {
            currentRepeatMode = RepeatMode.ALL;
            btnRepeat.setBackgroundResource(R.drawable.ic_repeat_all);
        } else if (currentRepeatMode.equals(RepeatMode.ALL)) {
            currentRepeatMode = RepeatMode.NEVER;
            btnRepeat.setBackgroundResource(R.drawable.ic_repeat_never);
        }
        musicService.setRepeatMode(currentRepeatMode);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnBackward:
                processBackward();
                break;
            case R.id.btnRepeat:
                changeRepeatMode();
                break;
            case R.id.btnForward:
                processForward();
                break;
            case R.id.btnPlay:
                handlePlaySong();
                break;
            case R.id.btnPreSong:
                playPrev();
                break;
            case R.id.btnNextSong:
                playNext();
                break;
            case R.id.btnMarkFavorite:
                handleMarkFavorite();
                break;
            case R.id.btnShuffle:
                handleChangeShuffle();
                break;
        }
    }
}