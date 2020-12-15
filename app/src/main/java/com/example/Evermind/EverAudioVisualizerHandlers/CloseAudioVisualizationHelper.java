package com.example.Evermind.EverAudioVisualizerHandlers;

import java.util.ArrayList;
import java.util.List;

public class CloseAudioVisualizationHelper {

    public interface OnOpenAudioStateListener {
        void open();
        void close();
    }

    public interface OnChangeColorListener {
        void changeColor(int color);
    }

    private static CloseAudioVisualizationHelper mInstance;
    private final List<OnOpenAudioStateListener> audioStateListeners = new ArrayList<>();
    private final List<OnChangeColorListener> colorListeners = new ArrayList<>();
    private CloseAudioVisualizationHelper() {}

    public static CloseAudioVisualizationHelper getInstance() {
        if(mInstance == null) {
            mInstance = new CloseAudioVisualizationHelper();
        }
        return mInstance;
    }

    public void setListener(OnOpenAudioStateListener listener) {
        if (!audioStateListeners.contains(listener)) {
            audioStateListeners.add(listener);
        }
    }

    public void setColorListener(OnChangeColorListener listener) {
        if (!colorListeners.contains(listener)) {
            colorListeners.add(listener);
        }
    }

    public void changeState(boolean state) {
        for (OnOpenAudioStateListener listener: audioStateListeners) {
            if (state) {
                listener.open();
            } else {
                listener.close();
            }
        }
    }

    public void changeColor(int color) {
        for (OnChangeColorListener listener: colorListeners) {
          listener.changeColor(color);
        }
    }


    public void clearListeners() {
        audioStateListeners.clear();
    }
}
