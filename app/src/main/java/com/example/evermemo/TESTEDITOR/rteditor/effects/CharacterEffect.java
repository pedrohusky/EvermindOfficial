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

import android.text.Spannable;
import android.text.Spanned;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.evermemo.TESTEDITOR.rteditor.RTEditText;
import com.example.evermemo.TESTEDITOR.rteditor.spans.RTSpan;
import com.example.evermemo.TESTEDITOR.rteditor.utils.Selection;

/**
 * CharacterEffects are always applied to the current selection, like bold text or typeface.
 */
abstract class CharacterEffect<V, C extends RTSpan<V>> extends Effect<V, C> {

    @NonNull
    @Override
    final protected SpanCollector<V> newSpanCollector(Class<? extends RTSpan<V>> spanClazz) {
        return new CharacterSpanCollector<V>(spanClazz);
    }

    @NonNull
    @Override
    final protected Selection getSelection(@NonNull RTEditText editor) {
        return new Selection(editor);
    }

    /**
     * Apply this effect to the selection.
     * If value is Null then the effect will be removed from the current selection.
     *
     * @param editor The editor to apply the effect to (current selection)
     * @param value  The value to apply (depends on the Effect)
     */
    public void applyToSelection(@NonNull RTEditText editor, @Nullable V value) {
        Selection selection = getSelection(editor);
        // SPAN_INCLUSIVE_INCLUSIVE is default for empty spans
        int flags = selection.isEmpty() ? Spanned.SPAN_INCLUSIVE_INCLUSIVE : Spanned.SPAN_EXCLUSIVE_INCLUSIVE;

        Spannable str = editor.getText();

        for (RTSpan<V> span : getSpans(str, selection, SpanCollectMode.SPAN_FLAGS)) {
            boolean sameSpan = span.getValue().equals(value);
            int spanStart = str.getSpanStart(span);
            if (spanStart < selection.start()) {
                // process preceding spans
                if (sameSpan) {
                    // we have a preceding span --> use SPAN_EXCLUSIVE_INCLUSIVE instead of SPAN_INCLUSIVE_INCLUSIVE
                    flags = Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
                    selection.offset(selection.start() - spanStart, 0);
                } else {
                    str.setSpan(newSpan(span.getValue()), spanStart, selection.start(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            int spanEnd = str.getSpanEnd(span);
            if (spanEnd > selection.end()) {
                // process succeeding spans
                if (sameSpan) {
                    selection.offset(0, spanEnd - selection.end());
                } else {
                    str.setSpan(newSpan(span.getValue()), selection.end(), spanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }
            }
            str.removeSpan(span);
        }

        if (value != null) {
            RTSpan<V> newSpan = newSpan(value);
            if (newSpan != null) {
                str.setSpan(newSpan, selection.start(), selection.end(), flags);
            }
        }
    }


    /**
     * Create an RTSpan for this effect.
     * This method is used in applyToSelection(RTEditText, V).
     * If the RTSpan can't be created only with V as value parameter then applyToSelection needs
     * to be implemented by the sub class.
     *
     * @return the class of the span this effect supports. Can be Null but then the subclass has to
     * override applyToSelection(RTEditText, V)
     */
    @Nullable
    abstract protected RTSpan<V> newSpan(V value);

}
