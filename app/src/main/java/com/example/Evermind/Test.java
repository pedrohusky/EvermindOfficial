package com.example.Evermind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import java.sql.Struct;
import java.util.ArrayList;

public class Test extends AppCompatActivity implements RecyclerGridAdapter.ItemClickListener {

    RecyclerGridAdapter adapter;
    private Object GridLayoutManager;


    public static ArrayList<String> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_item_list);

        Intent intent = getIntent();
        Object savednotes = intent.getStringExtra("notes");
        if (savednotes != null) {
            notes.add(savednotes.toString());
        } else {
            notes.add("Create a new note");
        }

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
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }

    public void onClick(View view) {
        Intent i = new Intent(this, NoteActivity.class);
        i.putExtra("notes", this.notes);
        startActivity(i);

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

