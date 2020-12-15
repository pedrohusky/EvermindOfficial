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
import androidx.core.content.ContextCompat;

import com.daimajia.swipe.SwipeLayout;
import com.example.Evermind.EverAudioVisualizerHandlers.CloseAudioVisualizationHelper;
import com.example.Evermind.EverAudioVisualizerHandlers.EverDbmHandler;
import com.example.Evermind.EverAudioVisualizerHandlers.EverGLAudioVisualizationView;
import com.example.Evermind.EverAudioVisualizerHandlers.EverVisualizerDbmHandler;
import com.example.Evermind.EverAudioVisualizerHandlers.EverVisualizerHandler;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTFormat;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTImage;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.koushikdutta.ion.Ion;
import com.masoudss.lib.Utils;
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
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;
import omrecorder.AudioChunk;
import omrecorder.PullTransport;

import static com.example.Evermind.TESTEDITOR.rteditor.media.crop.RotateBitmap.TAG;

public class NoteContentsBinder extends ItemBinder<EverLinkedMap, NoteContentsBinder.NoteCreatorHolder> {

    private final ArrayList<String> array = new ArrayList<>();
    private final Context context;
    public int SelectedDrawPosition;
    private WeakReference<RTEditText> ActiveEditor;
    private List<EverLinkedMap> arrayToAdd;
    private int FinalYHeight;
    private WeakReference<RTEditText> everEditor;
    private WeakReference<ImageView> everDrawImage;
    private WeakReference<CardView> cardDrawRecycler;
    private WeakReference<CardView> cardEditorRecycler;
    private WeakReference<SwipeLayout> swipe;
    private int old;
    private int newline;


    public NoteContentsBinder(Context context) {
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public NoteCreatorHolder createViewHolder(ViewGroup parent) {
        return new NoteCreatorHolder(inflate(parent, R.layout.aa));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof EverLinkedMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bindViewHolder(NoteCreatorHolder holder, EverLinkedMap everMap) {
        swipe = new WeakReference<>(holder.itemView.findViewById(R.id.testSwipe));
        everEditor = new WeakReference<>(holder.itemView.findViewById(R.id.evermindEditor));
        everEditor.get().setPaddingRelative(8, 8, 8, 8);
        swipe = new WeakReference<>(holder.itemView.findViewById(R.id.testSwipe));

        ((MainActivity) context).registerEditor(everEditor.get(), true);
        holder.setContentHTML(everMap.getContent());
        holder.setDrawContent(everMap.getDrawLocation());
        holder.setAudio(everMap.getRecord());

        swipe.get().setShowMode(SwipeLayout.ShowMode.PullOut);
        swipe.get().setClickToClose(true);



        if (holder.getAdapterPosition() == ((MainActivity)context).actualNote.get().everLinkedContents.size()-1) {
            ((MainActivity) context).noteCreator.get().startPostponeTransition();
        }
    }

    @Override
    public int getSpanSize(int maxSpanCount) {
        return super.getSpanSize(1);
    }



    class NoteCreatorHolder extends ItemViewHolder<EverLinkedMap> implements View.OnClickListener, View.OnLongClickListener, PullTransport.OnAudioChunkPulledListener, CloseAudioVisualizationHelper.OnOpenAudioStateListener, CloseAudioVisualizationHelper.OnChangeColorListener {

        private EverVisualizerHandler visualizerHandler;
        private EverVisualizerDbmHandler mini_visualizerHandler;
        private MediaPlayer mediaPlayer;
        private EverWave waveSeek;
        private ImageButton playAudio;
        private boolean isPlaying = false;
        private boolean prepared = false;
       private ValueAnimator va;
        private int ActiveEditorPosition;
        private int imageDecoySize;
        private String audioPath;
        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("ClickableViewAccessibility")
        public NoteCreatorHolder(View itemView) {
            super(itemView);
            everEditor = new WeakReference<>(itemView.findViewById(R.id.evermindEditor));
            everDrawImage = new WeakReference<>(itemView.findViewById(R.id.recycler_imageView));
            cardDrawRecycler = new WeakReference<>(itemView.findViewById(R.id.cardDrawRecycler));
            cardEditorRecycler = new WeakReference<>(itemView.findViewById(R.id.cardEditorRecycler));
            swipe = new WeakReference<>(itemView.findViewById(R.id.testSwipe));
            ImageButton delete = itemView.findViewById(R.id.deleteFromRecycler);
            playAudio = itemView.findViewById(R.id.AudioViewButton);
            everEditor.get().setTextSize(22);


            delete.setOnClickListener(v ->
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ((MainActivity)context).noteCreator.get().everCreatorHelper.onLongPress(getAdapterPosition());
                }
            }, 150));

            playAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        //  String record = ((MainActivity)context).noteCreator.get().everCreatorHelper.list.get(holder.getAdapterPosition()).getRecord();
                        if (prepared) {
                            toggleAudio(mediaPlayer);
                        } else {

                            mediaPlayer.prepareAsync();
                        }
                    }
            });

            PushDownAnim.setPushDownAnimTo(delete).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(playAudio).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(everDrawImage.get()).setScale(PushDownAnim.MODE_SCALE, 0.7f);


            everDrawImage.get().setOnClickListener(this);
            //itemView.setOnClickListener(this);

            itemView.findViewById(R.id.everDraw).setOnTouchListener((view, motionEvent) -> {
                EverDraw e = itemView.findViewById(R.id.everDraw);
                cardDrawRecycler = new WeakReference<>(itemView.findViewById(R.id.cardDrawRecycler));
                ((MainActivity) context).noteCreator.get().everCreatorHelper.textDrawRecycler.get().suppressLayout(true);
                ((SwipeLayout)itemView.findViewById(R.id.testSwipe)).setSwipeEnabled(false);

                int y = (int) motionEvent.getY();

                if (y >= FinalYHeight) {
                    FinalYHeight = y;
                    ((MainActivity) context).noteCreator.get().everCreatorHelper.FinalYHeight = y;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (y >= e.getHeight() - 75) {
                        ((MainActivity) context).noteCreator.get().everCreatorHelper.textDrawRecycler.get().suppressLayout(false);
                        TransitionManager.beginDelayedTransition(((MainActivity) context).cardNoteCreator.get(), new TransitionSet()
                                .addTransition(new ChangeBounds()));
                        System.out.println(y);
                            ViewGroup.LayoutParams params1 = cardDrawRecycler.get().getLayoutParams();

                            params1.height = FinalYHeight + 100;
                            cardDrawRecycler.get().setLayoutParams(params1);

                        ViewGroup.LayoutParams params = e.getLayoutParams();

                        params.height =  FinalYHeight + 100;

                        e.setLayoutParams(params);


                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                ((SwipeLayout)itemView.findViewById(R.id.testSwipe)).setSwipeEnabled(true);
                                ((MainActivity) context).noteCreator.get().everCreatorHelper.textDrawRecycler.get().suppressLayout(true);
                            }, 150);

                        return false;
                    }
                } else {
                    ViewGroup.LayoutParams params1 = cardDrawRecycler.get().getLayoutParams();

                    params1.height = e.getHeight();
                    params1.width = e.getWidth();
                    cardDrawRecycler.get().setLayoutParams(params1);
                }
                return false;
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML(String contentHTML) {
            everEditor = new WeakReference<>(itemView.findViewById(R.id.evermindEditor));
            cardEditorRecycler = new WeakReference<>(itemView.findViewById(R.id.cardEditorRecycler));
            int size = ((MainActivity) context).noteCreator.get().everCreatorHelper.everLinkedContents.size();
            if (contentHTML.equals("▓")) {
                cardEditorRecycler.get().setVisibility(View.GONE);
                everEditor.get().setVisibility(View.GONE);
                System.out.println("Position = " + getLayoutPosition() + " GONE1" + "content os =" + contentHTML);
            } else if (contentHTML.equals("") && getLayoutPosition() != size -1) {
                cardEditorRecycler.get().setVisibility(View.GONE);
                everEditor.get().setVisibility(View.GONE);
                System.out.println("Position = " + getLayoutPosition() + " GONE2  " + size);

            }else {

                cardEditorRecycler.get().setVisibility(View.VISIBLE);
                everEditor.get().setRichTextEditing(true, contentHTML);
               everEditor.get().setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    ((MainActivity)context).noteCreator.get().everCreatorHelper.cardWidth = cardEditorRecycler.get().getWidth();
                    ((MainActivity)context).noteCreator.get().everCreatorHelper.cardHeight = cardEditorRecycler.get().getHeight();
                }, 50);

            }


            everEditor.get().setOnRichContentListener((contentUri, description) -> {
                if (description.getMimeTypeCount() > 0 && contentUri != null) {
                    final String fileExtension = MimeTypeMap.getSingleton()
                            .getExtensionFromMimeType(description.getMimeType(0));
                    final String filename = System.currentTimeMillis() + "." + fileExtension;
                    File richContentFile = new File(context.getFilesDir(), filename);
                    if (!writeToFileFromContentUri(richContentFile, contentUri)) {
                        System.out.println("Cant Write File");
                    }

                    ((MainActivity)context).mRTManager.insertImage(ActiveEditor.get(), new RTImage() {
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

            everEditor.get().setOnFocusChangeListener((view, b) -> {
                everEditor = new WeakReference<>(itemView.findViewById(R.id.evermindEditor));
                ActiveEditor = everEditor;
                ((MainActivity)context).everBallsHelper.metaColorForeGroundColor = context.getColor(R.color.White);
                ((MainActivity)context).everBallsHelper.metaColorHighlightColor = context.getColor(R.color.White);

                ((MainActivity) context).registerEditor(everEditor.get(), true);
                ((MainActivity) context).noteCreator.get().everCreatorHelper.activeEditor = ActiveEditor;
                ((MainActivity) context).noteCreator.get().everCreatorHelper.activeEditorPosition = getLayoutPosition();
                ActiveEditorPosition = getLayoutPosition();
                ((MainActivity) context).OnFocusChangeEditor(b);
            });

           // everEditor.get().setOnInitialLoadListener(isReady -> {
             //   if (isReady) {
              //      if (getLayoutPosition() == Size-1 ) {
                       ///////////////////////////// ((MainActivity)context).beginDelayedTransition(((MainActivity)context).findViewById(R.id.card_note_creator));
                //        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            //   if (!((MainActivity) context).noteCreator.get().everCreatorHelper.hasImages) {

                   //         Toast.makeText(context, "p = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
                    //        ((MainActivity) context).noteCreator.get().everCreatorHelper.startPostponeTransition();
                            //   }


                      //  }, 300);

                    //}
                //}
           // });
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                everEditor = new WeakReference<>(itemView.findViewById(R.id.evermindEditor));
                everEditor.get().addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        old = everEditor.get().getCurrentCursorLine();

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {



                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        newline = everEditor.get().getCurrentCursorLine();
                        if (newline > old) {
                        //    ((MainActivity)context).beginDelayedTransition(((MainActivity)context).noteCreator.get().everCreatorHelper.cardView.get());
                            new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity)context).noteCreator.get().everCreatorHelper.scrollView.get().smoothScrollBy(0, everEditor.get().getLineHeight()+20), 5);
                        } else if (newline < old){
                        //    ((MainActivity)context).beginDelayedTransition(((MainActivity)context).noteCreator.get().everCreatorHelper.cardView.get());
                            new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity)context).noteCreator.get().everCreatorHelper.scrollView.get().smoothScrollBy(0, -everEditor.get().getLineHeight()+20), 5);
                        }
                        updateContents(getItem());
                    }
                });
            }, 100);

            //everEditor.get().setOnTextChangeListener(text -> updateContents());
        }

        void setDrawContent(String draw) {
            everDrawImage = new WeakReference<>(itemView.findViewById(R.id.recycler_imageView));
            cardDrawRecycler = new WeakReference<>(itemView.findViewById(R.id.cardDrawRecycler));
            swipe = new WeakReference<>(itemView.findViewById(R.id.testSwipe));

            if (new File(draw).exists()) {
                itemView.findViewById(R.id.everDraw).setVisibility(View.GONE);
                swipe.get().setVisibility(View.VISIBLE);
                cardDrawRecycler.get().setVisibility(View.VISIBLE);
                everDrawImage.get().setVisibility(View.VISIBLE);

                if (cardDrawRecycler.get().getHeight() > 0) {
                    ViewGroup.LayoutParams params = everDrawImage.get().getLayoutParams();
                    params.height = cardDrawRecycler.get().getHeight();
                    params.width = cardDrawRecycler.get().getWidth();

                    everDrawImage.get().setLayoutParams(params);
                } else  {
                    Bitmap bitmap = BitmapFactory.decodeFile(draw);
                    ViewGroup.LayoutParams params = everDrawImage.get().getLayoutParams();
                    params.height = bitmap.getHeight();
                    params.width = bitmap.getWidth();
                    everDrawImage.get().setLayoutParams(params);
                    bitmap.recycle();
                }

            //    Glide.with(context)
            //            .asBitmap()
            //            .skipMemoryCache(true)
            //            .diskCacheStrategy(DiskCacheStrategy.NONE)
            //            .transition(GenericTransitionOptions.with(R.anim.grid_new_item_anim))
            //            .error(R.drawable.ic_baseline_clear_24)
            //            .load(draw)
            //            .into(everDrawImage.get());
                Ion.with(everDrawImage.get())
                        .error(R.drawable.ic_baseline_clear_24)
                        .animateLoad(R.anim.grid_new_item_anim)
                        .animateIn(R.anim.grid_new_item_anim)
                        .load(draw);


            } else {
                swipe.get().setVisibility(View.GONE);
                cardDrawRecycler.get().setVisibility(View.GONE);
                everDrawImage.get().setVisibility(View.GONE);
            }
        }

        void setAudio(String audioPath) {
            if (new File(audioPath).exists()) {
                playAudio = itemView.findViewById(R.id.AudioViewButton);

                mediaPlayer = new MediaPlayer();
               // mediaPlayer.setOnPreparedListener(mp -> {
              //      prepared = true;
              //      toggleAudio( mp, itemView);
               //     mp.setOnCompletionListener(mp12 -> {
               //         isPlaying = false;
               //         playAudio.setImageResource(R.drawable.aar_ic_play);
               //     });
               // });
                try {
                    mediaPlayer.setDataSource(((MainActivity)context).noteCreator.get().everCreatorHelper.everLinkedContents.get(getAdapterPosition()).getRecord());
                    System.out.println("Data source set = " + ((MainActivity)context).noteCreator.get().everCreatorHelper.everLinkedContents.get(getAdapterPosition()).getRecord());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.audioPath = audioPath;
               addVisualizer(audioPath, Integer.parseInt(((MainActivity) context).actualNote.get().getNoteColor()));

            } else {

                itemView.findViewById(R.id.card_everPlayer).setVisibility(View.GONE);

            }


        }

        void startAudio(int audioDuration) {

            int percent = 110;
            va = ValueAnimator.ofFloat(0, percent);
            va.setInterpolator(new LinearInterpolator());
            int result = audioDuration * percent / 100;
            va.setDuration(result);
            va.addUpdateListener(animation -> {
                System.out.println(animation.getAnimatedValue().toString());
                waveSeek.setProgress(Math.round((Float) animation.getAnimatedValue()));
            });
            playAudio.setImageResource(R.drawable.aar_ic_pause);
            mediaPlayer.start();
            mini_visualizerHandler.onResume();
            va.start();

        }

        void resumeAudio() {
            playAudio.setImageResource(R.drawable.aar_ic_pause);
            mediaPlayer.start();
            mini_visualizerHandler.onResume();
            va.resume();
        }

        void toggleAudio(MediaPlayer mp) {
            if (isPlaying()) {
               pauseAudio(mp);
            } else {
                if (getAudioTime() > 0) {
                    resumeAudio();
                } else {
                    startAudio(mp.getDuration());
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void updateContents(EverLinkedMap note) {
            new Thread(() -> {
                array.clear();
                arrayToAdd = ((MainActivity) context).noteCreator.get().everCreatorHelper.everLinkedContents.getData();
                for (EverLinkedMap strg : arrayToAdd) {
                    if (strg.getContent().endsWith("┼")) {
                        array.add(strg.getContent());
                    } else {
                        array.add(strg.getContent() + "┼");
                    }
                }
                //   arrayToAdd = null;
                String text;
                if (ActiveEditor != null) {
                    if (ActiveEditor.get() != null) {
                        text = ActiveEditor.get().getText(RTFormat.HTML);
                        if (ActiveEditorPosition > array.size()) {
                            array.add(text + "┼");
                        } else {
                            if (text.endsWith("<br>")) {
                                array.set(ActiveEditorPosition, text.substring(0, text.length() - 4) + "┼");
                            } else {
                                array.set(ActiveEditorPosition, text + "┼");
                            }
                        }

                        String toDatabase = String.join("", array);
                        note.setContent(text);
                       ((MainActivity) context).actualNote.get().setContent(toDatabase);
                    }
                }
            }).start();
        }

        private void pauseAudio(MediaPlayer mp) {
            va.pause();
            mp.pause();
            mini_visualizerHandler.onPause();
            playAudio.setImageResource(R.drawable.aar_ic_play);
            //TODO FNISH THIS AND MAKE SEEK WORKS
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

        @Override
        public void onClick(View view) {



            InputMethodManager keyboard = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyboard.isActive()) {
                keyboard.hideSoftInputFromWindow(everDrawImage.get().getWindowToken(), 0);
            }


            new Handler(Looper.getMainLooper()).post(() -> {
                FinalYHeight = 0;
//TODO FIX DRAW NOT EDITING AFTER THE FIRST ONE
                SelectedDrawPosition = getLayoutPosition();

                ImageView imageView = itemView.findViewById(R.id.recycler_imageView);
                EverDraw draw = itemView.findViewById(R.id.everDraw);
                cardDrawRecycler = new WeakReference<>(itemView.findViewById(R.id.cardDrawRecycler));
                draw.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = draw.getLayoutParams();
                params.height = imageView.getHeight();

                System.out.println("height = " + imageView.getHeight());
                ((MainActivity)context).beginDelayedTransition(cardDrawRecycler.get());

                draw.setLayoutParams(params);

                ((MainActivity) context).noteCreator.get().everCreatorHelper.onDrawClick(draw, SelectedDrawPosition, getItem().getDrawLocation(), cardDrawRecycler.get());

            });

          }

          void addVisualizer(String audioPath, int color) {
              CardView layout = itemView.findViewById(R.id.card_visualizer);
              CardView layout1 = itemView.findViewById(R.id.card_everPlayer);
              EverGLAudioVisualizationView visualizer = new EverGLAudioVisualizationView.Builder(itemView.getContext())
                      .setLayersCount(1)
                      .setWavesCount(6)
                      .setWavesHeight(1200f)
                      .setWavesFooterHeight(550f)
                      .setBubblesPerLayer(15)
                      .setBubblesSize(35f)
                      .setBubblesRandomizeSize(true)
                      .setBackgroundColor(Util.getDarkerColor(color))
                      .setLayerColors(new int[]{color})
                      .build();

              mini_visualizerHandler = EverDbmHandler.Factory.newVisualizerHandler(context, mediaPlayer);
              visualizer.linkTo(mini_visualizerHandler);
              mini_visualizerHandler.setInnerOnPreparedListener(mp -> {
                  prepared = true;
                  waveSeek.setOnProgressChanged((waveformSeekBar, i, b) -> {
                      if (b) {
                          if (i >= 0) {
                              pauseAudio(mediaPlayer);
                              int time = mp.getDuration()/110*i;
                              mediaPlayer.seekTo(time);
                              va.start();
                              va.setCurrentPlayTime(time);
                              resumeAudio();

                          }
                      }
                  });
                  toggleAudio( mp);

              });
              mini_visualizerHandler.setInnerOnCompletionListener(mp -> {
                  isPlaying = false;
                  playAudio.setImageResource(R.drawable.aar_ic_play);
              });

              //  visualizer.stopRendering();
              waveSeek = new EverWave(context);
              waveSeek.setMMaxValue(100);
              waveSeek.setWaveWidth(Utils.INSTANCE.dp(context,3));
              waveSeek.setWaveGap(Utils.INSTANCE.dp(context,2));
              waveSeek.setWaveMinHeight(Utils.INSTANCE.dp(context,5));
              waveSeek.setWaveCornerRadius(Utils.INSTANCE.dp(context,2));
              waveSeek.setWaveGravity(WaveGravity.CENTER);
              waveSeek.setWaveBackgroundColor(ContextCompat.getColor(context, R.color.Grey));
              waveSeek.setWaveProgressColor(ContextCompat.getColor(context,R.color.SkyBlue));
              waveSeek.setSampleFrom(audioPath, false);

              CloseAudioVisualizationHelper.getInstance().setListener(this);
              CloseAudioVisualizationHelper.getInstance().setColorListener(this);

              layout1.addView(visualizer, 0);
              layout.addView(waveSeek);
              mini_visualizerHandler.onPause();
              itemView.findViewById(R.id.card_everPlayer).setVisibility(View.VISIBLE);
              ImageView imageDecoy = itemView.findViewById(R.id.imageDecoy);
              imageDecoy.setBackgroundColor(Integer.parseInt(((MainActivity)context).actualNote.get().getNoteColor()));
              new Handler(Looper.getMainLooper()).postDelayed(() -> {
                  imageDecoySize = imageDecoy.getHeight();
                  ((MainActivity)context).animateHeightChange(imageDecoy, 500, 0);
              }, 1000);
          }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }

        @Override
        public void onAudioChunkPulled(AudioChunk audioChunk) {
            float amplitude = isPlaying ? (float) audioChunk.maxAmplitude() : 0f;
            visualizerHandler.onDataReceived(amplitude);
            System.out.println("1212121212212");
        }

        @Override
        public void open() {
            ((MainActivity)context).animateHeightChange(itemView.findViewById(R.id.imageDecoy), 500, 0);
        }

        @Override
        public void close() {
            ((MainActivity)context).animateHeightChange(itemView.findViewById(R.id.imageDecoy), 500, imageDecoySize);
        }

        @Override
        public void changeColor(int color) {
            ((MainActivity)context).animateHeightChange(itemView.findViewById(R.id.imageDecoy), 500, imageDecoySize);
            ((MainActivity)context).everThemeHelper.tintView(itemView.findViewById(R.id.imageDecoy), color);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                addVisualizer(audioPath, color);
            }, 500);
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