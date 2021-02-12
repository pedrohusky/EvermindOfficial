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

package com.example.Evermind.TESTEDITOR.toolbar.spinner;

import androidx.annotation.Nullable;

import com.example.Evermind.TESTEDITOR.rteditor.fonts.RTTypeface;

/**
 * The spinner item for the font.
 */
public class FontSpinnerItem extends SpinnerItem {
    @Nullable
    final private RTTypeface mTypeface;

    public FontSpinnerItem(@Nullable RTTypeface typeface) {
        super(typeface == null ? "" : typeface.getName());
        mTypeface = typeface;
    }

    @Nullable
    public RTTypeface getTypeface() {
        return mTypeface;
    }
}