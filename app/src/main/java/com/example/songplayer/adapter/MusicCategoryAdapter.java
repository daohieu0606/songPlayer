package com.example.songplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;

import java.util.ArrayList;

public class MusicCategoryAdapter extends RecyclerView.Adapter<MusicCategoryAdapter.ViewHolder> {

    private ArrayList<String> listCategories;

    public MusicCategoryAdapter(ArrayList<String> listCategories) {
        this.listCategories = listCategories;
    }

    public void setListCategories(ArrayList<String> categories){
        this.listCategories = categories;
    }
    @NonNull
    @Override
    public MusicCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.music_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicCategoryAdapter.ViewHolder holder, int position) {
            holder.txtCategory.setText(listCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return listCategories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
        }
    }
}
