package com.example.Evermind;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
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

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.blurry.Blurry;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    public EverDataBase mDatabaseEver;
    public Integer ID;
    public Note_Model actualNote;
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
    public BottomNavigationView note_bottom_bar;
    public Animation bottom_nav_anim;
    public Animation bottom_nav_anim_reverse;
    public Boolean DrawVisualizerIsShowing = false;
    public Boolean DrawOn = false;
    public NotesScreen noteScreen;
    public boolean atHome = true;
    public boolean newNote = false;
    public int selectedPosition;
    public int selectedID;
    public CardView cardNoteCreator;
    public NoteEditorFragmentJavaFragment noteCreator;
    public RecyclerView contentRecycler;
    public TextView title;
    public RecyclerView imageRecycler;
    //   public ArrayList<Note_Model> notesModels = new ArrayList<>();
    public ArrayList<String> names = new ArrayList<>();
    public int noteIdIncrement = 0;
    public EverAdapter everAdapter;
    public Window everMainWindow;
    public ImageButton drawColor;
    public ImageButton drawSize;
    public ListSection<Note_Model> noteModelSection = new ListSection<>();
    public MultiViewAdapter adapter = new MultiViewAdapter();
    public RecyclerView recyclertest;
    private AppBarConfiguration mAppBarConfiguration;
    private HorizontalScrollView scrollView1;
    private ImageButton DrawChangeColor;
    private ImageButton DrawChangeSize;
    private CardView DrawOptions;
    private CardView size_visualizer;
    private ImageView ImageSizeView;
    private Toolbar toolbar;
    private int size = 4;
    private EverPopup popupWindowHelperColor;
    private EverPopup popupWindowHelper;
    public int defaultToolbarColor;
    private boolean toolbarDown = false;
    private boolean bottomBarUp = false;
    private boolean cardDown = false;

    public @NonNull
    static Bitmap createBitmapFromView(@NonNull View view, int width, int height) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabaseEver = new EverDataBase(this);

        everMainWindow = getWindow();


        //TODO: optimize performance a little more and make sure everything that we changed is working as intended

        if (getIntent().hasExtra("notes")) {
            //   notesModels = (ArrayList<Note_Model>) getIntent().getSerializableExtra("notes");
            noteModelSection.addAll((ArrayList<Note_Model>) getIntent().getSerializableExtra("notes"));
            noteScreen = new NotesScreen();
            if (noteModelSection.size() > 0) {
                noteIdIncrement = noteModelSection.get(0).getId();
            }
        }
        adapter.registerItemBinders(new NoteModelBinder(this));
        adapter.addSection(noteModelSection);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper()).post(() -> {

            //  giphyLibrary = new GiphyLibrary();

            Fresco.initialize(this);

        });

        atHome = true;

        //   new Thread(() -> {
        // a potentially time consuming task

        note_bottom_bar = findViewById(R.id.note_bottom_bar);
        bottom_nav_anim = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim);
        bottom_nav_anim_reverse = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim_reverse);
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
        toolbar = findViewById(R.id.toolbar);
        scrollView1 = findViewById(R.id.scroll_draw);
        drawColor = findViewById(R.id.drawColor);
        drawSize = findViewById(R.id.drawSize);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView1);

        defaultToolbarColor = toolbar.getBackgroundTintList().getDefaultColor();


        new Handler(Looper.getMainLooper()).post(() -> {

            setSupportActionBar(toolbar);

            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


            //   DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_note)
                    //  .setDrawerLayout(drawer)
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

        DrawChangeColor.setOnClickListener(view -> CloseOrOpenDrawColors(false));

        DrawChangeSize.setOnClickListener(view -> CloseOrOpenDrawSize(false));


        drawSize.setOnClickListener(v -> {
            // createPopupMenu(drawColor, R.layout.draw_size_popup, true, "dropdown", 0,0);
            View popView = LayoutInflater.from(MainActivity.this).inflate(R.layout.draw_size_popup, null);
            popupWindowHelperColor = new EverPopup(popView);
            popupWindowHelperColor.showAsDropDown(drawColor);
            SeekBar seekBar = popView.findViewById(R.id.seekBar2);
            //TODO TRY TO USE THE DRAW OPTIONS FROM HOME SCREEN BUTTONS NOT POPUP PLEASE FUTRURE PREDO
            ImageButton imageButton = popView.findViewById(R.id.drawSizeVisualizer);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int amount = progress / 2;
                    imageButton.setScaleY(amount);
                    imageButton.setScaleX(amount);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        });

        drawColor.setOnClickListener(v -> createPopupMenu(v, R.layout.draw_options_popup, true, "dropdown", 0, 0));

        note_bottom_bar.setOnNavigationItemSelectedListener(item -> {
            int id_nav = item.getItemId();


            switch (id_nav) {
                case R.id.nav_formatText:
                    createPopupMenu(note_bottom_bar, R.layout.format_popup, false, "popup", 0, 0);
                    break;

                case R.id.nav_paragraph:
                    createPopupMenu(note_bottom_bar, R.layout.paragraph_popup, false, "popup", 100, 0);
                    break;

                case R.id.nav_checkbox:
                    //     blurView(this, 25, 2, findViewById(R.id.homeNotesrelative));
                    createPopupMenu(drawColor, R.layout.importer_popup, false, "popup", 200, 0);
                    break;

                case R.id.nav_bullets:
                    break;

                case R.id.nav_draw:

                    CloseOrOpenDraWOptions(1400, false);
                    noteCreator.everDraw = findViewById(R.id.EverDraw);
                    InputMethodManager keyboard1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    keyboard1.hideSoftInputFromWindow(seekBarDrawSize.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                default:
                    return true;
            }
            return true;
        });

        //   }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

      //  if (!actualNote.getNoteColor().equals("000000")) {
            if (toolbar.getBackgroundTintList().getDefaultColor() != defaultToolbarColor) {
                tintSystemBars(defaultToolbarColor);
            }
      //  }

        new Handler(Looper.getMainLooper()).postDelayed(this::setLightStatusBar, 250);

        CloseAllButtons();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (!atHome) {


                if (actualNote.getTitle().equals("") && actualNote.getContent().equals("") && actualNote.getDrawLocation().equals("") && actualNote.getImageURLS().equals("")) {
                    removeNote(actualNote.getActualPosition(), actualNote.getId());
                    //  notesModels.remove(actualNote.getActualPosition());

                    System.out.println("Note with id = " + actualNote.getId() + " deleted. <-- called from OnBackPress in MainActivity, thx future pedro");
                } else {
                    updateNote(actualNote.getActualPosition(), actualNote);
                }


                //   mDatabaseEver.setNoteModel(String.valueOf(actualNote.getId()), actualNote.getTitle(), actualNote.getContent(), actualNote.getDrawLocation(), actualNote.getImageURLS(), actualNote.getNoteColor());

//                notesModels.set(actualNote.getActualPosition(), actualNote);
                System.out.println("draw in actual = " + actualNote.getDraws().size());

                FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
                beginDelayedTransition(cardNoteCreator);
                transaction.setReorderingAllowed(true);
                if (newNote) {
                    transaction.addSharedElement(cardNoteCreator, "card" + actualNote.getId());
                } else {
                    transaction.addSharedElement(cardNoteCreator, "card" + actualNote.getId());
                    transaction.addSharedElement(contentRecycler, "textRecycler" + actualNote.getId());
                    transaction.addSharedElement(title, "title" + actualNote.getId());
                    transaction.addSharedElement(imageRecycler, "imageRecycler" + actualNote.getId());
                }
                transaction.hide(noteCreator);
                transaction.replace(R.id.nav_host_fragment, noteScreen);
                transaction.show(noteScreen);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            atHome = true;

            newNote = false;
        }, 110);


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

                    ObjectAnimator transAnimation2 = ObjectAnimator.ofFloat(note_bottom_bar, "translationY", note_bottom_bar.getTranslationY(), 200);
                    transAnimation2.setDuration(500);//set duration
                    transAnimation2.start();//start animation

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

        //TODO TRY TO CREATE A FUNC IN DATABASE TO GET THE LAST CREATED ID AND TRY TO FIX THE SHARED ELEMNT IN TITLE

        //  mDatabaseEver.AddNoteContent("", "");
        noteIdIncrement++;
        //   notesModels.add(0, newNoteModel);
        actualNote = new Note_Model(noteIdIncrement, 0, "", "", "", "", "", "000000");
        selectedPosition = 0;
        addNote(actualNote);
        atHome = false;
        newNote = true;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            NoteEditorFragmentJavaFragment fragment = NoteEditorFragmentJavaFragment.newInstance();
            CardView card = recyclertest.findContainingViewHolder(recyclertest.getChildAt(0)).itemView.findViewById(R.id.mainCard);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setEnterTransition(new Fade());
                noteScreen.setExitTransition(new Fade());
            }
            FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);

            card.setTransitionName("card" + noteIdIncrement);//was +1
            names.add(card.getTransitionName());
            transaction.addSharedElement(card, card.getTransitionName());
            transaction.replace(R.id.nav_host_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }, 450);
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
    //TODO ADD OPTION TO CHANGE COLOR IN NOTE SCREEN LONG PRESS

    public void OnFocusChangeEditor(boolean focused) {
        if (focused) {
            switchToolbars(true, true, true);
        }
    }

    public void TransformUriToFile(Uri uri, boolean addToDatabase, String fileType, String imagesUrls, int ID, int position) throws
            IOException {

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

                actualNote.setImageURLS(file.toString() + "┼" + imagesUrls.replaceAll("[\\[\\](){}]", ""));
                //   noteModelSection.get(position).setImageURLS(file.toString() + "┼" + imagesUrls.replaceAll("[\\[\\](){}]", ""));
                // mDatabaseEver.insertImageToDatabase(String.valueOf(ID), file.toString(), imagesUrls.replaceAll("[\\[\\](){}]", ""));
                updateNote(position, actualNote);
            }
        }
    }

    public void onItemClickFromNoteScreen(View view, View view2, View view3, View view4, int position, Note_Model actualNote) {

    //    NoteEditorFragmentJavaFragment fragment = NoteEditorFragmentJavaFragment.newInstance();
        NoteEditorFragmentJavaFragment fragment = new NoteEditorFragmentJavaFragment();

        this.ID = actualNote.getId();
        this.actualNote = actualNote;
        atHome = false;
        newNote = false;
        names.add(view.getTransitionName());
        names.add(view2.getTransitionName());
        names.add(view3.getTransitionName());
        names.add(view4.getTransitionName());
        selectedPosition = position;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Fade());
            noteScreen.setExitTransition(new Fade());
        }
        System.out.println("ID = " + ID + " position = " + position);
        FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        //TODO FIX SHRD ANIMATION NOT WORKING BECAUSE WE CHANGE ADAPTER FIX IT
        transaction.addSharedElement(view, view.getTransitionName());
        transaction.addSharedElement(view2, view2.getTransitionName());
        transaction.addSharedElement(view3, view3.getTransitionName());
        transaction.addSharedElement(view4, view4.getTransitionName());
        transaction.hide(noteScreen);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void CloseOrOpenToolbarUndoRedo(boolean UndoRedo) {
        if (UndoRedo) {
            Undo.setVisibility(View.INVISIBLE);
            Redo.setVisibility(View.INVISIBLE);
        } else {
            Undo.setVisibility(View.VISIBLE);
            Redo.setVisibility(View.VISIBLE);
        }
    }

    public void switchToolbars(boolean show, boolean bottomToolbar, boolean showUndoRedo) {

        if (show) {
            System.out.println("show");
            if (bottomToolbar) {
                if (!bottomBarUp) {
                    System.out.println("bottom bar up");
                    animateObject(note_bottom_bar, "translationY", 0, 500);
                    bottomBarUp = true;
                }
            } else {
                System.out.println("bottom bar down");
                animateObject(note_bottom_bar, "translationY", 200, 500);
                bottomBarUp = false;
            }
            if (!toolbarDown) {
                System.out.println("toolbar down");
                animateObject(toolbar, "translationY", 0, 500);
                toolbarDown = true;
            }
            if (!cardDown) {
                System.out.println("card down");
                animateObject(cardNoteCreator, "translationY", 200, 500);
                cardDown = true;
            }

            CloseOrOpenToolbarUndoRedo(!showUndoRedo);

        } else {
            System.out.println("hide");
            animateObject(toolbar, "translationY", -200, 150);
            animateObject(note_bottom_bar, "translationY", 200, 150);
            animateObject(cardNoteCreator, "translationY", 30, 150);
            toolbarDown = false;
            bottomBarUp = false;
            cardDown = false;
        }
    }

    public void CloseOrOpenDraWOptions(int height, boolean close) {

        noteCreator.drawFromRecycler = false;

        if (close) {

            //    TransitionManager.beginDelayedTransition(cardNoteCreator, new TransitionSet()
            //           .addTransition(new ChangeBounds()));

            ResizeCardViewToWrapContent();

            CloseOrOpenNoteContents();

            animateObject(DrawOptions, "translationY", 200, 500);

            if (!actualNote.getNoteColor().equals("000000")) {
                cardNoteCreator.setCardBackgroundColor(Integer.parseInt(actualNote.getNoteColor()));
            }

            CloseOrOpenDrawColors(close);

            CloseOrOpenDrawSize(close);

            DrawOn = false;

        } else {

            animateObject(DrawOptions, "translationY", -200, 500);

            ResizeEverDrawToPrepareNoteToDraw(height);

            cardNoteCreator.setCardBackgroundColor(defaultToolbarColor);

            CloseOrOpenNoteContents();

            DrawOn = true;

            // editor.putBoolean("DrawOn", true);
            // editor.apply();
        }
    }

    public void CloseOrOpenDraWOptionsFromRecycler(EverFlowScrollView scroll, RecyclerView recyclerView, boolean close) {


        if (close) {

           // noteCreator.everDraw.setVisibility(View.GONE);
            animateObject(DrawOptions, "translationY", 200, 500);
            CloseOrOpenDrawColors(close);
            CloseOrOpenDrawSize(close);
            DrawOn = false;
            scroll.setCanScroll(true);
            recyclerView.suppressLayout(false);

        } else {

            animateObject(DrawOptions, "translationY", -200, 500);

             switchToolbars(true, false, true);

            DrawOn = true;
            scroll.setCanScroll(false);
            recyclerView.suppressLayout(true);
        }
    }

    public void CloseOrOpenDrawSize(boolean CloseOpenedDrawSize) {

        if (CloseOpenedDrawSize) {

            DrawChangeColor.setVisibility(View.VISIBLE);
            DrawChangeSize.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // ChangeColor.setVisibility(View.GONE);

                seekBarDrawSize.setVisibility(View.GONE);

            }, 100);

        } else {

            DrawChangeColor.setVisibility(View.GONE);
            DrawChangeSize.setVisibility(View.VISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> seekBarDrawSize.setVisibility(View.VISIBLE), 100);
        }
    }

    public void CloseOrOpenDrawColors(boolean CloseOpenedDrawColors) {

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
        }
    }

    public void CloseAllButtons() {

        if (bottomBarUp) {
            switchToolbars(false, false, false);
        } else {
            if (toolbarDown) {
                switchToolbars(false, false, false);
            }
        }

        InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(toolbar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (seekBarDrawSize.getVisibility() == View.VISIBLE) {
            CloseOrOpenDrawSize(true);
        }

        if (BlackDraw.getVisibility() == View.VISIBLE) {
            CloseOrOpenDrawColors(true);
        }

        if (DrawOptions.getVisibility() == View.VISIBLE) {
            //    CloseOrOpenDraWOptions(0, true);
        }
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

        beginDelayedTransition(cardView);

        ViewGroup.LayoutParams params = everDraw.getLayoutParams();

        params.height = height;
        everDraw.setVisibility(View.VISIBLE);

        everDraw.setLayoutParams(params);
    }

    private void ResizeCardViewToWrapContent() {

        CardView cardView = findViewById(R.id.card_note_creator);

        beginDelayedTransition(cardView);

        ViewGroup.LayoutParams params = cardView.getLayoutParams();

        params.height = WRAP_CONTENT;

        cardView.setLayoutParams(params);
    }

    public void clickToOpenCustomize(View view) {
        createPopupMenu(note_bottom_bar, R.layout.note_customization_color_layout, true, "dropdown", 0, 10);
    }

    public void formatClick(View view) {
        switch (view.getTag().toString()) {
            case "increaseSize":
                if (size < 7) {

                    size++;
                    noteCreator.activeEditor.setFontSize(size);
                }
                break;

            case "decreaseSize":
                if (size > 3) {

                    size--;
                    noteCreator.activeEditor.setFontSize(size);
                }
                break;

            case "changeColor":
                createPopupMenu(note_bottom_bar, R.layout.color_change_popup, true, "popup", 50, -180);
                break;

            case "bold":
                noteCreator.activeEditor.setBold();
                break;

            case "italic":
                noteCreator.activeEditor.setItalic();
                break;

            case "underline":
                noteCreator.activeEditor.setUnderline();
                break;

            case "striketrough":
                noteCreator.activeEditor.setStrikeThrough();
                break;

            case "highlight":
                createPopupMenu(note_bottom_bar, R.layout.highlight_color_change_popup, true, "popup", 50, -180);
                break;
            case "clearHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(Color.WHITE);
                break;
        }
        // popupWindowHelper.dismiss();
    }

    public void colorChangeClick(View view) {
        switch (view.getTag().toString()) {
            case "black":
                noteCreator.activeEditor.setTextColor(GetColor(R.color.Black));
                break;
            case "white":
                noteCreator.activeEditor.setTextColor(GetColor(R.color.White));
                break;
            case "magenta":
                noteCreator.activeEditor.setTextColor(GetColor(R.color.Magenta));
                break;
            case "purple":
                noteCreator.activeEditor.setTextColor(GetColor(R.color.Pink));
                break;
            case "orange":
                noteCreator.activeEditor.setTextColor(GetColor(R.color.Orange));
                break;
            case "blue":
                noteCreator.activeEditor.setTextColor(GetColor(R.color.SkyBlue));
                break;
            case "yellow":
                noteCreator.activeEditor.setTextColor(GetColor(R.color.YellowSun));
                break;
            case "green":
                noteCreator.activeEditor.setTextColor(GetColor(R.color.GrassGreen));
                break;
        }
        popupWindowHelperColor.dismiss();
        Blurry.delete(findViewById(R.id.homeNotesrelative));
    }

    public void drawColorChangeClick(View view) {

        if (DrawOn) {
            switch (view.getTag().toString()) {
                case "black":
                    noteCreator.everDraw.setColor(GetColor(R.color.Black));
                    break;

                case "white":
                    noteCreator.everDraw.setColor(GetColor(R.color.White));
                    break;

                case "magenta":
                    noteCreator.everDraw.setColor(GetColor(R.color.Magenta));
                    break;

                case "purple":
                    noteCreator.everDraw.setColor(GetColor(R.color.Pink));
                    break;

                case "orange":
                    noteCreator.everDraw.setColor(GetColor(R.color.Orange));
                    break;

                case "blue":
                    noteCreator.everDraw.setColor(GetColor(R.color.SkyBlue));
                    break;

                case "yellow":
                    noteCreator.everDraw.setColor(GetColor(R.color.YellowSun));
                    break;

                case "green":
                    noteCreator.everDraw.setColor(GetColor(R.color.GrassGreen));
                    break;
            }

        } else {
            switch (view.getTag().toString()) {
                case "black":
                    tintSystemBars(GetColor(R.color.Black));
                    actualNote.setNoteColor(String.valueOf(R.color.Black));
                    break;

                case "white":
                    tintSystemBars(GetColor(R.color.White));
                    actualNote.setNoteColor(String.valueOf(GetColor(R.color.White)));
                    break;

                case "magenta":
                    tintSystemBars(GetColor(R.color.Magenta));
                    actualNote.setNoteColor(String.valueOf(GetColor(R.color.Magenta)));
                    break;

                case "purple":
                    tintSystemBars(GetColor(R.color.Pink));
                    actualNote.setNoteColor(String.valueOf(GetColor(R.color.Pink)));
                    break;

                case "orange":
                    tintSystemBars(GetColor(R.color.Orange));
                    actualNote.setNoteColor(String.valueOf(GetColor(R.color.Orange)));
                    break;

                case "blue":
                    tintSystemBars(GetColor(R.color.SkyBlue));
                    actualNote.setNoteColor(String.valueOf(GetColor(R.color.SkyBlue)));
                    break;

                case "yellow":
                    tintSystemBars(GetColor(R.color.YellowSun));
                    actualNote.setNoteColor(String.valueOf(GetColor(R.color.YellowSun)));
                    break;

                case "green":
                    tintSystemBars(GetColor(R.color.GrassGreen));
                    actualNote.setNoteColor(String.valueOf(GetColor(R.color.GrassGreen)));
                    break;
            }

        }
        popupWindowHelperColor.dismiss();
//        Blurry.delete(findViewById(R.id.homeNotesrelative));
    }

    public void highlightColorChangeClick(View view) {
        switch (view.getTag().toString()) {
            case "blackHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(GetColor(R.color.Black));
                break;

            case "whiteHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(GetColor(R.color.White));
                break;

            case "magentaHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(GetColor(R.color.Magenta));
                break;

            case "purpleHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(GetColor(R.color.Pink));
                break;

            case "orangeHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(GetColor(R.color.Orange));
                break;

            case "blueHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(GetColor(R.color.SkyBlue));
                break;

            case "yellowHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(GetColor(R.color.YellowSun));
                break;

            case "greenHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(GetColor(R.color.GrassGreen));
                break;

            case "clearHighlight":
                noteCreator.activeEditor.setTextBackgroundColor(Color.WHITE);
                break;

        }
        popupWindowHelperColor.dismiss();
        Blurry.delete(findViewById(R.id.homeNotesrelative));
    }

    public void paragraphClick(View view) {
        switch (view.getTag().toString()) {
            case "numbers":
                noteCreator.activeEditor.setNumbers();
                break;

            case "bullets":
                noteCreator.activeEditor.setBullets();
                break;

            case "alignLeft":
                noteCreator.activeEditor.setAlignLeft();
                break;

            case "alignCenter":
                noteCreator.activeEditor.setAlignCenter();
                break;

            case "alignRight":
                noteCreator.activeEditor.setAlignRight();
                break;

        }
        popupWindowHelper.dismiss();
    }

    public void importerClick(View view) {
        noteCreator.importerClick(view, popupWindowHelper);
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
        Blurry.delete(findViewById(R.id.homeNotesrelative));
        popupWindowHelperColor.dismiss();
    }

    public void ChangeNoteColor(int color) {
        mDatabaseEver.editColor(String.valueOf(selectedID), String.valueOf(color));
        //  notesModels.get(selectedPosition).setNoteColor(String.valueOf(color));
        noteModelSection.get(selectedPosition).setNoteColor(String.valueOf(color));
        updateNote(selectedPosition, noteModelSection.get(selectedPosition));
    }

    public void ClearIonCache() {
        Ion.getDefault(this).getBitmapCache().clear();
        Ion.getDefault(this).getCache().clear();
    }

    public void tintSystemBars(int color) {

        setDarkStatusBar();

        // Initial colors of each system bar.
        final int statusBarColor = everMainWindow.getStatusBarColor();
        // final int toolbarColor = everMainWindow.getStatusBarColor();

        // Desired final colors of each bar.
        final int statusBarToColor = color;
        //  final int toolbarToColor = color;

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(animation -> {
            // Use animation position to blend colors.
            float position = animation.getAnimatedFraction();

            // Apply blended color to the status bar.
            int blended = blendColors(statusBarColor, statusBarToColor, position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                everMainWindow.setStatusBarColor(blended);
            }

            // Apply blended color to the ActionBar.
            //   blended = blendColors(toolbarColor, toolbarToColor, position);
            //  ColorDrawable background = new ColorDrawable(blended);
            //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
            toolbar.setBackgroundTintList(ColorStateList.valueOf(blended));
            note_bottom_bar.setBackgroundTintList(ColorStateList.valueOf(blended));
            cardNoteCreator.setBackgroundTintList(ColorStateList.valueOf(blended));
        });

        anim.setDuration(500).start();
    }

    public int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }

    private void animationTimer(int originalPosition, int finalPosition) {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(animation -> {
            // Use animation position to blend colors.
            float position = animation.getAnimatedFraction();

            // Apply blended color to the status bar.
            int blended = goToValue(originalPosition, finalPosition, position);

            cardNoteCreator.setTranslationY(blended);
        });

        anim.setDuration(500).start();
    }

    private int goToValue(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;
        final float r = to * ratio + from * inverseRatio;
        return (int) r;
    }

    private void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
            getWindow().getDecorView().setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.GRAY); // optional
        }
    }

    private void setDarkStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
            flags = flags ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // use XOR here for remove LIGHT_STATUS_BAR from flags
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    public void animateObject(View view, String effect, int amount, int duration) {
        ObjectAnimator transAnimation2 = ObjectAnimator.ofFloat(view, effect, view.getTranslationY(), amount);
        transAnimation2.setDuration(duration);//set duration
        transAnimation2.start();//start animation
    }

    public void swipeItemsListener(View view, Note_Model noteModel) {
        selectedPosition = noteModel.getActualPosition();
        selectedID = noteModel.getId();
        switch (view.getTag().toString()) {
            case "changeColor":
                createPopupMenu(view, R.layout.swipe_color_change_popup, true, "dropdown", 0, 0);
                break;

            case "delete":
                deleteNoteDialog(noteModel.getActualPosition(), noteModel.getId());
                break;
        }
    }

    public void deleteNoteDialog(int deletePosition, int ID) {
        blurView(this, 25, 2, findViewById(R.id.homeNotesrelative));

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this note!")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(sDialog -> {
                    sDialog
                            .setTitleText("Deleted!")
                            .setContentText("Your note in position: " + deletePosition + ", with ID: " + ID + " was deleted.")
                            .setConfirmText("OK")
                            .setConfirmClickListener(sweetAlertDialog -> sDialog.dismissWithAnimation())
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    sDialog.setOnCancelListener(dialog -> Blurry.delete(findViewById(R.id.homeNotesrelative)));
                    sDialog.setOnDismissListener(dialog -> {
                        Blurry.delete(findViewById(R.id.homeNotesrelative));
                        removeNote(deletePosition, ID);
                    });
                    //  notesModels.remove(deletePosition);

                })
                .show();
    }

    private void createPopupMenu(View view, int layout, boolean assistPopup, String showAs, int xOffset, int yOffset) {


        View popView = LayoutInflater.from(this).inflate(layout, null);
        if (assistPopup) {
            popupWindowHelperColor = new EverPopup(popView);
        } else {
            popupWindowHelper = new EverPopup(popView);
        }
        switch (showAs) {
            case "dropdown":
                if (assistPopup) {
                    popupWindowHelperColor.showAsDropDown(view, xOffset, yOffset);
                } else {
                    popupWindowHelper.showAsDropDown(view, xOffset, yOffset);
                }
                break;

            case "popup":
                if (assistPopup) {
                    popupWindowHelperColor.showAsPopUp(view, xOffset, yOffset);
                } else {
                    popupWindowHelper.showAsPopUp(view, xOffset, yOffset);
                }
                break;

            case "top":
                if (assistPopup) {
                    popupWindowHelperColor.showFromTop(view);
                } else {
                    popupWindowHelper.showFromTop(view);
                }
                break;

            case "bottom":
                if (assistPopup) {
                    popupWindowHelperColor.showFromBottom(view);
                } else {
                    popupWindowHelper.showFromBottom(view);
                }
                break;
        }
    }

    public void blurView(Context context, int radius, int sampling, ViewGroup viewGroup) {
        Blurry.with(context).radius(radius).sampling(sampling).onto(viewGroup);
    }

    public void updateNote(int p, @Nullable Note_Model note) {
        noteModelSection.set(p, note);
        if (note != null) {
           System.out.println("Id =" + note.getId());
            mDatabaseEver.setNoteModel(String.valueOf(note.getId()), note.getTitle(), note.getContent(), note.getDrawLocation(), note.getImageURLS(), note.getNoteColor());
        }
        //adapter.notifyItemChanged(p);
    }

    public void removeNote(int p, int ID) {
        mDatabaseEver.deleteNote(ID);
        noteModelSection.remove(p);
        for (int position = 0; position < noteModelSection.size(); position++) {
            noteModelSection.get(position).setActualPosition(position);
        }
        // adapter.notifyItemRemoved(p);
    }

    public void addNote(Note_Model newNote) {
        //TODO FIX ANIMATION WHEN ADDING NOTE WHEN CREATING TO DO THE SHARED ELEMENT TRANSITION AND TRY TO USE ADAPTER.NOTIFIYITEMATPOSITION
        mDatabaseEver.AddNoteContent("", "");
        noteModelSection.add(0, newNote);
        for (int position = 0; position < noteModelSection.size(); position++) {
            noteModelSection.get(position).setActualPosition(position);
        }
    }

    public void beginDelayedTransition(ViewGroup view) {
        TransitionManager.beginDelayedTransition(view, new TransitionSet()
                .addTransition(new ChangeBounds()));
    }
    public void pushDownOnTouch(View view, MotionEvent event, float pushScale, int duration) {
        int i = event.getAction();
        if( i == MotionEvent.ACTION_DOWN ){
            makeDecisionAnimScale( view,
                    pushScale,
                    duration,
                    new AccelerateDecelerateInterpolator());
        }else if( i == MotionEvent.ACTION_CANCEL
                || i == MotionEvent.ACTION_UP ){
            makeDecisionAnimScale( view,
                    1f,
                    duration,
                    new AccelerateDecelerateInterpolator());
        }
    }
    public void makeDecisionAnimScale( final View view, float pushScale, long duration, TimeInterpolator interpolator){

        animScale( view, pushScale, duration, interpolator );
    }

    public void animScale(final View view, float scale, long duration, TimeInterpolator interpolator ){
        AnimatorSet scaleAnimSet = new AnimatorSet();
        view.animate().cancel();
        if( scaleAnimSet != null ){
            scaleAnimSet.cancel();
        }

        ObjectAnimator scaleX = ObjectAnimator.ofFloat( view, "scaleX", scale );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat( view, "scaleY", scale );
        scaleX.setInterpolator( interpolator );
        scaleX.setDuration( duration );
        scaleY.setInterpolator( interpolator );
        scaleY.setDuration( duration );

        scaleAnimSet = new AnimatorSet();
        scaleAnimSet
                .play( scaleX )
                .with( scaleY );
        scaleX.addListener( new AnimatorListenerAdapter(){
            @Override
            public void onAnimationStart( Animator animation ){
                super.onAnimationStart( animation );
            }

            @Override
            public void onAnimationEnd( Animator animation ){
                super.onAnimationEnd( animation );
            }
        } );
        scaleX.addUpdateListener( new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate( ValueAnimator valueAnimator ){
                View p = (View) view.getParent();
                if( p != null ) p.invalidate();
            }
        } );
        scaleAnimSet.start();
    }
}

