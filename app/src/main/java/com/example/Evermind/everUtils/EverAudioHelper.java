package com.example.Evermind.everUtils;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Evermind.EverAudioVisualizerHandlers.EverGLAudioVisualizationView;
import com.example.Evermind.EverAudioVisualizerHandlers.EverVisualizerHandler;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cafe.adriel.androidaudiorecorder.Util;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;

public class EverAudioHelper implements PullTransport.OnAudioChunkPulledListener, MediaPlayer.OnCompletionListener {

    @NonNull
    private final WeakReference<MainActivity> mainActivity;
    @NonNull
    private final EverVisualizerHandler visualizerHandler;
    private TextView timerView;
    private ImageButton playView;
    private String filePath;
    private AudioSource source;
    private AudioChannel channel;
    private AudioSampleRate sampleRate;
    private Recorder recorder;
    private Timer timer;
    private int recorderSecondsElapsed;

    public boolean isRecording() {
        return isRecording;
    }

    public boolean hasRecordStarted() {
        return recorderSecondsElapsed > 0;
    }

    private boolean isRecording;
    private String ORIGINAL_PATH = "";
    private final String restartTimer = "00:00:00";
    private EverGLAudioVisualizationView visualizerView;
    private final List<Float> amplitudes = new ArrayList<>();

    public EverAudioHelper(Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
        visualizerHandler = new EverVisualizerHandler();
        visualizerView = mainActivity.get().getButtonsBinding().everGLAudioVisualizationView;
        visualizerView.linkTo(visualizerHandler);
        mainActivity.get().getHandler().postDelayed(() -> {
            ImageButton stopView = mainActivity.get().getEverViewManagement().getStopView();
            ImageButton saveView = mainActivity.get().getEverViewManagement().getSaveView();
            visualizerView = mainActivity.get().getButtonsBinding().everGLAudioVisualizationView;
            timerView = mainActivity.get().getButtonsBinding().audioTime;
            playView = mainActivity.get().getButtonsBinding().play;
            PushDownAnim.setPushDownAnimTo(playView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(stopView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(saveView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            //  togglePlaying(v);
            playView.setOnClickListener(this::toggleRecording);

            stopView.setOnClickListener(v -> stopRecording());

            saveView.setOnClickListener(v -> {
                stop(true);

                    v.postDelayed(() -> {
                        mainActivity.get().runOnUiThread(() -> {
                        mainActivity.get().getActualNote().addEverLinkedMap(this.filePath + "<AMP>" + amplitudes.toString().replaceAll("[\\[\\](){}]", ""), mainActivity.get(), false);

                        });
                    }, 350);
            });

        }, 250);

    }


    public void configureAudio(String filePath, AudioSource source, AudioChannel channel, AudioSampleRate sampleRate, int color) {
        this.filePath = filePath;
        ORIGINAL_PATH = filePath;
        this.source = source;
        this.channel = channel;
        this.sampleRate = sampleRate;
        int ifColor;
        if (color == -1) {
            ifColor = Util.getDarkerColor(color);
        } else {
            ifColor = color;
        }
      //  visualizerView.setBackgroundColor(ifColor);
        visualizerView.updateColor(new EverGLAudioVisualizationView.ColorsBuilder<>(mainActivity.get()).setBackgroundColor(ifColor).setLayerColors(new int[]{Color.WHITE}));
        mainActivity.get().getEverViewManagement().switchBottomBars("audio");


        visualizerView.onPause();
        visualizerView.postDelayed(() -> {
            visualizerView.onResume();

        }, 250);
        // });


    }

    @Override
    public void onAudioChunkPulled(@NonNull AudioChunk audioChunk) {
        float amplitude = isRecording ? (float) audioChunk.maxAmplitude() : 0f;
        visualizerHandler.onDataReceived(amplitude);
        amplitudes.add(amplitude);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlaying();
    }

    public void toggleRecording(View v) {
        if (isRecording) {
            isRecording = false;
            playView.setImageResource(R.drawable.aar_ic_play);
            if (recorder != null) {
                recorder.pauseRecording();
            }
            stopTimer();
        } else {
            isRecording = true;
            playView.setImageResource(R.drawable.aar_ic_pause);

            visualizerView.onResume();
            if (recorder == null) {
                timerView.setText(restartTimer);
                amplitudes.clear();

                filePath = ORIGINAL_PATH + "record_" + System.currentTimeMillis() + ".wav";
                recorder = OmRecorder.wav(
                        new PullTransport.Default(Util.getMic(source, channel, sampleRate), this),
                        new File(filePath));
            }
            recorder.resumeRecording();
            startTimer();
        }
      //  toggleTimer();
    }

    private void stopPlaying() {
        isRecording = false;
        playView.setImageResource(R.drawable.aar_ic_play);
        stopTimer();
        timerView.setText(restartTimer);
    }

    private void stopRecording() {
        recorderSecondsElapsed = 0;
        if (recorder != null) {
            recorder.stopRecording();
            recorder = null;
            playView.setImageResource(R.drawable.aar_ic_play);
            isRecording = false;
        }
        stopTimer();
        timerView.setText(restartTimer);
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void updateTimer() {
        mainActivity.get().runOnUiThread(() -> {
            if (isRecording) {
                recorderSecondsElapsed++;
                timerView.setText(Util.formatSeconds(recorderSecondsElapsed));
            }
        });
    }

    public void stop(boolean close) {
        visualizerView.onPause();
        stopRecording();
        mainActivity.get().getEverViewManagement().switchBottomBars("audio");
    }
}
