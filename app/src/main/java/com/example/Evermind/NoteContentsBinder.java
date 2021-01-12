package com.example.Evermind;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.example.Evermind.EverAudioVisualizerHandlers.EverDbmHandler;
import com.example.Evermind.EverAudioVisualizerHandlers.EverGLAudioVisualizationView;
import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.EverAudioVisualizerHandlers.EverVisualizerDbmHandler;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTFormat;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTImage;
import com.example.Evermind.databinding.FragmentNoteCreatorBinding;
import com.example.Evermind.databinding.MainViewNoteCreatorRecyclerBinding;
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

import cafe.adriel.androidaudiorecorder.Util;
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.Evermind.TESTEDITOR.rteditor.media.crop.RotateBitmap.TAG;

public class NoteContentsBinder extends ItemBinder<EverLinkedMap, NoteContentsBinder.NoteCreatorHolder> {

    private final Context context;
    private final WeakReference<MainActivity> mainActivity;

    public NoteContentsBinder(Context context) {
        this.context = context;
        mainActivity = new WeakReference<>(((MainActivity)context));
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public NoteCreatorHolder createViewHolder(ViewGroup parent) {
         return new NoteCreatorHolder(inflate(parent, R.layout.main_view_note_creator_recycler));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof EverLinkedMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bindViewHolder(NoteCreatorHolder holder, EverLinkedMap everMap) {


        if (((MainActivity)context).getActualNote().everLinkedContents.size() == 1) {
            ((MainActivity)context).getCardNoteCreator().setMinimumHeight(1650);
        } else {
            ((MainActivity)context).getCardNoteCreator().setMinimumHeight(0);
        }

      holder.init();

        new Handler(Looper.getMainLooper()).post(() -> ((MainActivity) context).getNoteCreator().startPostponeTransition());
    }

    @Override
    public int getSpanSize(int maxSpanCount) {
        return super.getSpanSize(1);
    }



    class NoteCreatorHolder extends ItemViewHolder<EverLinkedMap> implements View.OnClickListener, View.OnLongClickListener, EverInterfaceHelper.OnOpenAudioStateListener, EverInterfaceHelper.OnChangeColorListener {

        private MediaPlayer mediaPlayer;
        private boolean prepared = false;
       private ValueAnimator va;
        private int ActiveEditorPosition;
        private int imageDecoySize;
        private EverGLAudioVisualizationView visualizationView;
        private TextWatcher watcher;
        public RTEditText ActiveEditor;
        private int SelectedDrawPosition;
        private int FinalYHeight;
        private String lastRecordPath = "null";
        private String lastPathDraw = "null";
        private String lastTextInEditor = "null";
        private final  MainViewNoteCreatorRecyclerBinding binding;
        EverVisualizerDbmHandler mini_visualizerHandler;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("ClickableViewAccessibility")
        public NoteCreatorHolder(View itemView) {
            super(itemView);
            binding = MainViewNoteCreatorRecyclerBinding.bind(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void init() {
            ((MainActivity)context).runOnUiThread(() -> {
                setContentHTML();
                setDrawContent();
                setAudio();
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML() {
            String text = getItem().getContent();
            if (!lastTextInEditor.equals(text)) {
                if (text.equals("▓") || text.equals("")) {
                    binding.evermindEditor.setVisibility(View.GONE);
                } else {
                    if (!lastTextInEditor.equals(getItem().getContent())) {
                        if (lastTextInEditor.equals("null")) {
                            ((MainActivity) context).registerEditor(binding.evermindEditor, true);
                            binding.evermindEditor.setPaddingRelative(8, 8, 8, 8);
                            binding.evermindEditor.setTextSize(22);
                            binding.evermindEditor.setRichTextEditing(true, text);
                            binding.evermindEditor.setOnRichContentListener((contentUri, description) -> {
                                if (description.getMimeTypeCount() > 0 && contentUri != null) {
                                    final String fileExtension = MimeTypeMap.getSingleton()
                                            .getExtensionFromMimeType(description.getMimeType(0));
                                    final String filename = System.currentTimeMillis() + "." + fileExtension;
                                    File richContentFile = new File(context.getFilesDir(), filename);
                                    if (!writeToFileFromContentUri(richContentFile, contentUri)) {
                                        System.out.println("Cant Write File");
                                    }

                                    ((MainActivity) context).getmRTManager().insertImage(ActiveEditor, new RTImage() {
                                        @Override
                                        public String getFilePath(RTFormat format) {
                                            return richContentFile.getPath();
                                        }

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
                            binding.evermindEditor.setOnFocusChangeListener((view, b) -> {
                                if (b) {
                                    ((MainActivity) context).OnFocusChangeEditor(b);
                                    ActiveEditor = binding.evermindEditor;
                                    ((MainActivity) context).getEverBallsHelper().metaColorForeGroundColor = context.getColor(R.color.White);
                                    ((MainActivity) context).getEverBallsHelper().metaColorHighlightColor = context.getColor(R.color.White);

                                    ((MainActivity) context).registerEditor(binding.evermindEditor, true);
                                    ((MainActivity) context).getNoteCreator().setActiveEditor(ActiveEditor);
                                    ((MainActivity) context).getNoteCreator().setActiveEditorPosition(getLayoutPosition());
                                    ActiveEditorPosition = getLayoutPosition();
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
                                        ((MainActivity) context).getActualNote().getContents().set(ActiveEditorPosition, text);
                                        getItem().setContent(text);

                                    }).start();
                                }
                            };
                        }

                        binding.evermindEditor.setVisibility(View.VISIBLE);
                        binding.evermindEditor.removeTextChangedListener(watcher);
                        binding.evermindEditor.setRichTextEditing(true, text);
                        lastTextInEditor = text;


                        new Handler(Looper.getMainLooper()).postDelayed(() -> binding.evermindEditor.addTextChangedListener(watcher), 100);
                    }
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        void setDrawContent() {
            String drawPath = getItem().getDrawLocation();
            if (!lastPathDraw.equals(drawPath)) {
                if (new File(drawPath).exists()) {

                    setupDrawView(false);

                    Bitmap bitmap = BitmapFactory.decodeFile(drawPath);
                    ViewGroup.LayoutParams params = binding.recyclerImageView.getLayoutParams();
                    params.height = bitmap.getHeight();
                    params.width = bitmap.getWidth();
                    binding.recyclerImageView.setLayoutParams(params);
                    bitmap.recycle();

                    Glide.with(binding.recyclerImageView).load(drawPath).into(binding.recyclerImageView);
                    //  Ion.with(everbinding.recyclerImageView).load(getItem().getDrawLocation());

                } else {
                    if (!drawPath.equals("▓")) {
                        setupDrawView(true);
                    } else {
                        binding.swipeDraw.setVisibility(View.GONE);
                    }
                }
            }
        }

        void setAudio() {
            String recordPath = getItem().getRecord();
            if (!lastRecordPath.equals(recordPath)) {

                if (new File(recordPath).exists()) {

                    if (lastRecordPath.equals("null")) {
                        mediaPlayer = new MediaPlayer();
                        mini_visualizerHandler = EverDbmHandler.Factory.newVisualizerHandler(context, mediaPlayer);
                        binding.everWave2.setMMaxValue(100);
                        binding.everWave2.setWaveBackgroundColor(mainActivity.get().getColor(R.color.Grey));
                        binding.everWave2.setWaveProgressColor(mainActivity.get().getColor(R.color.SkyBlue));
                        binding.everWave2.setWaveGravity(WaveGravity.CENTER);
                        mini_visualizerHandler.setInnerOnPreparedListener(mp -> {
                            prepared = true;
                            binding.everWave2.setOnProgressChanged((waveformSeekBar, i, b) -> {
                                if (b) {
                                    if (i >= 0) {
                                        toggleAudio(mp, 0);
                                        int time = mp.getDuration()/110*i;
                                        toggleAudio(mp, time);

                                    }
                                }
                            });
                            toggleAudio(mp, 0);
                        });
                        mini_visualizerHandler.setInnerOnCompletionListener(mp -> {
                            binding.AudioViewButton.setImageResource(R.drawable.aar_ic_play);
                            new Handler(Looper.getMainLooper()).postDelayed(() -> visualizationView.onPause(), 1000);
                        });
                        binding.AudioViewButton.setOnClickListener(v -> {
                            if (prepared) {
                                toggleAudio(mediaPlayer, 0);
                            } else {
                                mediaPlayer.prepareAsync();
                            }
                        });

                        binding.deleteRecordRecycler.setOnClickListener(v ->
                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    if (ActiveEditor != null) {
                                        if (ActiveEditor != null) {
                                            ActiveEditor.clearFocus();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        ((MainActivity) context).getNoteCreator().onLongPress(getLayoutPosition(), false);
                                    }
                                }, 150));
                        PushDownAnim.setPushDownAnimTo(binding.AudioViewButton).setScale(PushDownAnim.MODE_SCALE, 0.7f);
                        PushDownAnim.setPushDownAnimTo(binding.deleteRecordRecycler).setScale(PushDownAnim.MODE_SCALE, 0.7f);
                    }
                        try {
                            if (mediaPlayer.getDuration() != -1) {
                                mediaPlayer.reset();
                            }
                            mediaPlayer.setDataSource(recordPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        lastRecordPath = recordPath;
                    binding.swipeRecord.setVisibility(View.VISIBLE);
                        addVisualizer(recordPath, Integer.parseInt(((MainActivity) context).getActualNote().getNoteColor()));
                    lastRecordPath = recordPath;
                } else {
                    binding.swipeRecord.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View view) {
            InputMethodManager keyboard = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyboard.isActive()) {
                keyboard.hideSoftInputFromWindow(binding.recyclerImageView.getWindowToken(), 0);
            }
            FinalYHeight = 0;
            SelectedDrawPosition = getAdapterPosition();

            ((MainActivity) context).getNoteCreator().onDrawClick(binding.everDraw, SelectedDrawPosition, getItem().getDrawLocation(), binding.cardDrawRecycler);

          }

          @SuppressLint("ClickableViewAccessibility")
          void setupDrawView(boolean empty) {

              if (lastPathDraw.equals("null")) {
                  binding.everDraw.setOnTouchListener((view, motionEvent) -> {
                      ((MainActivity) context).getNoteCreator().getTextAndDrawRecyclerView().suppressLayout(true);
                      binding.swipeDraw.setSwipeEnabled(false);

                      int y = (int) motionEvent.getY();

                      if (y >= FinalYHeight) {
                          FinalYHeight = y;
                          ((MainActivity) context).getNoteCreator().setFinalYHeight(y);
                      }

                      if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                          ((MainActivity) context).getNoteCreator().getTextAndDrawRecyclerView().suppressLayout(false);
                          if (y >= binding.everDraw.getHeight() - 75) {
                              TransitionManager.beginDelayedTransition(((MainActivity) context).getCardNoteCreator(), new TransitionSet()
                                      .addTransition(new ChangeBounds()));
                              ViewGroup.LayoutParams params1 = binding.cardDrawRecycler.getLayoutParams();

                              params1.height = FinalYHeight + 100;
                              binding.cardDrawRecycler.setLayoutParams(params1);


                              new Handler(Looper.getMainLooper()).postDelayed(() -> binding.swipeDraw.setSwipeEnabled(true), 150);

                              return false;
                          } else {
                              new Handler(Looper.getMainLooper()).postDelayed(() -> binding.swipeDraw.setSwipeEnabled(true), 150);
                          }
                      }
                      return false;
                  });
                  binding.swipeDraw.setShowMode(SwipeLayout.ShowMode.PullOut);
                  binding.swipeDraw.setClickToClose(true);
                  binding.recyclerImageView.setOnClickListener(this);
                  binding.deleteDrawRecycler.setOnClickListener(v ->
                          new Handler(Looper.getMainLooper()).postDelayed(() -> {
                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                  ((MainActivity) context).getNoteCreator().onLongPress(getAdapterPosition(), true);
                              }
                          }, 150));

                  PushDownAnim.setPushDownAnimTo(binding.deleteDrawRecycler).setScale(PushDownAnim.MODE_SCALE, 0.7f);

                  PushDownAnim.setPushDownAnimTo(binding.recyclerImageView).setScale(PushDownAnim.MODE_SCALE, 0.7f);
              }

              binding.swipeDraw.setVisibility(View.VISIBLE);
              binding.recyclerImageView.setVisibility(View.VISIBLE);
              binding.swipeDraw.setSwipeEnabled(true);
              ViewGroup.LayoutParams params = binding.cardDrawRecycler.getLayoutParams();

              params.height = WRAP_CONTENT;

              binding.cardDrawRecycler.setLayoutParams(params);

              binding.everDraw.setVisibility(View.GONE);

              ((MainActivity) context).getNoteCreator().setEverDraw(binding.everDraw);


              if (empty) {
                  binding.swipeDraw.setSwipeEnabled(false);

                  binding.recyclerImageView.setVisibility(View.GONE);
                  binding.everDraw.setVisibility(View.VISIBLE);
                  ((MainActivity) context).getEverViewManagement().beginDelayedTransition(binding.cardDrawRecycler);
                  ViewGroup.LayoutParams params1 = binding.cardDrawRecycler.getLayoutParams();

                  params1.height = 1000;

                  binding.cardDrawRecycler.setLayoutParams(params1);
                  FinalYHeight = 0;
                  SelectedDrawPosition = getAdapterPosition();
                  ((MainActivity) context).getNoteCreator().onDrawClick(binding.everDraw, SelectedDrawPosition, getItem().getDrawLocation(), binding.cardDrawRecycler);

              }
              lastPathDraw = getItem().getDrawLocation();
          }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }

        @Override
        public void open() {
            ((MainActivity)context).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, 0);
        }

        @Override
        public void close() {
            ((MainActivity)context).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, imageDecoySize);
        }

        @Override
        public void changeColor(int color) {
            ((MainActivity)context).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, imageDecoySize);
            ((MainActivity)context).getEverThemeHelper().tintView(binding.imageDecoy, color, 0);
            if (!getItem().getRecord().isEmpty()) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> addVisualizer(getItem().getRecord(), color), 500);
            }
        }

        private void addVisualizer(String audioPath, int color) {
            visualizationView = new EverGLAudioVisualizationView.Builder(itemView.getContext())
                    .setLayersCount(1)
                    .setWavesCount(5)
                    .setWavesHeight(1200f)
                    .setWavesFooterHeight(550f)
                    .setBubblesPerLayer(25)
                    .setBubblesSize(35f)
                    .setBubblesRandomizeSize(true)
                    .setBackgroundColor(Util.getDarkerColor(color))
                    .setLayerColors(new int[]{color})
                    .build();
            visualizationView.linkTo(mini_visualizerHandler);
            visualizationView.onPause();

            binding.everWave2.setSampleFrom(audioPath, false);

            binding.cardEverPlayer.addView(visualizationView, 0);

            EverInterfaceHelper.getInstance().setListener(this);
            EverInterfaceHelper.getInstance().setColorListener(this);

            int colorBlue = mainActivity.get().getColor(R.color.SkyBlue);
            if (color != colorBlue) {
                mainActivity.get().getEverThemeHelper().tintView(binding.everWave2, colorBlue, 1000);
            } else {
                mainActivity.get().getEverThemeHelper().tintView(binding.everWave2, mainActivity.get().getColor(R.color.White), 1000);
            }

            binding.imageDecoy.setBackgroundColor(Integer.parseInt(((MainActivity)context).getActualNote().getNoteColor()));
            binding.swipeRecord.setVisibility(View.VISIBLE);

            new Handler(Looper.myLooper()).postDelayed(() -> {

                visualizationView.onResume();

                new Handler(Looper.myLooper()).postDelayed(() -> {

                    imageDecoySize = binding.imageDecoy.getHeight();

                    ((MainActivity)context).getEverViewManagement().animateHeightChange(binding.imageDecoy, 500, 0);

                    visualizationView.onPause();

                }, 150);
            }, 1000);
        }

        private void toggleAudio(MediaPlayer mp, int actualTime) {
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

        private boolean isPlaying() { return mediaPlayer.isPlaying(); }

        private int getAudioTime() {
            if (va != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return Math.toIntExact(va.getCurrentPlayTime()*100);
                } else {
                    return (int) va.getCurrentPlayTime()*100;
                }
            } else {
                return 0;
            }
        }
    }

    public boolean writeToFileFromContentUri(File file, Uri uri) {
        if (file == null || uri == null) return false;
        try {
            InputStream stream = context.getContentResolver().openInputStream(uri);
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