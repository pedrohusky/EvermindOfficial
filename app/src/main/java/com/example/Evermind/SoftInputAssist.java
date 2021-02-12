package com.example.Evermind;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SoftInputAssist {
    @Nullable
    private View rootView;
    @Nullable
    private ViewGroup contentContainer;
    @Nullable
    private ViewTreeObserver viewTreeObserver;
    private final ViewTreeObserver.OnGlobalLayoutListener listener = () -> possiblyResizeChildOfContent();
    private final Rect contentAreaOfWindowBounds = new Rect();
    @NonNull
    private final FrameLayout.LayoutParams rootViewLayout;
    private int usableHeightPrevious = 0;

    public SoftInputAssist(@NonNull Activity activity) {
        contentContainer = activity.findViewById(android.R.id.content);
        rootView = contentContainer.getChildAt(0);
        rootViewLayout = (FrameLayout.LayoutParams) rootView.getLayoutParams();
    }

    public void onPause() {
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnGlobalLayoutListener(listener);
        }
    }

    public void onResume() {
        if (viewTreeObserver == null || !viewTreeObserver.isAlive()) {
            viewTreeObserver = rootView.getViewTreeObserver();
        }

        viewTreeObserver.addOnGlobalLayoutListener(listener);
    }

    public void onDestroy() {
        rootView = null;
        contentContainer = null;
        viewTreeObserver = null;
    }

    private void possiblyResizeChildOfContent() {
        contentContainer.getWindowVisibleDisplayFrame(contentAreaOfWindowBounds);
        int usableHeightNow = contentAreaOfWindowBounds.height();

        if (usableHeightNow != usableHeightPrevious) {
            rootViewLayout.height = usableHeightNow;
            rootView.layout(contentAreaOfWindowBounds.left, contentAreaOfWindowBounds.top, contentAreaOfWindowBounds.right, contentAreaOfWindowBounds.bottom);
            rootView.requestLayout();

            usableHeightPrevious = usableHeightNow;
        }
    }
}
