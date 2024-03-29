package com.example.evermemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

public class EverFlowScrollView extends ScrollView {

    private boolean mScrollable = true;

    public EverFlowScrollView(Context context) {
        super(context);
    }

    public EverFlowScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EverFlowScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCanScroll(boolean enabled) {
        mScrollable = enabled;
    }

    public boolean isScrollable() {
        return mScrollable;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollable) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return mScrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!mScrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }

}