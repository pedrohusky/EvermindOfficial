package com.example.Evermind.everUtils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.EverWave;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cafe.adriel.androidaudiorecorder.Util;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class EverThemeHelper implements EverInterfaceHelper.OnChangeColorListener {

    @NonNull
    private final WeakReference<MainActivity> mainActivity;
    public int defaultTheme;
    public int accentTheme;
    public int actualAccentColor;
    public int pendingColor;
    public boolean pendingColorChange = false;

    public boolean isDarkMode() {
        return isDarkMode;
    }

    private boolean isDarkMode = false;
    public EverThemeHelper(Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
        defaultTheme = getColor(R.color.White);
        accentTheme = getColor(R.color.Black);
    }

    public void tintSystemBarsAccent(int color, int duration) {

        // Initial colors of each system bar.
        final int statusBarColor = mainActivity.get().getEverMainWindow().getStatusBarColor();


        if (mainActivity.get().getEverThemeHelper().isDarkMode) {
            setDarkStatusBar();
        } else {
            setLightStatusBar();
        }

        final int toColor = color;

      //  new Handler(Looper.getMainLooper()).post(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(animation -> {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = blendColors(statusBarColor, toColor, position);

                if (mainActivity.get().getEverThemeHelper().isDarkMode) {
                    int blendedD = blendColors(statusBarColor, mainActivity.get().getColor(R.color.NightBlack), position);
                    mainActivity.get().getEverMainWindow().setStatusBarColor(blendedD);
                    mainActivity.get().getButtonsBinding().frameSizeDecoy.setBackgroundColor(blendedD);

                } else {
                    mainActivity.get().getEverMainWindow().setStatusBarColor(blended);
                    mainActivity.get().getButtonsBinding().frameSizeDecoy.setBackgroundColor(blended);
                }


                // Apply blended color to the ActionBar.
                //   blended = blendColors(toolbarColor, toolbarToColor, position);
                //  ColorDrawable background = new ColorDrawable(blended);
                //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
                if (mainActivity.get().getEverViewManagement().getAudioOptions() != null) {
                    mainActivity.get().getEverViewManagement().getAudioOptions().findViewById(R.id.imageAudioDecoy).setBackgroundColor(blended);
                }


                mainActivity.get().getEverViewManagement().getBottomBar().setBarIndicatorColor(blended);

                    //   System.out.println(cardColor);
                    if (!mainActivity.get().isAtHome()) {
                        //   mainActivity.get().findViewById(R.id.nestedScroll).setBackgroundColor(blended);
                        if (!mainActivity.get().getEverThemeHelper().isDarkMode) {
                            if (mainActivity.get().getNoteCreator() != null) {
                                Objects.requireNonNull(mainActivity.get().getNoteCreator().getBinding().toColorLinearLayoutNoteCreator).setBackgroundTintList(ColorStateList.valueOf(blended));
                            }
                        }
                    }
            });
            anim.setInterpolator(new LinearOutSlowInInterpolator());
            anim.setDuration(duration).start();
         //   actualAccentColor = color;
       // });
    }

    public int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    public void tintViewAccent(@NonNull View view, int color, int duration) {
        int finalDuration = duration;
        if (duration == 0) {
            finalDuration = 500;
        }

        if (view.getTag() == null) {
            view.setTag("");
        }


        switch (view.getTag().toString()) {
            case "imageDecoy" :
                int imageFromColor;
                imageFromColor = view.getSolidColor();
                final int finalImageFromColor = imageFromColor;
                ValueAnimator iA = ValueAnimator.ofFloat(0, 1);
                iA.addUpdateListener(animation -> {
                    float p = (float) animation.getAnimatedValue();
                    int c = blendColors(finalImageFromColor, color, p);
                    view.setBackgroundColor(c);

                });
                iA.setInterpolator(new LinearOutSlowInInterpolator());
                iA.setDuration(finalDuration).start();
                break;

            case "wave" :
                EverWave wave = ((EverWave)view);
                int waveSeekColor;
                waveSeekColor = wave.getWaveProgressColor();
                final int waveFromColor = waveSeekColor;
                ValueAnimator wA = ValueAnimator.ofFloat(0, 1);
                wA.addUpdateListener(animation -> {
                    float p = (float) animation.getAnimatedValue();
                    int c = blendColors(waveFromColor, color, p);
                    wave.setWaveProgressColor(c);
                });
                wA.setInterpolator(new LinearOutSlowInInterpolator());
                wA.setDuration(finalDuration).start();
                break;

            case "card" :
                CardView cardView = ((CardView)view);
                int cardBkgColor;
                cardBkgColor = cardView.getCardBackgroundColor().getDefaultColor();
                final int finalCardBkgColor = cardBkgColor;
                ValueAnimator cA = ValueAnimator.ofFloat(0, 1);
                cA.addUpdateListener(animation -> {
                    float p = (float) animation.getAnimatedValue();
                    int c = blendColors(finalCardBkgColor, color, p);
                    cardView.setCardBackgroundColor(c);
                });
                cA.setInterpolator(new LinearOutSlowInInterpolator());
                cA.setDuration(finalDuration).start();
                break;

            case "imageView" :
                ImageView img = ((ImageView)view);
                int imageViewFromColor;
                imageViewFromColor = img.getImageTintList().getDefaultColor();
                final int finalImageViewFromColor  = imageViewFromColor;
                ValueAnimator imgA = ValueAnimator.ofFloat(0, 1);
                imgA.addUpdateListener(animation -> {
                    float p = (float) animation.getAnimatedValue();
                    int c = blendColors(finalImageViewFromColor, color, p);
                    img.setImageTintList(ColorStateList.valueOf(c));
                });
                imgA.setInterpolator(new LinearOutSlowInInterpolator());
                imgA.setDuration(finalDuration).start();
                break;

            case "bottomBar" :
                if (isDarkMode) {
                    int bottomFromColor;
                    bottomFromColor =  mainActivity.get().getEverViewManagement().getBottomBar().getBarBackgroundColor();
                    final int finalBottomFromColor = bottomFromColor;
                    ValueAnimator bA = ValueAnimator.ofFloat(0, 1);
                    bA.addUpdateListener(animation -> {
                        float p = (float) animation.getAnimatedValue();
                        int c = blendColors(finalBottomFromColor, mainActivity.get().getColor(R.color.NightBlack), p);
                        mainActivity.get().getEverViewManagement().getBottomBar().setBarBackgroundColor(c);
                    });
                    bA.setInterpolator(new LinearOutSlowInInterpolator());
                    bA.setDuration(finalDuration).start();
                    int bottomAccentFromColor;
                    bottomAccentFromColor =  mainActivity.get().getEverViewManagement().getBottomBar().getBarIndicatorColor();
                    final int finalBottomAccentFromColor = bottomAccentFromColor;
                    ValueAnimator bAc = ValueAnimator.ofFloat(0, 1);
                    bAc.addUpdateListener(animation -> {
                        float p = (float) animation.getAnimatedValue();
                        int c = blendColors(finalBottomAccentFromColor, color, p);
                        mainActivity.get().getEverViewManagement().getBottomBar().setBarIndicatorColor(Util.getDarkerColor(c));
                    });
                    bAc.setInterpolator(new LinearOutSlowInInterpolator());
                    bAc.setDuration(finalDuration).start();
                } else {
                    int bottomFromColor;
                    bottomFromColor =  mainActivity.get().getEverViewManagement().getBottomBar().getBarIndicatorColor();
                    final int finalBottomFromColor = bottomFromColor;
                    ValueAnimator bA = ValueAnimator.ofFloat(0, 1);
                    bA.addUpdateListener(animation -> {
                        float p = (float) animation.getAnimatedValue();
                        int c = blendColors(finalBottomFromColor, color, p);
                        mainActivity.get().getEverViewManagement().getBottomBar().setBarIndicatorColor(c);
                    });
                    bA.setInterpolator(new LinearOutSlowInInterpolator());
                    bA.setDuration(finalDuration).start();
                }

                break;

            case "toolbar":
                int toolbarDefaultFromColor;
                toolbarDefaultFromColor = mainActivity.get().getButtonsBinding().toolbar.getBackgroundTintList().getDefaultColor();
                final int finalToolbarDefaultFromColor = toolbarDefaultFromColor;
                ValueAnimator tA = ValueAnimator.ofFloat(0, 1);
                tA.addUpdateListener(animation -> {
                    float p = (float) animation.getAnimatedValue();
                    int c = blendColors(finalToolbarDefaultFromColor, color, p);
                    mainActivity.get().getButtonsBinding().toolbar.setBackgroundTintList(ColorStateList.valueOf(c));
                });
                tA.setInterpolator(new LinearOutSlowInInterpolator());
                tA.setDuration(finalDuration).start();
                break;

            case "":
                int defaultFromColor;
                if (view.getBackgroundTintList() != null) {
                    defaultFromColor = view.getBackgroundTintList().getDefaultColor();
                } else {
                    defaultFromColor = -1;
                }
                final int finalDefaultFromColor = defaultFromColor;
                ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
                anim.addUpdateListener(animation -> {
                    float p = (float) animation.getAnimatedValue();
                    int c = blendColors(finalDefaultFromColor, color, p);
                    view.setBackgroundTintList(ColorStateList.valueOf(c));
                });
                anim.setInterpolator(new LinearOutSlowInInterpolator());
                anim.setDuration(finalDuration).start();
                break;


        }

    }

    private void setLightStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = mainActivity.get().getWindow().getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void setDarkStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = mainActivity.get().getWindow().getDecorView();
            decor.setSystemUiVisibility(0);
        }
    }

    private int getColor(int color) {
        return mainActivity.get().getColor(color);
    }


    @Override
    public void changeAccentColor(int color) {
        tintSystemBarsAccent(color, 500);
    }

    public void enterDarkMode() {
        if (!isDarkMode) {
            isDarkMode = true;
            setDarkStatusBar();
            accentTheme = getColor(android.R.color.darker_gray);
            defaultTheme = getColor(R.color.NightBlack);
            tintSystemBarsAccent(Util.getDarkerColor(actualAccentColor), 500);
            EverInterfaceHelper.getInstance().enterDarkMode(actualAccentColor, true);
        } else {
            isDarkMode = false;
            setLightStatusBar();
            accentTheme = getColor(R.color.Black);
            defaultTheme = getColor(R.color.White);
            tintSystemBarsAccent(actualAccentColor, 500);
            EverInterfaceHelper.getInstance().enterDarkMode(actualAccentColor, false);
        }
    }
}
