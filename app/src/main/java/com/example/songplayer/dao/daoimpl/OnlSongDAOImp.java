package com.example.songplayer.dao.daoimpl;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.songplayer.dao.daoimpl.callback.Callback;
import com.example.songplayer.dao.daointerface.SongDAO;
import com.example.songplayer.db.entity.SongEntity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnlSongDAOImp implements SongDAO {



    private static final String TAG = "TESST";
    private List<SongEntity> listSongEntities;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SongEntity");


    public OnlSongDAOImp() {
        listSongEntities = new ArrayList<>();
    }

    //Lấy tất cả dữ liệu trên firebase rồi gán vào listMutableLiveData
    public void fetchOnlineSongs(Callback callback) {
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
                    listSongEntities = songEntities;

                    if(callback!=null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                callback.done(songEntities);
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void fetchOnlineSongs(){
        fetchOnlineSongs(null);
    }
    public List<SongEntity> getAllSongs() {
        return listSongEntities;
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
        File musicFile = new File(downloadFolder.getAbsolutePath(),fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(musicFile);

        // Create a storage reference from our app
        // Get the default bucket from a custom FirebaseApp
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://testfirebase-b2bcc.appspot.com/"+fileName);
        final long TWENTY_MEGABYTE = 1024 * 1024*20;

        gsReference.getFile(musicFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if(handler!=null) handler.success();
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                final int percent = (int) (snapshot.getBytesTransferred()*100.0/snapshot.getTotalByteCount());
                if(handler!=null ) handler.updateProgress(percent);

            }
        })

        ;



    }

    public  interface UIHandler{
        void updateProgress(int percent);
        void success();
    }

}
