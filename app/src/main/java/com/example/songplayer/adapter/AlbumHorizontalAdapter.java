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

import static com.example.songplayer.utils.Constants.DEFAULT_NAME;

public class AlbumHorizontalAdapter extends RecyclerView.Adapter<AlbumHorizontalAdapter.ViewHolder> {

    private List<AlbumEntity> albums;
    private DashboardFragment fragment;
    public AlbumHorizontalAdapter(List<AlbumEntity> newItemList, DashboardFragment fragment) {
        this.fragment = fragment;
        this.albums = newItemList;
    }


    @NonNull
    @Override
    public AlbumHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHorizontalAdapter.ViewHolder holder, int position) {

        String name =albums.get(position).getAlbumName();

        if(name == null || "".trim().equals(name)){
            name = DEFAULT_NAME;
        }

        holder.txtAlbumName.setText(name);
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
