/*
 * Copyright (C) 2015-2018 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.Evermind.TESTEDITOR.toolbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;

import java.util.logging.Handler;

import cafe.adriel.androidaudiorecorder.Util;

/**
 * An ImageButton for the toolbar.
 * It adds a checked state.
 */
public class RTToolbarImageButton extends androidx.appcompat.widget.AppCompatImageButton implements EverInterfaceHelper.OnChangeColorListener, EverInterfaceHelper.OnEnterDarkMode {
    private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};

    private boolean mChecked;
    private boolean isDarkMode = false;

    public RTToolbarImageButton(@NonNull Context context) {
        this(context, null);
    }

    public RTToolbarImageButton(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.rte_ToolbarButton);
    }

    public RTToolbarImageButton(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RTToolbarImageButton, defStyle, 0);
        mChecked = a.getBoolean(R.styleable.RTToolbarImageButton_checked, false);
        a.recycle();
        this.setTag("");
    }

    public void initButton() {

        EverInterfaceHelper.getInstance().setAccentColorListener(this);
        EverInterfaceHelper.getInstance().setDarkModeListeners(this);
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (mChecked) {
                if (!((MainActivity)getContext()).getActualNote().getNoteColor().equals("-1")) {
                    ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(this, Integer.parseInt(((MainActivity)getContext()).getActualNote().getNoteColor()), 0);
                } else {
                    ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(this, getContext().getColor(R.color.NightLessBlack), 0);
                }
                this.setImageTintList(ColorStateList.valueOf( getContext().getColor(R.color.White)));

            } else {

                if (!((MainActivity)getContext()).getActualNote().getNoteColor().equals("-1")) {
                    this.setImageTintList(ColorStateList.valueOf(Integer.parseInt(((MainActivity)getContext()).getActualNote().getNoteColor())));
                } else {
                    this.setImageTintList(ColorStateList.valueOf( getContext().getColor(R.color.NightLessBlack)));
                }
                ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(this, ((MainActivity)getContext()).getEverThemeHelper().defaultTheme, 0);
            }
        }

    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + CHECKED_STATE_SET.length);
        if (mChecked) mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return drawableState;
    }

    @Override
    public void changeAccentColor(int color) {

        int darkColor;

        if (isDarkMode) {
            darkColor =  getContext().getColor(android.R.color.darker_gray);
        } else {
            darkColor =  getContext().getColor(R.color.NightLessBlack);
        }


                if (color != getContext().getColor(R.color.White)) {
                    RTToolbarImageButton.this.setImageTintList(ColorStateList.valueOf(color));
                } else {
                    RTToolbarImageButton.this.setImageTintList(ColorStateList.valueOf(darkColor));
                }
                if (mChecked) {
                    if (color != getContext().getColor(R.color.White)) {
                        ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(RTToolbarImageButton.this, color, 0);
                    } else {
                        ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(RTToolbarImageButton.this, darkColor, 0);
                    }
                    RTToolbarImageButton.this.setImageTintList(ColorStateList.valueOf( getContext().getColor(R.color.White)));

                } else {

                    ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(RTToolbarImageButton.this, ((MainActivity)getContext()).getEverThemeHelper().defaultTheme, 0);
                }


    }

    @Override
    public void swichDarkMode(int color, boolean isDarkMode) {
        this.isDarkMode = isDarkMode;
        int darkColor;
        if (isDarkMode) {
         darkColor = getContext().getColor(android.R.color.darker_gray);
        } else {
            darkColor = getContext().getColor(R.color.NightLessBlack);
        }

        if (color != getContext().getColor(R.color.White)) {
            this.setImageTintList(ColorStateList.valueOf(color));
        } else {
            this.setImageTintList(ColorStateList.valueOf(darkColor));
        }

        if (mChecked) {
            if (color == getContext().getColor(R.color.White)) {
                this.setImageTintList(ColorStateList.valueOf( getContext().getColor(R.color.White)));
                ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(this, darkColor, 0);
            } else {
                this.setImageTintList(ColorStateList.valueOf( color));
                ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(this, Util.getDarkerColor(color), 0);
            }
        } else {

            ((MainActivity)getContext()).getEverThemeHelper().tintViewAccent(RTToolbarImageButton.this, ((MainActivity)getContext()).getEverThemeHelper().defaultTheme, 0);
        }
    }
}