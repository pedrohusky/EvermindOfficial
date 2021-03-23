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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;
import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.databinding.DrawViewNotescreenRecyclerBinding;
import com.example.Evermind.databinding.MainViewNotescreenRecyclerBinding;
import com.example.Evermind.databinding.RecordViewNotescreenRecyclerBinding;
import com.example.Evermind.databinding.TextViewNotescreenRecyclerBinding;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.masoudss.lib.WaveGravity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidaudiorecorder.Util;

public class noteContentsNoteScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<EverLinkedMap> noteData = new ArrayList<>();

    public noteContentsNoteScreenAdapter(List<EverLinkedMap> noteData) {
        this.noteData.addAll(noteData);
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
                noteScreenTextViewHolder textHolder = ((noteScreenTextViewHolder)viewHolder);
                textHolder.everLinkedMap = noteData.get(position);
                textHolder.setContentHTML();
                break;
            case 2:
                noteScreenDrawViewHolder drawHolder = ((noteScreenDrawViewHolder)viewHolder);
                drawHolder.everLinkedMap = noteData.get(position);
                drawHolder.setDrawContent();
                break;
            case 3:
                noteScreenRecordViewHolder recordHolder = ((noteScreenRecordViewHolder)viewHolder);
                recordHolder.everLinkedMap = noteData.get(position);
                recordHolder.setWaveSeek();
                break;
        }

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
    public static class noteScreenTextViewHolder extends RecyclerView.ViewHolder implements EverInterfaceHelper.OnEnterDarkMode {
        @NonNull
        private final TextViewNotescreenRecyclerBinding binding;
        private String lastTextInEditor = "null";
        private EverLinkedMap everLinkedMap;

        @SuppressLint("ClickableViewAccessibility")
        public noteScreenTextViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            binding = TextViewNotescreenRecyclerBinding.bind(view);
            if (lastTextInEditor.equals("null")) {
                binding.recyclerEverEditor.setPadding(15, 15, 15, 15);
                binding.recyclerEverEditor.setTextSize(18);
            }

            if (((MainActivity) view.getContext()).getEverThemeHelper().isDarkMode()) {
                binding.recyclerEverEditor.setTextColor(itemView.getContext().getColor(R.color.White));
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

        @Override
        public void swichDarkMode(int color, boolean isDarkMode) {
            binding.recyclerEverEditor.setTextColor(itemView.getContext().getColor(R.color.White));
        }
    }

    public static class noteScreenDrawViewHolder extends RecyclerView.ViewHolder implements EverInterfaceHelper.OnDownloadCompleted {
        @NonNull
        private final DrawViewNotescreenRecyclerBinding binding;
        private String lastDrawPath = "null";
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
                System.out.println("TRying...");
                if (fileExists(drawPath, 0)) {
                    binding.recyclerImage.setVisibility(View.VISIBLE);
                    int h = (Integer.parseInt(split[1]) / 2);
                    int w = (Integer.parseInt(split[2]) / 2);
                    Glide.with(binding.recyclerImage).
                            load(f).
                            transition(DrawableTransitionOptions.withCrossFade()).
                            encodeQuality(25).
                            override(w,h).
                            into(binding.recyclerImage);
                } else {
                    binding.recyclerImage.setVisibility(View.GONE);
                }
        //    }
            lastDrawPath = drawPath;
        }
        private boolean fileExists(String path, int type) {
                File tempFile = new File(path);
                if (!tempFile.exists()) {
                    EverInterfaceHelper.getInstance().setDownloadListener(this);
                    downloadKey = tempFile.getName();
                    switch (type) {
                        case 0:
                            ((MainActivity)itemView.getContext()).getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 0, getAdapterPosition());
                            break;
                        case 1:
                            ((MainActivity)itemView.getContext()).getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 1, getAdapterPosition());
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
        private int lastColorKnown = -1;
        private String downloadKey;
        private String lastRecordPath = "null";
        private EverLinkedMap everLinkedMap;

        @SuppressLint("ClickableViewAccessibility")
        public noteScreenRecordViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View


            binding = RecordViewNotescreenRecyclerBinding.bind(view);

            if (((MainActivity)view.getContext()).getEverThemeHelper().isDarkMode()) {
                binding.everWave.setWaveBackgroundColor(itemView.getContext().getColor(R.color.NightBlack));
                binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(itemView.getContext().getColor(R.color.Grey))));
            }

            if (lastRecordPath.equals("null")) {
                binding.everWave.setWaveGravity(WaveGravity.CENTER);
            }

            EverInterfaceHelper.getInstance().setDarkModeListeners(this);
        }

        void setWaveSeek() {
            String recordPath = everLinkedMap.getRecord();
           // if (!lastRecordPath.equals(recordPath)) {

                if (!recordPath.equals("▓") && recordPath != null) {
                    if (fileExists(recordPath, 1)) {
                        binding.cardEverNoteScreenPlayer.setVisibility(View.VISIBLE);
                        binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(everLinkedMap.getColor()));
                        lastColorKnown = everLinkedMap.getColor();

                        binding.everWave.setVisibility(View.VISIBLE);
                        binding.everWave.setSample(everLinkedMap.getAudioData());
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
                        ((MainActivity)itemView.getContext()).getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 0, getAdapterPosition());
                        break;
                    case 1:
                        ((MainActivity)itemView.getContext()).getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 1, getAdapterPosition());
                        break;
                }
            }
            return tempFile.exists();
        }

        @Override
        public void swichDarkMode(int color, boolean isDarkMode) {
            binding.everWave.setWaveBackgroundColor(itemView.getContext().getColor(R.color.NightBlack));
            binding.cardEverNoteScreenPlayer.setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(itemView.getContext().getColor(R.color.Grey))));
        }

        @Override
        public void downloadCompleted(File file, int p, String downloadKey) {
            if (downloadKey.equals(this.downloadKey)) {
                everLinkedMap.setRecord(file.getPath());
                setWaveSeek();
            }
        }
    }
}

