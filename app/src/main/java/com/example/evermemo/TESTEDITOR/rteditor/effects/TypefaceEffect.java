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

package com.example.evermemo.TESTEDITOR.rteditor.effects;

import androidx.annotation.Nullable;

import com.example.evermemo.TESTEDITOR.rteditor.fonts.RTTypeface;
import com.example.evermemo.TESTEDITOR.rteditor.spans.RTSpan;
import com.example.evermemo.TESTEDITOR.rteditor.spans.TypefaceSpan;

/**
 * Typeface / Fonts.
 */
public class TypefaceEffect extends CharacterEffect<RTTypeface, TypefaceSpan> {

    /**
     * @return If the value is Null then return Null -> remove all TypefaceSpan.
     */
    @Nullable
    @Override
    protected RTSpan<RTTypeface> newSpan(@Nullable RTTypeface value) {
        return value == null ? null : new TypefaceSpan(value);
    }

}
