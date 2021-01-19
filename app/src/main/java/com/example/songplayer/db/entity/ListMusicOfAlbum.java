package com.example.songplayer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "listMusicOfAlbum",
foreignKeys = {
        @ForeignKey(
                entity = SongEntity.class,
                parentColumns = "id",
                childColumns = "songID",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = AlbumEntity.class,
                parentColumns = "id",
                childColumns = "albumID",
                onDelete = ForeignKey.CASCADE
        )
},primaryKeys = {
        "songID",
        "albumID"
})
public class ListMusicOfAlbum {

    @ColumnInfo(name = "songID")
    private int songID;

    @ColumnInfo(name = "albumID")
    private int albumID;

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public ListMusicOfAlbum(int songID, int albumID) {
        this.songID = songID;
        this.albumID = albumID;
    }
}
