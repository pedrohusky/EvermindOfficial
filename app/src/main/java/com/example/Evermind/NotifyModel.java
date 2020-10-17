package com.example.Evermind;

import java.io.Serializable;

public class NotifyModel  implements Serializable {
    private int position;
    private boolean added;
    private boolean removed;

    public NotifyModel(int position, boolean added, boolean removed) {
        this.position = position;
        this.added = added;
        this.removed = removed;
    }
    public int getPosition() {
        return position;
    }

    public boolean isAdded() {
        return added;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

}
