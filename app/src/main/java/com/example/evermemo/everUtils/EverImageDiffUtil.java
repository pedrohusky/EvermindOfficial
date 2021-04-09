package com.example.evermemo.everUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

public class EverImageDiffUtil extends DiffUtil.Callback {

    private final List<String> oldNoteList;
    private final List<String> newNoteList;

    public EverImageDiffUtil(List<String> oldEmployeeList, List<String> newEmployeeList) {
        this.oldNoteList = oldEmployeeList;
        this.newNoteList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return oldNoteList.size();
    }

    @Override
    public int getNewListSize() {
        return newNoteList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldNoteList.get(oldItemPosition), newNoteList.get(
                newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final String oldNote = oldNoteList.get(oldItemPosition);
        final String newNote = newNoteList.get(newItemPosition);

        return oldNote.equals(newNote);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}