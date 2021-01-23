package com.example.songplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.db.entity.Playlist;

import java.util.ArrayList;

public class ListPlayListAdapter extends RecyclerView.Adapter<ListPlayListAdapter.ViewHolder> {

    private ArrayList<Playlist> playlists;
    private ListPlayListCallback callback;

    public ListPlayListAdapter(ArrayList<Playlist> playlists, ListPlayListCallback callback) {
        this.playlists = playlists;
        this.callback = callback;

    }

    @NonNull
    @Override
    public ListPlayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPlayListAdapter.ViewHolder holder, int position) {
            holder.playlistName.setText(playlists.get(position).getPlayListName());
            holder.btnPlaylist.setOnClickListener((v)->{
                callback.add(playlists.get(position));
            });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView playlistName;
        public ImageButton btnPlaylist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistName = itemView.findViewById(R.id.playlistName);
            btnPlaylist = itemView.findViewById(R.id.btnPlaylistAdd);

        }
    }

    @FunctionalInterface
    public interface ListPlayListCallback {
        void add(Playlist playlist);
    }
}
