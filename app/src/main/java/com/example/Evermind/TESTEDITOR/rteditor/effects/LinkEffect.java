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

package com.example.Evermind.TESTEDITOR.rteditor.effects;

import android.text.Spannable;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.spans.LinkSpan;
import com.example.Evermind.TESTEDITOR.rteditor.spans.RTSpan;
import com.example.Evermind.TESTEDITOR.rteditor.utils.Selection;

/**
 * Links.
 */
public class LinkEffect extends CharacterEffect<String, LinkSpan> {

    @NonNull
    @Override
    protected RTSpan<String> newSpan(String value) {
        return new LinkSpan(value);
    }

    @Override
    public void applyToSelection(@NonNull RTEditText editor, @Nullable String url) {
        Selection selection = getSelection(editor);
        Spannable str = editor.getText();

        if (url == null) {
            // adjacent links need to be removed --> expand the selection by [1, 1]
            for (RTSpan<String> span : getSpans(str, selection.offset(1, 1), SpanCollectMode.EXACT)) {
                str.removeSpan(span);
            }
        }
        else {
            for (RTSpan<String> span : getSpans(str, selection, SpanCollectMode.EXACT)) {
                str.removeSpan(span);
            }

            // if url is Null then the link won't be set meaning existing links will be removed
            str.setSpan(newSpan(url), selection.start(), selection.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}