package com.example.Evermind;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.RTTextView;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.masoudss.lib.WaveformSeekBar;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MoldableView extends View implements EverInterfaceHelper.OnDownloadCompleted {
    private EverLinkedMap everLinkedMap;
    private EverWave audioWave;
    private LinearLayout linearLayout;
    private CardView card_everNoteScreenPlayer;
    private RTTextView textContent;
    private ImageView drawContent;
    private String downloadKey;
    private int position;
    private File downloadFile;
    private WeakReference<MainActivity> mainActivity;

    public MoldableView(Context context) {
        super(context);
    }

    public MoldableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoldableView(Context context, EverLinkedMap everLinkedMap, LinearLayout linearLayout) {
        super(context);
        mainActivity = new WeakReference<>(((MainActivity)context));
        this.linearLayout = linearLayout;
        this.everLinkedMap = everLinkedMap;
        if (fileExists(everLinkedMap.getDrawLocation(), 0) || fileExists(everLinkedMap.getRecord(), 1))
            initialize(linearLayout);
    }

    private int getViewType() {
        return everLinkedMap.getType();
    }

    private boolean fileExists(String path, int type) {

            File tempFile = new File(path);
            if (!tempFile.exists()) {
                EverInterfaceHelper.getInstance().setDownloadListener(this);
                downloadKey = tempFile.getName();
                switch (type) {
                    case 0:
                        mainActivity.get().getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 0, position);
                        break;
                    case 1:
                        mainActivity.get().getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 1, position);
                        break;
                }
            }
            return tempFile.exists();

    }

    private void initialize(LinearLayout linearLayout) {
        switch (getViewType()) {
            case 1:
                if (!everLinkedMap.getContent().equals("▓")) {
                    textContent = new RTTextView(this.getContext());
                    textContent.setTextSize(20);
                    textContent.setRichTextEditing(everLinkedMap.getContent());
                    linearLayout.addView(textContent);
                } else {
                    this.setVisibility(GONE);
                }

                break;

            case 2:
                if (!everLinkedMap.getDrawLocation().equals("▓") || !everLinkedMap.getDrawLocation().equals("")) {
                    drawContent = new ImageView(this.getContext());
                    String path = everLinkedMap.getDrawLocation();
                    String[] split = path.split("<>");
                    File f = new File(path);
                    if (downloadFile != null) {
                        f = downloadFile;
                    }
                    int h = (Integer.parseInt(split[1]) / 2);
                    int w = (Integer.parseInt(split[2]) / 2);
                    Glide.with(drawContent).load(f).transition(DrawableTransitionOptions.withCrossFade()).encodeQuality(25).override(w,h).into(drawContent);
                    linearLayout.addView(drawContent);
                } else {
                    this.setVisibility(GONE);
                }

                break;

            case 3:
                if (!everLinkedMap.getRecord().equals("▓")) {
                    audioWave = new EverWave(this.getContext());
                    String[] s = everLinkedMap.getRecord().split("<AMP>");
                    card_everNoteScreenPlayer = new CardView(this.getContext());
                    card_everNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(everLinkedMap.getColor()));
                    card_everNoteScreenPlayer.addView(audioWave);

                    List<Integer> amplitude = new ArrayList<>();
                    for (String audioData : s[1].split(",")) {
                        amplitude.add(Math.round(Float.parseFloat(audioData)));
                    }

                    audioWave.setSample(amplitude);

                    linearLayout.addView(card_everNoteScreenPlayer);
                } else {
                    this.setVisibility(GONE);
                }
                break;
        }
    }

    @Override
    public void downloadCompleted(File file, int p, String downloadKey) {
        if (downloadKey.equals(this.downloadKey)) {
            downloadFile = file;
            initialize(linearLayout);
        }
    }
}
