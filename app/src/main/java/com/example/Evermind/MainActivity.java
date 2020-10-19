package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
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
import com.koushikdutta.ion.Ion;
import com.muehlemann.giphy.GiphyLibrary;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    public BottomNavigationView note_bottom_bar;
    public Animation bottom_nav_anim;
    public Animation bottom_nav_anim_reverse;
    public Boolean DrawVisualizerIsShowing = false;
    public Boolean DrawOn = false;
    private CardView size_visualizer;
    private ImageView ImageSizeView;
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
           // new ARE_BackgroundColor(ChangeColor, R.color.Magenta);
            DrawChangeColor = findViewById(R.id.DrawChangeColor);
            DrawChangeSize = findViewById(R.id.DrawChangeSize);
            size_visualizer = findViewById(R.id.draw_sizeVisualizerCardView);
            ImageSizeView = findViewById(R.id.draw_size_visualizer);
            DrawOptions = findViewById(R.id.draw_options);
            seekBarDrawSize = findViewById(R.id.draw_size_seekbar);
            BlackDraw = findViewById(R.id.Drawblack);
            BlueDraw = findViewById(R.id.Drawblue);
            PurpleDraw = findViewById(R.id.Drawpurple);
            MagentaDraw = findViewById(R.id.Drawmagenta);
            OrangeDraw = findViewById(R.id.Draworange);
            YellowDraw = findViewById(R.id.Drawyellow);
            GreenDraw = findViewById(R.id.Drawgreen);
            DrawOptions = findViewById(R.id.draw_options);
            size_visualizer = findViewById(R.id.draw_sizeVisualizerCardView);
            ImageSizeView = findViewById(R.id.draw_size_visualizer);
            Undo = findViewById(R.id.Undo);
            Redo = findViewById(R.id.Redo);
            Delete = findViewById(R.id.Delete);
            Save = findViewById(R.id.Save);
            Bullets = findViewById(R.id.Bullets);
            Numbers = findViewById(R.id.Numbers);
            spacing = findViewById(R.id.paragraph_spacing);
            toolbar = findViewById(R.id.toolbar);
            scrollView1 = findViewById(R.id.scroll_draw);
            //OverScrollDecoratorHelper.setUpOverScroll(scrollView1);


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



            DrawChangeColor.setOnClickListener(view -> CloseOrOpenDrawColors());

            DrawChangeSize.setOnClickListener(view -> CloseOrOpenDrawSize());






            note_bottom_bar.setOnNavigationItemSelectedListener(item -> {
                int id_nav = item.getItemId();


                switch (id_nav) {
                    case R.id.nav_formatText:

                        View popView = LayoutInflater.from(this).inflate(R.layout.format_popup, null);
                        popupWindowHelper = new PopupWindowHelper(popView);
                        popupWindowHelper.showAsPopUp(note_bottom_bar);

                        break;

                    case R.id.nav_paragraph:

                        View popView2 = LayoutInflater.from(this).inflate(R.layout.paragraph_popup, null);
                        popupWindowHelper = new PopupWindowHelper(popView2);
                        popupWindowHelper.showAsPopUp(note_bottom_bar, -50, 0);

                        break;

                    case R.id.nav_checkbox:

                        View popView3 = LayoutInflater.from(this).inflate(R.layout.importer_popup, null);
                        popupWindowHelper = new PopupWindowHelper(popView3);
                        popupWindowHelper.showAsPopUp(note_bottom_bar, -100, 0);

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
        FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
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

    public void onItemClickFromRecyclerAtNotescreen(View view, View view2, View view3, View view4, View view5, int position, int ID) {
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
        transaction.addSharedElement(view5, "htmltext");
        transaction.hide(noteScreen);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    public void formatClick(View view) {
        switch (view.getTag().toString()) {
            case "increaseSize":
                if (size < 7) {

                    size++;
                    EverAdapter.GetActiveEditor().setEditorFontSize(size);
                }
                break;

            case "decreaseSize":
                if (size > 3) {

                    size--;
                    EverAdapter.GetActiveEditor().setEditorFontSize(size);
                }
                break;

            case "changeColor":
                View popView = LayoutInflater.from(this).inflate(R.layout.color_change_popup, null);
                popupWindowHelperColor = new PopupWindowHelper(popView);
                popupWindowHelperColor.showAsPopUp(note_bottom_bar, 50, -180);
                break;

            case "bold":
                EverAdapter.GetActiveEditor().setBold();
                break;

            case "italic":
                EverAdapter.GetActiveEditor().setItalic();
                break;

            case "underline":
                EverAdapter.GetActiveEditor().setUnderline();
                break;

            case "striketrough":
                EverAdapter.GetActiveEditor().setStrikeThrough();
                break;

            case "highlight":
                View popView2 = LayoutInflater.from(this).inflate(R.layout.highlight_color_change_popup, null);
                popupWindowHelperColor = new PopupWindowHelper(popView2);
                popupWindowHelperColor.showAsPopUp(note_bottom_bar, 50, -180);
                break;
            case "clearHighlight":
               EverAdapter.GetActiveEditor().setTextBackgroundColor(Color.WHITE);
                break;
        }
       // popupWindowHelper.dismiss();
    }

    public void colorChangeClick(View view) {
        switch (view.getTag().toString()) {
            case "black":
                EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.Black));
                break;

            case "white":
                EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.White));
                break;

            case "magenta":
                EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.Magenta));
                break;

            case "purple":
                EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.Pink));
                break;

            case "orange":
                EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.Orange));
                break;

            case "blue":
                EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.SkyBlue));
                break;

            case "yellow":
                EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.YellowSun));
                break;

            case "green":
                EverAdapter.GetActiveEditor().setTextColor(GetColor(R.color.GrassGreen));
                break;

        }
        popupWindowHelperColor.dismiss();
    }

    public void highlightColorChangeClick(View view) {
        switch (view.getTag().toString()) {
            case "blackHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.Black));
                break;

            case "whiteHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.White));
                break;

            case "magentaHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.Magenta));
                break;

            case "purpleHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.Pink));
                break;

            case "orangeHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.Orange));
                break;

            case "blueHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.SkyBlue));
                break;

            case "yellowHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.YellowSun));
                break;

            case "greenHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(GetColor(R.color.GrassGreen));
                break;

            case "clearHighlight":
                EverAdapter.GetActiveEditor().setTextBackgroundColor(Color.WHITE);
                break;

        }
        popupWindowHelperColor.dismiss();
    }

    public void paragraphClick(View view) {
        switch (view.getTag().toString()) {
            case "numbers":
                EverAdapter.GetActiveEditor().setNumbers();
                break;

            case "bullets":
                EverAdapter.GetActiveEditor().setBullets();
                break;

            case "alignLeft":
                EverAdapter.GetActiveEditor().setAlignLeft();
                break;

            case "alignCenter":
                EverAdapter.GetActiveEditor().setAlignCenter();
                break;

            case "alignRight":
                EverAdapter.GetActiveEditor().setAlignRight();
                break;

        }
        popupWindowHelper.dismiss();
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
        popupWindowHelper.dismiss();
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
    public @NonNull static Bitmap createBitmapFromView(@NonNull View view, int width, int height) {
        if (width > 0 && height > 0) {
            view.measure(View.MeasureSpec.makeMeasureSpec(DynamicUnitUtils
                            .convertDpToPixels(width), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(DynamicUnitUtils
                            .convertDpToPixels(height), View.MeasureSpec.EXACTLY));
        }
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable background = view.getBackground();

        if (background != null) {
            background.draw(canvas);
        }
        view.draw(canvas);

        return bitmap;
    }

    public void ClearIonCache() {
        Ion.getDefault(this).getBitmapCache().clear();
        Ion.getDefault(this).getCache().clear();
    }
}

