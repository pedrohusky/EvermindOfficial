package com.example.evermemo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.transition.ChangeBounds;
import androidx.transition.ChangeClipBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeScroll;
import androidx.transition.ChangeTransform;
import androidx.transition.Fade;
import androidx.transition.TransitionSet;

/**
 * Utility class for creating a default transition that automatically fades,
 * moves, and resizes views during a scene change.
 *
 * <p>An AutoTransition can be described in a resource file by using the
 * tag <code>autoTransition</code>, along with the other standard
 */
public class EverAutoTransition extends TransitionSet {

    /**
     * Constructs an AutoTransition object, which is a TransitionSet which
     * first fades out disappearing targets, then moves and resizes existing
     * targets, and finally fades in appearing targets.
     *
     */
    public EverAutoTransition() {
        init();
    }

    public EverAutoTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds());
        addTransition(new ChangeImageTransform());
    }
}