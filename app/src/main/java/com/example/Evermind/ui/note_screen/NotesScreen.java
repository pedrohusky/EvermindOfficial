package com.example.Evermind.ui.note_screen;

import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.EvershootInterpolator;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.RecyclerGridAdapterNoteScreen;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import mva2.adapter.MultiViewAdapter;
import mva2.adapter.util.Mode;

public class NotesScreen extends Fragment implements AdapterView.OnItemLongClickListener {

    public static int fromPosition;
    public static int toPosition;
    public WeakReference<MultiViewAdapter> adapter;
    private WeakReference<RecyclerView> recyclerView;
    private int count;
    private  View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_screen_notes, container, false);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
        setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
        init();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void StartPostpone() {
        startPostponedEnterTransition();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        return true;
    }

    public void init() {
        WeakReference<MainActivity> mainActivity = new WeakReference<>(((MainActivity) requireActivity()));

        if (mainActivity.get().isGrid) {
            count = 2;
        } else {
            count = 1;
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL);
        recyclerView = new WeakReference<>(rootView.findViewById(R.id.notesRecycler));

        recyclerView.get().setLayoutManager(staggeredGridLayoutManager);
        adapter = ((MainActivity)requireActivity()).adapter;
        adapter.get().setSelectionMode(Mode.MULTIPLE);
        recyclerView.get().setItemAnimator(new LandingAnimator(new EvershootInterpolator(1f)));
        recyclerView.get().setAdapter(new AlphaInAnimationAdapter(adapter.get()));

        OverScrollDecoratorHelper.setUpOverScroll(recyclerView.get(), OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        ((MainActivity)requireActivity()).recyclertest = recyclerView;
        recyclerView.get().scrollToPosition(((MainActivity)requireActivity()).selectedPosition);
        ((MainActivity)requireActivity()).selectedPosition = -1;

        recyclerView.get().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if (mainActivity.get().noteModelSection.size() == 0) { StartPostpone(); }
                recyclerView.get().getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        mainActivity.get().noteScreen = this;


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
                adapter.get().notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                adapter.get().notifyItemRemoved(viewHolder.getAdapterPosition());
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
}









