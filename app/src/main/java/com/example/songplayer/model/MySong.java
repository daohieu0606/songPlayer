package com.example.songplayer.model;

public interface MySong {
    public void setSongName(String newName);
    public void setUriString(String newUriString);
    public String getSongName();
    public String getUriString();
    public String getPath();
    public void setPath(String path);
    public int getId();
    public void setId(int id);
    public double getSize();
    public void setSize(double size);
    public String getArtist();
    public void setArtist(String artist);
    public String getSinger();
    public void setSinger(String singer);
    public boolean isFavorite();
    public void setFavorite(boolean isFavorite);
}
