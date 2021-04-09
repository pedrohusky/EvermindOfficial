package com.example.evermemo.EverAudioVisualizerHandlers;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.evermemo.R;

/**
 * Wrapper for visualizer.
 */
class EverVisualizerWrapper {

    private static final long WAIT_UNTIL_HACK = 500;
    @NonNull
    private final Visualizer.OnDataCaptureListener captureListener;
    private final int captureRate;
    @Nullable
    private Visualizer visualizer;
    @Nullable
    private MediaPlayer mediaPlayer;
    private long lastZeroArrayTimestamp;

    public EverVisualizerWrapper(@NonNull Context context, int audioSessionId, @NonNull final EverVisualizerWrapper.OnFftDataCaptureListener onFftDataCaptureListener) {
        mediaPlayer = MediaPlayer.create(context, R.raw.av_workaround_1min);
        visualizer = new Visualizer(audioSessionId);
        visualizer.setEnabled(false);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        captureRate = Visualizer.getMaxCaptureRate();
        captureListener = new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, @NonNull byte[] fft, int samplingRate) {
                boolean allZero = EverAudioUtils.allElementsAreZero(fft);
                if (lastZeroArrayTimestamp == 0) {
                    if (allZero) {
                        lastZeroArrayTimestamp = System.currentTimeMillis();
                    }
                } else {
                    if (!allZero) {
                        lastZeroArrayTimestamp = 0;
                    } else if (System.currentTimeMillis() - lastZeroArrayTimestamp >= WAIT_UNTIL_HACK) {
                        setEnabled(true);
                        lastZeroArrayTimestamp = 0;
                    }
                }
                onFftDataCaptureListener.onFftDataCapture(fft);
            }
        };
        visualizer.setEnabled(true);
    }

    public void release() {
        visualizer.setEnabled(false);
        visualizer.release();
        visualizer = null;
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void setEnabled(final boolean enabled) {
        if (visualizer == null) return;
        visualizer.setEnabled(false);
        if (enabled) {
            visualizer.setDataCaptureListener(captureListener, captureRate, false, true);
        } else {
            visualizer.setDataCaptureListener(null, captureRate, false, false);
        }
        visualizer.setEnabled(true);
    }

    public interface OnFftDataCaptureListener {
        void onFftDataCapture(byte[] fft);
    }
}

