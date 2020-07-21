package com.example.Evermind;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Evermind.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.sql.Array;
import java.sql.Struct;
import java.util.ArrayList;
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
        setContentView(R.layout.fragment_item_list);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.Evermind", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

       if (set == null) {
           notes.add("Create a new note"); }
       else {
           notes = new ArrayList<>(set); }


        //Intent intent = getIntent();

        //int noteid = intent.getIntExtra("noteId", -1);
        //String savednote = intent.getStringExtra("notes");


        // data to populate the RecyclerView with
        String[] data = notes.toArray(new String[0]);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        //int mNoOfColumns = Utility.calculateNoOfColumns(this, 2);

        //GridLayoutManager = new GridLayoutManager(this, mNoOfColumns);


        adapter = new RecyclerGridAdapter(this, data);
        //adapter.setClickListener();
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnLongClickListener(this);

        //HashSet<String> sett = new HashSet(Test.notes);
        //sharedPreferences.edit().putStringSet("notes", sett).apply();
    }


    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        String waswrote = adapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
        intent.putExtra("noteId", position);
        intent.putExtra("WhatWasWrote", waswrote);
        startActivity(intent);
    }

    //This start NoteEditor and send Array to
    public void onClick(View view) {

        //Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
        String waswrote = "";
        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
        int itemCount = adapter.getItemCount(); //MAYBE /// adapter.getItemCount() + 1 //// IS RIGHT
        intent.putExtra("noteId", itemCount);
        intent.putExtra("addnote", true);
        intent.putExtra("WhatWasWrote", waswrote);
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int noteToDelete = i;
        Toast.makeText(this, "uuuuuu", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(getApplicationContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(noteToDelete);
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

