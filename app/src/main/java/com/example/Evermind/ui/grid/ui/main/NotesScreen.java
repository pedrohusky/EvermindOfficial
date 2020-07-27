package com.example.Evermind.ui.grid.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Evermind.DataBaseHelper;
import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteEditorActivity;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.RecyclerGridAdapter;
import com.example.Evermind.Test;
import com.example.Evermind.ui.dashboard.NoteEditorFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import static android.content.Context.MODE_PRIVATE;

public class NotesScreen extends Fragment implements RecyclerGridAdapter.ItemClickListener, AdapterView.OnItemLongClickListener {


    public static RecyclerGridAdapter adapter;
    public static DataBaseHelper databaseHelper;

    //Notes Array \/
    public static ArrayList<String> notes = new ArrayList<>();
    //Notes Array /\
    public static ArrayList<String> titles = new ArrayList<>();
    public static ArrayList<String> dates = new ArrayList<>();
    public static ArrayList<Integer> ids = new ArrayList<>();

    public static ArrayList<Integer> text_colors = new ArrayList<>();
    public static ArrayList<String> text_colors_texts = new ArrayList<>();
    public static ArrayList<Integer> background_colors = new ArrayList<>();
    public static ArrayList<String> background_colors_texts = new ArrayList<>();
    public static ArrayList<String> striketroughs = new ArrayList<>();
    public static ArrayList<String> underlines = new ArrayList<>();
    public static ArrayList<String> set_subscripts = new ArrayList<>();
    public static ArrayList<String> clickable_texts = new ArrayList<>();
    public static ArrayList<Integer> clickable_texts_colors = new ArrayList<>();

    private MainViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        databaseHelper = new DataBaseHelper(getActivity());
        return inflater.inflate(R.layout.grid_notes_home_screen, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

            notes = databaseHelper.getContentsFromDatabase();
            titles = databaseHelper.getTitlesFromDatabase();
            dates = databaseHelper.getDateFromDatabase();
            ids = databaseHelper.getIDFromDatabase();
            text_colors = databaseHelper.getTextColorFromDatabase();
            text_colors_texts = databaseHelper.getTextColorTextFromDatabase();
            background_colors = databaseHelper.getBackgroundColorFromDatabase();
            background_colors_texts = databaseHelper.getBackgroundColorTextFromDatabase();
            striketroughs = databaseHelper.getStriketroughFromDatabase();
            underlines = databaseHelper.getUnderlineFromDatabase();
            set_subscripts = databaseHelper.getSetSubscriptFromDatabase();
            clickable_texts = databaseHelper.getClickableTextFromDatabase();
            clickable_texts_colors = databaseHelper.getClickableTextColorFromDatabase();

            //titles = new ArrayList(title_content_from_device);
           // Collections.sort(notes, new Comparator<String>() { // Sort alphabetically TODO
           //     @Override
           //     public int compare(String s, String t1) {
          //          return s.compareTo(t1);
          //      }
           // });




        // data to populate the RecyclerView with


        String[] data = notes.toArray(new String[0]);
        String[] title = titles.toArray(new String[0]);
        String[] date = dates.toArray(new String[0]);
        Integer[] id = ids.toArray(new Integer[0]);
        Integer[] text_color = text_colors.toArray(new Integer[0]);
        String[] text_color_text = text_colors_texts.toArray(new String[0]);
        Integer[] background_color = background_colors.toArray(new Integer[0]);
        String[] background_color_text = background_colors_texts.toArray(new String[0]);
        String[] striketrough = striketroughs.toArray(new String[0]);
        String[] underline = underlines.toArray(new String[0]);
        String[] set_subscript = set_subscripts.toArray(new String[0]);
        String[] clickable_text = clickable_texts.toArray(new String[0]);
        Integer[] clickable_text_color = clickable_texts_colors.toArray(new Integer[0]);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        // set up the RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.rvNumbers);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
/// TODO \/
        adapter = new RecyclerGridAdapter(this.getActivity(), data, title, date, text_color, text_color_text, background_color, background_color_text, striketrough, underline, set_subscript, clickable_text, clickable_text_color); //requireContext() works too
        recyclerView.setAdapter(adapter);
 ///       TODO /\
        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {


        String title_content = titles.get(position);
        Integer id = ids.get(position);
        String waswrote = notes.get(position);


        Bundle newBundle = new Bundle();
        newBundle.putString("Content", waswrote);
        newBundle.putString("title", title_content);
        newBundle.putInt("noteId", id);
        newBundle.putBoolean("addnote", true);
        Fragment fragment = new NoteEditorFragment();
        fragment.setArguments(newBundle);


        EditText editText = this.getActivity().findViewById(R.id.myEditText);
        editText.setVisibility(View.VISIBLE);
        BottomNavigationView bottomNavigationView = this.getActivity().findViewById(R.id.navigation_note);
        bottomNavigationView.setVisibility(View.VISIBLE);



        //Handles ID to delete note //
        SharedPreferences preferences= getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("noteId", id);
        editor.commit();



        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_nav_home_to_nav_note, newBundle);




    }

    //This start NoteEditor and send Array to
    public void onClick(View view) {

        //Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
       // String waswrote = "";
     //   Intent intent = new Intent(getActivity(), NoteEditorActivity.class);
     //   int itemCount = adapter.getItemCount(); //MAYBE /// adapter.getItemCount() + 1 //// IS RIGHT
      //  intent.putExtra("noteId", itemCount);
      //  intent.putExtra("addnote", true);
      //  intent.putExtra("WhatWasWrote", waswrote);
      //  startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        // final int noteToDelete = i;
        Toast.makeText(getActivity(), "uuuuuu", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //notes.remove(noteToDelete);
                                adapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.Evermind", MODE_PRIVATE);
                                HashSet<String> set = new HashSet(Test.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();

                            }
                        }
                )
                .setNegativeButton("No", null)
                .show();

        return true;
    }
}









