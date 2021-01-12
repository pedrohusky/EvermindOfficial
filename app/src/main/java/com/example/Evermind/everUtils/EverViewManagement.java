package com.example.Evermind.everUtils;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.TESTEDITOR.toolbar.HorizontalRTToolbar;
import com.example.Evermind.TESTEDITOR.toolbar.RTToolbarImageButton;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.SmoothBottomBar;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EverViewManagement {
    private final Context context;
    private final ImageButton Undo;
    private final ImageButton Redo;
    private final ImageButton Delete;
    private final ImageButton Save;
    private final ImageButton changeNoteColorButton;
    private final CardView size_visualizer;
    private final ImageView ImageSizeView;
    private final WeakReference<MainActivity> mainActivity;
    private final RelativeLayout relativeLayout;
    private final HorizontalRTToolbar editorToolbar;
    private final SmoothBottomBar note_bottom_bar;
    private SeekBar seekBarDrawSize;
    private RTToolbarImageButton Bold;
    private RTToolbarImageButton Italic;
    private RTToolbarImageButton Underline;
    private RTToolbarImageButton StrikeThrough;
    private RTToolbarImageButton Bullets;
    private RTToolbarImageButton Numbers;
    private RTToolbarImageButton AlignLeft;
    private RTToolbarImageButton AlignCenter;
    private RTToolbarImageButton AlignRight;
    private ImageButton DrawChangeColor;
    private ImageButton DrawChangeSize;
    private CardView ImporterOptions;
    private CardView ParagraphOptions;
    private CardView FormatOptions;
    private CardView SelectOptions;
    private CardView AudioOptions;
    private CardView DrawOptions;
    private int audioDecoySize = 0;
    private boolean bottomBarUp = false;
    private boolean formatOptions = false;
    private boolean paragraphOptions = false;
    private boolean importerOptions = false;
    private boolean drawOptions = false;
    private boolean drawsize = false;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean striketrough = false;
    private boolean numbers = false;
    private boolean bullets = false;
    private View oldView;
    private ImageButton stopView;
    private ImageButton saveView;
    private ImageButton playView;
    private TextView timerView;
    private String lastSearch;
    private boolean audioOpen = false;
    private boolean DrawVisualizerIsShowing = false;
    private boolean drawing = false;
    private ValueAnimator heightAnimator;
    private ValueAnimator widthAnimator;
    public EverViewManagement(Context context) {
        this.context = context;
        mainActivity = new WeakReference<>(((MainActivity) context));
        relativeLayout = mainActivity.get().findViewById(R.id.relativeLayout_Buttons);
        editorToolbar = new HorizontalRTToolbar(context);
        mainActivity.get().getmRTManager().registerToolbar(null, editorToolbar);
        note_bottom_bar = mainActivity.get().findViewById(R.id.bottom_bar);
        note_bottom_bar.setOnItemReselected(integer -> {
            switch (integer) {
                case 0:
                    if (formatOptions) {
                        CloseOrOpenFormatOptions(true);
                        formatOptions = false;
                    } else {
                        CloseOrOpenFormatOptions(false);
                        formatOptions = true;
                    }
                    break;

                case 1:
                    if (paragraphOptions) {
                        CloseOrOpenParagraphOptions(true);
                        paragraphOptions = false;
                    } else {
                        CloseOrOpenParagraphOptions(false);
                        paragraphOptions = true;
                    }
                    break;

                case 2:
                    if (importerOptions) {
                        CloseOrOpenImporterOptions(true);
                        importerOptions = false;
                    } else {
                        CloseOrOpenImporterOptions(false);
                        importerOptions = true;
                    }
                    break;

                case 3:
                    if (audioOpen) {
                        mainActivity.get().getAudioHelper().stop(true);
                    } else {
                        mainActivity.get().recordAudio();
                    }
                    break;

                case 4:
                    if (drawOptions) {
                        mainActivity.get().AddDraw(true);
                    } else {
                        InputMethodManager keyboard1 = (InputMethodManager) mainActivity.get().getSystemService(INPUT_METHOD_SERVICE);
                        keyboard1.hideSoftInputFromWindow(mainActivity.get().getNoteCreator().getCardNoteCreator().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> mainActivity.get().AddDraw(false), 250);
                    }
                    break;

                case 5:

                    break;
            }
            return null;
        });
        note_bottom_bar.setOnItemSelected(integer -> {
            switch (integer) {
                case 0:
                    if (formatOptions) {
                        CloseOrOpenFormatOptions(true);
                        formatOptions = false;
                    } else {
                        CloseOrOpenFormatOptions(false);
                        formatOptions = true;
                    }
                    break;

                case 1:
                    if (paragraphOptions) {
                        CloseOrOpenParagraphOptions(true);
                        paragraphOptions = false;
                    } else {
                        CloseOrOpenParagraphOptions(false);
                        paragraphOptions = true;
                    }
                    break;

                case 2:
                    if (importerOptions) {
                        CloseOrOpenImporterOptions(true);
                        importerOptions = false;
                    } else {
                        CloseOrOpenImporterOptions(false);
                        importerOptions = true;
                    }
                    break;

                case 3:

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(mainActivity.get(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);

                    } else {

                        mainActivity.get().recordAudio();

                    }
                    break;

                case 4:
                    if (drawOptions) {
                        mainActivity.get().AddDraw(true);
                    } else {
                        InputMethodManager keyboard1 = (InputMethodManager) mainActivity.get().getSystemService(INPUT_METHOD_SERVICE);
                        keyboard1.hideSoftInputFromWindow(mainActivity.get().getNoteCreator().getCardNoteCreator().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> mainActivity.get().AddDraw(false), 250);
                    }
                    break;

                case 5:

                    break;
            }
            return null;
        });

        size_visualizer = mainActivity.get().findViewById(R.id.draw_sizeVisualizerCardView);
        ImageSizeView = mainActivity.get().findViewById(R.id.draw_size_visualizer);

        Undo = mainActivity.get().findViewById(R.id.toolbar_undo);
        Redo = mainActivity.get().findViewById(R.id.toolbar_redo);
        Delete = mainActivity.get().findViewById(R.id.Delete);
        Save = mainActivity.get().findViewById(R.id.Save);
        //scrollView1 = findViewById(R.id.scroll_draw);
        changeNoteColorButton = mainActivity.get().findViewById(R.id.changeNotecolorButton);
        List<View> views = new ArrayList<>();
        views.add(Undo);
        views.add(Redo);
        views.add(Save);
        views.add(Delete);
        views.add(changeNoteColorButton);


        mainActivity.get().applyPushDownToViews(views, 0.7f);
    }

    public ImageButton getUndo() {
        return Undo;
    }

    public ImageButton getRedo() {
        return Redo;
    }

    public ImageButton getDelete() {
        return Delete;
    }

    public ImageButton getSave() {
        return Save;
    }

    public SmoothBottomBar getBottomBar() {
        return note_bottom_bar;
    }

    public ImageButton getStopView() {
        return stopView;
    }

    public ImageButton getSaveView() {
        return saveView;
    }

    public ImageButton getPlayView() {
        return playView;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isStriketrough() {
        return striketrough;
    }

    public void setStriketrough(boolean striketrough) {
        this.striketrough = striketrough;
    }

    public boolean isNumbers() {
        return numbers;
    }

    public boolean isBullets() {
        return bullets;
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    public void ShowDrawSizeVisualizer() {
        if (!DrawVisualizerIsShowing) {
            size_visualizer.setVisibility(View.VISIBLE);
            size_visualizer.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_formatter));
            ImageSizeView.setVisibility(View.VISIBLE);
            DrawVisualizerIsShowing = true;
        }
    }

    public void ModifyDrawSizeVisualizer(int value) {

        ImageSizeView.setScaleX(value / 85F);
        ImageSizeView.setScaleY(value / 85F);

    }

    public void CloseOrOpenToolbarUndoRedo(boolean UndoRedo) {
        if (UndoRedo) {
            changeNoteColorButton.setVisibility(View.GONE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Undo.setVisibility(View.VISIBLE);
                Redo.setVisibility(View.VISIBLE);
            }, 250);
        } else {
            Undo.setVisibility(View.GONE);
            Redo.setVisibility(View.GONE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!mainActivity.get().isAtHome()) {
                    changeNoteColorButton.setVisibility(View.VISIBLE);
                } else {
                    changeNoteColorButton.setVisibility(View.GONE);
                }
            }, 250);
        }
    }

    public void CloseOrOpenAudioOptions(boolean close) {

        if (close) {
            animateHeightChange(oldView.findViewById(R.id.imageAudioDecoy), 350, audioDecoySize);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                animateHeightChange(AudioOptions, 500, 0);
                //     animateWidthChange(AudioOptions, 500, 0);
                EverInterfaceHelper.getInstance().changeState(true);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    relativeLayout.removeView(AudioOptions);
                }, 650);
            }, 350);
            audioOpen = false;
        } else {

            if (paragraphOptions) {
                CloseOrOpenParagraphOptions(true);
                paragraphOptions = false;
            }
            if (importerOptions) {
                CloseOrOpenImporterOptions(true);
                importerOptions = false;
            }
            if (formatOptions) {
                CloseOrOpenFormatOptions(true);
                formatOptions = false;
            }
            if (drawOptions) {
                CloseOrOpenDrawOptions(true);
                drawOptions = false;
            }
            oldView = View.inflate(context, R.layout.audio_options_viewbuttons, null);
            AudioOptions = ((CardView) oldView);
            relativeLayout.addView(oldView);
            ViewGroup.LayoutParams layoutParams = AudioOptions.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = relativeLayout.getWidth();
            AudioOptions.setLayoutParams(layoutParams);
            oldView.findViewById(R.id.imageAudioDecoy).setBackgroundColor(Integer.parseInt(mainActivity.get().getActualNote().getNoteColor()));

            EverInterfaceHelper.getInstance().changeState(false);
            //  new Handler(Looper.getMainLooper()).postDelayed(() -> {
            animateHeightChange(AudioOptions, 500, 350);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                ImageView imageView = oldView.findViewById(R.id.imageAudioDecoy);
                if (imageView != null)
                audioDecoySize = imageView.getHeight();
                animateHeightChange(imageView, 350, 0);
            }, 600);
            //  }, 500);


            audioOpen = true;

            timerView = AudioOptions.findViewById(R.id.audio_time);
            playView = AudioOptions.findViewById(R.id.play);
            stopView = AudioOptions.findViewById(R.id.stop);
            saveView = AudioOptions.findViewById(R.id.saveAudio);


            timerView.setTranslationX(relativeLayout.getWidth() / 2.65f);
        }
    }

    public void switchToolbars(boolean show, boolean bottomToolbar, boolean showUndoRedo) {

        if (show) {
            if (bottomToolbar) {
                if (!bottomBarUp) {
                    animateHeightChange(note_bottom_bar, 600, 150);
                    //   note_bottom_bar.setVisibility(View.VISIBLE);
                    bottomBarUp = true;
                }
            } else {
                //  note_bottom_bar.setVisibility(View.GONE);
                animateHeightChange(note_bottom_bar, 600, 0);

                bottomBarUp = false;
            }

            CloseOrOpenToolbarUndoRedo(showUndoRedo);

        } else {
            // note_bottom_bar.setVisibility(View.GONE);
            animateHeightChange(note_bottom_bar, 600, 0);
            CloseOrOpenToolbarUndoRedo(false);
            bottomBarUp = false;
        }
    }

    public void animateHeightChange(View view, int duration, int amount) {
        //   new Handler(Looper.getMainLooper()).post(() -> {
         heightAnimator = ValueAnimator.ofInt(view.getMeasuredHeight(), amount);
        heightAnimator.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
        });
        heightAnimator.setDuration(duration);
        heightAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        heightAnimator.start();
        //   });
    }

    public void animateWidthChange(View view, int duration, int amount) {
        //    new Handler(Looper.getMainLooper()).post(() -> {
        widthAnimator = ValueAnimator.ofInt(view.getMeasuredWidth(), amount);
        widthAnimator.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = val;
            view.setLayoutParams(layoutParams);
        });
        widthAnimator.setDuration(duration);
        widthAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        widthAnimator.start();
        //   });
    }

    public void CloseOrOpenDrawSize(boolean CloseOpenedDrawSize) {

        if (CloseOpenedDrawSize) {

            DrawChangeColor.setVisibility(View.VISIBLE);
            DrawChangeSize.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // ChangeColor.setVisibility(View.GONE);

                seekBarDrawSize.setVisibility(View.GONE);

            }, 100);

        } else {

            DrawChangeColor.setVisibility(View.GONE);
            DrawChangeSize.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> seekBarDrawSize.setVisibility(View.VISIBLE), 100);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void CloseOrOpenDrawOptions(boolean Close) {

        if (Close) {
            // animateObject(DrawOptions, "translationY", DCY, 350);
            CloseOrOpenDrawSize(Close);
            animateHeightChange(DrawOptions, 500, 0);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                relativeLayout.removeView(DrawOptions);
            }, 500);
            drawing = false;
            drawOptions = false;
        } else {

            if (paragraphOptions) {
                CloseOrOpenParagraphOptions(true);
                paragraphOptions = false;
            }
            if (importerOptions) {
                CloseOrOpenImporterOptions(true);
                importerOptions = false;
            }
            if (formatOptions) {
                CloseOrOpenFormatOptions(true);
                formatOptions = false;
            }
            if (audioOpen) {
                CloseOrOpenAudioOptions(true);
                audioOpen = false;
            }
            //  animateObject(DrawOptions, "translationY", DOY-50, 350);

            if (oldView != null) {
                relativeLayout.removeView(oldView);
            }
            oldView = View.inflate(context, R.layout.draw_options_viewbuttons, null);
            DrawOptions = ((CardView) oldView);
            relativeLayout.addView(DrawOptions);
            ViewGroup.LayoutParams layoutParams = DrawOptions.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = relativeLayout.getWidth();
            DrawOptions.setLayoutParams(layoutParams);
            animateHeightChange(DrawOptions, 500, 150);
            drawing = true;
            drawOptions = true;

            DrawChangeColor = DrawOptions.findViewById(R.id.DrawChangeColor);
            DrawChangeSize = DrawOptions.findViewById(R.id.DrawChangeSize);
            PushDownAnim.setPushDownAnimTo(DrawChangeColor).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(DrawChangeSize).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(DrawOptions).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            seekBarDrawSize = DrawOptions.findViewById(R.id.draw_size_seekbar);
            seekBarDrawSize.setOnTouchListener((v, event) -> {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            DrawVisualizerIsShowing = false;

                            Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fade_out_formatter);

                            size_visualizer.startAnimation(fadeout);


                        }, 450);
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle Seekbar touch events.
                v.onTouchEvent(event);
                return true;
            });
            seekBarDrawSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    mainActivity.get().getNoteCreator().getEverDraw().setStrokeWidth((float) (i * 1.1));

                    if (!mainActivity.get().getEverViewManagement().DrawVisualizerIsShowing) {
                        mainActivity.get().getEverViewManagement().ShowDrawSizeVisualizer();
                    }
                    mainActivity.get().getEverViewManagement().ModifyDrawSizeVisualizer(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    public void CloseOrOpenFormatOptions(boolean Close) {

        if (Close) {
            // animateObject(FormatOptions, "translationY", DCY, 350);
            animateHeightChange(FormatOptions, 500, 0);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                relativeLayout.removeView(FormatOptions);
            }, 500);
            formatOptions = false;

        } else {

            if (paragraphOptions) {
                CloseOrOpenParagraphOptions(true);
                paragraphOptions = false;
            }
            if (importerOptions) {
                CloseOrOpenImporterOptions(true);
                importerOptions = false;
            }
            if (drawOptions) {
                CloseOrOpenDrawOptions(true);
                drawOptions = false;
            }
            if (audioOpen) {
                CloseOrOpenAudioOptions(true);
                audioOpen = false;
            }
            //  animateObject(FormatOptions, "translationY", DOY, 350);

            oldView = View.inflate(context, R.layout.format_options_viewbuttons, null);
            FormatOptions = ((CardView) oldView);
            relativeLayout.addView(FormatOptions);
            ViewGroup.LayoutParams layoutParams = FormatOptions.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = relativeLayout.getWidth();
            FormatOptions.setLayoutParams(layoutParams);
            animateHeightChange(FormatOptions, 500, 150);
            ImageButton increaseSize = FormatOptions.findViewById(R.id.IncreaseSize1);
            ImageButton decreaseSize = FormatOptions.findViewById(R.id.DecreaseSize1);

            FormatOptions = FormatOptions.findViewById(R.id.format_selectors);
            Bold = FormatOptions.findViewById(R.id.toolbar_bold);
            Italic = FormatOptions.findViewById(R.id.toolbar_italic);
            Underline = FormatOptions.findViewById(R.id.toolbar_underline);
            StrikeThrough = FormatOptions.findViewById(R.id.toolbar_strikethrough);
            ImageButton highlight = FormatOptions.findViewById(R.id.HighlightText1);
            ImageButton changeColor = FormatOptions.findViewById(R.id.ChangeColor1);


            PushDownAnim.setPushDownAnimTo(Bold).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(Italic).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(Underline).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(StrikeThrough).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(highlight).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(changeColor).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(increaseSize).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(decreaseSize).setScale(PushDownAnim.MODE_SCALE, 0.7f);

            editorToolbar.init(true);
            formatOptions = true;
        }
    }

    public void CloseOrOpenSelectionOptions(boolean Close) {
        if (Close) {
            animateHeightChange(SelectOptions, 500, 0);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                relativeLayout.removeView(SelectOptions);
            }, 500);
        } else {

            if (oldView != null) {
                relativeLayout.removeView(oldView);
            }

            oldView = LayoutInflater.from(context).inflate(R.layout.select_options_viewbuttons, null, false);
            SelectOptions = ((CardView) oldView);
            relativeLayout.addView(SelectOptions);
            ViewGroup.LayoutParams layoutParams = SelectOptions.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = relativeLayout.getWidth();
            SelectOptions.setLayoutParams(layoutParams);
            mainActivity.get().getEverNoteManagement().setNotesSelected(false);
            animateHeightChange(SelectOptions, 500, 150);

            ImageButton selectionChangeColor = SelectOptions.findViewById(R.id.selectChangeColor);
            ImageButton selectionDelete = SelectOptions.findViewById(R.id.selectDelete);

            PushDownAnim.setPushDownAnimTo(selectionChangeColor).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(selectionDelete).setScale(PushDownAnim.MODE_SCALE, 0.7f);
        }
    }

    public void CloseOrOpenParagraphOptions(boolean Close) {
        if (Close) {
            //  animateObject(ParagraphOptions, "translationY", DCY, 350);
            animateHeightChange(ParagraphOptions, 500, 0);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                relativeLayout.removeView(ParagraphOptions);
            }, 500);
            paragraphOptions = false;
        } else {

            if (importerOptions) {
                CloseOrOpenImporterOptions(true);
                importerOptions = false;
            }
            if (formatOptions) {
                CloseOrOpenFormatOptions(true);
                formatOptions = false;
            }
            if (drawOptions) {
                CloseOrOpenDrawOptions(true);
                drawOptions = false;
            }
            if (audioOpen) {
                CloseOrOpenAudioOptions(true);
                audioOpen = false;
            }

            oldView = View.inflate(context, R.layout.paragraph_options_viewbuttons, null);
            ParagraphOptions = ((CardView) oldView);
            relativeLayout.addView(ParagraphOptions);
            ViewGroup.LayoutParams layoutParams = ParagraphOptions.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = relativeLayout.getWidth();
            ParagraphOptions.setLayoutParams(layoutParams);
            //  animateObject(ParagraphOptions, "translationY", DOY, 350);
            animateHeightChange(ParagraphOptions, 500, 150);

            Bullets = ParagraphOptions.findViewById(R.id.toolbar_bullet);
            Numbers = ParagraphOptions.findViewById(R.id.toolbar_number);
            AlignLeft = ParagraphOptions.findViewById(R.id.toolbar_align_left);
            AlignCenter = ParagraphOptions.findViewById(R.id.toolbar_align_center);
            AlignRight = ParagraphOptions.findViewById(R.id.toolbar_align_right);


            PushDownAnim.setPushDownAnimTo(Bullets).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(Numbers).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(AlignLeft).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(AlignCenter).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(AlignRight).setScale(PushDownAnim.MODE_SCALE, 0.7f);

            editorToolbar.init(false);

            paragraphOptions = true;
        }
    }

    public void CloseOrOpenImporterOptions(boolean Close) {

        if (Close) {
            //  animateObject(ImporterOptions, "translationY", DCY+50, 350);
            animateHeightChange(ImporterOptions, 500, 0);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                relativeLayout.removeView(ImporterOptions);
            }, 500);
            importerOptions = false;
        } else {

            if (paragraphOptions) {
                CloseOrOpenParagraphOptions(true);
                paragraphOptions = false;
            }
            if (formatOptions) {
                CloseOrOpenFormatOptions(true);
                formatOptions = false;
            }
            if (drawOptions) {
                CloseOrOpenDrawOptions(true);
                drawOptions = false;
            }
            if (audioOpen) {
                CloseOrOpenAudioOptions(true);
                audioOpen = false;
            }
            // animateObject(ImporterOptions, "translationY", DOY, 350);
            oldView = View.inflate(context, R.layout.imports_options_viewbuttons, null);
            ImporterOptions = ((CardView) oldView);
            relativeLayout.addView(ImporterOptions);
            ViewGroup.LayoutParams layoutParams = ImporterOptions.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = relativeLayout.getWidth();
            ImporterOptions.setLayoutParams(layoutParams);
            animateHeightChange(ImporterOptions, 500, 150);

            ImageButton googlePhotos = ImporterOptions.findViewById(R.id.GooglePhotos);
            ImageButton files = ImporterOptions.findViewById(R.id.Files);
            ImageButton gallery = ImporterOptions.findViewById(R.id.Gallery);


            PushDownAnim.setPushDownAnimTo(googlePhotos).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(files).setScale(PushDownAnim.MODE_SCALE, 0.7f);
            PushDownAnim.setPushDownAnimTo(gallery).setScale(PushDownAnim.MODE_SCALE, 0.7f);

            importerOptions = true;
        }
    }

    public void CloseAllButtons() {

        switchToolbars(false, false, false);

        InputMethodManager keyboard = (InputMethodManager) mainActivity.get().getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(mainActivity.get().getToolbar().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (paragraphOptions) {
            CloseOrOpenParagraphOptions(true);
            paragraphOptions = false;
        }
        if (importerOptions) {
            CloseOrOpenImporterOptions(true);
            importerOptions = false;
        }
        if (formatOptions) {
            CloseOrOpenFormatOptions(true);
            formatOptions = false;
        }
        if (drawOptions) {
            CloseOrOpenDrawOptions(true);
            drawOptions = false;
        }

        if (audioOpen) {
            CloseOrOpenAudioOptions(true);
            audioOpen = false;
        }


        if (seekBarDrawSize != null) {
            if (seekBarDrawSize.getVisibility() == View.VISIBLE) {
                CloseOrOpenDrawSize(true);
            }
        }
    }

    public void animateObject(View view, String effect, int amount, int duration) {
        ObjectAnimator transAnimation2 = ObjectAnimator.ofFloat(view, effect, view.getTranslationY(), amount);
        transAnimation2.setDuration(duration);//set duration
        transAnimation2.setInterpolator(new LinearOutSlowInInterpolator());
        transAnimation2.start();//start animation
    }

    public void beginDelayedTransition(ViewGroup view) {
        TransitionManager.beginDelayedTransition(view, new TransitionSet()
                .addTransition(new ChangeBounds()));
    }

    public void pushDownOnTouch(View view, MotionEvent event, float pushScale, int duration) {
        int i = event.getAction();
        if (i == MotionEvent.ACTION_DOWN) {
            makeDecisionAnimScale(view,
                    pushScale,
                    duration,
                    new AccelerateDecelerateInterpolator());
        } else if (i == MotionEvent.ACTION_CANCEL
                || i == MotionEvent.ACTION_UP) {
            makeDecisionAnimScale(view,
                    1f,
                    duration,
                    new AccelerateDecelerateInterpolator());
        }
    }

    public void makeDecisionAnimScale(final View view, float pushScale, long duration, TimeInterpolator interpolator) {

        animScale(view, pushScale, duration, interpolator);
    }

    public void animScale(final View view, float scale, long duration, TimeInterpolator interpolator) {
        AnimatorSet scaleAnimSet = new AnimatorSet();
        view.animate().cancel();
        if (scaleAnimSet != null) {
            scaleAnimSet.cancel();
        }

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleX.setInterpolator(interpolator);
        scaleX.setDuration(duration);
        scaleY.setInterpolator(interpolator);
        scaleY.setDuration(duration);

        scaleAnimSet = new AnimatorSet();
        scaleAnimSet
                .play(scaleX)
                .with(scaleY);
        scaleX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        scaleX.addUpdateListener(valueAnimator -> {
            View p = (View) view.getParent();
            if (p != null) p.invalidate();
        });
        scaleAnimSet.start();
    }

    public void switchToolbars(boolean showmain) {
        if (showmain) {
            if (mainActivity.get().getEverNoteManagement() != null) {
                if (mainActivity.get().getEverNoteManagement().isSearching()) {
                    mainActivity.get().getTitleBox().setVisibility(View.VISIBLE);
                    mainActivity.get().getTitleBox().removeTextChangedListener(mainActivity.get().getTitleWatcher());
                   // mainActivity.get().getTitleBox().setText("");
                    mainActivity.get().getTitleBox().setText(lastSearch);
                    mainActivity.get().getTitleBox().addTextChangedListener(mainActivity.get().getSearchWatcher());
                    Save.setVisibility(View.INVISIBLE);
                    Delete.setVisibility(View.INVISIBLE);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Save.setImageResource(R.drawable.ic_baseline_search_24);
                        Delete.setImageResource(R.drawable.ic_baseline_library_books_24);
                        Save.setVisibility(View.VISIBLE);
                        Delete.setVisibility(View.VISIBLE);
                    }, 150);

                    Save.setTag("Search");
                    Delete.setTag("GridLayout");
                } else {
                    mainActivity.get().getTitleBox().setVisibility(View.VISIBLE);
                    mainActivity.get().getTitleBox().removeTextChangedListener(mainActivity.get().getTitleWatcher());
                    mainActivity.get().getTitleBox().setText("EVERMEMO");
                    mainActivity.get().getTitleBox().setFocusable(false);
                    mainActivity.get().getTitleBox().setFocusableInTouchMode(false);
                    Save.setVisibility(View.INVISIBLE);
                    Delete.setVisibility(View.INVISIBLE);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Save.setImageResource(R.drawable.ic_baseline_search_24);
                        Delete.setImageResource(R.drawable.ic_baseline_library_books_24);
                        Save.setVisibility(View.VISIBLE);
                        Delete.setVisibility(View.VISIBLE);
                    }, 150);

                    Save.setTag("Search");
                    Delete.setTag("GridLayout");
                }
            }


            //  findViewById(R.id.note_Creator_ToolbarLayout).setVisibility(View.GONE);
            //   findViewById(R.id.mainScreen_ToolbarLayout).setVisibility(View.VISIBLE);

        } else {
            // findViewById(R.id.mainScreen_ToolbarLayout).setVisibility(View.GONE);
            //  edit.get().setVisibility(View.GONE);
            mainActivity.get().setTitleWatcher(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mainActivity.get().getActualNote().setTitle(s.toString());
                }
            });
            Save.setVisibility(View.INVISIBLE);
            Delete.setVisibility(View.INVISIBLE);
            mainActivity.get().getTitleBox().removeTextChangedListener(mainActivity.get().getSearchWatcher());
            lastSearch = mainActivity.get().getTitleBox().getText().toString();
         //   mainActivity.get().getTitleBox().setText(" ");
            mainActivity.get().getTitleBox().setText(mainActivity.get().getActualNote().getTitle());
            mainActivity.get().getTitleBox().setHint("Create a title");

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Save.setImageResource(R.drawable.ic_baseline_check_24);
                Delete.setImageResource(R.drawable.clear);
                Save.setVisibility(View.VISIBLE);
                Delete.setVisibility(View.VISIBLE);
                mainActivity.get().getTitleBox().addTextChangedListener(mainActivity.get().getTitleWatcher());
                mainActivity.get().getTitleBox().setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) {
                        switchToolbars(true, false, false);
                    }
                });
                mainActivity.get().getTitleBox().setFocusable(true);
                mainActivity.get().getTitleBox().setFocusableInTouchMode(true);
            }, 150);
            Save.setTag("Save");
            Delete.setTag("Delete");
            // findViewById(R.id.note_Creator_ToolbarLayout).setVisibility(View.VISIBLE);

        }
    }

    public void drawChangeSizeClick() {
        if (drawsize) {
            CloseOrOpenDrawSize(true);
            drawsize = false;
        } else {
            CloseOrOpenDrawSize(false);
            drawsize = true;
        }
    }

    public void clearBooleans() {
        DrawVisualizerIsShowing = false;
        drawing = false;
        bottomBarUp = false;
        formatOptions = false;
        paragraphOptions = false;
        importerOptions = false;
        drawOptions = false;
        drawsize = false;
        bold = false;
        italic = false;
        underline = false;
        striketrough = false;
        numbers = false;
        bullets = false;
        audioOpen = false;
    }

    public RTToolbarImageButton getBold() {
        return Bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public RTToolbarImageButton getItalic() {
        return Italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public RTToolbarImageButton getUnderline() {
        return Underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public RTToolbarImageButton getStrikeThrough() {
        return StrikeThrough;
    }

    public RTToolbarImageButton getBullets() {
        return Bullets;
    }

    public void setBullets(boolean bullets) {
        this.bullets = bullets;
    }

    public RTToolbarImageButton getNumbers() {
        return Numbers;
    }

    public void setNumbers(boolean numbers) {
        this.numbers = numbers;
    }

    public RTToolbarImageButton getAlignLeft() {
        return AlignLeft;
    }

    public RTToolbarImageButton getAlignCenter() {
        return AlignCenter;
    }

    public RTToolbarImageButton getAlignRight() {
        return AlignRight;
    }

    public void setDrawOptions(boolean drawOptions) {
        this.drawOptions = drawOptions;
    }

    public CardView getAudioOptions() {
        return AudioOptions;
    }

    public TextView getTimerView() {
        return timerView;
    }
}
