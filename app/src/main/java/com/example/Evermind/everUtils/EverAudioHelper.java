package com.example.Evermind.everUtils;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.Evermind.EverAudioVisualizerHandlers.EverGLAudioVisualizationView;
import com.example.Evermind.EverAudioVisualizerHandlers.EverVisualizerHandler;
import com.example.Evermind.MainActivity;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import cafe.adriel.androidaudiorecorder.Util;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;

public class EverAudioHelper implements PullTransport.OnAudioChunkPulledListener, MediaPlayer.OnCompletionListener {

    private final WeakReference<MainActivity> mainActivity;
    private final EverVisualizerHandler visualizerHandler;
    private TextView timerView;
    private ImageButton playView;
    private RelativeLayout relativeLayout;
    private String filePath;
    private AudioSource source;
    private AudioChannel channel;
    private AudioSampleRate sampleRate;
    private Recorder recorder;
    private Timer timer;
    private int recorderSecondsElapsed;
    private boolean isRecording;
    private View added;
    private String ORIGINAL_PATH = "";
    private final String restartTimer = "00:00:00";
    private EverGLAudioVisualizationView visualizerView;

    public EverAudioHelper(Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
        visualizerHandler = new EverVisualizerHandler();
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
        visualizerView = new EverGLAudioVisualizationView.Builder(mainActivity.get())
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(1300f)
                .setWavesFooterHeight(R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(ifColor)
                .setLayerColors(new int[]{mainActivity.get().getColor(R.color.White)})
                .setIsTop(true)
                .build();
        mainActivity.get().getEverViewManagement().CloseOrOpenAudioOptions(false);

        relativeLayout = mainActivity.get().getEverViewManagement().getAudioOptions().findViewById(R.id.relativeAudio);
        timerView = mainActivity.get().getEverViewManagement().getTimerView();
        playView = mainActivity.get().getEverViewManagement().getPlayView();

        if (added != null) {
            relativeLayout.removeView(added);
        }
        relativeLayout.addView(visualizerView, 0);
        added = visualizerView;
        visualizerView.linkTo(visualizerHandler);
        visualizerView.onPause();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            visualizerView.onResume();
            ImageButton stopView = mainActivity.get().getEverViewManagement().getStopView();
            ImageButton saveView = mainActivity.get().getEverViewManagement().getSaveView();
            PushDownAnim.setPushDownAnimTo(playView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(stopView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(saveView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            //  togglePlaying(v);
            playView.setOnClickListener(this::toggleRecording);
            stopView.setOnClickListener(v -> stopRecording());
            saveView.setOnClickListener(v -> {
                stopRecording();

                new Thread(() -> AndroidAudioConverter.with(mainActivity.get())
                        .setFile(new File(this.filePath))
                        .setFormat(AudioFormat.MP3)
                        .setCallback(new IConvertCallback() {
                            @Override
                            public void onSuccess(File convertedFile) {
                               mainActivity.get().runOnUiThread(() -> {
                                   mainActivity.get().getActualNote().addEverLinkedMap(convertedFile.getPath(), mainActivity.get(), false);
                                   stop(true);
                               });
                            }
                            @Override
                            public void onFailure(Exception error) {
                                error.printStackTrace();
                            }
                        })
                        .convert()).start();

            });
        }, 350);
        // });


    }

    @Override
    public void onAudioChunkPulled(AudioChunk audioChunk) {
        float amplitude = isRecording ? (float) audioChunk.maxAmplitude() : 0f;
        visualizerHandler.onDataReceived(amplitude);
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
        mainActivity.get().getEverViewManagement().CloseOrOpenAudioOptions(close);
    }
}
