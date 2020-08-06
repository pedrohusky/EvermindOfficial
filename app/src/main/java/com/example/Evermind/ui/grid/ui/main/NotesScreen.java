package com.example.Evermind.ui.grid.ui.main;
import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Evermind.CheckboxAdapter;
import com.example.Evermind.Checkboxlist_model;
import com.example.Evermind.DataBaseHelper;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.R;
import com.example.Evermind.RecyclerGridAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;

import static android.content.Context.MODE_PRIVATE;

public class NotesScreen extends Fragment implements RecyclerGridAdapter.ItemClickListener, AdapterView.OnItemLongClickListener {


    public static RecyclerGridAdapter adapter;
    public static ImagesRecyclerGridAdapter listadapter;
    public static DataBaseHelper databaseHelper;
    public static ArrayList<String> notes = new ArrayList<>();
    public static ArrayList<String> titles = new ArrayList<>();
    public static ArrayList<String> dates = new ArrayList<>();
    public static ArrayList<Integer> ids = new ArrayList<>();
    public static ArrayList<String> ImagesURLs = new ArrayList<>();
    public static ArrayList<Integer> ImagesIDs = new ArrayList<>();
    public static String[] ImageURL;
    public static String[] data;
    public static String[] title;
    public static String[] date;
    public static Integer[] id;

    public static int fromPosition;
    public static int toPosition;

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

        ids = databaseHelper.getIDFromDatabase();

        id = ids.toArray(new Integer[0]);

        notes = databaseHelper.getContentsFromDatabase();

        data = notes.toArray(new String[0]);

        titles = databaseHelper.getTitlesFromDatabase();

        title = titles.toArray(new String[0]);

        dates = databaseHelper.getDateFromDatabase();

        date = dates.toArray(new String[0]);

        ImagesURLs = databaseHelper.getImageURLFromDatabase();

        ImageURL = ImagesURLs.toArray(new String[0]);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                fromPosition = viewHolder.getAdapterPosition();
                toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(ids, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(ids, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                ids.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                databaseHelper.deleteNote(viewHolder.getAdapterPosition());
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        // set up the RecyclerView
        RecyclerView recyclerView = getActivity().findViewById(R.id.rvNumbers);


        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new RecyclerGridAdapter(this.getActivity(), data, title, date ); //requireContext() works too
        recyclerView.setAdapter(adapter);

        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {


        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();


        Integer id = ids.get(position);
        String title = titles.get(position);
        String content = notes.get(position);

        editor.putInt("noteId", id);
        editor.putInt("position", position);
        editor.putString("title", title);
        editor.putString("content", content);
        editor.putBoolean("athome", false);
        editor.putBoolean("newnote", false);
        editor.putBoolean("BlackHighlight?", false);
        editor.putBoolean("DeleteNSave", false);
        editor.putBoolean("UndoRedo", false);
        editor.apply();

        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_nav_home_to_nav_note);


    }

    //This start NoteEditor and send Array to
    public void onClick(View view) {

    }

    @Override
    public void onLongPress(View view, int position) {

        PopupWindowHelper popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(getActivity()).inflate(R.layout.note_customization_layout, null);
        popupWindowHelper = new PopupWindowHelper(popView);

       popupWindowHelper.showAsDropDown(view);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
         final int noteToDelete = i;

        return true;
    }
    }









