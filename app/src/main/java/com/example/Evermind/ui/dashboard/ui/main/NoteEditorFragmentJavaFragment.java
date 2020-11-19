package com.example.Evermind.ui.dashboard.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.daimajia.swipe.SwipeLayout;
import com.example.Evermind.EverBitmapMerger;
import com.example.Evermind.EverDraw;
import com.example.Evermind.EverNestedScrollView;
import com.example.Evermind.EverPopup;
import com.example.Evermind.EvershootInterpolator;
import com.example.Evermind.ImagesBinder;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteContentsBinder;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.SoftInputAssist;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.koushikdutta.ion.Ion;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.StaticOverScrollDecorAdapter;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

import static android.app.Activity.RESULT_OK;

public class NoteEditorFragmentJavaFragment extends Fragment implements ImagesRecyclerGridAdapter.ItemClickListener {

    private static final String GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos";
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    // public RecyclerView textanddrawRecyclerView;
    public WeakReference<RecyclerView> textanddrawRecyclerView;
    public boolean drawFromRecycler = false;
    public boolean hasImages = false;
    public boolean hasDraws = false;
    // public Note_Model actualNote;
    public WeakReference<Note_Model> actualNote;
    public WeakReference<EverDraw> everDraw;
    public int FinalYHeight;
    public WeakReference<RTEditText> activeEditor;
    public int activeEditorPosition = -1;
    public WeakReference<MultiViewAdapter> adptr ;
    public ListSection<EverLinkedMap> list = new ListSection<>();
    List<String> bitmaps = new ArrayList<>();
    private NoteEditorFragmentMainViewModel mViewModel;
    public WeakReference<EverNestedScrollView> scrollView;
    public WeakReference<EditText> TitleTextBox;
    public WeakReference<CardView> cardView;
    public WeakReference<Bitmap> savedbitmap;
    public WeakReference<Bitmap> newDrawedbitmap;
    public WeakReference<Bitmap> finalBitmap;
    private int drawPosition = 0;
    private String savedBitmapPath;
    public WeakReference<ImagesRecyclerGridAdapter> imageAdapter;
    private String originalString;
    private WeakReference<RecyclerView> recyclerViewImage;
    private Handler delayedHandler;
    public WeakReference<MainActivity> mainActivity;
    public ListSection<String> listImages;
    private EverDraw drawToRemove;
    private View rootView;
    public  int cardWidth;
    public int cardHeight;
    private CardView card;

    public static NoteEditorFragmentJavaFragment newInstance() {
        return new NoteEditorFragmentJavaFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        postponeEnterTransition();

         rootView = inflater.inflate(R.layout.fragment_note_creator, container, false);

        init();

        setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
        setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));

        return rootView;


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



    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        mainActivity = new WeakReference<>(((MainActivity) requireActivity()));

        mainActivity.get().noteCreator = new WeakReference<>(this);

        mainActivity.get().switchToolbars(false);

        actualNote = mainActivity.get().actualNote;

        if (actualNote.get().getImages().size() > 0) {
            hasImages = true;
        }
        if (actualNote.get().getDraws().size() > 0) {
            hasDraws = true;
        }

        everDraw = new WeakReference<>(rootView.findViewById(R.id.EverDraw));
        // scrollView = new WeakReference<>(mainActivity.get().findViewById(R.id.scrollview));
        TitleTextBox = new WeakReference<>(rootView.findViewById(R.id.TitleTextBox));
        textanddrawRecyclerView = new WeakReference<>(rootView.findViewById(R.id.TextAndDrawRecyclerView));
        cardView = new WeakReference<>(rootView.findViewById(R.id.card_note_creator));
        scrollView = new WeakReference<>(rootView.findViewById(R.id.nestedScroll));
        SetupNoteEditorRecycler();
        recyclerViewImage = new WeakReference<>(rootView.findViewById(R.id.ImagesRecycler));
        cardView.get().requestFocus();


        if (!mainActivity.get().names.isEmpty()) {
            if (mainActivity.get().newNote) {
                cardView.get().setTransitionName(mainActivity.get().names.get(0));
            } else {
                textanddrawRecyclerView.get().setTransitionName(mainActivity.get().names.get(0));
                cardView.get().setTransitionName(mainActivity.get().names.get(1));
                TitleTextBox.get().setTransitionName(mainActivity.get().names.get(2));
                recyclerViewImage.get().setTransitionName(mainActivity.get().names.get(3));
            }
            mainActivity.get().names.clear();
        }

        if (actualNote != null) {
            TitleTextBox.get().setText(actualNote.get().getTitle());
        }
        mainActivity.get().title = TitleTextBox;

        reloadImages();

        new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(TitleTextBox.get()));

        SoftInputAssist softInputAssist = new SoftInputAssist(requireActivity());

        boolean NewNote = mainActivity.get().newNote;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (NewNote) {

                mainActivity.get().switchToolbars(true, true, false);
            }

        }, 750);

        //  EverScrollDecorator.setUpOverScroll(scrollView.get());
        everDraw.get().setOnTouchListener((view, motionEvent) -> {

            scrollView.get().setCanScroll(false);

            int y = (int) motionEvent.getY();

            if (y >= FinalYHeight) {
                FinalYHeight = y;
            }

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                if (y >= everDraw.get().getHeight() - 75) {

                    new Handler(Looper.getMainLooper()).post(() -> {


                        TransitionManager.beginDelayedTransition(cardView.get(), new TransitionSet()
                                .addTransition(new ChangeBounds()));

                        ViewGroup.LayoutParams params = everDraw.get().getLayoutParams();

                        params.height = FinalYHeight + 100;

                        everDraw.get().setLayoutParams(params);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            scrollView.get().setCanScroll(true);
                        }, 150);

                    });

                    return false;
                }
            }
            return false;
        });

        mainActivity.get().seekBarDrawSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                everDraw.get().setStrokeWidth(i);

                if (mainActivity.get().DrawVisualizerIsShowing) {

                    ModifyDrawSizeVisualizer(i);

                } else {
                    ShowDrawSizeVisualizer();
                    ModifyDrawSizeVisualizer(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mViewModel = ViewModelProviders.of(this).get(NoteEditorFragmentMainViewModel.class);

        TitleTextBox.get().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                //new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mainActivity.get().switchToolbars(true, false, false);
                // }, 400);
            }
        });
        // TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
        //         .addTransition(new ChangeBounds()));


        if (!actualNote.get().getNoteColor().equals("000000")) {
            mainActivity.get().tintSystemBars(Integer.parseInt(actualNote.get().getNoteColor()), 500);
        }

        // }).start();
    }

    private void onBackPressed(Boolean delete) {

        if (delete) {

            //  mainActivity.get().mDatabaseEver.deleteNote(actualNote.getId());
            mainActivity.get().removeNote(actualNote.get());
            //  mainActivity.get().notesModels.remove(actualNote.getActualPosition());

        } else {

            actualNote.get().setTitle(TitleTextBox.get().getText().toString());

            mainActivity.get().actualNote = actualNote;

        }
        mainActivity.get().onBackPressed();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void TransformBitmapToFile(Bitmap bitmap, String fileType) {

        File directory = mainActivity.get().getDir("imageDir", Context.MODE_PRIVATE);

        drawPosition = list.size() - 1;


        File file = new File(directory, "Draw" +  System.currentTimeMillis() + fileType);

        String drawLocation = actualNote.get().getDrawLocation();


        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos;

            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //  mainActivity.get().mDatabaseEver.insertNoteBackgroundToDatabase(String.valueOf(actualNote.getId()), file.toString(), drawLocation);
            actualNote.get().setDrawLocation(drawLocation + file.toString() + "┼");
            mainActivity.get().updateNote(actualNote.get().getActualPosition(), actualNote.get());
            //  addNote(drawPosition, new EverLinkedMap(list.get(drawPosition).getContent(), file.toString()));
            if (list.get(drawPosition).getContent().equals("")) {
                addNoteContent(drawPosition, new EverLinkedMap("▓", file.toString()));
            } else {
                addNoteContent(drawPosition, new EverLinkedMap(list.get(drawPosition).getContent(), file.toString()));
            }


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void UpdateBitmapToFile(Bitmap bitmap) {

        File toDelete = new File(savedBitmapPath);
        if (toDelete.exists()) {
            toDelete.delete();
        }

        String s = savedBitmapPath.substring(0, savedBitmapPath.length()-17) ;
        File file = new File(s + System.currentTimeMillis() +".jpg");

        Log.d("path", file.toString());
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> strings = actualNote.get().getDraws();

        strings.set(strings.indexOf(savedBitmapPath), file.toString());

        for (int p = 0; p < strings.size(); p++) {
            if (!strings.get(p).equals("")) {
                strings.set(p, strings.get(p) + "┼");
            }
        }
        String editDraw = strings.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        actualNote.get().setDrawLocation(editDraw);
        mainActivity.get().updateNote(actualNote.get().getActualPosition(), actualNote.get());
        updateNote(drawPosition,  actualNote.get().getDraws().get(drawPosition));

    }

    public void importerClick(View view, EverPopup popupWindowHelper) {
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // new Thread(() -> {

        if (mainActivity.get().DrawVisualizerIsShowing) {

            if (resultCode != RESULT_OK) {

                Uri gif = data.getData();

                try {
                    TransformUriToFile(gif, true, ".gif", actualNote.get().getImageURLS(), actualNote.get().getId(), actualNote.get().getActualPosition());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //reloadImages();
            }
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(Objects.requireNonNull(data));
            if (resultUri != null) {
                try {
                    TransformUriToFile(resultUri, true, ".jpg", actualNote.get().getImageURLS(), actualNote.get().getId(), actualNote.get().getActualPosition());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            // reloadImages();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(Objects.requireNonNull(data));
        } else {
            if (requestCode != RESULT_OK) {

                File directory = mainActivity.get().getDir("imageDir", Context.MODE_PRIVATE);

                Uri uri = Uri.fromFile(new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + ".jpg"));

                if (data != null) {

                    UCrop.Options options = new UCrop.Options();
                    if (!actualNote.get().getNoteColor().equals("000000")) {
                        options.setToolbarColor(Integer.parseInt(actualNote.get().getNoteColor()));
                        options.setStatusBarColor(Integer.parseInt(actualNote.get().getNoteColor()));
                        options.setCropFrameColor(Integer.parseInt(actualNote.get().getNoteColor()));
                        options.setCropGridCornerColor(Integer.parseInt(actualNote.get().getNoteColor()));
                        options.setActiveControlsWidgetColor(Integer.parseInt(actualNote.get().getNoteColor()));
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

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onImageLongPress(View view, int position) {
        EverPopup popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(requireActivity()).inflate(R.layout.options_attached, null);
        popupWindowHelper = new EverPopup(popView);
        popupWindowHelper.showAsDropDown(view, 350, -25);

        List<String> htmls = actualNote.get().getImages();

        String selectedImagePath = htmls.get(position);

        // everAdapter.SelectedDrawPosition = position;

        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        ArrayList<String> images = new ArrayList<>();

        imageView.setOnClickListener(view1 -> {

            for (String html : htmls) {
                if (html.equals(selectedImagePath)) {
                    File file = new File(selectedImagePath);
                    if (file.exists()) {
                        file.delete();
                    }
                } else {
                    images.add(html + "┼");
                }
            }

            String joined_arrayPath = String.join("", images);
            actualNote.get().setImageURLS(joined_arrayPath);
            mainActivity.get().mDatabaseEver.editImages(String.valueOf(actualNote.get().getId()), joined_arrayPath);
            //mainActivity.get().notesModels.set(position, actualNote);

            view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter));

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                view.setVisibility(View.GONE);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    imageAdapter.get().updateAdapter(joined_arrayPath, position);
                    if (joined_arrayPath.equals("")) {
                        reloadImages();
                    }
                }, 25);
            }, 50);
            popupWindowHelper.dismiss();
        });
    }


    public void onItemClick(View view, int position) {

        //  everAdapter.get().setFinalYHeight(0);
        //  savedbitmap = everAdapter.get().getDrawBitmap(position);
        //  savedBitmapPath = everAdapter.getImagePath(position);
        //  drawPosition = position;
        //  System.out.println("Selected position of draw is =" + drawPosition);
        //   drawFromRecycler = true;
        //   everDraw = everAdapter.getSelectedDraw(position);
        //   CloseOrOpenDraWOptionsFromRecycler(false);
    }

    public void onItemClickTemporaryINHERE_REMOVE_LATER(EverDraw view, int position, Bitmap bitmap, String path, CardView swipe) {


        // everAdapter.setFinalYHeight(0);
        this.card = swipe;
        drawToRemove = view;
        savedbitmap = new WeakReference<>(bitmap);
        savedBitmapPath = path;
        drawPosition = position;
        System.out.println("Selected position of draw is =" + drawPosition);
        drawFromRecycler = true;
        this.everDraw = new WeakReference<>(view);
        CloseOrOpenDraWOptionsFromRecycler(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    public void onLongPress(View view, int position) {

        String finalS;
        List<String> htmls = actualNote.get().getDraws();
        List<String> strings = actualNote.get().getContents();
        List<String> arrayList = new ArrayList<>();
        List<String> stringsList = new ArrayList<>();
        String selectedDrawPath = list.get(position).getDrawLocation();
        int positionToRemove = position;
        String toReplace = "";
        int positionToReplace = 0;

        for (String s : strings) {
            if (s.equals(strings.get(positionToRemove))) {
                toReplace = strings.get(positionToRemove);
                positionToReplace = positionToRemove;
            } else {
                stringsList.add(s + "┼");
            }
        }
        if (list.get(positionToRemove + 1).getContent().equals("▓")) {
            finalS = toReplace + "┼";
        } else {
            finalS = toReplace + "<br>" + list.get(positionToRemove + 1).getContent() + "┼";
        }

        if (stringsList.size() > positionToReplace) {
            stringsList.set(positionToReplace, finalS);
        } else {
            stringsList.add(finalS);
        }

        for (String html : htmls) {
            if (!html.equals(selectedDrawPath)) {
                arrayList.add(html + "┼");
            } else {
                File file = new File(selectedDrawPath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        String joined_arrayString = String.join("", stringsList);
        String joined_arrayPath = String.join("", arrayList);
        actualNote.get().setDrawLocation(joined_arrayPath);
        actualNote.get().setContent(joined_arrayString);
        mainActivity.get().updateNote(actualNote.get().getActualPosition(), actualNote.get());
        updateNoteContent(positionToRemove, finalS.replaceAll("┼", ""));
        stringsList.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SetupNoteEditorRecycler() {

        bitmaps.clear();

        list = new ListSection<>();

        int i = 0;

        bitmaps = actualNote.get().getDraws();
        List<String> contentsSplitted = actualNote.get().getContents();

        if (bitmaps.size() != contentsSplitted.size()) {
            for (i = bitmaps.size(); i < contentsSplitted.size(); i++) {
                bitmaps.add("");
            }
        }
        int size = contentsSplitted.size();

        for (i = 0; i < size; i++) {
            list.add(new EverLinkedMap(contentsSplitted.get(i), bitmaps.get(i)));
        }

        if (list.size() == 0) {
            list.add(new EverLinkedMap("", ""));
        }
        if (list.size() != 0 && !list.get(list.size() - 1).getDrawLocation().equals("")) {
            list.add(new EverLinkedMap("", ""));
        }

        //TODO WHEN CREATING A NEW DRAW WITHOUT CONTENT IS NOT BEING REMOVED

        LinearLayoutManager a = new LinearLayoutManager(requireActivity());
        textanddrawRecyclerView.get().setLayoutManager(a);
        adptr = new WeakReference<>(new MultiViewAdapter());
        adptr.get().registerItemBinders(new NoteContentsBinder(requireActivity()));
        adptr.get().addSection(list);
        textanddrawRecyclerView.get().setItemAnimator(new LandingAnimator(new EvershootInterpolator(1F)));
        textanddrawRecyclerView.get().setAdapter(new AlphaInAnimationAdapter(adptr.get()));
        mainActivity.get().contentRecycler = textanddrawRecyclerView;
        mainActivity.get().cardNoteCreator = cardView;
    }


    public void startPostponeTransition() {
        startPostponedEnterTransition();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveBitmapFromDraw(boolean fromRecycler) {
        textanddrawRecyclerView.get().suppressLayout(false);
        if (fromRecycler) {
            if (mainActivity.get().DrawOn) {

                if (!everDraw.get().getMPaths().isEmpty()) {

                    UpdateBitmapToFile(((MainActivity)requireActivity()).createBitmapFromView(card, card.getWidth(),card.getHeight()));

                    everDraw.get().clearCanvas();

                    FinalYHeight = 0;

                }
                CloseOrOpenDraWOptionsFromRecycler(true);
            } else {
                onBackPressed(false);
            }
        } else {

            if (mainActivity.get().DrawOn) {

                Bitmap bitmap = everDraw.get().getBitmap(Color.WHITE);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, cardWidth, FinalYHeight + 75);

                if (!everDraw.get().getMPaths().isEmpty()) {

                    CloseOrOpenDraWOptions(0, true);

                    delayedHandler = new Handler(Looper.getMainLooper());
                    delayedHandler.postDelayed(() -> {
                        TransformBitmapToFile(resizedBitmap, ".jpeg");
                    }, 250);

                    everDraw.get().clearCanvas();

                    FinalYHeight = 0;

                } else {

                    CloseOrOpenDraWOptions(0, true);

                    FinalYHeight = 0;
                }

            } else {
                onBackPressed(false);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void mergeBitmaps(Bitmap baseBitmap, Bitmap mergeBitmap) {

        EverBitmapMerger task = new EverBitmapMerger();
        task.setBaseBitmap(baseBitmap)
                .setMergeBitmap(mergeBitmap)
                .setMergeListener((task1, mergedBitmap) -> {
                    finalBitmap = new WeakReference<>(mergedBitmap);
                    UpdateBitmapToFile(finalBitmap.get());
                })
                .merge();
    }

    private void CloseOrOpenDraWOptions(int height, boolean close) {

        mainActivity.get().CloseOrOpenDrawOptions(height, close);
    }

    private void CloseOrOpenDraWOptionsFromRecycler(boolean close) {

        CloseOpenedButtons(true, close);

        mainActivity.get().CloseOrOpenDraWOptionsFromRecycler(scrollView.get(), textanddrawRecyclerView.get(), close);

    }

    private void ShowDrawSizeVisualizer() {

        mainActivity.get().ShowDrawSizeVisualizer();
    }

    private void ModifyDrawSizeVisualizer(int value) {

        mainActivity.get().ModifyDrawSizeVisualizer(value);

    }

    private void CloseOpenedButtons(boolean isDraw, boolean close) {
        if (!isDraw) {
            mainActivity.get().CloseOrOpenDrawOptions(0, close);

        }
    }

    public void reloadImages() {
        StaggeredGridLayoutManager staggeredGridLayoutManagerImage = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerViewImage.get().setLayoutManager(staggeredGridLayoutManagerImage);

        MultiViewAdapter adapter = new MultiViewAdapter();
        listImages = new ListSection<>();

        listImages.addAll(actualNote.get().getImages());
        System.out.println("aaaaaaaaaaaaaa " + list.size() + "imgs =" + actualNote.get().getImages().toString());
        adapter.addSection(listImages);
        adapter.registerItemBinders(new ImagesBinder(requireActivity(), 0, false));
        recyclerViewImage.get().setItemAnimator(new LandingAnimator(new EvershootInterpolator(1f)));
        recyclerViewImage.get().setAdapter(new AlphaInAnimationAdapter(adapter));

        if (actualNote.get().getImages().size() > 0) {

            recyclerViewImage.get().setVisibility(View.VISIBLE);


        } else {
            recyclerViewImage.get().setVisibility(View.GONE);
        }
        mainActivity.get().imageRecycler = recyclerViewImage;
    }

    public void updateNote(int p, String everLinkedMap) {
        mainActivity.get().beginDelayedTransition(cardView.get());
        list.get(p).setDrawLocation(everLinkedMap);
        adptr.get().notifyItemChanged(p);
    }

    public void updateNoteContent(int p, String newString) {
        View currentFocus = requireActivity().getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        mainActivity.get().beginDelayedTransition(cardView.get());
        removeNote(p);
        list.set(p, new EverLinkedMap(newString, list.get(p).getDrawLocation()));
    }

    public void removeNote(int p) {
        list.remove(p);
    }

    public void addNoteContent(int p, EverLinkedMap newEverLinkedMap) {
        mainActivity.get().beginDelayedTransition(cardView.get());
        list.set(p, newEverLinkedMap);
        list.add(new EverLinkedMap("", ""));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void removeImage(int p) {
        List<String> toRemove = new ArrayList<>();
        for (String paths : listImages.getData()) {
            if (paths.equals(listImages.get(p))) {
                File file = new File(paths);
                if (file.exists()) {
                    file.delete();
                }
            } else {
                toRemove.add(paths + "┼");
            }
        }
        mainActivity.get().mDatabaseEver.editImages(String.valueOf(actualNote.get().getId()), String.join("", toRemove));
        actualNote.get().setImageURLS(String.join("", toRemove));
        listImages.remove(p);
    }

    public void addImage(String path) {
        //  new Handler(Looper.getMainLooper()).post(() -> {
        if (recyclerViewImage.get().getVisibility() == View.GONE) {
            recyclerViewImage.get().setVisibility(View.VISIBLE);
        }
        mainActivity.get().beginDelayedTransition(cardView.get());
        listImages.add(path);
        if (listImages.size() != 1) {
            recyclerViewImage.get().removeViewAt(listImages.size() - 2);
        }

        // });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void TransformUriToFile(Uri uri, boolean addToDatabase, String fileType, String imagesUrls, int ID, int position) throws
            IOException {

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mainActivity.get().getContentResolver(), uri);

        File directory = mainActivity.get().getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + fileType);

        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos;

            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();


            if (addToDatabase) {
                addImage(file.toString());
                //   actualNote.get().setImageURLS(file.toString() + "┼" + imagesUrls.replaceAll("[\\[\\](){}]", ""));
                //  actualNote.get().getImages().add(file.toString());
                actualNote.get().setImages(listImages.getData());
                //   actualNote.get().setImageURLS(file.toString() + "┼" + actualNote.get().getImageURLS());
                // actualNote.get().setImageURLS(file.toString() + "┼" + imagesUrls);
                //   noteModelSection.get(position).setImageURLS(file.toString() + "┼" + imagesUrls.replaceAll("[\\[\\](){}]", ""));
                // mDatabaseEver.insertImageToDatabase(String.valueOf(ID), file.toString(), imagesUrls.replaceAll("[\\[\\](){}]", ""));
                mainActivity.get().updateNote(position, actualNote.get());
            }
        }
    }

}
