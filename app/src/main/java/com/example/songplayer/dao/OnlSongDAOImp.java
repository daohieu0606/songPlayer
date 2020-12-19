package com.example.songplayer.dao;

import android.app.Application;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.songplayer.db.entity.SongEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlSongDAOImp implements SongDAO{
    private static final String TAG = "TESST";
    private MutableLiveData<List<SongEntity>> listMutableLiveData;
    private Application context;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SongEntity");

    public OnlSongDAOImp(Application newContext) {
        context = newContext;
        listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.setValue(new ArrayList<>());

        loadDefaultSongList();
    }

    //Lấy tất cả dữ liệu trên firebase rồi gán vào listMutableLiveData
    private void loadDefaultSongList() {
        ArrayList<SongEntity> songEntities =  new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        songEntities.add(ds.getValue(SongEntity.class));
                    }

                    listMutableLiveData.setValue(songEntities);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public MutableLiveData<List<SongEntity>> getAllSongs() {
        return listMutableLiveData;
    }

    // Xóa một file trên firebase dựa trên id
    public void delete(int ID) {
        Query songDel = ref.orderByChild("id").equalTo(ID);
        songDel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        ds.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled", error.toException());
            }
        });
    }

    //thêm một file lên firebase
    public void insert(SongEntity songEntity) {

        ref.push().setValue(songEntity);

    }

    //update một file trên firebase
    public void update(SongEntity songEntity) {
        Query songDel = ref.orderByChild("id").equalTo(songEntity.getId());
        songDel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        HashMap<String,Object> result= new HashMap<>();
                        result.put("id", songEntity.getId());
                        result.put("songName", songEntity.getSongName());
                        result.put("uriString", songEntity.getUriString());
                        result.put("path", songEntity.getPath());
                        result.put("size", songEntity.getSize());
                        result.put("singer", songEntity.getSinger());
                        result.put("favorite", songEntity.isFavorite());
                        result.put("artist", songEntity.getArtist());

                        ds.getRef().updateChildren(result);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled", error.toException());
            }
        });
    }

}
//Test in activity
    //        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference songNode = FirebaseDatabase.getInstance().getReference().child("SongEntity");
//        for (int i=0; i<7;i++){
//            DatabaseReference songPush = songNode.push();
//            String ID = songPush.getKey();
//            SongEntity songEntity =new SongEntity("SongName","uriString",
//                    "PathString",1.2,"Artist","Singer",false);
//
//            songPush.setValue(songEntity);
//        }
//
//
//        DatabaseReference artistNode =FirebaseDatabase.getInstance().getReference().child("ArtistEntity");
//        DatabaseReference artistPush = artistNode.push();
//        artistPush.setValue(new ArtistEntity("Artist"));
//
//        DatabaseReference albumNode =FirebaseDatabase.getInstance().getReference().child("AlbumEntity");
//        DatabaseReference albumPush = albumNode.push();
//        albumPush.setValue(new AlbumEntity("Love and Heavy"));
//    List<SongEntity> songEntityList = new ArrayList<>();
//    ArrayList<SongEntity> songEntities =  new ArrayList<>();
//    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SongEntity");
//        ref.addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot snapshot) {
//        if (snapshot.exists()){
//        for (DataSnapshot ds:snapshot.getChildren()){
//        songEntities.add(ds.getValue(SongEntity.class));
//        }
//        songEntityList.addAll(songEntities);
//        }
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//        });
//
//        Query songDel = ref.orderByChild("id").equalTo(570734123);
//        songDel.addListenerForSingleValueEvent(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot snapshot) {
//        if (snapshot.exists()){
//        for (DataSnapshot ds:snapshot.getChildren()){
//        DatabaseReference a = ds.getRef();
//        a.removeValue();
//        }
//        }
//
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//        });

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String value = snapshot.getValue(String.class);
//                Log.d("Data","Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
