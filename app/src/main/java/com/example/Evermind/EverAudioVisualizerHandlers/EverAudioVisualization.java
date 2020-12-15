package com.example.Evermind.EverAudioVisualizerHandlers;


import androidx.annotation.NonNull;

/**
 * Audio visualization view interface
 */
public interface EverAudioVisualization {

    /**
     * Link view to custom implementation of {@link EverDbmHandler}.
     * @param dbmHandler instance of DbmHandler
     */
    <T> void linkTo(@NonNull EverDbmHandler<T> dbmHandler);

    /**
     * Resume audio visualization.
     */
    void onResume();

    /**
     * Pause audio visualization.
     */
    void onPause();

    /**
     * Release resources of audio visualization.
     */
    void release();
}
