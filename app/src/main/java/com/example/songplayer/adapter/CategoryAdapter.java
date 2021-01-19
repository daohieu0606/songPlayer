package com.example.songplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.Data.DummyData;
import com.example.songplayer.R;
import com.example.songplayer.db.entity.Genre;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Genre> categories;
    private List<Integer> colors = DummyData.gradients;
    public CategoryAdapter(List<Genre> newItemList) {
        categories = newItemList;
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_category, null);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (categories != null) {
            holder.itemView.setBackgroundResource(colors.get(position%colors.size()));
            holder.textCategory.setText(categories.get(position).getGenreName());
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Genre> genres) {
        categories = genres;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.categoryName);
        }
    }
}
