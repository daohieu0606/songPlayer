package com.example.songplayer.dao;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.songplayer.db.entity.PlaylistEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlPlaylistDAOImplement implements PlaylistDAO {
    private MutableLiveData<List<PlaylistEntity>> listMutableLiveData;
    private Application context;

    public OnlPlaylistDAOImplement(Application context) {
        this.context = context;

        this.listMutableLiveData = new MutableLiveData<>();
        this.listMutableLiveData.setValue(new ArrayList<>());

    }

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Playlist");

    private void loadAllPlaylist() {
        ArrayList<PlaylistEntity> PlaylistEntities = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        PlaylistEntities.add(ds.getValue(PlaylistEntity.class));
                    }

                    listMutableLiveData.setValue(PlaylistEntities);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", String.valueOf(error));
            }
        });
    }

    @Override
    public MutableLiveData<List<PlaylistEntity>> getAllPlaylist() {
        return this.listMutableLiveData;
    }

    @Override
    public void insert(PlaylistEntity playlistEntity) {
        ref.push().setValue(playlistEntity);
    }

    @Override
    public void update(PlaylistEntity playlistEntity) {
        Query playlistUpdate = ref.orderByChild("id").equalTo(playlistEntity.getId());
        playlistUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("id", playlistEntity.getId());
                        result.put("playlistName", playlistEntity.getPlaylistName());
                        result.put("uriString", playlistEntity.getPlaylistUri());
                        result.put("listSongID", playlistEntity.getListSong());

                        ds.getRef().updateChildren(result);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", String.valueOf(error));
            }
        });

    }


    @Override
    public void delete(int ID) {
        Query playlistDel = ref.orderByChild("id").equalTo(ID);
        playlistDel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ds.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", String.valueOf(error));
            }
        });
    }
}
