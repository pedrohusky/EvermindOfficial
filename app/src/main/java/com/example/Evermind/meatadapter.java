package com.example.Evermind;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import no.danielzeller.metaballslib.menu.MetaBallAdapter;

public class meatadapter implements MetaBallAdapter {
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Drawable> backgrounds = new ArrayList<>();
    private ArrayList<Integer> menuColors = new ArrayList<>();



    public meatadapter(ArrayList<Integer> colors, ArrayList<Drawable> backgrounds) {
        this.colors = colors;
        this.backgrounds = backgrounds;
        this.menuColors.addAll(colors);
    }

    @Override
    public int itemsCount() {
        return colors.size();
    }

    public void setColors(ArrayList<Integer> colors) {
        this.colors = colors;
    }

    public void setBackgrounds(ArrayList<Drawable> backgrounds) {
        this.backgrounds = backgrounds;
    }

    public void setMenuColors(ArrayList<Integer> menuColors) {
        this.menuColors = menuColors;
    }

    @Override
    public int menuItemBackgroundColor(int i) {
        return colors.get(i);
    }

    @Nullable
    @Override
    public Drawable menuItemIcon(int i) {
        return backgrounds.get(i);
    }

    @Override
    public int menuItemIconTint(int i) {
        return menuColors.get(i);
    }


}
