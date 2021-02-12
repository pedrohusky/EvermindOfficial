package com.example.Evermind;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import no.danielzeller.metaballslib.menu.MetaBallAdapter;

public class meatadapter implements MetaBallAdapter {
    private List<Integer> colors;
    private ArrayList<Drawable> backgrounds;
    private List<Integer> menuColors;



    public meatadapter(List<Integer> colors, ArrayList<Drawable> backgrounds) {
        this.colors = colors;
        this.backgrounds = backgrounds;
        this.menuColors = this.colors;
    }

    public void updateAdapter(List<Integer> colors) {
        this.colors = colors;
    }

    @Override
    public int itemsCount() {
        return colors.size();
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }

    public void setBackgrounds(ArrayList<Drawable> backgrounds) {
        this.backgrounds = backgrounds;
    }

    public void setMenuColors(List<Integer> menuColors) {
        this.menuColors = menuColors;
    }

    @Override
    public int menuItemBackgroundColor(int i) {
        return colors.get(i);
    }

    @Override
    public Drawable menuItemIcon(int i) {
        return backgrounds.get(i);
    }

    @Override
    public int menuItemIconTint(int i) {
        return menuColors.get(i);
    }


}
