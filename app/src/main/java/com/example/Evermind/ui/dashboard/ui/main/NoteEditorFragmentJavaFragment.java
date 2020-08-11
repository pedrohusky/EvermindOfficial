package com.example.Evermind.ui.dashboard.ui.main;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.divyanshu.draw.widget.DrawView;
import com.example.Evermind.EverDataBase;
import com.example.Evermind.EvermindEditor;
import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.R;
import com.example.Evermind.SoftInputAssist;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muehlemann.giphy.GiphyLibrary;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.muehlemann.giphy.GiphyLibrary.API_KEY;

public class NoteEditorFragmentJavaFragment extends Fragment implements GiphyLibrary.Listener {

    private NoteEditorFragmentMainViewModel mViewModel;

    public static ImagesRecyclerGridAdapter adapter;

    private EverDataBase everDataBase;

    private EvermindEditor evermindEditor;

    public static String ImagesURLs;

    public Boolean CloseFormatter = false;
    public Boolean CloseParagraph = false;
    public Boolean CloseImporter = false;
    public Boolean CloseOpenedColors = false;
    public Boolean CloseOpenedDrawOptions = false;
    public Boolean CloseOpenedDrawColors = false;
    public Boolean CloseOpenedDrawSize = false;
    public Boolean CloseOpenedColorsHighlight = false;
    public Boolean DeleteSave = false;

    public Boolean DrawVisualizerIsShowing = false;
    public Boolean DrawOn = false;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static int fromPosition;
    public static int toPosition;

    private boolean tomanocu = false;

    private GiphyLibrary giphyLibrary;

    private static final String GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos";

    int size = 4;

    public static NoteEditorFragmentJavaFragment newInstance() {
        return new NoteEditorFragmentJavaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        return inflater.inflate(R.layout.fragment_note_creator, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //////////////////////////////////////// INICIAL VARIALBES \/

        everDataBase = new EverDataBase(getActivity());

        evermindEditor = requireActivity().findViewById(R.id.ToSaveNoteText);

        if (!everDataBase.getBackgroundFromDatabaseWithID(GetIDFromSharedPreferences()).equals("┼")) {

            Bitmap bitmap = BitmapFactory.decodeFile(everDataBase.getBackgroundFromDatabaseWithID(GetIDFromSharedPreferences()));

            if (bitmap != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

                evermindEditor.setBackground(bitmapDrawable);
            } }

        SoftInputAssist softInputAssist = new SoftInputAssist(requireActivity());

        evermindEditor.setEditorHeight(150);
        evermindEditor.setEditorFontSize(22);
        evermindEditor.setPadding(15, 15, 15, 15);
        // evermindEditor.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        EditText TitleTextBox = requireActivity().findViewById(R.id.TitleTextBox);
        ImageButton Undo = requireActivity().findViewById(R.id.Undo);
        ImageButton Redo = requireActivity().findViewById(R.id.Redo);
        ImageButton Delete = requireActivity().findViewById(R.id.Delete);
        ImageButton Save = requireActivity().findViewById(R.id.Save);

        String title = everDataBase.getTitlesFromDatabaseWithID(GetIDFromSharedPreferences());

        new Handler(Looper.getMainLooper()).post(() -> {

            TitleTextBox.setText(title);

        });

        String content = everDataBase.getContentsFromDatabaseWithID(GetIDFromSharedPreferences());
        boolean NewNote = GetNewNoteFromSharedPreferences();

        new Handler(Looper.getMainLooper()).post(() -> {

            evermindEditor.setHtml(content);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                if (NewNote) {
                    evermindEditor.focusEditor();

                    Undo.setVisibility(View.VISIBLE);
                    Redo.setVisibility(View.VISIBLE);
                    Delete.setVisibility(View.VISIBLE);
                    Save.setVisibility(View.VISIBLE);

                    InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(evermindEditor, 0);
                }

            }, 500);

        });

        //////////////////////////////////////// INICIAL VARIALBES /\

        BottomNavigationView note_bottom_bar = requireActivity().findViewById(R.id.note_bottom_bar);

        Animation bottom_nav_anim = AnimationUtils.loadAnimation(requireActivity(), R.anim.translate_up_anim);
        Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(requireActivity(), R.anim.translate_up_anim_reverse);

        ScrollView scrollView = requireActivity().findViewById(R.id.scrollview);
        HorizontalScrollView scrollView1 = requireActivity().findViewById(R.id.scroll_formatter);
        HorizontalScrollView scrollView2 = requireActivity().findViewById(R.id.scroll_paragraph);
        HorizontalScrollView scrollView3 = requireActivity().findViewById(R.id.scroll_import);
        HorizontalScrollView scrollView4 = requireActivity().findViewById(R.id.scroll_draw);

        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView1);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView2);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView3);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView4);

        ImageButton GooglePhotos = requireActivity().findViewById(R.id.GooglePhotos);
        ImageButton Gallery = requireActivity().findViewById(R.id.Gallery);
        ImageButton Files = requireActivity().findViewById(R.id.Files);

        ImageButton ChangeColor = requireActivity().findViewById(R.id.ChangeColor);

        ImageButton DrawChangeColor = requireActivity().findViewById(R.id.DrawChangeColor);
        ImageButton DrawChangeSize = requireActivity().findViewById(R.id.DrawChangeSize);

        SeekBar seekBarDrawSize = requireActivity().findViewById(R.id.draw_size_seekbar);

        ImageButton HighlightText = requireActivity().findViewById(R.id.HighlightText);

        ImageButton Black = requireActivity().findViewById(R.id.black);
        ImageButton Blue = requireActivity().findViewById(R.id.blue);
        ImageButton Purple = requireActivity().findViewById(R.id.purple);
        ImageButton Magenta = requireActivity().findViewById(R.id.magenta);
        ImageButton Orange = requireActivity().findViewById(R.id.orange);
        ImageButton Yellow = requireActivity().findViewById(R.id.yellow);
        ImageButton Green = requireActivity().findViewById(R.id.green);

        ImageButton BlackDraw = requireActivity().findViewById(R.id.Drawblack);
        ImageButton BlueDraw = requireActivity().findViewById(R.id.Drawblue);
        ImageButton PurpleDraw = requireActivity().findViewById(R.id.Drawpurple);
        ImageButton MagentaDraw = requireActivity().findViewById(R.id.Drawmagenta);
        ImageButton OrangeDraw = requireActivity().findViewById(R.id.Draworange);
        ImageButton YellowDraw = requireActivity().findViewById(R.id.Drawyellow);
        ImageButton GreenDraw = requireActivity().findViewById(R.id.Drawgreen);

        ImageButton ClearHighlight = requireActivity().findViewById(R.id.clearhighlight);
        ImageButton BlackHighlight = requireActivity().findViewById(R.id.blackhighlight);
        ImageButton BlueHighlight = requireActivity().findViewById(R.id.bluehighlight);
        ImageButton PurpleHighlight = requireActivity().findViewById(R.id.purplehighlight);
        ImageButton MagentaHighlight = requireActivity().findViewById(R.id.magentahighlight);
        ImageButton OrangeHighlight = requireActivity().findViewById(R.id.orangehighlight);
        ImageButton YellowHighlight = requireActivity().findViewById(R.id.yellowhighlight);
        ImageButton GreenHighlight = requireActivity().findViewById(R.id.greenhighlight);

        CardView cardView = requireActivity().findViewById(R.id.cardView);

        RecyclerView recyclerViewImage = requireActivity().findViewById(R.id.ImagesRecycler);

        DrawView drawView = requireActivity().findViewById(R.id.draw_view);

        new Thread(() -> {

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

            SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

            new Handler(Looper.getMainLooper()).post(() -> {

                giphyLibrary = new GiphyLibrary();

            });

            KeyboardVisibilityEvent.setEventListener(
                    requireActivity(),
                    isOpen -> {

                        if (isOpen) {

                            new Handler(Looper.getMainLooper()).post(() -> {

                                TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                                        .addTransition(new ChangeBounds()));

                               // evermindEditor.setEditorHeight(250);

                                ViewGroup.LayoutParams params = cardView.getLayoutParams();

                                params.height = 1100;

                                cardView.setLayoutParams(params);
                            });
                        } else {

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
                    drawView.setStrokeWidth(i);

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
                    evermindEditor.setFontSize(size);
                }

            });

            requireActivity().findViewById(R.id.DecreaseSize).setOnClickListener(v -> {


                if (size > 3) {

                    size--;
                    evermindEditor.setFontSize(size);
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

                        giphyLibrary.start(requireActivity(), this, API_KEY);


                        break;

                    case R.id.nav_draw:

                        OpenOrCloseDrawOptions();

                        DrawOn = true;

                        InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(requireView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                    default:
                        return true;
                }
                return true;
            });

            Delete.setOnClickListener(view -> {
                if (DrawOn) {
                    drawView.clearCanvas();
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

                    Bitmap bitmap = drawView.getBitmap();

                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    bitmapDrawable.setGravity(Gravity.CENTER|Gravity.END);

                    try {
                        TransformBitmapToFile(bitmap, true, ".jpeg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    evermindEditor.setBackground(bitmapDrawable);

                    drawView.clearCanvas();

                    OpenOrCloseDrawOptions();
                } else {
                    onBackPressed(false);
                }
            });

            Undo.setOnClickListener(view -> {
                if (DrawOn) {
                    drawView.undo();
                } else {
                    evermindEditor.undo();
                }
            });

            Redo.setOnClickListener(view -> {
                if (DrawOn) {
                    drawView.redo();
                } else {
                    evermindEditor.redo();
                }
            });

            requireActivity().findViewById(R.id.Bold).setOnClickListener(v -> evermindEditor.setBold());

            requireActivity().findViewById(R.id.Italic).setOnClickListener(v -> evermindEditor.setItalic());

            requireActivity().findViewById(R.id.Striketrough).setOnClickListener(v -> evermindEditor.setStrikeThrough());

            requireActivity().findViewById(R.id.Underline).setOnClickListener(v -> evermindEditor.setUnderline());

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


            requireActivity().findViewById(R.id.AlignLeft).setOnClickListener(v -> evermindEditor.setAlignLeft());
            requireActivity().findViewById(R.id.AlignCenter).setOnClickListener(v -> evermindEditor.setAlignCenter());
            requireActivity().findViewById(R.id.AlignRight).setOnClickListener(v -> evermindEditor.setAlignRight());


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


            evermindEditor.setOnFocusChangeListener((view, b) -> {

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
                                    keyboard.showSoftInput(evermindEditor, 0);
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

                    }, 200);

                } else {

                    new Handler(Looper.getMainLooper()).post(() -> {

                        //      TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                        //              .addTransition(new ChangeBounds()));

                        //    ViewGroup.LayoutParams params = cardView.getLayoutParams();

                        //    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
                        //     cardView.setLayoutParams(params);


                        ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, false, false, 0);
                        ApplyChangesToSharedPreferences("UndoRedo", false, "", true, false, false, 0);

                        Undo.setVisibility(View.GONE);
                        Redo.setVisibility(View.GONE);

                        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    });
                }
            });


            evermindEditor.setOnTextChangeListener(text -> new Thread(() -> {

                String transformToHexHTML = replaceRGBColorsWithHex(evermindEditor.getHtml());

                // focusOnView(scrollView, mEditor);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    everDataBase.editContent(Integer.toString(GetIDFromSharedPreferences()), transformToHexHTML);


                }, 750);

            }).start());

            TitleTextBox.setOnFocusChangeListener((view, b) -> {

                if (b) {

                    new Handler(Looper.getMainLooper()).post(() -> {

                        InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(TitleTextBox, 0);

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

                    new Handler(Looper.getMainLooper()).post(() -> {

                        InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(requireActivity().INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        // Delete.setVisibility(View.GONE);
                        // Save.setVisibility(View.GONE);

                        ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, false, false, 0);

                    });
                }


            });


            //////////////////////////////////////////// HANDLE IMAGES \/


            ImagesURLs = everDataBase.getImageURLFromDatabaseWithID(preferences.getInt("noteId", -1));

            //ImagesURLs.removeAll(Collections.singletonList(""));

            if (ImagesURLs.length() > 0) {
                GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getActivity(), GridLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setSpanCount(2);

                new Handler(Looper.getMainLooper()).post(() -> {

                    recyclerViewImage.setLayoutManager(staggeredGridLayoutManager);

                    adapter = new ImagesRecyclerGridAdapter(this.getActivity(), ImagesURLs, preferences.getInt("position", -1), ImagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);

                    recyclerViewImage.setAdapter(adapter);

                });
            }

        }).start();

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
        CardView cardView = requireActivity().findViewById(R.id.cardView);
        switch (name) {
            case "GooglePhotos":

                Intent intentGooglePhotos = new Intent();
                intentGooglePhotos.setAction(Intent.ACTION_PICK);
                intentGooglePhotos.setType("image/*");
                intentGooglePhotos.setPackage(GOOGLE_PHOTOS_PACKAGE_NAME);
                startActivityForResult(intentGooglePhotos, 101);


                new Handler(Looper.getMainLooper()).post(() -> {

                    TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                            .addTransition(new ChangeBounds()));

                    ViewGroup.LayoutParams params = cardView.getLayoutParams();

                    params.height = WRAP_CONTENT;

                    cardView.setLayoutParams(params);
                });

                break;

            case "Gallery":


                break;

            case "Files":

                Intent intentFiles = new Intent();
                intentFiles.setType("image/*");
                intentFiles.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentFiles, "Select Picture"), 101);

                new Handler(Looper.getMainLooper()).post(() -> {

                    TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                            .addTransition(new ChangeBounds()));

                    ViewGroup.LayoutParams params = cardView.getLayoutParams();

                    params.height = WRAP_CONTENT;

                    cardView.setLayoutParams(params);
                });

                break;


            default:
                break;
        }
    }


    private void OpenOrCloseColors(Boolean highlight) {

        new Thread(() -> {

            ImageButton ChangeColor = requireActivity().findViewById(R.id.ChangeColor);
            ImageButton Bold = requireActivity().findViewById(R.id.Bold);
            ImageButton Italic = requireActivity().findViewById(R.id.Italic);
            ImageButton Underline = requireActivity().findViewById(R.id.Underline);
            ImageButton Striketrough = requireActivity().findViewById(R.id.Striketrough);
            ImageButton HighlightText = requireActivity().findViewById(R.id.HighlightText);

            ImageButton Black = requireActivity().findViewById(R.id.black);
            ImageButton Blue = requireActivity().findViewById(R.id.blue);
            ImageButton Purple = requireActivity().findViewById(R.id.purple);
            ImageButton Magenta = requireActivity().findViewById(R.id.magenta);
            ImageButton Orange = requireActivity().findViewById(R.id.orange);
            ImageButton Yellow = requireActivity().findViewById(R.id.yellow);
            ImageButton Green = requireActivity().findViewById(R.id.green);

            ImageButton ClearHighlight = requireActivity().findViewById(R.id.clearhighlight);
            ImageButton BlackHighlight = requireActivity().findViewById(R.id.blackhighlight);
            ImageButton BlueHighlight = requireActivity().findViewById(R.id.bluehighlight);
            ImageButton PurpleHighlight = requireActivity().findViewById(R.id.purplehighlight);
            ImageButton MagentaHighlight = requireActivity().findViewById(R.id.magentahighlight);
            ImageButton OrangeHighlight = requireActivity().findViewById(R.id.orangehighlight);
            ImageButton YellowHighlight = requireActivity().findViewById(R.id.yellowhighlight);
            ImageButton GreenHighlight = requireActivity().findViewById(R.id.greenhighlight);

            ImageButton Increase = requireActivity().findViewById(R.id.IncreaseSize);
            ImageButton Decrease = requireActivity().findViewById(R.id.DecreaseSize);
            ImageButton Left = requireActivity().findViewById(R.id.AlignLeft);
            ImageButton Center = requireActivity().findViewById(R.id.AlignCenter);
            ImageButton Right = requireActivity().findViewById(R.id.AlignRight);

            if (CloseOpenedColors) {

                if (highlight) {


                    new Handler(Looper.getMainLooper()).post(() -> {

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

                    });
                } else {

                    new Handler(Looper.getMainLooper()).post(() -> {

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

                    });
                }

            } else {
                if (highlight) {
                    new Handler(Looper.getMainLooper()).post(() -> {

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
                    });

                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {


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
                    });
                }
            }

        }).start();
    }

    private void OpenOrCloseDrawOptions() {

        new Thread(() -> {

            Animation fadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_formatter);
            Animation fadeout = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_formatter);
            CardView draw_color = requireActivity().findViewById(R.id.draw_options);

            EditText TitleTextBox = requireActivity().findViewById(R.id.TitleTextBox);

            ImageButton change_color = requireActivity().findViewById(R.id.DrawChangeColor);
            ImageButton change_size = requireActivity().findViewById(R.id.DrawChangeSize);
            RecyclerView recyclerViewImage = requireActivity().findViewById(R.id.ImagesRecycler);

            DrawView drawView = requireActivity().findViewById(R.id.draw_view);

            if (CloseOpenedDrawOptions) {

                new Handler(Looper.getMainLooper()).post(() -> {

                    draw_color.startAnimation(fadeout);
                    change_color.setVisibility(View.GONE);
                    change_size.setVisibility(View.GONE);
                    TitleTextBox.setVisibility(View.GONE);


                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        drawView.setVisibility(View.GONE);
                        recyclerViewImage.setVisibility(View.VISIBLE);

                        draw_color.setVisibility(View.GONE);

                    }, 100);

                    CloseOpenedDrawOptions = false;

                });

            } else {
                new Handler(Looper.getMainLooper()).post(() -> {

                    drawView.setVisibility(View.VISIBLE);
                    recyclerViewImage.setVisibility(View.GONE);
                    TitleTextBox.setVisibility(View.GONE);

                    draw_color.setVisibility(View.VISIBLE);

                    draw_color.startAnimation(fadein);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        change_color.setVisibility(View.VISIBLE);
                        change_size.setVisibility(View.VISIBLE);
                    }, 100);

                    CloseOpenedDrawOptions = true;
                });
            }

        }).start();
    }

    private void OpenOrCloseDrawSize() {

        new Thread(() -> {

            ImageButton change_color = requireActivity().findViewById(R.id.DrawChangeColor);
            ImageButton change_size = requireActivity().findViewById(R.id.DrawChangeSize);
            SeekBar size_seekbar = requireActivity().findViewById(R.id.draw_size_seekbar);
            ImageButton Black = requireActivity().findViewById(R.id.Drawblack);
            ImageButton Blue = requireActivity().findViewById(R.id.Drawblue);
            ImageButton Purple = requireActivity().findViewById(R.id.Drawpurple);
            ImageButton Magenta = requireActivity().findViewById(R.id.Drawmagenta);
            ImageButton Orange = requireActivity().findViewById(R.id.Draworange);
            ImageButton Yellow = requireActivity().findViewById(R.id.Drawyellow);
            ImageButton Green = requireActivity().findViewById(R.id.Drawgreen);

            if (CloseOpenedDrawSize) {

                new Handler(Looper.getMainLooper()).post(() -> {

                    change_color.setVisibility(View.VISIBLE);
                    change_size.setVisibility(View.VISIBLE);
                    Black.setVisibility(View.GONE);
                    Blue.setVisibility(View.GONE);
                    Purple.setVisibility(View.GONE);
                    Magenta.setVisibility(View.GONE);
                    Orange.setVisibility(View.GONE);
                    Yellow.setVisibility(View.GONE);
                    Green.setVisibility(View.GONE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        // ChangeColor.setVisibility(View.GONE);

                        size_seekbar.setVisibility(View.GONE);

                    }, 100);

                    CloseOpenedDrawSize = false;

                });

            } else {

                new Handler(Looper.getMainLooper()).post(() -> {

                    change_color.setVisibility(View.GONE);
                    change_size.setVisibility(View.VISIBLE);
                    Black.setVisibility(View.GONE);
                    Blue.setVisibility(View.GONE);
                    Purple.setVisibility(View.GONE);
                    Magenta.setVisibility(View.GONE);
                    Orange.setVisibility(View.GONE);
                    Yellow.setVisibility(View.GONE);
                    Green.setVisibility(View.GONE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> size_seekbar.setVisibility(View.VISIBLE), 100);

                    CloseOpenedDrawSize = true;
                });
            }

        }).start();
    }

    private void OpenOrCloseDrawColors() {

        new Thread(() -> {

            ImageButton change_color = requireActivity().findViewById(R.id.DrawChangeColor);
            ImageButton change_size = requireActivity().findViewById(R.id.DrawChangeSize);
            ImageButton Black = requireActivity().findViewById(R.id.Drawblack);
            ImageButton Blue = requireActivity().findViewById(R.id.Drawblue);
            ImageButton Purple = requireActivity().findViewById(R.id.Drawpurple);
            ImageButton Magenta = requireActivity().findViewById(R.id.Drawmagenta);
            ImageButton Orange = requireActivity().findViewById(R.id.Draworange);
            ImageButton Yellow = requireActivity().findViewById(R.id.Drawyellow);
            ImageButton Green = requireActivity().findViewById(R.id.Drawgreen);

            if (CloseOpenedDrawColors) {

                new Handler(Looper.getMainLooper()).post(() -> {

                    Black.setVisibility(View.GONE);
                    Blue.setVisibility(View.GONE);
                    Purple.setVisibility(View.GONE);
                    Magenta.setVisibility(View.GONE);
                    Orange.setVisibility(View.GONE);
                    Yellow.setVisibility(View.GONE);
                    Green.setVisibility(View.GONE);


                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        // ChangeColor.setVisibility(View.GONE);

                        change_color.setVisibility(View.VISIBLE);
                        change_size.setVisibility(View.VISIBLE);


                    }, 150);

                    CloseOpenedDrawColors = false;

                });

            } else {

                new Handler(Looper.getMainLooper()).post(() -> {

                    change_color.setVisibility(View.GONE);
                    change_size.setVisibility(View.GONE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        // ChangeColor.setVisibility(View.GONE);

                        Black.setVisibility(View.VISIBLE);
                        Blue.setVisibility(View.VISIBLE);
                        Purple.setVisibility(View.VISIBLE);
                        Magenta.setVisibility(View.VISIBLE);
                        Orange.setVisibility(View.VISIBLE);
                        Yellow.setVisibility(View.VISIBLE);
                        Green.setVisibility(View.VISIBLE);


                    }, 150);


                    CloseOpenedDrawColors = true;
                });
            }

        }).start();
    }

    private void ShowDrawSizeVisualizer() {

        new Thread(() -> {

            Animation fadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_formatter);
            Animation fadeout = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_formatter);
            CardView size_visualizer = requireActivity().findViewById(R.id.draw_sizeVisualizer);

            ImageView ImageSizeView = requireActivity().findViewById(R.id.draw_size_visualizer);

            DrawVisualizerIsShowing = true;

            new Handler(Looper.getMainLooper()).post(() -> {
                // ChangeColor.setVisibility(View.GONE);

                size_visualizer.setVisibility(View.VISIBLE);
                size_visualizer.startAnimation(fadein);

            });

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

        }).start();
    }

    private void ModifyDrawSizeVisualizer(int value) {

        new Thread(() -> {

            ImageView ImageSizeView = requireActivity().findViewById(R.id.draw_size_visualizer);

            ImageSizeView.setScaleX(value);
            ImageSizeView.setScaleY(value);

        }).start();
    }

    public void ColorClickedSwitcher(String color, boolean highlight) {

        if (highlight) {

            switch (color) {

                case "Clear":

                    ImageButton ClearHighlight = requireActivity().findViewById(R.id.clearhighlight);

                    evermindEditor.setTextBackgroundColor(Color.WHITE);

                    OpenOrCloseColors(true);

                    ClearHighlight.setVisibility(View.GONE);

                    break;

                case "Black":

                    evermindEditor.setTextBackgroundColor(GetColor(R.color.Black));

                    OpenOrCloseColors(true);


                    break;

                case "Blue":


                    evermindEditor.setTextBackgroundColor(GetColor(R.color.SkyBlueHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Purple":


                    evermindEditor.setTextBackgroundColor(GetColor(R.color.PinkHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Magenta":


                    evermindEditor.setTextBackgroundColor(GetColor(R.color.MagentaHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Orange":


                    evermindEditor.setTextBackgroundColor(GetColor(R.color.OrangeHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Yellow":

                    evermindEditor.setTextBackgroundColor(GetColor(R.color.YellowSunHighlight));

                    OpenOrCloseColors(true);


                    break;

                case "Green":

                    evermindEditor.setTextBackgroundColor(GetColor(R.color.GrassGreen));

                    OpenOrCloseColors(true);


                    break;

                default:


                    break;
            }
        } else {
            switch (color) {

                case "Black":

                    evermindEditor.setTextColor(GetColor((R.color.Black)));

                    OpenOrCloseColors(false);


                    break;

                case "Blue":

                    evermindEditor.setTextColor(GetColor(R.color.SkyBlue));

                    //  Blue = !Blue;


                    OpenOrCloseColors(false);


                    break;

                case "Purple":

                    evermindEditor.setTextColor(GetColor((R.color.Pink)));

                    //  Purple = !Purple;

                    OpenOrCloseColors(false);


                    break;

                case "Magenta":

                    evermindEditor.setTextColor(GetColor(R.color.Magenta));

                    //  Magenta = !Magenta;


                    OpenOrCloseColors(false);


                    break;

                case "Orange":


                    evermindEditor.setTextColor(GetColor((R.color.Orange)));

                    //  Orange = !Orange;

                    OpenOrCloseColors(false);


                    break;

                case "Yellow":


                    evermindEditor.setTextColor(GetColor((R.color.YellowSun)));

                    // Yellow = !Yellow;

                    OpenOrCloseColors(false);


                    break;

                case "Green":


                    evermindEditor.setTextColor(GetColor((R.color.GrassGreen)));

                    // Green = !Green;

                    OpenOrCloseColors(false);


                    break;

                default:


                    break;
            }
        }
    }

    public void DrawColorClickedSwitcher(String color) {

        DrawView drawView = requireActivity().findViewById(R.id.draw_view);

        switch (color) {

            case "Black":

                drawView.setColor(GetColor(R.color.Black));

                OpenOrCloseDrawColors();


                break;

            case "Blue":

                drawView.setColor(GetColor(R.color.SkyBlue));


                OpenOrCloseDrawColors();


                break;

            case "Purple":

                drawView.setColor(GetColor(R.color.Pink));

                OpenOrCloseDrawColors();


                break;

            case "Magenta":

                drawView.setColor(GetColor(R.color.Magenta));


                OpenOrCloseDrawColors();


                break;

            case "Orange":


                drawView.setColor(GetColor(R.color.Orange));

                OpenOrCloseDrawColors();


                break;

            case "Yellow":


                drawView.setColor(GetColor(R.color.YellowSun));

                OpenOrCloseDrawColors();


                break;

            case "Green":


                drawView.setColor(GetColor(R.color.GrassGreen));

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

        new Handler(Looper.getMainLooper()).post(() -> {

            if (delete) {

                CloseAllButtons();

                //Hide nav view \/ \/ \/
                BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.note_bottom_bar);
                Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_anim_reverse);
                bottomNavigationView.startAnimation(bottom_nav_anim_reverse);


                //Hide nav view /\ /\ /\
                ApplyChangesToSharedPreferences("athome", false, "", true, true, false, 0);
                ApplyChangesToSharedPreferences("content", true, "", false, false, false, 0);

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_note_to_nav_home);
                CloseEditorButtonsSaveDelete();
            } else {


                EditText editText = requireActivity().findViewById(R.id.TitleTextBox);

                new Thread(() -> {
                    int id = GetIDFromSharedPreferences();
                    everDataBase.editTitle(Integer.toString(id), editText.getText().toString());

                    //Hide nav view \/ \/ \/
                    BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.note_bottom_bar);
                    Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(requireActivity(), R.anim.translate_up_anim_reverse);
                    bottomNavigationView.startAnimation(bottom_nav_anim_reverse);

                    ApplyChangesToSharedPreferences("athome", false, "", true, true, false, 0);
                    ApplyChangesToSharedPreferences("content", true, "", false, false, false, 0);


                }).start();

                CloseAllButtons();

                CloseEditorButtonsSaveDelete();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_note_to_nav_home);
            }
        });
    }

    private void CloseOrOpenFormatter() {

        Animation fadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_formatter);
        CardView format_text = requireActivity().findViewById(R.id.format_selector);

        ImageButton ChangeColor = requireActivity().findViewById(R.id.ChangeColor);
        ImageButton Bold = requireActivity().findViewById(R.id.Bold);
        ImageButton Italic = requireActivity().findViewById(R.id.Italic);
        ImageButton Underline = requireActivity().findViewById(R.id.Underline);
        ImageButton Striketrough = requireActivity().findViewById(R.id.Striketrough);
        ImageButton HighlightText = requireActivity().findViewById(R.id.HighlightText);

        ImageButton Black = requireActivity().findViewById(R.id.black);
        ImageButton Blue = requireActivity().findViewById(R.id.blue);
        ImageButton Purple = requireActivity().findViewById(R.id.purple);
        ImageButton Magenta = requireActivity().findViewById(R.id.magenta);
        ImageButton Orange = requireActivity().findViewById(R.id.orange);
        ImageButton Yellow = requireActivity().findViewById(R.id.yellow);
        ImageButton Green = requireActivity().findViewById(R.id.green);

        ImageButton BlackHighlight = requireActivity().findViewById(R.id.blackhighlight);
        ImageButton BlueHighlight = requireActivity().findViewById(R.id.bluehighlight);
        ImageButton PurpleHighlight = requireActivity().findViewById(R.id.purplehighlight);
        ImageButton MagentaHighlight = requireActivity().findViewById(R.id.magentahighlight);
        ImageButton OrangeHighlight = requireActivity().findViewById(R.id.orangehighlight);
        ImageButton YellowHighlight = requireActivity().findViewById(R.id.yellowhighlight);
        ImageButton GreenHighlight = requireActivity().findViewById(R.id.greenhighlight);

        ImageButton Increase = requireActivity().findViewById(R.id.IncreaseSize);
        ImageButton Decrease = requireActivity().findViewById(R.id.DecreaseSize);

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

                format_text.startAnimation(fadeout);

                format_text.setVisibility(View.GONE);

            }, 150);

            CloseFormatter = false;


        } else {

            format_text.setVisibility(View.VISIBLE);

            format_text.startAnimation(fadein);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                Increase.setVisibility(View.VISIBLE);
                Decrease.setVisibility(View.VISIBLE);
                ChangeColor.setVisibility(View.VISIBLE);
                Bold.setVisibility(View.VISIBLE);
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

        Animation fadein = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter);
        CardView format_text = requireActivity().findViewById(R.id.format_paragraph);

        ImageButton Bullets = requireActivity().findViewById(R.id.Bullets);
        ImageButton Numbers = requireActivity().findViewById(R.id.Numbers);
        ImageButton Left = requireActivity().findViewById(R.id.AlignLeft);
        ImageView spacing = requireActivity().findViewById(R.id.imageView2);
        ImageButton Center = requireActivity().findViewById(R.id.AlignCenter);
        ImageButton Right = requireActivity().findViewById(R.id.AlignRight);

        if (CloseParagraph) {

            spacing.setVisibility(View.GONE);
            Bullets.setVisibility(View.GONE);
            Numbers.setVisibility(View.GONE);
            Left.setVisibility(View.GONE);
            Center.setVisibility(View.GONE);
            Right.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                format_text.startAnimation(fadeout);

                format_text.setVisibility(View.GONE);

            }, 150);

            CloseParagraph = false;


        } else {

            format_text.setVisibility(View.VISIBLE);

            format_text.startAnimation(fadein);


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

        Animation fadein = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_out_formatter);
        CardView importer = requireActivity().findViewById(R.id.import_options);

        ImageButton Google = requireActivity().findViewById(R.id.GooglePhotos);
        ImageButton Gallery = requireActivity().findViewById(R.id.Gallery);
        ImageButton Files = requireActivity().findViewById(R.id.Files);

        if (CloseImporter) {

            Google.setVisibility(View.GONE);
            Gallery.setVisibility(View.GONE);
            Files.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                importer.startAnimation(fadeout);

              //  importer.setVisibility(View.GONE);

            }, 150);

            CloseImporter = false;


        } else {

            importer.setVisibility(View.VISIBLE);

            importer.startAnimation(fadein);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                Google.setVisibility(View.VISIBLE);
                Gallery.setVisibility(View.VISIBLE);
                Files.setVisibility(View.VISIBLE);

            }, 150);

            CloseImporter = true;

        }
    }

    private void CloseEditorButtonsSaveDelete() {

        ImageButton Delete = requireActivity().findViewById(R.id.Delete);
        ImageButton Save = requireActivity().findViewById(R.id.Save);

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
        SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        return preferences.getInt("noteId", -1);
    }

    private Boolean GetNewNoteFromSharedPreferences() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        return preferences.getBoolean("newnote", false);
    }

    private Boolean GetDeleteNSaveFromSharedPreferences() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        return preferences.getBoolean("DeleteNSave", false);
    }

    private void ApplyChangesToSharedPreferences(String name, boolean string, String text, boolean bolean, boolean value, boolean integer, int id) {

        SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

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

        SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

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

        SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        File directory = this.requireActivity().getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + fileType);

        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos;

            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();


            if (addToDatabase) {
                everDataBase.insertNoteBackgroundToDatabase(String.valueOf(preferences.getInt("noteId", -1)), file.toString());

            }
        }
    }

    private void ReloadImagesRecycler() {

        SharedPreferences preferences = requireActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            ImagesURLs = everDataBase.getImageURLFromDatabaseWithID(preferences.getInt("noteId", -1));
            RecyclerView recyclerView = requireActivity().findViewById(R.id.ImagesRecycler);
            adapter = new ImagesRecyclerGridAdapter(this.requireActivity(), ImagesURLs, preferences.getInt("position", -1), ImagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);
            recyclerView.invalidate();
            recyclerView.setAdapter(adapter);

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

    @Override
    public void onGiphySelected(String url) {

    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color,null);
    }
}

