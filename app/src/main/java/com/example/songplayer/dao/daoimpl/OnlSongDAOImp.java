package com.example.songplayer.dao.daoimpl;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.songplayer.dao.daointerface.SongDAO;
import com.example.songplayer.db.entity.SongEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class OnlSongDAOImp implements SongDAO {


    private static final String TAG = "TESST";
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SongEntity");


    public OnlSongDAOImp() {
    }

    //Lấy tất cả dữ liệu trên firebase rồi gán vào listMutableLiveData
    public List<SongEntity> fetchOnlineSongs() {
        Semaphore semaphore = new Semaphore(0);
        ArrayList<SongEntity> songEntities = new ArrayList<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        SongEntity songEntity = ds.getValue(SongEntity.class);
                        songEntity.setOnline(true);
                        songEntities.add(songEntity);
                    }
                }
                semaphore.release();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                semaphore.release();
            }
        });

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return songEntities;

    }


    // Xóa một file trên firebase dựa trên id
    public void delete(int ID) {
        Query songDel = ref.orderByChild("id").equalTo(ID);
        songDel.addListenerForSingleValueEvent(new ValueEventListener() {
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
                Log.e(TAG, "onCancelled", error.toException());
            }
        });
    }


    @Override
    public List<SongEntity> getAllSongs() {
        return fetchOnlineSongs();
    }

    //thêm một file lên firebase
    public void insert(SongEntity songEntity) {
        ref.push().setValue(songEntity);
    }

    //update một file lên firebase
    public void update(SongEntity songEntity) {
        Query songDel = ref.orderByChild("id").equalTo(songEntity.getId());
        songDel.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        HashMap<String, Object> result = new HashMap<>();
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

    public void downloadFile(String fileName, UIHandler handler) throws FileNotFoundException {


        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File musicFile = new File(downloadFolder.getAbsolutePath(), fileName);

        // Create a storage reference from our app
        // Get the default bucket from a custom FirebaseApp
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://songplayer-82ff0.appspot.com/" + fileName);

        gsReference.getFile(musicFile).addOnSuccessListener(taskSnapshot -> {
            if (handler != null) handler.success();
        }).addOnProgressListener(snapshot -> {
            final int percent = (int) (snapshot.getBytesTransferred() * 100.0 / snapshot.getTotalByteCount());
            if (handler != null) handler.updateProgress(percent);
        });


    }

    public interface UIHandler {
        void updateProgress(int percent);

        void success();
    }

}
