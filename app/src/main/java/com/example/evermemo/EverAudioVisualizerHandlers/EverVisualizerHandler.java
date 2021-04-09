package com.example.evermemo.EverAudioVisualizerHandlers;


public class EverVisualizerHandler extends EverDbmHandler<Float> {
    public EverVisualizerHandler() {
    }

    protected void onDataReceivedImpl(Float amplitude, int layersCount, float[] dBmArray, float[] ampsArray) {
        amplitude = amplitude / 100.0F;
        if ((double) amplitude <= 0.5D) {
            amplitude = 0.0F;
        } else if ((double) amplitude > 0.5D && (double) amplitude <= 0.6D) {
            amplitude = 0.2F;
        } else if ((double) amplitude > 0.6D && (double) amplitude <= 0.7D) {
            amplitude = 0.6F;
        } else if ((double) amplitude > 0.7D) {
            amplitude = 1.0F;
        }

        try {
            dBmArray[0] = amplitude;
            ampsArray[0] = amplitude;
        } catch (Exception var6) {
        }

    }

    public void stop() {
        try {
            this.calmDownAndStopRendering();
        } catch (Exception var2) {
        }

    }
}

