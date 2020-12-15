package com.example.Evermind.everUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.example.Evermind.EverAudioVisualizerHandlers.CloseAudioVisualizationHelper;
import com.example.Evermind.EverCircularColorSelect;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effects;
import com.example.Evermind.meatadapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import kotlin.Unit;

public class EverBallsHelper {

    public EverBallsHelper(Context context) {
        this.context = context;
        mainActivity = new WeakReference<>(((MainActivity)context));
        metaColors = mainActivity.get().findViewById(R.id.metaBallMenu);
        for (int ignored : new int[8]) {
            bkg.add(ResourcesCompat.getDrawable(context.getResources(), R.drawable.circle_colors, null));
        }
    }

    private final Context context;
    private final WeakReference<MainActivity> mainActivity;
    private int[] colors = new int[0];
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
        colors = context.getResources().getIntArray(R.array.noteColor);
        metaColors.setAdapter(new meatadapter(colors, bkg));
        metaColors.init(true, false, metaColorNoteColor);
        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:

                    CloseAudioVisualizationHelper.getInstance().changeColor(colors[0]);
                    mainActivity.get().everThemeHelper.tintSystemBars(colors[0], 1000);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[0]);
                    metaColorNoteColor = colors[0];
                    mainActivity.get().actualNote.get().setNoteColor(String.valueOf(colors[0]));
                    metaColors.toggleMenu(true, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Black));
                    break;
                case 1:

                    CloseAudioVisualizationHelper.getInstance().changeColor(colors[1]);
                    mainActivity.get().everThemeHelper.tintSystemBars(colors[1], 1000);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[1]);
                    metaColorNoteColor = colors[1];
                    mainActivity.get().actualNote.get().setNoteColor(String.valueOf(colors[1]));
                    metaColors.toggleMenu(true, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.White));
                    break;
                case 2:

                    CloseAudioVisualizationHelper.getInstance().changeColor(colors[2]);
                    mainActivity.get().everThemeHelper.tintSystemBars(colors[2], 1000);
                    mainActivity.get().everThemeHelper.tintView(metaColors, colors[2]);
                    metaColorNoteColor = colors[2];
                    mainActivity.get().actualNote.get().setNoteColor(String.valueOf(colors[2]));
                    metaColors.toggleMenu(true, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Magenta));
                    break;
                case 3:
                    CloseAudioVisualizationHelper.getInstance().changeColor(colors[3]);
                    mainActivity.get().everThemeHelper.tintSystemBars(colors[3], 1000);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[3]);
                    metaColorNoteColor = colors[3];
                    mainActivity.get().actualNote.get().setNoteColor(String.valueOf(colors[3]));
                    metaColors.toggleMenu(true, false);


                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Pink));
                    break;
                case 4:

                    CloseAudioVisualizationHelper.getInstance().changeColor(colors[4]);
                    mainActivity.get().everThemeHelper.tintSystemBars(colors[4], 1000);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[4]);
                    metaColorNoteColor = colors[4];
                    mainActivity.get().actualNote.get().setNoteColor(String.valueOf(colors[4]));
                    metaColors.toggleMenu(true, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Orange));
                    break;
                case 5:

                    CloseAudioVisualizationHelper.getInstance().changeColor(colors[5]);
                    mainActivity.get().everThemeHelper.tintSystemBars(colors[5], 1000);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[5]);
                    metaColorNoteColor = colors[5];
                    mainActivity.get().actualNote.get().setNoteColor(String.valueOf(colors[5]));
                    metaColors.toggleMenu(true, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.SkyBlue));
                    break;
                case 6:

                    CloseAudioVisualizationHelper.getInstance().changeColor(colors[6]);
                    mainActivity.get().everThemeHelper.tintSystemBars(colors[6], 1000);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[6]);
                    metaColorNoteColor = colors[6];
                    mainActivity.get().actualNote.get().setNoteColor(String.valueOf(colors[6]));
                    metaColors.toggleMenu(true, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.YellowSun));
                    break;
                case 7:

                    CloseAudioVisualizationHelper.getInstance().changeColor(colors[7]);
                    mainActivity.get().everThemeHelper.tintSystemBars(colors[7], 1000);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[7]);
                    metaColorNoteColor = colors[7];
                    mainActivity.get().actualNote.get().setNoteColor(String.valueOf(colors[7]));
                    metaColors.toggleMenu(true, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.GrassGreen));
                    break;
            }
            return null;
        });
    }

    public void initEverBallsHighlight() {
        colors = context.getResources().getIntArray(R.array.highlight);
        metaColors.setAdapter(new meatadapter(colors, bkg));
        metaColors.init(false, true, metaColorHighlightColor);
        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:

                    metaColorHighlightColor = colors[0];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BGCOLOR, colors[0]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[0]);
                    metaColors.toggleMenu(false, true);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Black));
                    break;
                case 1:
                    metaColorHighlightColor = colors[1];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BGCOLOR, colors[1]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[1]);
                    metaColors.toggleMenu(false, true);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.White));
                    break;
                case 2:

                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BGCOLOR, colors[2]);
                    metaColorHighlightColor = colors[2];
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[2]);
                    metaColors.toggleMenu(false, true);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Magenta));
                    break;
                case 3:

                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[3]);
                    metaColorHighlightColor = colors[3];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BGCOLOR, colors[3]);
                    metaColors.toggleMenu(false, true);


                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Pink));
                    break;
                case 4:

                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BGCOLOR, colors[4]);
                    metaColorHighlightColor = colors[4];
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[4]);
                    metaColors.toggleMenu(false, true);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Orange));
                    break;
                case 5:

                    metaColorHighlightColor = colors[5];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BGCOLOR, colors[5]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[5]);
                    metaColors.toggleMenu(false, true);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.SkyBlue));
                    break;
                case 6:

                    metaColorHighlightColor = colors[6];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BGCOLOR, colors[6]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[6]);
                    metaColors.toggleMenu(false, true);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.YellowSun));
                    break;
                case 7:

                    metaColorHighlightColor = colors[7];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BGCOLOR, colors[7]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[7]);
                    metaColors.toggleMenu(false, true);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.GrassGreen));
                    break;
            }

            return null;
        });
    }

    public void initEverBallsForeground() {
        colors = context.getResources().getIntArray(R.array.foreground);
        metaColors.setAdapter(new meatadapter(colors, bkg));
        metaColors.init(false, false, metaColorForeGroundColor);
        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:
                    metaColorForeGroundColor = colors[0];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[0]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[0]);
                    metaColors.toggleMenu(false, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Black));
                    break;
                case 1:
                    metaColorForeGroundColor = colors[1];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[1]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[1]);
                    metaColors.toggleMenu(false, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.White));
                    break;
                case 2:
                    metaColorForeGroundColor = colors[2];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[2]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[2]);
                    metaColors.toggleMenu(false, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Magenta));
                    break;
                case 3:
                    //  mainActivity.get().everThemeHelper.tintView(metaColors, GetColor(R.color.Orange));
                    metaColorForeGroundColor = colors[3];
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[3]);
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[3]);
                    metaColors.toggleMenu(false, false);


                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Pink));
                    break;
                case 4:
                    metaColorForeGroundColor = colors[4];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[4]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[4]);
                    metaColors.toggleMenu(false, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Orange));
                    break;
                case 5:
                    metaColorForeGroundColor = colors[5];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[5]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[5]);
                    metaColors.toggleMenu(false, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.SkyBlue));
                    break;
                case 6:
                    metaColorForeGroundColor = colors[6];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[6]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[6]);
                    metaColors.toggleMenu(false, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.YellowSun));
                    break;
                case 7:
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[7]);
                    metaColorForeGroundColor = colors[7];
                    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[7]);
                    metaColors.toggleMenu(false, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.GrassGreen));
                    break;
            }

            return null;
        });
    }

    public void initEverBallsDraw() {
        colors = context.getResources().getIntArray(R.array.foreground);
        metaColors.setAdapter(new meatadapter(colors, bkg));
        metaColors.init(false, false, metaColorDraw);
        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:
                    metaColorDraw = colors[0];
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().setColor(colors[0]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[0]);
                    metaColors.toggleMenu(false, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Black));
                    break;
                case 1:
                    metaColorDraw = colors[1];
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().setColor(colors[1]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[1]);
                    metaColors.toggleMenu(false, false);
                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.White));
                    break;
                case 2:

                    metaColorDraw = colors[2];
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().setColor(colors[2]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[2]);
                    metaColors.toggleMenu(false, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Magenta));
                    break;
                case 3:

                    //  mainActivity.get().everThemeHelper.tintView(metaColors, GetColor(R.color.Orange));
                    metaColorDraw = colors[3];
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[3]);
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().setColor(colors[3]);
                    metaColors.toggleMenu(false, false);


                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Pink));
                    break;
                case 4:
                    metaColorDraw = colors[4];
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().setColor(colors[4]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[4]);
                    metaColors.toggleMenu(false, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.Orange));
                    break;
                case 5:

                    metaColorDraw = colors[5];
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().setColor(colors[5]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[5]);
                    metaColors.toggleMenu(false, false);

                    //    mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.SkyBlue));
                    break;
                case 6:

                    metaColorDraw = colors[6];
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().setColor(colors[6]);
                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[6]);
                    metaColors.toggleMenu(false, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.YellowSun));
                    break;
                case 7:

                   mainActivity.get().everThemeHelper.tintView(metaColors, colors[7]);
                    metaColorDraw = colors[7];
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().setColor(colors[7]);
                    metaColors.toggleMenu(false, false);

                    //  mainActivity.get().noteCreator.get().everCreatorHelper.activeEditor.get().setTextColorR.color.GrassGreen));
                    break;
            }

            return null;
        });
    }

    public void initEverBallsPaintType() {
        colors = context.getResources().getIntArray(R.array.noteColor);
        metaColors.setAdapter(new meatadapter(colors, bkg));
        metaColors.init(false, false, metaColorDraw);
        metaColors.setOnItemSelectedListener(integer -> {
            switch (integer) {
                case 0:
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().paintType("stroke");


                    break;
                case 1:
                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().paintType("fill");
                    break;
                case 2:

                    mainActivity.get().noteCreator.get().everCreatorHelper.everDraw.get().paintType("fillStroke");
                    break;
            }

            return null;
        });
    }
}
