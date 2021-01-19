package com.example.songplayer.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "genres",primaryKeys = {
        "genreID",
        "genreName"
}
)
public class Genre {

    @ColumnInfo(name = "genreID")
    private int genreID;

    @ColumnInfo(name = "genreName")
    @NonNull
    private String genreName;

    public Genre() {
        genreName = "";
    }

    public int getGenreID() {
        return genreID;
    }

    public void setGenreID(int genreID) {
        this.genreID = genreID;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
