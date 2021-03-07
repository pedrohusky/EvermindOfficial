package com.example.Evermind.EverAudioVisualizerHandlers;

import java.util.ArrayList;
import java.util.List;

public class EverInterfaceHelper {

    public interface OnOpenAudioStateListener {
        void open();
        void close();
    }

    public interface OnChangeColorListener {
        void changeAccentColor(int color);
    }

    public interface OnEnterDarkMode {
        void enterDarkMode(int color);
    }

    public interface OnHideListener {
        void hide();
        void show();
    }

    public interface OnCanPerformClickListener {
        void canPerformClick();
        void cantPerformClick();
    }

    private static EverInterfaceHelper mInstance;
    private final List<OnOpenAudioStateListener> audioStateListeners = new ArrayList<>();
    private final List<OnChangeColorListener> colorListeners = new ArrayList<>();
    private final List<OnEnterDarkMode> darkModeListeners = new ArrayList<>();
    private final List<OnHideListener> hideListeners = new ArrayList<>();
    private final List<OnCanPerformClickListener> clickListeners = new ArrayList<>();

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

    public void setDarkModeListeners(OnEnterDarkMode listener) {
        if (!darkModeListeners.contains(listener)) {
            darkModeListeners.add(listener);
        }
    }

    public void setClickListener(OnCanPerformClickListener listener) {
        if (!clickListeners.contains(listener)) {
            clickListeners.add(listener);
        }
    }

    public void setHideListener(OnHideListener listener) {
        if (!hideListeners.contains(listener)) {
            hideListeners.add(listener);
        }
    }

    public void setAccentColorListener(OnChangeColorListener listener) {
        if (!colorListeners.contains(listener)) {
            colorListeners.add(listener);
        }
    }

    public void removeColorListener(OnChangeColorListener listener) {
        colorListeners.remove(listener);
    }
    public void setCantClick() {
        for (OnCanPerformClickListener listener: clickListeners) {
           listener.cantPerformClick();
        }
    }

    public void setCanClick() {
        for (OnCanPerformClickListener listener: clickListeners) {
            listener.canPerformClick();
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

    public void changeAccentColor(int color) {
        for (OnChangeColorListener listener: colorListeners) {
          listener.changeAccentColor(color);
        }
    }

    public void enterDarkMode(int color) {
        for (OnEnterDarkMode listener: darkModeListeners) {
            listener.enterDarkMode(color);
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
        System.out.println(audioStateListeners.size() + " " + clickListeners.size() + " " + colorListeners.size() + " " + darkModeListeners.size() + " " + hideListeners.size());
        audioStateListeners.clear();
        clickListeners.clear();
        //colorListeners.clear();
        //darkModeListeners.clear();
      //  colorListeners.clear();
    }
}
