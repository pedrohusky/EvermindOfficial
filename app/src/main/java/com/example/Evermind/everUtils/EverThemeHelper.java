package com.example.Evermind.everUtils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.ImageView;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.Evermind.MainActivity;
import com.example.Evermind.R;

import java.lang.ref.WeakReference;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class EverThemeHelper {

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
    }

    public void tintSystemBars(int color, int duration) {

        setDarkStatusBar();

        // Initial colors of each system bar.
        final int statusBarColor = mainActivity.get().everMainWindow.getStatusBarColor();
        final int buttonsColor = actualColor;
        // final int toolbarColor = everMainWindow.getStatusBarColor();

        // Desired final colors of each bar.
        final int buttonToColor;
        if (color == defaultTheme) {
            buttonToColor = R.color.Black;
        } else {
            buttonToColor = color;
        }
        final int statusBarToColor = color;
        //  final int toolbarToColor = color;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(animation -> {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = blendColors(statusBarColor, statusBarToColor, position);
                int buttonsfinalColor = blendColors(buttonsColor, buttonToColor, position);
                mainActivity.get().everMainWindow.setStatusBarColor(blended);

                // Apply blended color to the ActionBar.
                //   blended = blendColors(toolbarColor, toolbarToColor, position);
                //  ColorDrawable background = new ColorDrawable(blended);
                //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
                mainActivity.get().Save.setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                mainActivity.get().Delete.setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                mainActivity.get().Undo.setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                mainActivity.get().Redo.setImageTintList(ColorStateList.valueOf(buttonsfinalColor));

                ((ImageView)mainActivity.get().findViewById(R.id.imageAudioDecoy)).setBackgroundColor(blended);

                mainActivity.get().note_bottom_bar.setBarIndicatorColor(blended);

                if (mainActivity.get().drawing) {
                    pendingColorChange = true;
                    pendingColor = color;
                } else {
                    mainActivity.get().cardNoteCreator.get().setBackgroundTintList(ColorStateList.valueOf(blended));
                }
            });
            anim.setInterpolator(new LinearOutSlowInInterpolator());
            anim.setDuration(duration).start();
            actualColor = color;
        }, 15);
    }

    public int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    public void tintView(View view, int color) {

        // Initial colors of each system bar.
        final int statusBarColor;
        if (view.getTag().equals("audioImageDecoy")) {
            statusBarColor = view.getSolidColor();
        } else {
             statusBarColor = view.getBackgroundTintList().getDefaultColor();
        }
        // final int toolbarColor = everMainWindow.getStatusBarColor();

        final int metacolor = mainActivity.get().everBallsHelper.getMainButtonColor();
        // Desired final colors of each bar.
        final int statusBarToColor = color;
        //  final int toolbarToColor = color;


        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(animation -> {
            // Use animation position to blend colors.
            float position = animation.getAnimatedFraction();

            // Apply blended color to the status bar.
            int blended = blendColors(statusBarColor, statusBarToColor, position);
            int blended1 = blendColors(metacolor, statusBarToColor, position);
            // Apply blended color to the ActionBar.
            //   blended = blendColors(toolbarColor, toolbarToColor, position);
            //  ColorDrawable background = new ColorDrawable(blended);
            //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
            if (view == mainActivity.get().note_bottom_bar) {
                mainActivity.get().note_bottom_bar.setBarIndicatorColor(blended);
            } else if (view == mainActivity.get().everBallsHelper.metaColors) {
                mainActivity.get().everBallsHelper.setMainButtonColor(blended1);
            } else if (view.getTag().equals("audioImageDecoy")) {
                view.setBackgroundColor(blended);
            } else {
                view.setBackgroundTintList(ColorStateList.valueOf(blended));
            }
        });
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        anim.setDuration(500).start();
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
        if (actualColor != getColor(R.color.White)) {
            tintSystemBars(getColor(R.color.White), 500);
        }
        new Handler(Looper.getMainLooper()).postDelayed(this::setLightStatusBar, 250);
    }



}
