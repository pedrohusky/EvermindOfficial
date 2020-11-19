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
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTVideo;

import java.io.IOException;

public class AudioProcessor extends MediaProcessor {

    public interface AudioProcessorListener extends MediaProcessorListener {
        void onAudioProcessed(RTAudio audio);
    }

    public AudioProcessor(String originalFile, RTMediaFactory<RTImage, RTAudio, RTVideo> mediaFactory, AudioProcessorListener listener) {
        super(originalFile, mediaFactory, listener);
    }

    @Override
    protected void processMedia() throws Exception {
        // TODO
    }

}