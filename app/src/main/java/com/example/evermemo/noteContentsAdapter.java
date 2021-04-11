package com.example.evermemo;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.evermemo.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.evermemo.TESTEDITOR.rteditor.RTEditText;
import com.example.evermemo.TESTEDITOR.rteditor.api.format.RTFormat;
import com.example.evermemo.TESTEDITOR.rteditor.api.media.RTImage;
import com.example.evermemo.databinding.DrawViewNoteCreatorRecyclerBinding;
import com.example.evermemo.databinding.RecordViewNoteCreatorRecyclerBinding;
import com.example.evermemo.databinding.TextViewNoteCreatorRecyclerBinding;
import com.example.evermemo.everUtils.EverLinkedDiffUtil;
import com.example.evermemo.recycler_models.EverLinkedMap;
import com.github.florent37.viewanimator.ViewAnimator;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.evermemo.TESTEDITOR.rteditor.media.crop.RotateBitmap.TAG;

public class noteContentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<EverLinkedMap> noteData = new ArrayList<>();
    private int color;
    private WeakReference<MainActivity> mainActivityWeakReference;

    public noteContentsAdapter(List<EverLinkedMap> noteData, int color) {
        this.color = color;
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
                        .inflate(R.layout.text_view_note_creator_recycler, viewGroup, false);
                break;
            case 2:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.draw_view_note_creator_recycler, viewGroup, false);
                break;
            case 3:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.record_view_note_creator_recycler, viewGroup, false);
                break;
        }
        if (viewType == 1) {
            return new TextViewHolder(view);
        } else if (viewType == 2) {
            return new DrawViewHolder(view);
        } else {
            return new RecordViewHolder(view);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        mainActivityWeakReference = new WeakReference<>(((MainActivity) viewHolder.itemView.getContext()));

        if (mainActivityWeakReference.get().getActualNote().getEverLinkedContents(false).size() == 1) {
            mainActivityWeakReference.get().getNoteCreator().gettextRecyclerCreator().setMinimumHeight(1650);
        } else {
            mainActivityWeakReference.get().getNoteCreator().gettextRecyclerCreator().setMinimumHeight(0);
        }

        switch (getItemViewType(position)) {
            case 1:
                TextViewHolder textHolder = ((TextViewHolder) viewHolder);
                textHolder.init(noteData.get(position));
                break;
            case 2:
                DrawViewHolder drawHolder = ((DrawViewHolder) viewHolder);
                drawHolder.init(noteData.get(position));
                break;
            case 3:
                RecordViewHolder recordHolder = ((RecordViewHolder) viewHolder);
                recordHolder.init(noteData.get(position), color);
                break;
        }
    }

    public List<EverLinkedMap> getData() {
        return noteData;
    }

    public void updateEverLinkedMaps(List<EverLinkedMap> everLinkedMaps, int color) {
        final EverLinkedDiffUtil diffCallback = new EverLinkedDiffUtil(this.noteData, everLinkedMaps);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.color = color;
        this.noteData.clear();
        this.noteData.addAll(everLinkedMaps);
        diffResult.dispatchUpdatesTo(this);
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
    public static class TextViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, EverInterfaceHelper.OnCanPerformClickListener, EverInterfaceHelper.OnEnterDarkMode {
        private final TextViewNoteCreatorRecyclerBinding binding;
        private final WeakReference<MainActivity> mainActivity;
        private final TextWatcher watcher;
        public RTEditText ActiveEditor;
        private EverLinkedMap everLinkedMap;

        @SuppressLint("ClickableViewAccessibility")
        public TextViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            mainActivity = new WeakReference<>(((MainActivity) itemView.getContext()));

            EverInterfaceHelper.getInstance().setClickListener(this);
            EverInterfaceHelper.getInstance().setDarkModeListeners(this);
            binding = TextViewNoteCreatorRecyclerBinding.bind(view);
            binding.view2.setOnClickListener(v -> mainActivity.get().getNoteCreator().setNoteState(1));

            //   if (lastTextInEditor.equals("null")) {
            mainActivity.get().registerEditor(binding.evermindEditor, true);
            binding.evermindEditor.setPaddingRelative(8, 8, 8, 8);
            binding.evermindEditor.setTextSize(22);
            binding.evermindEditor.setOnRichContentListener((contentUri, description) -> {
                if (description.getMimeTypeCount() > 0 && contentUri != null) {
                    final String fileExtension = MimeTypeMap.getSingleton()
                            .getExtensionFromMimeType(description.getMimeType(0));
                    final String filename = System.currentTimeMillis() + "." + fileExtension;
                    File richContentFile = new File(mainActivity.get().getLocalFileDirectory(), filename);
                    if (!writeToFileFromContentUri(richContentFile, contentUri)) {
                        System.out.println("Cant Write File");
                    }

                    mainActivity.get().getmRTManager().insertImage(ActiveEditor, new RTImage() {
                        @NonNull
                        @Override
                        public String getFilePath(RTFormat format) {
                            return richContentFile.getPath();
                        }

                        @NonNull
                        @Override
                        public String getFileName() {
                            return filename;
                        }

                        @Override
                        public String getFileExtension() {
                            return fileExtension;
                        }

                        @Override
                        public boolean exists() {
                            return richContentFile.exists();
                        }

                        @Override
                        public void remove() {
                            if (richContentFile.delete()) {
                                System.out.println("IMAGE FROM KEYBOARD DELETED!");
                            }
                        }

                        @Override
                        public int getWidth() {
                            return 0;
                        }

                        @Override
                        public int getHeight() {
                            return 0;
                        }

                        @Override
                        public long getSize() {
                            return 0;
                        }
                    });
                }
            });
            binding.evermindEditor.setOnFocusChangeListener((view2, b) -> {
                if (b) {
                    mainActivity.get().getEverViewManagement().switchToolbars(true, true, true);
                    ActiveEditor = binding.evermindEditor;
                    mainActivity.get().getEverBallsHelper().metaColorForeGroundColor = itemView.getContext().getColor(R.color.White);
                    mainActivity.get().getEverBallsHelper().metaColorHighlightColor = itemView.getContext().getColor(R.color.White);

                    mainActivity.get().registerEditor(binding.evermindEditor, true);
                    mainActivity.get().getNoteCreator().setActiveEditor(ActiveEditor);
                    //  mainActivity.get().getNoteCreator().setActiveEditorPosition(getLayoutPosition());
                    //   ActiveEditorPosition = getLayoutPosition();
                } else {
                    mainActivity.get().getNoteCreator().setActiveEditor(null);
                }
            });
            watcher = new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = ActiveEditor.getText(RTFormat.HTML);
                    everLinkedMap.setContent(text);
                }
            };
            //}
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void init(EverLinkedMap everLinkedMap) {
            this.everLinkedMap = everLinkedMap;
            this.setContentHTML();
            //   mainActivity.get().runOnUiThread(() -> {
            //     setContentHTML();
            //    });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML() {
            String text = everLinkedMap.getContent();
            if (text.equals("▓") || text.equals("")) {
                binding.evermindEditor.setVisibility(View.GONE);
            } else {


                if (mainActivity.get().getEverThemeHelper().isDarkMode()) {
                    binding.evermindEditor.setTextColor(mainActivity.get().getColor(R.color.White));
                } else {
                    binding.evermindEditor.setTextColor(mainActivity.get().getColor(R.color.NightBlack));
                }
                binding.evermindEditor.setVisibility(View.VISIBLE);
                binding.evermindEditor.removeTextChangedListener(watcher);
                binding.evermindEditor.setRichTextEditing(true, text);
                ActiveEditor = binding.evermindEditor;


                binding.evermindEditor.addTextChangedListener(watcher);

            }
        }

        @Override
        public void canPerformClick() {
            binding.view2.setVisibility(View.GONE);
        }

        @Override
        public void cantPerformClick() {
            binding.view2.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        public boolean writeToFileFromContentUri(@Nullable File file, @Nullable Uri uri) {
            if (file == null || uri == null) return false;
            try {
                InputStream stream = itemView.getContext().getContentResolver().openInputStream(uri);
                OutputStream output = new FileOutputStream(file);
                if (stream == null) return false;
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = stream.read(buffer)) != -1) output.write(buffer, 0, read);
                output.flush();
                output.close();
                stream.close();
                System.out.println("File created successfully at: " + file.getPath());
                return true;
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Couldn't open stream: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException on stream: " + e.getMessage());
            }
            return false;
        }

        @Override
        public void swichDarkMode(int color, boolean isDarkMode) {
            if (isDarkMode) {
                binding.evermindEditor.setTextColor(mainActivity.get().getColor(R.color.White));
            } else {
                binding.evermindEditor.setTextColor(mainActivity.get().getColor(R.color.NightBlack));
            }
        }
    }

    public static class DrawViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, EverInterfaceHelper.OnCanPerformClickListener, EverInterfaceHelper.OnDownloadCompleted {
        private final DrawViewNoteCreatorRecyclerBinding binding;
        private final WeakReference<MainActivity> mainActivity;
        private final View.OnClickListener decoyClick = v -> ((MainActivity) v.getContext()).getNoteCreator().setNoteState(1);
        private final View.OnLongClickListener longClick = v -> {
            EverPopup popupWindowHelper;
            View popView;
            popView = LayoutInflater.from(v.getContext()).inflate(R.layout.options_attached, null);
            popupWindowHelper = new EverPopup(popView);
            popupWindowHelper.showAsPopUp(v, v.getWidth() / 3, v.getHeight() / 2);
            ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

            imageView.setOnClickListener(view1 -> {
                ((MainActivity) v.getContext()).getNoteCreator().removeEverLinkedContentAtPosition(getAdapterPosition());
                popupWindowHelper.dismiss();
            });
            return false;
        };
        private int SelectedDrawPosition;
        private int FinalYHeight;
        private EverLinkedMap everLinkedMap;
        private final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager keyboard = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (keyboard.isActive()) {
                    keyboard.hideSoftInputFromWindow(binding.recyclerImageView.getWindowToken(), 0);
                }
                FinalYHeight = 0;
                SelectedDrawPosition = getAdapterPosition();

                // mainActivity.get().getNoteCreator().setEverDraw(binding.everDrawCanvas);

                binding.everDrawCanvas.getLayoutParams().height = binding.cardDrawRecycler.getHeight();

                mainActivity.get().getNoteCreator().onDrawClick(binding.everDrawCanvas, SelectedDrawPosition, everLinkedMap.getDrawLocation(), binding.cardDrawRecycler);

            }
        };
        private String downloadKey;

        @SuppressLint("ClickableViewAccessibility")
        public DrawViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            mainActivity = new WeakReference<>(((MainActivity) itemView.getContext()));

            EverInterfaceHelper.getInstance().setClickListener(this);
            binding = DrawViewNoteCreatorRecyclerBinding.bind(view);
            binding.view2.setOnClickListener(v -> mainActivity.get().getNoteCreator().setNoteState(1));

            binding.recyclerImageView.setOnLongClickListener(longClick);

            //  if (lastPathDraw.equals("null")) {
            binding.everDrawCanvas.setOnTouchListener((view3, motionEvent) -> {
                mainActivity.get().getNoteCreator().gettextRecyclerCreator().suppressLayout(true);

                int y = (int) motionEvent.getY();

                if (y >= FinalYHeight) {
                    FinalYHeight = y;
                    mainActivity.get().getNoteCreator().setFinalYHeight(y);
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mainActivity.get().getNoteCreator().gettextRecyclerCreator().suppressLayout(false);
                    if (y >= binding.cardDrawRecycler.getHeight() - 75) {
                        TransitionManager.beginDelayedTransition(mainActivity.get().getCardNoteCreator(), new TransitionSet()
                                .addTransition(new ChangeBounds()));

                        ViewGroup.LayoutParams params = binding.cardDrawRecycler.getLayoutParams();

                        params.height = FinalYHeight + 100;
                        binding.cardDrawRecycler.setLayoutParams(params);
                        ViewGroup.LayoutParams params1 = binding.everDrawCanvas.getLayoutParams();

                        params1.height = FinalYHeight + 100;
                        binding.everDrawCanvas.setLayoutParams(params1);

                        return false;
                    }
                }
                return false;
            });
            binding.recyclerImageView.setOnClickListener(decoyClick);

            PushDownAnim.setPushDownAnimTo(binding.recyclerImageView);
            //    }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void init(EverLinkedMap everLinkedMap) {
            this.everLinkedMap = everLinkedMap;
            // mainActivity.get().runOnUiThread(this::setDrawContent);
            this.setDrawContent();
        }

        @SuppressLint("ClickableViewAccessibility")
        void setDrawContent() {
            String drawPath = everLinkedMap.getDrawLocation();
            //   if (!lastPathDraw.equals(drawPath)) {
            String[] split = drawPath.split("<>");
            File f = new File(drawPath);
            if (split.length == 3) {
                f = new File(split[3]);
            }
            if (fileExists(drawPath, 0)) {

                setupDrawView(false);
                int h = Integer.parseInt(split[1]);
                int w = Integer.parseInt(split[2]);
                ViewGroup.LayoutParams params = binding.recyclerImageView.getLayoutParams();
                params.height = h;
                params.width = w;
                Glide.with(binding.recyclerImageView).load(f).transition(DrawableTransitionOptions.withCrossFade()).encodeQuality(100).into(binding.recyclerImageView);

            } else {
                if (drawPath.equals("edit")) {
                    setupDrawView(true);
                } else {
                    binding.cardDrawRecycler.setVisibility(View.GONE);
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        void setupDrawView(boolean empty) {

            binding.cardDrawRecycler.setVisibility(View.VISIBLE);
            binding.recyclerImageView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = binding.cardDrawRecycler.getLayoutParams();

            params.height = WRAP_CONTENT;

            binding.everDrawCanvas.setVisibility(View.GONE);

            mainActivity.get().getNoteCreator().setEverDraw(binding.everDrawCanvas);


            if (empty) {

                binding.recyclerImageView.setVisibility(View.GONE);
                binding.everDrawCanvas.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params1 = binding.cardDrawRecycler.getLayoutParams();

                params1.height = 1000;
                FinalYHeight = 0;
                SelectedDrawPosition = getAdapterPosition();
                Toast.makeText(mainActivity.get(), "We are empty.", Toast.LENGTH_SHORT).show();
                mainActivity.get().getNoteCreator().onDrawClick(binding.everDrawCanvas, SelectedDrawPosition, everLinkedMap.getDrawLocation(), binding.cardDrawRecycler);

            }
        }

        @Override
        public void canPerformClick() {
            binding.view2.setVisibility(View.GONE);
            binding.recyclerImageView.setOnClickListener(clickListener);
        }

        @Override
        public void cantPerformClick() {
            binding.view2.setVisibility(View.VISIBLE);
            binding.recyclerImageView.setOnClickListener(decoyClick);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        private boolean fileExists(String path, int type) {
            File tempFile = new File(path);
            if (!tempFile.exists() && !path.equals("edit")) {
                EverInterfaceHelper.getInstance().setDownloadListener(this);
                downloadKey = tempFile.getName();
                switch (type) {
                    case 0:
                        mainActivity.get().getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 0, getAdapterPosition());
                        break;
                    case 1:
                        mainActivity.get().getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 1, getAdapterPosition());
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

    public static class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, EverInterfaceHelper.OnChangeColorListener, EverInterfaceHelper.OnCanPerformClickListener, EverInterfaceHelper.OnDownloadCompleted, EverInterfaceHelper.OnEnterDarkMode {
        private final RecordViewNoteCreatorRecyclerBinding binding;
        private final WeakReference<MainActivity> mainActivity;
        private final View.OnClickListener decoyClick = v -> ((MainActivity) v.getContext()).getNoteCreator().setNoteState(1);
        private final View.OnLongClickListener longClick = v -> {
            EverPopup popupWindowHelper;
            View popView;
            popView = LayoutInflater.from(v.getContext()).inflate(R.layout.options_attached, null);
            popupWindowHelper = new EverPopup(popView);
            popupWindowHelper.showAsPopUp(v, v.getWidth() / 3, v.getHeight() / 2);
            ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

            imageView.setOnClickListener(view1 -> {
                ((MainActivity) v.getContext()).getNoteCreator().removeEverLinkedContentAtPosition(getAdapterPosition());
                popupWindowHelper.dismiss();
            });
            return false;
        };
        private int color;
        private MediaPlayer mediaPlayer;
        private boolean prepared = false;
        private ValueAnimator va;
        private EverLinkedMap everLinkedMap;
        private String downloadKey;

        @SuppressLint("ClickableViewAccessibility")
        public RecordViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            mainActivity = new WeakReference<>(((MainActivity) itemView.getContext()));
            binding = RecordViewNoteCreatorRecyclerBinding.bind(view);
            binding.view2.setOnClickListener(v -> mainActivity.get().getNoteCreator().setNoteState(1));

            binding.AudioViewButton.setOnLongClickListener(longClick);
            binding.everWave2.setWaveGravity(EverWave.WaveGravity.CENTER);
            binding.everWave2.setMaxValue(110);
            binding.AudioViewButton.setOnTouchListener((view1, motionEvent) -> {
                mainActivity.get().getEverViewManagement().pushDownOnTouch(binding.cardButton, motionEvent, 0.7f, 100);
                return false;
            });
            EverInterfaceHelper.getInstance().setClickListener(this);
            EverInterfaceHelper.getInstance().setAccentColorListener(this);
            EverInterfaceHelper.getInstance().setDarkModeListeners(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void init(EverLinkedMap everLinkedMap, int color) {
            this.everLinkedMap = everLinkedMap;
            this.color = color;
            this.setAudio();
        }

        void setAudio() {
            String recordPath = everLinkedMap.getRecord();
            if (!recordPath.equals("▓")) {

                if (recordPath.contains("record")) {

                    updateColor(color, false);

                    mainActivity.get().asyncTask(() -> {
                        try {
                            mediaPlayer = new MediaPlayer();

                            if (mediaPlayer.getDuration() != -1) {
                                mediaPlayer.reset();
                            }

                            if (fileExists(recordPath, 1)) {
                                mediaPlayer.setDataSource(recordPath);
                                mediaPlayer.setOnCompletionListener(mediaPlayer -> binding.AudioViewButton.setImageResource(R.drawable.aar_ic_play));
                                mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                                    prepared = true;
                                    binding.everWave2.setOnProgressChanged((waveformSeekBar, i, b) -> {
                                        if (b) {
                                            if (i >= 0) {
                                                toggleAudio(mediaPlayer, 0);
                                                int time = mediaPlayer.getDuration() / 110 * i;
                                                toggleAudio(mediaPlayer, time);

                                            }
                                        }
                                    });
                                    toggleAudio(mediaPlayer, 0);
                                });
                                //  binding.cardEverPlayer.setBackgroundTintList(ColorStateList.valueOf(color));
                                EverWaveOptions.getSampleFrom(everLinkedMap.getRecord(), ints -> {
                                    binding.everWave2.setSample(ints);
                                    return Unit.INSTANCE;
                                });

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, () -> ViewAnimator.animate(binding.everWave2)
                            .duration(250)
                            .interpolator(new LinearOutSlowInInterpolator())
                            .alpha(0, 1)
                            .scale(1.25f, 1f)
                            .start());

                    int colorBlue = itemView.getContext().getColor(R.color.SkyBlue);
                    if (color == colorBlue) {
                        binding.everWave2.setWaveProgressColor(colorBlue);
                    } else {
                        binding.everWave2.setWaveProgressColor(itemView.getContext().getColor(R.color.White));
                    }

                    binding.cardEverPlayer.setVisibility(View.VISIBLE);
                    binding.AudioViewButton.setOnClickListener(v -> {
                        if (prepared) {
                            System.out.println("Prepared. Playing.");
                            toggleAudio(mediaPlayer, 0);
                        } else {
                            System.out.println("Preparing async.");
                            mediaPlayer.prepareAsync();
                        }
                    });
                }

            } else {
                binding.cardEverPlayer.setVisibility(View.GONE);
            }
        }

        private boolean fileExists(String path, int type) {
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                EverInterfaceHelper.getInstance().setDownloadListener(this);
                downloadKey = tempFile.getName();
                switch (type) {
                    case 0:
                        mainActivity.get().getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 0, getAdapterPosition());
                        break;
                    case 1:
                        mainActivity.get().getFirebaseHelper().getFileFromFirebase(tempFile.getName(), 1, getAdapterPosition());
                        break;
                }
            }
            return tempFile.exists();
        }

        @Override
        public void downloadCompleted(File file, int p, String downloadKey) {
            if (downloadKey.equals(this.downloadKey) || this.downloadKey.isEmpty()) {
                everLinkedMap.setRecord(file.getPath());
                setAudio();
                ViewAnimator.animate(binding.getRoot())
                        .duration(250)
                        .interpolator(new LinearOutSlowInInterpolator())
                        //  .waitForHeight()
                        .alpha(0, 1)
                        .height(0, binding.getRoot().getHeight())
                        .start();
            }
        }

        @Override
        public void changeAccentColor(int color) {

            updateColor(color, true);
        }

        private void toggleAudio(@NonNull MediaPlayer mp, int actualTime) {
            if (isPlaying()) {
                binding.AudioViewButton.setImageResource(R.drawable.aar_ic_play);
                va.pause();
                mp.pause();
            } else {
                if (getAudioTime() > 0) {
                    binding.AudioViewButton.setImageResource(R.drawable.aar_ic_pause);
                    if (actualTime != 0) {
                        va.setCurrentPlayTime(actualTime);
                        mediaPlayer.seekTo(actualTime);
                    }
                    va.resume();
                } else {

                    int percent = 110;
                    int result = mp.getDuration() * percent / 100;

                    va = ValueAnimator.ofFloat(0, percent);
                    va.setInterpolator(new LinearInterpolator());
                    va.setDuration(result);

                    va.addUpdateListener(animation -> binding.everWave2.setProgress(Math.round((Float) animation.getAnimatedValue())));

                    va.start();
                    binding.AudioViewButton.setImageResource(R.drawable.aar_ic_pause);
                }
                mp.start();
            }
        }

        private boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        private int getAudioTime() {
            if (va != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return Math.toIntExact(va.getCurrentPlayTime() * 100);
                } else {
                    return (int) va.getCurrentPlayTime() * 100;
                }
            } else {
                return 0;
            }
        }

        @Override
        public void canPerformClick() {
            binding.view2.setVisibility(View.GONE);
        }

        @Override
        public void cantPerformClick() {
            binding.view2.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public void swichDarkMode(int color, boolean isDarkMode) {
            updateColor(color, true);
        }

        private void updateColor(int color, boolean isSmooth) {
            if (isSmooth) {
                if (mainActivity.get().getEverThemeHelper().isDarkMode()) {
                    mainActivity.get().getEverThemeHelper().tintViewAccent(binding.everWave2, color, 0);
                    binding.everWave2.setWaveProgressColor(itemView.getContext().getColor(R.color.Grey));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(binding.cardButton, mainActivity.get().getColor(R.color.Grey), 0);
                    mainActivity.get().getEverThemeHelper().tintViewAccent(binding.cardEverPlayer, mainActivity.get().getColor(R.color.NightBlack), 0);
                } else {
                    mainActivity.get().getEverThemeHelper().tintViewAccent(binding.everWave2, mainActivity.get().getColor(R.color.White), 0);
                    binding.everWave2.setWaveProgressColor(itemView.getContext().getColor(R.color.SkyBlue));
                    mainActivity.get().getEverThemeHelper().tintViewAccent(binding.cardButton, mainActivity.get().getColor(R.color.White), 0);
                    mainActivity.get().getEverThemeHelper().tintViewAccent(binding.cardEverPlayer, color, 0);
                }
            } else {
                if (mainActivity.get().getEverThemeHelper().isDarkMode()) {
                    binding.everWave2.setWaveBackgroundColor(color);
                    binding.everWave2.setWaveProgressColor(itemView.getContext().getColor(R.color.Grey));
                    binding.cardButton.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getColor(R.color.Grey)));
                    binding.cardEverPlayer.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getColor(R.color.NightBlack)));
                } else {
                    binding.everWave2.setWaveBackgroundColor(mainActivity.get().getColor(R.color.White));
                    binding.everWave2.setWaveProgressColor(itemView.getContext().getColor(R.color.SkyBlue));
                    binding.cardButton.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getColor(R.color.White)));
                    binding.cardEverPlayer.setBackgroundTintList(ColorStateList.valueOf(color));
                }
            }
        }
    }
}

