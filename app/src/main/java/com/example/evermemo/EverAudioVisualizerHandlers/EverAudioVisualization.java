package com.example.evermemo.EverAudioVisualizerHandlers;


import androidx.annotation.NonNull;

/**
 * Audio visualization view interface
 */
public interface EverAudioVisualization {

    /**
     * Link view to custom implementation of {@link EverDbmHandler}.
     *
     * @param EverDbmHandler instance of EverDbmHandler
     */
    <T> void linkTo(@NonNull EverDbmHandler<T> EverDbmHandler);


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
