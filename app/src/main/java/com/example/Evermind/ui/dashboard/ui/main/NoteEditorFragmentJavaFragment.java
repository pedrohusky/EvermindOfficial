package com.example.Evermind.ui.dashboard.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.Evermind.EverDataBase;
import com.example.Evermind.EverDraw;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.R;
import com.example.Evermind.SoftInputAssist;
import com.example.Evermind.recycler_models.Content;
import com.example.Evermind.recycler_models.Draw;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.Item;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muehlemann.giphy.GiphyLibrary;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class NoteEditorFragmentJavaFragment extends Fragment implements EverAdapter.ItemClickListener {

    private NoteEditorFragmentMainViewModel mViewModel;
    public static ImagesRecyclerGridAdapter adapter;
    public static EverAdapter everAdapter;
    private EverDataBase everDataBase;
    private RecyclerView textanddrawRecyclerView;
    // private EvermindEditor evermindEditor;
    private static String ImagesURLs;
    private Boolean CloseFormatter = false;
    private Boolean CloseParagraph = false;
    private Boolean CloseImporter = false;
    private Boolean CloseOpenedColors = false;
    private Boolean CloseOpenedDrawOptions = false;
    private Boolean CloseOpenedDrawColors = false;
    private Boolean CloseOpenedDrawSize = false;
    private Boolean CloseOpenedColorsHighlight = false;
    public Boolean DeleteSave = false;
    private EditText TitleTextBox;
    private ImageButton Undo;
    private ImageButton Redo;
    private ImageButton Delete;
    private ImageButton Save;
    private ImageButton ChangeColor;
    private ImageButton Bold;
    private ImageButton Italic;
    private ImageButton Underline;
    private ImageButton Striketrough;
    private ImageButton HighlightText;
    private ImageButton Black;
    private ImageButton Blue;
    private ImageButton Purple;
    private ImageButton Magenta;
    private ImageButton Orange;
    private ImageButton Yellow;
    private ImageButton Green;
    private ImageButton BlackDraw;
    private ImageButton BlueDraw;
    private ImageButton PurpleDraw;
    private ImageButton MagentaDraw;
    private ImageButton OrangeDraw;
    private ImageButton YellowDraw;
    private ImageButton GreenDraw;
    private ImageButton DrawChangeColor;
    private ImageButton DrawChangeSize;
    private CardView DrawOptions;
    private SeekBar seekBarDrawSize;
    private ImageButton ClearHighlight;
    private ImageButton BlackHighlight;
    private ImageButton BlueHighlight;
    private ImageButton PurpleHighlight;
    private ImageButton MagentaHighlight;
    private ImageButton OrangeHighlight;
    private ImageButton YellowHighlight;
    private ImageButton GreenHighlight;
    private ImageButton Increase;
    private ImageButton Decrease;
    private ImageButton Left;
    private ImageButton Center;
    private ImageButton Right;
    private BottomNavigationView note_bottom_bar;
    private Animation bottom_nav_anim;
    private Animation bottom_nav_anim_reverse;
    private ScrollView scrollView;
    private HorizontalScrollView scrollView1;
    private HorizontalScrollView scrollView2;
    private HorizontalScrollView scrollView3;
    private HorizontalScrollView scrollView4;
    private ImageButton GooglePhotos;
    private ImageButton Gallery;
    private ImageButton Files;
    private CardView cardView;
    private RecyclerView recyclerViewImage;
    private EverDraw everDraw;
    public Boolean DrawVisualizerIsShowing = false;
    public Boolean DrawOn = false;
    private Animation fadein;
    private Animation fadeout;
    private CardView size_visualizer;
    private ImageView ImageSizeView;
    private CardView format_selector;
    private CardView paragraph_selector;
    private CardView importer_selector;
    private ImageButton Bullets;
    private ImageButton Numbers;
    private ImageView spacing;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private boolean tomanocu = false;
    private int FinalYHeight;
    private GiphyLibrary giphyLibrary;
    private static final String GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos";
    private int size = 4;
    public List<Item> items;
    public List<String> bitmaps;
    private int i;

    public static NoteEditorFragmentJavaFragment newInstance() {
        return new NoteEditorFragmentJavaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        everDataBase = new EverDataBase(requireActivity());

        preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        editor = preferences.edit();

        return inflater.inflate(R.layout.fragment_note_creator, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //////////////////////////////////////// INICIAL VARIALBES \/

        //evermindEditor = requireActivity().findViewById(R.id.ToSaveNoteText);

        SoftInputAssist softInputAssist = new SoftInputAssist(requireActivity());

        //    evermindEditor.setEditorFontSize(22);
        //    evermindEditor.setBackgroundColor(GetColor(R.color.Transparent));
        //   evermindEditor.setPadding(15, 15, 15, 15);
        // evermindEditor.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        TitleTextBox = requireActivity().findViewById(R.id.TitleTextBox);
        textanddrawRecyclerView = requireActivity().findViewById(R.id.TextAndDrawRecyclerView);
        Undo = requireActivity().findViewById(R.id.Undo);
        Redo = requireActivity().findViewById(R.id.Redo);
        Delete = requireActivity().findViewById(R.id.Delete);
        Save = requireActivity().findViewById(R.id.Save);

        String title = everDataBase.getTitlesFromDatabaseWithID(GetIDFromSharedPreferences());

        TitleTextBox.setText(title);

        SetupNoteEditorRecycler(false);

        boolean NewNote = GetNewNoteFromSharedPreferences();

        // evermindEditor.setHtml(content);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (NewNote) {
                // evermindEditor.focusEditor();

                Undo.setVisibility(View.VISIBLE);
                Redo.setVisibility(View.VISIBLE);
                Delete.setVisibility(View.VISIBLE);
                Save.setVisibility(View.VISIBLE);

                InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(EverAdapter.GetActiveEditor(), 0);
            }

        }, 500);

        //////////////////////////////////////// INICIAL VARIALBES /\
        //new Thread(() -> {

        note_bottom_bar = requireActivity().findViewById(R.id.note_bottom_bar);
        bottom_nav_anim = AnimationUtils.loadAnimation(requireActivity(), R.anim.translate_up_anim);
        bottom_nav_anim_reverse = AnimationUtils.loadAnimation(requireActivity(), R.anim.translate_up_anim_reverse);
        scrollView = requireActivity().findViewById(R.id.scrollview);
        scrollView1 = requireActivity().findViewById(R.id.scroll_formatter);
        scrollView2 = requireActivity().findViewById(R.id.scroll_paragraph);
        scrollView3 = requireActivity().findViewById(R.id.scroll_import);
        scrollView4 = requireActivity().findViewById(R.id.scroll_draw);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView1);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView2);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView3);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView4);
        GooglePhotos = requireActivity().findViewById(R.id.GooglePhotos);
        Gallery = requireActivity().findViewById(R.id.Gallery);
        Files = requireActivity().findViewById(R.id.Files);
        ChangeColor = requireActivity().findViewById(R.id.ChangeColor);
        DrawChangeColor = requireActivity().findViewById(R.id.DrawChangeColor);
        DrawChangeSize = requireActivity().findViewById(R.id.DrawChangeSize);
        size_visualizer = requireActivity().findViewById(R.id.draw_sizeVisualizerCardView);
        ImageSizeView = requireActivity().findViewById(R.id.draw_size_visualizer);
        DrawOptions = requireActivity().findViewById(R.id.draw_options);
        seekBarDrawSize = requireActivity().findViewById(R.id.draw_size_seekbar);
        Italic = requireActivity().findViewById(R.id.Italic);
        Bold = requireActivity().findViewById(R.id.Bold);
        Underline = requireActivity().findViewById(R.id.Underline);
        Striketrough = requireActivity().findViewById(R.id.Striketrough);
        HighlightText = requireActivity().findViewById(R.id.HighlightText);
        Black = requireActivity().findViewById(R.id.black);
        Blue = requireActivity().findViewById(R.id.blue);
        Purple = requireActivity().findViewById(R.id.purple);
        Magenta = requireActivity().findViewById(R.id.magenta);
        Orange = requireActivity().findViewById(R.id.orange);
        Yellow = requireActivity().findViewById(R.id.yellow);
        Green = requireActivity().findViewById(R.id.green);
        BlackDraw = requireActivity().findViewById(R.id.Drawblack);
        BlueDraw = requireActivity().findViewById(R.id.Drawblue);
        PurpleDraw = requireActivity().findViewById(R.id.Drawpurple);
        MagentaDraw = requireActivity().findViewById(R.id.Drawmagenta);
        OrangeDraw = requireActivity().findViewById(R.id.Draworange);
        YellowDraw = requireActivity().findViewById(R.id.Drawyellow);
        GreenDraw = requireActivity().findViewById(R.id.Drawgreen);
        ClearHighlight = requireActivity().findViewById(R.id.clearhighlight);
        BlackHighlight = requireActivity().findViewById(R.id.blackhighlight);
        BlueHighlight = requireActivity().findViewById(R.id.bluehighlight);
        PurpleHighlight = requireActivity().findViewById(R.id.purplehighlight);
        MagentaHighlight = requireActivity().findViewById(R.id.magentahighlight);
        OrangeHighlight = requireActivity().findViewById(R.id.orangehighlight);
        YellowHighlight = requireActivity().findViewById(R.id.yellowhighlight);
        GreenHighlight = requireActivity().findViewById(R.id.greenhighlight);
        cardView = requireActivity().findViewById(R.id.card_note_creator);
        recyclerViewImage = requireActivity().findViewById(R.id.ImagesRecycler);
        everDraw = requireActivity().findViewById(R.id.EverDraw);
        fadein = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in_formatter);
        fadeout = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter);
        DrawOptions = requireActivity().findViewById(R.id.draw_options);
        size_visualizer = requireActivity().findViewById(R.id.draw_sizeVisualizerCardView);
        ImageSizeView = requireActivity().findViewById(R.id.draw_size_visualizer);
        format_selector = requireActivity().findViewById(R.id.format_selector);
        importer_selector = requireActivity().findViewById(R.id.import_options);
        paragraph_selector = requireActivity().findViewById(R.id.format_paragraph);
        Bullets = requireActivity().findViewById(R.id.Bullets);
        Numbers = requireActivity().findViewById(R.id.Numbers);
        spacing = requireActivity().findViewById(R.id.paragraph_spacing);
        Increase = requireActivity().findViewById(R.id.IncreaseSize);
        Decrease = requireActivity().findViewById(R.id.DecreaseSize);
        Left = requireActivity().findViewById(R.id.AlignLeft);
        Right = requireActivity().findViewById(R.id.AlignRight);
        Center = requireActivity().findViewById(R.id.AlignCenter);


        /////////////////////////////////////////////////////////////////////////// MainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);


        Black.setOnClickListener(view -> ColorClickedSwitcher("Black", false));

        Blue.setOnClickListener(view -> ColorClickedSwitcher("Blue", false));

        Purple.setOnClickListener(view -> ColorClickedSwitcher("Purple", false));

        Magenta.setOnClickListener(view -> ColorClickedSwitcher("Magenta", false));

        Orange.setOnClickListener(view -> ColorClickedSwitcher("Orange", false));

        Yellow.setOnClickListener(view -> ColorClickedSwitcher("Yellow", false));

        Green.setOnClickListener(view -> ColorClickedSwitcher("Green", false));

        BlackDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Black"));

        BlueDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Blue"));

        PurpleDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Purple"));

        MagentaDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Magenta"));

        OrangeDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Orange"));

        YellowDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Yellow"));

        GreenDraw.setOnClickListener(view -> DrawColorClickedSwitcher("Green"));

        ClearHighlight.setOnClickListener(view -> ColorClickedSwitcher("Clear", true));

        BlackHighlight.setOnClickListener(view -> ColorClickedSwitcher("Black", true));

        BlueHighlight.setOnClickListener(view -> ColorClickedSwitcher("Blue", true));

        PurpleHighlight.setOnClickListener(view -> ColorClickedSwitcher("Purple", true));

        MagentaHighlight.setOnClickListener(view -> ColorClickedSwitcher("Magenta", true));

        OrangeHighlight.setOnClickListener(view -> ColorClickedSwitcher("Orange", true));

        YellowHighlight.setOnClickListener(view -> ColorClickedSwitcher("Yellow", true));

        GreenHighlight.setOnClickListener(view -> ColorClickedSwitcher("Green", true));


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

                    params.height = everDraw.getHeight() + 200;

                    everDraw.setLayoutParams(params);

                });

            }
            return false;
        });

        KeyboardVisibilityEvent.setEventListener(
                requireActivity(),
                isOpen -> {

                    if (isOpen) {

                    /*    new Handler(Looper.getMainLooper()).post(() -> {

                            TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                                    .addTransition(new ChangeBounds()));

                            // evermindEditor.setEditorHeight(250);

                            ViewGroup.LayoutParams params = cardView.getLayoutParams();

                            params.height = 1100;

                            cardView.setLayoutParams(params);
                        });
                  */  } else {

                        //      if (DrawOn) {

                        //         new Handler(Looper.getMainLooper()).post(() -> {

                        //             TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                        //                    .addTransition(new ChangeBounds()));

                        // evermindEditor.setEditorHeight(250);

                        //           ViewGroup.LayoutParams params = cardView.getLayoutParams();

                        //           params.height = 2000;

                        //          cardView.setLayoutParams(params); });

                        //    } else {

                        new Handler(Looper.getMainLooper()).post(() -> {

                            TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                                    .addTransition(new ChangeBounds()));

                            ViewGroup.LayoutParams params = cardView.getLayoutParams();

                            params.height = WRAP_CONTENT;

                            cardView.setLayoutParams(params);
                        });
                    }// }
                });


        GooglePhotos.setOnClickListener(view -> {
            requestPermissions(PERMISSIONS_STORAGE, 0);
            openImageChooser("GooglePhotos");
        });

        Gallery.setOnClickListener(view -> {
            requestPermissions(PERMISSIONS_STORAGE, 0);
            openImageChooser("Gallery");
        });

        Files.setOnClickListener(view -> {
            requestPermissions(PERMISSIONS_STORAGE, 0);
            openImageChooser("Files");
        });

        ChangeColor.setOnClickListener(view -> {
            if (CloseOpenedColors) {

                OpenOrCloseColors(false);


            } else {

                OpenOrCloseColors(false);

            }
        });

        DrawChangeColor.setOnClickListener(view -> OpenOrCloseDrawColors());

        DrawChangeSize.setOnClickListener(view -> OpenOrCloseDrawSize());

        seekBarDrawSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                everDraw.setStrokeWidth(i);

                if (DrawVisualizerIsShowing) {

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

        HighlightText.setOnClickListener(view -> {
            if (CloseOpenedColorsHighlight) {

                OpenOrCloseColors(true);


            } else {

                OpenOrCloseColors(true);

            }
        });


        requireActivity().findViewById(R.id.IncreaseSize).setOnClickListener(v -> {


            if (size < 7) {

                size++;
                EverAdapter.GetActiveEditor().setFontSize(size);
            }

        });

        requireActivity().findViewById(R.id.DecreaseSize).setOnClickListener(v -> {


            if (size > 3) {

                size--;
                EverAdapter.GetActiveEditor().setFontSize(size);
            }
        });


        note_bottom_bar.setOnNavigationItemSelectedListener(item -> {
            int id_nav = item.getItemId();


            switch (id_nav) {
                case R.id.nav_formatText:

                    //TODO TO USE LATER THIS CODE TO SWITCH ANIM \/

                    CloseOrOpenFormatter();

                    //TODO TO USE LATER THIS CODE TO SWITCH ANIM /\

                    break;

                case R.id.nav_paragraph:

                    CloseOrOpenParagraph();

                    break;

                case R.id.nav_checkbox:

                    CloseOrOpenImporter();

                    break;

                case R.id.nav_bullets:

                    tomanocu = true;

                    requestPermissions(PERMISSIONS_STORAGE, 0);

                    // giphyLibrary.start(requireActivity(), this, API_KEY);


                    break;

                case R.id.nav_draw:

                    OpenOrCloseDrawOptions();

                    InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                    keyboard.hideSoftInputFromWindow(requireView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                default:
                    return true;
            }
            return true;
        });

        Delete.setOnClickListener(view -> {
            if (DrawOn) {
                everDraw.clearCanvas();
                OpenOrCloseDrawOptions();
            } else {
                new AlertDialog.Builder(requireActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {

                                    int id1 = preferences.getInt("noteId", -1);

                                    everDataBase.deleteNote(id1);

                                    onBackPressed(true);
                                }
                        )
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Save.setOnClickListener(view -> {
            if (DrawOn) {

                Bitmap bitmap = everDraw.getBitmap();
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), FinalYHeight + 75);

                try {
                    TransformBitmapToFile(resizedBitmap, true, ".jpeg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                everDraw.clearCanvas();

                OpenOrCloseDrawOptions();

                SetupNoteEditorRecycler(true);
            } else {
                onBackPressed(false);
            }
        });

        Undo.setOnClickListener(view -> {
            if (DrawOn) {
                everDraw.undo();
            } else {
                EverAdapter.GetActiveEditor().undo();
            }
        });

        Redo.setOnClickListener(view -> {
            if (DrawOn) {
                everDraw.redo();
            } else {
                EverAdapter.GetActiveEditor().redo();
            }
        });

        requireActivity().findViewById(R.id.Bold).setOnClickListener(v -> EverAdapter.GetActiveEditor().setBold());

        requireActivity().findViewById(R.id.Italic).setOnClickListener(v -> EverAdapter.GetActiveEditor().setItalic());

        requireActivity().findViewById(R.id.Striketrough).setOnClickListener(v -> EverAdapter.GetActiveEditor().setStrikeThrough());

        requireActivity().findViewById(R.id.Underline).setOnClickListener(v -> EverAdapter.GetActiveEditor().setUnderline());

        //     getActivity().findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.setHeading(1);
        //          }
        //      });

        //      getActivity().findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
        //        @Override public void onClick(View v) {
        //             mEditor.setHeading(2);
        //         }
        //      });

        ///      getActivity().findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.setHeading(3);
        //            }
        //     });

        //     getActivity().findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
        //         @Override public void onClick(View v) {
        //             mEditor.setHeading(4);
        //        }
        //      });

        //     getActivity().findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
        //         @Override public void onClick(View v) {
        //             mEditor.setHeading(5);
        //        }
        //    });

        //      getActivity().findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.setHeading(6);
        //          }
        //     });


        requireActivity().findViewById(R.id.AlignLeft).setOnClickListener(v -> EverAdapter.GetActiveEditor().setAlignLeft());
        requireActivity().findViewById(R.id.AlignCenter).setOnClickListener(v -> EverAdapter.GetActiveEditor().setAlignCenter());
        requireActivity().findViewById(R.id.AlignRight).setOnClickListener(v -> EverAdapter.GetActiveEditor().setAlignRight());


        //      getActivity().findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
        //                      "dachshund");
        //          }
        //     });

        //     getActivity().findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
        ////         @Override public void onClick(View v) {
        //             mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
        //         }


        mViewModel = ViewModelProviders.of(this).get(NoteEditorFragmentMainViewModel.class);


    /*    EverAdapter.GetActiveEditor().setOnFocusChangeListener((view, b) -> {

            if (b) {

                new Handler(Looper.getMainLooper()).postDelayed(() -> {


                    note_bottom_bar.setVisibility(View.VISIBLE);
                    note_bottom_bar.startAnimation(bottom_nav_anim);

                    DeleteSave = GetDeleteNSaveFromSharedPreferences();

                    if (DeleteSave) {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            Undo.setVisibility(View.VISIBLE);
                            Redo.setVisibility(View.VISIBLE);


                            InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                            if (keyboard.isActive()) {

                            } else {
                                keyboard.showSoftInput(EverAdapter.GetActiveEditor(), 0);
                            }

                        }, 200);

                    } else {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            Undo.setVisibility(View.VISIBLE);
                            Redo.setVisibility(View.VISIBLE);
                            Delete.setVisibility(View.VISIBLE);
                            Save.setVisibility(View.VISIBLE);

                        }, 200);

                    }

                    ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, true, false, 0);
                    ApplyChangesToSharedPreferences("UndoRedo", false, "", true, true, false, 0);

                }, 450);

            } else {

                new Handler(Looper.getMainLooper()).post(() -> {

                    //      TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                    //              .addTransition(new ChangeBounds()));

                    //    ViewGroup.LayoutParams params = cardView.getLayoutParams();

                    //    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
                    //     cardView.setLayoutParams(params);

                    if (DrawOn) {

                    } else {

                        ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, false, false, 0);
                        ApplyChangesToSharedPreferences("UndoRedo", false, "", true, false, false, 0);

                        Undo.setVisibility(View.GONE);
                        Redo.setVisibility(View.GONE);

                        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                });
            }
        });
*/

        //evermindEditor.setOnTextChangeListener(text -> new Thread(() -> {

        //     String transformToHexHTML = replaceRGBColorsWithHex(evermindEditor.getHtml());

        //     System.out.println(EverAdapter.GetActiveEditor().toString());

        // focusOnView(scrollView, mEditor);
//
        //       new Handler(Looper.getMainLooper()).postDelayed(() -> {

        //         everDataBase.editContent(Integer.toString(GetIDFromSharedPreferences()), transformToHexHTML);


        //     }, 750);

        //  }).start());

        TitleTextBox.setOnFocusChangeListener((view, b) -> {

            if (b) {

                new Handler(Looper.getMainLooper()).post(() -> {

                    //InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                    // keyboard.showSoftInput(TitleTextBox, 0);

                    DeleteSave = GetDeleteNSaveFromSharedPreferences();

                    if (DeleteSave) {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            Undo.setVisibility(View.GONE);
                            Redo.setVisibility(View.GONE);

                        }, 200);


                    } else {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            Delete.setVisibility(View.VISIBLE);
                            Save.setVisibility(View.VISIBLE);

                        }, 200);

                    }

                    ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, true, false, 0);


                    if (CloseFormatter) {
                        CloseOrOpenFormatter();
                    }

                    note_bottom_bar.startAnimation(bottom_nav_anim_reverse);

                    // new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    // note_bottom_bar.setVisibility(View.GONE);

                    // }, 200);

                });

            } else {

                // InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                // keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                new Handler(Looper.getMainLooper()).post(() -> {

                    // Delete.setVisibility(View.GONE);
                    // Save.setVisibility(View.GONE);

                    ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, false, false, 0);

                });
            }


        });


        //////////////////////////////////////////// HANDLE IMAGES \/


        ImagesURLs = everDataBase.getImageURLFromDatabaseWithID(GetIDFromSharedPreferences());

        StaggeredGridLayoutManager staggeredGridLayoutManagerImage = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //ImagesURLs.removeAll(Collections.singletonList(""));

        if (ImagesURLs.length() > 0) {

            staggeredGridLayoutManagerImage.setSpanCount(2);

            //   new Handler(Looper.getMainLooper()).post(() -> {


            recyclerViewImage.setLayoutManager(staggeredGridLayoutManagerImage);

            adapter = new ImagesRecyclerGridAdapter(this.getActivity(), ImagesURLs, preferences.getInt("position", -1), ImagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);

            recyclerViewImage.setVisibility(View.VISIBLE);
            recyclerViewImage.setAdapter(adapter);

            // });
        } else {
            recyclerViewImage.setVisibility(View.GONE);
        }

        // }).start();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new Thread(() -> {

            if (tomanocu) {

                if (resultCode != RESULT_OK) {

                    Uri gif = data.getData();

                    try {
                        TransformUriToFile(gif, true, ".gif");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (requestCode != RESULT_OK) {

                Uri imageUri = data.getData();

                try {
                    TransformUriToFile(imageUri, true, ".jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    //////////////////////////////////////////// HANDLE IMAGES /\ /\ /\ /\ /\
    void openImageChooser(String name) {

        switch (name) {
            case "GooglePhotos":

                Intent intentGooglePhotos = new Intent();
                intentGooglePhotos.setAction(Intent.ACTION_PICK);
                intentGooglePhotos.setType("image/*");
                intentGooglePhotos.setPackage(GOOGLE_PHOTOS_PACKAGE_NAME);
                startActivityForResult(intentGooglePhotos, 101);


               ResizeCardViewToWrapContent();

                break;

            case "Gallery":


                break;

            case "Files":

                Intent intentFiles = new Intent();
                intentFiles.setType("image/*");
                intentFiles.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentFiles, "Select Picture"), 101);

                ResizeCardViewToWrapContent();

                break;


            default:
                break;
        }
    }


    private void OpenOrCloseColors(Boolean highlight) {

            if (CloseOpenedColors) {

                if (highlight) {

                        BlackHighlight.setVisibility(View.GONE);
                        BlueHighlight.setVisibility(View.GONE);
                        PurpleHighlight.setVisibility(View.GONE);
                        MagentaHighlight.setVisibility(View.GONE);
                        OrangeHighlight.setVisibility(View.GONE);
                        YellowHighlight.setVisibility(View.GONE);
                        GreenHighlight.setVisibility(View.GONE);
                        ClearHighlight.setVisibility(View.GONE);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            ChangeColor.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Italic.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Underline.setVisibility(View.VISIBLE);
                            Striketrough.setVisibility(View.VISIBLE);
                            HighlightText.setVisibility(View.VISIBLE);

                            Increase.setVisibility(View.VISIBLE);
                            Decrease.setVisibility(View.VISIBLE);
                            Left.setVisibility(View.VISIBLE);
                            Center.setVisibility(View.VISIBLE);
                            Right.setVisibility(View.VISIBLE);
                        }, 200);

                        CloseOpenedColorsHighlight = false;

                } else {

                        Black.setVisibility(View.GONE);
                        Blue.setVisibility(View.GONE);
                        Purple.setVisibility(View.GONE);
                        Magenta.setVisibility(View.GONE);
                        Orange.setVisibility(View.GONE);
                        Yellow.setVisibility(View.GONE);
                        Green.setVisibility(View.GONE);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {


                            //  ChangeColor.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Italic.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Underline.setVisibility(View.VISIBLE);
                            Striketrough.setVisibility(View.VISIBLE);
                            HighlightText.setVisibility(View.VISIBLE);

                            Increase.setVisibility(View.VISIBLE);
                            Decrease.setVisibility(View.VISIBLE);
                            Left.setVisibility(View.VISIBLE);
                            Center.setVisibility(View.VISIBLE);
                            Right.setVisibility(View.VISIBLE);

                        }, 200);

                        CloseOpenedColors = false;

                }

            } else {
                if (highlight) {

                        ChangeColor.setVisibility(View.GONE);
                        Bold.setVisibility(View.GONE);
                        Italic.setVisibility(View.GONE);
                        Bold.setVisibility(View.GONE);
                        Underline.setVisibility(View.GONE);
                        Striketrough.setVisibility(View.GONE);
                        HighlightText.setVisibility(View.GONE);
                        ClearHighlight.setVisibility(View.VISIBLE);

                        Increase.setVisibility(View.GONE);
                        Decrease.setVisibility(View.GONE);
                        Left.setVisibility(View.GONE);
                        Center.setVisibility(View.GONE);
                        Right.setVisibility(View.GONE);


                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            // ChangeColor.setVisibility(View.GONE);

                            BlackHighlight.setVisibility(View.VISIBLE);
                            BlueHighlight.setVisibility(View.VISIBLE);
                            PurpleHighlight.setVisibility(View.VISIBLE);
                            MagentaHighlight.setVisibility(View.VISIBLE);
                            OrangeHighlight.setVisibility(View.VISIBLE);
                            YellowHighlight.setVisibility(View.VISIBLE);
                            GreenHighlight.setVisibility(View.VISIBLE);
                        }, 250);

                        CloseOpenedColorsHighlight = true;

                } else {

                        BlackHighlight.setVisibility(View.GONE);
                        BlueHighlight.setVisibility(View.GONE);
                        PurpleHighlight.setVisibility(View.GONE);
                        MagentaHighlight.setVisibility(View.GONE);
                        OrangeHighlight.setVisibility(View.GONE);
                        YellowHighlight.setVisibility(View.GONE);
                        GreenHighlight.setVisibility(View.GONE);

                        Bold.setVisibility(View.GONE);
                        Italic.setVisibility(View.GONE);
                        Bold.setVisibility(View.GONE);
                        Underline.setVisibility(View.GONE);
                        Striketrough.setVisibility(View.GONE);
                        HighlightText.setVisibility(View.GONE);

                        Increase.setVisibility(View.GONE);
                        Decrease.setVisibility(View.GONE);
                        Left.setVisibility(View.GONE);
                        Center.setVisibility(View.GONE);
                        Right.setVisibility(View.GONE);


                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            // ChangeColor.setVisibility(View.GONE);

                            Black.setVisibility(View.VISIBLE);
                            Blue.setVisibility(View.VISIBLE);
                            Purple.setVisibility(View.VISIBLE);
                            Magenta.setVisibility(View.VISIBLE);
                            Orange.setVisibility(View.VISIBLE);
                            Yellow.setVisibility(View.VISIBLE);
                            Green.setVisibility(View.VISIBLE);
                        }, 250);

                        CloseOpenedColors = true;
                }
            }
    }

    private void OpenOrCloseDrawOptions() {

            if (CloseOpenedDrawOptions) {

                    ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, false, false, 0);
                    ApplyChangesToSharedPreferences("UndoRedo", false, "", true, false, false, 0);

                    Undo.setVisibility(View.GONE);
                    Redo.setVisibility(View.GONE);

                    DrawOptions.startAnimation(fadeout);
                    DrawChangeColor.setVisibility(View.GONE);
                    DrawChangeSize.setVisibility(View.GONE);
                    TitleTextBox.setVisibility(View.VISIBLE);
                    textanddrawRecyclerView.setVisibility(View.VISIBLE);
                    //  evermindEditor.setVisibility(View.VISIBLE);


                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        everDraw.setVisibility(View.GONE);
                        recyclerViewImage.setVisibility(View.VISIBLE);

                        DrawOptions.setVisibility(View.GONE);

                        DrawOn = false;

                        editor.putBoolean("DrawOn", false);
                        editor.apply();

                    }, 100);

                    CloseOpenedDrawOptions = false;

            } else {

                    everDraw.setVisibility(View.VISIBLE);
                    recyclerViewImage.setVisibility(View.GONE);
                    TitleTextBox.setVisibility(View.GONE);
                    textanddrawRecyclerView.setVisibility(View.GONE);

                    DrawOptions.setVisibility(View.VISIBLE);

                    ResizeEverDrawToPrepareNoteToDraw();

                    DrawOptions.startAnimation(fadein);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        DrawChangeColor.setVisibility(View.VISIBLE);
                        DrawChangeSize.setVisibility(View.VISIBLE);
                        Undo.setVisibility(View.VISIBLE);
                        Redo.setVisibility(View.VISIBLE);
                        Save.setVisibility(View.VISIBLE);
                        Delete.setVisibility(View.VISIBLE);

                        DrawOn = true;

                        editor.putBoolean("DrawOn", true);
                        editor.apply();

                    }, 100);

                    CloseOpenedDrawOptions = true;
            }
    }

    private void OpenOrCloseDrawSize() {

            if (CloseOpenedDrawSize) {

                    DrawChangeColor.setVisibility(View.VISIBLE);
                    DrawChangeSize.setVisibility(View.VISIBLE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        // ChangeColor.setVisibility(View.GONE);

                        seekBarDrawSize.setVisibility(View.GONE);

                    }, 100);

                    CloseOpenedDrawSize = false;

            } else {

                    DrawChangeColor.setVisibility(View.GONE);
                    DrawChangeSize.setVisibility(View.VISIBLE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> seekBarDrawSize.setVisibility(View.VISIBLE), 100);

                    CloseOpenedDrawSize = true;
            }
    }

    private void OpenOrCloseDrawColors() {

            if (CloseOpenedDrawColors) {

                    BlackDraw.setVisibility(View.GONE);
                    BlueDraw.setVisibility(View.GONE);
                    PurpleDraw.setVisibility(View.GONE);
                    MagentaDraw.setVisibility(View.GONE);
                    OrangeDraw.setVisibility(View.GONE);
                    YellowDraw.setVisibility(View.GONE);
                    GreenDraw.setVisibility(View.GONE);


                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        // ChangeColor.setVisibility(View.GONE);

                        DrawChangeColor.setVisibility(View.VISIBLE);
                        DrawChangeSize.setVisibility(View.VISIBLE);


                    }, 150);

                    CloseOpenedDrawColors = false;

            } else {

                    DrawChangeColor.setVisibility(View.GONE);
                    DrawChangeSize.setVisibility(View.GONE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        // ChangeColor.setVisibility(View.GONE);

                        BlackDraw.setVisibility(View.VISIBLE);
                        BlueDraw.setVisibility(View.VISIBLE);
                        PurpleDraw.setVisibility(View.VISIBLE);
                        MagentaDraw.setVisibility(View.VISIBLE);
                        OrangeDraw.setVisibility(View.VISIBLE);
                        YellowDraw.setVisibility(View.VISIBLE);
                        GreenDraw.setVisibility(View.VISIBLE);


                    }, 150);


                    CloseOpenedDrawColors = true;
            }
    }

    private void ShowDrawSizeVisualizer() {

        DrawVisualizerIsShowing = true;
        // ChangeColor.setVisibility(View.GONE);

        size_visualizer.setVisibility(View.VISIBLE);
        size_visualizer.startAnimation(fadein);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // ChangeColor.setVisibility(View.GONE);

                ImageSizeView.setVisibility(View.VISIBLE);

            }, 100);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                DrawVisualizerIsShowing = false;

                size_visualizer.startAnimation(fadeout);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    ImageSizeView.setVisibility(View.GONE);
                    size_visualizer.setVisibility(View.GONE);


                }, 100);


            }, 1250);
    }

    private void ModifyDrawSizeVisualizer(int value) {

        ImageSizeView.setScaleX(value);
        ImageSizeView.setScaleY(value);

    }

    public void ColorClickedSwitcher(String color, boolean highlight) {

        if (highlight) {

            switch (color) {

                case "Clear":

                    EverAdapter.GetActiveEditor().setTextBackgroundColor(Color.WHITE);

                    OpenOrCloseColors(true);

                    ClearHighlight.setVisibility(View.GONE);

                    break;

                case "Black":

                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.Black));

                    OpenOrCloseColors(true);


                    break;

                case "Blue":


                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.SkyBlueHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Purple":


                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.PinkHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Magenta":


                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.MagentaHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Orange":


                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.OrangeHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Yellow":

                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.YellowSunHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Green":

                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.GrassGreen));

                    OpenOrCloseColors(true);


                    break;

                default:


                    break;
            }
        } else {
            switch (color) {

                case "Black":

                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.Black)));

                    OpenOrCloseColors(false);


                    break;

                case "Blue":

                    EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.SkyBlue));

                    //  Blue = !Blue;


                    OpenOrCloseColors(false);


                    break;

                case "Purple":

                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.Pink)));

                    //  Purple = !Purple;

                    OpenOrCloseColors(false);


                    break;

                case "Magenta":

                    EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.Magenta));

                    //  Magenta = !Magenta;


                    OpenOrCloseColors(false);


                    break;

                case "Orange":


                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.Orange)));

                    //  Orange = !Orange;

                    OpenOrCloseColors(false);


                    break;

                case "Yellow":


                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.YellowSun)));

                    // Yellow = !Yellow;

                    OpenOrCloseColors(false);


                    break;

                case "Green":


                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.GrassGreen)));

                    // Green = !Green;

                    OpenOrCloseColors(false);


                    break;

                default:


                    break;
            }
        }
    }

    public void DrawColorClickedSwitcher(String color) {

        switch (color) {

            case "Black":

                everDraw.setColor(GetColor(R.color.Black));

                OpenOrCloseDrawColors();


                break;

            case "Blue":

                everDraw.setColor(GetColor(R.color.SkyBlue));


                OpenOrCloseDrawColors();


                break;

            case "Purple":

                everDraw.setColor(GetColor(R.color.Pink));

                OpenOrCloseDrawColors();


                break;

            case "Magenta":

                everDraw.setColor(GetColor(R.color.Magenta));


                OpenOrCloseDrawColors();


                break;

            case "Orange":


                everDraw.setColor(GetColor(R.color.Orange));

                OpenOrCloseDrawColors();


                break;

            case "Yellow":


                everDraw.setColor(GetColor(R.color.YellowSun));

                OpenOrCloseDrawColors();


                break;

            case "Green":


                everDraw.setColor(GetColor(R.color.GrassGreen));

                OpenOrCloseDrawColors();


                break;

            default:


                break;
        }
    }

    private static String replaceRGBColorsWithHex(String html) {
        // using regular expression to find all occurences of rgb(a,b,c) using
        // capturing groups to get separate numbers.
        Pattern p = Pattern.compile("(rgb\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\))");
        Matcher m = p.matcher(html);

        while (m.find()) {
            // get whole matched rgb(a,b,c) text
            String foundRGBColor = m.group(1);
            //  System.out.println("Found: " + foundRGBColor);

            // get r value
            String rString = m.group(2);
            // get g value
            String gString = m.group(3);
            // get b value
            String bString = m.group(4);

            //  System.out.println(" separated r value: " + rString);
            //   System.out.println(" separated g value: " + gString);
            //   System.out.println(" separated b value: " + bString);

            // converting numbers from string to int
            int rInt = Integer.parseInt(rString);
            int gInt = Integer.parseInt(gString);
            int bInt = Integer.parseInt(bString);

            // converting int to hex value
            String rHex = Integer.toHexString(rInt);
            String gHex = Integer.toHexString(gInt);
            String bHex = Integer.toHexString(bInt);

            // add leading zero if number is small to avoid converting
            // rgb(1,2,3) to rgb(#123)
            String rHexFormatted = String.format("%2s", rHex).replace(" ", "0");
            String gHexFormatted = String.format("%2s", gHex).replace(" ", "0");
            String bHexFormatted = String.format("%2s", bHex).replace(" ", "0");

            //   System.out.println(" converted " + rString + " to hex: " + rHexFormatted);
            //  System.out.println(" converted " + gString + " to hex: " + gHexFormatted);
            //   System.out.println(" converted " + bString + " to hex:" + bHexFormatted);

            // concatenate new color in hex
            String hexColorString = "#" + rHexFormatted + gHexFormatted + bHexFormatted;

            if (foundRGBColor != null) {
                html = html.replaceAll(Pattern.quote(foundRGBColor), hexColorString);
            }
        }
        return html;
    }

    private void onBackPressed(Boolean delete) {

            if (delete) {

                //Hide nav view \/ \/ \/

                note_bottom_bar.startAnimation(bottom_nav_anim_reverse);


                //Hide nav view /\ /\ /\
                ApplyChangesToSharedPreferences("athome", false, "", true, true, false, 0);
                ApplyChangesToSharedPreferences("content", true, "", false, false, false, 0);

                CloseAllButtons();

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_note_to_nav_home);

            } else {

                new Thread(() -> {
                    int id = GetIDFromSharedPreferences();
                    everDataBase.editTitle(Integer.toString(id), TitleTextBox.getText().toString());

                    if (TitleTextBox.getText().length() <= 1 && EverAdapter.GetContents().equals("") && items.size() <= 1) {
                        everDataBase.deleteNote(id);
                        System.out.println("Note with id = " + id + " deleted. <-- called from OnBackPress in NoteEditorFragmentJava, thx future pedro");
                    }

                    //Hide nav view \/ \/ \/

                    ApplyChangesToSharedPreferences("athome", false, "", true, true, false, 0);
                    ApplyChangesToSharedPreferences("content", true, "", false, false, false, 0);


                }).start();

                note_bottom_bar.startAnimation(bottom_nav_anim_reverse);

                CloseAllButtons();

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_note_to_nav_home);
            }
    }

    private void CloseOrOpenFormatter() {

        if (CloseFormatter) {

            ChangeColor.setVisibility(View.GONE);
            Bold.setVisibility(View.GONE);
            Italic.setVisibility(View.GONE);
            Bold.setVisibility(View.GONE);
            Underline.setVisibility(View.GONE);
            Striketrough.setVisibility(View.GONE);
            HighlightText.setVisibility(View.GONE);

            Black.setVisibility(View.GONE);
            Blue.setVisibility(View.GONE);
            Purple.setVisibility(View.GONE);
            Magenta.setVisibility(View.GONE);
            Orange.setVisibility(View.GONE);
            Yellow.setVisibility(View.GONE);
            Green.setVisibility(View.GONE);

            BlackHighlight.setVisibility(View.GONE);
            BlueHighlight.setVisibility(View.GONE);
            PurpleHighlight.setVisibility(View.GONE);
            MagentaHighlight.setVisibility(View.GONE);
            OrangeHighlight.setVisibility(View.GONE);
            YellowHighlight.setVisibility(View.GONE);
            GreenHighlight.setVisibility(View.GONE);

            Increase.setVisibility(View.GONE);
            Decrease.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                format_selector.startAnimation(fadeout);

                format_selector.setVisibility(View.GONE);

            }, 150);

            CloseFormatter = false;


        } else {

            format_selector.setVisibility(View.VISIBLE);

            format_selector.startAnimation(fadein);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                Increase.setVisibility(View.VISIBLE);
                Decrease.setVisibility(View.VISIBLE);
                ChangeColor.setVisibility(View.VISIBLE);
                Italic.setVisibility(View.VISIBLE);
                Bold.setVisibility(View.VISIBLE);
                Underline.setVisibility(View.VISIBLE);
                Striketrough.setVisibility(View.VISIBLE);
                HighlightText.setVisibility(View.VISIBLE);

            }, 150);

            CloseFormatter = true;

        }
    }

    private void CloseOrOpenParagraph() {

        if (CloseParagraph) {

            spacing.setVisibility(View.GONE);
            Bullets.setVisibility(View.GONE);
            Numbers.setVisibility(View.GONE);
            Left.setVisibility(View.GONE);
            Center.setVisibility(View.GONE);
            Right.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                paragraph_selector.startAnimation(fadeout);

                paragraph_selector.setVisibility(View.GONE);

            }, 150);

            CloseParagraph = false;


        } else {

            paragraph_selector.setVisibility(View.VISIBLE);

            paragraph_selector.startAnimation(fadein);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                spacing.setVisibility(View.VISIBLE);
                Bullets.setVisibility(View.VISIBLE);
                Numbers.setVisibility(View.VISIBLE);
                Left.setVisibility(View.VISIBLE);
                Center.setVisibility(View.VISIBLE);
                Right.setVisibility(View.VISIBLE);

            }, 150);

            CloseParagraph = true;

        }
    }

    private void CloseOrOpenImporter() {

        if (CloseImporter) {

            GooglePhotos.setVisibility(View.GONE);
            Gallery.setVisibility(View.GONE);
            Files.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                importer_selector.startAnimation(fadeout);

                //  importer.setVisibility(View.GONE);

            }, 150);

            CloseImporter = false;


        } else {

            importer_selector.setVisibility(View.VISIBLE);

            importer_selector.startAnimation(fadein);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                GooglePhotos.setVisibility(View.VISIBLE);
                Gallery.setVisibility(View.VISIBLE);
                Files.setVisibility(View.VISIBLE);

            }, 150);

            CloseImporter = true;

        }
    }

    private void CloseEditorButtonsSaveDelete() {

        Delete.setVisibility(View.GONE);
        Save.setVisibility(View.GONE);

    }

    private void focusOnView(ScrollView scroll, View view) {
        new Handler(Looper.getMainLooper()).post(() -> {
            //FOR HORIZONTAL SCROLL VIEW
            ////      int vLeft = view.getLeft();
            //    int vRight = view.getRight();
            //     int sWidth = scroll.getWidth();
            //     scroll.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);


            int vTop = view.getTop();
            int vBottom = view.getBottom();
            int sHeight = scroll.getBottom();
            //scroll.smoothScrollTo(((vTop + vBottom - sHeight) / 2), 0);
            scroll.smoothScrollTo(0, vTop + vBottom - sHeight / 2);
        });
    }

    private int GetIDFromSharedPreferences() {
        return preferences.getInt("noteId", -1);
    }

    private Boolean GetNewNoteFromSharedPreferences() {
        return preferences.getBoolean("newnote", false);
    }

    private Boolean GetDeleteNSaveFromSharedPreferences() {
        return preferences.getBoolean("DeleteNSave", false);
    }

    private void ApplyChangesToSharedPreferences(String name, boolean string, String text, boolean bolean, boolean value, boolean integer, int id) {

        if (string) {
            editor.putString(name, text);
            editor.apply();
        }
        if (integer) {
            editor.putInt(name, id);
            editor.apply();
        }
        if (bolean) {
            editor.putBoolean(name, value);
            editor.apply();
        }

    }

    private void TransformUriToFile(Uri uri, boolean addToDatabase, String fileType) throws IOException {

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.requireActivity().getContentResolver(), uri);

        File directory = this.requireActivity().getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + fileType);

        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos;

            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();


            if (addToDatabase) {

                everDataBase.insertImageToDatabase(String.valueOf(preferences.getInt("noteId", -1)), file.toString(), ImagesURLs.toString().replaceAll("[\\[\\](){}]", ""));

                ReloadImagesRecycler();
            }
        }
    }

    private void TransformBitmapToFile(Bitmap bitmap, boolean addToDatabase, String fileType) throws IOException {

        File directory = this.requireActivity().getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + fileType);

        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos;

            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();


            if (addToDatabase) {
                everDataBase.insertNoteBackgroundToDatabase(String.valueOf(preferences.getInt("noteId", -1)), file.toString(), everDataBase.getBackgroundFromDatabaseWithID(GetIDFromSharedPreferences()));

            }
        }
    }

    private void ReloadImagesRecycler() {

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            ImagesURLs = everDataBase.getImageURLFromDatabaseWithID(preferences.getInt("noteId", -1));
            adapter = new ImagesRecyclerGridAdapter(this.requireActivity(), ImagesURLs, preferences.getInt("position", -1), ImagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);
            recyclerViewImage.removeAllViews();
            recyclerViewImage.setAdapter(adapter);

        }, 400);
    }

    private void CloseAllButtons() {
        if (CloseFormatter) {
            CloseOrOpenFormatter();
        }
        if (CloseImporter) {
            CloseOrOpenImporter();
        }
        if (CloseParagraph) {
            CloseOrOpenParagraph();
        }
        if (CloseImporter) {
            CloseOrOpenImporter();
        }
        if (CloseOpenedDrawOptions) {
            OpenOrCloseDrawOptions();
        }
        if (DeleteSave) {
            CloseEditorButtonsSaveDelete();
        }
    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(requireActivity(), ((ImageView) view).getDrawable().toString(), Toast.LENGTH_SHORT).show();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView) view).getDrawable();
        Bitmap drawableBitmap = bitmapDrawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        OpenOrCloseDrawOptions();
        everDraw.setBitmap(drawableBitmap);
       // everDraw.setBackground(((ImageView) view).getDrawable());
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onLongPress(View view, int position) {

    }

    private void SetupNoteEditorRecycler(boolean clearAndAdd) {

        items = new ArrayList<>();
        bitmaps = new ArrayList<>();
        i = 0;

        String[] html = everDataBase.getBackgroundFromDatabaseWithID(GetIDFromSharedPreferences()).replaceAll("[\\[\\](){}]", "").trim().split("┼");

        bitmaps.addAll(Arrays.asList(html));

        String[] strings = everDataBase.getContentsFromDatabaseWithID(GetIDFromSharedPreferences()).replaceAll("[\\[\\](){}]", "").trim().split("┼");

        if (html.length == 0 && strings.length == 0) {
            Content content = new Content("");
            items.add(new Item(0, content));
        }

        if (strings.length == 0 && html.length >= 1) {
            Draw draw1 = new Draw(bitmaps.get(i));
            items.add(new Item(1, draw1));
            i++;
            if (i >= strings.length) {
                Content content = new Content("");
                items.add(new Item(0, content));
            }
        } else {

            for (String text : strings) {
                Content content1 = new Content(text);
                items.add(new Item(0, content1));

                if (i <= bitmaps.size() - 1) {
                    Draw draw1 = new Draw(bitmaps.get(i));
                    items.add(new Item(1, draw1));
                    i++;
                    if (i >= strings.length) {
                        Content content = new Content("");
                        items.add(new Item(0, content));
                    }
                }
            }
        }

        if (clearAndAdd) {

            everAdapter.UpdateAdapter(items, everDataBase.getContentsFromDatabaseWithID(GetIDFromSharedPreferences()));

        } else {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

            textanddrawRecyclerView.setLayoutManager(staggeredGridLayoutManager);

            everAdapter = new EverAdapter(requireActivity(), items, GetIDFromSharedPreferences(), everDataBase, everDataBase.getContentsFromDatabaseWithID(GetIDFromSharedPreferences()));

            textanddrawRecyclerView.setAdapter(everAdapter);

            EverAdapter.setClickListener(this);
        }

    }
    private void ResizeEverDrawToPrepareNoteToDraw() {
        //new Handler(Looper.getMainLooper()).postDelayed(() -> {
            TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                    .addTransition(new ChangeBounds()));

            ViewGroup.LayoutParams params = everDraw.getLayoutParams();

            params.height = 1400;

            everDraw.setLayoutParams(params);

        //}, 500);
    }

    private void ResizeCardViewToWrapContent() {

            TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                    .addTransition(new ChangeBounds()));

            ViewGroup.LayoutParams params = cardView.getLayoutParams();

            params.height = WRAP_CONTENT;

            cardView.setLayoutParams(params);
    }
}
