package com.example.Evermind.everUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.EverCircularColorSelect;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effects;
import com.example.Evermind.meatadapter;
import com.example.Evermind.noteContentsAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EverBallsHelper {


    public EverBallsHelper(@NonNull Context context) {
        this.context = context;
        mainActivity = new WeakReference<>(((MainActivity)context));
        metaColors = mainActivity.get().findViewById(R.id.metaBallMenu);
        for (int ignored : new int[8]) {
            bkg.add(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle_colors, null));
        }
        meatadapter = new meatadapter(colors, bkg);
        metaColors.setAdapter(meatadapter);
        metaColors.setOpenAnimationDuration(250);
        metaColors.setCloseAnimationDuration(250);
    }
    public meatadapter meatadapter;

    @NonNull
    private final Context context;
    @NonNull
    private final WeakReference<MainActivity> mainActivity;
    private final List<Integer> colors = new ArrayList<>();
    public int metaColorNoteColor;
    public int metaColorDraw;
    public int metaColorHighlightColor;
    public int metaColorForeGroundColor;
    public EverCircularColorSelect metaColors;
    private final ArrayList<Drawable> bkg = new ArrayList<>();

    public int getMainButtonColor() {
       return metaColors.getMainButtonColor();
    }

    public void setMainButtonColor(int color) {
         metaColors.setMainButtonColor(color);
    }

    public void clearColors() {
        metaColorNoteColor = Color.WHITE;
        metaColorDraw = Color.WHITE;
        metaColorForeGroundColor = Color.BLACK;
        metaColorHighlightColor = Color.WHITE;
    }

    public void initEverBallsNoteColor() {

        colors.clear();
        for (int color : context.getResources().getIntArray(R.array.noteColor)) {
            colors.add(color);
        }

        meatadapter = new meatadapter(colors, bkg);
        metaColors.setAdapter(meatadapter);
        metaColors.init(true, false, metaColorNoteColor);

        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(0), 0);
                    EverInterfaceHelper.getInstance().changeAccentColor(colors.get(0));
                    metaColorNoteColor = colors.get(0);
                    mainActivity.get().getActualNote().setNoteColor(String.valueOf(colors.get(0)));
                    metaColors.toggleMenu(true, false);

                    //  getActiveEditor().get().setTextColorR.color.Black));
                    break;
                case 1:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(1), 0);
                    EverInterfaceHelper.getInstance().changeAccentColor(colors.get(1));
                    metaColorNoteColor = colors.get(1);
                    mainActivity.get().getActualNote().setNoteColor(String.valueOf(colors.get(1)));
                    metaColors.toggleMenu(true, false);

                    //  getActiveEditor().get().setTextColorR.color.White));
                    break;
                case 2:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(2), 0);
                    EverInterfaceHelper.getInstance().changeAccentColor(colors.get(2));
                    metaColorNoteColor = colors.get(2);
                    mainActivity.get().getActualNote().setNoteColor(String.valueOf(colors.get(2)));
                    metaColors.toggleMenu(true, false);

                    //    getActiveEditor().get().setTextColorR.color.Magenta));
                    break;
                case 3:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(3), 0);
                    EverInterfaceHelper.getInstance().changeAccentColor(colors.get(3));
                    metaColorNoteColor = colors.get(3);
                    mainActivity.get().getActualNote().setNoteColor(String.valueOf(colors.get(3)));
                    metaColors.toggleMenu(true, false);


                    //  getActiveEditor().get().setTextColorR.color.Pink));
                    break;
                case 4:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(4), 0);
                    EverInterfaceHelper.getInstance().changeAccentColor(colors.get(4));
                    metaColorNoteColor = colors.get(4);
                    mainActivity.get().getActualNote().setNoteColor(String.valueOf(colors.get(4)));
                    metaColors.toggleMenu(true, false);

                    //    getActiveEditor().get().setTextColorR.color.Orange));
                    break;
                case 5:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(5), 0);
                    EverInterfaceHelper.getInstance().changeAccentColor(colors.get(5));
                    metaColorNoteColor = colors.get(5);
                    mainActivity.get().getActualNote().setNoteColor(String.valueOf(colors.get(5)));
                    metaColors.toggleMenu(true, false);

                    //    getActiveEditor().get().setTextColorR.color.SkyBlue));
                    break;
                case 6:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(6), 0);
                    EverInterfaceHelper.getInstance().changeAccentColor(colors.get(6));
                    metaColorNoteColor = colors.get(6);
                    mainActivity.get().getActualNote().setNoteColor(String.valueOf(colors.get(6)));
                    metaColors.toggleMenu(true, false);

                    //  getActiveEditor().get().setTextColorR.color.YellowSun));
                    break;
                case 7:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(7), 0);
                    EverInterfaceHelper.getInstance().changeAccentColor(colors.get(7));
                    mainActivity.get().getEverThemeHelper().tintSystemBarsAccent(colors.get(7), 1000);
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(7), 0);
                    metaColorNoteColor = colors.get(7);
                    mainActivity.get().getActualNote().setNoteColor(String.valueOf(colors.get(7)));
                    metaColors.toggleMenu(true, false);

                    //  getActiveEditor().get().setTextColorR.color.GrassGreen));
                    break;
            }
            return null;
        });
    }

    public void initEverBallsHighlight() {

        colors.clear();
        for (int color : context.getResources().getIntArray(R.array.highlight)) {
            colors.add(color);
        }
        meatadapter = new meatadapter(colors, bkg);
        metaColors.setAdapter(meatadapter);
        metaColors.init(false, true, metaColorHighlightColor);

        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:

                    metaColorHighlightColor = colors.get(0);
                    getActiveEditor().applyEffect(Effects.BGCOLOR, colors.get(0));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(0), 0);
                    metaColors.toggleMenu(false, true);

                    //  getActiveEditor().setTextColorR.color.Black));
                    break;
                case 1:
                    metaColorHighlightColor = colors.get(1);
                    getActiveEditor().applyEffect(Effects.BGCOLOR, colors.get(1));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(1), 0);
                    metaColors.toggleMenu(false, true);

                    //  getActiveEditor().setTextColorR.color.White));
                    break;
                case 2:

                    getActiveEditor().applyEffect(Effects.BGCOLOR, colors.get(2));
                    metaColorHighlightColor = colors.get(2);
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(2), 0);
                    metaColors.toggleMenu(false, true);

                    //    getActiveEditor().setTextColorR.color.Magenta));
                    break;
                case 3:

                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(3), 0);
                    metaColorHighlightColor = colors.get(3);
                    getActiveEditor().applyEffect(Effects.BGCOLOR, colors.get(3));
                    metaColors.toggleMenu(false, true);


                    //  getActiveEditor().setTextColorR.color.Pink));
                    break;
                case 4:

                    getActiveEditor().applyEffect(Effects.BGCOLOR, colors.get(4));
                    metaColorHighlightColor = colors.get(4);
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(4), 0);
                    metaColors.toggleMenu(false, true);

                    //    getActiveEditor().setTextColorR.color.Orange));
                    break;
                case 5:

                    metaColorHighlightColor = colors.get(5);
                    getActiveEditor().applyEffect(Effects.BGCOLOR, colors.get(5));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(5), 0);
                    metaColors.toggleMenu(false, true);

                    //    getActiveEditor().setTextColorR.color.SkyBlue));
                    break;
                case 6:

                    metaColorHighlightColor = colors.get(6);
                    getActiveEditor().applyEffect(Effects.BGCOLOR, colors.get(6));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(6), 0);
                    metaColors.toggleMenu(false, true);

                    //  getActiveEditor().setTextColorR.color.YellowSun));
                    break;
                case 7:

                    metaColorHighlightColor = colors.get(7);
                    getActiveEditor().applyEffect(Effects.BGCOLOR, colors.get(7));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(7), 0);
                    metaColors.toggleMenu(false, true);

                    //  getActiveEditor().setTextColorR.color.GrassGreen));
                    break;
            }

            return null;
        });
    }

    public void initEverBallsForeground() {


        colors.clear();
        for (int color : context.getResources().getIntArray(R.array.foreground)) {
            colors.add(color);
        }
        meatadapter = new meatadapter(colors, bkg);
        metaColors.setAdapter(meatadapter);

        metaColors.init(false, false, metaColorForeGroundColor);

        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:
                    metaColorForeGroundColor = colors.get(0);
                    getActiveEditor().applyEffect(Effects.FONTCOLOR, colors.get(0));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(0), 0);
                    metaColors.toggleMenu(false, false);

                    //  getActiveEditor().setTextColorR.color.Black));
                    break;
                case 1:
                    metaColorForeGroundColor = colors.get(1);
                    getActiveEditor().applyEffect(Effects.FONTCOLOR, colors.get(1));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(1), 0);
                    metaColors.toggleMenu(false, false);

                    //  getActiveEditor().setTextColorR.color.White));
                    break;
                case 2:
                    metaColorForeGroundColor = colors.get(2);
                    getActiveEditor().applyEffect(Effects.FONTCOLOR, colors.get(2));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(2), 0);
                    metaColors.toggleMenu(false, false);

                    //    getActiveEditor().setTextColorR.color.Magenta));
                    break;
                case 3:
                    //  mainActivity.get().getEverThemeHelper().tintView(metaColors, GetColor(R.color.Orange));
                    metaColorForeGroundColor = colors.get(3);
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(3), 0);
                    getActiveEditor().applyEffect(Effects.FONTCOLOR, colors.get(3));
                    metaColors.toggleMenu(false, false);


                    //  getActiveEditor().setTextColorR.color.Pink));
                    break;
                case 4:
                    metaColorForeGroundColor = colors.get(4);
                    getActiveEditor().applyEffect(Effects.FONTCOLOR, colors.get(4));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(4), 0);
                    metaColors.toggleMenu(false, false);

                    //    getActiveEditor().setTextColorR.color.Orange));
                    break;
                case 5:
                    metaColorForeGroundColor = colors.get(5);
                    getActiveEditor().applyEffect(Effects.FONTCOLOR, colors.get(5));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(5), 0);
                    metaColors.toggleMenu(false, false);

                    //    getActiveEditor().setTextColorR.color.SkyBlue));
                    break;
                case 6:
                    metaColorForeGroundColor = colors.get(6);
                    getActiveEditor().applyEffect(Effects.FONTCOLOR, colors.get(6));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(6), 0);
                    metaColors.toggleMenu(false, false);

                    //  getActiveEditor().setTextColorR.color.YellowSun));
                    break;
                case 7:
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(7), 0);
                    metaColorForeGroundColor = colors.get(7);
                    getActiveEditor().applyEffect(Effects.FONTCOLOR, colors.get(7));
                    metaColors.toggleMenu(false, false);

                    //  getActiveEditor().setTextColorR.color.GrassGreen));
                    break;
            }

            return null;
        });
    }

    public void initEverBallsDraw() {


        colors.clear();
        for (int color : context.getResources().getIntArray(R.array.foreground)) {
            colors.add(color);
        }

        meatadapter = new meatadapter(colors, bkg);
        metaColors.setAdapter(meatadapter);
        metaColors.init(false, false, metaColorDraw);

        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:
                    metaColorDraw = colors.get(0);
                    mainActivity.get().getNoteCreator().getEverDraw().setColor(colors.get(0));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(0), 0);
                    metaColors.toggleMenu(false, false);

                    //  getActiveEditor().get().setTextColorR.color.Black));
                    break;
                case 1:
                    metaColorDraw = colors.get(1);
                    mainActivity.get().getNoteCreator().getEverDraw().setColor(colors.get(1));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(1), 0);
                    metaColors.toggleMenu(false, false);
                    //  getActiveEditor().get().setTextColorR.color.White));
                    break;
                case 2:

                    metaColorDraw = colors.get(2);
                    mainActivity.get().getNoteCreator().getEverDraw().setColor(colors.get(2));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(2), 0);
                    metaColors.toggleMenu(false, false);

                    //    getActiveEditor().get().setTextColorR.color.Magenta));
                    break;
                case 3:

                    //  mainActivity.get().getEverThemeHelper().tintView(metaColors, GetColor(R.color.Orange));
                    metaColorDraw = colors.get(3);
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(3), 0);
                    mainActivity.get().getNoteCreator().getEverDraw().setColor(colors.get(3));
                    metaColors.toggleMenu(false, false);


                    //  getActiveEditor().get().setTextColorR.color.Pink));
                    break;
                case 4:
                    metaColorDraw = colors.get(4);
                    mainActivity.get().getNoteCreator().getEverDraw().setColor(colors.get(4));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(4), 0);
                    metaColors.toggleMenu(false, false);

                    //    getActiveEditor().get().setTextColorR.color.Orange));
                    break;
                case 5:

                    metaColorDraw = colors.get(5);
                    mainActivity.get().getNoteCreator().getEverDraw().setColor(colors.get(5));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(5), 0);
                    metaColors.toggleMenu(false, false);

                    //    getActiveEditor().get().setTextColorR.color.SkyBlue));
                    break;
                case 6:

                    metaColorDraw = colors.get(6);
                    mainActivity.get().getNoteCreator().getEverDraw().setColor(colors.get(6));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(6), 0);
                    metaColors.toggleMenu(false, false);

                    //  getActiveEditor().get().setTextColorR.color.YellowSun));
                    break;
                case 7:

                    mainActivity.get().getEverThemeHelper().tintViewAccent(metaColors, colors.get(7), 0);
                    metaColorDraw = colors.get(7);
                    mainActivity.get().getNoteCreator().getEverDraw().setColor(colors.get(7));
                    metaColors.toggleMenu(false, false);

                    //  getActiveEditor().get().setTextColorR.color.GrassGreen));
                    break;
            }

            return null;
        });
    }

    public void initEverBallsPaintType() {

        colors.clear();
        for (int color : context.getResources().getIntArray(R.array.noteColor)) {
            colors.add(color);
        }
        meatadapter = new meatadapter(colors, bkg);
        metaColors.setAdapter(meatadapter);

        metaColors.init(false, false, metaColorDraw);

        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:
                    mainActivity.get().getNoteCreator().getEverDraw().paintType("stroke");


                    break;
                case 1:
                    mainActivity.get().getNoteCreator().getEverDraw().paintType("fill");
                    break;
                case 2:

                    mainActivity.get().getNoteCreator().getEverDraw().paintType("fillStroke");
                    break;
            }

            return null;
        });
    }
    
    private RTEditText getActiveEditor() {
        RTEditText newOne = mainActivity.get().getNoteCreator().getActiveEditor();
        if (newOne == null) {
            for (int i = 0; i < mainActivity.get().getNoteCreator().gettextRecyclerCreator().getChildCount(); i++) {
                noteContentsAdapter.ViewHolder holder = ((noteContentsAdapter.ViewHolder) mainActivity.get().getNoteCreator().gettextRecyclerCreator().findViewHolderForAdapterPosition(i));

                if (Objects.requireNonNull(holder).ActiveEditor != null) {
                    newOne = holder.ActiveEditor;
                }
            }
        }
        return  newOne;
    }
}
