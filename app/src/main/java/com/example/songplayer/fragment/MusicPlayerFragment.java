package com.example.songplayer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.songplayer.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayerFragment extends Fragment {

    // VIEW
    private ImageButton ibtnPlay;
    private ImageButton ibtnForward;
    private ImageButton ibtnBackward;
    private ImageButton ibtnDownload;
    private ImageButton ibtnPlay2;
    private ImageButton ibtnLove;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_music_player, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view) {
        ibtnPlay = view.findViewById(R.id.ibtnPlay);
        ibtnForward = view.findViewById(R.id.ibtnForward);
        ibtnBackward = view.findViewById(R.id.ibtnBackward);
        ibtnDownload = view.findViewById(R.id.ibtnDownload);
        ibtnPlay2 = view.findViewById(R.id.ibtnPlay2);

        ibtnLove = view.findViewById(R.id.ibtnLove);

        ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.string.pause_ic).equals((String) view.getTag())) {
                    ibtnPlay.setImageResource(R.drawable.playic);
                    ibtnPlay.setTag(getString(R.string.play_ic));
                } else {
                    ibtnPlay.setImageResource(R.drawable.pauseic);
                    ibtnPlay.setTag(getString(R.string.pause_ic));

                }
            }
        });

        ibtnPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.string.pause_ic).equals((String) view.getTag())) {
                    ibtnPlay2.setImageResource(R.drawable.playic);
                    ibtnPlay2.setTag(getString(R.string.play_ic));
                } else {
                    ibtnPlay2.setImageResource(R.drawable.pauseic);
                    ibtnPlay2.setTag(getString(R.string.pause_ic));

                }
            }
        });

        ibtnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getString(R.string.not_love).equals(view.getTag())) {
                    ibtnLove.setImageResource(R.drawable.favimagered);
                    ibtnLove.setTag(getString(R.string.love));
                } else {
                    ibtnLove.setImageResource(R.drawable.favsic);
                    ibtnLove.setTag(getString(R.string.not_love));
                }
            }
        });


    }
}