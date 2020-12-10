package com.example.songplayer.adapter.adaper_item;

public class DrawerItem {
    private int iconID;
    private String drawerTitle;

    public DrawerItem(int iconID, String drawerTitle) {
        this.iconID = iconID;
        this.drawerTitle = drawerTitle;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getDrawerTitle() {
        return drawerTitle;
    }

    public void setDrawerTitle(String drawerTitle) {
        this.drawerTitle = drawerTitle;
    }
}
