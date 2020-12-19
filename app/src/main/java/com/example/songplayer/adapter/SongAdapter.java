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

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {



    private final List<SongEntity> songs;
    private final List<Integer> gradients;

    public SongAdapter(List<SongEntity> songs, List<Integer> gradients) {
        this.songs = songs;
        this.gradients = gradients;
    }

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position%gradients.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.ViewHolder holder, int position) {

        final SongEntity currentSong = songs.get(position);
        SongAdapterCallback callback = null;

        if (holder.itemView.getContext() instanceof SongAdapterCallback) {
            callback = (SongAdapterCallback) holder.itemView.getContext();
        } else {
            try {
                throw new Exception("Haven't implemented SongAdapterCallback in activity or fragment yet !!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (currentSong != null) {
            holder.txtSongName.setText(currentSong.getSongName());
            SongAdapterCallback finalCallback = callback;
            holder.btnDownloadSong.setOnClickListener((view) -> {
                finalCallback.downloadASong(currentSong);
            });

            holder.btnMarkFavoriteSong.setOnClickListener((view)->{
                finalCallback.favoriteASong(currentSong);
            });
        }

        holder.itemView.setBackgroundResource(gradients.get(getItemViewType(position)));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btnMoreSongOptions;
        Button btnMarkFavoriteSong;
        Button btnDownloadSong;
        TextView txtSongName;
        ImageView imgSongThumbnail;

        public ViewHolder(@NonNull View view) {
            super(view);

            btnMoreSongOptions = view.findViewById(R.id.btnMoreSongOptions);
            btnMarkFavoriteSong = view.findViewById(R.id.btnMarkFavoriteSong);
            btnDownloadSong = view.findViewById(R.id.btnDownloadSong);
            txtSongName = view.findViewById(R.id.txtSongName);
            imgSongThumbnail = view.findViewById(R.id.imgSongThumbnail);



        }
    }

    public static interface SongAdapterCallback {
        void downloadASong(SongEntity song);

        void favoriteASong(SongEntity song);

        void setASongAsRingTone(SongEntity song);

        void setASongAsNotification(SongEntity song);

        void setASongAsAlarm(SongEntity song);

    }
}
