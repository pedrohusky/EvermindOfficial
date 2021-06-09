package com.example.evermemo.everUtils;

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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.evermemo.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.evermemo.MainActivity;
import com.example.evermemo.R;
import com.example.evermemo.TESTEDITOR.toolbar.HorizontalRTToolbar;
import com.example.evermemo.TESTEDITOR.toolbar.RTToolbarImageButton;
import com.example.evermemo.databinding.HomeScreenButtonsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cafe.adriel.androidaudiorecorder.Util;
import me.ibrahimsn.lib.SmoothBottomBar;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EverViewManagement implements EverInterfaceHelper.OnEnterDarkMode {
    @NonNull
    private final Context context;
    @NonNull
    private final WeakReference<MainActivity> mainActivity;
    private final HomeScreenButtonsBinding buttonsBinding;
    private int audioDecoySize = 0;
    private boolean bottomBarUp = false;
    private String lastSearch;
    private String lastOpenedTab = "";
    private boolean isDrawing = false;
    private final HorizontalRTToolbar toolbar;
    private ValueAnimator heightAnimator;

    public EverViewManagement(@NonNull Context context) {
        this.context = context;
        mainActivity = new WeakReference<>(((MainActivity) context));
        buttonsBinding = mainActivity.get().getButtonsBinding();
        toolbar = new HorizontalRTToolbar(context);
        mainActivity.get().getmRTManager().registerToolbar(null, toolbar);
        EverInterfaceHelper.getInstance().setDarkModeListeners(this);
        buttonsBinding.bottomBar.setOnItemReselected(integer -> {
            switch (integer) {
                case 0:
                    switchBottomBars("format");
                    break;

                case 1:
                    switchBottomBars("paragraph");
                    break;

                case 2:
                    switchBottomBars("import");
                    break;

                case 3:
                    if (mainActivity.get().getAudioHelper() == null) {
                        mainActivity.get().setAudioHelper(new EverAudioHelper(mainActivity.get()));
                    }
                    if (mainActivity.get().getAudioHelper().isRecording()) {
                        mainActivity.get().getAudioHelper().stop(true);
                    } else if (mainActivity.get().getAudioHelper().hasRecordStarted()) {
                        mainActivity.get().getAudioHelper().stop(true);
                    } else {
                        mainActivity.get().recordAudio();
                    }
                    break;

                case 4:
                    switchBottomBars("newDraw");
                    break;

                case 5:

                    break;
            }
            return null;
        });
        buttonsBinding.bottomBar.setOnItemSelected(integer -> {
            switch (integer) {
                case 0:
                    switchBottomBars("format");
                    break;

                case 1:
                    switchBottomBars("paragraph");
                    break;

                case 2:
                    switchBottomBars("import");
                    break;

                case 3:

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(mainActivity.get(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);

                    } else {
                        if (mainActivity.get().getAudioHelper() == null) {
                            mainActivity.get().setAudioHelper(new EverAudioHelper(mainActivity.get()));
                        }
                        mainActivity.get().recordAudio();

                    }
                    break;

                case 4:
                    switchBottomBars("newDraw");
                    break;

                case 5:

                    break;
            }
            return null;
        });
        //scrollView1 = findViewById(R.id.scroll_draw);
        List<View> views = new ArrayList<>();
        views.add(buttonsBinding.toolbarUndo);
        views.add(buttonsBinding.toolbarRedo);
        views.add(buttonsBinding.Delete);
        views.add(buttonsBinding.toolbarBold);
        views.add(buttonsBinding.toolbarItalic);
        views.add(buttonsBinding.toolbarUnderline);
        views.add(buttonsBinding.toolbarStrikethrough);
        views.add(buttonsBinding.HighlightText1);
        views.add(buttonsBinding.ChangeColor1);
        views.add(buttonsBinding.IncreaseSize1);
        views.add(buttonsBinding.DecreaseSize1);
        views.add(buttonsBinding.selectChangeColor);
        views.add(buttonsBinding.Delete);
        views.add(buttonsBinding.toolbarBullet);
        views.add(buttonsBinding.toolbarNumber);
        views.add(buttonsBinding.toolbarAlignLeft);
        views.add(buttonsBinding.toolbarAlignCenter);
        views.add(buttonsBinding.toolbarAlignRight);
        views.add(buttonsBinding.GooglePhotos);
        views.add(buttonsBinding.Files);
        views.add(buttonsBinding.Gallery);


        mainActivity.get().applyPushDownToViews(views, 0.7f);

        SeekBar drawSizeSeekbar = buttonsBinding.cardDrawMoreOptions.findViewById(R.id.draw_size_seeekbar);
        ColorPickerView colorPickerView = buttonsBinding.cardDrawMoreOptions.findViewById(R.id.colorPickerView);
        ImageView circleVisualizer = buttonsBinding.cardDrawMoreOptions.findViewById(R.id.circleVisualizer);
        drawSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getHandler() != null) {
                    seekBar.getHandler().postDelayed(() -> {
                        float f = (float) progress / 100;
                        circleVisualizer.setScaleX(f);
                        circleVisualizer.setScaleY(f);
                        mainActivity.get().getNoteCreator().getEverDraw().setStrokeWidth(progress * 0.8f);
                    }, 10);
                } else {
                    float f = (float) progress / 100;
                    circleVisualizer.setScaleX(f);
                    circleVisualizer.setScaleY(f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public ImageButton getUndo() {
        return buttonsBinding.toolbarUndo;
    }

    public ImageButton getRedo() {
        return buttonsBinding.toolbarRedo;
    }

    public ImageButton getDelete() {
        return buttonsBinding.Delete;
    }


    public SmoothBottomBar getBottomBar() {
        return buttonsBinding.bottomBar;
    }

    public ImageButton getStopView() {
        return buttonsBinding.stop;
    }

    public ImageButton getSaveView() {
        return buttonsBinding.saveAudio;
    }

    public ImageButton getPlayView() {
        return buttonsBinding.play;
    }

    public void CloseOrOpenToolbarUndoRedo(boolean UndoRedo) {
        if (UndoRedo) {
            buttonsBinding.changeNotecolorButton.setVisibility(View.GONE);
            // mainActivity.get().getHandler().postDelayed(() -> {
            buttonsBinding.toolbarUndo.setVisibility(View.VISIBLE);
            buttonsBinding.toolbarRedo.setVisibility(View.VISIBLE);
            //     }, 250);
        } else {
            buttonsBinding.toolbarUndo.setVisibility(View.GONE);
            buttonsBinding.toolbarRedo.setVisibility(View.GONE);
            //    mainActivity.get().getHandler().postDelayed(() -> {
            if (!mainActivity.get().isAtHome()) {
                buttonsBinding.changeNotecolorButton.setVisibility(View.VISIBLE);
            } else {
                buttonsBinding.changeNotecolorButton.setVisibility(View.GONE);
            }
            //   }, 250);
        }
    }

    public void switchToolbars(boolean show, boolean bottomToolbar, boolean showUndoRedo) {
        if (show) {
            if (bottomToolbar) {
                if (!bottomBarUp) {
                    animateHeightChange(buttonsBinding.bottomBar, 600, 150, null);

                    animateHeightChange(buttonsBinding.decoySpace, 500, 150, null);
                    //   buttonsBinding.bottomBar.setVisibility(View.VISIBLE);
                    bottomBarUp = true;
                }
            } else {
                //  buttonsBinding.bottomBar.setVisibility(View.GONE);
                animateHeightChange(buttonsBinding.bottomBar, 600, 0, null);
                animateHeightChange(buttonsBinding.decoySpace, 500, 0, null);

                bottomBarUp = false;
            }

            CloseOrOpenToolbarUndoRedo(showUndoRedo);

        } else {
            // buttonsBinding.bottomBar.setVisibility(View.GONE);
            animateHeightChange(buttonsBinding.bottomBar, 600, 0, null);
            animateHeightChange(buttonsBinding.decoySpace, 500, 0, null);
            CloseOrOpenToolbarUndoRedo(false);
            bottomBarUp = false;
        }
    }

    public void animateHeightChange(@NonNull View view, int duration, int amount, @Nullable Runnable action) {
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
        if (action != null) {
            heightAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    action.run();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    heightAnimator.end();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        //   });
    }

    public void animateWidthChange(@NonNull View view, int duration, int amount) {
        //    new Handler(Looper.getMainLooper()).post(() -> {
        ValueAnimator widthAnimator = ValueAnimator.ofInt(view.getMeasuredWidth(), amount);
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


    public void CloseAllButtons() {

        switchToolbars(false, false, false);

        InputMethodManager keyboard = (InputMethodManager) mainActivity.get().getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(mainActivity.get().getToolbar().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        switchBottomBars(lastOpenedTab);


    }

    public void animateObject(@NonNull View view, String effect, int amount, int duration) {
        ObjectAnimator transAnimation2 = ObjectAnimator.ofFloat(view, effect, view.getTranslationY(), amount);
        transAnimation2.setDuration(duration);//set duration
        transAnimation2.setInterpolator(new LinearOutSlowInInterpolator());
        transAnimation2.start();//start animation
    }

    public void beginDelayedTransition(ViewGroup view) {
        TransitionManager.beginDelayedTransition(view, new TransitionSet()
                .addTransition(new ChangeBounds()));
    }

    public void pushDownOnTouch(@NonNull View view, @NonNull MotionEvent event, float pushScale, int duration) {
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

    public void makeDecisionAnimScale(@NonNull final View view, float pushScale, long duration, TimeInterpolator interpolator) {

        animScale(view, pushScale, duration, interpolator);
    }

    public void animScale(@NonNull final View view, float scale, long duration, TimeInterpolator interpolator) {
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
                    //     mainActivity.get().getTitleBox().setVisibility(View.VISIBLE);
                    mainActivity.get().getTitleBox().removeTextChangedListener(mainActivity.get().getTitleWatcher());
                    // mainActivity.get().getTitleBox().setText("");
                    mainActivity.get().getTitleBox().setText(lastSearch);
                    mainActivity.get().getTitleBox().addTextChangedListener(mainActivity.get().getSearchWatcher());
                    buttonsBinding.Delete.setVisibility(View.INVISIBLE);
                    buttonsBinding.Delete.postDelayed(() -> {
                        buttonsBinding.Delete.setImageResource(R.drawable.ic_baseline_library_books_24);
                        buttonsBinding.Delete.setVisibility(View.VISIBLE);
                    }, 150);

                    buttonsBinding.Delete.setTag("GridLayout");
                } else {
                    //   mainActivity.get().getTitleBox().setVisibility(View.VISIBLE);
                    mainActivity.get().getTitleBox().removeTextChangedListener(mainActivity.get().getTitleWatcher());
                    mainActivity.get().getTitleBox().setText("EVERMEMO");
                    mainActivity.get().getTitleBox().setFocusable(false);
                    mainActivity.get().getTitleBox().setFocusableInTouchMode(false);
                    buttonsBinding.Delete.setVisibility(View.INVISIBLE);
                    buttonsBinding.Delete.postDelayed(() -> {
                        buttonsBinding.Delete.setImageResource(R.drawable.ic_baseline_library_books_24);
                        buttonsBinding.Delete.setVisibility(View.VISIBLE);
                    }, 150);

                    buttonsBinding.Delete.setTag("GridLayout");
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
                public void afterTextChanged(@NonNull Editable s) {
                    mainActivity.get().getActualNote().setTitle(s.toString());
                }
            });
            buttonsBinding.Delete.setVisibility(View.INVISIBLE);
            mainActivity.get().getTitleBox().removeTextChangedListener(mainActivity.get().getSearchWatcher());
            lastSearch = mainActivity.get().getTitleBox().getText().toString();
            //   mainActivity.get().getTitleBox().setText(" ");
            if (mainActivity.get().getActualNote() != null) {
                mainActivity.get().getTitleBox().setText(mainActivity.get().getActualNote().getTitle());
            }
            mainActivity.get().getTitleBox().setHint("Create a title");

            buttonsBinding.Delete.postDelayed(() -> {
                buttonsBinding.Delete.setImageResource(R.drawable.ic_baseline_check_24);
                buttonsBinding.Delete.setVisibility(View.VISIBLE);
                mainActivity.get().getTitleBox().addTextChangedListener(mainActivity.get().getTitleWatcher());
                mainActivity.get().getTitleBox().setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) {
                        switchToolbars(true, false, false);
                    }
                });
                mainActivity.get().getTitleBox().setFocusable(true);
                mainActivity.get().getTitleBox().setFocusableInTouchMode(true);
            }, 150);

            buttonsBinding.Delete.setTag("Save");
            // findViewById(R.id.note_Creator_ToolbarLayout).setVisibility(View.VISIBLE);

        }
    }

    public void clearBooleans() {
        bottomBarUp = false;
        lastOpenedTab = "";
    }

    public RTToolbarImageButton getBold() {
        return buttonsBinding.toolbarBold;
    }

    public RTToolbarImageButton getItalic() {
        return buttonsBinding.toolbarItalic;
    }

    public RTToolbarImageButton getUnderline() {
        return buttonsBinding.toolbarUnderline;
    }

    public RTToolbarImageButton getStrikeThrough() {
        return buttonsBinding.toolbarStrikethrough;
    }

    public RTToolbarImageButton getBullets() {
        return buttonsBinding.toolbarBullet;
    }

    public RTToolbarImageButton getNumbers() {
        return buttonsBinding.toolbarNumber;
    }

    public RTToolbarImageButton getAlignLeft() {
        return buttonsBinding.toolbarAlignLeft;
    }

    public RTToolbarImageButton getAlignCenter() {
        return buttonsBinding.toolbarAlignCenter;
    }

    public RTToolbarImageButton getAlignRight() {
        return buttonsBinding.toolbarAlignRight;
    }

    public ConstraintLayout getAudioOptions() {
        return buttonsBinding.cardAudioOptions;
    }

    public TextView getTimerView() {
        return buttonsBinding.audioTime;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void switchBottomBars(String name) {

        int delay = 0;

        if (!lastOpenedTab.equals("")) {
            delay = 250;
        }

        switch (lastOpenedTab) {
            case "format":
                animateHeightChange(buttonsBinding.cardFormatMoreOptions, 250, 0, new Runnable() {
                    @Override
                    public void run() {
                        buttonsBinding.cardFormatMoreOptions.setVisibility(View.GONE);
                    }
                });
                animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                break;
            case "paragraph":
                animateHeightChange(buttonsBinding.cardParagraphOptions, 250, 0, null);
                animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                break;

            case "import":
                animateHeightChange(buttonsBinding.cardImportOptions, 250, 0, null);
                animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                break;

            case "audio":
                mainActivity.get().getAudioHelper().stopRender();
                buttonsBinding.cardAudioOptions.postDelayed(() -> {
                    animateHeightChange(buttonsBinding.cardAudioOptions, 100, 0, new Runnable() {
                        @Override
                        public void run() {
                            buttonsBinding.everGLAudioVisualizationView.setVisibility(View.GONE);
                        }
                    });
                    animateHeightChange(buttonsBinding.decoySpace, 100, buttonsBinding.bottomBar.getHeight(), null);
                }, 150);
                //  EverInterfaceHelper.getInstance().changeState(true);
                break;

            case "draw":
                if (mainActivity.get().getNoteCreator().isNewDraw()) {
                    mainActivity.get().getNoteCreator().removeEverLinkedContentAtPosition(mainActivity.get().getActualNote().getEverLinkedContents(false).size() - 2);
                }
                isDrawing = false;
                animateHeightChange(buttonsBinding.cardDrawMoreOptions, 250, 0, new Runnable() {
                    @Override
                    public void run() {
                        buttonsBinding.cardDrawMoreOptions.setVisibility(View.GONE);
                    }
                });
                animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                break;

            case "select":
                animateHeightChange(buttonsBinding.cardSelectors, 250, 0, null);
                animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                break;
        }

        getBottomBar().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!name.equals(lastOpenedTab)) {
                    switch (name) {
                        case "format":
                            buttonsBinding.cardFormatMoreOptions.setVisibility(View.VISIBLE);
                            animateHeightChange(buttonsBinding.cardFormatMoreOptions, 350, 200,null);
                            animateHeightChange(buttonsBinding.decoySpace, 500, 300, null);
                            lastOpenedTab = name;
                            break;
                        case "paragraph":
                            buttonsBinding.cardParagraphOptions.setVisibility(View.VISIBLE);
                            animateHeightChange(buttonsBinding.cardParagraphOptions, 350, 200, null);
                            animateHeightChange(buttonsBinding.decoySpace, 500, 300, null);
                            lastOpenedTab = name;
                            break;

                        case "import":
                            buttonsBinding.cardImportOptions.setVisibility(View.VISIBLE);
                            animateHeightChange(buttonsBinding.cardImportOptions, 350, 200, null);
                            animateHeightChange(buttonsBinding.decoySpace, 500, 300, null);
                            lastOpenedTab = name;
                            break;

                        case "audio":
                            buttonsBinding.cardAudioOptions.setVisibility(View.VISIBLE);
                            //   EverInterfaceHelper.getInstance().changeState(false);
                            animateHeightChange(buttonsBinding.cardAudioOptions, 350, 450, new Runnable() {
                                @Override
                                public void run() {
                                    buttonsBinding.everGLAudioVisualizationView.setVisibility(View.VISIBLE);
                                }
                            });
                            animateHeightChange(buttonsBinding.decoySpace, 500, 600, null);
                            lastOpenedTab = name;
                            break;

                        case "newDraw":

                            if (!isDrawing) {
                                buttonsBinding.cardDrawMoreOptions.setVisibility(View.VISIBLE);
                                InputMethodManager keyboard1 = (InputMethodManager) mainActivity.get().getSystemService(INPUT_METHOD_SERVICE);
                                keyboard1.hideSoftInputFromWindow(mainActivity.get().getNoteCreator().getCardNoteCreator().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                if (mainActivity.get().getNoteCreator().getActiveEditor() != null) {
                                    mainActivity.get().getNoteCreator().getActiveEditor().clearFocus();
                                }
                                mainActivity.get().getActualNote().addEverLinkedMap("edit", mainActivity.get(), true);
                                mainActivity.get().getNoteCreator().setNewDraw(true);
                                isDrawing = true;
                                animateHeightChange(buttonsBinding.decoySpace, 500, 300, null);
                                animateHeightChange(buttonsBinding.cardDrawMoreOptions, 350, 200, null);
                                lastOpenedTab = "draw";
                            }
                            break;

                        case "draw":

                            buttonsBinding.cardDrawMoreOptions.setVisibility(View.VISIBLE);
                            InputMethodManager keyboard2 = (InputMethodManager) mainActivity.get().getSystemService(INPUT_METHOD_SERVICE);
                            keyboard2.hideSoftInputFromWindow(mainActivity.get().getNoteCreator().getCardNoteCreator().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            if (mainActivity.get().getNoteCreator().getActiveEditor() != null) {
                                mainActivity.get().getNoteCreator().getActiveEditor().clearFocus();
                            }
                            isDrawing = true;

                            //  inflateLayoutByName("draw");
                            animateHeightChange(buttonsBinding.decoySpace, 500, 300, null);
                            animateHeightChange(buttonsBinding.cardDrawMoreOptions, 350, 200, null);

                            lastOpenedTab = name;
                            break;

                        case "select":
                            buttonsBinding.cardSelectors.setVisibility(View.VISIBLE);
                            mainActivity.get().getEverNoteManagement().setNotesSelected(false);
                            animateHeightChange(buttonsBinding.cardSelectors, 350, 200, null);
                            animateHeightChange(buttonsBinding.decoySpace, 500, 300, null);
                            lastOpenedTab = name;
                            break;
                    }
                } else {
                    switch (name) {
                        case "format":
                            animateHeightChange(buttonsBinding.cardFormatMoreOptions, 250, 0, new Runnable() {
                                @Override
                                public void run() {
                                    buttonsBinding.cardFormatMoreOptions.setVisibility(View.GONE);
                                }
                            });
                            animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                            break;
                        case "paragraph":
                            animateHeightChange(buttonsBinding.cardParagraphOptions, 250, 0, null);
                            animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                            break;

                        case "import":
                            animateHeightChange(buttonsBinding.cardImportOptions, 250, 0, null);
                            animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                            break;

                        case "audio":
                            //    mainActivity.get().getAudioHelper().stop(true);
                            mainActivity.get().getAudioHelper().stopRender();
                            mainActivity.get().getUIHandler().postDelayed(() -> {
                                animateHeightChange(buttonsBinding.cardAudioOptions, 250, 0, new Runnable() {
                                    @Override
                                    public void run() {
                                        buttonsBinding.everGLAudioVisualizationView.setVisibility(View.GONE);
                                    }
                                });
                                animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                            }, 175);
                            //   EverInterfaceHelper.getInstance().changeState(true);
                            break;

                        case "draw":
                            if (mainActivity.get().getNoteCreator().isNewDraw()) {
                                mainActivity.get().getNoteCreator().removeEverLinkedContentAtPosition(mainActivity.get().getActualNote().getEverLinkedContents(false).size() - 2);
                            }
                            isDrawing = false;
                            animateHeightChange(buttonsBinding.cardDrawMoreOptions, 250, 0, new Runnable() {
                                @Override
                                public void run() {
                                    buttonsBinding.cardDrawMoreOptions.setVisibility(View.GONE);
                                }
                            });
                            animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                            break;

                        case "newDraw":
                            if (mainActivity.get().getNoteCreator().isNewDraw()) {
                                mainActivity.get().getNoteCreator().removeEverLinkedContentAtPosition(mainActivity.get().getActualNote().getEverLinkedContents(false).size() - 2);
                            }
                            isDrawing = false;
                            animateHeightChange(buttonsBinding.cardDrawMoreOptions, 250, 0, null);
                            animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                            break;

                        case "select":
                            animateHeightChange(buttonsBinding.cardSelectors, 250, 0, null);
                            animateHeightChange(buttonsBinding.decoySpace, 250, buttonsBinding.bottomBar.getHeight(), null);
                            break;
                    }
                    lastOpenedTab = "";
                }
            }
        }, delay);


        buttonsBinding.upDraw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println(-1);
                ((MainActivity) context).getNoteCreator().gettextRecyclerCreator().smoothScrollBy(0, -200, new LinearOutSlowInInterpolator());
                return false;
            }
        });

        buttonsBinding.downDraw.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println(-1);
                ((MainActivity) context).getNoteCreator().gettextRecyclerCreator().smoothScrollBy(0, 200, new LinearOutSlowInInterpolator());
                return false;
            }
        });


    }


    @Override
    public void swichDarkMode(int color, boolean isDarkMode) {
        if (isDarkMode) {
            mainActivity.get().getEverThemeHelper().tintViewAccent(buttonsBinding.bottomBar, mainActivity.get().getEverThemeHelper().defaultTheme, 500);
            mainActivity.get().getEverThemeHelper().tintViewAccent(buttonsBinding.toolbar, mainActivity.get().getEverThemeHelper().defaultTheme, 600);
            mainActivity.get().getEverThemeHelper().tintViewAccent(buttonsBinding.frameSizeDecoy, Util.getDarkerColor(mainActivity.get().getEverThemeHelper().defaultTheme), 500);
            mainActivity.get().getEverThemeHelper().tintViewAccent(buttonsBinding.notMoreOptionsLinearLayout, mainActivity.get().getEverThemeHelper().defaultTheme, 500);
            buttonsBinding.HighlightText1.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getEverThemeHelper().defaultTheme));
            buttonsBinding.searchBox.setSearchBarColor(Color.WHITE);
        } else {
            mainActivity.get().getEverThemeHelper().tintViewAccent(buttonsBinding.bottomBar, mainActivity.get().getEverThemeHelper().defaultTheme, 500);
            mainActivity.get().getEverThemeHelper().tintViewAccent(buttonsBinding.toolbar, mainActivity.get().getEverThemeHelper().defaultTheme, 600);
            mainActivity.get().getEverThemeHelper().tintViewAccent(buttonsBinding.frameSizeDecoy, mainActivity.get().getEverThemeHelper().defaultTheme, 500);
            mainActivity.get().getEverThemeHelper().tintViewAccent(buttonsBinding.notMoreOptionsLinearLayout, mainActivity.get().getEverThemeHelper().defaultTheme, 500);
            buttonsBinding.HighlightText1.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getEverThemeHelper().defaultTheme));
            buttonsBinding.searchBox.setSearchBarColor(mainActivity.get().getColor(R.color.NightBlack));
        }

    }
}
