package com.example.Evermind;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import androidx.core.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.example.Evermind.ui.note_screen.NotesScreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.muehlemann.giphy.GiphyLibrary;
import com.sysdata.kt.htmltextview.SDHtmlTextView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    public GiphyLibrary giphyLibrary;

    public EverDataBase mDatabaseEver;

    public Integer ID;

    public RecyclerView recyclertest;

    private HorizontalScrollView scrollView1;
    private HorizontalScrollView scrollView2;
    private HorizontalScrollView scrollView3;
    private HorizontalScrollView scrollView4;
    public Boolean CloseFormatter = false;
    public Boolean CloseParagraph = false;
    public Boolean CloseImporter = false;
    public Boolean CloseOpenedColors = false;
    public Boolean CloseOpenedDrawOptions = false;
    public Boolean CloseOpenedDrawColors = false;
    public Boolean CloseOpenedDrawSize = false;
    public Boolean CloseOpenedColorsHighlight = false;
    public Boolean DeleteSave = false;
    public Boolean UndoRedo = false;
    public Boolean bottomBarShowing = false;
    public Boolean showNoteContents = false;
    public SeekBar seekBarDrawSize;
    public ImageButton Undo;
    public ImageButton Redo;
    public ImageButton Delete;
    public ImageButton Save;
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
    public ImageButton BlackDraw;
    public ImageButton BlueDraw;
    public ImageButton PurpleDraw;
    public ImageButton MagentaDraw;
    public ImageButton OrangeDraw;
    public ImageButton YellowDraw;
    public ImageButton GreenDraw;
    private ImageButton DrawChangeColor;
    private ImageButton DrawChangeSize;
    private CardView DrawOptions;
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
    public ImageButton GooglePhotos;
    public ImageButton Gallery;
    public ImageButton Files;
    public BottomNavigationView note_bottom_bar;
    public Animation bottom_nav_anim;
    public Animation bottom_nav_anim_reverse;
    public Boolean DrawVisualizerIsShowing = false;
    public Boolean DrawOn = false;
    private CardView size_visualizer;
    private ImageView ImageSizeView;
    private CardView format_selector;
    private CardView paragraph_selector;
    private CardView importer_selector;
    private ImageButton Bullets;
    private ImageButton Numbers;
    private ImageView spacing;
    private Toolbar toolbar;
    private int size = 4;
    private String ImagesUrls;
    public Boolean addedNote;
    public NotesScreen noteScreen;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public boolean atHome = true;
    public boolean newNote = false;
    public int selectedPosition;
    public int selectedID;
    public CardView cardNoteCreator;
    private PopupWindowHelper popupWindowHelperColor;
    private PopupWindowHelper popupWindowHelper;
    public NoteEditorFragmentJavaFragment noteCreator;
    public RecyclerView contentRecycler;
    public TextView title;
    public RecyclerView imageRecycler;
    public String[] arrays = new String[0];
    public ArrayList<Note_Model> notesModels = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabaseEver = new EverDataBase(this);

        //TODO: optimize performance a little more and make sure everything that we changed is working as intended

        notesModels = (ArrayList<Note_Model>) getIntent().getSerializableExtra("notes");
        noteScreen = new NotesScreen();


        System.out.println(notesModels.toString());
        preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        editor = preferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper()).post(() -> {

            giphyLibrary = new GiphyLibrary();

            Fresco.initialize(this);

        });

        SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        atHome = true;
        editor.apply();


        //////////CLEAR SHAREDPREFS

        new Thread(() -> {
            // a potentially time consuming task

            note_bottom_bar = findViewById(R.id.note_bottom_bar);
            bottom_nav_anim = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim);
            bottom_nav_anim_reverse = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim_reverse);
            ChangeColor = findViewById(R.id.ChangeColor);
            DrawChangeColor = findViewById(R.id.DrawChangeColor);
            DrawChangeSize = findViewById(R.id.DrawChangeSize);
            size_visualizer = findViewById(R.id.draw_sizeVisualizerCardView);
            ImageSizeView = findViewById(R.id.draw_size_visualizer);
            DrawOptions = findViewById(R.id.draw_options);
            seekBarDrawSize = findViewById(R.id.draw_size_seekbar);
            Italic = findViewById(R.id.Italic);
            Bold = findViewById(R.id.Bold);
            Underline = findViewById(R.id.Underline);
            Striketrough = findViewById(R.id.Striketrough);
            HighlightText = findViewById(R.id.HighlightText);
            Black = findViewById(R.id.black);
            Blue = findViewById(R.id.blue);
            Purple = findViewById(R.id.purple);
            Magenta = findViewById(R.id.magenta);
            Orange = findViewById(R.id.orange);
            Yellow = findViewById(R.id.yellow);
            Green = findViewById(R.id.green);
            BlackDraw = findViewById(R.id.Drawblack);
            BlueDraw = findViewById(R.id.Drawblue);
            PurpleDraw = findViewById(R.id.Drawpurple);
            MagentaDraw = findViewById(R.id.Drawmagenta);
            OrangeDraw = findViewById(R.id.Draworange);
            YellowDraw = findViewById(R.id.Drawyellow);
            GreenDraw = findViewById(R.id.Drawgreen);
            ClearHighlight = findViewById(R.id.clearhighlight);
            BlackHighlight = findViewById(R.id.blackhighlight);
            BlueHighlight = findViewById(R.id.bluehighlight);
            PurpleHighlight = findViewById(R.id.purplehighlight);
            MagentaHighlight = findViewById(R.id.magentahighlight);
            OrangeHighlight = findViewById(R.id.orangehighlight);
            YellowHighlight = findViewById(R.id.yellowhighlight);
            GreenHighlight = findViewById(R.id.greenhighlight);
            DrawOptions = findViewById(R.id.draw_options);
            size_visualizer = findViewById(R.id.draw_sizeVisualizerCardView);
            ImageSizeView = findViewById(R.id.draw_size_visualizer);
            format_selector = findViewById(R.id.format_selector);
            importer_selector = findViewById(R.id.import_options);
            paragraph_selector = findViewById(R.id.format_paragraph);
            Undo = findViewById(R.id.Undo);
            Redo = findViewById(R.id.Redo);
            Delete = findViewById(R.id.Delete);
            Save = findViewById(R.id.Save);
            Bullets = findViewById(R.id.Bullets);
            Numbers = findViewById(R.id.Numbers);
            spacing = findViewById(R.id.paragraph_spacing);
            Increase = findViewById(R.id.IncreaseSize);
            Decrease = findViewById(R.id.DecreaseSize);
            Left = findViewById(R.id.AlignLeft);
            Right = findViewById(R.id.AlignRight);
            Center = findViewById(R.id.AlignCenter);
            GooglePhotos = findViewById(R.id.GooglePhotos);
            Gallery = findViewById(R.id.Gallery);
            Files = findViewById(R.id.Files);
            toolbar = findViewById(R.id.toolbar);
            scrollView1 = findViewById(R.id.scroll_draw);
            scrollView2 = findViewById(R.id.scroll_formatter);
            scrollView3 = findViewById(R.id.scroll_import);
            scrollView4 = findViewById(R.id.scroll_paragraph);
            //OverScrollDecoratorHelper.setUpOverScroll(scrollView1);
            OverScrollDecoratorHelper.setUpOverScroll(scrollView2);
            OverScrollDecoratorHelper.setUpOverScroll(scrollView3);
            OverScrollDecoratorHelper.setUpOverScroll(scrollView4);

            new Handler(Looper.getMainLooper()).post(() -> {

                setSupportActionBar(toolbar);

                Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


                //  DrawerLayout drawer = findViewById(R.id.drawer_layout);
                NavigationView navigationView = findViewById(R.id.nav_view);

                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.

                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_note)
                        // .setDrawerLayout(drawer)
                        .build();

                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);

            });


            Black.setOnClickListener(view -> ColorClickedSwitcher("Black", false));

            Blue.setOnClickListener(view -> ColorClickedSwitcher("Blue", false));

            Purple.setOnClickListener(view -> ColorClickedSwitcher("Purple", false));

            Magenta.setOnClickListener(view -> ColorClickedSwitcher("Magenta", false));

            Orange.setOnClickListener(view -> ColorClickedSwitcher("Orange", false));

            Yellow.setOnClickListener(view -> ColorClickedSwitcher("Yellow", false));

            Green.setOnClickListener(view -> ColorClickedSwitcher("Green", false));

            ClearHighlight.setOnClickListener(view -> ColorClickedSwitcher("Clear", true));

            BlackHighlight.setOnClickListener(view -> ColorClickedSwitcher("Black", true));

            BlueHighlight.setOnClickListener(view -> ColorClickedSwitcher("Blue", true));

            PurpleHighlight.setOnClickListener(view -> ColorClickedSwitcher("Purple", true));

            MagentaHighlight.setOnClickListener(view -> ColorClickedSwitcher("Magenta", true));

            OrangeHighlight.setOnClickListener(view -> ColorClickedSwitcher("Orange", true));

            YellowHighlight.setOnClickListener(view -> ColorClickedSwitcher("Yellow", true));

            GreenHighlight.setOnClickListener(view -> ColorClickedSwitcher("Green", true));


            KeyboardVisibilityEvent.setEventListener(
                    this,
                    isOpen -> {

                        if (isOpen) {

                            //TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                            //        .addTransition(new ChangeBounds()));

                            // evermindEditor.setEditorHeight(250);

                            // ViewGroup.LayoutParams params = cardView.getLayoutParams();

                            //params.height = 1100;

                            // cardView.setLayoutParams(params);

                        } else {

                            //  TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                            //          .addTransition(new ChangeBounds()));

                            // ViewGroup.LayoutParams params = cardView.getLayoutParams();

                            //params.height = WRAP_CONTENT;

                            // cardView.setLayoutParams(params);
                        }
                    });


            ChangeColor.setOnClickListener(view -> {
                if (CloseOpenedColors) {

                    CloseOrOpenColors(false);


                } else {

                    CloseOrOpenColors(false);

                }
            });

            DrawChangeColor.setOnClickListener(view -> CloseOrOpenDrawColors());

            DrawChangeSize.setOnClickListener(view -> CloseOrOpenDrawSize());

            HighlightText.setOnClickListener(view -> {
                if (CloseOpenedColorsHighlight) {

                    CloseOrOpenColors(true);


                } else {

                    CloseOrOpenColors(true);

                }
            });


            findViewById(R.id.IncreaseSize).setOnClickListener(v -> {


                if (size < 7) {

                    size++;
                    EverAdapter.GetActiveEditor().setFontSize(size);
                }

            });

            findViewById(R.id.DecreaseSize).setOnClickListener(v -> {


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


                        break;

                    case R.id.nav_draw:

                        CloseOrOpenDraWOptions(1400);

                        InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(seekBarDrawSize.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    default:
                        return true;
                }
                return true;
            });

            findViewById(R.id.Bold).setOnClickListener(v -> EverAdapter.GetActiveEditor().setBold());
            findViewById(R.id.Italic).setOnClickListener(v -> EverAdapter.GetActiveEditor().setItalic());
            findViewById(R.id.Striketrough).setOnClickListener(v -> EverAdapter.GetActiveEditor().setStrikeThrough());
            findViewById(R.id.Underline).setOnClickListener(v -> EverAdapter.GetActiveEditor().setUnderline());

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


            findViewById(R.id.AlignLeft).setOnClickListener(v -> EverAdapter.GetActiveEditor().setAlignLeft());
            findViewById(R.id.AlignCenter).setOnClickListener(v -> EverAdapter.GetActiveEditor().setAlignCenter());
            findViewById(R.id.AlignRight).setOnClickListener(v -> EverAdapter.GetActiveEditor().setAlignRight());


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

        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        if (bottomBarShowing) {
            note_bottom_bar.startAnimation(bottom_nav_anim_reverse);
        }

        if (!notesModels.get(selectedPosition).getTitle().equals("") && !notesModels.get(selectedPosition).getContent().equals("") && !notesModels.get(selectedPosition).getDrawLocation().equals("") && !notesModels.get(selectedPosition).getImageURLS().equals("")) {
            mDatabaseEver.deleteNote(notesModels.get(selectedPosition).getId());
            notesModels.remove(notesModels.get(selectedPosition).getActualPosition());
            System.out.println("Note with id = " + notesModels.get(selectedPosition).getId() + " deleted. <-- called from OnBackPress in NoteEditorFragmentJava, thx future pedro");
        }


        if (!atHome) {
            new Thread(() -> { mDatabaseEver.setNoteModel(String.valueOf(notesModels.get(selectedPosition).getId()), notesModels.get(selectedPosition).getTitle(), notesModels.get(selectedPosition).getContent(), notesModels.get(selectedPosition).getDrawLocation(), notesModels.get(selectedPosition).getImageURLS(), notesModels.get(selectedPosition).getNoteColor()); }).start();

            FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
            TransitionManager.beginDelayedTransition(cardNoteCreator, new TransitionSet()
                    .addTransition(new ChangeBounds()));
            transaction.setReorderingAllowed(true);
            cardNoteCreator.setTransitionName("card" + selectedPosition);
            contentRecycler.setTransitionName("textRecycler" + selectedPosition);
            title.setTransitionName("title" + selectedPosition);
            imageRecycler.setTransitionName("imageRecycler" + selectedPosition);
            transaction.addSharedElement(cardNoteCreator, "card" + selectedPosition);
            transaction.addSharedElement(contentRecycler, "textRecycler" + selectedPosition);
            transaction.addSharedElement(title, "title" + selectedPosition);
            transaction.addSharedElement(imageRecycler, "imageRecycler" + selectedPosition);
            transaction.hide(noteCreator);
            transaction.replace(R.id.nav_host_fragment, noteScreen);
            transaction.show(noteScreen);
            transaction.addToBackStack(null);
            transaction.commit();
        }


        CloseAllButtons();

        atHome = true;

        newNote = false;

        addedNote = false;


        // new Handler(Looper.getMainLooper()).postDelayed(super::onBackPressed, 190);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (atHome) {
            return super.onOptionsItemSelected(item);
        } else {

            new Thread(() -> {
                // a potentially time consuming task
                EditText title_editText = this.findViewById(R.id.TitleTextBox);
                int id = preferences.getInt("noteId", -1);

                mDatabaseEver.editTitle(Integer.toString(id), title_editText.getText().toString());

                new Handler(Looper.getMainLooper()).post(() -> {

                    //Hide nav view \/ \/ \/

                    note_bottom_bar.startAnimation(bottom_nav_anim_reverse);

                    CloseAllButtons();

                });

                atHome = true;
                editor.apply();

            }).start();

            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_note_to_nav_home);


            return true;
        }

    }

    public void onClick(View v) {

        mDatabaseEver.AddNoteContent("", "");
        if (notesModels.isEmpty()) {
            ID = 1;
        } else {
            ID = notesModels.get(0).getId() + 1;
        }
        Note_Model newNoteModel = new Note_Model(ID, 0, "", "", "", "", "", "000000");
        notesModels.add(0, newNoteModel);

        addedNote = true;

        if (!notesModels.isEmpty()) {
            editor.putInt("noteId", ID);
            atHome = false;
            newNote = true;
            DeleteSave = false;
            UndoRedo = false;
            editor.apply();

        } else {
            editor.putInt("noteId", ID);
            atHome = false;
            newNote = true;
            DeleteSave = false;
            UndoRedo = false;
            editor.apply();
        }

        NoteEditorFragmentJavaFragment fragment = NoteEditorFragmentJavaFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("noteModel", newNoteModel);
        fragment.setArguments(bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Fade());
            noteScreen.setExitTransition(new Fade());
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        v.setTransitionName("card" + (ID) + 1);
        transaction.addSharedElement(v, "card");
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void ShowDrawSizeVisualizer() {

        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);

        DrawVisualizerIsShowing = true;
        // ChangeColor.setVisibility(View.GONE);

        size_visualizer.setVisibility(View.VISIBLE);
        size_visualizer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter));
        ImageSizeView.setVisibility(View.VISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            DrawVisualizerIsShowing = false;

            size_visualizer.startAnimation(fadeout);


        }, 1250);
    }

    public void ModifyDrawSizeVisualizer(int value) {

        ImageSizeView.setScaleX(value / 85F);
        ImageSizeView.setScaleY(value / 85F);

    }

    public void OnFocusChangeEditor(boolean focused) {

        if (focused) {

            new Handler(Looper.getMainLooper()).postDelayed(() -> {


                if (bottomBarShowing) {

                } else {
                    CloseOrOpenBottomNoteBar(false);
                }

            }, 450);

        } else {

            new Handler(Looper.getMainLooper()).post(() -> {

                if (DrawOn) {

                } else {
                    CloseOrOpenToolbarUndoRedo();
                }

            });
        }
    }

    public void TransformUriToFile(Uri uri, boolean addToDatabase, String fileType, String imagesUrls, int ID, int position) throws
            IOException {

        ImagesUrls = imagesUrls;

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

        File directory = getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + fileType);

        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos;

            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();


            if (addToDatabase) {

                notesModels.get(position).setImageURLS(file.toString() + "┼" + ImagesUrls.replaceAll("[\\[\\](){}]", ""));
                mDatabaseEver.insertImageToDatabase(String.valueOf(ID), file.toString(), ImagesUrls.replaceAll("[\\[\\](){}]", ""));
            }
        }
    }
    //TODO ADD OPTION TO CHANGE COLOR IN NOTE SCREEN LONG PRESS

    public void onItemClickFromRecyclerAtNotescreen(View view, View view2, View view3, View view4, int position, int ID) {
        //    SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        //   SharedPreferences.Editor editor = preferences.edit();
        NoteEditorFragmentJavaFragment fragment = NoteEditorFragmentJavaFragment.newInstance();
        Bundle bundle = new Bundle();



        bundle.putSerializable("noteModel", notesModels.get(position));
        bundle.putInt("noteID", ID);
        atHome = false;
        newNote = false;
        fragment.setArguments(bundle);
        selectedPosition = position;

        editor.putInt("noteId", ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Fade());
            noteScreen.setExitTransition(new Fade());
        }
        System.out.println("ID = " + ID + " position = " + position);
        FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.addSharedElement(view, "textRecycler");
        transaction.addSharedElement(view2, "card");
        transaction.addSharedElement(view3, "title");
        transaction.addSharedElement(view4, "imageRecycler");
        transaction.hide(noteScreen);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void ColorClickedSwitcher(String color, boolean highlight) {

        if (highlight) {

            switch (color) {

                case "Clear":

                    EverAdapter.GetActiveEditor().setTextBackgroundColor(Color.WHITE);

                    CloseOrOpenColors(true);

                    ClearHighlight.setVisibility(View.GONE);

                    break;

                case "Black":

                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.PurpleHighlight));

                    CloseOrOpenColors(true);


                    break;

                case "Blue":


                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.SkyBlueHighlight));

                    CloseOrOpenColors(true);


                    break;

                case "Purple":


                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.PinkHighlight));

                    CloseOrOpenColors(true);


                    break;

                case "Magenta":


                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.MagentaHighlight));

                    CloseOrOpenColors(true);


                    break;

                case "Orange":


                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.OrangeHighlight));

                    CloseOrOpenColors(true);


                    break;

                case "Yellow":

                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.YellowSunHighlight));

                    CloseOrOpenColors(true);


                    break;

                case "Green":

                    EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.GrassGreen));

                    CloseOrOpenColors(true);


                    break;

                default:


                    break;
            }
        } else {
            switch (color) {

                case "Black":

                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.Black)));

                    CloseOrOpenColors(false);


                    break;

                case "Blue":

                    EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.SkyBlue));

                    //  Blue = !Blue;


                    CloseOrOpenColors(false);


                    break;

                case "Purple":

                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.Pink)));

                    //  Purple = !Purple;

                    CloseOrOpenColors(false);


                    break;

                case "Magenta":

                    EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.Magenta));

                    //  Magenta = !Magenta;


                    CloseOrOpenColors(false);


                    break;

                case "Orange":


                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.Orange)));

                    //  Orange = !Orange;

                    CloseOrOpenColors(false);


                    break;

                case "Yellow":


                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.YellowSun)));

                    // Yellow = !Yellow;

                    CloseOrOpenColors(false);


                    break;

                case "Green":


                    EverAdapter.GetActiveEditor().setTextColor(GetColor((R.color.GrassGreen)));

                    // Green = !Green;

                    CloseOrOpenColors(false);


                    break;

                default:


                    break;
            }
        }
    }

    public void CloseOrOpenFormatter() {

        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter);

        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);

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

            }, 250);

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

            }, 130);

            CloseFormatter = true;

        }
    }

    public void CloseOrOpenParagraph() {

        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter);

        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);

        if (CloseParagraph) {

            spacing.setVisibility(View.GONE);
            Bullets.setVisibility(View.GONE);
            Numbers.setVisibility(View.GONE);
            Left.setVisibility(View.GONE);
            Center.setVisibility(View.GONE);
            Right.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                paragraph_selector.startAnimation(fadeout);

            }, 250);

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

            }, 130);

            CloseParagraph = true;

        }
    }

    public void CloseOrOpenImporter() {

        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter);

        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);

        if (CloseImporter) {

            GooglePhotos.setVisibility(View.GONE);
            Gallery.setVisibility(View.GONE);
            Files.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                importer_selector.startAnimation(fadeout);

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

    public void CloseOrOpenToolbarUndoRedo() {

        if (UndoRedo) {

            Undo.setVisibility(View.GONE);
            Redo.setVisibility(View.GONE);

            UndoRedo = false;

        } else {

            Undo.setVisibility(View.VISIBLE);
            Redo.setVisibility(View.VISIBLE);

            UndoRedo = true;

        }
    }

    public void CloseOrOpenBottomNoteBar(boolean inTitle) {

        if (inTitle) {
            if (bottomBarShowing) {


                note_bottom_bar.startAnimation(bottom_nav_anim_reverse);

                bottomBarShowing = false;

            } else {

                toolbar.setVisibility(View.VISIBLE);
                note_bottom_bar.startAnimation(bottom_nav_anim);

                bottomBarShowing = true;

            }
        } else {

            if (bottomBarShowing) {

                toolbar.setVisibility(View.GONE);
                Save.setVisibility(View.GONE);
                Delete.setVisibility(View.GONE);
                note_bottom_bar.startAnimation(bottom_nav_anim_reverse);

                bottomBarShowing = false;

            } else {

                toolbar.setVisibility(View.VISIBLE);
                Save.setVisibility(View.VISIBLE);
                Delete.setVisibility(View.VISIBLE);
                note_bottom_bar.setVisibility(View.VISIBLE);
                note_bottom_bar.startAnimation(bottom_nav_anim);

                bottomBarShowing = true;

            }
        }
    }

    public void CloseOrOpenColors(Boolean highlight) {

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

    public void CloseOrOpenDraWOptions(int height) {

        noteCreator.drawFromRecycler = false;

        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter);

        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);

        if (CloseOpenedDrawOptions) {

            DeleteSave = false;
            UndoRedo = false;

            ResizeCardViewToWrapContent();

            CloseOrOpenToolbarUndoRedo();

            CloseOrOpenNoteContents();

            DrawOptions.startAnimation(fadeout);

            if (CloseOpenedDrawColors) {
                CloseOrOpenDrawColors();
            }
            if (CloseOpenedDrawSize) {
                CloseOrOpenDrawSize();
            }

            //  evermindEditor.setVisibility(View.VISIBLE);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                DrawChangeSize.setVisibility(View.GONE);
                DrawChangeColor.setVisibility(View.GONE);

                DrawOn = false;

                editor.putBoolean("DrawOn", false);
                editor.apply();

            }, 100);

            CloseOpenedDrawOptions = false;

        } else {

            DrawOptions.setVisibility(View.VISIBLE);

            DrawOptions.startAnimation(fadein);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                if (bottomBarShowing) {

                } else {
                    CloseOrOpenBottomNoteBar(false);
                }

                ResizeEverDrawToPrepareNoteToDraw(height);

                CloseOrOpenToolbarUndoRedo();

                CloseOrOpenNoteContents();

                DrawChangeSize.setVisibility(View.VISIBLE);
                DrawChangeColor.setVisibility(View.VISIBLE);

                DrawOn = true;

                editor.putBoolean("DrawOn", true);
                editor.apply();

            }, 100);
            CloseOpenedDrawOptions = true;
        }
    }

    public void CloseOrOpenDraWOptionsFromRecycler(EverFlowScrollView scroll, RecyclerView recyclerView) {

        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter);

        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);

        if (CloseOpenedDrawOptions) {

            DeleteSave = false;
            UndoRedo = false;

            CloseOrOpenToolbarUndoRedo();

            DrawOptions.startAnimation(fadeout);

            if (CloseOpenedDrawColors) {
                CloseOrOpenDrawColors();
            }
            if (CloseOpenedDrawSize) {
                CloseOrOpenDrawSize();
            }


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                DrawChangeSize.setVisibility(View.GONE);
                DrawChangeColor.setVisibility(View.GONE);

                DrawOn = false;

                scroll.setCanScroll(true);
                recyclerView.suppressLayout(false);

                editor.putBoolean("DrawOn", false);
                editor.apply();

            }, 100);

            CloseOpenedDrawOptions = false;

        } else {

            DrawOptions.setVisibility(View.VISIBLE);

            DrawOptions.startAnimation(fadein);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                if (bottomBarShowing) {

                } else {
                    CloseOrOpenBottomNoteBar(false);
                }

                CloseOrOpenToolbarUndoRedo();

                DrawChangeSize.setVisibility(View.VISIBLE);
                DrawChangeColor.setVisibility(View.VISIBLE);

                DrawOn = true;

                scroll.setCanScroll(false);
                recyclerView.suppressLayout(true);

                editor.putBoolean("DrawOn", true);
                editor.apply();

            }, 100);
            CloseOpenedDrawOptions = true;
        }
    }

    public void CloseOrOpenDrawSize() {

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

    public void CloseOrOpenDrawColors() {

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

    public void CloseAllButtons() {
        if (CloseOpenedDrawOptions) {
            CloseOrOpenDraWOptions(0);
        }
        if (CloseOpenedDrawColors) {
            CloseOrOpenDrawColors();
        }
        if (CloseOpenedDrawSize) {
            CloseOrOpenDrawSize();
        }
        if (CloseFormatter) {
            CloseOrOpenFormatter();
        }
        if (CloseParagraph) {
            CloseOrOpenParagraph();
        }
        if (CloseImporter) {
            CloseOrOpenImporter();
        }
        if (bottomBarShowing) {
            CloseOrOpenBottomNoteBar(false);
        }
        if (UndoRedo) {
            CloseOrOpenToolbarUndoRedo();
        }

        InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(toolbar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }

    private void CloseOrOpenNoteContents() {

        EditText TitleTextBox = findViewById(R.id.TitleTextBox);
        RecyclerView recyclerViewImage = findViewById(R.id.ImagesRecycler);
        RecyclerView textanddrawRecyclerView = findViewById(R.id.TextAndDrawRecyclerView);
        EverDraw everDraw = findViewById(R.id.EverDraw);


        if (showNoteContents) {

            TitleTextBox.setVisibility(View.VISIBLE);
            recyclerViewImage.setVisibility(View.VISIBLE);
            textanddrawRecyclerView.setVisibility(View.VISIBLE);
            everDraw.setVisibility(View.GONE);

            showNoteContents = false;

        } else {

            TitleTextBox.setVisibility(View.GONE);
            recyclerViewImage.setVisibility(View.GONE);
            textanddrawRecyclerView.setVisibility(View.GONE);
            everDraw.setVisibility(View.VISIBLE);

            showNoteContents = true;
        }
    }

    private void ResizeEverDrawToPrepareNoteToDraw(int height) {

        CardView cardView = findViewById(R.id.card_note_creator);

        EverDraw everDraw = findViewById(R.id.EverDraw);

        TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                .addTransition(new ChangeBounds()));

        ViewGroup.LayoutParams params = everDraw.getLayoutParams();

        params.height = height;
        everDraw.setVisibility(View.VISIBLE);

        everDraw.setLayoutParams(params);
    }

    private void ResizeCardViewToWrapContent() {

        CardView cardView = findViewById(R.id.card_note_creator);

        TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                .addTransition(new ChangeBounds()));

        ViewGroup.LayoutParams params = cardView.getLayoutParams();

        params.height = WRAP_CONTENT;

        cardView.setLayoutParams(params);
    }

    public void OpenCustomizationPopup(View view, int id, int position) {

        View popView = LayoutInflater.from(this).inflate(R.layout.note_customization_layout, null);
        popupWindowHelper = new PopupWindowHelper(popView);
        popupWindowHelper.showAsDropDown(view);
        selectedID = id;
        Toast.makeText(this, "id is = " + id + "position = " + position, Toast.LENGTH_SHORT).show();
        selectedPosition = position;
    }

    public void clickToOpenCustomize(View view) {


        View popView = LayoutInflater.from(this).inflate(R.layout.note_customization_color_layout, null);
        popupWindowHelperColor = new PopupWindowHelper(popView);
        popupWindowHelperColor.showAsDropDown(view, 0, 10);

    }

    public void clickToCustomize(View view) {
        switch (view.getTag().toString()) {
            case "black":
                ChangeNoteColor(GetColor(R.color.Black));
                break;

            case "white":
                ChangeNoteColor(GetColor(R.color.White));
                break;

            case "magenta":
                ChangeNoteColor(GetColor(R.color.Magenta));
                break;

            case "pink":
                ChangeNoteColor(GetColor(R.color.Pink));
                break;

            case "orange":
                ChangeNoteColor(GetColor(R.color.Orange));
                break;

            case "blue":
                ChangeNoteColor(GetColor(R.color.SkyBlue));
                break;

            case "yellow":
                ChangeNoteColor(GetColor(R.color.YellowSun));
                break;

            case "green":
                ChangeNoteColor(GetColor(R.color.GrassGreen));
                break;
        }
        popupWindowHelperColor.dismiss();
    }

    public void deleteNote(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {

                            popupWindowHelper.dismiss();

                            mDatabaseEver.deleteNote(selectedID);

                            notesModels.remove(selectedPosition);
                            noteScreen.adapter.notifyItemRemoved(selectedPosition);
                        }
                )
                .setNegativeButton("No", null)
                .show();

    }

    public void ChangeNoteColor(int color) {
        mDatabaseEver.editColor(String.valueOf(selectedID), String.valueOf(color));
        notesModels.get(selectedPosition).setNoteColor(String.valueOf(color));
        recyclertest.removeViewAt(selectedPosition);
    }
}

