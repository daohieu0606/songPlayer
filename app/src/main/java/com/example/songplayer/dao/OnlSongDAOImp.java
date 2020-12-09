package com.example.songplayer.dao;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import com.example.songplayer.db.SongEntity;
import java.util.ArrayList;
import java.util.List;

public class OnlSongDAOImp implements SongDAO{
    private static final String TAG = "TESST";
    private MutableLiveData<List<SongEntity>> listMutableLiveData;
    private Application context;

    public OnlSongDAOImp(Application newContext) {
        context = newContext;
        listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.setValue(new ArrayList<>());

        loadDefaultSongList();
    }

    //Lấy tất cả dữ liệu trên firebase rồi gán vào listMutableLiveData
    private void loadDefaultSongList() {
    }

    public MutableLiveData<List<SongEntity>> getAllSongs() {
        return listMutableLiveData;
    }

    // Xóa một file trên firebase dựa trên id
    public void delete(int ID) {
    }

    //thêm một file lên firebase
    public void insert(SongEntity songEntity) {
    }

    //update một file trên firebase
    public void update(SongEntity songEntity) {
    }

}
