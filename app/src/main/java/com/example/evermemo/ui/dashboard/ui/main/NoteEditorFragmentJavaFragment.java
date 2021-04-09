package com.example.evermemo.ui.dashboard.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.evermemo.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.evermemo.EverDraw;
import com.example.evermemo.EverPopup;
import com.example.evermemo.MainActivity;
import com.example.evermemo.Note_Model;
import com.example.evermemo.R;
import com.example.evermemo.TESTEDITOR.rteditor.RTEditText;
import com.example.evermemo.databinding.FragmentNoteCreatorBinding;
import com.example.evermemo.imagesAdapter;
import com.example.evermemo.noteContentsAdapter;
import com.example.evermemo.recycler_models.EverLinkedMap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cafe.adriel.androidaudiorecorder.Util;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class NoteEditorFragmentJavaFragment extends Fragment implements EverInterfaceHelper.OnChangeColorListener, EverInterfaceHelper.OnEnterDarkMode {

    private final int EDIT_STATE = 1;
    // public EverNoteCreatorHelper everCreatorHelper;
    private final int VISUALIZE_STATE = 0;
    public int drawPosition = 0;
    public FragmentNoteCreatorBinding binding;
    private WeakReference<MainActivity> mainActivity;
    private EverDraw everDraw;
    private int FinalYHeight;
    private Note_Model actualNote;
    private RTEditText activeEditor;
    private noteContentsAdapter noteContentAdapter;
    private imagesAdapter imagesMultiViewAdapter;
    private EverDraw drawToRemove;
    private String savedBitmapPath;
    private CardView card;
    private boolean newDraw = false;
    private int noteState = VISUALIZE_STATE;
    private BottomSheetBehavior bottomSheetBehavior;
    private int bottomSheetHeight = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        postponeEnterTransition();

        setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));

        binding = FragmentNoteCreatorBinding.inflate(inflater, container, false);
        //  binding.toColorLinearLayoutNoteCreator.setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(getContext().getColor(R.color.White))));
        if (((MainActivity) binding.getRoot().getContext()).getEverThemeHelper().isDarkMode()) {
            binding.toColorLinearLayoutNoteCreator.setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(getContext().getColor(R.color.NightBlack))));
            binding.cardNoteCreator.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.NightLessBlack)));

        } else {
            binding.toColorLinearLayoutNoteCreator.setBackgroundTintList(ColorStateList.valueOf(Util.getDarkerColor(getContext().getColor(R.color.White))));
            binding.cardNoteCreator.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(R.color.White)));
        }


        //setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));

        return binding.getRoot();


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //   if (everCreatorHelper == null) {
        //  everCreatorHelper = new EverNoteCreatorHelper(requireActivity());
        mainActivity = new WeakReference<>(((MainActivity) requireActivity()));

        // NotesViewModel model = new ViewModelProvider(this).get(NotesViewModel.class);


//        mainActivity.get().getActualNote().getEverLinkedContents(false);
        init(mainActivity.get().getActualNote());
        // model.setActualNote(mainActivity.get().getActualNote());

        setNoteState(VISUALIZE_STATE);
        //   }
    }

    public FragmentNoteCreatorBinding getBinding() {
        return binding;
    }

    public void init(Note_Model actualNote) {

        this.actualNote = actualNote;

        if (mainActivity.get() == null) {
            mainActivity = new WeakReference<>(((MainActivity) requireActivity()));
        }


        mainActivity.get().getEverViewManagement().switchToolbars(false);

        if (!mainActivity.get().getNames().isEmpty()) {
            if (mainActivity.get().isNewNote()) {
                binding.cardNoteCreator.setTransitionName(mainActivity.get().getNames().get(0));
            } else {
                binding.textRecyclerCreator.setTransitionName(mainActivity.get().getNames().get(0));
                binding.cardNoteCreator.setTransitionName(mainActivity.get().getNames().get(1));
                binding.imageRecyclerCreator.setTransitionName(mainActivity.get().getNames().get(2));
            }
            mainActivity.get().getNames().clear();
        }

        mainActivity.get().runOnUiThread(() -> setupRecycler(actualNote));
        binding.textRecyclerCreator.setOnClickListener(v -> {
            int i = 0;
            int p = 0;
            for (EverLinkedMap e : getEverLinkedContents()) {
                if (!e.getContentByType(e.getType()).equals("▓")) {
                    p = i;
                }
                i++;
            }
            Toast.makeText(getContext(), String.valueOf(p), Toast.LENGTH_SHORT).show();

            RTEditText text = binding.textRecyclerCreator.getChildAt(p).findViewById(R.id.evermindEditor);
            text.requestFocus();
            text.setSelection(text.getText().length());

            InputMethodManager keyboard1 = (InputMethodManager) mainActivity.get().getSystemService(INPUT_METHOD_SERVICE);
            keyboard1.showSoftInput(text, 0);
        });

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        bottomSheetBehavior.setPeekHeight(175);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.expandIcon.setFraction(slideOffset, false);
            }
        });

        EverInterfaceHelper.getInstance().setAccentColorListener(this);
        EverInterfaceHelper.getInstance().setDarkModeListeners(this);

        EverInterfaceHelper.getInstance().changeAccentColor(Integer.parseInt(actualNote.getNoteColor()));


        // }).start();
    }

    private void setupRecycler(Note_Model actualNote) {

        if (noteContentAdapter == null) {
            noteContentAdapter = new noteContentsAdapter(new ArrayList<>(), Integer.parseInt(actualNote.getNoteColor()));
        }

        LinearLayoutManager a = new LinearLayoutManager(mainActivity.get());
        a.setOrientation(RecyclerView.VERTICAL);
        a.setInitialPrefetchItemCount(3);
        binding.textRecyclerCreator.setHasFixedSize(true);
        binding.textRecyclerCreator.setLayoutManager(a);
        binding.textRecyclerCreator.setItemAnimator(new LandingAnimator(new LinearOutSlowInInterpolator()));
        binding.textRecyclerCreator.setAdapter(noteContentAdapter);
        noteContentAdapter.updateEverLinkedMaps(actualNote.getEverLinkedContents(false), Integer.parseInt(actualNote.getNoteColor()));


        binding.cardNoteCreator.requestFocus();


        binding.imageRecyclerCreator.setBackgroundColor(Integer.parseInt(actualNote.getNoteColor()));

        setupImageRecycler();


        boolean NewNote = mainActivity.get().isNewNote();

        mainActivity.get().getHandler().postDelayed(() -> {

            if (NewNote) {

                mainActivity.get().getEverViewManagement().switchToolbars(true, true, false);
            }

        }, 750);

        //  EverScrollDecorator.setUpOverScroll(scrollView.get());


        // TransitionManager.beginDelayedTransition(binding.cardNoteCreator, new TransitionSet()
        //         .addTransition(new ChangeBounds()));

        if (mainActivity.get().getEverThemeHelper().isDarkMode()) {
            if (!actualNote.getNoteColor().equals("-1")) {
                //   mainActivity.get().getEverThemeHelper().tintSystemBarsAccent(Util.getDarkerColor(Integer.parseInt(actualNote.getNoteColor())), 500);
                mainActivity.get().getEverBallsHelper().metaColorNoteColor = Util.getDarkerColor(Integer.parseInt(actualNote.getNoteColor()));
            } else {
                //       mainActivity.get().getEverThemeHelper().tintSystemBarsAccent(Util.getDarkerColor(getContext().getColor(R.color.NightAccent)), 500);
            }
            //   mainActivity.get().getEverThemeHelper().tintViewAccent(mainActivity.get().getButtonsBinding().frameSizeDecoy, Util.getDarkerColor(getContext().getColor(R.color.NightBlack)), 500);
        } else {
            if (!actualNote.getNoteColor().equals("-1")) {
                //  mainActivity.get().getEverThemeHelper().tintSystemBarsAccent(Integer.parseInt(actualNote.getNoteColor()), 500);
                mainActivity.get().getEverBallsHelper().metaColorNoteColor = Integer.parseInt(actualNote.getNoteColor());
            }
        }

        startPostponeTransition();
    }

    private void onBackPressed() {
        mainActivity.get().onBackPressed();
    }

    public String getSavedBitmapPath() {
        return savedBitmapPath;
    }

    public void onDrawClick(@NonNull EverDraw view, int position, String path, CardView card) {
        if (drawToRemove != null) {
            drawToRemove.setVisibility(View.GONE);
        }
        this.card = card;
        drawToRemove = view;
        savedBitmapPath = path;
        drawPosition = position;
        this.everDraw = view;
        everDraw.setColor(mainActivity.get().getColor(R.color.Black));
        this.everDraw.setVisibility(View.VISIBLE);
        if (!mainActivity.get().getEverViewManagement().isDrawing())
            mainActivity.get().getEverViewManagement().switchBottomBars("draw");
    }

    @SuppressLint("InflateParams")
    public void onImageLongPress(View view, int position) {
        EverPopup popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(mainActivity.get()).inflate(R.layout.options_attached, null);
        popupWindowHelper = new EverPopup(popView);
        popupWindowHelper.showAsPopUp(view, view.getWidth() / 3, view.getHeight() / 2);
        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        imageView.setOnClickListener(view1 -> {
            removeImageAtPosition(position);
            popupWindowHelper.dismiss();
        });
    }


    @Override
    public void onPause() {
        //  EverInterfaceHelper.getInstance().changeState(true);
        mainActivity.get().getButtonsBinding().everGLAudioVisualizationView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        //  EverInterfaceHelper.getInstance().changeState(false);
        mainActivity.get().getButtonsBinding().everGLAudioVisualizationView.onResume();
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveBitmapFromDraw() {
        binding.textRecyclerCreator.suppressLayout(false);
        if (mainActivity.get().getEverViewManagement().isDrawing()) {

            Toast.makeText(getContext(), "We Are Drawing! And Is Paths empty? " + everDraw.getMPaths().isEmpty(), Toast.LENGTH_SHORT).show();
            if (!everDraw.getMPaths().isEmpty()) {

                mainActivity.get().getEverBitmapHelper().UpdateBitmapToFile(mainActivity.get().getEverBitmapHelper().createBitmapFromView(card, FinalYHeight), drawPosition);

                everDraw.clearCanvas();

                newDraw = false;

                FinalYHeight = 0;

            }
            mainActivity.get().getEverViewManagement().switchBottomBars("draw");
        } else {
            onBackPressed();
        }
        drawToRemove = null;
    }


    public void setupImageRecycler() {


        if (imagesMultiViewAdapter == null) {
            imagesMultiViewAdapter = new imagesAdapter(new ArrayList<>(), false);
        }

        LinearLayoutManager staggeredGridLayoutManagerImage = new LinearLayoutManager(getContext());

        binding.imageRecyclerCreator.setHasFixedSize(true);
        binding.imageRecyclerCreator.setLayoutManager(staggeredGridLayoutManagerImage);
        binding.imageRecyclerCreator.setItemAnimator(new LandingAnimator(new LinearOutSlowInInterpolator()));
        binding.imageRecyclerCreator.setAdapter(new AlphaInAnimationAdapter(imagesMultiViewAdapter));
        //   binding.imageRecyclerCreator.scrollToPosition(0);
        //  binding.expandIcon.setState(EverExpandArrow.LESS, true);
        boolean exists = false;
        for (String path : getListImages()) {
            if (new File(path).exists()) {
                exists = true;
            }
        }

        if (exists) {

            imagesMultiViewAdapter.updateImages(getListImages());

            binding.bottomSheet.setVisibility(View.VISIBLE);
            binding.imageRecyclerCreator.setVisibility(View.VISIBLE);

        } else {
            binding.imageRecyclerCreator.setVisibility(View.GONE);
            binding.bottomSheet.setVisibility(View.GONE);
        }


    }


    public void updateDrawAtPosition(int p, String drawLocation) {
        EverLinkedMap e = getEverLinkedContents().get(p);
        e.setDrawLocation(drawLocation);
        getEverLinkedContents().remove(p);
        noteContentAdapter.updateEverLinkedMaps(getEverLinkedContents(), Integer.parseInt(actualNote.getNoteColor()));
        getEverLinkedContents().add(p, e);
        noteContentAdapter.updateEverLinkedMaps(getEverLinkedContents(), Integer.parseInt(actualNote.getNoteColor()));
    }

    public void removeEverLinkedContentAtPosition(int p) {
        EverLinkedMap aboveEver;
        EverLinkedMap belowEver;
        EverLinkedMap everToRemove = getEverLinkedContents().get(p);
        if (p != 0) {

            if (getEverLinkedContents().size() >= p + 1) { //TODO remove this -> =
                aboveEver = getEverLinkedContents().get(p - 1);
                belowEver = getEverLinkedContents().get(p + 1);
                if (!belowEver.getContent().equals("▓") && !aboveEver.isEmpty()) {
                    belowEver.mergeWith(aboveEver);
                    getEverLinkedContents().remove(aboveEver);
                    EverLinkedMap toAdd = new EverLinkedMap();
                    toAdd.setContent(belowEver.getContent());
                    getEverLinkedContents().remove(belowEver);
                    getEverLinkedContents().add(p - 1, toAdd);
                    noteContentAdapter.updateEverLinkedMaps(getEverLinkedContents(), Integer.parseInt(actualNote.getNoteColor()));
                }

            }
        }

        getEverLinkedContents().remove(everToRemove);

        if (getEverLinkedContents().size() == 0) {
            getEverLinkedContents().add(new EverLinkedMap());
        }

        noteContentAdapter.updateEverLinkedMaps(getEverLinkedContents(), Integer.parseInt(actualNote.getNoteColor()));
    }

    public void removeImageAtPosition(int p) {
        if (new File(getListImages().get(p)).delete()) {
            getListImages().remove(p);
            mainActivity.get().getActualNote().setImages(getListImages());
            imagesMultiViewAdapter.updateImages(getListImages());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addImage(String path) {
        if (binding.bottomSheet.getVisibility() == View.GONE) {
            binding.bottomSheet.setVisibility(View.VISIBLE);
            binding.imageRecyclerCreator.setVisibility(View.VISIBLE);
        }
        System.out.println("Before" + getListImages().toString() + " " + getListImages().size());
        mainActivity.get().getEverViewManagement().animateHeightChange(binding.bottomSheet, 350, 800, null);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        getListImages().add(0, path);
        System.out.println("After" + getListImages().toString());
        binding.imageRecyclerCreator.scrollToPosition(0);
        mainActivity.get().getActualNote().setImages(getListImages());
        imagesMultiViewAdapter.updateImages(getListImages());
        System.out.println("After note" + mainActivity.get().getActualNote().getImages().toString());
        mainActivity.get().getFirebaseHelper().putFile(new File(path), 2, 0, null, null);
    }

    public RTEditText getActiveEditor() {
        return activeEditor;
    }

    public void setActiveEditor(RTEditText activeEditor) {
        this.activeEditor = activeEditor;
    }


    @Override
    public void changeAccentColor(int color) {
        mainActivity.get().getEverThemeHelper().tintSystemBarsAccent(color, 500);
    }

    public void removeColorListenerFromCreatorHelper() {
        EverInterfaceHelper.getInstance().removeColorListener(this);
    }

    public RecyclerView gettextRecyclerCreator() {
        return binding.textRecyclerCreator;
    }

    public EverDraw getEverDraw() {
        return everDraw;
    }

    public void setEverDraw(EverDraw everDraw) {
        this.everDraw = everDraw;
    }

    public void setFinalYHeight(int size) {
        FinalYHeight = size;
    }

    public CardView getCardNoteCreator() {
        return binding.cardNoteCreator;
    }

    public boolean isNewDraw() {
        return newDraw;
    }

    public void setNewDraw(boolean newDraw) {
        this.newDraw = newDraw;
    }

    public List<EverLinkedMap> getEverLinkedContents() {
        return mainActivity.get().getActualNote().getEverLinkedContents(false);
    }

    public List<String> getListImages() {
        return mainActivity.get().getActualNote().getImages();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        // new Thread(() -> {

        // if (mainActivity.get().getEverViewManagement().DrawVisualizerIsShowing) {

        //     if (resultCode != RESULT_OK) {

        //       Uri gif = Objects.requireNonNull(data).getData();

        //      mainActivity.get().everBitmapHelper.SaveImageAndAddToDatabase(gif);
        //reloadImages();
        //    }
        //  }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(Objects.requireNonNull(data));
            if (resultUri != null) {
                mainActivity.get().getEverBitmapHelper().SaveImageAndAddToDatabase(resultUri);

            }
            // reloadImages();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(Objects.requireNonNull(data));
            Objects.requireNonNull(cropError).printStackTrace();
        } else {
            if (requestCode != RESULT_OK) {

                File directory = mainActivity.get().getDir("imageDir", Context.MODE_PRIVATE);

                Uri uri = Uri.fromFile(new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + ".jpg"));

                if (data != null) {

                    UCrop.Options options = new UCrop.Options();
                    int color = Integer.parseInt(mainActivity.get().getActualNote().getNoteColor());
                    if (color != -1) {
                        options.setToolbarColor(color);
                        options.setStatusBarColor(color);
                        options.setCropFrameColor(color);
                        options.setCropGridCornerColor(color);
                        options.setActiveControlsWidgetColor(color);
                    }
                    UCrop.of(Objects.requireNonNull(data.getData()), uri)
                            .withOptions(options)
                            .start(requireActivity(), this);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
        //  }).start();
    }

    public void importerClick(View view) {
        switch (view.getTag().toString()) {
            case "gallery":
                // TODO
                break;

            case "googlePhotos":
                Intent intentGooglePhotos = new Intent();
                intentGooglePhotos.setAction(Intent.ACTION_PICK);
                intentGooglePhotos.setType("image/*");
                intentGooglePhotos.setPackage("com.google.android.apps.photos");
                startActivityForResult(intentGooglePhotos, 101);
                break;

            case "files":
                Intent intentFiles = new Intent();
                intentFiles.setType("image/*");
                intentFiles.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentFiles, "Select Picture"), 101);
                break;
        }

    }

    public void startPostponeTransition() {
        startPostponedEnterTransition();
        mainActivity.get().getUIHandler().postDelayed(() -> EverInterfaceHelper.getInstance().hide(), 300);
    }

    private void switchNoteStates() {
        switch (noteState) {

            case VISUALIZE_STATE:

                EverInterfaceHelper.getInstance().setCantClick();

                mainActivity.get().getEverViewManagement().CloseAllButtons();

                if (activeEditor != null && activeEditor.hasFocus()) {
                    activeEditor.clearFocus();
                }


                // mainActivity.get().getEverViewManagement().CloseOrOpenToolbarUndoRedo(false);

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (binding.bottomSheet.getHeight() == 1) {

                    mainActivity.get().getEverViewManagement().animateHeightChange(binding.bottomSheet, 350, bottomSheetHeight, null);

                }

                break;

            case EDIT_STATE:


                EverInterfaceHelper.getInstance().setCanClick();

                mainActivity.get().getEverViewManagement().switchToolbars(true, true, true);

                if (bottomSheetHeight == 0) {
                    bottomSheetHeight = binding.bottomSheet.getHeight();
                }
                mainActivity.get().getEverViewManagement().animateHeightChange(binding.bottomSheet, 350, 1, null);
                // bottomSheetBehavior.setPeekHeight(0);

                break;
        }
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void updateContents(List<EverLinkedMap> everLinkedMaps) {
        noteContentAdapter.updateEverLinkedMaps(everLinkedMaps, Integer.parseInt(actualNote.getNoteColor()));

    }

    public int getNoteState() {
        return noteState;
    }

    public void setNoteState(int state) {
        noteState = state;
        switchNoteStates();
    }

    @Override
    public void swichDarkMode(int color, boolean isDarkMode) {
        if (isDarkMode) {
            mainActivity.get().getEverThemeHelper().tintViewAccent(binding.toColorLinearLayoutNoteCreator, Util.getDarkerColor(mainActivity.get().getEverThemeHelper().defaultTheme), 500);
        } else {
            mainActivity.get().getEverThemeHelper().tintViewAccent(binding.toColorLinearLayoutNoteCreator, mainActivity.get().getEverThemeHelper().defaultTheme, 500);
        }
        mainActivity.get().getEverThemeHelper().tintViewAccent(binding.cardNoteCreator, mainActivity.get().getEverThemeHelper().defaultTheme, 500);
    }
}
