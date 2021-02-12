package com.example.Evermind.everUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.Evermind.Note_Model;
import com.example.Evermind.recycler_models.EverLinkedMap;

import java.util.List;

public class EverNoteDiffUtil extends DiffUtil.Callback {

    private final List<Note_Model> oldNoteList;
    private final List<Note_Model> newNoteList;

    public EverNoteDiffUtil(List<Note_Model> oldEmployeeList, List<Note_Model> newEmployeeList) {
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
        return oldNoteList.get(oldItemPosition).getId() == newNoteList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNoteList.get(oldItemPosition).toString().equals(newNoteList.get(newItemPosition).toString());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}