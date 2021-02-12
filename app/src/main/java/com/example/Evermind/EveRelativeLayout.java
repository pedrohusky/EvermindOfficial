package com.example.Evermind;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

public class EveRelativeLayout extends RelativeLayout {

    private final int[] mInsets = new int[4];

    public EveRelativeLayout(Context context) {
        super(context);
    }

    public EveRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EveRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EveRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.getSystemWindowInsetLeft();
            mInsets[1] = insets.getSystemWindowInsetTop();
            mInsets[2] = insets.getSystemWindowInsetRight();
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
                    insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }
}