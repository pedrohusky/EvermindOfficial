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
import android.os.Build;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
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

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
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

    @Nullable
    private RTToolbarListener mListener;

    private ViewGroup mToolbarContainer;

    private int size = 25;

    /*
     * The buttons
     */
    private RTToolbarImageButton mBold;
    private RTToolbarImageButton mItalic;
    private RTToolbarImageButton mUnderline;
    private RTToolbarImageButton mStrikethrough;
    private RTToolbarImageButton mAlignLeft;
    private RTToolbarImageButton mAlignCenter;
    private RTToolbarImageButton mAlignRight;
    private RTToolbarImageButton mBullet;
    private RTToolbarImageButton mNumber;

    /*
     * The Spinners and their SpinnerAdapters
     */

    private Context context;
    private final WeakReference<MainActivity> mainActivity;

    //  private ColorPickerListener mColorPickerListener;

    // ****************************************** Initialize Methods *******************************************

    public HorizontalRTToolbar(Context context) {
        super(context);
        this.context = context;
        mainActivity = new WeakReference<>(((MainActivity)context));
        init();
    }

    public HorizontalRTToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = new WeakReference<>(((MainActivity)context));
    }

    public HorizontalRTToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mainActivity = new WeakReference<>(((MainActivity)context));
    }

    public void init() {
    //    RTEditText editor = a.getNoteCreator().getActiveEditor();


           mBold = initImageButton(mainActivity.get().getButtonsBinding().toolbarBold);
           mItalic = initImageButton(mainActivity.get().getButtonsBinding().toolbarItalic);
           mUnderline = initImageButton(mainActivity.get().getButtonsBinding().toolbarUnderline);
           mStrikethrough = initImageButton(mainActivity.get().getButtonsBinding().toolbarStrikethrough);

           mAlignLeft = initImageButton(mainActivity.get().getButtonsBinding().toolbarAlignLeft);
           mAlignCenter = initImageButton(mainActivity.get().getButtonsBinding().toolbarAlignCenter);
           mAlignRight = initImageButton(mainActivity.get().getButtonsBinding().toolbarAlignRight);
           mBullet = initImageButton(mainActivity.get().getButtonsBinding().toolbarBullet);
           mNumber = initImageButton(mainActivity.get().getButtonsBinding().toolbarNumber);
           initImageButton(mainActivity.get().getButtonsBinding().upDraw);
        initImageButton(mainActivity.get().getButtonsBinding().downDraw);
        initImageButton(mainActivity.get().getButtonsBinding().ChangeColor1);
        initImageButton(mainActivity.get().getButtonsBinding().DecreaseSize1);
        initImageButton(mainActivity.get().getButtonsBinding().IncreaseSize1);
        initImageButton(mainActivity.get().getButtonsBinding().Delete);
      //  initImageButton(mainActivity.get().getButtonsBinding().toolbarUndo);
        initImageButton(mainActivity.get().getButtonsBinding().toolbarRedo);
        initImageButton(mainActivity.get().getButtonsBinding().play);
        initImageButton(mainActivity.get().getButtonsBinding().saveAudio);
        initImageButton(mainActivity.get().getButtonsBinding().imageButton2);
        initImageButton(mainActivity.get().getButtonsBinding().imageButton4);
        initImageButton(mainActivity.get().getButtonsBinding().imageButton5);
        initImageButton(mainActivity.get().getButtonsBinding().Files);
        initImageButton(mainActivity.get().getButtonsBinding().Gallery);
        initImageButton(mainActivity.get().getButtonsBinding().DrawChangeColor);
        initImageButton(mainActivity.get().getButtonsBinding().DrawChangeSize);
        initImageButton(mainActivity.get().getButtonsBinding().HighlightText1);
        initImageButton(mainActivity.get().getButtonsBinding().changeNotecolorButton);
        initImageButton(mainActivity.get().getButtonsBinding().toolbarUndo);
   //     mSuperscript = initImageButton(R.id.toolbar_superscript);
    //    mSubscript = initImageButton(R.id.toolbar_subscript);

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

      //  a.getmRTManager().onSelectionChanged(editor, editor.getSelectionStart(), editor.getSelectionEnd());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // configure regular action buttons


    }

    @Nullable
    private RTToolbarImageButton initImageButton(@Nullable RTToolbarImageButton icon) {
        if (icon != null) {
            icon.initButton();
            icon.setOnClickListener(this);
        }
        return icon;
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

    @NonNull
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

    }

    @Override
    public void setSubscript(boolean enabled) {
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
       // if (mFont != null) {
       //     if (typeface != null) {
       //         for (int pos = 0; pos < mFontAdapter.getCount(); pos++) {
        //            FontSpinnerItem item = mFontAdapter.getItem(pos);
       //           //  if (typeface.equals(item.getTypeface())) {
                   //     mFontAdapter.setSelectedItem(pos);
                   //     mFont.setSelection(pos);
                   //     break;
                   // }
         //       }
        //    }
        //    else {
      //          mFontAdapter.setSelectedItem(0);
       //         mFont.setSelection(0);
       //     }
      //  }
    }

    /**
     * Set the text size.
     *
     * @param size the text size, if -1 then no text size is set (e.g. when selection spans more than one text size)
     */
    @Override
    public void setFontSize(int size) {
     //   if (mFontSize != null) {
     //       if (size <= 0) {
     //           mFontSizeAdapter.updateSpinnerTitle("");
     //           mFontSizeAdapter.setSelectedItem(0);
     //           mFontSize.setSelection(0);
     //       } else {
     //           size = Helper.convertSpToPx(size);
      //          mFontSizeAdapter.updateSpinnerTitle(Integer.toString(size));
      //          for (int pos = 0; pos < mFontSizeAdapter.getCount(); pos++) {
       //             FontSizeSpinnerItem item = mFontSizeAdapter.getItem(pos);
       //             if (size == item.getFontSize()) {
        //                mFontSizeAdapter.setSelectedItem(pos);
        //                mFontSize.setSelection(pos);
        //                break;
        //            }
        //        }
       //     }
      //  }
    }

    @Override
    public void setFontColor(int color) {
        //if (mFontColor != null) setFontColor(color, mFontColor, mFontColorAdapter);
    }

    @Override
    public void setBGColor(int color) {
       // if (mBGColor != null) setFontColor(color, mBGColor, mBGColorAdapter);
    }

    @Override
    public void removeFontColor() {
      //  if (mFontColor != null) {
      //      mFontColorAdapter.setSelectedItem(0);
      //      mFontColor.setSelection(0);
       // }
    }

    @Override
    public void removeBGColor() {
     //   if (mBGColor != null) {
     //       mBGColorAdapter.setSelectedItem(0);
      //      mBGColor.setSelection(0);
      //  }
    }

    // ****************************************** Item Selected Methods *******************************************

    @Override
    public void onClick(@NonNull View v) {
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

            else if (id == R.id.IncreaseSize1) {
                if (size < 76) {

                    size++;
                    mainActivity.get().getNoteCreator().getActiveEditor().applyEffect(Effects.FONTSIZE, size);
                    // noteCreator.activeEditor.setFontSize(size);
                }
            }

            else if (id == R.id.DecreaseSize1) {
                if (size > 10) {

                    size--;
                    mainActivity.get().getNoteCreator().getActiveEditor().applyEffect(Effects.FONTSIZE, size);
                    // noteCreator.getActiveEditor().setFontSize(size);
                }
            }

            else if (id == R.id.ChangeColor1) {
                mainActivity.get().getEverBallsHelper().initEverBallsForeground();
            }

            else if (id == R.id.HighlightText1) {
                mainActivity.get().getEverBallsHelper().initEverBallsHighlight();
            }

            else if (id == R.id.DrawChangeColor) {
                mainActivity.get().getEverBallsHelper().initEverBallsDraw();
            }

            else if (id == R.id.toolbar_undo) {
                mainActivity.get().getEverViewManagement().animateObject(v, "rotation", -360, 250);
                if ( mainActivity.get().getEverViewManagement().isDrawing()) {
                    mainActivity.get().getNoteCreator().getEverDraw().undo();
                } else {
                    mainActivity.get().getEverThemeHelper().enterDarkMode();
                    mainActivity.get().getmRTManager().onUndo();
                }
            }

            else if (id == R.id.toolbar_redo) {
                mainActivity.get().getEverViewManagement().animateObject(v, "rotation", 360, 250);
                if ( mainActivity.get().getEverViewManagement().isDrawing()) {
                    mainActivity.get().getNoteCreator().getEverDraw().redo();
                } else {
                    mainActivity.get().getmRTManager().onRedo();
                }
            }

            else if (id == R.id.imageButton5) {
                mainActivity.get().getEverBallsHelper().initEverBallsPaintType();
            }

            else if (id == R.id.changeNotecolorButton) {
                Toast.makeText(mainActivity.get(), v.toString(), Toast.LENGTH_SHORT).show();
                mainActivity.get().getEverBallsHelper().initEverBallsNoteColor();
            }

            else if (id == R.id.imageButton4) {
                mainActivity.get().getNoteCreator().getEverDraw().setColor(Color.WHITE);
            }

            else if (id == R.id.Delete) {
                if (view.getTag().equals("Save")) {
                    if (everViewManagement.isDrawing()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            noteCreator.SaveBitmapFromDraw();
                        }
                    } else {
                        onBackPressed();
                    }
                }
                if (view.getTag().equals("GridLayout")) {
                    changeLayout(view);
                }
            }

           // else if (id == R.id.toolbar_inc_indent) {
            //    mListener.onEffectSelected(Effects.INDENTATION, Helper.getLeadingMarging());
           // }

           // else if (id == R.id.toolbar_dec_indent) {
           //     mListener.onEffectSelected(Effects.INDENTATION, -Helper.getLeadingMarging());
           // }

           // else if (id == R.id.toolbar_link) {
          //      mListener.onCreateLink();
          //  }

          //  else if (id == R.id.toolbar_image) {
          //      mListener.onPickImage();
           // }

          //  else if (id == R.id.toolbar_image_capture) {
          //      mListener.onCaptureImage();
          //  }

           // else if (id == R.id.toolbar_clear) {
         //       mListener.onClearFormatting();
          //  }

           
        }
    }

}
