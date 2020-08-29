package com.example.Evermind.ui.dashboard.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.Gravity;
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
import android.widget.Toast;

import com.example.Evermind.EverDraw;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.SoftInputAssist;
import com.example.Evermind.recycler_models.Content;
import com.example.Evermind.recycler_models.Draw;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.Item;
import java.io.File;
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
    private boolean showNoteContents = false;
    private EditText TitleTextBox;
    private CardView cardView;
    private List<Item> items;
    private  ArrayList<String> toAdd;
    private int i;
    private int realID;
    private EverDraw everDraw;
    private int FinalYHeight;
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
        ScrollView scrollView = requireActivity().findViewById(R.id.scrollview);
        TitleTextBox = requireActivity().findViewById(R.id.TitleTextBox);
        textanddrawRecyclerView = requireActivity().findViewById(R.id.TextAndDrawRecyclerView);
        cardView = requireActivity().findViewById(R.id.card_note_creator);

        String title = ((MainActivity) requireActivity()).mDatabaseEver.getTitlesFromDatabaseWithID(realID);

        TitleTextBox.setText(title);

        SetupNoteEditorRecycler(false);

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
            if (((MainActivity) requireActivity()).DrawOn) {

                Bitmap bitmap = everDraw.getBitmap();
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), FinalYHeight + 75);

                if (!everDraw.getMPaths().isEmpty()) {

                    try {
                        TransformBitmapToFile(resizedBitmap, true, ".jpeg");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    everDraw.clearCanvas();

                    CloseOrOpenDraWOptions(0);

                    FinalYHeight = 0;

                    SetupNoteEditorRecycler(true);


                } else {

                    CloseOrOpenDraWOptions(0);

                    FinalYHeight = 0;

                }

            } else {
                onBackPressed(false);
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

        if (((MainActivity) requireActivity()).CloseOpenedDrawOptions) {

            ((MainActivity) requireActivity()).CloseOrOpenDraWOptions(0);

        } else {

            ((MainActivity) requireActivity()).CloseOrOpenDraWOptions(height);
        }
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

    private void TransformBitmapToFile(Bitmap bitmap, boolean addToDatabase, String fileType) throws
            IOException {

        File directory = requireActivity().getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + fileType);

        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos;

            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();


            if (addToDatabase) {
                ((MainActivity) requireActivity()).mDatabaseEver.insertNoteBackgroundToDatabase(String.valueOf(((MainActivity) requireActivity()).preferences.getInt("noteId", -1)), file.toString(), ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID));

            }
        }
    }

    private void CloseAllButtons() {
        ((MainActivity) requireActivity()).CloseAllButtons();
    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }

    @Override
    public void onItemClick(View view, int position) {

        if (view != null) {

            BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView) view).getDrawable();
            Bitmap drawableBitmap = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            CloseOrOpenDraWOptions(drawableBitmap.getHeight());
            everDraw.setBitmap(drawableBitmap);



            // everDraw.setBackground(((ImageView) view).getDrawable());
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onLongPress(View view, int position) {

        PopupWindowHelper popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(getActivity()).inflate(R.layout.options_attached, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        popupWindowHelper.showAsDropDown(view, 350, -25);

        String selectedDrawPath = ((Draw) items.get(position).getObject()).getFileLocation();

        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        ArrayList<String> arrayList = new ArrayList<>();

        imageView.setOnClickListener(view1 -> {
            String[] htmls =  ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID).split("┼");

            for (String html: htmls) {
                if (!html.equals(selectedDrawPath)) {
                    arrayList.add(html + "┼");
                }
            }
            String[]  finalArray = arrayList.toArray(new String[0]);
             ((MainActivity) requireActivity()).mDatabaseEver.insertNoteBackgroundToDatabase(String.valueOf(realID), Arrays.toString(finalArray).replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", ""), "┼");

           view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter));

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                view.setVisibility(View.GONE);
            }, 150);
            popupWindowHelper.dismiss();
        });


    }

    public void DrawColorClickedSwitcher(String color) {

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

    private void SetupNoteEditorRecycler(boolean clearAndAdd) {

        items = new ArrayList<>();
        List<String> bitmaps = new ArrayList<>();
        toAdd = new ArrayList<>();
        i = 0;

        String[] html = ((MainActivity) requireActivity()).mDatabaseEver.getBackgroundFromDatabaseWithID(realID).split("┼");

        bitmaps.addAll(Arrays.asList(html));

        String[] strings = ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID).split("┼");

        if (html.length == 0 && strings.length == 0) {
            Content content = new Content("<br>");
            toAdd.add("<br>");
            items.add(new Item(0, content));
            System.out.println("Added with 0 in both contents.");
        }

        if (strings.length == 0 && html.length >= 1) {
            Draw draw1 = new Draw(bitmaps.get(i));
            items.add(new Item(1, draw1));
            i++;
            System.out.println("Added draw");
            if (i >= strings.length) {
                Content content = new Content("");
                toAdd.add("");
                items.add(new Item(0, content));
                System.out.println("Added with i >= strings.lengh");
            }
        } else {

            for (String text : strings) {
                Content content1 = new Content(text);
                toAdd.add(text);
                items.add(new Item(0, content1));
                System.out.println("Added contents." + "contents is " + text);

                if (i <= bitmaps.size() - 1) {

                    Draw draw1 = new Draw(bitmaps.get(i));
                    items.add(new Item(1, draw1));
                    i++;
                    System.out.println("Added draws.");
                    Content content3 = new Content("");
                    toAdd.add("");
                    items.add(new Item(0, content3));

                    if (i >= strings.length && bitmaps.size() - 1 < strings.length) {
                        Content content = new Content("");
                        toAdd.add("");
                        items.add(new Item(0, content));
                        System.out.println("Added with i >= strrings.lengh && bitmap.size - 1 <= strings.lengh AND ADDED BR.");

                    } else {
                        if (i >= strings.length) {
                            for (String string : bitmaps) {
                                if (i <= bitmaps.size() - 1) {
                                    Draw draw2 = new Draw(bitmaps.get(i));
                                    items.add(new Item(1, draw2));
                                    Content content = new Content("");
                                    toAdd.add("");
                                    items.add(new Item(0, content));
                                    i++;
                                    System.out.println("Added with bitmaps in a row. Added blank to contents.");
                                }
                            }
                        }
                    }
                }
            }
        }

        if (clearAndAdd) {

            String[] arrayString = toAdd.toArray(new String[0]);

            everAdapter.UpdateAdapter(items, ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID), arrayString);

        } else {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

            textanddrawRecyclerView.setLayoutManager(staggeredGridLayoutManager);

            String[] arrayString = toAdd.toArray(new String[0]);

            everAdapter = new EverAdapter(requireActivity(), items, realID, ((MainActivity) requireActivity()).mDatabaseEver, ((MainActivity) requireActivity()).mDatabaseEver.getContentsFromDatabaseWithID(realID), arrayString);

            textanddrawRecyclerView.setAdapter(everAdapter);
            EverAdapter.setClickListener(this);

        }
    }
}

// TODO FIX NOT SHOWING FIRST AND LAST ITEMS
