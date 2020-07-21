package com.example.Evermind.ui.grid.ui.main;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Evermind.NoteActivity;
import com.example.Evermind.R;
import com.example.Evermind.RecyclerGridAdapter;

import java.util.ArrayList;

public class TestegrfidFragment extends Fragment {

    public static RecyclerGridAdapter adapter;
    private Object GridLayoutManager;

    //Notes Array \/
    public static ArrayList<String> notes = new ArrayList<>();
    //Notes Array /\

    private MainViewModel mViewModel;

    ArrayList<String> getNotes = new ArrayList<>();

    public static TestegrfidFragment newInstance() {
        return new TestegrfidFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        //GridView maingrid = (GridView) getView().findViewById(R.id.mainGrid);
        //notes.add("Example");
        //ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, notes);
        //maingrid.setAdapter(arrayAdapter);

        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }


        @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);


        Intent intent = getActivity().getIntent();

            int noteid = intent.getIntExtra("notesId", -1);
            if (noteid != -1) {
                String savednotes = intent.getStringExtra("notes");
                notes.set(noteid, savednotes);
            } else {

                //  String or Object? \/  //
                String savednotes = intent.getStringExtra("notes");
                if (savednotes != null) {
                    notes.add(savednotes.toString());
                } else {
                    notes.add("Create a new note");
                }

            }


            // data to populate the RecyclerView with
            String[] data = notes.toArray(new String[0]);


            // set up the RecyclerView
            RecyclerView recyclerView = getActivity().findViewById(R.id.rvNumbers);
            int numberOfColumns = 2;
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

            //int mNoOfColumns = Utility.calculateNoOfColumns(this, 2);

            //GridLayoutManager = new GridLayoutManager(this, mNoOfColumns);


            adapter = new RecyclerGridAdapter(getActivity(), data);
            //adapter.setClickListener();
            adapter.setClickListener((AdapterView.OnItemClickListener) this.getActivity());
            recyclerView.setAdapter(adapter);
        }

    //@Override
    public void onItemClick(View view, int position) {
        //Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        String waswrote = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra("noteId", position);
        intent.putExtra("WhatWasWrote", waswrote);
        startActivity(intent);
    }

    //This start NoteEditor and send Array to
    public void onClick(View view) {

        //Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
        String waswrote = "";
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        int itemCount = adapter.getItemCount(); //MAYBE /// adapter.getItemCount() + 1 //// IS RIGHT
        intent.putExtra("noteId", itemCount);
        intent.putExtra("WhatWasWrote", waswrote);
        startActivity(intent);

    }

}