package com.example.songplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.songplayer.R;
import com.example.songplayer.activity.MainActivity;
import com.example.songplayer.adapter.DrawerAdapter;
import com.example.songplayer.adapter.adaper_item.DrawerItem;
import com.example.songplayer.adapter.adaper_item.SimpleItem;
import com.example.songplayer.adapter.adaper_item.SimpleItemWithTopPart;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class DrawerCreater {

    public static final int POS_HOME = 0;
    public static final int POS_FAVORITE = 1;
    public static final int POS_LYRIC = 2;
    public static final int POS_DOWNLOAD = 3;
    public static final int POS_PLAYLIST = 4;
    public static final int POS_ALBUM = 5;
    public static final int POS_CATEGORY = 6;
    public static final int POS_MUSIC = 7;

    private Context context;
    private Drawable[] screenIcons;
    private String[] screenTitles;
    private final Toolbar toolbar;


    public DrawerCreater(Context context, Toolbar toolbar) {
        this.context = context;
        this.toolbar = toolbar;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = context.getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(context, id);
            }
        }
        ta.recycle();
        return icons;
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(getColor(R.color.white))
                .withTextTint(getColor(R.color.white))
                .withSelectedIconTint(getColor(R.color.accent_material_dark))
                .withSelectedTextTint(getColor(R.color.accent_material_dark));
    }

    private DrawerItem createItemFor(int position, String topTitle) {
        return new SimpleItemWithTopPart(screenIcons[position], screenTitles[position], topTitle)
                .withIconTint(getColor(R.color.white))
                .withTextTint(getColor(R.color.white))
                .withSelectedIconTint(getColor(R.color.accent_material_dark))
                .withSelectedTextTint(getColor(R.color.accent_material_dark));
    }

    private int getColor(int id) {
        return context.getColor(id);
    }

    public SlidingRootNav createDrawer() {

        screenTitles = new String[]{
                context.getString(R.string.home),
                context.getString(R.string.favorite),
                context.getString(R.string.lyric),
                context.getString(R.string.download),
                context.getString(R.string.share_app),
                context.getString(R.string.rate_app),
                context.getString(R.string.category),
                context.getString(R.string.music_app)
        };

        screenIcons = loadScreenIcons();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_FAVORITE),
                createItemFor(POS_LYRIC),
                createItemFor(POS_DOWNLOAD),
                createItemFor(POS_PLAYLIST, "List"),
                createItemFor(POS_ALBUM),
                createItemFor(POS_CATEGORY),
                createItemFor(POS_MUSIC, "Music player")

        ));

        SlidingRootNav slidingRootNav = new SlidingRootNavBuilder((Activity) context)
                .withToolbarMenuToggle(this.toolbar)
                .withDragDistance(130)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(((MainActivity) context).getSavedInstance())
                .withMenuLayout(R.layout.drawer_menu_left)
                .inject();


        adapter.setListener((DrawerAdapter.OnItemSelectedListener) context);

        RecyclerView menu = ((MainActivity) context).getMenu();

        menu.setNestedScrollingEnabled(false);
        menu.setLayoutManager(new LinearLayoutManager(context));
        menu.setAdapter(adapter);
        adapter.setSelected(POS_HOME);

        return slidingRootNav;

    }
}
