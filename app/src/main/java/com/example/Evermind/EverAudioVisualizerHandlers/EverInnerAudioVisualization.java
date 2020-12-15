package com.example.Evermind.EverAudioVisualizerHandlers;


import androidx.annotation.Nullable;

/**
 * Inner audio visualization interface.
 */
interface EverInnerAudioVisualization {

    /**
     * Start rendering of data.
     */
    void startRendering();

    /**
     * Stop rendering of data.
     */
    void stopRendering();

    /**
     * Set calm down listener.
     * @param calmDownListener calm down listener or null
     */
    void calmDownListener(@Nullable EverInnerAudioVisualization.CalmDownListener calmDownListener);

    /**
     * Called when data received.
     * @param dBmArray normalized dBm values for every layer
     * @param ampsArray amplitude values for every layer
     */
    void onDataReceived(float[] dBmArray, float[] ampsArray);

    /**
     * Listener that notifies about waves calm down.
     */
    interface CalmDownListener {

        /**
         * Called when all waves calm down.
         */
        void onCalmedDown();
    }
}

