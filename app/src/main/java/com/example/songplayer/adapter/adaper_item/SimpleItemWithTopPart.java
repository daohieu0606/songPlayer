package com.example.songplayer.adapter.adaper_item;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.songplayer.R;
import com.example.songplayer.adapter.DrawerAdapter;

public class SimpleItemWithTopPart extends DrawerItem<SimpleItemWithTopPart.ViewHolder> {

    private int selectedItemIconTint;
    private int selectedItemTextTint;

    private int normalItemIconTint;
    private int normalItemTextTint;

    private final Drawable icon;
    private final String title;

    private final String topTitle;

    public SimpleItemWithTopPart(Drawable icon, String title,String topTitle) {
        this.icon = icon;
        this.title = title;
        this.topTitle = topTitle;

    }

    @Override
    public SimpleItemWithTopPart.ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.option_item_with_top_part, parent, false);
        return new SimpleItemWithTopPart.ViewHolder(v);
    }

    @Override
    public void bindViewHolder(SimpleItemWithTopPart.ViewHolder holder) {
        holder.title.setText(title);
        holder.topTitle.setText(topTitle);
        holder.topTitle.setTextColor(normalItemTextTint);
        holder.icon.setImageDrawable(icon);

        holder.title.setTextColor(isChecked ? selectedItemTextTint : normalItemTextTint);
        holder.title.setTextColor(isChecked ? selectedItemTextTint : normalItemTextTint);



    }

    public SimpleItemWithTopPart withSelectedIconTint(int selectedItemIconTint) {
        this.selectedItemIconTint = selectedItemIconTint;
        return this;
    }

    public SimpleItemWithTopPart withSelectedTextTint(int selectedItemTextTint) {
        this.selectedItemTextTint = selectedItemTextTint;
        return this;
    }

    public SimpleItemWithTopPart withIconTint(int normalItemIconTint) {
        this.normalItemIconTint = normalItemIconTint;
        return this;
    }

    public SimpleItemWithTopPart withTextTint(int normalItemTextTint) {
        this.normalItemTextTint = normalItemTextTint;
        return this;
    }

    static class ViewHolder extends DrawerAdapter.ViewHolder {

        private ImageView icon;
        private TextView title;
        private TextView topTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            topTitle = itemView.findViewById(R.id.txtTopTitle);

        }
    }

}
