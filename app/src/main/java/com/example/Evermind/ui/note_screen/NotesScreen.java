package com.example.Evermind.ui.note_screen;

import android.content.res.ColorStateList;
import android.os.Bundle;
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

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.customLandingAnimator;
import com.example.Evermind.everUtils.EverNoteManagement;
import com.example.Evermind.noteModelsAdapter;
import com.mlsdev.animatedrv.AnimatedRecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cafe.adriel.androidaudiorecorder.Util;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class NotesScreen extends Fragment implements AdapterView.OnItemLongClickListener, EverInterfaceHelper.OnEnterDarkMode {

    private noteModelsAdapter adapter;
    private AnimatedRecyclerView recyclerView;
    private  View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home_screen_notes, container, false);
        postpone();
       // setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(new ));
        //setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));

        ((MainActivity) requireActivity()).setNoteScreen(new WeakReference<>(this));
        EverInterfaceHelper.getInstance().setDarkModeListeners(this);
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

    public void init(@NonNull EverNoteManagement noteManagement) {
        setLayoutManager(noteManagement);
        adapter = new noteModelsAdapter(getContext(), noteManagement.getNoteModelSection());
        recyclerView.setItemAnimator(new customLandingAnimator());
        recyclerView.setAdapter(adapter);
        try {
            recyclerView.notifyDataSetChanged();
            ((MainActivity)getContext()).holdViewsToDarken(recyclerView);
            ((MainActivity)getContext()).holdViewsToDarken(rootView.findViewById(R.id.imageButton));
        } catch (Exception e) {
            e.printStackTrace();
        }
        noteManagement.setNoteScreenRecycler(recyclerView);

        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                startPostponedEnterTransition();
                OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setLayoutManager(@NonNull EverNoteManagement noteManagement) {
        int count;
        if (noteManagement.isGrid()) {
            count = 2;
        } else {
            count = 1;
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL);
        recyclerView = rootView.findViewById(R.id.notesRecycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        if (adapter != null) {

            recyclerView.scheduleLayoutAnimation();
        //    adapter.changeLayout();
        }
    }

    public noteModelsAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void enterDarkMode(int color) {
        rootView.findViewById(R.id.homeNotesrelative).setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(getContext().getColor(R.color.NightBlack))));
        recyclerView.setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(getContext().getColor(R.color.NightBlack))));
        rootView.findViewById(R.id.imageButton).setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.NightBlack)));
    }
}









