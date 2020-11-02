package com.example.Evermind.ui.dashboard.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.ChangeBounds;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.EverBitmapMerger;
import com.example.Evermind.EverDraw;
import com.example.Evermind.EverFlowScrollView;
import com.example.Evermind.EverPopup;
import com.example.Evermind.ImagesBinder;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteContentsBinder;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.SoftInputAssist;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.richeditor.RichEditor;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.adapters.StaticOverScrollDecorAdapter;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

import static android.app.Activity.RESULT_OK;

public class NoteEditorFragmentJavaFragment extends Fragment implements EverAdapter.ItemClickListener, ImagesRecyclerGridAdapter.ItemClickListener {

    private static final String GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public RecyclerView textanddrawRecyclerView;
    public boolean drawFromRecycler = false;
    public boolean hasImages = false;
    public boolean hasDraws = false;
    public Note_Model actualNote;
    public EverAdapter everAdapter;
    public EverDraw everDraw;
    public int FinalYHeight;
    public RichEditor activeEditor;
    public int activeEditorPosition = -1;
    public MultiViewAdapter adptr = new MultiViewAdapter();
    public ListSection<EverLinkedMap> list = new ListSection<>();
    List<String> bitmaps = new ArrayList<>();
    private NoteEditorFragmentMainViewModel mViewModel;
    private EverFlowScrollView scrollView;
    private EditText TitleTextBox;
    private CardView cardView;
    private Bitmap savedbitmap;
    private Bitmap newDrawedbitmap;
    private Bitmap finalBitmap;
    private int drawPosition = 0;
    private String savedBitmapPath;
    private ImagesRecyclerGridAdapter imageAdapter;
    private String originalString;
    private RecyclerView recyclerViewImage;
    private boolean canFocusTitle = false;
    private Handler delayedHandler;
    private Handler noDelayHandler;
    private Thread assistiveThread;
    private MainActivity mainActivity;

    public static NoteEditorFragmentJavaFragment newInstance() {
        return new NoteEditorFragmentJavaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        postponeEnterTransition();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
            setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
        }


        return inflater.inflate(R.layout.fragment_note_creator, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainActivity = ((MainActivity)requireActivity());

        mainActivity.noteCreator = this;

        actualNote = mainActivity.actualNote;

        if (actualNote.getImageURLS().length() > 1) {
            hasImages = true;
        }
        if (actualNote.getDrawLocation().length() > 1) {
            hasDraws = true;
        }

        everDraw =mainActivity.findViewById(R.id.EverDraw);
        scrollView =mainActivity.findViewById(R.id.scrollview);
        TitleTextBox =mainActivity.findViewById(R.id.TitleTextBox);
        textanddrawRecyclerView =mainActivity.findViewById(R.id.TextAndDrawRecyclerView);
        cardView =mainActivity.findViewById(R.id.card_note_creator);
        SetupNoteEditorRecycler();
        recyclerViewImage =mainActivity.findViewById(R.id.ImagesRecycler);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            canFocusTitle = true;
        }, 1000);

        if (!mainActivity.names.isEmpty()) {
            if (mainActivity.newNote) {
                cardView.setTransitionName(mainActivity.names.get(0));
            } else {
                textanddrawRecyclerView.setTransitionName(mainActivity.names.get(0));
                cardView.setTransitionName(mainActivity.names.get(1));
                TitleTextBox.setTransitionName(mainActivity.names.get(2));
                recyclerViewImage.setTransitionName(mainActivity.names.get(3));
            }
            mainActivity.names.clear();
        }

        if (actualNote != null) {
            TitleTextBox.setText(actualNote.getTitle());
        }
        mainActivity.title = TitleTextBox;

        reloadImages();

        new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(TitleTextBox));

        SoftInputAssist softInputAssist = new SoftInputAssist(requireActivity());

        boolean NewNote = mainActivity.newNote;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (NewNote) {

                mainActivity.switchToolbars(true, false, false);

                InputMethodManager keyboard = (InputMethodManager)mainActivity.getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(TitleTextBox, 0);
            }

        }, 750);

        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        everDraw.setOnTouchListener((view, motionEvent) -> {

            int y = (int) motionEvent.getY();

            if (y >= FinalYHeight) {
                FinalYHeight = y;
            }

            if (y >= everDraw.getHeight() - 75) {

                new Handler(Looper.getMainLooper()).post(() -> {

                    mainActivity.beginDelayedTransition(cardView);

                    ViewGroup.LayoutParams params = everDraw.getLayoutParams();

                    params.height = FinalYHeight + 200;

                    everDraw.setLayoutParams(params);

                });

            }
            return false;
        });

        mainActivity.seekBarDrawSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                everDraw.setStrokeWidth(i);

                if (mainActivity.DrawVisualizerIsShowing) {

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

        ImageButton blackDraw = mainActivity.BlackDraw;
        ImageButton blueDraw = mainActivity.BlueDraw;
        ImageButton purpleDraw = mainActivity.PurpleDraw;
        ImageButton magentaDraw = mainActivity.MagentaDraw;
        ImageButton orangeDraw = mainActivity.OrangeDraw;
        ImageButton yellowDraw = mainActivity.YellowDraw;
        ImageButton greenDraw = mainActivity.GreenDraw;

        TitleTextBox.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                //new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    mainActivity.switchToolbars(true, false, false);
               // }, 400);
            }
        });

        blackDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Black"));

        blueDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Blue"));

        purpleDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Purple"));

        magentaDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Magenta"));

        orangeDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Orange"));

        yellowDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Yellow"));

        greenDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Green"));

        mainActivity.Delete.setOnClickListener(view -> {

            if (mainActivity.DrawOn) {
                everDraw.clearCanvas();
                CloseOrOpenDraWOptions(0, true);
                FinalYHeight = 0;
            } else {
                mainActivity.deleteNoteDialog(actualNote.getActualPosition(), actualNote.getId());
            }
        });


        mainActivity.Save.setOnClickListener(view -> {

            if (drawFromRecycler) {

                Bitmap toResizeBitmap = everDraw.getBitmap(Color.TRANSPARENT);

                if (toResizeBitmap.getHeight() >= FinalYHeight + 75) {
                    newDrawedbitmap = Bitmap.createBitmap(toResizeBitmap, 0, 0, toResizeBitmap.getWidth(), FinalYHeight + 75);
                } else {
                    newDrawedbitmap = Bitmap.createBitmap(toResizeBitmap, 0, 0, toResizeBitmap.getWidth(), FinalYHeight);
                }

                SaveBitmapFromDraw(true);
                drawFromRecycler = false;
            } else {

                String content = actualNote.getContent();

                mainActivity.mDatabaseEver.editContent(String.valueOf(actualNote.getId()), content + "┼");

                SaveBitmapFromDraw(false);

            }
        });


        mainActivity.Undo.setOnClickListener(view -> {

            mainActivity.animateObject(mainActivity.Undo, "rotation", -360, 250);

            if (mainActivity.DrawOn) {
                everDraw.undo();
            } else {
                activeEditor.undo();
            }
        });

        mainActivity.Redo.setOnClickListener(view -> {

            mainActivity.animateObject(mainActivity.Redo, "rotation", 360, 250);

            if (mainActivity.DrawOn) {
                everDraw.redo();
            } else {
                activeEditor.redo();
            }
        });
        // TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
        //         .addTransition(new ChangeBounds()));

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ConstraintLayout constraintLayout =mainActivity.findViewById(R.id.note_creator_constraint);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.card_note_creator, ConstraintSet.BOTTOM, R.id.card_note_creator, ConstraintSet.BOTTOM, 400);
            constraintSet.applyTo(constraintLayout);
        }, 450);

        if (!actualNote.getNoteColor().equals("000000")) {
            mainActivity.tintSystemBars(Integer.parseInt(actualNote.getNoteColor()));
        }

        // }).start();

    }

    private void onBackPressed(Boolean delete) {

        if (delete) {

            //  mainActivity.mDatabaseEver.deleteNote(actualNote.getId());
            mainActivity.removeNote(actualNote.getActualPosition(), actualNote.getId());
            //  mainActivity.notesModels.remove(actualNote.getActualPosition());

        } else {

            actualNote.setTitle(TitleTextBox.getText().toString());

            mainActivity.actualNote = actualNote;

        }
       mainActivity.onBackPressed();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void TransformBitmapToFile(Bitmap bitmap, String fileType) {

        File directory =mainActivity.getDir("imageDir", Context.MODE_PRIVATE);

        drawPosition = list.size() - 1;

        File file = new File(directory, "Draw" + "RealID" + actualNote.getId() + "WithID" + drawPosition + fileType);

        String drawLocation = actualNote.getDrawLocation();


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
            //  mainActivity.mDatabaseEver.insertNoteBackgroundToDatabase(String.valueOf(actualNote.getId()), file.toString(), drawLocation);
            actualNote.setDrawLocation(drawLocation + file.toString() + "┼");
            mainActivity.updateNote(actualNote.getActualPosition(), actualNote);
          //  addNote(drawPosition, new EverLinkedMap(list.get(drawPosition).getContent(), file.toString()));
            if (list.get(drawPosition).getContent().equals("")) {
                addNote(drawPosition, new EverLinkedMap("▓", file.toString()));
            } else {
                addNote(drawPosition, new EverLinkedMap(list.get(drawPosition).getContent(), file.toString()));
            }


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void UpdateBitmapToFile(Bitmap bitmap) {

        File toDelete = new File(savedBitmapPath);
        if (toDelete.exists()) {
            toDelete.delete();
        }

        File file = new File(savedBitmapPath);

        Log.d("path", file.toString());
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> strings = actualNote.getDraws();

        strings.set(strings.indexOf(savedBitmapPath), file.toString());

        for (int p = 0; p < strings.size(); p++) {
            if (!strings.get(p).equals("")) {
                strings.set(p, strings.get(p) + "┼");
            }
        }

        String editDraw = strings.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        ;
        actualNote.setDrawLocation(editDraw);
        mainActivity.updateNote(actualNote.getActualPosition(), actualNote);
        updateNote(drawPosition, new EverLinkedMap(actualNote.getContents().get(drawPosition), actualNote.getDraws().get(drawPosition)));
        mainActivity.ClearIonCache();
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
        popupWindowHelper.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        new Thread(() -> {

            if (mainActivity.DrawVisualizerIsShowing) {

                if (resultCode != RESULT_OK) {

                    Uri gif = data.getData();

                    try {
                        mainActivity.TransformUriToFile(gif, true, ".gif", actualNote.getImageURLS(), actualNote.getId(), actualNote.getActualPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    reloadImages();
                }
            }

            if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(Objects.requireNonNull(data));
                if (resultUri != null) {
                    try {
                        mainActivity.TransformUriToFile(resultUri, true, ".jpg", actualNote.getImageURLS(), actualNote.getId(), actualNote.getActualPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                reloadImages();
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(Objects.requireNonNull(data));
            } else {
                if (requestCode != RESULT_OK) {

                    File directory =mainActivity.getDir("imageDir", Context.MODE_PRIVATE);

                    Uri uri = Uri.fromFile(new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + ".jpg"));

                    if (data != null) {

                        UCrop.Options options = new UCrop.Options();
                        if (!actualNote.getNoteColor().equals("000000")) {
                            options.setToolbarColor(Integer.parseInt(actualNote.getNoteColor()));
                            options.setStatusBarColor(Integer.parseInt(actualNote.getNoteColor()));
                            options.setCropFrameColor(Integer.parseInt(actualNote.getNoteColor()));
                            options.setCropGridCornerColor(Integer.parseInt(actualNote.getNoteColor()));
                            options.setActiveControlsWidgetColor(Integer.parseInt(actualNote.getNoteColor()));
                        }
                        UCrop.of(Objects.requireNonNull(data.getData()), uri)
                                .withOptions(options)
                                .start(requireActivity(), this);
                    }
                }
            }

            super.onActivityResult(requestCode, resultCode, data);
        }).start();
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

        ArrayList<String> htmls = actualNote.getImages();

        String selectedImagePath = htmls.get(position);

        everAdapter.SelectedDrawPosition = position;

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
            actualNote.setImageURLS(joined_arrayPath);
            mainActivity.mDatabaseEver.editImages(String.valueOf(actualNote.getId()), joined_arrayPath);
            //mainActivity.notesModels.set(position, actualNote);

            view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter));

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                view.setVisibility(View.GONE);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    imageAdapter.updateAdapter(joined_arrayPath, position);
                    if (joined_arrayPath.equals("")) {
                        reloadImages();
                    }
                }, 25);
            }, 50);
            popupWindowHelper.dismiss();
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        everAdapter.setFinalYHeight(0);
        savedbitmap = everAdapter.getDrawBitmap(position);
        savedBitmapPath = everAdapter.getImagePath(position);
        drawPosition = position;
        System.out.println("Selected position of draw is =" + drawPosition);
        drawFromRecycler = true;
        everDraw = everAdapter.getSelectedDraw(position);
        CloseOrOpenDraWOptionsFromRecycler(false);
    }

    public void onItemClickTemporaryINHERE_REMOVE_LATER(EverDraw view, int position, Bitmap bitmap, String path) {


        // everAdapter.setFinalYHeight(0);
        savedbitmap = bitmap;
        savedBitmapPath = path;
        drawPosition = position;
        System.out.println("Selected position of draw is =" + drawPosition);
        drawFromRecycler = true;
        this.everDraw = view;
        CloseOrOpenDraWOptionsFromRecycler(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLongPress(View view, int position) {

        EverPopup popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(getActivity()).inflate(R.layout.options_attached, null);
        popupWindowHelper = new EverPopup(popView);
        popupWindowHelper.showAsDropDown(view, 350, -25);

        String selectedDrawPath = list.get(position).getDrawLocation();

        int positionToRemove = position;

        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> stringsList = new ArrayList<>();

        imageView.setOnClickListener(view1 -> {
            // TODO  String[] htmls =  mainActivity.mDatabaseEver.getBackgroundFromDatabaseWithID(realID).split("┼");
            //  String[] strings =  mainActivity.mDatabaseEver.getContentsFromDatabaseWithID(realID).split("┼");
            ArrayList<String> htmls = actualNote.getDraws();
            ArrayList<String> strings = actualNote.getContents();

            String toReplace = "";
            int positionToReplace = -1;

            for (int i = 0; i < strings.size(); i++) {
                if (i != position) {
                    stringsList.add(strings.get(i) + "┼");
                } else {
                    if (i != strings.size() && i != 0) {
                        toReplace = strings.get(i);
                        positionToReplace = i;
                    } else if (i == strings.size()) {
                        toReplace = strings.get(i--);
                        positionToReplace = i--;
                    } else {
                        toReplace = strings.get(i);
                        positionToReplace = i;
                    }
                    stringsList.add("");
                }
            }
            if (!toReplace.isEmpty()) {
                if (toReplace.equals("▓")) {
                    toReplace = "";
                }

                if (positionToReplace != (stringsList.size() - 1) && positionToReplace < (stringsList.size() - 1)) {
                    originalString = stringsList.get(positionToReplace);
                    if (originalString.equals("") && !toReplace.equals("")) {
                        stringsList.set(positionToReplace, toReplace);
                    } else {
                        if (toReplace.equals("")) {
                            if (originalString.equals("▓")) {
                                originalString = "";
                            }
                            stringsList.set(positionToReplace, toReplace + originalString);
                        } else {
                            if (originalString.equals("▓")) {
                                originalString = "";
                            }
                            stringsList.set(positionToReplace, toReplace + "<br>" + originalString);
                        }
                    }
                } else if (positionToReplace == stringsList.size() && stringsList.size() > 0) {
                    String s1 = stringsList.get(stringsList.size() + 1);
                    stringsList.set(stringsList.size() + 1, s1 + "<br>" + toReplace);
                } else {
                    //TODO \/ MAYBE WE NEED TO REMOVE THIS BECAUSE IT MAY NEVER BE CALLED
                    String s2 = stringsList.get(positionToReplace--);
                    stringsList.set(0, toReplace + "<br>" + s2);
                }

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

            String[] finalArray = arrayList.toArray(new String[0]);
            String[] finalString = stringsList.toArray(new String[0]);
            String joined_arrayString = String.join("", finalString);
            String joined_arrayPath = String.join("", finalArray);
            System.out.println("Strings is = " + joined_arrayString);
            System.out.println("draws is = " + joined_arrayPath);
            // mainActivity.mDatabaseEver.editContent(String.valueOf(actualNote.getId()), joined_arrayString);
            //  mainActivity.notesModels.get(actualNote.getActualPosition()).setContent(joined_arrayString);
            //  mainActivity.mDatabaseEver.editDraw(String.valueOf(actualNote.getId()), joined_arrayPath);
            //   mainActivity.notesModels.get(actualNote.getActualPosition()).setDrawLocation(joined_arrayPath);
            actualNote.setDrawLocation(joined_arrayPath);
            actualNote.setContent(joined_arrayString);
            mainActivity.updateNote(actualNote.getActualPosition(), actualNote);

            //view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter));

            // SetupNoteEditorRecycler(true, true, true, false, positionToRemove);
            removeNote(positionToRemove);

            stringsList.clear();
            popupWindowHelper.dismiss();
        });


    }

    public void DrawColorClickedSwitcher(String color) {

        switch (color) {

            case "Black":

                everDraw.setColor(GetColor(R.color.Black));

                CloseOrOpenDrawColors(false);

                break;

            case "Blue":

                everDraw.setColor(GetColor(R.color.SkyBlue));

                CloseOrOpenDrawColors(false);

                break;

            case "Purple":

                everDraw.setColor(GetColor(R.color.Pink));

                CloseOrOpenDrawColors(false);

                break;

            case "Magenta":

                everDraw.setColor(GetColor(R.color.Magenta));

                CloseOrOpenDrawColors(false);

                break;

            case "Orange":


                everDraw.setColor(GetColor(R.color.Orange));

                CloseOrOpenDrawColors(false);

                break;

            case "Yellow":

                everDraw.setColor(GetColor(R.color.YellowSun));

                CloseOrOpenDrawColors(false);

                break;

            case "Green":

                everDraw.setColor(GetColor(R.color.GrassGreen));

                CloseOrOpenDrawColors(false);

                break;

            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SetupNoteEditorRecycler() {

       // items = new ArrayList<>();
        bitmaps.clear();

        int i = 0;

        //Collections.reverse(html);

        bitmaps = actualNote.getDraws();
        List<String> contentsSplitted = actualNote.getContents();

        if (bitmaps.size() != contentsSplitted.size()) {
            for (i = bitmaps.size(); i < contentsSplitted.size(); i++) {
              bitmaps.add( "");
                System.out.println("added bit = " + i + " times.");
            }
        }
        int size = contentsSplitted.size();

        for (i = 0; i < size; i++) {
            list.add(new EverLinkedMap(contentsSplitted.get(i), bitmaps.get(i)));
              System.out.println(i);
        }

        if (list.size() == 0) {
            list.add(new EverLinkedMap("", ""));
        }
        if (list.size() != 0 && !list.get(list.size() - 1).getDrawLocation().equals("")) {
            list.add(new EverLinkedMap("", ""));
        }

        //TODO WHEN CREATING A NEW DRAW WITHOUT CONTENT IS NOT BEING REMOVED

        LinearLayoutManager a = new LinearLayoutManager(requireActivity());
        textanddrawRecyclerView.setLayoutManager(a);

        adptr.registerItemBinders(new NoteContentsBinder(requireActivity(), list.size()));
        adptr.addSection(list);
        textanddrawRecyclerView.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1F)));
        textanddrawRecyclerView.setAdapter(new AlphaInAnimationAdapter(adptr));
        mainActivity.contentRecycler = textanddrawRecyclerView;
        mainActivity.cardNoteCreator = cardView;


        //  adptr.setClickListener(this);


    }


    public void startPostponeTransition() {
        startPostponedEnterTransition();
    }

    private String IfContentIsBiggerReturnNothing(int content, int bitmap, List<String> string, Integer i) {
        if (content > bitmap && i == content - 1) {
            return "";
        }
        return string.get(i);
    }

    private int SeeIfIsHigherThan(int base, int toCalc) {
        return Math.max(base, toCalc);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SaveBitmapFromDraw(boolean fromRecycler) {
        textanddrawRecyclerView.suppressLayout(false);
        if (fromRecycler) {
            if (mainActivity.DrawOn) {

                if (!everDraw.getMPaths().isEmpty()) {

                    System.out.println("is valid 1 = " + savedbitmap.toString() + " is valid 2 = " + newDrawedbitmap.toString());

                    mergeBitmaps(savedbitmap, newDrawedbitmap);

                    everDraw.clearCanvas();

                    FinalYHeight = 0;


                    // SetupNoteEditorRecycler(true, true, false, false, drawPosition);

                }
                CloseOrOpenDraWOptionsFromRecycler(true);
            } else {
                onBackPressed(false);
            }
        } else {

            if (mainActivity.DrawOn) {

                Bitmap bitmap = everDraw.getBitmap(Color.WHITE);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), FinalYHeight + 75);

                if (!everDraw.getMPaths().isEmpty()) {

                    CloseOrOpenDraWOptions(0, true);

                    delayedHandler = new Handler(Looper.getMainLooper());
                    delayedHandler.postDelayed(() -> {
                        TransformBitmapToFile(resizedBitmap, ".jpeg");
                    }, 250);

                    everDraw.clearCanvas();

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
                    finalBitmap = mergedBitmap;
                    UpdateBitmapToFile(finalBitmap);
                })
                .merge();
    }

    private void CloseOrOpenDraWOptions(int height, boolean close) {

        // CloseOpenedButtons(true, close);

        if (height == 0) {

            mainActivity.CloseOrOpenDraWOptions(0, close);

        } else {

            mainActivity.CloseOrOpenDraWOptions(height, close);
        }
    }

    private void CloseOrOpenDraWOptionsFromRecycler(boolean close) {

        CloseOpenedButtons(true, close);

        mainActivity.CloseOrOpenDraWOptionsFromRecycler(scrollView, textanddrawRecyclerView, close);

    }

    private void CloseOrOpenDrawColors(boolean close) {

        mainActivity.CloseOrOpenDrawColors(close);
    }

    private void ShowDrawSizeVisualizer() {

        mainActivity.ShowDrawSizeVisualizer();
    }

    private void ModifyDrawSizeVisualizer(int value) {

        mainActivity.ModifyDrawSizeVisualizer(value);

    }

    private void CloseOpenedButtons(boolean isDraw, boolean close) {
        if (!isDraw) {
            mainActivity.CloseOrOpenDraWOptions(0, close);

        }
    }

    public void reloadImages() {
        //TODO  String imagesURLs = mainActivity.mDatabaseEver.getImageURLFromDatabaseWithID(realID);
        ArrayList<String> imagesURLs = actualNote.getImages();
        StaggeredGridLayoutManager staggeredGridLayoutManagerImage = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (!actualNote.getImageURLS().equals("")) {

                Toast.makeText(requireActivity(), "img size =" + imagesURLs.size() + "cont =" + imagesURLs.toString(), Toast.LENGTH_SHORT).show();

                recyclerViewImage.setLayoutManager(staggeredGridLayoutManagerImage);

                MultiViewAdapter adapter = new MultiViewAdapter();
                ListSection<String> list = new ListSection<>();
                list.addAll(actualNote.getImages());
                adapter.addSection(list);
                adapter.registerItemBinders(new ImagesBinder(requireActivity(), list.size(), false));

               // imageAdapter = new ImagesRecyclerGridAdapter(requireActivity(), imagesURLs, false);

                recyclerViewImage.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));

                recyclerViewImage.setVisibility(View.VISIBLE);
                recyclerViewImage.setAdapter(new AlphaInAnimationAdapter(adapter));

             //   imageAdapter.setClickListener(this);

            } else {
                recyclerViewImage.setVisibility(View.GONE);
            }
            mainActivity.imageRecycler = recyclerViewImage;

        }, 200);
    }

    public void updateNote(int p, EverLinkedMap everLinkedMap) {
        mainActivity.beginDelayedTransition(cardView);
      //  list.get(p).setContent(everLinkedMap.getContent());
        list.get(p).setDrawLocation(everLinkedMap.getDrawLocation());
        textanddrawRecyclerView.removeViewAt(p);
        adptr.notifyItemChanged(p);
    }

    public void removeNote(int p) {
        list.remove(p);
    }

    public void addNote(int p, EverLinkedMap newEverLinkedMap) {
        mainActivity.beginDelayedTransition(cardView);
        list.get(p).setContent(newEverLinkedMap.getContent());
        list.get(p).setDrawLocation(newEverLinkedMap.getDrawLocation());
        textanddrawRecyclerView.removeViewAt(p);
       // adptr.notifyItemChanged(p);
        list.add(new EverLinkedMap("", ""));
    }


}
