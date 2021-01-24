package com.example.songplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.db.entity.SongEntity;

import java.util.List;

public class SongAdapter2 extends RecyclerView.Adapter<SongAdapter2.ViewHolder> {


    private List<SongEntity> songs;
    private final List<Integer> gradients;
    private final SongAdapterCallback2 callback;
    private Object listItem;

    public SongAdapter2(List<SongEntity> songs, List<Integer> gradients, SongAdapterCallback2 callback, Object listItem) {
        this.songs = songs;
        this.callback = callback;
        this.gradients = gradients;
        this.listItem = listItem;
    }

    public void setSongs(List<SongEntity> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_song2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.btnDownloadSong.setOnClickListener((v) -> {
            callback.downloadSong(songs.get(position));
        });

        holder.btnDeleteFromList.setOnClickListener((v) -> {
            callback.deleteSongFromList(songs.get(position), listItem);
        });

        holder.btnMarkFavoriteSong.setOnClickListener((v)->{
            callback.toggleFavorite(songs.get(position));
        });

        SongEntity currentSong = songs.get(position);

        holder.txtSongName.setText(songs.get(position).getSongName());
        holder.itemView.setBackgroundResource(gradients.get(getItemViewType(position)));

        if (!currentSong.isOnline()) {
            holder.btnDownloadSong.setVisibility(View.INVISIBLE);
            holder.btnDownloadSong.setEnabled(false);

        } else {
            holder.btnDownloadSong.setEnabled(true);
            holder.btnDownloadSong.setVisibility(View.VISIBLE);

            holder.btnDownloadSong.setOnClickListener((view) -> {
                callback.downloadSong(currentSong);
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position % gradients.size();
    }


    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btnDeleteFromList;
        Button btnMarkFavoriteSong;
        Button btnDownloadSong;
        TextView txtSongName;
        ImageView imgSongThumbnail;

        public ViewHolder(@NonNull View view) {
            super(view);
            btnDeleteFromList = view.findViewById(R.id.btnDeleteFromList);
            btnMarkFavoriteSong = view.findViewById(R.id.btnMarkFavoriteSong);
            btnDownloadSong = view.findViewById(R.id.btnDownloadSong);

            txtSongName = view.findViewById(R.id.txtSongName);
            imgSongThumbnail = view.findViewById(R.id.imgSongThumbnail);

        }
    }

    public interface SongAdapterCallback2 {
        void onClickPlay(SongEntity song);


        void downloadSong(SongEntity songEntity);

        void deleteSongFromList(SongEntity songEntity, Object listObj);

        void toggleFavorite(SongEntity songEntity);
    }

}