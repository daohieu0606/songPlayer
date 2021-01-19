package com.example.songplayer.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity(tableName = "genres",primaryKeys = {
        "genreID",
        "genreName"
}
)
public class Genre {

    @ColumnInfo(name = "genreID")
    private int genreID;

    @ColumnInfo(name = "genreName", defaultValue = "")
    @NonNull
    private String genreName;

    public Genre() {
        genreName = "";
    }

    public Genre(int genreID, @NonNull String genreName) {
        this.genreID = genreID;
        this.genreName = genreName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return genreID == genre.genreID &&
                genreName.equals(genre.genreName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreID, genreName);
    }

    @NotNull
    @Override
    public String toString() {
        return "Genre{" +
                "genreID=" + genreID +
                ", genreName='" + genreName + '\'' +
                '}';
    }
}
