package com.example.songplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.MyApplication;
import com.example.songplayer.R;
import com.example.songplayer.adapter.ListPlayListAdapter;
import com.example.songplayer.db.entity.ListMusicOfPlaylist;
import com.example.songplayer.db.entity.Playlist;
import com.example.songplayer.db.entity.SongEntity;

import java.util.ArrayList;

public class MyDialog {

    private AlertDialog dialog;

    public MyDialog(Context context, String titleText, SongEntity song, ArrayList<Playlist> playlists) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inf.inflate(R.layout.dialog, null);
        builder.setView(view);

        EditText playListName = view.findViewById(R.id.edtPlayListName);

        RecyclerView recyclerView = view.findViewById(R.id.rvPlaylistList);
        recyclerView.setAdapter(new ListPlayListAdapter(playlists,(playList)->{
            new Thread(()->{
                MyApplication.database.listMusicOfPlaylistDAORoom().insert(new ListMusicOfPlaylist(playList.getPlaylistID(),song.getId()));
                ((Activity)context).runOnUiThread(() -> {
                    Toast.makeText(MyApplication.getContext(),"Insert the song into playlist "+ playList.getPlayListName()+ " successfylly",Toast.LENGTH_SHORT).show();
                });
            }).start();
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        view.findViewById(R.id.add_new_play_list).setOnClickListener((v) -> {
            new Thread(() -> {
                Playlist playlist = new Playlist(playListName.getText().toString());

                MyApplication.database.playlistDAORoom()
                        .insert(playlist);
                int id = playlist.getPlaylistID();

                MyApplication.database.listMusicOfPlaylistDAORoom()
                        .insert(new ListMusicOfPlaylist(id, song.getId()));


            }).start();
        });

        TextView title = new TextView(context);
        title.setText(titleText);
        title.setBackgroundColor(context.getColor(R.color.colorAccent));
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);

        title.setTextSize(20);

        builder.setCustomTitle(title);
        dialog = builder.create();
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.hide();
    }


}
