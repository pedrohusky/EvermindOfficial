package com.example.Evermind.EverAudioVisualizerHandlers;

import java.util.ArrayList;
import java.util.List;

public class EverInterfaceHelper {

    public interface OnOpenAudioStateListener {
        void open();
        void close();
    }

    public interface OnChangeColorListener {
        void changeColor(int color);
    }

    public interface OnHideListener {
        void hide();
        void show();
    }

    private static EverInterfaceHelper mInstance;
    private final List<OnOpenAudioStateListener> audioStateListeners = new ArrayList<>();
    private final List<OnChangeColorListener> colorListeners = new ArrayList<>();
    private final List<OnHideListener> hideListeners = new ArrayList<>();
    private EverInterfaceHelper() {}

    public static EverInterfaceHelper getInstance() {
        if(mInstance == null) {
            mInstance = new EverInterfaceHelper();
        }
        return mInstance;
    }

    public void setListener(OnOpenAudioStateListener listener) {
        if (!audioStateListeners.contains(listener)) {
            audioStateListeners.add(listener);
        }
    }

    public void setHideListener(OnHideListener listener) {
        if (!hideListeners.contains(listener)) {
            hideListeners.add(listener);
        }
    }

    public void setColorListener(OnChangeColorListener listener) {
        if (!colorListeners.contains(listener)) {
            colorListeners.add(listener);
        }
    }

    public void removeColorListener(OnChangeColorListener listener) {
        colorListeners.remove(listener);
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

    public void hide() {
        for (OnHideListener listener: hideListeners) {
            listener.hide();
        }
    }

    public void show() {
        for (OnHideListener listener: hideListeners) {
            listener.show();
        }
    }


    public void clearListeners() {
        audioStateListeners.clear();
        colorListeners.clear();
    }
}
