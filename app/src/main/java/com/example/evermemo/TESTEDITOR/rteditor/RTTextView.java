/*
 * Copyright 2015 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.evermemo.TESTEDITOR.rteditor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.example.evermemo.TESTEDITOR.rteditor.api.format.RTFormat;
import com.example.evermemo.TESTEDITOR.rteditor.api.format.RTHtml;
import com.example.evermemo.TESTEDITOR.rteditor.api.format.RTText;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTMedia;
import com.example.evermemo.TESTEDITOR.rteditor.effects.NumberEffect;
import com.example.evermemo.TESTEDITOR.rteditor.utils.Paragraph;
import com.example.evermemo.TESTEDITOR.rteditor.utils.RTLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RTTextView extends androidx.appcompat.widget.AppCompatTextView {
    private final boolean mUseRTFormatting = true;
    private final Set<RTMedia> mOriginalMedia = new HashSet<RTMedia>();
    private boolean mLayoutChanged;
    private RTLayout mRTLayout;
    private Thread thread;

    public RTTextView(@NonNull Context context) {
        super(context);
    }

    public RTTextView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RTTextView(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @NonNull
    public ArrayList<Paragraph> getParagraphs() {
        return getRTLayout().getParagraphs();
    }


    private RTLayout getRTLayout() {
        synchronized (this) {
            if (mRTLayout == null || mLayoutChanged) {
                mRTLayout = new RTLayout(getEditableText());
                mLayoutChanged = false;
            }
        }
        return mRTLayout;
    }

    public void setRichTextEditing(String content) {

        RTText rtText = new RTHtml<>(RTFormat.HTML, content);

        setText(rtText);
    }

    public void setText(@NonNull RTText rtText) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (rtText.getFormat() instanceof RTFormat.Html) {
                if (mUseRTFormatting) {
                    RTText rtSpanned = rtText.convertTo(RTFormat.SPANNED, null);

                    super.setText(rtSpanned.getText(), BufferType.EDITABLE);
                    String numberText = rtText.getText().toString();
                    if (numberText.contains("</li><li>") || numberText.contains("<ol><li>") || numberText.contains("</li></ol>")) {
                        new NumberEffect().applyToSelectionTextView(this, null, false);
                    }

                    //TODO TO EDIT \/
                    // collect all current media
                    //   Spannable text = getEditableText();
                    //   for (MediaSpan span : text.getSpans(0, text.length(), MediaSpan.class)) {
                    ///     mOriginalMedia.add(span.getMedia());
                    //   }

                    //   Effects.cleanupParagraphs(this);
                } else {
                    RTText rtPlainText = rtText.convertTo(RTFormat.PLAIN_TEXT, null);
                    super.setText(rtPlainText.getText());
                }
            } else if (rtText.getFormat() instanceof RTFormat.PlainText) {
                CharSequence text = rtText.getText();
                super.setText(text == null ? "" : text.toString());
            }

            onSelectionChanged(0, 0);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        // if text is selected we ignore a loss of focus to prevent Android from terminating
        // text selection when one of the spinners opens (text size, color, bg color)
        if (!mUseRTFormatting || hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }
}