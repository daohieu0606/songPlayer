package com.example.songplayer.dao.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.songplayer.db.entity.Performer;

import java.util.List;
@Dao
public interface PerformerDAORoom {
    @Query("select * from performers")
    LiveData<List<Performer>> getAllPerformer();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Performer performer);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Performer performer);

    @Query("delete from performers where performerID = :ID")
    void delete(int ID);
}
