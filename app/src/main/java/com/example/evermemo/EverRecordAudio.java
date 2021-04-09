package com.example.evermemo;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

public class EverRecordAudio {

    @NonNull
    public final String path;
    final MediaRecorder recorder = new MediaRecorder();
    final Context context;

    public EverRecordAudio(@NonNull String path, Context context) {
        this.path = sanitizePath(path);
        this.context = context;
    }

    @NonNull
    private String sanitizePath(@NonNull String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".3gp";
        }
        return path;
    }

    public void start() throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted.  It is " + state
                    + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        System.out.println("last directory is = " + directory.getPath());
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();
    }

    public void stop() {
        recorder.stop();
        recorder.release();
    }

    public void playarcoding() throws IOException {
        MediaPlayer mp = new MediaPlayer();
        mp.setDataSource(path);
        mp.prepare();
        mp.start();
        mp.setVolume(10, 10);
    }
}