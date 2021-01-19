package com.example.songplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.fragment.DashboardFragment;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<AlbumEntity> albums;
    private DashboardFragment fragment;
    public AlbumAdapter(List<AlbumEntity> newItemList, DashboardFragment fragment) {
        this.fragment = fragment;
        this.albums = newItemList;
    }


    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {

        holder.txtAlbumName.setText(albums.get(position).getAlbumName());
        holder.itemView.setOnClickListener((v)->{
            fragment.displayListSongOfAlbum(albums.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void setAlbum(List<AlbumEntity> albumEntities) {
        albums = albumEntities;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtAlbumName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAlbumName = itemView.findViewById(R.id.txtAlbumName);
        }
    }
}
