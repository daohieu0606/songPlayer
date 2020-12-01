package com.ledungcobra.songplayer.ui_adaper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ledungcobra.songplayer.R;
import com.ledungcobra.songplayer.ui_adaper.adaper_item.DrawerItem;

import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private OnDrawerItemClickListener listener;
    List<DrawerItem> drawerItemList;

    public DrawerAdapter(List<DrawerItem> drawerItems){
        this.drawerItemList = drawerItems;
    }

    public void setOnItemClickListener(OnDrawerItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.drawer_item,parent,false);

        if(viewType == 1){
            itemView = inflater.inflate(R.layout.drawer_item_underlined,parent,false);
        }

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(drawerItemList.get(position));
            }
        });

        holder.imgIcon.setImageResource(drawerItemList.get(position).getIconID());
        holder.tvDrawerItemTitle.setText(drawerItemList.get(position).getDrawerTitle());

        if(position == 3){
            holder.tvCategory.setText("Community");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 3){
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return drawerItemList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView imgIcon;
        public TextView tvDrawerItemTitle;
        public TextView tvCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imv_icon);
            imgIcon.setColorFilter(Color.WHITE);
            tvDrawerItemTitle = itemView.findViewById(R.id.tv_menu_title);
            tvCategory = itemView.findViewById(R.id.tv_menu_category);
        }
    }

    public interface OnDrawerItemClickListener{
        void onClick(DrawerItem item);
    }
}
