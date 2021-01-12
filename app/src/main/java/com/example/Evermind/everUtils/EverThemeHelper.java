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

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.EverWave;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;

import java.lang.ref.WeakReference;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class EverThemeHelper implements EverInterfaceHelper.OnChangeColorListener {

    private final WeakReference<MainActivity> mainActivity;
    public int defaultTheme;
    public int accentTheme;
    public int actualColor;
    public int pendingColor;
    public boolean pendingColorChange = false;

    public EverThemeHelper(Context context) {
        mainActivity = new WeakReference<>(((MainActivity)context));
        defaultTheme = getColor(R.color.White);
        accentTheme = getColor(R.color.Grey);
      //  EverInterfaceHelper.getInstance().setColorListener(this);
    }

    public void tintSystemBars(int color, int duration) {

        setDarkStatusBar();

        // Initial colors of each system bar.
        final int statusBarColor = mainActivity.get().getEverMainWindow().getStatusBarColor();
        final int buttonsColor = actualColor;
       // final int cardFromColor = mainActivity.get().findViewById(R.id.toColorLinearLayoutNoteCreator).getSolidColor();
        // final int toolbarColor = everMainWindow.getStatusBarColor();

        // Desired final colors of each bar.
        final int buttonToColor;
        if (color == defaultTheme) {
            buttonToColor = R.color.Black;
        } else {
            buttonToColor = color;
        }
        final int toColor = color;
        //  final int toolbarToColor = color;
        new Handler(Looper.getMainLooper()).post(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(animation -> {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = blendColors(statusBarColor, toColor, position);
              ///  int cardColor = blendColors(cardFromColor, toColor, position);
                int buttonsfinalColor = blendColors(buttonsColor, buttonToColor, position);

                mainActivity.get().getEverMainWindow().setStatusBarColor(blended);


                // Apply blended color to the ActionBar.
                //   blended = blendColors(toolbarColor, toolbarToColor, position);
                //  ColorDrawable background = new ColorDrawable(blended);
                //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
                mainActivity.get().getEverViewManagement().getSave().setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                mainActivity.get().getEverViewManagement().getDelete().setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                mainActivity.get().getEverViewManagement().getUndo().setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                mainActivity.get().getEverViewManagement().getRedo().setImageTintList(ColorStateList.valueOf(buttonsfinalColor));

                if (mainActivity.get().getEverViewManagement().getAudioOptions() != null) {
                    mainActivity.get().getEverViewManagement().getAudioOptions().findViewById(R.id.imageAudioDecoy).setBackgroundColor(blended);
                }

                mainActivity.get().getEverViewManagement().getBottomBar().setBarIndicatorColor(blended);

                if (mainActivity.get().getEverViewManagement().isDrawing()) {
                    pendingColorChange = true;
                    pendingColor = color;
                } else {
                 //   System.out.println(cardColor);
                    if (!mainActivity.get().isAtHome()) {
                     //   mainActivity.get().findViewById(R.id.nestedScroll).setBackgroundColor(blended);
                        mainActivity.get().getNoteCreator().getBinding().toColorLinearLayoutNoteCreator.setBackgroundTintList(ColorStateList.valueOf(blended));
                    }
                }
            });
            anim.setInterpolator(new LinearOutSlowInInterpolator());
            anim.setDuration(duration).start();
            actualColor = color;
        });
    }

    public int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    public void tintView(View view, int color, int duration) {
        int finalDuration = duration;
        if (duration == 0) {
            finalDuration = 500;
        }

         int statusColor = -1;
        if (view.getTag().equals("audioImageDecoy")) {
            statusColor = view.getSolidColor();
        } else if (!view.getTag().equals("wave")){
             statusColor = view.getBackgroundTintList().getDefaultColor();
        }
        final int statusBarColor = statusColor;

        int metaColor = -1;
        if (mainActivity.get().getEverBallsHelper() != null) {
            metaColor = mainActivity.get().getEverBallsHelper().getMainButtonColor();
        }
        final int metacolor = metaColor;

        int waveSeekColor = -1;
        if (view.getTag().equals("wave")) {
            waveSeekColor = ((EverWave)view).getWaveProgressColor();
        }
        final int waveColor = waveSeekColor;
        //  final int toolbarToColor = color;

        final int toColor = color;

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(animation -> {

            float position = animation.getAnimatedFraction();

            int blended = blendColors(statusBarColor, toColor, position);
            int blended1 = blendColors(metacolor, toColor, position);
            int blend2 = blendColors(waveColor , toColor, position);

            if (mainActivity.get().getEverViewManagement() != null) {

                if (view == mainActivity.get().getEverViewManagement().getBottomBar()) {
                    mainActivity.get().getEverViewManagement().getBottomBar().setBarIndicatorColor(blended);
                }
                else if (view == mainActivity.get().getEverBallsHelper().metaColors) {
                    mainActivity.get().getEverBallsHelper().setMainButtonColor(blended1);
                }
                else if (view.getTag().equals("audioImageDecoy")) {
                    view.setBackgroundColor(blended);
                }
                else if (view.getTag().equals("wave")) {
                    ((EverWave)view).setWaveProgressColor(blend2);
                }
                else {
                    view.setBackgroundTintList(ColorStateList.valueOf(blended));
                }
            }
        });
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        anim.setDuration(finalDuration).start();
    }

    private void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowInsetsController windowInsetsController = mainActivity.get().getWindow().getDecorView().getWindowInsetsController(); // get current flag
            windowInsetsController.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
            mainActivity.get().getWindow().setStatusBarColor(Color.GRAY); // optional
        }
    }

    private void setDarkStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowInsetsController windowInsetsController = mainActivity.get().getWindow().getDecorView().getWindowInsetsController(); // get current flag
            windowInsetsController.setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);
        }
    }

    private int getColor(int color) {
        return mainActivity.get().getColor(color);
    }

    public void clearOnBack() {

        tintSystemBars(getColor(R.color.White), 500);

        //EverInterfaceHelper.getInstance().removeColorListener(this);

        new Handler(Looper.getMainLooper()).postDelayed(this::setLightStatusBar, 250);
    }


    @Override
    public void changeColor(int color) {
        tintSystemBars(color, 1000);
    }
}
