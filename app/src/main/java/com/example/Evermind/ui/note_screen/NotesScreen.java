package com.example.Evermind.ui.note_screen;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteModelBinder;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.everUtils.EverNoteManagement;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import mva2.adapter.MultiViewAdapter;
import mva2.adapter.util.Mode;

public class NotesScreen extends Fragment implements AdapterView.OnItemLongClickListener {

    private MultiViewAdapter adapter;
    private RecyclerView recyclerView;
    private  View rootView;
    private boolean hasInitialized = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_screen_notes, container, false);
        postpone();
       // setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(new ));
        //setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));

        ((MainActivity) requireActivity()).setNoteScreen(new WeakReference<>(this));
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

    public void postpone() {
        postponeEnterTransition();
    }

    public void StartPostpone() {
        startPostponedEnterTransition();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        return true;
    }

    public void init(EverNoteManagement noteManagement) {
        hasInitialized = true;
        setLayoutManager(noteManagement);
        adapter = new MultiViewAdapter();
        adapter.registerItemBinders(new NoteModelBinder(requireActivity()));
        adapter.addSection(noteManagement.getNoteModelSection());
        adapter.setSelectionMode(Mode.MULTIPLE);
        recyclerView.setItemAnimator(new LandingAnimator(new LinearOutSlowInInterpolator()));
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
        noteManagement.setNoteScreenRecycler(recyclerView);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    public void setLayoutManager(EverNoteManagement noteManagement) {
        int count;
        if (noteManagement.isGrid()) {
            count = 2;
        } else {
            count = 1;
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL);
        recyclerView = rootView.findViewById(R.id.notesRecycler);
        if (adapter != null) {
            adapter.removeAllSections();
            adapter.addSection(((MainActivity)getContext()).getEverNoteManagement().getNoteModelSection());
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    public MultiViewAdapter getAdapter() {
        return adapter;
    }
}









