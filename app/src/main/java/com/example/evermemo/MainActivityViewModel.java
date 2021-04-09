package com.example.evermemo;

import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private boolean hasStarted = false;

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public boolean hasStarted() {
        return hasStarted;

    }
}
