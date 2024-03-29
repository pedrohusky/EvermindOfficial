package com.example.evermemo;

import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

public class EverTransition extends TransitionSet {
    public EverTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds());
        addTransition(new ChangeClipBounds());
        addTransition(new ChangeTransform());
        addTransition(new ChangeImageTransform());
    }
}