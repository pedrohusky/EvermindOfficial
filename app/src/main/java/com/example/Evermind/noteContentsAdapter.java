package com.example.Evermind;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Evermind.EverAudioVisualizerHandlers.EverDbmHandler;
import com.example.Evermind.EverAudioVisualizerHandlers.EverGLAudioVisualizationView;
import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.EverAudioVisualizerHandlers.EverVisualizerDbmHandler;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTFormat;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTImage;
import com.example.Evermind.databinding.MainViewNoteCreatorRecyclerBinding;
import com.example.Evermind.everUtils.EverLinkedDiffUtil;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.masoudss.lib.WaveGravity;
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

import cafe.adriel.androidaudiorecorder.Util;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.Evermind.TESTEDITOR.rteditor.media.crop.RotateBitmap.TAG;

public class noteContentsAdapter extends RecyclerView.Adapter<noteContentsAdapter.ViewHolder> {

    private final List<EverLinkedMap> noteData = new ArrayList<>();

    public noteContentsAdapter(List<EverLinkedMap> noteData) {
        this.noteData.addAll(noteData);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.main_view_note_creator_recycler, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        MainActivity m = ((MainActivity)viewHolder.itemView.getContext());

        if (m.getActualNote().everLinkedContents.size() == 1) {
            m.getNoteCreator().gettextRecyclerCreator().setMinimumHeight(1650);
        } else {
            m.getNoteCreator().gettextRecyclerCreator().setMinimumHeight(0);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewHolder.init(noteData.get(position));
        }
    }

    public List<EverLinkedMap> getData() {
        return noteData;
    }

    public void updateEverLinkedMaps(List<EverLinkedMap> everLinkedMaps) {
        final EverLinkedDiffUtil diffCallback = new EverLinkedDiffUtil(this.noteData, everLinkedMaps);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

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
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, EverInterfaceHelper.OnOpenAudioStateListener, EverInterfaceHelper.OnChangeColorListener, EverInterfaceHelper.OnCanPerformClickListener {
        private final MainViewNoteCreatorRecyclerBinding binding;
        private final WeakReference<MainActivity> mainActivity;
        private final View.OnClickListener decoyClick = v -> ((MainActivity)v.getContext()).getNoteCreator().setNoteState(1);
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
        public RTEditText ActiveEditor;
        private MediaPlayer mediaPlayer;
        private boolean prepared = false;
        private ValueAnimator va;
     //   private int ActiveEditorPosition;
        private int imageDecoySize;
        private EverGLAudioVisualizationView visualizationView;
        private TextWatcher watcher;
        private int SelectedDrawPosition;
        private int FinalYHeight;
        private String lastRecordPath = "null";
        private String lastPathDraw = "null";
        private String lastTextInEditor = "null";
        private EverVisualizerDbmHandler mini_visualizerHandler;
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

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            mainActivity = new WeakReference<>(((MainActivity)itemView.getContext()));

            EverInterfaceHelper.getInstance().setClickListener(this);
            binding = MainViewNoteCreatorRecyclerBinding.bind(view);
            binding.view2.setOnClickListener(v -> mainActivity.get().getNoteCreator().setNoteState(1));

            binding.recyclerImageView.setOnLongClickListener(longClick);
            binding.AudioViewButton.setOnLongClickListener(longClick);

            if (lastTextInEditor.equals("null")) {
                mainActivity.get().registerEditor(binding.evermindEditor, true);
                binding.evermindEditor.setPaddingRelative(8, 8, 8, 8);
                binding.evermindEditor.setTextSize(22);
                binding.evermindEditor.setOnRichContentListener((contentUri, description) -> {
                    if (description.getMimeTypeCount() > 0 && contentUri != null) {
                        final String fileExtension = MimeTypeMap.getSingleton()
                                .getExtensionFromMimeType(description.getMimeType(0));
                        final String filename = System.currentTimeMillis() + "." + fileExtension;
                        File richContentFile = new File(itemView.getContext().getFilesDir(), filename);
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
                        new Thread(() -> {
                            String text = ActiveEditor.getText(RTFormat.HTML);
                            EverLinkedMap e = mainActivity.get().getActualNote().getEverLinkedContents(false).get(getAdapterPosition());
                            e.setContent(text);
                            mainActivity.get().getActualNote().getEverLinkedContents(false).set(getAdapterPosition(), e);
                        }).start();
                    }
                };
            }

            if (lastRecordPath.equals("null")) {

                binding.everWave2.setWaveBackgroundColor(itemView.getContext().getColor(R.color.Grey));
                binding.everWave2.setWaveProgressColor(itemView.getContext().getColor(R.color.SkyBlue));
                binding.everWave2.setWaveGravity(WaveGravity.CENTER);
                binding.everWave2.setMMaxValue(110);
                PushDownAnim.setPushDownAnimTo(binding.AudioViewButton).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            }

            if (lastPathDraw.equals("null")) {
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
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void init(EverLinkedMap everLinkedMap) {
            this.everLinkedMap = everLinkedMap;
            mainActivity.get().runOnUiThread(() -> {
                setContentHTML();
                setDrawContent();
                setAudio();
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML() {
            String text = everLinkedMap.getContent();
        //    if (!lastTextInEditor.equals(text)) {
                if (text.equals("▓") || text.equals("")) {
                    binding.evermindEditor.setVisibility(View.GONE);
                } else {
                    if (!lastTextInEditor.equals(everLinkedMap.getContent())) {


                        if (mainActivity.get().getEverThemeHelper().isDarkMode()) {
                            binding.evermindEditor.setTextColor(mainActivity.get().getColor(R.color.PurpleHighlight));
                        }
                        binding.evermindEditor.setVisibility(View.VISIBLE);
                        binding.evermindEditor.removeTextChangedListener(watcher);
                        binding.evermindEditor.setRichTextEditing(true, text);
                        ActiveEditor = binding.evermindEditor;
                        lastTextInEditor = text;


                        binding.evermindEditor.addTextChangedListener(watcher);
                    } else {
                        binding.evermindEditor.setVisibility(View.VISIBLE);
                    }
                }
           // } else {
               // if (text.equals("▓") || text.equals("")) {
             //       binding.evermindEditor.setVisibility(View.GONE);
             //   } else {
             //       binding.evermindEditor.setVisibility(View.VISIBLE);
             //   }
           // }
        }

        @SuppressLint("ClickableViewAccessibility")
        void setDrawContent() {
            String drawPath = everLinkedMap.getDrawLocation();
         //   if (!lastPathDraw.equals(drawPath)) {
                String[] split = drawPath.split("<>");
                File f = new File(split[0]);
                if (f.exists()) {

                    setupDrawView(false);
                    int h = Integer.parseInt(split[1]);
                    int w = Integer.parseInt(split[2]);
                    ViewGroup.LayoutParams params = binding.recyclerImageView.getLayoutParams();
                    params.height = h;
                    params.width = w;
                    Glide.with(binding.recyclerImageView).asBitmap().load(f).encodeQuality(100).into(binding.recyclerImageView);

                } else {
                    if (!drawPath.equals("▓")) {
                        setupDrawView(true);
                    } else {
                        binding.cardDrawRecycler.setVisibility(View.GONE);
                    }
                }
         //   } else {
          //      binding.cardDrawRecycler.setVisibility(View.VISIBLE);
          //  }
        }

        void setAudio() {
            if (!everLinkedMap.getRecord().equals("▓")) {
                String[] recordAndAmplitude = everLinkedMap.getRecord().split("<AMP>");
                String recordPath = recordAndAmplitude[0];
           //     if (!lastRecordPath.equals(recordPath)) {

                    if (recordPath.contains("record")) {

                        try {
                            mediaPlayer = new MediaPlayer();
                            mini_visualizerHandler = EverDbmHandler.Factory.newVisualizerHandler(itemView.getContext(), mediaPlayer);

                            if (mediaPlayer.getDuration() != -1) {
                                mediaPlayer.reset();
                            }
                            mediaPlayer.setDataSource(recordPath);
                            List<Integer> amplitude = new ArrayList<>();
                            for (String f : recordAndAmplitude[1].split(",")) {
                                amplitude.add(Math.round(Float.parseFloat(f)));
                            }
                            addVisualizer(amplitude, Integer.parseInt(mainActivity.get().getActualNote().getNoteColor()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        binding.cardEverPlayer.setVisibility(View.VISIBLE);
                        lastRecordPath = recordPath;
                        mini_visualizerHandler.setInnerOnPreparedListener(mp -> {
                            prepared = true;
                            binding.everWave2.setOnProgressChanged((waveformSeekBar, i, b) -> {
                                if (b) {
                                    if (i >= 0) {
                                        toggleAudio(mp, 0);
                                        int time = mp.getDuration() / 110 * i;
                                        toggleAudio(mp, time);

                                    }
                                }
                            });
                            toggleAudio(mp, 0);
                        });
                        mini_visualizerHandler.setInnerOnCompletionListener(mp -> {
                            binding.AudioViewButton.setImageResource(R.drawable.aar_ic_play);
                            mainActivity.get().getHandler().postDelayed(() -> {
                                if (!isPlaying()) {
                                    visualizationView.onPause();
                                }
                            }, 1000);
                        });
                        binding.AudioViewButton.setOnClickListener(v -> {
                            if (prepared) {
                                toggleAudio(mediaPlayer, 0);
                            } else {
                                mediaPlayer.prepareAsync();
                            }
                        });
                    }

              //  } else {
               //     binding.cardEverPlayer.setVisibility(View.GONE);
              //  }
            } else {
            //    if (!everLinkedMap.getRecord().equals("▓")) {
              //      binding.cardEverPlayer.setVisibility(View.VISIBLE);
            //    } else {
                    binding.cardEverPlayer.setVisibility(View.GONE);
              //  }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        void setupDrawView(boolean empty) {

            binding.cardDrawRecycler.setVisibility(View.VISIBLE);
            binding.recyclerImageView.setVisibility(View.VISIBLE);
            //   TransitionManager.beginDelayedTransition(mainActivity.get().getCardNoteCreator(), new TransitionSet()
            //            .addTransition(new ChangeBounds()));
            ViewGroup.LayoutParams params = binding.cardDrawRecycler.getLayoutParams();

            params.height = WRAP_CONTENT;

            binding.everDrawCanvas.setVisibility(View.GONE);

            mainActivity.get().getNoteCreator().setEverDraw(binding.everDrawCanvas);


            if (empty) {

                binding.recyclerImageView.setVisibility(View.GONE);
                binding.everDrawCanvas.setVisibility(View.VISIBLE);
                // mainActivity.get().getEverViewManagement().beginDelayedTransition(binding.cardDrawRecycler);
                ViewGroup.LayoutParams params1 = binding.cardDrawRecycler.getLayoutParams();

                params1.height = 1000;
                FinalYHeight = 0;
                SelectedDrawPosition = getAdapterPosition();
                Toast.makeText(mainActivity.get(), "We are empty.", Toast.LENGTH_SHORT).show();
                mainActivity.get().getNoteCreator().onDrawClick(binding.everDrawCanvas, SelectedDrawPosition, everLinkedMap.getDrawLocation(), binding.cardDrawRecycler);

            }
            lastPathDraw = everLinkedMap.getDrawLocation();
        }



        @Override
        public void open() {
            binding.everGLAudioVisualizationView2.onResume();
            binding.everGLAudioVisualizationView2.setVisibility(View.VISIBLE);
            ((MainActivity) itemView.getContext()).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, 0, null);
        }

        @Override
        public void close() {
            binding.everGLAudioVisualizationView2.onPause();
            ((MainActivity) itemView.getContext()).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, imageDecoySize, new Runnable() {
                @Override
                public void run() {
                    binding.everGLAudioVisualizationView2.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void changeAccentColor(int color) {
            ((MainActivity)itemView.getContext()).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, imageDecoySize, null);
            ((MainActivity)itemView.getContext()).getEverThemeHelper().tintViewAccent(binding.imageDecoy, color, 0);
            if (!everLinkedMap.getRecord().equals("▓")) {
                mainActivity.get().getUIHandler().postDelayed(() -> {
                    if (binding.everGLAudioVisualizationView2.getVisibility() == View.VISIBLE) {
                        visualizationView.updateColor(new EverGLAudioVisualizationView.Builder(itemView.getContext())
                                .setBackgroundColor(Util.getDarkerColor(color))
                                .setLayerColors(new int[]{color}));
                        visualizationView.onResume();
                        mainActivity.get().getUIHandler().postDelayed(() -> {

                            imageDecoySize = binding.imageDecoy.getHeight();

                            ((MainActivity)itemView.getContext()).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, 0, null);

                            visualizationView.onPause();

                        }, 150);
                    }
                }, 500);
            }
        }

        private void addVisualizer(List<Integer> amplitude, int color) {
            visualizationView = binding.everGLAudioVisualizationView2;
            visualizationView.updateColor(new EverGLAudioVisualizationView.Builder(itemView.getContext())
                    .setBackgroundColor(Util.getDarkerColor(color))
                    .setLayerColors(new int[]{color}));

            binding.everWave2.setSample(amplitude);
            visualizationView.setVisibility(View.VISIBLE);
            visualizationView.linkTo(mini_visualizerHandler);
            visualizationView.onPause();


            EverInterfaceHelper.getInstance().setListener(this);
            EverInterfaceHelper.getInstance().setAccentColorListener(this);

            int colorBlue = itemView.getContext().getColor(R.color.SkyBlue);
            if (color != colorBlue) {
                mainActivity.get().getEverThemeHelper().tintViewAccent(binding.everWave2, colorBlue, 1000);
            } else {
                mainActivity.get().getEverThemeHelper().tintViewAccent(binding.everWave2, itemView.getContext().getColor(R.color.White), 1000);
            }

            binding.imageDecoy.setBackgroundColor(Integer.parseInt(((MainActivity)itemView.getContext()).getActualNote().getNoteColor()));
            binding.cardEverPlayer.setVisibility(View.VISIBLE);

            mainActivity.get().getUIHandler().postDelayed(() -> {

                visualizationView.onResume();

                mainActivity.get().getUIHandler().postDelayed(() -> {

                    imageDecoySize = binding.imageDecoy.getHeight();

                    ((MainActivity)itemView.getContext()).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, 0, null);

                    visualizationView.onPause();

                }, 150);
            }, 1000);
        }

        private void toggleAudio(@NonNull MediaPlayer mp, int actualTime) {
            if (isPlaying()) {
                binding.AudioViewButton.setImageResource(R.drawable.aar_ic_play);
                va.pause();
                mp.pause();
                visualizationView.onPause();
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
                visualizationView.onResume();
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
    }
}

