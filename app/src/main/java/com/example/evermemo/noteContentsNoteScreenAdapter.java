package com.example.evermemo;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.evermemo.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.evermemo.databinding.DrawViewNotescreenRecyclerBinding;
import com.example.evermemo.databinding.RecordViewNotescreenRecyclerBinding;
import com.example.evermemo.databinding.TextViewNotescreenRecyclerBinding;
import com.example.evermemo.everUtils.EverLinkedDiffUtil;
import com.example.evermemo.recycler_models.EverLinkedMap;
import com.github.florent37.viewanimator.AnimationBuilder;
import com.github.florent37.viewanimator.ViewAnimator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidaudiorecorder.Util;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class noteContentsNoteScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<EverLinkedMap> noteData = new ArrayList<>();
    private int color;

    public noteContentsNoteScreenAdapter(List<EverLinkedMap> noteData, int color) {
        this.noteData.addAll(noteData);
        this.color = color;
    }

    @Override
    public int getItemViewType(int position) {
        return noteData.get(position).getType();
    }



    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = null;
        switch (viewType) {

            case 1:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.text_view_notescreen_recycler, viewGroup, false);
                break;
            case 2:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.draw_view_notescreen_recycler, viewGroup, false);
                break;
            case 3:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.record_view_notescreen_recycler, viewGroup, false);
                break;
        }
        if (viewType == 1) {
            return new noteScreenTextViewHolder(view);
        } else if (viewType == 2) {
            return new noteScreenDrawViewHolder(view);
        } else {
            return new noteScreenRecordViewHolder(view);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        switch (getItemViewType(position)) {
            case 1:
                noteScreenTextViewHolder textHolder = ((noteScreenTextViewHolder) viewHolder);
                textHolder.everLinkedMap = noteData.get(position);
                textHolder.setContentHTML();
                break;
            case 2:
                noteScreenDrawViewHolder drawHolder = ((noteScreenDrawViewHolder) viewHolder);
                drawHolder.everLinkedMap = noteData.get(position);
                drawHolder.setDrawContent();
                break;
            case 3:
                noteScreenRecordViewHolder recordHolder = ((noteScreenRecordViewHolder) viewHolder);
                recordHolder.everLinkedMap = noteData.get(position);
                recordHolder.color = color;
                recordHolder.setWaveSeek();
                break;
        }

    }

    public void updateList(List<EverLinkedMap> everLinkedMaps, int color) {
          final EverLinkedDiffUtil diffCallback = new EverLinkedDiffUtil(this.noteData, everLinkedMaps);
           final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

           System.out.println("Old dat = " + this.noteData.toString());
           System.out.println("NEw data = " + everLinkedMaps.toString());
        this.color = color;
        this.noteData.clear();
        this.noteData.addAll(everLinkedMaps);
           diffResult.dispatchUpdatesTo(this);
    }

    public List<EverLinkedMap> getList() {
        return this.noteData;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return noteData.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class noteScreenTextViewHolder extends RecyclerView.ViewHolder implements EverInterfaceHelper.OnEnterDarkMode {
        @NonNull
        private final TextViewNotescreenRecyclerBinding binding;
        private EverLinkedMap everLinkedMap;

        @SuppressLint("ClickableViewAccessibility")
        public noteScreenTextViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            binding = TextViewNotescreenRecyclerBinding.bind(view);
                binding.recyclerEverEditor.setPadding(15, 15, 15, 15);
                binding.recyclerEverEditor.setTextSize(18);

            if (((MainActivity) view.getContext()).getEverThemeHelper().isDarkMode()) {
                binding.recyclerEverEditor.setTextColor(itemView.getContext().getColor(R.color.White));
            } else {
                binding.recyclerEverEditor.setTextColor(itemView.getContext().getColor(R.color.NightBlack));
            }

            EverInterfaceHelper.getInstance().setDarkModeListeners(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML() {
            String itemContent = everLinkedMap.getContent();
                if (!itemContent.equals("▓") && !itemContent.equals("<br>") && !itemContent.equals("")) {
                    binding.recyclerEverEditor.setVisibility(View.VISIBLE);
                    binding.recyclerEverEditor.setRichTextEditing(itemContent);
                } else {
                    binding.recyclerEverEditor.setVisibility(View.GONE);
                }
        }

        @Override
        public void swichDarkMode(int color, boolean isDarkMode) {
            if (isDarkMode) {
                binding.recyclerEverEditor.setTextColor(itemView.getContext().getColor(R.color.White));
            } else {
                binding.recyclerEverEditor.setTextColor(itemView.getContext().getColor(R.color.NightBlack));
            }
        }
    }

    public static class noteScreenDrawViewHolder extends RecyclerView.ViewHolder implements EverInterfaceHelper.OnDownloadCompleted {
        @NonNull
        private final DrawViewNotescreenRecyclerBinding binding;
        private EverLinkedMap everLinkedMap;
        private String downloadKey;

        @SuppressLint("ClickableViewAccessibility")
        public noteScreenDrawViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            binding = DrawViewNotescreenRecyclerBinding.bind(view);
        }

        void setDrawContent() {
            String drawPath = everLinkedMap.getDrawLocation();

            //   if (!lastDrawPath.equals(drawPath)) {
            String[] split = drawPath.split("<>");
            File f = new File(drawPath);
            if (fileExists(drawPath, 0)) {
                binding.recyclerImage.setVisibility(View.VISIBLE);
                int h = (Integer.parseInt(split[1]) / 2);
                ViewAnimator.animate(binding.getRoot()).interpolator(new LinearOutSlowInInterpolator()).duration(650).waitForHeight().height(0, h).alpha(0, 1).start();
                int w = (Integer.parseInt(split[2]) / 2);
                Glide.with(binding.recyclerImage).
                        load(f).
                        transition(DrawableTransitionOptions.withCrossFade()).
                        encodeQuality(25).
                        override(w, h).
                        into(binding.recyclerImage);
            } else {
                binding.recyclerImage.setVisibility(View.GONE);
            }
        }

        private boolean fileExists(String path, int type) {
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                EverInterfaceHelper.getInstance().setDownloadListener(this);
                downloadKey = tempFile.getName();
                switch (type) {
                    case 0:
                        ((MainActivity) itemView.getContext()).getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 0, getAdapterPosition());
                        break;
                    case 1:
                        ((MainActivity) itemView.getContext()).getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 1, getAdapterPosition());
                        break;
                }
            }
            return tempFile.exists();
        }

        @Override
        public void downloadCompleted(File file, int p, String downloadKey) {
            if (downloadKey.equals(this.downloadKey) || this.downloadKey.isEmpty()) {
                everLinkedMap.setDrawLocation(file.getPath());
                setDrawContent();
            }
        }
    }

    public static class noteScreenRecordViewHolder extends RecyclerView.ViewHolder implements EverInterfaceHelper.OnEnterDarkMode, EverInterfaceHelper.OnDownloadCompleted {
        @NonNull
        private final RecordViewNotescreenRecyclerBinding binding;
        private String downloadKey;
        private EverLinkedMap everLinkedMap;
        private int color;

        @SuppressLint("ClickableViewAccessibility")
        public noteScreenRecordViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View


            binding = RecordViewNotescreenRecyclerBinding.bind(view);



            binding.everWave.setWaveGravity(EverWave.WaveGravity.CENTER);

            EverInterfaceHelper.getInstance().setDarkModeListeners(this);
        }

        void setWaveSeek() {
            String recordPath = everLinkedMap.getRecord();
            // if (!lastRecordPath.equals(recordPath)) {

            if (!recordPath.equals("▓") && recordPath != null) {
                if (fileExists(recordPath, 1)) {


                    updateColor();

                    binding.cardEverNoteScreenPlayer.setVisibility(View.VISIBLE);
                    // lastColorKnown = color;

                    binding.everWave.setVisibility(View.VISIBLE);
                    ((MainActivity) itemView.getContext()).asyncTask(() -> EverWaveOptions.getSampleFrom(everLinkedMap.getRecord(), ints -> {
                        binding.everWave.setSample(ints);
                        return Unit.INSTANCE;
                    }), () -> ViewAnimator.animate(binding.everWave)
                            .duration(250)
                            .interpolator(new LinearOutSlowInInterpolator())
                            .alpha(0, 1)
                            .scale(1.25f, 1f)
                            .start());
                } else {
                    binding.cardEverNoteScreenPlayer.setVisibility(View.GONE);
                }
            } else {
                binding.cardEverNoteScreenPlayer.setVisibility(View.GONE);
            }
            //  }
            //  lastRecordPath = recordPath;
        }

        private boolean fileExists(String path, int type) {
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                EverInterfaceHelper.getInstance().setDownloadListener(this);
                downloadKey = tempFile.getName();
                switch (type) {
                    case 0:
                        ((MainActivity) itemView.getContext()).getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 0, getAdapterPosition());
                        break;
                    case 1:
                        ((MainActivity) itemView.getContext()).getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 1, getAdapterPosition());
                        break;
                }
            }
            return tempFile.exists();
        }

        @Override
        public void swichDarkMode(int color, boolean isDarkMode) {
            updateColor();
        }

        @Override
        public void downloadCompleted(File file, int p, String downloadKey) {
            if (downloadKey.equals(this.downloadKey)) {
                everLinkedMap.setRecord(file.getPath());
                setWaveSeek();
                ViewAnimator.animate(binding.everWave)
                        .duration(250)
                        .interpolator(new LinearOutSlowInInterpolator())
                        //  .waitForHeight()
                        .alpha(0, 1)
                        .height(0, binding.cardEverNoteScreenPlayer.getHeight())
                        .start();
            }
        }

        private void updateColor() {
            if (((MainActivity)itemView.getContext()).getEverThemeHelper().isDarkMode()) {
                binding.everWave.setWaveBackgroundColor(this.color);
                binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getColor(R.color.NightBlack)));
            } else {
                if (this.color == -1) {
                    binding.everWave.setWaveBackgroundColor(itemView.getContext().getColor(R.color.NightLessBlack));
                } else {
                    binding.everWave.setWaveBackgroundColor(Color.WHITE);
                }
                binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(this.color));
            }
        }
    }
}

