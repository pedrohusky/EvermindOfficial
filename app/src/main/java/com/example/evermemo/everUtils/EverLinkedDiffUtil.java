package com.example.evermemo.everUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.evermemo.recycler_models.EverLinkedMap;

import java.util.List;

public class EverLinkedDiffUtil extends DiffUtil.Callback {

    private final List<EverLinkedMap> oldNoteList;
    private final List<EverLinkedMap> newNoteList;

    public EverLinkedDiffUtil(List<EverLinkedMap> oldEmployeeList, List<EverLinkedMap> newEmployeeList) {
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
        final EverLinkedMap oldNote = oldNoteList.get(oldItemPosition);
        final EverLinkedMap newNote = newNoteList.get(newItemPosition);

        return oldNote.toString().equals(newNote.toString());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final EverLinkedMap oldNote = oldNoteList.get(oldItemPosition);
        final EverLinkedMap newNote = newNoteList.get(newItemPosition);

        return oldNote.toString().equals(newNote.toString());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}