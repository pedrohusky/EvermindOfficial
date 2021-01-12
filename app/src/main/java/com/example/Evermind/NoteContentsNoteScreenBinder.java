package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.RTTextView;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTText;
import com.example.Evermind.databinding.MainViewNotescreenRecyclerBinding;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.koushikdutta.ion.Ion;
import com.masoudss.lib.Utils;
import com.masoudss.lib.WaveGravity;

import java.io.File;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class NoteContentsNoteScreenBinder extends ItemBinder<EverLinkedMap, NoteContentsNoteScreenBinder.NoteScreenHolder> {

    private final Context context;

    public NoteContentsNoteScreenBinder(Context context) {
        this.context = context;
    }


    @Override
    public NoteScreenHolder createViewHolder(ViewGroup parent) {
        return new NoteScreenHolder(inflate(parent, R.layout.main_view_notescreen_recycler));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof EverLinkedMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bindViewHolder(NoteScreenHolder holder, EverLinkedMap everMap) {
        holder.setContentHTML();
        holder.setDrawContent();
        holder.setWaveSeek();
    }

    class NoteScreenHolder extends ItemViewHolder<EverLinkedMap> {

        private String lastTextInEditor = "null";
        private String lastDrawPath = "null";
        private String lastRecordPath = "null";
        private final MainViewNotescreenRecyclerBinding binding;

        @SuppressLint("ClickableViewAccessibility")
        public NoteScreenHolder(View itemView) {
            super(itemView);
            binding = MainViewNotescreenRecyclerBinding.bind(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML() {
            String itemContent = getItem().getContent();
            if (!lastTextInEditor.equals(itemContent)) {
                if (!itemContent.equals("▓")) {
                    if (itemContent.equals("<br>") || itemContent.equals("")) {
                        binding.recyclerEverEditor.setVisibility(View.GONE);
                        
                    } else {
                        //   new Handler(Looper.getMainLooper()).post(() -> {
                        if (lastTextInEditor.equals("null")) {
                            binding.recyclerEverEditor.setPadding(15, 15, 15, 15);
                        }
                        binding.recyclerEverEditor.setVisibility(View.VISIBLE);

                            if (getItem().getContent().length() <= 10) {
                                binding.recyclerEverEditor.setTextSize(22);
                            }
                            if (getItem().getContent().length() <= 5) {
                                binding.recyclerEverEditor.setTextSize(25);
                            }
                            if (getItem().getContent().length() >= 10) {
                                binding.recyclerEverEditor.setTextSize(18);
                            }
                        binding.recyclerEverEditor.setRichTextEditing(itemContent);
                        lastTextInEditor = itemContent;
                        //  });
                    }
                } else {
                    binding.recyclerEverEditor.setVisibility(View.GONE);
                }
            }
        }


        void setDrawContent() {
            String drawPath = getItem().getDrawLocation();
            if (!lastDrawPath.equals(drawPath)) {
                if (new File(drawPath).exists()) {

                    binding.recyclerImage.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeFile(drawPath);
                    ViewGroup.LayoutParams params = binding.recyclerImage.getLayoutParams();
                    params.height = bitmap.getHeight() / 2;
                    params.width = MATCH_PARENT;
                    binding.recyclerImage.setLayoutParams(params);
                    bitmap.recycle();
                    Glide.with(binding.recyclerImage).load(drawPath).into(binding.recyclerImage);
                    lastDrawPath = drawPath;
                } else {
                    binding.recyclerImage.setVisibility(View.GONE);
                }
            }
        }

        void setWaveSeek() {
            String recordPath = getItem().getRecord();
            if (!lastRecordPath.equals(recordPath)) {
                if (new File(recordPath).exists()) {

                    if (lastRecordPath.equals("null")) {
                        binding.everWave.setMMaxValue(100);
                        binding.everWave.setWaveGravity(WaveGravity.CENTER);
                    }

                    binding.cardEverNoteScreenPlayer.setVisibility(View.VISIBLE);
                    binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(getItem().getColor()));
                    binding.everWave.setSampleFrom(recordPath, true);
                    lastRecordPath = recordPath;
                } else {
                    binding.cardEverNoteScreenPlayer.setVisibility(View.GONE);
                }
            }
        }
    }
}