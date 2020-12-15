package com.example.Evermind;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.Evermind.recycler_models.EverLinkedMap;

import java.util.ArrayList;

public class EverDiffUtil extends DiffUtil.Callback {
    ArrayList<Note_Model> newList;
    ArrayList<Note_Model> oldList;

    public EverDiffUtil(ArrayList<Note_Model> newList, ArrayList<Note_Model> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0 ;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0 ;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

    //    int result = newList.get(newItemPosition).compareTo (oldList.get(oldItemPosition));
     //   if (result==0){
     //       System.out.println(true+ "aaaa");
     //       return true;
     //   }
        System.out.println(false + "aaaa");
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
     //   int result = newList.get(newItemPosition).compareTo (oldList.get(oldItemPosition));
     //   if (result==0){
     //       System.out.println(true+ "aaaa");
     //       return true;
     //   }
        System.out.println(false + "aaaa");
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Note_Model newNotModel = newList.get(newItemPosition);
        Note_Model oldNoteModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();
        if(newNotModel.getId() != (oldNoteModel.getId())){
            diff.putInt("id", newNotModel.getId());
        }
        if(!newNotModel.getContentsAsString().equals (oldNoteModel.getContentsAsString())){
            diff.putString("content", newNotModel.getContentsAsString());
        }
        if(!newNotModel.getTitle().equals (oldNoteModel.getTitle())){
            diff.putString("title", newNotModel.getTitle());
        }
        if(!newNotModel.getImagesAsString().equals (oldNoteModel.getImagesAsString())){
            diff.putString("images", newNotModel.getContentsAsString());
        }
        if(!newNotModel.getDrawsAsString().equals (oldNoteModel.getDrawsAsString())){
            diff.putString("draws", newNotModel.getContentsAsString());
        }
        if(!newNotModel.getNoteColor().equals (oldNoteModel.getNoteColor())){
            diff.putString("color", newNotModel.getContentsAsString());
        }
        if(newNotModel.getActualPosition() != (oldNoteModel.getActualPosition())){
            diff.putInt("position", newNotModel.getActualPosition());
        }
        if (diff.size()==0){
            return null;
        }
        return diff;
    }
}