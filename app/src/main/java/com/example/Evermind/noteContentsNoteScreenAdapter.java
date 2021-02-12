package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.target.Target;
import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.databinding.MainViewNotescreenRecyclerBinding;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.masoudss.lib.WaveGravity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidaudiorecorder.Util;

public class noteContentsNoteScreenAdapter extends RecyclerView.Adapter<noteContentsNoteScreenAdapter.noteScreenViewHolder> {

    private final List<EverLinkedMap> noteData = new ArrayList<>();

    public noteContentsNoteScreenAdapter(List<EverLinkedMap> noteData) {
        this.noteData.addAll(noteData);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public noteScreenViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.main_view_notescreen_recycler, viewGroup, false);

        return new noteScreenViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(noteScreenViewHolder viewHolder, final int position) {
        viewHolder.everLinkedMap = noteData.get(position);
        viewHolder.setContentHTML();
        viewHolder.setDrawContent();
        viewHolder.setWaveSeek();
    }

    public void updateList(List<EverLinkedMap> everLinkedMaps) {
        //  final EverLinkedDiffUtil diffCallback = new EverLinkedDiffUtil(old, everLinkedMaps);
        //   final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, false);

        this.noteData.clear();
        this.noteData.addAll(everLinkedMaps);
        //   diffResult.dispatchUpdatesTo(this);
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
    public static class noteScreenViewHolder extends RecyclerView.ViewHolder implements EverInterfaceHelper.OnEnterDarkMode {
        @NonNull
        private final MainViewNotescreenRecyclerBinding binding;
        private String lastTextInEditor = "null";
        private String lastDrawPath = "null";
        private int lastColorKnown = -1;
        private String lastRecordPath = "null";
        private EverLinkedMap everLinkedMap;

        @SuppressLint("ClickableViewAccessibility")
        public noteScreenViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            binding = MainViewNotescreenRecyclerBinding.bind(view);
            if (lastTextInEditor.equals("null")) {
                binding.recyclerEverEditor.setPadding(15, 15, 15, 15);
                binding.recyclerEverEditor.setTextSize(18);
            }

            if (((MainActivity)view.getContext()).getEverThemeHelper().isDarkMode()) {
                binding.recyclerEverEditor.setTextColor(itemView.getContext().getColor(R.color.White));
                binding.everWave.setWaveBackgroundColor(itemView.getContext().getColor(R.color.NightBlack));
                binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(itemView.getContext().getColor(R.color.Grey))));
            }

            if (lastRecordPath.equals("null")) {
                binding.everWave.setWaveGravity(WaveGravity.CENTER);
            }

            EverInterfaceHelper.getInstance().setDarkModeListeners(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML() {
            String itemContent = everLinkedMap.getContent();
            if (!lastTextInEditor.equals(itemContent)) {
                if (!itemContent.equals("▓") && !itemContent.equals("<br>") && !itemContent.equals("")) {
                    binding.recyclerEverEditor.setVisibility(View.VISIBLE);
                    binding.recyclerEverEditor.setRichTextEditing(itemContent);
                } else {
                    binding.recyclerEverEditor.setVisibility(View.GONE);
                }
            }
            lastTextInEditor = itemContent;
        }


        void setDrawContent() {
            String drawPath = everLinkedMap.getDrawLocation();

            if (!lastDrawPath.equals(drawPath)) {
                String[] split = drawPath.split("<>");
                File f = new File(split[0]);
                if (f.exists()) {
                    binding.recyclerImage.setVisibility(View.VISIBLE);
                    int h = (Integer.parseInt(split[1]) / 2);
                    int w = (Integer.parseInt(split[2]) / 2);
                    Glide.with(binding.recyclerImage).asBitmap().load(f).encodeQuality(25).override(w, h).into(binding.recyclerImage);
                } else {
                    binding.recyclerImage.setVisibility(View.GONE);
                }
            }
            lastDrawPath = drawPath;
        }

        void setWaveSeek() {
            String recordPath = everLinkedMap.getRecord();
            if (!lastRecordPath.equals(recordPath)) {

                if (!recordPath.equals("▓")) {
                    String[] s = recordPath.split("<AMP>");
                    binding.cardEverNoteScreenPlayer.setVisibility(View.VISIBLE);
                    binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(everLinkedMap.getColor()));
                    lastColorKnown = everLinkedMap.getColor();


                    List<Integer> amplitude = new ArrayList<>();
                    for (String f : s[1].split(",")) {
                        amplitude.add(Math.round(Float.parseFloat(f)));
                    }
                    binding.everWave.setVisibility(View.VISIBLE);
                    binding.everWave.setSample(amplitude);

                } else {
                    binding.cardEverNoteScreenPlayer.setVisibility(View.GONE);
                }
            }
            lastRecordPath = recordPath;
        }

        @Override
        public void enterDarkMode(int color) {
            binding.recyclerEverEditor.setTextColor(itemView.getContext().getColor(R.color.White));
            binding.everWave.setWaveBackgroundColor(itemView.getContext().getColor(R.color.NightBlack));
            binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(itemView.getContext().getColor(R.color.Grey))));
        }
    }
}

