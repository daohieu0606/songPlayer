package com.example.songplayer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.db.entity.AlbumEntity;

import java.util.List;
import java.util.Random;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    private List<AlbumEntity> itemList;
    private Activity activity;
    private int[] itemBackground;

    public AlbumAdapter(Activity newActivity, List<AlbumEntity> newItemList) {
        itemList = newItemList;
        activity = newActivity;

        itemBackground = new int[10];
        itemBackground[0] = R.drawable.gradient1;
        itemBackground[1] = R.drawable.gradient2;
        itemBackground[2] = R.drawable.gradient3;
        itemBackground[3] = R.drawable.gradient4;
        itemBackground[4] = R.drawable.gradient5;
        itemBackground[5] = R.drawable.gradient6;
        itemBackground[6] = R.drawable.gradient7;
        itemBackground[7] = R.drawable.gradient8;
        itemBackground[8] = R.drawable.gradient9;
        itemBackground[9] = R.drawable.gradient10;

    }

    public AlbumAdapter(Activity newActivity) {
        activity = newActivity;

        itemBackground = new int[10];
        itemBackground[0] = R.drawable.gradient1;
        itemBackground[1] = R.drawable.gradient2;
        itemBackground[2] = R.drawable.gradient3;
        itemBackground[3] = R.drawable.gradient4;
        itemBackground[4] = R.drawable.gradient5;
        itemBackground[5] = R.drawable.gradient6;
        itemBackground[6] = R.drawable.gradient7;
        itemBackground[7] = R.drawable.gradient8;
        itemBackground[8] = R.drawable.gradient9;
        itemBackground[9] = R.drawable.gradient10;

    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AlbumAdapter.ViewHolder result = null;

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_listview, null);
        result = new AlbumAdapter.ViewHolder(view);

        Random random = new Random();
        view.setBackgroundResource(itemBackground[random.nextInt(10)]);
        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        if (0 <= position && itemList.size() > position) {
            holder.txtCatalogueName.setText(itemList.get(position).getAlbumName());
            if (itemList.get(position).getArtUri() != null) {
                holder.albumThumbnail.setImageURI(itemList.get(position).getArtUri());
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setAlbum(List<AlbumEntity> albumEntities) {
        itemList = albumEntities;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtCatalogueName;
        public ImageView albumThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCatalogueName = itemView.findViewById(R.id.txtCatalogueName);
            albumThumbnail = itemView.findViewById(R.id.songIcon);
        }
    }
}
