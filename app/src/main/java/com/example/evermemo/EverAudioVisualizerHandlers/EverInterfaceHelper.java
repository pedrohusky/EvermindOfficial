package com.example.evermemo.EverAudioVisualizerHandlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EverInterfaceHelper {

    private static EverInterfaceHelper mInstance;
    private final List<OnOpenAudioStateListener> audioStateListeners = new ArrayList<>();
    private final List<OnChangeColorListener> colorListeners = new ArrayList<>();
    private final List<OnEnterDarkMode> darkModeListeners = new ArrayList<>();
    private final List<OnHideListener> hideListeners = new ArrayList<>();
    private final List<OnCanPerformClickListener> clickListeners = new ArrayList<>();
    private final List<OnDownloadCompleted> downloadListeners = new ArrayList<>();

    public static EverInterfaceHelper getInstance() {
        if (mInstance == null) {
            mInstance = new EverInterfaceHelper();
        }
        return mInstance;
    }

    public void setListener(OnOpenAudioStateListener listener) {
        if (!audioStateListeners.contains(listener)) {
            audioStateListeners.add(listener);
        }
    }

    public void setDownloadListener(OnDownloadCompleted listener) {
        if (!downloadListeners.contains(listener)) {
            downloadListeners.add(listener);
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
        for (OnCanPerformClickListener listener : clickListeners) {
            listener.cantPerformClick();
        }
    }

    public void setCanClick() {
        for (OnCanPerformClickListener listener : clickListeners) {
            listener.canPerformClick();
        }
    }

    public void changeState(boolean state) {
        for (OnOpenAudioStateListener listener : audioStateListeners) {
            if (state) {
                listener.open();
            } else {
                listener.close();
            }
        }
    }

    public void changeAccentColor(int color) {
        for (OnChangeColorListener listener : colorListeners) {
            listener.changeAccentColor(color);
        }
    }

    public void enterDarkMode(int color, boolean isDarkMode) {
        for (OnEnterDarkMode listener : darkModeListeners) {
            if (listener != null) {
                listener.swichDarkMode(color, isDarkMode);
            }
        }
    }

    public void sendDownloadToListeners(File file, int p, String downloadKey) {
        for (OnDownloadCompleted listener : downloadListeners) {
            listener.downloadCompleted(file, p, downloadKey);
            //    downloadListeners.remove(listener);
        }
    }

    public void hide() {
        for (OnHideListener listener : hideListeners) {
            listener.hide();
        }
    }

    public void remove() {
        for (OnOpenAudioStateListener listener : audioStateListeners) {
            listener.remove();
        }
    }

    public void removeDarkModeListener(OnEnterDarkMode listener) {
        darkModeListeners.remove(listener);
    }

    public void show() {
        for (OnHideListener listener : hideListeners) {
            listener.show();
        }
    }

    public void clearListeners() {
        audioStateListeners.clear();
        clickListeners.clear();
        //colorListeners.clear();
    }

    public interface OnOpenAudioStateListener {
        void open();

        void close();

        void remove();
    }

    public interface OnDownloadCompleted {
        void downloadCompleted(File file, int p, String downloadKey);
    }

    public interface OnChangeColorListener {
        void changeAccentColor(int color);
    }

    public interface OnEnterDarkMode {
        void swichDarkMode(int color, boolean isDarkMode);
    }

    public interface OnHideListener {
        void hide();

        void show();
    }


    public interface OnCanPerformClickListener {
        void canPerformClick();

        void cantPerformClick();
    }
}
