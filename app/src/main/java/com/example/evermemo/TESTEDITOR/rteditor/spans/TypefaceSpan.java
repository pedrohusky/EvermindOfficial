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

package com.example.evermemo.TESTEDITOR.rteditor.spans;


import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.TextPaint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.evermemo.TESTEDITOR.rteditor.fonts.RTTypeface;

/**
 * Custom TypefaceSpan class to add support for fonts
 */
public class TypefaceSpan extends android.text.style.TypefaceSpan implements RTSpan<RTTypeface> {

    @Nullable
    private final RTTypeface mTypeface;

    public TypefaceSpan(RTTypeface typeface) {
        super("");
        mTypeface = typeface;
    }

    public TypefaceSpan(@NonNull Parcel src) {
        super(src);
        mTypeface = null;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint paint) {
        applyCustomTypeFace(paint, mTypeface.getTypeface());
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        applyCustomTypeFace(paint, mTypeface.getTypeface());
    }

    private void applyCustomTypeFace(@NonNull Paint paint, @NonNull Typeface tf) {
        Typeface old = paint.getTypeface();
        int oldStyle = old == null ? 0 : old.getStyle();

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }

    @Nullable
    @Override
    public RTTypeface getValue() {
        return mTypeface;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(null, flags);
    }
}
