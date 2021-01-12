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
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;

/**
 * An ImageButton for the toolbar.
 * It adds a checked state.
 */
public class RTToolbarImageButton extends androidx.appcompat.widget.AppCompatImageButton implements EverInterfaceHelper.OnChangeColorListener {
    private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};

    private boolean mChecked;

    public RTToolbarImageButton(Context context) {
        this(context, null);
    }

    public RTToolbarImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.rte_ToolbarButton);
    }

    public RTToolbarImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RTToolbarImageButton, defStyle, 0);
        mChecked = a.getBoolean(R.styleable.RTToolbarImageButton_checked, false);
        a.recycle();
        EverInterfaceHelper.getInstance().setColorListener(this);
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (mChecked) {
                if (!((MainActivity)getContext()).getActualNote().getNoteColor().equals("-1")) {
                    ((MainActivity)getContext()).getEverThemeHelper().tintView(this, Integer.parseInt(((MainActivity)getContext()).getActualNote().getNoteColor()), 0);
                } else {
                    ((MainActivity)getContext()).getEverThemeHelper().tintView(this, getContext().getColor(R.color.SkyBlueHighlight), 0);
                }
            } else {
                ((MainActivity)getContext()).getEverThemeHelper().tintView(this, ((MainActivity)getContext()).getEverThemeHelper().defaultTheme, 0);
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
    public void changeColor(int color) {
        if (mChecked) {
            System.out.println(color);
            if (color == getContext().getColor(R.color.White)) {
                ((MainActivity)getContext()).getEverThemeHelper().tintView(this, getContext().getColor(R.color.SkyBlueHighlight), 0);
            } else {
                ((MainActivity)getContext()).getEverThemeHelper().tintView(this, color, 0);
            }
        }
    }
}