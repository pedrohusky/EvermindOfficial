package com.example.Evermind.ui.dashboard.ui.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.os.Handler;
import android.os.Looper;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.Evermind.EverBitmapMerger;
import com.example.Evermind.EverDraw;
import com.example.Evermind.EverFlowScrollView;
import com.example.Evermind.EverTransition;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.MainActivity;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.SoftInputAssist;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.example.Evermind.ui.note_screen.NotesScreen;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.adapters.StaticOverScrollDecorAdapter;

import static android.app.Activity.RESULT_OK;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class NoteEditorFragmentJavaFragment extends Fragment implements EverAdapter.ItemClickListener, ImagesRecyclerGridAdapter.ItemClickListener {

    private NoteEditorFragmentMainViewModel mViewModel;
    private EverAdapter everAdapter;
    public RecyclerView textanddrawRecyclerView;
    private EverFlowScrollView scrollView;
    private EditText TitleTextBox;
    private CardView cardView;
    private List<EverLinkedMap> items = new ArrayList<>();
    private  ArrayList<String> toAdd ;
    private int i;
  //  private int realID;
    private EverDraw everDraw;
    private int FinalYHeight;
    private Bitmap savedbitmap;
    private Bitmap newDrawedbitmap;
    private Bitmap finalBitmap;
    private int drawPosition = 0;
    public boolean drawFromRecycler = false;
    private String savedBitmapPath;
    List<String> bitmaps = new ArrayList<>();
    private boolean openedFromButton = false;
    private ImagesRecyclerGridAdapter imageAdapter;
    private String originalString;
    private Note_Model actualNote;

    private RecyclerView recyclerViewImage;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos";

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

        ((MainActivity)requireActivity()).noteCreator = this;

        Bundle arguments = getArguments();
        if (arguments != null) {
            actualNote = (Note_Model)arguments.getSerializable("noteModel");
//             textanddrawRecyclerView.setBackgroundColor(Integer.parseInt(actualNote.getNoteColor()));
            ((MainActivity)requireActivity()).arrays=arguments.getStringArray("arrays");
            arguments.clear();
        }


        recyclerViewImage = requireActivity().findViewById(R.id.ImagesRecycler);
        if (actualNote.getImageURLS().length() > 1) {
            recyclerViewImage.setVisibility(View.VISIBLE);
        }
        everDraw = requireActivity().findViewById(R.id.EverDraw);
        scrollView = requireActivity().findViewById(R.id.scrollview);
        TitleTextBox = requireActivity().findViewById(R.id.TitleTextBox);
        textanddrawRecyclerView = requireActivity().findViewById(R.id.TextAndDrawRecyclerView);
        cardView = requireActivity().findViewById(R.id.card_note_creator);

        SetupNoteEditorRecycler(false, false, false, false, 0);

        reloadImages();

        if (actualNote != null) {
            TitleTextBox.setText(actualNote.getTitle());
            TitleTextBox.setBackgroundColor(Integer.parseInt(actualNote.getNoteColor()));
        }
        ((MainActivity) requireActivity()).title = TitleTextBox;

        new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(TitleTextBox));

        SoftInputAssist softInputAssist = new SoftInputAssist(requireActivity());

        boolean NewNote = ((MainActivity) requireActivity()).newNote;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (NewNote) {

                CloseOrOpenBottomNoteBar(false);
                CloseOrOpenToolbarUndoRedo();

                InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(EverAdapter.GetActiveEditor(), 0);
            }

        }, 500);

        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        everDraw.setOnTouchListener((view, motionEvent) -> {

            int y = (int) motionEvent.getY();

            if (y >= FinalYHeight) {
                FinalYHeight = y;
            }

            if (y >= everDraw.getHeight() - 75) {

                new Handler(Looper.getMainLooper()).post(() -> {

                    TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                            .addTransition(new ChangeBounds()));

                    ViewGroup.LayoutParams params = everDraw.getLayoutParams();

                    params.height = FinalYHeight + 200;

                    everDraw.setLayoutParams(params);

                });

            }
            return false;
        });

        ((MainActivity) requireActivity()).seekBarDrawSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                everDraw.setStrokeWidth(i);

                if (((MainActivity) requireActivity()).DrawVisualizerIsShowing) {

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

        ImageButton blackDraw = ((MainActivity) requireActivity()).BlackDraw;
        ImageButton blueDraw = ((MainActivity) requireActivity()).BlueDraw;
        ImageButton purpleDraw = ((MainActivity) requireActivity()).PurpleDraw;
        ImageButton magentaDraw = ((MainActivity) requireActivity()).MagentaDraw;
        ImageButton orangeDraw = ((MainActivity) requireActivity()).OrangeDraw;
        ImageButton yellowDraw = ((MainActivity) requireActivity()).YellowDraw;
        ImageButton greenDraw = ((MainActivity) requireActivity()).GreenDraw;

        TitleTextBox.setOnFocusChangeListener((view, b) -> {

            if (b) {

                if (((MainActivity) requireActivity()).DeleteSave) {

                    ((MainActivity) requireActivity()).CloseOrOpenToolbarUndoRedo();
                }

                if (((MainActivity) requireActivity()).CloseFormatter) {

                    ((MainActivity) requireActivity()).CloseOrOpenFormatter();
                }

                if (((MainActivity) requireActivity()).bottomBarShowing) {

                    ((MainActivity) requireActivity()).CloseOrOpenBottomNoteBar(true);
                }

            }
        });

        blackDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Black"));

        blueDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Blue"));

        purpleDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Purple"));

        magentaDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Magenta"));

        orangeDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Orange"));

        yellowDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Yellow"));

        greenDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Green"));

        ((MainActivity) requireActivity()).GooglePhotos.setOnClickListener(view -> {
            requestPermissions(PERMISSIONS_STORAGE, 0);
            openImageChooser("GooglePhotos");
        });

        ((MainActivity) requireActivity()).Gallery.setOnClickListener(view -> {
            requestPermissions(PERMISSIONS_STORAGE, 0);
            openImageChooser("Gallery");
        });

        ((MainActivity) requireActivity()).Files.setOnClickListener(view -> {
            requestPermissions(PERMISSIONS_STORAGE, 0);
            openImageChooser("Files");
        });

        ((MainActivity) requireActivity()).Delete.setOnClickListener(view -> {


                if (((MainActivity) requireActivity()).DrawOn) {
                    everDraw.clearCanvas();
                    CloseOrOpenDraWOptions(0);
                    FinalYHeight = 0;
                } else {
                    new AlertDialog.Builder(requireActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this note?")
                            .setPositiveButton("Yes", (dialogInterface, i) -> {

                                      //TODO maybe fix it? // int id1 = ((MainActivity) requireActivity()).preferences.getInt("noteId", -1);

                                        ((MainActivity) requireActivity()).mDatabaseEver.deleteNote(actualNote.getId());

                                ((MainActivity)requireActivity()).notesModels.remove(actualNote.getActualPosition());

                                        onBackPressed(true);
                                    }
                            )
                            .setNegativeButton("No", null)
                            .show();
                }


        });




        ((MainActivity) requireActivity()).Save.setOnClickListener(view -> {


            if (drawFromRecycler) {
                Bitmap toResizeBitmap = EverAdapter.getSelectedDraw(EverAdapter.getSelectedDrawPosition()).getBitmap(Color.TRANSPARENT);
                if (toResizeBitmap.getHeight() >= EverAdapter.getFinalYHeight() + 75) {
                    newDrawedbitmap = Bitmap.createBitmap(toResizeBitmap, 0, 0, toResizeBitmap.getWidth(), EverAdapter.getFinalYHeight() + 75);
                } else {
                    newDrawedbitmap = Bitmap.createBitmap(toResizeBitmap, 0, 0, toResizeBitmap.getWidth(), EverAdapter.getFinalYHeight());
                }

                SaveBitmapFromDraw(true);
                drawFromRecycler = false;
            } else {

                String content = actualNote.getContent();

                ((MainActivity) requireActivity()).mDatabaseEver.editContent(String.valueOf(actualNote.getId()), content + "┼");

                SaveBitmapFromDraw(false);

            }
        });





        ((MainActivity) requireActivity()).Undo.setOnClickListener(view -> {
            if (((MainActivity) requireActivity()).DrawOn) {
                everDraw.undo();
            } else {
                EverAdapter.GetActiveEditor().undo();
            }
        });

        ((MainActivity) requireActivity()).Redo.setOnClickListener(view -> {
            if (((MainActivity) requireActivity()).DrawOn) {
                everDraw.redo();
            } else {
                EverAdapter.GetActiveEditor().redo();
            }
        });
       // TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
       //         .addTransition(new ChangeBounds()));

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ConstraintLayout constraintLayout = requireActivity().findViewById(R.id.note_creator_constraint);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(R.id.card_note_creator,ConstraintSet.BOTTOM,R.id.card_note_creator,ConstraintSet.BOTTOM,300);
            constraintSet.applyTo(constraintLayout);
        }, 450);

        startPostponedEnterTransition();


        // }).start();

    }

    private void onBackPressed(Boolean delete) {

        if (delete) {

           // CloseAllButtons();

            ((MainActivity) requireActivity()).mDatabaseEver.deleteNote(actualNote.getId());
            ((MainActivity) requireActivity()).notesModels.remove(actualNote.getActualPosition());

        } else {

            new Thread(() -> {
                actualNote.setTitle(TitleTextBox.getText().toString());
            }).start();

          //  CloseAllButtons();

        }
        ((MainActivity) requireActivity()).addedNote = false;
        ((MainActivity)requireActivity()).onBackPressed();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void TransformBitmapToFile(Bitmap bitmap, String fileType) {

        File directory = requireActivity().getDir("imageDir", Context.MODE_PRIVATE);

        drawPosition = items.size();

        System.out.println("Draw at position = " + drawPosition);

        File file = new File(directory, "Draw" + "RealID" + actualNote.getId() + "WithID" + drawPosition + fileType);
        System.out.println(file.toString());

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
                ((MainActivity) requireActivity()).mDatabaseEver.insertNoteBackgroundToDatabase(String.valueOf(actualNote.getId()), file.toString(), actualNote.getDrawLocation());
                actualNote.setDrawLocation(file.toString() + "┼" + actualNote.getDrawLocation());
                ((MainActivity) requireActivity()).notesModels.get(actualNote.getActualPosition()).setDrawLocation(actualNote.getDrawLocation());
                SetupNoteEditorRecycler(true, false, false, true, actualNote.getDrawLocation().split("┼").length);

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

                //TODO FIX IT    String[] list = ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID).split("┼");
            String[] list = actualNote.getDrawLocation().split("┼");
                    ArrayList<String> strings = new ArrayList<>();
                    for (String item : list) {
                        if (item.contains("DrawID" + drawPosition)) {
                            strings.add(file.toString() + "┼");
                        } else {
                            if (!item.equals("")) {
                                strings.add(item + "┼");
                            }
                        }
                    }
                    String editDraw = strings.toString().replaceAll("[()\\[\\]]", "").replaceAll(",", "").replaceAll(" ", "");
                    actualNote.setDrawLocation(editDraw);
        ((MainActivity)requireActivity()).notesModels.get(actualNote.getActualPosition()).setDrawLocation(editDraw);
                    ((MainActivity) requireActivity()).mDatabaseEver.editDraw(String.valueOf(actualNote.getId()), editDraw);

                    SetupNoteEditorRecycler(true, true, false, false, drawPosition);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        new Thread(() -> {

            if (((MainActivity) requireActivity()).CloseOpenedColorsHighlight) {

                if (resultCode != RESULT_OK) {

                    Uri gif = data.getData();

                    try {
                        ((MainActivity) requireActivity()).TransformUriToFile(gif, true, ".gif", actualNote.getImageURLS(), actualNote.getId(), actualNote.getActualPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    reloadImages();
                } }

            if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(Objects.requireNonNull(data));
                if (resultUri != null) {
                    try {
                        ((MainActivity) requireActivity()).TransformUriToFile(resultUri, true, ".jpg", actualNote.getImageURLS(), actualNote.getId(), actualNote.getActualPosition());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                reloadImages();
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(Objects.requireNonNull(data));
            } else {
            if (requestCode != RESULT_OK) {

                File directory = requireActivity().getDir("imageDir", Context.MODE_PRIVATE);

               // File file = new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + ".jpg");

                Uri uri = Uri.fromFile(new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + ".jpg"));

                if (data != null) {
                    UCrop.of(Objects.requireNonNull(data.getData()), uri)
                           // .withAspectRatio(16, 9)
                          //  .withMaxResultSize(cardView.getWidth(), 300)
                            .start(requireActivity(), this);
                }
            } }

            super.onActivityResult(requestCode, resultCode, data);
        }).start();
    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }

    @Override
    public void onImageClick(View view, int position) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onImageLongPress(View view, int position) {
        PopupWindowHelper popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(requireActivity()).inflate(R.layout.options_attached, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        popupWindowHelper.showAsDropDown(view, 350, -25);

       // String[] htmls =  ((MainActivity) requireActivity()).mDatabaseEver.getImageURLFromDatabaseWithID(realID).split("┼");
        String[] htmls = actualNote.getImageURLS().split("┼");

        String selectedImagePath = htmls[position];

        EverAdapter.SelectedDrawPosition = position;

        System.out.println(selectedImagePath + " and all is = " + Arrays.toString(htmls));

        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        ArrayList<String> images = new ArrayList<>();

        imageView.setOnClickListener(view1 -> {


            for (String html: htmls) {
                if (!html.equals(selectedImagePath)) {
                    images.add(html + "┼");
                } else {
                    File file = new File(selectedImagePath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }

            String[]  finalArray = images.toArray(new String[0]);
            String joined_arrayPath = String.join("", finalArray);

            ((MainActivity) requireActivity()).mDatabaseEver.editImages(String.valueOf(actualNote.getId()), joined_arrayPath);
            ((MainActivity) requireActivity()).notesModels.get(actualNote.getActualPosition()).setImageURLS(joined_arrayPath);

            view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter));

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                view.setVisibility(View.GONE);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                     imageAdapter.updateAdapter(joined_arrayPath, position);
                    if (joined_arrayPath.equals("")) {
                        reloadImages();
                    }
                }, 25);
            }, 75);
            popupWindowHelper.dismiss();
        });
    }

    @Override
    public void onItemClick(View view, int position) {

           EverAdapter.setFinalYHeight(0);
           savedbitmap = EverAdapter.getDrawBitmap(position).getBitmap();
           savedBitmapPath = EverAdapter.getImagePath(position);
           drawPosition = position;
           System.out.println("Selected position of draw is =" + drawPosition);
           drawFromRecycler = true;

        CloseOrOpenDraWOptionsFromRecycler();
           }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLongPress(View view, int position) {

        PopupWindowHelper popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(getActivity()).inflate(R.layout.options_attached, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        popupWindowHelper.showAsDropDown(view, 350, -25);

        String selectedDrawPath = items.get(position).getDrawLocation();

        int positionToRemove = position;

        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> stringsList = new ArrayList<>();

        imageView.setOnClickListener(view1 -> {
         // TODO  String[] htmls =  ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID).split("┼");
          //  String[] strings =  ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID).split("┼");
            String[] htmls = actualNote.getDrawLocation().split("┼");
            String[] strings = actualNote.getContent().split("┼");

            String toReplace = "";
            System.out.println("Strings is = " + Arrays.toString(strings));
            int positionToReplace = -1;

            for (int i = 0; i < strings.length ; i++) {
                if (i != position) {
                    stringsList.add(strings[i] + "┼");
                } else {
                        if (i != strings.length && i != 0) {
                            toReplace = strings[i];
                            positionToReplace = i;
                        } else if (i == strings.length){
                            toReplace = strings[i--];
                            positionToReplace = i--;
                        } else {
                            toReplace = strings[i];
                            positionToReplace = i;
                        }
                        stringsList.add("");
                    }
                }
            if (!toReplace.isEmpty()) {
                if (toReplace.equals("▓")) {
                    toReplace = "";
                }
                //TODO FIX THIS SHIT WHEN DELETING DRAWS, REMOVE <BR> IF CONTENT IS ▓, thx future pedro
                if (positionToReplace != (stringsList.size()-1) && positionToReplace < (stringsList.size()-1)) {
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
                    String s1 = stringsList.get(stringsList.size()+1);
                    stringsList.set(stringsList.size()+1, s1 + "<br>" + toReplace);
                } else {
                    //TODO \/ MAYBE WE NEED TO REMOVE THIS BECAUSE IT MAY NEVER BE CALLED
                    String s2 = stringsList.get(positionToReplace--);
                    stringsList.set(0, toReplace + "<br>" + s2);
                }
                //TODO ////////////////////// WE NEED TO HIDE EDITORS IN NOTE SCREEN ADAPTER AND WE NMEED TO REMOVE THE EXTRA "EDITOR"? OR IMAGE OF IT I GUESS AND FIX DELETE DRAW IF THERE IS NONE TEXT IN LAST EDITOR THX FUTURE PREDO
            }
            for (String html: htmls) {
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
            ((MainActivity) requireActivity()).mDatabaseEver.editContent(String.valueOf(actualNote.getId()), joined_arrayString);
            ((MainActivity) requireActivity()).notesModels.get(actualNote.getActualPosition()).setContent(joined_arrayString);
            ((MainActivity) requireActivity()).mDatabaseEver.editDraw(String.valueOf(actualNote.getId()), joined_arrayPath);
            ((MainActivity) requireActivity()).notesModels.get(actualNote.getActualPosition()).setDrawLocation(joined_arrayPath);
            actualNote.setDrawLocation(joined_arrayPath);
            actualNote.setContent(joined_arrayString);

           //view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter));

            SetupNoteEditorRecycler(true, true, true, false, positionToRemove);

            stringsList.clear();
            popupWindowHelper.dismiss();
        });


    }

    public void DrawColorClickedSwitcher(String color) {

        if (!openedFromButton) {
            everDraw = EverAdapter.getSelectedDraw(drawPosition);
        }


        switch (color) {

            case "Black":

                everDraw.setColor(GetColor(R.color.Black));

                CloseOrOpenDrawColors();


                break;

            case "Blue":

                everDraw.setColor(GetColor(R.color.SkyBlue));


                CloseOrOpenDrawColors();


                break;

            case "Purple":

                everDraw.setColor(GetColor(R.color.Pink));

                CloseOrOpenDrawColors();


                break;

            case "Magenta":

                everDraw.setColor(GetColor(R.color.Magenta));


                CloseOrOpenDrawColors();


                break;

            case "Orange":


                everDraw.setColor(GetColor(R.color.Orange));

                CloseOrOpenDrawColors();


                break;

            case "Yellow":


                everDraw.setColor(GetColor(R.color.YellowSun));

                CloseOrOpenDrawColors();


                break;

            case "Green":


                everDraw.setColor(GetColor(R.color.GrassGreen));

                CloseOrOpenDrawColors();


                break;

            default:


                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SetupNoteEditorRecycler(boolean clearAndAdd, boolean notifyChange, boolean removed, boolean add, int position) {

        items = new ArrayList<>();
        bitmaps.clear();
        toAdd = new ArrayList<>();

        i = 0;

      String[] html = actualNote.getDrawLocation().split("┼");
        Collections.reverse(Arrays.asList(html));

        bitmaps.addAll(Arrays.asList(html));

       // String[] strings = ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID).split("┼");

        List<String> contentsSplitted = new ArrayList<>(Arrays.asList(actualNote.getContent().split("┼")));

       String contents = actualNote.getContent();

        if (contentsSplitted.size() != bitmaps.size()) {
            for (i = contentsSplitted.size(); i < bitmaps.size(); i++) {
                    contentsSplitted.add(contentsSplitted.size(), "▓");
               System.out.println("added = " + i + " times.");
                }
        }
        if (bitmaps.size() != contentsSplitted.size()) {
            for (i = bitmaps.size(); i < contentsSplitted.size(); i++) {
                bitmaps.add(bitmaps.size(), "");
                System.out.println("added bit = " + i + " times.");
            }
        }
        int size = bitmaps.size() - 1;

        for (i = 0; i <= size ; i++) {
            items.add(new EverLinkedMap(contentsSplitted.get(i), IfContentIsBiggerReturnNothing(contentsSplitted.size(), bitmaps.size(), bitmaps, i)));
            toAdd.add(contentsSplitted.get(i));
        }

        if (items.size() == 0) {
            items.add(new EverLinkedMap("<br>", ""));
            toAdd.add("");
        }
         if (items.size() != 0 && !items.get(items.size()-1).getDrawLocation().equals("")) {
            items.add(new EverLinkedMap("<br>", ""));
            toAdd.add("");
        }



            if (clearAndAdd) {

                String[] arrayString = toAdd.toArray(new String[0]);

                everAdapter.UpdateAdapter(items, contents, arrayString, notifyChange, removed, add, position);

            } else {

                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

                textanddrawRecyclerView.setLayoutManager(staggeredGridLayoutManager);

                String[] arrayString = toAdd.toArray(new String[0]);

                everAdapter = new EverAdapter(requireActivity(), items, actualNote.getId(), actualNote.getActualPosition(), ((MainActivity) requireActivity()).mDatabaseEver, contents, arrayString, cardView, textanddrawRecyclerView);
                textanddrawRecyclerView.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
                 //   textanddrawRecyclerView.setAdapter(everAdapter);
                textanddrawRecyclerView.setAdapter(new AlphaInAnimationAdapter(everAdapter));
                ((MainActivity)requireActivity()).contentRecycler = textanddrawRecyclerView;
                ((MainActivity)requireActivity()).cardNoteCreator = cardView;
                textanddrawRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {


                    @Override
                    public boolean onPreDraw() {
                        textanddrawRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });

                    EverAdapter.setClickListener(this);

            }

      //  }).start();
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
            if (((MainActivity) requireActivity()).DrawOn) {

                if (!EverAdapter.getSelectedDraw(EverAdapter.getSelectedDrawPosition()).getMPaths().isEmpty()) {

                    mergeBitmaps(savedbitmap, newDrawedbitmap);

                    EverAdapter.getSelectedDraw(EverAdapter.getSelectedDrawPosition()).clearCanvas();

                    FinalYHeight = 0;

                    CloseOrOpenDraWOptionsFromRecycler();

                    Toast.makeText(requireActivity(), String.valueOf(drawPosition), Toast.LENGTH_SHORT).show();

                   // SetupNoteEditorRecycler(true, true, false, false, drawPosition);

                } else {

                    CloseOrOpenDraWOptionsFromRecycler();
                }
            } else {
                onBackPressed(false);
            }
        } else {

            if (((MainActivity) requireActivity()).DrawOn) {

                Bitmap bitmap = everDraw.getBitmap(Color.WHITE);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), FinalYHeight + 75);

                if (!everDraw.getMPaths().isEmpty()) {

                    TransformBitmapToFile(resizedBitmap, ".jpeg");

                    everDraw.clearCanvas();

                    FinalYHeight = 0;

                    CloseOrOpenDraWOptions(0);


                } else {

                    CloseOrOpenDraWOptions(0);

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

    private void CloseOrOpenDraWOptions(int height) {

        CloseOpenedButtons(true);

        if (height == 0) {

            ((MainActivity) requireActivity()).CloseOrOpenDraWOptions(0);

        } else {

            ((MainActivity) requireActivity()).CloseOrOpenDraWOptions(height);
        }
    }

    private void CloseOrOpenDraWOptionsFromRecycler() {

        CloseOpenedButtons(true);

        ((MainActivity) requireActivity()).CloseOrOpenDraWOptionsFromRecycler(scrollView, textanddrawRecyclerView);

    }

    private void CloseOrOpenDrawColors() {

        ((MainActivity) requireActivity()).CloseOrOpenDrawColors();
    }

    private void ShowDrawSizeVisualizer() {

        ((MainActivity) requireActivity()).ShowDrawSizeVisualizer();
    }

    private void ModifyDrawSizeVisualizer(int value) {

        ((MainActivity) requireActivity()).ModifyDrawSizeVisualizer(value);

    }

    private void CloseOrOpenToolbarUndoRedo() {

        ((MainActivity) requireActivity()).CloseOrOpenToolbarUndoRedo();
    }

    private void CloseOrOpenBottomNoteBar(boolean inTitle) {

        ((MainActivity) requireActivity()).CloseOrOpenBottomNoteBar(inTitle);
    }

    private void CloseAllButtons() {
        ((MainActivity) requireActivity()).CloseAllButtons();
    }

    private void CloseOpenedButtons(boolean isDraw) {
        if (((MainActivity) requireActivity()).CloseFormatter) {
            ((MainActivity) requireActivity()).CloseOrOpenFormatter();
        }
        if (((MainActivity) requireActivity()).CloseImporter) {
            ((MainActivity) requireActivity()).CloseOrOpenImporter();
        }
        if (((MainActivity) requireActivity()).CloseParagraph) {
            ((MainActivity) requireActivity()).CloseOrOpenParagraph();
        }
        if (!isDraw) {
            if ( ((MainActivity) requireActivity()).CloseOpenedDrawOptions) {
                ((MainActivity) requireActivity()).CloseOrOpenDraWOptions(0);
            }
        }
    }

    public void reloadImages() {
      //TODO  String imagesURLs = ((MainActivity) requireActivity()).mDatabaseEver.getImageURLFromDatabaseWithID(realID);
        String imagesURLs = actualNote.getImageURLS();
        StaggeredGridLayoutManager staggeredGridLayoutManagerImage = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (!imagesURLs.equals("")) {

                staggeredGridLayoutManagerImage.setSpanCount(2);

                recyclerViewImage.setLayoutManager(staggeredGridLayoutManagerImage);

                imageAdapter = new ImagesRecyclerGridAdapter(requireActivity(), imagesURLs, imagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);

                recyclerViewImage.setVisibility(View.VISIBLE);
                recyclerViewImage.setAdapter(imageAdapter);

                imageAdapter.setClickListener(this);

                ((MainActivity) requireActivity()).imageRecycler = recyclerViewImage;

            } else {
                recyclerViewImage.setVisibility(View.GONE);
                ((MainActivity) requireActivity()).imageRecycler = recyclerViewImage;
            }

        }, 200);
    }

    private void openImageChooser (String name){

        switch (name) {
            case "GooglePhotos":

                Intent intentGooglePhotos = new Intent();
                intentGooglePhotos.setAction(Intent.ACTION_PICK);
                intentGooglePhotos.setType("image/*");
                intentGooglePhotos.setPackage(GOOGLE_PHOTOS_PACKAGE_NAME);
                startActivityForResult(intentGooglePhotos, 101);

                break;

            case "Gallery":


                break;

            case "Files":

                Intent intentFiles = new Intent();
                intentFiles.setType("image/*");
                intentFiles.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentFiles, "Select Picture"), 101);

                break;


            default:
                break;
        }
    }

}
