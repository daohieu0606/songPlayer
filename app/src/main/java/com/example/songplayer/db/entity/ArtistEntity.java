package com.example.songplayer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.songplayer.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "artists")
@TypeConverters({DateConverter.class})
public class ArtistEntity {

    @ColumnInfo(name = "id")
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "composerName")
    private String composerName;

    @ColumnInfo(name = "composerDOB")
    private Date composerDOB;

    @ColumnInfo(name = "composerStory")
    private String composerStory;

    public ArtistEntity(String artistName) {
        this.composerName = artistName;
    }

    public ArtistEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComposerName() {
        return composerName;
    }

    public void setComposerName(String composerName) {
        this.composerName = composerName;
    }

    public Date getComposerDOB() {
        return composerDOB;
    }

    public void setComposerDOB(Date composerDOB) {
        this.composerDOB = composerDOB;
    }

    public String getComposerStory() {
        return composerStory;
    }

    public void setComposerStory(String composerStory) {
        this.composerStory = composerStory;
    }

    @Override
    public String toString() {
        return "ArtistEntity{" +
                "id=" + id +
                ", composerName='" + composerName + '\'' +
                ", composerDOB=" + composerDOB +
                ", composerStory='" + composerStory + '\'' +
                '}';
    }
}
