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
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.TESTEDITOR.rteditor.RTToolbar;
import com.example.Evermind.TESTEDITOR.rteditor.RTToolbarListener;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effects;
import com.example.Evermind.TESTEDITOR.rteditor.fonts.FontManager;
import com.example.Evermind.TESTEDITOR.rteditor.fonts.RTTypeface;
import com.example.Evermind.TESTEDITOR.rteditor.utils.Helper;
import com.example.Evermind.TESTEDITOR.toolbar.spinner.BGColorSpinnerItem;
import com.example.Evermind.TESTEDITOR.toolbar.spinner.ColorSpinnerItem;
import com.example.Evermind.TESTEDITOR.toolbar.spinner.FontColorSpinnerItem;
import com.example.Evermind.TESTEDITOR.toolbar.spinner.FontSizeSpinnerItem;
import com.example.Evermind.TESTEDITOR.toolbar.spinner.FontSpinnerItem;
import com.example.Evermind.TESTEDITOR.toolbar.spinner.SpinnerItem;
import com.example.Evermind.TESTEDITOR.toolbar.spinner.SpinnerItemAdapter;
import com.example.Evermind.TESTEDITOR.toolbar.spinner.SpinnerItems;

import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is a concrete implementation of the RTToolbar interface. It uses
 * toggle buttons for the effects with a simple on/off (like bold/not bold) and
 * Spinners for the more complex formatting (background color, font color, font
 * size).
 * <p>
 * While the included rte_toolar layout puts all icons in a row it's easy to use
 * multiple toolbars, each with a subset of formatting options (e.g. one for the
 * character formatting, one for the paragraph formatting, one for all the rest
 * like insert image, undo/redo etc.).
 */
public class HorizontalRTToolbar extends LinearLayout implements RTToolbar, View.OnClickListener {

    /*
     * We need a unique id for the toolbar because the RTManager is capable of managing multiple toolbars
     */
    private static final AtomicInteger sIdCounter = new AtomicInteger(0);
    private int mId;

    private RTToolbarListener mListener;

    private ViewGroup mToolbarContainer;

    /*
     * The buttons
     */
    private RTToolbarImageButton mBold;
    private RTToolbarImageButton mItalic;
    private RTToolbarImageButton mUnderline;
    private RTToolbarImageButton mStrikethrough;
    private RTToolbarImageButton mSuperscript;
    private RTToolbarImageButton mSubscript;
    private RTToolbarImageButton mAlignLeft;
    private RTToolbarImageButton mAlignCenter;
    private RTToolbarImageButton mAlignRight;
    private RTToolbarImageButton mBullet;
    private RTToolbarImageButton mNumber;

    /*
     * The Spinners and their SpinnerAdapters
     */
    private Spinner mFont;
    private SpinnerItemAdapter<FontSpinnerItem> mFontAdapter;

    private Spinner mFontSize;
    private SpinnerItemAdapter<FontSizeSpinnerItem> mFontSizeAdapter;

    private Spinner mFontColor;
    private SpinnerItemAdapter<? extends ColorSpinnerItem> mFontColorAdapter;

    private Spinner mBGColor;
    private SpinnerItemAdapter<? extends ColorSpinnerItem> mBGColorAdapter;

    private final int mCustomColorFont = Color.BLACK;
    private Context context;
    private final int mCustomColorBG = Color.BLACK;

    private final int mPickerId = -1;
    private MainActivity a;
    //  private ColorPickerListener mColorPickerListener;

    // ****************************************** Initialize Methods *******************************************

    public HorizontalRTToolbar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public HorizontalRTToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalRTToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
       a =  ((MainActivity)context);
        mBold = initImageButton(a.Bold);
        mItalic = initImageButton(a.Italic);
        mUnderline = initImageButton(a.Underline);
        mStrikethrough = initImageButton(a.StrikeThrough);
   //     mSuperscript = initImageButton(R.id.toolbar_superscript);
    //    mSubscript = initImageButton(R.id.toolbar_subscript);
        mAlignLeft = initImageButton(a.AlignLeft);
       mAlignCenter = initImageButton(a.AlignCenter);
        mAlignRight = initImageButton(a.AlignRight);
        mBullet = initImageButton(a.Bullets);
        mNumber = initImageButton(a.Numbers);

      //  initImageButton(R.id.toolbar_inc_indent);
      //  initImageButton(R.id.toolbar_dec_indent);
      ////  initImageButton(R.id.toolbar_link);
       // initImageButton(R.id.toolbar_image);
//        initImageButton(R.id.toolbar_undo);
     //   initImageButton(R.id.toolbar_redo);
      //  initImageButton(R.id.toolbar_clear);
        synchronized (sIdCounter) {
            mId = sIdCounter.getAndIncrement();
        }
      //  SetColorPickerListenerEvent.setListener(mPickerId, mColorPickerListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // configure regular action buttons


    }

    private RTToolbarImageButton initImageButton(RTToolbarImageButton icon) {
        RTToolbarImageButton button = icon;
        if (button != null) {
            button.setOnClickListener(this);
        }
        return button;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

     //   if (mColorPickerListener != null && mPickerId != -1) {
       //     SetColorPickerListenerEvent.setListener(mPickerId, mColorPickerListener);
       // }
    }

    // ****************************************** RTToolbar Methods *******************************************

    @Override
    public void setToolbarContainer(ViewGroup toolbarContainer) {
        mToolbarContainer = toolbarContainer;
    }

    @Override
    public ViewGroup getToolbarContainer() {
        return mToolbarContainer == null ? this : mToolbarContainer;
    }

    @Override
    public void setToolbarListener(RTToolbarListener listener) {
        mListener = listener;
    }

    @Override
    public void removeToolbarListener() {
        mListener = null;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public void setBold(boolean enabled) {
        if (mBold != null) mBold.setChecked(enabled);
    }

    @Override
    public void setItalic(boolean enabled) {
        if (mItalic != null) mItalic.setChecked(enabled);
    }

    @Override
    public void setUnderline(boolean enabled) {
        if (mUnderline != null) mUnderline.setChecked(enabled);
    }

    @Override
    public void setStrikethrough(boolean enabled) {
        if (mStrikethrough != null) mStrikethrough.setChecked(enabled);
    }

    @Override
    public void setSuperscript(boolean enabled) {
        if (mSuperscript != null) mSuperscript.setChecked(enabled);
    }

    @Override
    public void setSubscript(boolean enabled) {
        if (mSubscript != null) mSubscript.setChecked(enabled);
    }

    @Override
    public void setBullet(boolean enabled) {
        if (mBullet != null) mBullet.setChecked(enabled);
    }

    @Override
    public void setNumber(boolean enabled) {
        if (mNumber != null) mNumber.setChecked(enabled);
    }

    @Override
    public void setAlignment(Layout.Alignment alignment) {
        if (mAlignLeft != null) mAlignLeft.setChecked(alignment == Layout.Alignment.ALIGN_NORMAL);
        if (mAlignCenter != null)
            mAlignCenter.setChecked(alignment == Layout.Alignment.ALIGN_CENTER);
        if (mAlignRight != null)
            mAlignRight.setChecked(alignment == Layout.Alignment.ALIGN_OPPOSITE);
    }

    @Override
    public void setFont(RTTypeface typeface) {
        if (mFont != null) {
            if (typeface != null) {
                for (int pos = 0; pos < mFontAdapter.getCount(); pos++) {
                    FontSpinnerItem item = mFontAdapter.getItem(pos);
                  //  if (typeface.equals(item.getTypeface())) {
                   //     mFontAdapter.setSelectedItem(pos);
                   //     mFont.setSelection(pos);
                   //     break;
                   // }
                }
            }
            else {
                mFontAdapter.setSelectedItem(0);
                mFont.setSelection(0);
            }
        }
    }

    /**
     * Set the text size.
     *
     * @param size the text size, if -1 then no text size is set (e.g. when selection spans more than one text size)
     */
    @Override
    public void setFontSize(int size) {
        if (mFontSize != null) {
            if (size <= 0) {
                mFontSizeAdapter.updateSpinnerTitle("");
                mFontSizeAdapter.setSelectedItem(0);
                mFontSize.setSelection(0);
            } else {
                size = Helper.convertSpToPx(size);
                mFontSizeAdapter.updateSpinnerTitle(Integer.toString(size));
                for (int pos = 0; pos < mFontSizeAdapter.getCount(); pos++) {
                    FontSizeSpinnerItem item = mFontSizeAdapter.getItem(pos);
                    if (size == item.getFontSize()) {
                        mFontSizeAdapter.setSelectedItem(pos);
                        mFontSize.setSelection(pos);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void setFontColor(int color) {
        if (mFontColor != null) setFontColor(color, mFontColor, mFontColorAdapter);
    }

    @Override
    public void setBGColor(int color) {
        if (mBGColor != null) setFontColor(color, mBGColor, mBGColorAdapter);
    }

    @Override
    public void removeFontColor() {
        if (mFontColor != null) {
            mFontColorAdapter.setSelectedItem(0);
            mFontColor.setSelection(0);
        }
    }

    @Override
    public void removeBGColor() {
        if (mBGColor != null) {
            mBGColorAdapter.setSelectedItem(0);
            mBGColor.setSelection(0);
        }
    }

    private void setFontColor(int color, Spinner spinner, SpinnerItemAdapter<? extends ColorSpinnerItem> adapter) {
        int color2Compare = color & 0xffffff;
        for (int pos = 0; pos < adapter.getCount(); pos++) {
            ColorSpinnerItem item = adapter.getItem(pos);
            if (!item.isEmpty() && color2Compare == (item.getColor() & 0xffffff)) {
                adapter.setSelectedItem(pos);
                spinner.setSelection(pos);
                break;
            }
        }
    }

    // ****************************************** Item Selected Methods *******************************************

    @Override
    public void onClick(View v) {
        if (mListener != null) {

            int id = v.getId();
            if (id == R.id.toolbar_bold) {
                mBold.setChecked(!mBold.isChecked());
                mListener.onEffectSelected(Effects.BOLD, mBold.isChecked());
            }

            else if (id == R.id.toolbar_italic) {
                mItalic.setChecked(!mItalic.isChecked());
                mListener.onEffectSelected(Effects.ITALIC, mItalic.isChecked());
            }

            else if (id == R.id.toolbar_underline) {
                mUnderline.setChecked(!mUnderline.isChecked());
                mListener.onEffectSelected(Effects.UNDERLINE, mUnderline.isChecked());
            }

            else if (id == R.id.toolbar_strikethrough) {
                mStrikethrough.setChecked(!mStrikethrough.isChecked());
                mListener.onEffectSelected(Effects.STRIKETHROUGH, mStrikethrough.isChecked());
            }

            else if (id == R.id.toolbar_superscript) {
                mSuperscript.setChecked(!mSuperscript.isChecked());
                mListener.onEffectSelected(Effects.SUPERSCRIPT, mSuperscript.isChecked());
                if (mSuperscript.isChecked() && mSubscript != null) {
                    mSubscript.setChecked(false);
                    mListener.onEffectSelected(Effects.SUBSCRIPT, mSubscript.isChecked());
                }
            }

            else if (id == R.id.toolbar_subscript) {
                mSubscript.setChecked(!mSubscript.isChecked());
                mListener.onEffectSelected(Effects.SUBSCRIPT, mSubscript.isChecked());
                if (mSubscript.isChecked() && mSuperscript != null) {
                    mSuperscript.setChecked(false);
                    mListener.onEffectSelected(Effects.SUPERSCRIPT, mSuperscript.isChecked());
                }
            }

            else if (id == R.id.toolbar_align_left) {
                if (mAlignLeft != null) mAlignLeft.setChecked(true);
                if (mAlignCenter != null) mAlignCenter.setChecked(false);
                if (mAlignRight != null) mAlignRight.setChecked(false);
                mListener.onEffectSelected(Effects.ALIGNMENT, Layout.Alignment.ALIGN_NORMAL);
            }

            else if (id == R.id.toolbar_align_center) {
                if (mAlignLeft != null) mAlignLeft.setChecked(false);
                if (mAlignCenter != null) mAlignCenter.setChecked(true);
                if (mAlignRight != null) mAlignRight.setChecked(false);
                mListener.onEffectSelected(Effects.ALIGNMENT, Layout.Alignment.ALIGN_CENTER);
            }

            else if (id == R.id.toolbar_align_right) {
                if (mAlignLeft != null) mAlignLeft.setChecked(false);
                if (mAlignCenter != null) mAlignCenter.setChecked(false);
                if (mAlignRight != null) mAlignRight.setChecked(true);
                mListener.onEffectSelected(Effects.ALIGNMENT, Layout.Alignment.ALIGN_OPPOSITE);
            }

            else if (id == R.id.toolbar_bullet) {
                mBullet.setChecked(!mBullet.isChecked());
                boolean isChecked = mBullet.isChecked();
                mListener.onEffectSelected(Effects.BULLET, isChecked);
                if (isChecked && mNumber != null) {
                    mNumber.setChecked(false);    // numbers will be removed by the NumberEffect.applyToSelection
                }
            }

            else if (id == R.id.toolbar_number) {
                mNumber.setChecked(!mNumber.isChecked());
                boolean isChecked = mNumber.isChecked();
                mListener.onEffectSelected(Effects.NUMBER, isChecked);
                if (isChecked && mBullet != null) {
                    mBullet.setChecked(false);    // bullets will be removed by the BulletEffect.applyToSelection
                }
            }

            else if (id == R.id.toolbar_inc_indent) {
                mListener.onEffectSelected(Effects.INDENTATION, Helper.getLeadingMarging());
            }

            else if (id == R.id.toolbar_dec_indent) {
                mListener.onEffectSelected(Effects.INDENTATION, -Helper.getLeadingMarging());
            }

            else if (id == R.id.toolbar_link) {
                mListener.onCreateLink();
            }

            else if (id == R.id.toolbar_image) {
                mListener.onPickImage();
            }

            else if (id == R.id.toolbar_image_capture) {
                mListener.onCaptureImage();
            }

            else if (id == R.id.toolbar_clear) {
                mListener.onClearFormatting();
            }

            else if (id == R.id.toolbar_undo) {
                mListener.onUndo();
            }

            else if (id == R.id.toolbar_redo) {
                mListener.onRedo();
            }
        }
    }

}
