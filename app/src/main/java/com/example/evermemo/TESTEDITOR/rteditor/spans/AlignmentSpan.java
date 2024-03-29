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
import android.text.Layout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for an alignment span (left, center, right alignment)
 */
public class AlignmentSpan extends android.text.style.AlignmentSpan.Standard implements RTSpan<Layout.Alignment>, RTParagraphSpan<Layout.Alignment> {

    /*
     * Map alignment directions for RTL languages (Hebrew, Arabic etc.)
     */
    private static final Map<Layout.Alignment, Layout.Alignment> sRTLMapping = new HashMap<>();

    static {
        sRTLMapping.put(Layout.Alignment.ALIGN_CENTER, Layout.Alignment.ALIGN_CENTER);
        sRTLMapping.put(Layout.Alignment.ALIGN_NORMAL, Layout.Alignment.ALIGN_OPPOSITE);
        sRTLMapping.put(Layout.Alignment.ALIGN_OPPOSITE, Layout.Alignment.ALIGN_NORMAL);
    }

    private boolean mIsRTL;

    public AlignmentSpan(Layout.Alignment align, boolean isRTL) {
        super(isRTL ? sRTLMapping.get(align) : align);

        mIsRTL = isRTL;
    }

    public AlignmentSpan(@NonNull Parcel src) {
        super(src);
    }

    @Nullable
    @Override
    public Layout.Alignment getValue() {
        Layout.Alignment align = getAlignment();
        return mIsRTL ? sRTLMapping.get(align) : align;
    }

    @Nullable
    @Override
    public com.example.evermemo.TESTEDITOR.rteditor.spans.AlignmentSpan createClone() {
        return new com.example.evermemo.TESTEDITOR.rteditor.spans.AlignmentSpan(getValue(), mIsRTL);
    }
}
