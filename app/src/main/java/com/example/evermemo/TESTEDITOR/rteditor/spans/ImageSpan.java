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


import androidx.annotation.NonNull;

import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTImage;

/**
 * An ImageSpan representing an embedded image.
 */
public class ImageSpan extends MediaSpan {

    public ImageSpan(@NonNull RTImage image, boolean isSaved) {
        super(image, isSaved);
    }

    @NonNull
    public RTImage getImage() {
        return (RTImage) mMedia;
    }

}