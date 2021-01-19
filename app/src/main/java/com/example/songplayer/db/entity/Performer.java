package com.example.songplayer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.songplayer.utils.DateConverter;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity(tableName = "performers")
@TypeConverters({DateConverter.class})
public class Performer {

    @ColumnInfo(name = "performerID")
    @PrimaryKey
    private int performerID;

    @ColumnInfo(name = "performerName")
    private String performerName;

    @ColumnInfo(name = "perfomerDOB")
    private Date performerDOB;

    @ColumnInfo(name = "performerStory")
    private String performerStory;


    public int getPerformerID() {
        return performerID;
    }

    public void setPerformerID(int performerID) {
        this.performerID = performerID;
    }

    public String getPerformerName() {
        return performerName;
    }

    public void setPerformerName(String performerName) {
        this.performerName = performerName;
    }

    public Date getPerformerDOB() {
        return performerDOB;
    }

    public void setPerformerDOB(Date performerDOB) {
        this.performerDOB = performerDOB;
    }

    public String getPerformerStory() {
        return performerStory;
    }

    public void setPerformerStory(String performerStory) {
        this.performerStory = performerStory;
    }

    @NotNull
    @Override
    public String toString() {
        return "Performer{" +
                "performerID=" + performerID +
                ", performerName='" + performerName + '\'' +
                ", performerDOB=" + performerDOB +
                ", performerStory='" + performerStory + '\'' +
                '}';
    }

}
