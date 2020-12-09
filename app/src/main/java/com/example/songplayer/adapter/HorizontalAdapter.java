package com.example.songplayer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder>{

    private List<String> itemList;
    private Activity activity;

    public HorizontalAdapter(Activity newActivity, List<String> newItemList) {
        itemList = newItemList;
        activity = newActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder result = null;

        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.item_catalogue_recyclerview, null);
        result = new ViewHolder(view);

        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (0 <= position && itemList.size() > position) {
            holder.txtSinger.setText(itemList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtSinger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSinger = itemView.findViewById(R.id.txtSinger);
        }
    }
}
