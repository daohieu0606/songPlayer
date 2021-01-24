package com.example.songplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.songplayer.R;
import com.example.songplayer.db.entity.SongEntity;
import com.example.songplayer.notification.NotificationHelper;
import com.example.songplayer.receiver.NotificationReceiver;
import com.example.songplayer.service.MusicService;
import com.example.songplayer.viewmodel.SongViewModel;

import java.util.ArrayList;

public class MusicPlayerFragment
        extends Fragment
        implements MediaController.MediaPlayerControl, View.OnClickListener, Runnable {

    public static final String INTENT_FILTER_ACTION = "SONG_CONTROL";
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
    private ToggleButton btnPlay2;

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
//    private boolean musicBound = false;

    private SongViewModel songViewModel;
    private MutableLiveData<SongEntity> currentSongLiveData = new MutableLiveData<>();

    private RepeatMode currentRepeatMode;

    private BroadcastReceiver songControlReceiver;

    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            musicService = MusicService.getInstance();
            if (musicService == null) {
                handler.postDelayed(this, 20);
            }
        }
    };

    public MusicPlayerFragment() {
        // Required empty public constructor
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


        currentSongLiveData.observe(getActivity(), songEntity -> {
            if (songEntity == null) {
                setDefaultUI();
            } else {
                try {
                    updateUIContent();
                } catch (Exception e) {
                    //TODO: HANDLE
                }
            }
        });

        NotificationHelper.createNotificationChannel(getContext());
        songControlReceiver = new NotificationReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                String message = intent.getStringExtra(INTENT_FILTER_ACTION);
                switch (message) {
                    case NotificationHelper.ACTION_PREVIOUS:
                        onTakePreSong();
                        break;
                    case NotificationHelper.ACTION_PLAY:
                        handlePlaySong();
                        break;
                    case NotificationHelper.ACTION_NEXT:
                        onTakeNextSong();
                        break;
                }
            }
        };
        getContext().registerReceiver(songControlReceiver, new IntentFilter(INTENT_FILTER_ACTION));
    }

    private void setDefaultUI() {
        txtSongTittle.setText("Song Name");
        txtSongName2.setText("Song Name");

        txtArtist.setText("Artist");
        txtArtist2.setText("Artist");

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art;
        art = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);

        btnPlay.setChecked(isPlaying());
    }

    private void onTakeNextSong() {
        playNext();
    }

    private void onTakePreSong() {
        playPrev();
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
        btnPlay2 = view.findViewById(R.id.btnPlay2);

        btnBackward.setOnClickListener(this::onClick);
        btnForward.setOnClickListener(this::onClick);
        btnPlay.setOnClickListener(this::onClick);
        btnPreSong.setOnClickListener(this::onClick);
        btnNextSong.setOnClickListener(this::onClick);
        btnMarkFavorite.setOnClickListener(this::onClick);
        btnShuffle.setOnClickListener(this::onClick);
        btnRepeat.setOnClickListener(this::onClick);
        btnPlay2.setOnClickListener(this::onClick);

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

    private void updateUIContent() throws Exception {
        SongEntity currentSong = currentSongLiveData.getValue();
        if (currentSong == null) {
            return;
        }
        txtSongTittle.setText(currentSong.getSongName());
        txtSongName2.setText(currentSong.getSongName());

        String artist = "Artist";
        if (!"".equals(currentSong.getArtist())) {
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
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        if (currentSong.isOnline()) {
            mmr.setDataSource(currentSong.getUriString());

        } else {
            mmr.setDataSource(getContext(), Uri.parse(currentSong.getUriString()));
        }

        rawArt = mmr.getEmbeddedPicture();
        if (null != rawArt) {
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);

            Glide.with(getContext()).load(art).apply(RequestOptions.circleCropTransform()).into(imgCenterSongThumbnail);
            imgSongThumbnail2.setImageBitmap(art);
        } else {
            // do nothing
        }


        btnMarkFavorite.setChecked(currentSong.isFavorite());

        btnPlay.setChecked(isPlaying());
    }

    private void playPrev() {
        if (musicService.takePreSong()) {
            playSong();
            currentSongLiveData.setValue(musicService.getCurrentSong());
        }
    }

    private void playNext() {
        if (musicService.takeNextSong()) {
            playSong();
            currentSongLiveData.setValue(musicService.getCurrentSong());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
        if (playIntent == null) {
            playIntent = new Intent(getActivity(), MusicService.class);
            Bundle bundle = getArguments();
            Object song = bundle.getSerializable(getString(R.string.SONG));
            if (song == null) {
                Object listSongsObj = bundle.getSerializable(getString(R.string.list_song));
                if (listSongsObj != null) {
                    ArrayList<SongEntity> data = (ArrayList)listSongsObj;
                    playIntent.putExtra(getString(R.string.list_song), data);

                    if(data.size()>0){
                        currentSongLiveData.setValue(data.get(0));
                    }else{
                        Toast.makeText(getContext(), "There is no songs", Toast.LENGTH_SHORT).show();
                    }
                    getContext().startService(playIntent);
                    handler.postDelayed(runnable, 0);

                } else {
                    Toast.makeText(getContext(), "There is no songs", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (getActivity() != null) {
                    currentSongLiveData.setValue((SongEntity) song);
                    playIntent.putExtra(getString(R.string.SONG), (SongEntity) song);
                    getContext().startService(playIntent);
                    handler.postDelayed(runnable, 0);
                }
            }


        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(songControlReceiver);
        getContext().stopService(playIntent);

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
        musicService.playMusic();
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
        } else if (musicService == null) {
            Toast.makeText(getContext(), "Your music service haven't connected yet", Toast.LENGTH_LONG).show();
            btnPlay.setChecked(false);
            result = false;
        }
        return result;
    }

    @Override
    public int getDuration() {
        if (musicService != null && isPlaying())
            return musicService.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicService != null && musicService.isPng())
            return musicService.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (musicService != null) {
            musicService.seek(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        if (musicService != null)
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
            if (!isPlaying()) {
                playSong();
            } else {
                musicService.pausePlayer();
            }
        }
        btnPlay.setChecked(isPlaying());
        btnPlay2.setChecked(isPlaying());
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
        SongEntity currentSong = currentSongLiveData.getValue();
        if (currentSong == null) {
            return;
        }
        currentSong.setFavorite(!currentSong.isFavorite());
        btnMarkFavorite.setChecked(currentSong.isFavorite());
        songViewModel.update(currentSongLiveData.getValue());
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
            case R.id.btnPlay2:
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