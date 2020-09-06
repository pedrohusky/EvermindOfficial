package com.example.Evermind.ui.dashboard.ui.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;

import com.example.Evermind.EverBitmapMerger;
import com.example.Evermind.EverDraw;
import com.example.Evermind.EverFlowScrollView;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.SoftInputAssist;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.EverLinkedMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class NoteEditorFragmentJavaFragment extends Fragment implements EverAdapter.ItemClickListener {

    private NoteEditorFragmentMainViewModel mViewModel;
    private EverAdapter everAdapter;
    private RecyclerView textanddrawRecyclerView;
    private EverFlowScrollView scrollView;
    private boolean showNoteContents = false;
    private EditText TitleTextBox;
    private CardView cardView;
    private ImageView decoyImage;
    private List<EverLinkedMap> items = new ArrayList<>();
    private  ArrayList<String> toAdd ;
    private int i;
    private int realID;
    private EverDraw everDraw;
    private int FinalYHeight;
    private Bitmap savedbitmap;
    private Bitmap newDrawedbitmap;
    private Bitmap finalBitmap;
    private int drawPosition = 0;
    private boolean drawFromRecycler = false;
    private EverBitmapMerger everBitmapMerger;
    private String savedBitmapPath;
    List<String> bitmaps = new ArrayList<>();
    public static NoteEditorFragmentJavaFragment newInstance() {
        return new NoteEditorFragmentJavaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_note_creator, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SoftInputAssist softInputAssist = new SoftInputAssist(requireActivity());

        realID = ((MainActivity) requireActivity()).GetIDFromSharedPreferences();

        everDraw = requireActivity().findViewById(R.id.EverDraw);
        scrollView = requireActivity().findViewById(R.id.scrollview);
        TitleTextBox = requireActivity().findViewById(R.id.TitleTextBox);
        textanddrawRecyclerView = requireActivity().findViewById(R.id.TextAndDrawRecyclerView);
        cardView = requireActivity().findViewById(R.id.card_note_creator);

        String title = ((MainActivity) requireActivity()).mDatabaseEver.getTitlesFromDatabaseWithID(realID);

        TitleTextBox.setText(title);

        SetupNoteEditorRecycler(false, false);

        boolean NewNote = ((MainActivity) requireActivity()).GetNewNoteFromSharedPreferences();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (NewNote) {

                CloseOrOpenBottomNoteBar(false);
                CloseOrOpenToolbarUndoRedo();

                InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(EverAdapter.GetActiveEditor(), 0);
            }

        }, 500);

        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        RecyclerView recyclerViewImage = requireActivity().findViewById(R.id.ImagesRecycler);

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

                                        int id1 = ((MainActivity) requireActivity()).preferences.getInt("noteId", -1);

                                        ((MainActivity) requireActivity()).mDatabaseEver.deleteNote(id1);

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

                String content = ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID);

                ((MainActivity) requireActivity()).mDatabaseEver.editContent(String.valueOf(realID), content + "┼");

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


        //////////////////////////////////////////// HANDLE IMAGES \/


        String imagesURLs = ((MainActivity) requireActivity()).mDatabaseEver.getImageURLFromDatabaseWithID(realID);

        StaggeredGridLayoutManager staggeredGridLayoutManagerImage = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //ImagesURLs.removeAll(Collections.singletonList(""));

        if (imagesURLs.length() > 0) {

            staggeredGridLayoutManagerImage.setSpanCount(2);

            //   new Handler(Looper.getMainLooper()).post(() -> {


            recyclerViewImage.setLayoutManager(staggeredGridLayoutManagerImage);

            ImagesRecyclerGridAdapter adapter = new ImagesRecyclerGridAdapter(requireActivity(), imagesURLs, ((MainActivity) requireActivity()).preferences.getInt("position", -1), imagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);

            recyclerViewImage.setVisibility(View.VISIBLE);
            recyclerViewImage.setAdapter(adapter);

            // });
        } else {
            recyclerViewImage.setVisibility(View.GONE);
        }

        // }).start();

    }


    //////////////////////////////////////////// HANDLE IMAGES /\ /\ /\ /\ /\

    private void CloseOrOpenDraWOptions(int height) {

        if (height == 0) {

            ((MainActivity) requireActivity()).CloseOrOpenDraWOptions(0);

        } else {

            ((MainActivity) requireActivity()).CloseOrOpenDraWOptions(height);
        }
    }

    private void CloseOrOpenDraWOptionsFromRecycler() {

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

    private void onBackPressed(Boolean delete) {

        if (delete) {

            ((MainActivity) requireActivity()).note_bottom_bar.startAnimation(((MainActivity) requireActivity()).bottom_nav_anim_reverse);

            ((MainActivity) requireActivity()).ApplyChangesToSharedPreferences("athome", false, "", true, true, false, 0);
            ((MainActivity) requireActivity()).ApplyChangesToSharedPreferences("content", true, "", false, false, false, 0);

            CloseAllButtons();

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_note_to_nav_home);

        } else {

            new Thread(() -> {
                ((MainActivity) requireActivity()).mDatabaseEver.editTitle(Integer.toString(realID), TitleTextBox.getText().toString());

                if (TitleTextBox.getText().length() <= 1 && EverAdapter.GetContents().equals("") && items.size() <= 1) {
                    ((MainActivity) requireActivity()).mDatabaseEver.deleteNote(realID);
                    System.out.println("Note with id = " + realID + " deleted. <-- called from OnBackPress in NoteEditorFragmentJava, thx future pedro");
                }

                //Hide nav view \/ \/ \/

                ((MainActivity) requireActivity()).ApplyChangesToSharedPreferences("athome", false, "", true, true, false, 0);
                ((MainActivity) requireActivity()).ApplyChangesToSharedPreferences("content", true, "", false, false, false, 0);


            }).start();

            ((MainActivity) requireActivity()).note_bottom_bar.startAnimation(((MainActivity) requireActivity()).bottom_nav_anim_reverse);

            CloseAllButtons();

            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_note_to_nav_home);
        }
    }

    private void CloseOrOpenToolbarUndoRedo() {

        ((MainActivity) requireActivity()).CloseOrOpenToolbarUndoRedo();
    }

    private void CloseOrOpenBottomNoteBar(boolean inTitle) {

        ((MainActivity) requireActivity()).CloseOrOpenBottomNoteBar(inTitle);
    }

    private void TransformBitmapToFile(Bitmap bitmap, String fileType) {

        File directory = requireActivity().getDir("imageDir", Context.MODE_PRIVATE);

        drawPosition = items.size();

        System.out.println("Draw at position = " + drawPosition);

        File file = new File(directory, "Draw" + "RealID" + realID + "WithID" + drawPosition + fileType);
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
                ((MainActivity) requireActivity()).mDatabaseEver.insertNoteBackgroundToDatabase(String.valueOf(((MainActivity) requireActivity()).preferences.getInt("noteId", -1)), file.toString(), ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID));
            }
    }

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

                    String[] list = ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID).split("┼");
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
                    ((MainActivity) requireActivity()).mDatabaseEver.editDraw(String.valueOf(realID), strings.toString().replaceAll("[()\\[\\]]", "").replaceAll(",", "").replaceAll(" ", ""));
                    SetupNoteEditorRecycler(true, true);
    }

    private void CloseAllButtons() {
        ((MainActivity) requireActivity()).CloseAllButtons();
    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }

    @Override
    public void onItemClick(View view, int position) {


        //System.out.println(view.toString());
         //   BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView)  ((LinearLayout) view).getChildAt(position)).getDrawable();

           EverAdapter.setFinalYHeight(0);
           savedbitmap = EverAdapter.getDrawBitmap(position).getBitmap();
           savedBitmapPath = EverAdapter.getImagePath(position);
           drawPosition = position;
           System.out.println("Selected position of draw is =" + drawPosition);
           drawFromRecycler = true;

        //    System.out.println("Saved bitmap from database to saved bitmaap successfull = " + savedbitmap.toString());

         //   Bitmap drawableBitmap = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
           // CloseOrOpenDraWOptions(drawableBitmap.getHeight());
            CloseOrOpenDraWOptionsFromRecycler();
           }


    @Override
    public void onClick(View view) {
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

        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> stringsList = new ArrayList<>();

        imageView.setOnClickListener(view1 -> {
            String[] htmls =  ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID).split("┼");
            String[] strings =  ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID).split("┼");
            String toReplace = "";
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
                    }
                }
            if (!toReplace.isEmpty()) { System.out.println("LENGHT = " +  (stringsList.size()));
                if (positionToReplace != (stringsList.size()) && positionToReplace < (stringsList.size())) {
                    String s = stringsList.get(positionToReplace);
                    stringsList.set(positionToReplace, toReplace + "<br>" + s);
                } else if (positionToReplace == stringsList.size()) {
                    String s1 = stringsList.get(stringsList.size()-1);
                    stringsList.set(stringsList.size()-1, s1 + "<br>" + toReplace);
                } else {
                    String s = stringsList.get(positionToReplace--);
                    stringsList.set(positionToReplace--, toReplace + "<br>" + s);
                }
            }
            for (String html: htmls) {
                if (!html.equals(selectedDrawPath)) {
                    arrayList.add(html + "┼");
                } else {
                    File file = new File(selectedDrawPath);
                    if (file.exists()) {
                        file.delete();
                        System.out.println("File deleted at pathj = " + selectedDrawPath);
                    }
                }
            }

            String[]  finalArray = arrayList.toArray(new String[0]);
            String[] finalString = stringsList.toArray(new String[0]);
            String joined_arrayString = String.join("", finalString);
            String joined_arrayPath = String.join("", finalArray);

            System.out.println("Finl = " + joined_arrayString);

            ((MainActivity) requireActivity()).mDatabaseEver.editContent(String.valueOf(realID), joined_arrayString);
            ((MainActivity) requireActivity()).mDatabaseEver.editDraw(String.valueOf(realID), joined_arrayPath);
            System.out.println("final paths is = " + joined_arrayPath);

            view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter));

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                view.setVisibility(View.GONE);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                   SetupNoteEditorRecycler(false, false);
                }, 100);
            }, 150);
            popupWindowHelper.dismiss();
        });


    }

    public void DrawColorClickedSwitcher(String color) {

        everDraw = EverAdapter.getSelectedDraw(drawPosition);

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

    private void SetupNoteEditorRecycler(boolean clearAndAdd, boolean notifyChange) {

        items = new ArrayList<>();
       // List<String> bitmaps = new ArrayList<>();
        bitmaps.clear();
        toAdd = new ArrayList<>();

        i = 0;

        String[] html = ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID).split("┼");

        bitmaps.addAll(Arrays.asList(html));

        String[] strings = ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID).split("┼");

        List<String> contentsSplitted = new ArrayList<>(Arrays.asList(strings));

        System.out.println(contentsSplitted.toString());

        String contents =  ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID);

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

        System.out.println("Bitmaps size = " + bitmaps.size());
        System.out.println("Content size = " + contentsSplitted.size());
        System.out.println("Size is =" + size);

        for (i = 0; i <= size ; i++) {
            items.add(new EverLinkedMap(contentsSplitted.get(i), IfContentIsBiggerReturnNothing(contentsSplitted.size(), bitmaps.size(), bitmaps, i)));
            toAdd.add(contentsSplitted.get(i));
        }
        if (items.size() == 0) {
            items.add(new EverLinkedMap("<br>", ""));
            //toAdd.add("");
        }
        if (items.size() != 0 && !items.get(items.size()-1).getDrawLocation().equals("")) {
            items.add(new EverLinkedMap("<br>", ""));
        }



            if (clearAndAdd) {

                String[] arrayString = toAdd.toArray(new String[0]);

                everAdapter.UpdateAdapter(items, contents, arrayString, notifyChange);

            } else {

                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

                textanddrawRecyclerView.setLayoutManager(staggeredGridLayoutManager);

                String[] arrayString = toAdd.toArray(new String[0]);

                everAdapter = new EverAdapter(requireActivity(), items, realID, ((MainActivity) requireActivity()).mDatabaseEver, contents, arrayString, cardView, textanddrawRecyclerView);

                    textanddrawRecyclerView.setAdapter(everAdapter);

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

    private void SaveBitmapFromDraw(boolean fromRecycler) {
        if (fromRecycler) {
            if (((MainActivity) requireActivity()).DrawOn) {

                if (!EverAdapter.getSelectedDraw(EverAdapter.getSelectedDrawPosition()).getMPaths().isEmpty()) {

                    mergeBitmaps(savedbitmap, newDrawedbitmap);

                    EverAdapter.getSelectedDraw(EverAdapter.getSelectedDrawPosition()).clearCanvas();

                    FinalYHeight = 0;

                    CloseOrOpenDraWOptionsFromRecycler();

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

                    SetupNoteEditorRecycler(true, false);

                } else {

                    CloseOrOpenDraWOptions(0);

                    FinalYHeight = 0;

                }

            } else {

                onBackPressed(false);


            }
        }
    }

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
}
//TODO MAYBE WE POSSIBILY CAN MERGE THE BITMAPS OVER ONE ANOTER AND WE NEED TO MAKE THE LAST EDITOR DISAPPEAR IF WE REMOVE IMAGES AND WE NEED TO MAKE THE TEXT IF THERE IS ONE MERGE WITH THE FIRST EDITOR THX FUTURE PEDRO