package com.example.Evermind;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import no.danielzeller.metaballslib.menu.MetaBallAdapter;

public class meatadapter implements MetaBallAdapter {
    private int[] colors;
    private ArrayList<Drawable> backgrounds;
    private int[] menuColors;



    public meatadapter(int[] colors, ArrayList<Drawable> backgrounds) {
        this.colors = colors;
        this.backgrounds = backgrounds;
        this.menuColors = this.colors;
    }

    @Override
    public int itemsCount() {
        return colors.length;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setBackgrounds(ArrayList<Drawable> backgrounds) {
        this.backgrounds = backgrounds;
    }

    public void setMenuColors(int[] menuColors) {
        this.menuColors = menuColors;
    }

    @Override
    public int menuItemBackgroundColor(int i) {
        return colors[i];
    }

    @Nullable
    @Override
    public Drawable menuItemIcon(int i) {
        return backgrounds.get(i);
    }

    @Override
    public int menuItemIconTint(int i) {
        return menuColors[i];
    }


}
