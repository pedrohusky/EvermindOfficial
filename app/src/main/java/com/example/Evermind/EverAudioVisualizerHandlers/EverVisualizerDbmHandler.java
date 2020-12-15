package com.example.Evermind.EverAudioVisualizerHandlers;


import android.content.Context;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.cleveroad.audiovisualization.DbmHandler;

/**
 * DbmHandler implementation for visualizer.
 */
public class EverVisualizerDbmHandler extends EverDbmHandler<byte[]> implements EverVisualizerWrapper.OnFftDataCaptureListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    /**
     * Maximum value of dB. Used for controlling wave height percentage.
     */
    private static final float MAX_DB_VALUE = 76;

    private final EverVisualizerWrapper visualizerWrapper;
    private float[] dbs;
    private float[] allAmps;
    private MediaPlayer.OnPreparedListener innerOnPreparedListener;
    private MediaPlayer.OnCompletionListener innerOnCompletionListener;
    private final float[] coefficients = new float[] {
            80 / 44100f,
            350 / 44100f,
            2500 / 44100f,
            10000 / 44100f,
    };

    EverVisualizerDbmHandler(@NonNull Context context, int audioSession) {
        visualizerWrapper = new EverVisualizerWrapper(context, audioSession, this);
    }

    EverVisualizerDbmHandler(@NonNull Context context, @NonNull MediaPlayer mediaPlayer) {
        this(context, mediaPlayer.getAudioSessionId());
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    protected void onDataReceivedImpl(byte[] fft, int layersCount, float[] dBmArray, float[] ampArray) {
        // calculate dBs and amplitudes
        int dataSize = fft.length / 2 - 1;
        if (dbs == null || dbs.length != dataSize) {
            dbs = new float[dataSize];
        }
        if (allAmps == null || allAmps.length != dataSize) {
            allAmps = new float[dataSize];
        }
        for (int i = 0; i < dataSize; i++) {
            float re = fft[2 * i];
            float im = fft[2 * i + 1];
            float sqMag = re * re + im * im;
            dbs[i] = EverAudioUtils.magnitudeToDb(sqMag);
            float k = 1;
            if (i == 0 || i == dataSize - 1) {
                k = 2;
            }
            allAmps[i] = (float) (k * Math.sqrt(sqMag) / dataSize);
        }
        for (int i = 0; i < layersCount; i++) {
            int index = (int) (coefficients[i] * fft.length);
            float db = dbs[index];
            float amp = allAmps[index];
            dBmArray[i] = db / MAX_DB_VALUE;
            ampArray[i] = amp;
        }
    }

    @Override
    public void onFftDataCapture(byte[] fft) {
        onDataReceived(fft);
    }

    @Override
    public void onResume() {
        super.onResume();
        visualizerWrapper.setEnabled(true);
    }

    @Override
    public void onPause() {
        visualizerWrapper.setEnabled(false);
        super.onPause();
    }

    @Override
    public void release() {
        super.release();
        visualizerWrapper.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        calmDownAndStopRendering();
        visualizerWrapper.setEnabled(false);
        if (innerOnCompletionListener != null) {
            innerOnCompletionListener.onCompletion(mp);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        startRendering();
        visualizerWrapper.setEnabled(true);
        if (innerOnPreparedListener != null) {
            innerOnPreparedListener.onPrepared(mp);
        }
    }

    public void setInnerOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.innerOnPreparedListener = onPreparedListener;
    }

    public void setInnerOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        this.innerOnCompletionListener = onCompletionListener;
    }
}

