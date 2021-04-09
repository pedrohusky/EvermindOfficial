package com.example.evermemo.EverAudioVisualizerHandlers;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.speech.SpeechRecognizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstract class for converting your data to dBm values.
 * When you're have new portion of data, call {@link #onDataReceived(Object)} method.
 */
public abstract class EverDbmHandler<TData> {

    private static final long UPDATE_INTERVAL = 16;
    private int layersCount;
    @Nullable
    private EverInnerAudioVisualization audioVisualization;
    @Nullable
    private float[] dBmArray;
    @Nullable
    private float[] ampsArray;
    private float[] emptyArray;
    private boolean released;
    @Nullable
    private Timer timer;

    void setUp(@NonNull EverInnerAudioVisualization audioVisualization, int layersCount) {
        this.audioVisualization = audioVisualization;
        this.layersCount = layersCount;
        this.dBmArray = new float[layersCount];
        this.ampsArray = new float[layersCount];
        this.emptyArray = new float[layersCount];
    }

    /**
     * Call this method when your data is available for conversion.
     *
     * @param data any data
     */
    public final void onDataReceived(TData data) {
        if (released)
            return;
        onDataReceivedImpl(data, layersCount, dBmArray, ampsArray);
        audioVisualization.onDataReceived(dBmArray, ampsArray);
        startRendering();
    }

    /**
     * Start rendering thread.
     */
    protected final void startRendering() {
        cancelTimer();
        audioVisualization.startRendering();
    }

    /**
     * Stop rendering thread.
     */
    protected final void stopRendering() {
        cancelTimer();
        audioVisualization.stopRendering();
    }

    /**
     * Post empty values to renderer and stop rendering thread after waves calm down.
     */
    protected final void calmDownAndStopRendering() {
        if (timer == null) {
            timer = new Timer("Stop Rendering Timer");
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (audioVisualization != null) {
                        audioVisualization.onDataReceived(emptyArray, emptyArray);
                    }
                }
            }, UPDATE_INTERVAL, UPDATE_INTERVAL);
        }
        audioVisualization.calmDownListener(new EverInnerAudioVisualization.CalmDownListener() {
            @Override
            public void onCalmedDown() {
                stopRendering();
            }
        });
    }

    /**
     * Cancel timer posting empty values.
     */
    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    /**
     * Called after {@link EverAudioVisualization#onResume()} call.
     */
    public void onResume() {

    }

    /**
     * Called after {@link EverAudioVisualization#onPause()} call.
     */
    public void onPause() {

    }

    /**
     * Called after {@link EverAudioVisualization#release()} ()} call.
     */
    public void release() {
        released = true;
        dBmArray = null;
        ampsArray = null;
        audioVisualization = null;
    }

    /**
     * Implement your own data conversion.
     *
     * @param data        any data
     * @param layersCount layers count
     * @param dBmArray    array of normalized (in range [0..1]) dBm values that should be populated by you.
     *                    Array size is equals to {@code layersCount} value.
     * @param ampsArray   array of amplitude values that should be populated by you.
     *                    Array size is equals to {@code layersCount} value.<br/><br/>
     *                    This values affect the appearance of bubbles. If new amplitude value is greater
     *                    than previous value and normalized dBm value is greater than 0.25, bubbles will appear on screen.
     *                    In case if amplitude is less than previous value, exponential smoothing (Holt - Winters)
     *                    used for smoothing amplitude values.
     */
    protected abstract void onDataReceivedImpl(TData data, int layersCount, float[] dBmArray, float[] ampsArray);

    public static class Factory {

        /**
         * Create new visualizer dBm handler.
         *
         * @param context        instance of context
         * @param audioSessionId audio session id
         * @return new visualizer dBm handler
         * @see Visualizer
         */
        @NonNull
        public static EverVisualizerDbmHandler newVisualizerHandler(@NonNull Context context, int audioSessionId) {
            return new EverVisualizerDbmHandler(context, audioSessionId);
        }

        /**
         * Create new visualizer dBm handler and wire with media player. At this point handler will set itself as
         * {@link MediaPlayer.OnPreparedListener} and {@link MediaPlayer.OnCompletionListener} of media player.
         *
         * @param context     instance of context
         * @param mediaPlayer instance of media player
         * @return new visualizer dBm handler
         * @see Visualizer,
         * @see EverVisualizerDbmHandler#setInnerOnPreparedListener(MediaPlayer.OnPreparedListener)
         * @see EverVisualizerDbmHandler#setInnerOnCompletionListener(MediaPlayer.OnCompletionListener)
         */
        @NonNull
        public static EverVisualizerDbmHandler newVisualizerHandler(@NonNull Context context, @NonNull MediaPlayer mediaPlayer) {
            return new EverVisualizerDbmHandler(context, mediaPlayer);
        }

        /**
         * Create new speech recognizer dBm handler. Default dBm values [min, max]:  [-2.12, 10.0].
         *
         * @param context instance of context
         * @return new speech recognizer dBm handler
         * @see SpeechRecognizer
         */
        @NonNull
        public static EverSpeechRecognizerDbmHandler newSpeechRecognizerHandler(@NonNull Context context) {
            return new EverSpeechRecognizerDbmHandler(context);
        }

        /**
         * Create new speech recognizer dBm handler.
         *
         * @param context       instance of context
         * @param minRmsDbValue minimum dBm value
         * @param maxRmsDbValue maximum dBm value
         * @return new speech recognizer dBm handler
         * @see SpeechRecognizer
         */
        @NonNull
        public static EverSpeechRecognizerDbmHandler newSpeechRecognizerDbmHandler(@NonNull Context context, float minRmsDbValue, float maxRmsDbValue) {
            return new EverSpeechRecognizerDbmHandler(context, minRmsDbValue, maxRmsDbValue);
        }
    }

}

