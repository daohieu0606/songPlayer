package com.example.songplayer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.db.entity.ArtistEntity;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder>{
    private List<ArtistEntity> artistEntityList;
    private Activity activity;

    public ArtistAdapter(Activity newActivity, List<ArtistEntity> newItemList) {
        artistEntityList = newItemList;
        activity = newActivity;
    }

    public ArtistAdapter(Activity newActivity) {
        activity = newActivity;
        artistEntityList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ArtistAdapter.ViewHolder result = null;

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_catalogue_recyclerview, null);
        result = new ArtistAdapter.ViewHolder(view);

        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.ViewHolder holder, int position) {
        if (0 <= position && artistEntityList.size() > position) {
            holder.txtSinger.setText(artistEntityList.get(position).getArtistName());
        }
    }

    @Override
    public int getItemCount() {
        return artistEntityList.size();
    }

    public void setArtists(List<ArtistEntity> artistEntities) {
        artistEntityList = artistEntities;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtSinger;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSinger = itemView.findViewById(R.id.txtSinger);
        }
    }
}
