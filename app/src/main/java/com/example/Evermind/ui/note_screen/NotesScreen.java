package com.example.Evermind.ui.note_screen;
import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.example.Evermind.EverDataBase;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.RecyclerGridAdapter;
import com.muehlemann.giphy.GiphyLibrary;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import static android.content.Context.MODE_PRIVATE;

public class NotesScreen extends Fragment implements RecyclerGridAdapter.ItemClickListener, AdapterView.OnItemLongClickListener {


    public RecyclerGridAdapter adapter;
    public  EverDataBase databaseEver;
    public  ArrayList<String> notes = new ArrayList<>();
    public  ArrayList<String> titles = new ArrayList<>();
    public  ArrayList<String> dates = new ArrayList<>();
    public  ArrayList<Integer> ids = new ArrayList<>();
    public  ArrayList<String> ImagesURLs = new ArrayList<>();
    public  String[] ImageURL;
    public  String[] data;
    public  String[] title;
    public  String[] date;
    public  Integer[] id;

    public static int fromPosition;
    public static int toPosition;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        databaseEver = new EverDataBase(requireActivity());

        ids = databaseEver.getIDFromDatabase();

        id = ids.toArray(new Integer[0]);

        notes = databaseEver.getContentsFromDatabase();

        data = notes.toArray(new String[0]);

        titles = databaseEver.getTitlesFromDatabase();

        title = titles.toArray(new String[0]);

        dates = databaseEver.getDateFromDatabase();

        date = dates.toArray(new String[0]);

        ImagesURLs = databaseEver.getImageURLFromDatabase();

        ImagesURLs.removeAll(Collections.singletonList(""));

        ImageURL = ImagesURLs.toArray(new String[0]);

        return inflater.inflate(R.layout.home_screen_notes, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainViewModel mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT, 0) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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
                databaseEver.deleteNote(viewHolder.getAdapterPosition());
            }

            @Override
            public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        // set up the RecyclerView
            RecyclerView recyclerView = requireActivity().findViewById(R.id.rvNumbers);

            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            adapter = new RecyclerGridAdapter(this.getActivity(), data, title, date, id, databaseEver); //requireContext() works too
            recyclerView.setAdapter(adapter);

            itemTouchHelper.attachToRecyclerView(recyclerView);

            adapter.setClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {

        SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();


        Integer id = ids.get(position);

        editor.putInt("noteId", id);
        editor.putBoolean("athome", false);
        editor.putBoolean("newnote", false);
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

        return true;
    }
}









