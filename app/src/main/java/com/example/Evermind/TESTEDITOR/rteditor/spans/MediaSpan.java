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

package com.example.Evermind.TESTEDITOR.rteditor.spans;


import androidx.annotation.NonNull;

import com.example.Evermind.TESTEDITOR.rteditor.api.RTApi;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTFormat;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTMedia;
import com.example.Evermind.TESTEDITOR.rteditor.media.MediaUtils;

public abstract class MediaSpan extends android.text.style.ImageSpan {

    @NonNull
    final protected RTMedia mMedia;

    // when saving the text delete the Media if the MediaSpan was removed from the text
    // when dismissing the text delete the Media if the MediaSpan was removed from the text and if the Media wasn't saved
    final private boolean mIsSaved;

    public MediaSpan(@NonNull RTMedia media, boolean isSaved) {
        super(RTApi.getApplicationContext(), MediaUtils.createFileUri(media.getFilePath(RTFormat.SPANNED)));
        mMedia = media;
        mIsSaved = isSaved;
    }

    @NonNull
    public RTMedia getMedia() {
        return mMedia;
    }

    public boolean isSaved() {
        return mIsSaved;
    }

}