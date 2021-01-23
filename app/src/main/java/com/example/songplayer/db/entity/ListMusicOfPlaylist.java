package com.example.songplayer.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "listMusicOfPlaylist",
 foreignKeys = {@ForeignKey(entity = Playlist.class, parentColumns = {"playlistID", },
    childColumns = {"playlistID"},
        onDelete = ForeignKey.CASCADE
 ),
         @ForeignKey(
                 entity = SongEntity.class,
                 parentColumns = {"id"},
                 childColumns = {"songID"},
                 onDelete = ForeignKey.CASCADE
         )

 },
        primaryKeys = {
        "playlistID",
                "songID"
        }

)
public
class ListMusicOfPlaylist {

    @ColumnInfo(name = "playlistID")
    private int playlistID;

    @ColumnInfo(name = "songID")
    private int songID;

    public int getPlaylistID() {
        return playlistID;
    }

    public void setPlaylistID(int playlistID) {
        this.playlistID = playlistID;
    }

    public int getSongID() {
        return songID;
    }

    public void setSongID(int songID) {
        this.songID = songID;
    }

    public ListMusicOfPlaylist(int playlistID, int songID) {
        this.playlistID = playlistID;
        this.songID = songID;
    }

    @Override
    public String toString() {
        return "ListMusicOfPlaylist{" +
                "playlistID=" + playlistID +
                ", songID=" + songID +
                '}';
    }
}
