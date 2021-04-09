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

import android.os.Parcel;

import androidx.annotation.NonNull;

/**
 * Our custom UnderlineSpan.
 * We need this to be able to distinguish between underlining done by the app and underlining
 * inserted by e.g. a spell checker or a keyboard with auto-complete function.
 */
public class UnderlineSpan extends android.text.style.UnderlineSpan implements RTSpan<Boolean> {

    public UnderlineSpan(Parcel src) {
        super(null);
    }

    @Override
    public Boolean getValue() {
        return Boolean.TRUE;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(null, flags);
    }
}
