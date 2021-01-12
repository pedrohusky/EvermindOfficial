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

package com.example.Evermind.TESTEDITOR.rteditor;

import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ParagraphStyle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;

import com.example.Evermind.TESTEDITOR.rteditor.api.RTMediaFactory;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTEditable;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTFormat;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTHtml;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTPlainText;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTText;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTAudio;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTImage;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTMedia;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTVideo;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effect;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effects;
import com.example.Evermind.TESTEDITOR.rteditor.effects.NumberEffect;
import com.example.Evermind.TESTEDITOR.rteditor.spans.BulletSpan;
import com.example.Evermind.TESTEDITOR.rteditor.spans.LinkSpan;
import com.example.Evermind.TESTEDITOR.rteditor.spans.MediaSpan;
import com.example.Evermind.TESTEDITOR.rteditor.spans.NumberSpan;
import com.example.Evermind.TESTEDITOR.rteditor.spans.RTSpan;
import com.example.Evermind.TESTEDITOR.rteditor.utils.Paragraph;
import com.example.Evermind.TESTEDITOR.rteditor.utils.RTLayout;
import com.example.Evermind.TESTEDITOR.rteditor.utils.Selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.N_MR1;

/**
 * The actual rich text editor (extending android.widget.EditText).
 */
public class RTEditText extends androidx.appcompat.widget.AppCompatEditText implements TextWatcher, SpanWatcher, LinkSpan.LinkSpanListener {

    // don't allow any formatting in text mode
    private boolean mUseRTFormatting = true;

    // for performance reasons we compute a new layout only if the text has changed
    private boolean mLayoutChanged;
    private RTLayout mRTLayout;    // don't call this mLayout because TextView has a mLayout too (no shadowing as both are private but still...)

    // while onSaveInstanceState() is running, don't modify any spans
    private boolean mIsSaving;

    /// while selection is changing don't apply any effects
    private boolean mIsSelectionChanging = false;

    // text has changed
    private boolean mTextChanged;

    // this indicates whether text is selected or not -> ignore window focus changes (by spinners)
    private boolean mTextSelected;

    private RTEditTextListener mListener;

    private RTMediaFactory<RTImage, RTAudio, RTVideo> mMediaFactory;

    private final Map<Integer, Stack<RTOperationManager.Operation>> mUndoStacks = new HashMap<>();
    private final Map<Integer, Stack<RTOperationManager.Operation>> mRedoStacks = new HashMap<>();

    // used to check if selection has changed
    private int mOldSelStart = -1;
    private int mOldSelEnd = -1;
    // we don't want to call Effects.cleanupParagraphs() if the paragraphs are already up to date
    private boolean mParagraphsAreUp2Date;
    // while Effects.cleanupParagraphs() is called, we ignore changes that would alter mParagraphsAreUp2Date
    private boolean mIgnoreParagraphChanges;

    /* Used for the undo / redo functions */

    // if True then text changes are not registered for undo/redo
    // we need this during the actual undo/redo operation (or an undo would create a change event itself)
    private boolean mIgnoreTextChanges;

    // Keep track of ordered list spans.
    // If one of them is selected, then append zero with char after new line.
    // This fixes the bug when bullet/number span is not applied to empty string
    private boolean mIsBulletSpanSelected;
    private boolean mIsNumberSpanSelected;
    private static final int MAX_NR_OF_OPERATIONS = 15;
    // The length of text before new char is added
    private int mPreviousTextLength;

    private int mSelStartBefore;        // selection start before text changed
    private int mSelEndBefore;          // selection end before text changed
    private String mOldText;            // old text before it changed
    private String mNewText;            // new text after it changed (needed in afterTextChanged to see if the text has changed)
    private Spannable mOldSpannable;    // undo/redo

    // we need to keep track of the media for this editor to be able to clean up after we're done
    private final Set<RTMedia> mOriginalMedia = new HashSet<RTMedia>();
    private final Set<RTMedia> mAddedMedia = new HashSet<RTMedia>();

    // ****************************************** Lifecycle Methods *******************************************

    public RTEditText(Context context) {
        super(context);
        init();
    }

    public RTEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RTEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_FILTER);
      //  addTextChangedListener(this);
        // we need this or links won't be clickable
        setMovementMethod(RTEditorMovementMethod.getInstance());
        allowImageInsertion();

    }

    /**
     * @param isSaved True if the text is saved, False if it's dismissed
     */
    void onDestroy(boolean isSaved) {
        // make sure all obsolete MediaSpan files are removed from the file system:
        // - when saving the text delete the MediaSpan if it was deleted
        // - when dismissing the text delete the MediaSpan if it was deleted and not saved before

        // collect all media the editor contains currently
        Set<RTMedia> mCurrentMedia = new HashSet<RTMedia>();
        Spannable text = getText();
        for (MediaSpan span : text.getSpans(0, text.length(), MediaSpan.class)) {
            mCurrentMedia.add(span.getMedia());
        }

        // now delete all those that aren't needed any longer
        Set<RTMedia> mMedia2Delete = isSaved ? mOriginalMedia : mCurrentMedia;
        mMedia2Delete.addAll(mAddedMedia);
        Set<RTMedia> mMedia2Keep = isSaved ? mCurrentMedia : mOriginalMedia;
        for (RTMedia media : mMedia2Delete) {
            if (!mMedia2Keep.contains(media)) {
                media.remove();
            }
        }
    }

    /**
     * Needs to be called if a media is added to the editor.
     * Important to be able to delete obsolete media once we're done editing.
     */
    void onAddMedia(RTMedia media) {
        mAddedMedia.add(media);
    }

    /**
     * This needs to be called before anything else because we need the media
     * factory.
     *
     * @param listener     The RTEditTextListener (the RTManager)
     * @param mediaFactory The RTMediaFactory
     */
    void register(RTEditTextListener listener, RTMediaFactory<RTImage, RTAudio, RTVideo> mediaFactory) {
        mListener = listener;
        mMediaFactory = mediaFactory;
    }

    /**
     * Usually called from the RTManager.onDestroy() method
     */
    void unregister() {
        mListener = null;
        mMediaFactory = null;
    }

    /**
     * Return all paragraphs as as array of selection objects
     */
    public ArrayList<Paragraph> getParagraphs() {
        return getRTLayout().getParagraphs();
    }

    /**
     * Find the start and end of the paragraph(s) encompassing the current selection.
     * A paragraph spans from one \n (exclusive) to the next one (inclusive)
     */
    public Selection getParagraphsInSelection() {
        RTLayout layout = getRTLayout();

        Selection selection = new Selection(this);
        int firstLine = layout.getLineForOffset(selection.start());
        int end = selection.isEmpty() ? selection.end() : selection.end() - 1;
        int lastLine = layout.getLineForOffset(end);

        return new Selection(layout.getLineStart(firstLine), layout.getLineEnd(lastLine));
    }

    private RTLayout getRTLayout() {
        synchronized (this) {
            if (mRTLayout == null || mLayoutChanged) {
                mRTLayout = new RTLayout(getText());
                mLayoutChanged = false;
            }
        }
        return mRTLayout;
    }

    /**
     * This method returns the Selection which makes sure that selection start is <= selection end.
     * Note: getSelectionStart()/getSelectionEnd() refer to the order in which text was selected.
     */
    Selection getSelection() {
        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        return new Selection(selStart, selEnd);
    }

    /**
     * @return the selected text (needed when creating links)
     */
    String getSelectedText() {
        Spannable text = getText();
        Selection sel = getSelection();
        if (sel.start() >= 0 && sel.end() >= 0 && sel.end() <= text.length()) {
            return text.subSequence(sel.start(), sel.end()).toString();
        }
        return null;
    }

    public Spannable cloneSpannable() {
        CharSequence text = super.getText();
        return new ClonedSpannableString(text != null ? text : "");
    }

    // ****************************************** Set/Get Text Methods *******************************************

    /**
     * Sets the edit mode to plain or rich text. The text will be converted
     * automatically to rich/plain text if autoConvert is True.
     *
     * @param useRTFormatting True if the edit mode should be rich text, False if the edit
     *                        mode should be plain text
     * @param autoConvert     Automatically convert the content to plain or rich text if
     *                        this is True
     */
    public void setRichTextEditing(boolean useRTFormatting, boolean autoConvert) {
        assertRegistration();

        if (useRTFormatting != mUseRTFormatting) {
            mUseRTFormatting = useRTFormatting;

            if (autoConvert) {
                RTFormat targetFormat = useRTFormatting ? RTFormat.PLAIN_TEXT : RTFormat.HTML;
                setText(getRichText(targetFormat));
            }

            if (mListener != null) {
                mListener.onRichTextEditingChanged(this, mUseRTFormatting);
            }
        }
    }

    /**
     * Sets the edit mode to plain or rich text and updates the content at the
     * same time. The caller needs to make sure the content matches the correct
     * format (if you pass in html code as plain text the editor will show the
     * html code).
     *
     * @param useRTFormatting True if the edit mode should be rich text, False if the edit
     *                        mode should be plain text
     * @param content         The new content
     */
    public void setRichTextEditing(boolean useRTFormatting, String content) {
        assertRegistration();

        if (useRTFormatting != mUseRTFormatting) {
            mUseRTFormatting = useRTFormatting;

            if (mListener != null) {
                mListener.onRichTextEditingChanged(this, mUseRTFormatting);
            }
        }

        RTText rtText = useRTFormatting ?
                new RTHtml<RTImage, RTAudio, RTVideo>(RTFormat.HTML, content) :
                new RTPlainText(content);

        setText(rtText);
    }

    /**
     * Set the text for this editor.
     * <p>
     * It will convert the text from rich text to plain text if the editor's
     * mode is set to use plain text. or to a spanned text (only supported
     * formatting) if the editor's mode is set to use rich text
     * <p>
     * We need to prevent onSelectionChanged() to do anything as long as
     * setText() hasn't finished because the Layout doesn't seem to update
     * before setText has finished but onSelectionChanged will still be called
     * during setText and will receive the out-dated Layout which doesn't allow
     * us to apply styles and such.
     */
    public void setText(RTText rtText) {
       // new Handler(Looper.getMainLooper()).post(() -> {
            assertRegistration();
            if (rtText.getFormat() instanceof RTFormat.Html) {
                if (mUseRTFormatting) {
                    RTText rtSpanned = rtText.convertTo(RTFormat.SPANNED, mMediaFactory);

                    super.setText(rtSpanned.getText(), BufferType.EDITABLE);
                    new NumberEffect().applyToSelection(this, null, null);
                    addSpanWatcher();

                    // collect all current media
                    Spannable text = getText();
                    for (MediaSpan span : Objects.requireNonNull(text).getSpans(0, text.length(), MediaSpan.class)) {
                        mOriginalMedia.add(span.getMedia());
                    }

                     Effects.cleanupParagraphs(this);
                } else {
                    RTText rtPlainText = rtText.convertTo(RTFormat.PLAIN_TEXT, mMediaFactory);
                    super.setText(rtPlainText.getText());
                }
            } else if (rtText.getFormat() instanceof RTFormat.PlainText) {
                CharSequence text = rtText.getText();
                super.setText(text == null ? "" : text.toString());
            }

            onSelectionChanged(0, 0);
    //    });
    }

    public boolean usesRTFormatting() {
        return mUseRTFormatting;
    }

    /**
     * Returns the content of this editor as a String. The caller is responsible
     * to call only formats that are supported by RTEditable (which is the rich
     * text editor's format and always the source format).
     *
     * @param format The RTFormat the text should be converted to.
     * @throws UnsupportedOperationException if the target format isn't supported.
     */
    public String getText(RTFormat format) {
        return getRichText(format).getText().toString();
    }

    /**
     * Same as "String getText(RTFormat format)" but this method returns the
     * RTText instead of just the actual text.
     */
    public RTText getRichText(RTFormat format) {
        assertRegistration();

        RTEditable rtEditable = new RTEditable(this);
        return rtEditable.convertTo(format, mMediaFactory);
    }

    private void assertRegistration() {
        if (mMediaFactory == null) {
            throw new IllegalStateException("The RTMediaFactory is null. Please make sure to register the editor at the RTManager before using it.");
        }
    }

    // ****************************************** TextWatcher / SpanWatcher *******************************************

    public boolean hasChanged() {
        return mTextChanged;
    }

    public void resetHasChanged() {
        mTextChanged = false;
        setParagraphsAreUp2Date(false);
    }

    /**
     * Ignore changes that would trigger a RTEditTextListener.onTextChanged()
     * method call. We need this during the actual undo/redo operation (or an
     * undo would create a change event itself).
     */
    synchronized void ignoreTextChanges() {
        mIgnoreTextChanges = true;
    }

    /**
     * If changes happen call RTEditTextListener.onTextChanged().
     * This is needed for the undo/redo functionality.
     */
    synchronized void registerTextChanges() {
        mIgnoreTextChanges = false;
    }

    @Override
    /* TextWatcher */
    public synchronized void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //TODO MAKE A FUNCTION TO NOT WORK IN SPANS WHEN REMOVING THEM
        // we use a String to get a static copy of the CharSequence (the CharSequence changes when the text changes...)
        String oldText = mOldText == null ? "" : mOldText;
        if (!mIgnoreTextChanges && !s.toString().equals(oldText)) {
            mSelStartBefore = getSelectionStart();
            mSelEndBefore = getSelectionEnd();
            mOldText = s.toString();
            mNewText = mOldText;
            mOldSpannable = cloneSpannable();
        }
        mLayoutChanged = true;
        mPreviousTextLength = s.length();
    }

    @Override
    /* TextWatcher */
    public synchronized void onTextChanged(CharSequence s, int start, int before, int count) {
        mLayoutChanged = true;
    }

    @Override
    /* TextWatcher */
    public synchronized void afterTextChanged(Editable s) {
        if (mIsBulletSpanSelected || mIsNumberSpanSelected) {
            boolean mBackSpace = mPreviousTextLength >= s.length();
            if (!mBackSpace && s.toString().endsWith("\n")) {
                // append zero width character
                this.append("\u200B");
            }
        }
        String theText = s.toString();
        String newText = mNewText == null ? "" : mNewText;
        if (mListener != null && !mIgnoreTextChanges && !newText.equals(theText)) {
            Spannable newSpannable = cloneSpannable();
            mListener.onTextChanged(this, mOldSpannable, newSpannable, mSelStartBefore, mSelEndBefore, getSelectionStart(), getSelectionEnd());
            mNewText = theText;
        }
        mLayoutChanged = true;
        mTextChanged = true;
        setParagraphsAreUp2Date(false);
        addSpanWatcher();
    }

    @Override
    /* SpanWatcher */
    public void onSpanAdded(Spannable text, Object what, int start, int end) {
        mTextChanged = true;
        // we need to keep track of ordered list spans
        if (what instanceof BulletSpan) {
            mIsBulletSpanSelected = true;
            // if text was empty then append zero width char
            // in order for the bullet to be shown when the span is selected
            if (text.toString().isEmpty()) {
                this.append("\u200B");
            }
        } else if (what instanceof NumberSpan) {
            mIsNumberSpanSelected = true;
            // if text was empty then append zero width char
            // in order for the number to be shown when the span is selected
            if (text.toString().isEmpty()) {
                this.append("\u200B");
            }
        }

        if (what instanceof RTSpan && what instanceof ParagraphStyle) {
            setParagraphsAreUp2Date(false);
        }
    }

    @Override
    /* SpanWatcher */
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
        mTextChanged = true;
        if (what instanceof RTSpan && what instanceof ParagraphStyle) {
            setParagraphsAreUp2Date(false);
        }
    }

    @Override
    /* SpanWatcher */
    public void onSpanRemoved(Spannable text, Object what, int start, int end) {
        mTextChanged = true;
        // we need to keep track of ordered list spans
        if (what instanceof BulletSpan) {
            mIsBulletSpanSelected = false;
        } else if (what instanceof NumberSpan) {
            mIsNumberSpanSelected = false;
        }

        if (what instanceof RTSpan && what instanceof ParagraphStyle) {
            setParagraphsAreUp2Date(false);
        }
    }

    /**
     * Add a SpanWatcher for the Changeable implementation
     */
    private void addSpanWatcher() {
        Spannable spannable = getText();
        if (spannable.getSpans(0, spannable.length(), getClass()) != null) {
            spannable.setSpan(this, 0, spannable.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    synchronized private void setParagraphsAreUp2Date(boolean value) {
        if (! mIgnoreParagraphChanges) {
            mParagraphsAreUp2Date = value;
        }
    }

    public interface OnRichContentListener {
        /**
         * Called whenever rich content is received
         * NOTE: the listener is run on a background thread by default; see runListenerInBackground
         *
         * @param contentUri  the uri from the content provider
         * @param description the clip description associated with the uri
         */
        void onRichContent(Uri contentUri, ClipDescription description);
    }

    private OnRichContentListener onRichContentListener = null;
    private String[] mimeTypes = {};
    private static final String TAG = "RichContentEditText";
    /**
     * Determines whether the listener is run on a background thread or on the main thread
     * True (recommended): runs the listener on a background thread to improve performance
     * False (not advised): runs the listener on the main thread
     * NOTE: if this is true, ensure you update any necessary views on the main thread
     */
    public boolean runListenerInBackground = true;

    /**
     * Sets the listener that is called whenever rich content is received
     * NOTE: the listener is run on a background thread by default; see runListenerInBackground
     *
     * @param onRichContentListener the listener
     */
    public void setOnRichContentListener(OnRichContentListener onRichContentListener) {
        this.onRichContentListener = onRichContentListener;
    }

    /**
     * Sets the mime types to accept from a keyboard
     *
     * @param mimeTypes a string array of mime types
     */
    public void setContentMimeTypes(String[] mimeTypes) {
        if (mimeTypes != null) this.mimeTypes = mimeTypes;
    }

    /**
     * A convenience method for allowing image insertion from a keyboard
     * Sets the appropriate mime types to accept images
     */
    public void allowImageInsertion() {
        setContentMimeTypes(new String[]{"image/*", "image/png", "image/gif", "image/jpeg"});
    }

    /**
     * A convenience method for disallowing rich content from a keyboard
     */
    public void disallowRichContent() {
        setContentMimeTypes(new String[]{});
    }

    /**
     * @return the currently allowed mime types
     */
    public String[] getContentMimeTypes() {
        return mimeTypes;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        final InputConnection ic = super.onCreateInputConnection(editorInfo);
        EditorInfoCompat.setContentMimeTypes(editorInfo, getContentMimeTypes());
        return InputConnectionCompat.createWrapper(ic, editorInfo,
                (inputContentInfo, flags, opts) -> {
                    if (SDK_INT >= N_MR1 && (flags & InputConnectionCompat
                            .INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                        try {
                            if (onRichContentListener != null) {
                                Runnable runnable = () -> {
                                    inputContentInfo.requestPermission();
                                    onRichContentListener.onRichContent(
                                            inputContentInfo.getContentUri(),
                                            inputContentInfo.getDescription());
                                    inputContentInfo.releasePermission();
                                };
                                if (runListenerInBackground) new Thread(runnable).start();
                                else runnable.run();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error accepting rich content: " + e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    }
                    return true;
                });
    }

    // ****************************************** Listener Methods *******************************************

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        // if text is selected we ignore a loss of focus to prevent Android from terminating
        // text selection when one of the spinners opens (text size, color, bg color)
        if (!mUseRTFormatting || hasWindowFocus || !mTextSelected) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (mUseRTFormatting && mListener != null) {
            mListener.onFocusChanged(this, focused);
        }

    }

    @Override
    protected void onSelectionChanged(int start, int end) {
        if (mOldSelStart != start || mOldSelEnd != end) {
            mOldSelStart = start;
            mOldSelEnd = end;

            mTextSelected = (end > start);

            super.onSelectionChanged(start, end);

            if (mUseRTFormatting) {

                if (!mIsSaving && !mParagraphsAreUp2Date) {
                    mIgnoreParagraphChanges = true;
                    Effects.cleanupParagraphs(this);
                    mIgnoreParagraphChanges = false;
                    setParagraphsAreUp2Date(true);
                }

                if (mListener != null) {
                    mIsSelectionChanging = true;
                    mListener.onSelectionChanged(this, start, end);
                    mIsSelectionChanging = false;
                }

            }
        }
    }

    /**
     * Call this to have an effect applied to the current selection.
     * You get the Effect object via the static data members (e.g., RTEditText.BOLD).
     * The value for most effects is a Boolean, indicating whether to add or remove the effect.
     */
    public <V extends Object, C extends RTSpan<V>> void applyEffect(Effect<V, C> effect, V value) {
        if (mUseRTFormatting && !mIsSelectionChanging && !mIsSaving) {
            Spannable oldSpannable = mIgnoreTextChanges ? null : cloneSpannable();

            effect.applyToSelection(this, value);

            synchronized (this) {
                if (mListener != null && !mIgnoreTextChanges) {
                    Spannable newSpannable = cloneSpannable();
                    mListener.onTextChanged(this, oldSpannable, newSpannable, getSelectionStart(), getSelectionEnd(),
                                            getSelectionStart(), getSelectionEnd());
                }
                mLayoutChanged = true;
            }
        }
    }

    @Override
    /* LinkSpanListener */
    public void onClick(LinkSpan linkSpan) {
        if (mUseRTFormatting && mListener != null) {
            mListener.onClick(this, linkSpan);
        }
    }
    public int getCurrentCursorLine() {
        int selectionStartPos = this.getSelectionStart();

        if (selectionStartPos < 0) {
            // There is no selection, so return -1 like getSelectionStart() does when there is no seleciton.
            return -1;
        }


        String preSelectionStartText = this.getText().toString().substring(0, selectionStartPos);

        return preSelectionStartText.lastIndexOf('\n');
    }
}