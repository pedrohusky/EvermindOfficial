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

package com.example.Evermind.TESTEDITOR.rteditor.media.choose.processor;

import com.example.Evermind.TESTEDITOR.rteditor.api.RTMediaFactory;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTAudio;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTImage;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTMediaSource;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTMediaType;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTVideo;

import java.io.IOException;
import java.io.InputStream;

public class ImageProcessor extends MediaProcessor {

    public interface ImageProcessorListener extends MediaProcessorListener {
        void onImageProcessed(RTImage image);
    }

    private final ImageProcessorListener mListener;

    public ImageProcessor(String originalFile, RTMediaFactory<RTImage, RTAudio, RTVideo> mediaFactory, ImageProcessorListener listener) {
        super(originalFile, mediaFactory, listener);
        mListener = listener;
    }

    @Override
    protected void processMedia() throws Exception {
        InputStream in = super.getInputStream();
        if (in == null) {
            if (mListener != null) {
                mListener.onError("No file found to process");
            }
        } else {
            RTMediaSource source = new RTMediaSource(RTMediaType.IMAGE, in, getOriginalFile(), getMimeType());
          //  RTImage image = mMediaFactory.create(source);
          //  if (image != null && mListener != null) {
          //      mListener.onImageProcessed(image);
          //  }
        }

    }

}