package com.example.Evermind.ui.note_screen;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.Evermind.EverRecyclerContentsNoteScreen;
import com.example.Evermind.MainActivity;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.RecyclerGridAdapterNoteScreen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class NotesScreen extends Fragment implements RecyclerGridAdapterNoteScreen.ItemClickListener, AdapterView.OnItemLongClickListener {


    public RecyclerGridAdapterNoteScreen adapter;
    private EverRecyclerContentsNoteScreen recyclerView;
    private  ArrayList<Integer> ids = new ArrayList<>();
    private ArrayList<Note_Model> noteModels = new ArrayList<>();


    public static int fromPosition;
    public static int toPosition;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       postponeEnterTransition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
            setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
        }

      // noteModels.clear();
        //
        //      //  databaseEver = ((MainActivity) requireActivity()).mDatabaseEver;
        //
        //      //  ids = databaseEver.getIDFromDatabase();
        //
        //      //  notes = databaseEver.getContentsFromDatabase();
        //
        //      //  titles = databaseEver.getTitlesFromDatabase();
        //
        //      //  dates = databaseEver.getDateFromDatabase();
        //
        //       // imageURL = databaseEver.getImageURLFromDatabase();
        //
        //      //  for  (int i = 0; i < ids.size() ; i++) {
        //       //     noteModels.add(new Note_Model(ids.get(i), titles.get(i), notes.get(i), dates.get(i), imageURL.get(i)));
        //       // }
        //
        //      // OrganizeInDescendingOrderByInteger();

        return inflater.inflate(R.layout.home_screen_notes, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) requireActivity()).noteScreen = this;



        MainViewModel mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT, 0) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                fromPosition = viewHolder.getAdapterPosition();
                toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(((MainActivity) requireActivity()).notesModels, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(((MainActivity) requireActivity()).notesModels, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                ids.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
               // databaseEver.deleteNote(viewHolder.getAdapterPosition());
            }

            @Override
            public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);


        // set up the RecyclerView
        recyclerView = requireActivity().findViewById(R.id.notesRecycler);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new RecyclerGridAdapterNoteScreen(requireActivity(), ((MainActivity)requireActivity()).notesModels); //requireContext() works too
        recyclerView.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
        recyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        ((MainActivity)requireActivity()).recyclertest = recyclerView;
        recyclerView.scrollToPosition(((MainActivity)requireActivity()).selectedPosition);
        ((MainActivity)requireActivity()).selectedPosition = 0;

        Bundle arguments = getArguments();
        if (arguments != null) {


             //   NotifyModel notifyModel = NotesScreenArgs.fromBundle(getArguments()).getNotifyModel();

             //   Toast.makeText(requireActivity(), "victory", Toast.LENGTH_SHORT).show();
               // NotifyChangesInNotes(notifyModel.isAdded(), notifyModel.isRemoved(), notifyModel.getPosition());

          //  NotifyChangesInNotes(arguments.getBoolean("added"), arguments.getBoolean("removed"), arguments.getInt("ID"));
        }
            itemTouchHelper.attachToRecyclerView(recyclerView);



            adapter.setClickListener(this);

             recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
    }




    @Override
    public void onItemClick(RecyclerView view, CardView view2, View view4, int position) {

        SharedPreferences.Editor editor = ((MainActivity) requireActivity()).editor;

        int id = ((MainActivity)requireActivity()).notesModels.get(position).getId();

        Toast.makeText(requireActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();

        editor.putInt("noteId", id);
      //  editor.putBoolean("athome", false);
      //  editor.putBoolean("newnote", false);
      //  editor.putBoolean("DeleteNSave", false);
      //  editor.putBoolean("UndoRedo", false);
        editor.apply();

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









