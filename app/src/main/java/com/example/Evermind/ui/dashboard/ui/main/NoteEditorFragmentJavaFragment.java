package com.example.Evermind.ui.dashboard.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.EverDraw;
import com.example.Evermind.EverPopup;
import com.example.Evermind.EvershootInterpolator;
import com.example.Evermind.ImagesBinder;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteContentsBinder;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.databinding.FragmentNoteCreatorBinding;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class NoteEditorFragmentJavaFragment extends Fragment implements EverInterfaceHelper.OnChangeColorListener {

    private WeakReference<MainActivity> mainActivity;
   // public EverNoteCreatorHelper everCreatorHelper;

    private EverDraw everDraw;
    private int FinalYHeight;
    private RTEditText activeEditor;
    private int activeEditorPosition = -1;
    private MultiViewAdapter multiViewAdapter;
    private ListSection<String> listImages;
    private EverDraw drawToRemove;
    public int drawPosition = 0;
    private String savedBitmapPath;
    private boolean drawFromRecycler = false;
    private CardView card;
    private boolean newDraw = false;
    public FragmentNoteCreatorBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        postponeEnterTransition();

        setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));

        binding = FragmentNoteCreatorBinding.inflate(inflater, container, false);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


     //   if (everCreatorHelper == null) {
          //  everCreatorHelper = new EverNoteCreatorHelper(requireActivity());
            mainActivity = new WeakReference<>(((MainActivity)requireActivity()));

     //   }
        init();
    }

    public FragmentNoteCreatorBinding getBinding() {
        return binding;
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init() {

        mainActivity.get().getEverViewManagement().switchToolbars(false);

        mainActivity.get().getActualNote().getEverContents(false);

        if (!mainActivity.get().getNames().isEmpty()) {
            if (mainActivity.get().isNewNote()) {
                binding.cardNoteCreator.setTransitionName(mainActivity.get().getNames().get(0));
            } else {
                binding.TextAndDrawRecyclerView.setTransitionName(mainActivity.get().getNames().get(0));
                binding.cardNoteCreator.setTransitionName(mainActivity.get().getNames().get(1));
                binding.ImagesRecycler.setTransitionName(mainActivity.get().getNames().get(2));
            }
            mainActivity.get().getNames().clear();
        }

        mainActivity.get().runOnUiThread(this::setupRecycler);

        binding.cardNoteCreator.setOnClickListener(v -> {
            int i = 0;
            int p = 0;
            for (EverLinkedMap e : getEverLinkedContents().getData()) {
                if (!e.getContent().equals("▓")) {
                    p = i;
                }
                i++;
            }

            RTEditText text = binding.TextAndDrawRecyclerView.getChildAt(p).findViewById(R.id.evermindEditor);
            text.requestFocus();
            text.setSelection(Objects.requireNonNull(text.getText()).length());

            InputMethodManager keyboard1 = (InputMethodManager) mainActivity.get().getSystemService(INPUT_METHOD_SERVICE);
            keyboard1.showSoftInput(text, 0);
        });

        EverInterfaceHelper.getInstance().setColorListener(this);

        // }).start();
    }

    private void setupRecycler() {
        if (multiViewAdapter == null) {
            multiViewAdapter = new MultiViewAdapter();
            multiViewAdapter.registerItemBinders(new NoteContentsBinder(mainActivity.get()));
            binding.TextAndDrawRecyclerView.setItemAnimator(new LandingAnimator(new LinearOutSlowInInterpolator()));
        }

        LinearLayoutManager a = new LinearLayoutManager(mainActivity.get());
        a.setOrientation(RecyclerView.VERTICAL);
        a.setInitialPrefetchItemCount(3);
        binding.TextAndDrawRecyclerView.setLayoutManager(a);
        multiViewAdapter.removeAllSections();
        binding.TextAndDrawRecyclerView.setAdapter(multiViewAdapter);
        multiViewAdapter.addSection(mainActivity.get().getActualNote().everLinkedContents);
        binding.cardNoteCreator.requestFocus();

        listImages = new ListSection<>();

        reloadImages();



        boolean NewNote = mainActivity.get().isNewNote();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (NewNote) {

                mainActivity.get().getEverViewManagement().switchToolbars(true, true, false);
            }

        }, 750);

        //  EverScrollDecorator.setUpOverScroll(scrollView.get());


        // TransitionManager.beginDelayedTransition(binding.cardNoteCreator, new TransitionSet()
        //         .addTransition(new ChangeBounds()));


        if (!mainActivity.get().getActualNote().getNoteColor().equals("-1")) {
            mainActivity.get().getEverThemeHelper().tintSystemBars(Integer.parseInt(mainActivity.get().getActualNote().getNoteColor()), 500);
            mainActivity.get().getEverBallsHelper().metaColorNoteColor = Integer.parseInt(mainActivity.get().getActualNote().getNoteColor());
        }
    }

    private void onBackPressed() {
        mainActivity.get().onBackPressed();
    }

    public String getSavedBitmapPath() {
        return savedBitmapPath;
    }

    public void onDrawClick(EverDraw view, int position, String path, CardView card) {
        if (drawToRemove != null) {
            drawToRemove.setVisibility(View.GONE);
        }
        this.card = card;
        drawToRemove = view;
        savedBitmapPath = path;
        drawPosition = position;
        drawFromRecycler = true;
        this.everDraw = view;
        this.everDraw.setVisibility(View.VISIBLE);
        mainActivity.get().getEverViewManagement().CloseOrOpenDrawOptions(false);
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onImageLongPress(View view, int position) {
        EverPopup popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(mainActivity.get()).inflate(R.layout.options_attached, null);
        popupWindowHelper = new EverPopup(popView);
        popupWindowHelper.showAsDropDown(view, 350, -25);
        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        imageView.setOnClickListener(view1 -> {
            removeImage(position);
            popupWindowHelper.dismiss();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onLongPress(int position, boolean isDraw) {
        int up = position-1;
        int below = position+1;
        if (position == 0) {
            up = 0;
            below = 0;
        }
        String toReplace = getEverLinkedContents().get(up).getContent();
        String toUpdate = getEverLinkedContents().get(below).getContent();
        String toDelete;
        if (isDraw) {
            toDelete = getEverLinkedContents().get(position).getDrawLocation();
        } else {
            toDelete = getEverLinkedContents().get(position).getRecord();
        }
        if (!toReplace.equals("▓") && !toUpdate.equals("▓")) {
            if (new File(toDelete).delete()) {
                getEverLinkedContents().remove(position);
                getEverLinkedContents().remove(position-1);
                EverLinkedMap e = getEverLinkedContents().get(position-1);
                e.setContent(toReplace + "<br>" + toUpdate);
                getEverLinkedContents().set(position-1, e);
                mainActivity.get().getActualNote().everLinkedToString();
                // mainActivity.get().clearIonCache(drawLocation);
            } else {
                if (toDelete.equals("")) {
                    getEverLinkedContents().remove(position);
                    getEverLinkedContents().remove(position-1);
                    EverLinkedMap e = getEverLinkedContents().get(position-1);
                    e.setContent(toReplace + "<br>" + toUpdate);
                    getEverLinkedContents().set(position-1, e);
                    mainActivity.get().getActualNote().everLinkedToString();
                }
            }

        } else {

            if (new File(toDelete).delete()) {
                getEverLinkedContents().remove(position);
                mainActivity.get().getActualNote().everLinkedToString();
            } else {
                if (toDelete.equals("")) {
                    getEverLinkedContents().remove(position);
                    mainActivity.get().getActualNote().everLinkedToString();
                }
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveBitmapFromDraw() {
        binding.TextAndDrawRecyclerView.suppressLayout(false);
        if (mainActivity.get().getEverViewManagement().isDrawing()) {

            if (!everDraw.getMPaths().isEmpty()) {

                mainActivity.get().getEverBitmapHelper().UpdateBitmapToFile(mainActivity.get().getEverBitmapHelper().createBitmapFromView(card, FinalYHeight), savedBitmapPath);

                everDraw.clearCanvas();

                newDraw = false;

                FinalYHeight = 0;

            }
            mainActivity.get().getEverViewManagement().CloseOrOpenDrawOptions(true);
        } else {
            onBackPressed();
        }
        drawToRemove = null;
    }



    public void reloadImages() {

        List<String> images = mainActivity.get().getActualNote().getImages();
        if (images.size() > 0) {

            if (binding.ImagesRecycler.getAdapter() == null) {
                StaggeredGridLayoutManager staggeredGridLayoutManagerImage = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

                binding.ImagesRecycler.setLayoutManager(staggeredGridLayoutManagerImage);
                binding.ImagesRecycler.setVisibility(View.VISIBLE);

                MultiViewAdapter adapter = new MultiViewAdapter();
                listImages = new ListSection<>();

                listImages.addAll(images);
                adapter.addSection(listImages);
                adapter.registerItemBinders(new ImagesBinder(mainActivity.get(), false));
                binding.ImagesRecycler.setItemAnimator(new LandingAnimator(new EvershootInterpolator(1f)));
                binding.ImagesRecycler.setAdapter(new AlphaInAnimationAdapter(adapter));
            } else {
                if (!listImages.getData().equals(images)) {
                    listImages.clear();
                    listImages.addAll(images);
                }
            }




        } else {
            binding.ImagesRecycler.setVisibility(View.GONE);
        }
        mainActivity.get().getNoteCreator().startPostponeTransition();
    }

    public void updateDraw(int p, String drawLocation) {
        mainActivity.get().getEverViewManagement().beginDelayedTransition(binding.cardNoteCreator);
        EverLinkedMap e = getEverLinkedContents().get(p);
        e.setDrawLocation(drawLocation);
        getEverLinkedContents().set(p, e);
    }

    public void updateNoteContent(int p, String content) {
        View currentFocus = mainActivity.get().getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        mainActivity.get().getEverViewManagement().beginDelayedTransition(binding.cardNoteCreator);
        removeNote(p);
        EverLinkedMap e = getEverLinkedContents().get(p).get();
        e.setContent(content);
        getEverLinkedContents().set(p, e);
    }

    public void removeNote(int p) {
        getEverLinkedContents().remove(p);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void removeImage(int p) {
        List<String> toRemove = new ArrayList<>();
        for (String paths : listImages.getData()) {
            if (paths.equals(listImages.get(p))) {
                File file = new File(paths);
                if (file.exists()) {
                    if (file.delete()){
                        System.out.println("Image deleted at " + p);
                    }
                }
            } else {
                toRemove.add(paths + "┼");
            }
        }
        mainActivity.get().getEverNoteManagement().getEverDataBase().editImages(String.valueOf(mainActivity.get().getActualNote().getId()), String.join("", toRemove));
        mainActivity.get().getActualNote().setImageURLS(String.join("", toRemove));
        listImages.remove(p);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addImage(String path) {
        if (binding.ImagesRecycler.getVisibility() == View.GONE) {
            binding.ImagesRecycler.setVisibility(View.VISIBLE);
        }
        mainActivity.get().getEverViewManagement().beginDelayedTransition(binding.cardNoteCreator);
        if (listImages == null) {
            listImages = new ListSection<>();
        }
        listImages.add(path);
        if (listImages.size() > 2) {
            binding.ImagesRecycler.removeViewAt(listImages.size() - 2);
        }
        mainActivity.get().getActualNote().setImages(listImages.getData());

        //reloadImages();

    }

    public RTEditText getActiveEditor() {
        return activeEditor;
    }

    public void setActiveEditor(RTEditText activeEditor) {
        this.activeEditor = activeEditor;
    }

    public int getActiveEditorPosition() {
        return activeEditorPosition;
    }

    public void setActiveEditorPosition(int activeEditorPosition) {
        this.activeEditorPosition = activeEditorPosition;
    }

    @Override
    public void changeColor(int color) {
        mainActivity.get().getEverThemeHelper().tintSystemBars(color, 1000);
    }

    public void removeColorListenerFromCreatorHelper() {
        EverInterfaceHelper.getInstance().removeColorListener(this);
    }

    public RecyclerView getTextAndDrawRecyclerView() {
        return binding.TextAndDrawRecyclerView;
    }

    public EverDraw getEverDraw() {
        return everDraw;
    }

    public int getFinalYHeight() {
        return FinalYHeight;
    }

    public void setFinalYHeight(int size) {
        FinalYHeight = size;
    }

    public CardView getCardNoteCreator() {
        return binding.cardNoteCreator;
    }

    public void setDrawFromRecycler(boolean drawFromRecycler) {
        this.drawFromRecycler = drawFromRecycler;
    }

    public boolean isDrawFromRecycler() {
        return drawFromRecycler;
    }

    public boolean isNewDraw() {
        return newDraw;
    }

    public void setNewDraw(boolean newDraw) {
        this.newDraw = newDraw;
    }

    public ListSection<EverLinkedMap> getEverLinkedContents() {
        return mainActivity.get().getActualNote().everLinkedContents;
    }

    public MultiViewAdapter getMultiViewAdapter() {
        return multiViewAdapter;
    }


    public void setEverDraw(EverDraw everDraw) {
        this.everDraw = everDraw;
    }

    public ListSection<String> getListImages() {
        return listImages;
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
        new Handler(Looper.getMainLooper()).postDelayed(() -> EverInterfaceHelper.getInstance().hide(), 200);
    }
}
