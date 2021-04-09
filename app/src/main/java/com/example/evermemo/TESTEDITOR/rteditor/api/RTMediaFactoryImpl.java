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

package com.example.evermemo.TESTEDITOR.rteditor.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTAudio;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTAudioImpl;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTImage;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTImageImpl;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTMediaSource;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTMediaType;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTVideo;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTVideoImpl;
import com.example.evermemo.TESTEDITOR.rteditor.media.MediaUtils;
import com.example.evermemo.TESTEDITOR.rteditor.utils.Helper;
import com.example.evermemo.TESTEDITOR.rteditor.utils.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is a basic implementation of the RTMediaFactory using either the
 * internal (as in Context.context.getFilesDir() or the primary external
 * file system (as in Context.getExternalFilesDir(String).
 */
public class RTMediaFactoryImpl implements RTMediaFactory<RTImage, RTAudio, RTVideo> {
    private static final long serialVersionUID = 6970361368051595063L;

    private final File mStoragePath;

    public RTMediaFactoryImpl(@NonNull Context context) {
        this(context, true);    // use external storage as default
    }

    public RTMediaFactoryImpl(@NonNull Context context, boolean externalStorage) {
        mStoragePath = externalStorage ?
                context.getExternalFilesDir(null) :
                context.getFilesDir();
    }

    /**
     * Returns the absolute file path for a certain RTMediaType.
     * <p>
     * The media type specific path as provided by RTMediaType is appended to
     * the storage path (e.g. <storage area>/images for image files).
     */
    @NonNull
    protected String getAbsolutePath(@NonNull RTMediaType mediaType) {
        File mediaPath = new File(mStoragePath.getAbsolutePath(), mediaType.mediaPath());
        if (!mediaPath.exists()) {
            mediaPath.mkdirs();
        }
        return mediaPath.getAbsolutePath();
    }

    /*
     * Use case 1: Inserting media objects into the rich text editor.
     *
     * This default implementation copies all files into the dedicated media
     * storage area.
     */

    @Nullable
    @Override
    /* @inheritDoc */
    public RTImage createImage(@NonNull RTMediaSource mediaSource) {
        File targetFile = loadMedia(mediaSource);
        return targetFile == null ? null :
                new RTImageImpl(targetFile.getAbsolutePath());
    }

    @Nullable
    @Override
    /* @inheritDoc */
    public RTAudio createAudio(@NonNull RTMediaSource mediaSource) {
        File targetFile = loadMedia(mediaSource);
        return targetFile == null ? null :
                new RTAudioImpl(targetFile.getAbsolutePath());
    }

    @Nullable
    @Override
    /* @inheritDoc */
    public RTVideo createVideo(@NonNull RTMediaSource mediaSource) {
        File targetFile = loadMedia(mediaSource);
        return targetFile == null ? null :
                new RTVideoImpl(targetFile.getAbsolutePath());
    }

    @NonNull
    private File loadMedia(@NonNull RTMediaSource mediaSource) {
        File targetPath = new File(getAbsolutePath(mediaSource.getMediaType()));
        File targetFile = MediaUtils.createUniqueFile(targetPath,
                mediaSource.getName(),
                mediaSource.getMimeType(),
                false);

        copyFile(mediaSource.getInputStream(), targetFile);

        return targetFile;
    }

    private void copyFile(InputStream in, File targetFile) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(targetFile);
            IOUtils.copy(in, out);
        } catch (IOException ioe) {
            Log.e(getClass().getSimpleName(), ioe.getMessage(), ioe);
        } finally {
            Helper.closeQuietly(out);
            Helper.closeQuietly(in);
        }
    }

    /*
     * Use case 2: Load a rich text with referenced media objects into the rich
     * text editor.
     *
     * This default implementation doesn't apply any transformations to the path
     * because the files are stored in the file system where they can be
     * accessed directly by the rich text editor (via ImageSpan).
     */

    @NonNull
    @Override
    /* @inheritDoc */
    public RTImage createImage(String path) {
        return new RTImageImpl(path);
    }

    @NonNull
    @Override
    /* @inheritDoc */
    public RTAudio createAudio(String path) {
        return new RTAudioImpl(path);
    }

    @NonNull
    @Override
    /* @inheritDoc */
    public RTVideo createVideo(String path) {
        return new RTVideoImpl(path);
    }

}