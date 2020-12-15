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

    private final TextView timerView;
    private final ImageButton restartView;
    private final ImageButton recordView;
    private final ImageButton playView;
    private final RelativeLayout relativeLayout;
    private final TextView statusView;
    private final WeakReference<MainActivity> mainActivity;
    private String filePath;
    private AudioSource source;
    private AudioChannel channel;
    private AudioSampleRate sampleRate;
    private MediaPlayer player;
    private Recorder recorder;
    private EverVisualizerHandler visualizerHandler;
    private Timer timer;
    private int recorderSecondsElapsed;
    private int playerSecondsElapsed;
    private boolean isRecording;
    private View added;
    private String ORIGINAL_PATH = "";
    private EverGLAudioVisualizationView visualizerView;

    public EverAudioHelper(Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
        statusView = mainActivity.get().findViewById(R.id.status);
        timerView = mainActivity.get().findViewById(R.id.audio_time);
        restartView = mainActivity.get().findViewById(R.id.restart);
        recordView = mainActivity.get().findViewById(R.id.record);
        playView = mainActivity.get().findViewById(R.id.play);
        ImageButton stopView = mainActivity.get().findViewById(R.id.stop);
        ImageButton saveView = mainActivity.get().findViewById(R.id.saveAudio);
        relativeLayout = mainActivity.get().findViewById(R.id.relativeAudio);
        PushDownAnim.setPushDownAnimTo(playView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
        PushDownAnim.setPushDownAnimTo(stopView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
        PushDownAnim.setPushDownAnimTo(saveView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
        //  togglePlaying(v);
        playView.setOnClickListener(this::toggleRecording);
        stopView.setOnClickListener(v -> stopRecording());
        saveView.setOnClickListener(v -> {
            stopRecording();
            IConvertCallback callback = new IConvertCallback() {
                @Override
                public void onSuccess(File convertedFile) {
                    Note_Model note = mainActivity.get().actualNote.get();
                    note.addRecord(convertedFile.getPath(), mainActivity.get());
                    mainActivity.get().CloseOrOpenAudioOptions(true);
                }

                @Override
                public void onFailure(Exception error) {
                    error.printStackTrace();
                }
            };
            AndroidAudioConverter.with(mainActivity.get())
                    .setFile(new File(filePath))
                    .setFormat(AudioFormat.MP3)
                    .setCallback(callback)
                    .convert();

        });


    }


    public void configureAudio(String filePath, AudioSource source, AudioChannel channel, AudioSampleRate sampleRate, int color) {
        this.filePath = filePath;
        ORIGINAL_PATH = filePath;
        this.source = source;
        this.channel = channel;
        this.sampleRate = sampleRate;
        visualizerView = new EverGLAudioVisualizationView.Builder(mainActivity.get())
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(800f)
                .setWavesFooterHeight(R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(Util.getDarkerColor(color))
                .setLayerColors(new int[]{color})
                .setIsTop(true)
                .build();
        if (added != null) {
            relativeLayout.removeView(added);
        }
        relativeLayout.addView(visualizerView, 0);
        added = visualizerView;
        visualizerView.onPause();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mainActivity.get().CloseOrOpenAudioOptions(false);
            new Handler(Looper.getMainLooper()).postDelayed(() -> visualizerView.onResume(), 500);
        }, 250);


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
        stopPlaying();
        if (isRecording) {
            pauseRecording();
        } else {
            resumeRecording();
        }
    }

    public void togglePlaying(View v) {
        pauseRecording();
        if (isPlaying()) {
            stopPlaying();
        } else {
            startPlaying();
        }
    }

    public void restartRecording(View v) {
        if (isRecording) {
            stopRecording();
        } else if (isPlaying()) {
            stopPlaying();
        } else {
            visualizerHandler = new EverVisualizerHandler();
            visualizerView.linkTo(visualizerHandler);
            visualizerView.release();
            if (visualizerHandler != null) {
                visualizerHandler.stop();
            }
        }
        statusView.setVisibility(View.INVISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
        timerView.setText("00:00:00");
        recorderSecondsElapsed = 0;
        playerSecondsElapsed = 0;
    }

    private void resumeRecording() {
        isRecording = true;
//            statusView.setText(R.string.aar_recording);
        //    statusView.setVisibility(View.VISIBLE);
        //    restartView.setVisibility(View.INVISIBLE);
        //playView.setVisibility(View.INVISIBLE);
//            recordView.setImageResource(R.drawable.aar_ic_pause);
        playView.setImageResource(R.drawable.aar_ic_pause);

        visualizerHandler = new EverVisualizerHandler();
        visualizerView.linkTo(visualizerHandler);
//            player.setOnCompletionListener(this);
        if (recorder == null) {
            timerView.setText("00:00:00");

            filePath = ORIGINAL_PATH + "record_" + System.currentTimeMillis() + ".wav";
            recorder = OmRecorder.wav(
                    new PullTransport.Default(Util.getMic(source, channel, sampleRate), this),
                    new File(filePath));
        }
        recorder.resumeRecording();

        startTimer();
    }

    private void pauseRecording() {
        isRecording = false;
//            statusView.setText(R.string.aar_paused);
//            statusView.setVisibility(View.VISIBLE);
        //   restartView.setVisibility(View.VISIBLE);
        playView.setVisibility(View.VISIBLE);
//            recordView.setImageResource(R.drawable.aar_ic_rec);
        playView.setImageResource(R.drawable.aar_ic_play);

        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if (recorder != null) {
            recorder.pauseRecording();
        }

        stopTimer();
    }

    private void stopRecording() {
        visualizerView.release();
        //TODO FIX THE RECORDED AUDIo BEING SAVED
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        recorderSecondsElapsed = 0;
        if (recorder != null) {
            recorder.stopRecording();
            recorder = null;
        }

        stopTimer();
    }

    private void startPlaying() {
        try {
            stopRecording();
            player = new MediaPlayer();
            player.setDataSource(filePath);
            player.prepare();
            player.start();


            //  visualizerView.linkTo(EverDbmHandler.Factory.newVisualizerHandler(mainActivity.get(), player));
            visualizerView.post(() -> player.setOnCompletionListener(EverAudioHelper.this));

            timerView.setText("00:00:00");
            statusView.setText(R.string.aar_playing);
            statusView.setVisibility(View.VISIBLE);
            playView.setImageResource(R.drawable.aar_ic_stop);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
//            statusView.setText("");
        //        statusView.setVisibility(View.INVISIBLE);
        playView.setImageResource(R.drawable.aar_ic_play);

        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if (player != null) {
            try {
                player.stop();
                player.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        stopTimer();
    }

    private boolean isPlaying() {
        try {
            return player != null && player.isPlaying() && !isRecording;
        } catch (Exception e) {
            return false;
        }
    }

    private void startTimer() {
        stopTimer();
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
            } else if (isPlaying()) {
                playerSecondsElapsed++;
                timerView.setText(Util.formatSeconds(playerSecondsElapsed));
            }
        });
    }

    public void stop(boolean close) {
        visualizerView.onPause();
        //visualizerView.stopRendering();
        mainActivity.get().CloseOrOpenAudioOptions(close);
    }
}
