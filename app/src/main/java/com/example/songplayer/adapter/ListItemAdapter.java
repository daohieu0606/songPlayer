package com.example.songplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.db.entity.AlbumEntity;
import com.example.songplayer.db.entity.Playlist;

import java.util.List;

import static com.example.songplayer.utils.Constants.DEFAULT_NAME;

public class ListItemAdapter<E> extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {

    private List<E> listItems;
    private AlbumVerticalAdapterCallback callback;

    public ListItemAdapter(List<E> listItems, AlbumVerticalAdapterCallback callback) {
        this.callback = callback;
        this.listItems = listItems;
    }


    @NonNull
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_listitem_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemAdapter.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener((v) -> {
            if (listItems.get(position) instanceof AlbumEntity) {
                callback.viewAlbumDetail(listItems.get(position));
            } else if (listItems.get(position) instanceof Playlist) {
                callback.viewPlaylistDetail(listItems.get(position));
            }
        });
        holder.btnPlayAList.setOnClickListener((v) -> {
            callback.playAllList(listItems.get(position));
        });

        if (listItems.get(position) instanceof Playlist) {
            String name = ((Playlist) listItems.get(position)).getPlayListName();
            if ("".trim().equals(name) || name == null) {
                name = DEFAULT_NAME;
            }
            holder.txtListName.setText(name);



            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    callback.deletePlaylistItem(listItems.get(position) );

                    return false;
                }
            });
        } else if (listItems.get(position) instanceof AlbumEntity) {
            String name = ((AlbumEntity) listItems.get(position)).getAlbumName();

            if ("".trim().equals(name) || name == null) {
                name = DEFAULT_NAME;
            }
            holder.txtListName.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setListItems(List<E> albumEntities) {
        listItems = albumEntities;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtListName;
        public ImageButton btnPlayAList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtListName = itemView.findViewById(R.id.txtAlbumName);
            btnPlayAList = itemView.findViewById(R.id.btnPlayAlbum);
        }
    }

    public interface AlbumVerticalAdapterCallback<E> {
        void playAllList(E listItem);
        void viewAlbumDetail(E listItem);
        void viewPlaylistDetail(E listItem);
        void deletePlaylistItem(E listItem);
    }
}
