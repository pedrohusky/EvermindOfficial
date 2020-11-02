package com.example.Evermind.ui.note_screen;

import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.EverRecyclerContentsNoteScreen;
import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteModelBinder;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.RecyclerGridAdapterNoteScreen;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

public class NotesScreen extends Fragment implements AdapterView.OnItemLongClickListener {

    public static int fromPosition;
    public static int toPosition;
    public MultiViewAdapter adapter;
    private EverRecyclerContentsNoteScreen recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       postponeEnterTransition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
            setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
        }

        return inflater.inflate(R.layout.home_screen_notes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity mainActivity = ((MainActivity) requireActivity());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
     //   GridLayoutManager grid = new GridLayoutManager(requireActivity(), 2);
        recyclerView = mainActivity.findViewById(R.id.notesRecycler);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = ((MainActivity)requireActivity()).adapter;
        recyclerView.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
        recyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));

        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        ((MainActivity)requireActivity()).recyclertest = recyclerView;
        //((MainActivity)requireActivity()).noteScreenAdapter = adapter;
        recyclerView.scrollToPosition(((MainActivity)requireActivity()).selectedPosition);
        ((MainActivity)requireActivity()).selectedPosition = -1;

        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
              //  if (mainActivity.noteModelSection.size() == 0) {
                StartPostpone();
              //  }
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        mainActivity.noteScreen = this;


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT, 0) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                fromPosition = viewHolder.getAdapterPosition();
                toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                     //   Collections.swap(mainActivity.noteModelSection, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                     //   Collections.swap(mainActivity.noteModelSection, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
               // databaseEver.deleteNote(viewHolder.getAdapterPosition());
            }

            @Override
            public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
            }
        };

      //  ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
      //  itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    public void StartPostpone() {
        startPostponedEnterTransition();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        return true;
    }
}









