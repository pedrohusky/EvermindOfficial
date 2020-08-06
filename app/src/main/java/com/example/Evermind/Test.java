package com.example.Evermind;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class Test extends AppCompatActivity implements RecyclerGridAdapter.ItemClickListener, AdapterView.OnItemLongClickListener {

    public static RecyclerGridAdapter adapter;
    private Object GridLayoutManager;


    //Notes Array \/
    public static ArrayList<String> notes = new ArrayList<>();
    //Notes Array /\

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_notes);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.Evermind", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        Intent intent = getIntent();

        int noteid = intent.getIntExtra("noteId", -1);
        String savednote = intent.getStringExtra("notes");

        if (set == null) {
            if (notes.contains("Create a new note") != true) {
                notes.add("Create a new note");
            }
        } else {

            notes = new ArrayList(set);
            Collections.sort(notes, new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    return s.compareTo(t1);
                }
            });

        }


        //Intent intent = getIntent();

        //int noteid = intent.getIntExtra("noteId", -1);
        //String savednote = intent.getStringExtra("notes");


        // data to populate the RecyclerView with
        String[] data = notes.toArray(new String[0]);
        String[] titles = notes.toArray(new String[0]);
        String[] dates = notes.toArray(new String[0]);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        //adapter = new RecyclerGridAdapter(this, data, titles, dates);
        //adapter.setClickListener();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnLongClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
//        String waswrote = adapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
        intent.putExtra("noteId", position);
       // intent.putExtra("WhatWasWrote", waswrote);
        startActivity(intent);
    }

    //This start NoteEditor and send Array to
    public void onClick(View view) {

        //Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
        String waswrote = "";
        Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
        int itemCount = adapter.getItemCount(); //MAYBE /// adapter.getItemCount() + 1 //// IS RIGHT
        intent.putExtra("noteId", itemCount);
        intent.putExtra("addnote", true);
        intent.putExtra("WhatWasWrote", waswrote);
        startActivity(intent);

    }

    @Override
    public void onLongPress(View view, int position) {

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
       // final int noteToDelete = i;
        Toast.makeText(this, "uuuuuu", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(getApplicationContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //notes.remove(noteToDelete);
                                adapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.Evermind", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet(Test.notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();

                            }
                        }
                )
                .setNegativeButton("No", null)
                .show();

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    //public static class Utility {
    // public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
    //    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    //  float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
    //   int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
    //  return noOfColumns;
    // }
    // }
}

















        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);

        //notes.add("CUUUUU");
        //notes.add("aaaaaaaaaaaa" +
        //        "" +
        //        "" +
        //        "" +
        //       "");

        //ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        //recyclerView.setAdapter(arrayAdapter);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    recyclerView.setElevation(10);
     //   }
    //}
//}

