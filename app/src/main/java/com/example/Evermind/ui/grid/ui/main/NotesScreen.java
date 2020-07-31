package com.example.Evermind.ui.grid.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Evermind.DataBaseHelper;
import com.example.Evermind.R;
import com.example.Evermind.RecyclerGridAdapter;
import com.example.Evermind.Test;
import com.example.Evermind.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
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
    public static String[] data;
    public static String[] title;
    public static String[] date;
    public static Integer[] id;

    private MainViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        databaseHelper = new DataBaseHelper(getActivity());
        return inflater.inflate(R.layout.home_screen_notes, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);



            notes = databaseHelper.getContentsFromDatabase();

            data = notes.toArray(new String[0]);

            titles = databaseHelper.getTitlesFromDatabase();

            title = titles.toArray(new String[0]);

            ids = databaseHelper.getIDFromDatabase();

            id = ids.toArray(new Integer[0]);

            dates = databaseHelper.getDateFromDatabase();

            date = dates.toArray(new String[0]);


          //  ids = databaseHelper.getIDFromDatabase();

            //titles = new ArrayList(title_content_from_device);
           // Collections.sort(notes, new Comparator<String>() { // Sort alphabetically TODO
           //     @Override
           //     public int compare(String s, String t1) {
          //          return s.compareTo(t1);
          //      }
           // });




        // data to populate the RecyclerView with


   //     String[] data = notes.toArray(new String[0]);
     //   String[] title = titles.toArray(new String[0]);
      //  String[] date = dates.toArray(new String[0]);
      //  Integer[] id = ids.toArray(new Integer[0]);




        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        // set up the RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.rvNumbers);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new RecyclerGridAdapter(this.getActivity(), data, title, date); //requireContext() works too
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

    }

    @Override
    public void onItemClick(View view, int position) {


        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();


        Integer id = ids.get(position);

        editor.putInt("noteId", id);
        editor.putBoolean("athome", false);
        editor.apply();


        EditText editText = this.getActivity().findViewById(R.id.myEditText);
        editText.setVisibility(View.VISIBLE);
        BottomNavigationView bottomNavigationView = this.getActivity().findViewById(R.id.navigation_note);
        bottomNavigationView.setVisibility(View.VISIBLE);

        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_nav_home_to_nav_note);


    }

    //This start NoteEditor and send Array to
    public void onClick(View view) {

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









