package com.example.Evermind;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.alimuzaffar.lib.widgets.AnimatedEditText;
import com.example.Evermind.EverAudioVisualizerHandlers.CloseAudioVisualizationHelper;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.RTManager;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTApi;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTMediaFactoryImpl;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTProxyImpl;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effects;
import com.example.Evermind.TESTEDITOR.toolbar.HorizontalRTToolbar;
import com.example.Evermind.TESTEDITOR.toolbar.RTToolbarImageButton;
import com.example.Evermind.everUtils.EverAudioHelper;
import com.example.Evermind.everUtils.EverBallsHelper;
import com.example.Evermind.everUtils.EverBitmapHelper;
import com.example.Evermind.everUtils.EverNoteManagement;
import com.example.Evermind.everUtils.EverPreferences;
import com.example.Evermind.everUtils.EverThemeHelper;
import com.example.Evermind.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.example.Evermind.ui.note_screen.NotesScreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.navigation.NavigationView;
import com.thekhaeng.pushdownanim.PushDownAnim;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.alterac.blurkit.BlurLayout;
import jp.wasabeef.blurry.Blurry;
import me.ibrahimsn.lib.SmoothBottomBar;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class MainActivity extends AppCompatActivity  {

    //VIEWS //FUNCTIONS
    public NotesScreen noteScreen;
    public SeekBar seekBarDrawSize;
    public ImageButton Undo;
    public ImageButton Redo;
    public ImageButton Delete;
    public ImageButton Save;
    public ImageButton IncreaseSize;
    public ImageButton DecreaseSize;
    public ImageButton changeColor;
    public RTToolbarImageButton Bold;
    public RTToolbarImageButton Italic;
    public RTToolbarImageButton Underline;
    public RTToolbarImageButton StrikeThrough;
    public ImageButton Highlight;
    public RTToolbarImageButton Bullets;
    public RTToolbarImageButton Numbers;
    public RTToolbarImageButton AlignLeft;
    public RTToolbarImageButton AlignCenter;
    public RTToolbarImageButton AlignRight;
    public ImageButton GooglePhotos;
    public ImageButton Files;
    public ImageButton Gallery;
    public ImageButton selectionDelete;
    public ImageButton selectionChangeColor;
    public ImageButton changeNoteColorButton;
    private ImageButton DrawChangeColor;
    private ImageButton DrawChangeSize;
    public CardView ImporterOptions;
    public CardView ParagraphOptions;
    public CardView FormatOptions;
    public CardView SelectOptions;
    public CardView AudioOptions;
    private CardView DrawOptions;
    private CardView size_visualizer;
    public SmoothBottomBar note_bottom_bar;
    public WeakReference<CardView> cardNoteCreator;
    public WeakReference<NoteEditorFragmentJavaFragment> noteCreator;
    public WeakReference<RecyclerView> contentRecycler;
    public WeakReference<EditText> title;
    public WeakReference<RecyclerView> imageRecycler;
    public WeakReference<MultiViewAdapter> adapter;
    WeakReference<AnimatedEditText> edit;
    private AppBarConfiguration mAppBarConfiguration;
    private ImageView ImageSizeView;
    private Toolbar toolbar;
    public BlurLayout blur;
    //HELPERS
    public EverBitmapHelper everBitmapHelper;
    public EverBallsHelper everBallsHelper;
    public EverNoteManagement everNoteManagement;
    private EverPopup popupWindowHelperColor;
    public RTManager mRTManager;
    public EverRecordAudio recorder;
    public EverAudioHelper audioHelper;
    public EverThemeHelper everThemeHelper;
    //LISTS
    public ArrayList<String> names = new ArrayList<>();
    private final List<View> views = new ArrayList<>();
    //IDK
    public Animation bottom_nav_anim;
    public Animation bottom_nav_anim_reverse;
    public Window everMainWindow;
    public EverPreferences prefs;
    // INT's
public int positionToScroll = 0;
    private int audioDecoySize = 0;
    private int size = 30;
    //BOOLEANS

    public boolean showNoteContents = false;
    public boolean DrawVisualizerIsShowing = false;
    public boolean drawing = false;
    public boolean atHome = true;
    public boolean newNote = false;
    private boolean bottomBarUp = false;
    private boolean formatOptions = false;
    private boolean paragraphOptions = false;
    private boolean importerOptions = false;
    private boolean drawOptions = false;
    private boolean drawsize = false;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean striketrough = false;
    private boolean numbers = false;
    private boolean bullets = false;
    private boolean isContentUp = false;
    private boolean audioOpen = false;
    private boolean pendingColorChange = false;

    public WeakReference<Note_Model> actualNote;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        postponeEnterTransition();

        everMainWindow = getWindow();

        prefs = new EverPreferences(this);

        everNoteManagement = new EverNoteManagement(this);

        //TODO: optimize performance a little more and make sure everything that we changed is working as intended

        if (getIntent().hasExtra("notes")) {
            //   notesModels = (ArrayList<Note_Model>) getIntent().getSerializableExtra("notes");
            everNoteManagement.noteModelSection.addAll(getIntent().getParcelableArrayListExtra("notes"));
            noteScreen = new NotesScreen();
            if ( everNoteManagement.noteModelSection.size() > 0) {
                everNoteManagement.noteIdIncrement = prefs.getInt("lastID", 0);
                // noteIdIncrement = noteModelSection.get(0).getId();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper()).post(() -> {

            //  giphyLibrary = new GiphyLibrary();
            if (!Fresco.hasBeenInitialized()) {
                RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, false));
                mRTManager = new RTManager(rtApi, savedInstanceState);
                everBitmapHelper = new EverBitmapHelper(this);
                everBallsHelper = new EverBallsHelper(this);
                audioHelper = new EverAudioHelper(this);
                everThemeHelper = new EverThemeHelper(this);
                AndroidAudioConverter.load(this, new ILoadCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "aaaa", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception error) {

                    }
                });
                Fresco.initialize(this);
                adapter = new WeakReference<>(new MultiViewAdapter());
                adapter.get().registerItemBinders(new NoteModelBinder(this));
                adapter.get().addSection(everNoteManagement.noteModelSection);
            }
        });
        new Handler(Looper.getMainLooper()).post(this::initializeViews);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initializeViews() {
        new Thread(() -> {
            // a potentially time consuming task
            note_bottom_bar = findViewById(R.id.bottom_bar);
            bottom_nav_anim = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim);
            bottom_nav_anim_reverse = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim_reverse);
            DrawChangeColor = findViewById(R.id.DrawChangeColor);
            DrawChangeSize = findViewById(R.id.DrawChangeSize);
            size_visualizer = findViewById(R.id.draw_sizeVisualizerCardView);
            ImageSizeView = findViewById(R.id.draw_size_visualizer);
            seekBarDrawSize = findViewById(R.id.draw_size_seekbar);
            IncreaseSize = findViewById(R.id.IncreaseSize1);
            DecreaseSize = findViewById(R.id.DecreaseSize1);
            DrawOptions = findViewById(R.id.draw_options);
            FormatOptions = findViewById(R.id.format_selectors);
            Bold = findViewById(R.id.toolbar_bold);
            Italic = findViewById(R.id.toolbar_italic);
            Underline = findViewById(R.id.toolbar_underline);
            StrikeThrough = findViewById(R.id.toolbar_strikethrough);
            Highlight = findViewById(R.id.HighlightText1);
            changeColor = findViewById(R.id.ChangeColor1);
            size_visualizer = findViewById(R.id.draw_sizeVisualizerCardView);
            ImageSizeView = findViewById(R.id.draw_size_visualizer);
            Bullets = findViewById(R.id.toolbar_bullet);
            Numbers = findViewById(R.id.toolbar_number);
            ParagraphOptions = findViewById(R.id.format_paragraph);
            ImporterOptions = findViewById(R.id.import_options);
            SelectOptions = findViewById(R.id.selectOptions);
            AlignLeft = findViewById(R.id.toolbar_align_left);
            AlignCenter = findViewById(R.id.toolbar_align_center);
            AlignRight = findViewById(R.id.toolbar_align_right);
            GooglePhotos = findViewById(R.id.GooglePhotos);
            Files = findViewById(R.id.Files);
            Gallery = findViewById(R.id.Gallery);
            Undo = findViewById(R.id.toolbar_undo);
            Redo = findViewById(R.id.toolbar_redo);
            Delete = findViewById(R.id.Delete);
            Save = findViewById(R.id.Save);
            toolbar = findViewById(R.id.toolbar);
            AudioOptions = findViewById(R.id.audio_options);
            //scrollView1 = findViewById(R.id.scroll_draw);
            changeNoteColorButton = findViewById(R.id.changeNotecolorButton);
            selectionChangeColor = findViewById(R.id.selectChangeColor);
            selectionDelete = findViewById(R.id.selectDelete);
            edit = new WeakReference<>(findViewById(R.id.searchBox));
            edit.get().setFocusable(false);
            blur = findViewById(R.id.viewblur);
            ImageButton fillCanvas = findViewById(R.id.fillCanvas);
            fillCanvas.setOnClickListener(v -> noteCreator.get().everCreatorHelper.everDraw.get().fillColor(Color.GREEN));
            //  OverScrollDecoratorHelper.setUpOverScroll(scrollView1);

            views.add(edit.get());
            views.add(selectionChangeColor);
            views.add(selectionDelete);
            views.add(DrawChangeColor);
            views.add(DrawChangeSize);
            views.add(changeColor);
            views.add(Highlight);
            views.add(IncreaseSize);
            views.add(DecreaseSize);
            views.add(Bold);
            views.add(Italic);
            views.add(Underline);
            views.add(StrikeThrough);
            views.add(Undo);
            views.add(Redo);
            views.add(Save);
            views.add(Delete);
            views.add(Bullets);
            views.add(Numbers);
            views.add(AlignLeft);
            views.add(AlignCenter);
            views.add(AlignRight);
            views.add(GooglePhotos);
            views.add(Files);
            views.add(Gallery);
            views.add(changeNoteColorButton);

            seekBarDrawSize.setOnTouchListener((v, event) -> {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            DrawVisualizerIsShowing = false;

                            Animation fadeout = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_formatter);

                            size_visualizer.startAnimation(fadeout);


                        }, 450);
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle Seekbar touch events.
                v.onTouchEvent(event);
                return true;
            });


            new Handler(Looper.getMainLooper()).post(() -> {

                //MAYBE TO REMOVE ALL OF THIS MINUS THE LAST LINE
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



            note_bottom_bar.setOnItemReselected(integer -> {
                switch (integer) {
                    case 0:
                        if (formatOptions) {
                            CloseOrOpenFormatOptions(true);
                            formatOptions = false;
                        } else {
                            CloseOrOpenFormatOptions(false);
                            formatOptions = true;
                        }
                        break;

                    case 1:
                        if (paragraphOptions) {
                            CloseOrOpenParagraphOptions(true);
                            paragraphOptions = false;
                        } else {
                            CloseOrOpenParagraphOptions(false);
                            paragraphOptions = true;
                        }
                        break;

                    case 2:
                        if (importerOptions) {
                            CloseOrOpenImporterOptions(true);
                            importerOptions = false;
                        } else {
                            CloseOrOpenImporterOptions(false);
                            importerOptions = true;
                        }
                        break;

                    case 3:
                        if (audioOpen) {
                            audioHelper.stop(true);
                        } else {
                            recordAudio();
                        }
                        break;

                    case 4:
                        break;

                    case 5:
                        if (drawOptions) {
                            CloseOrOpenDrawOptions(0, true);
                        } else {
                            CloseOrOpenDrawOptions(1400, false);
                            noteCreator.get().everCreatorHelper.everDraw = new WeakReference<>(findViewById(R.id.EverDraw));
                            InputMethodManager keyboard1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            keyboard1.hideSoftInputFromWindow(noteCreator.get().everCreatorHelper.everDraw.get().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        break;
                }
                return null;
            });
            note_bottom_bar.setOnItemSelected(integer -> {
                switch (integer) {
                    case 0:
                        if (formatOptions) {
                            CloseOrOpenFormatOptions(true);
                            formatOptions = false;
                        } else {
                            CloseOrOpenFormatOptions(false);
                            formatOptions = true;
                        }
                        break;

                    case 1:
                        if (paragraphOptions) {
                            CloseOrOpenParagraphOptions(true);
                            paragraphOptions = false;
                        } else {
                            CloseOrOpenParagraphOptions(false);
                            paragraphOptions = true;
                        }
                        break;

                    case 2:
                        if (importerOptions) {
                            CloseOrOpenImporterOptions(true);
                            importerOptions = false;
                        } else {
                            CloseOrOpenImporterOptions(false);
                            importerOptions = true;
                        }
                        break;

                    case 3:

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);

                        } else {

                            recordAudio();

                        }
                        break;

                    case 4:
                        break;

                    case 5:
                        if (drawOptions) {
                            CloseOrOpenDrawOptions(0, true);
                        } else {
                            CloseOrOpenDrawOptions(1400, false);
                            noteCreator.get().everCreatorHelper.everDraw = new WeakReference<>(findViewById(R.id.EverDraw));
                            InputMethodManager keyboard1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            keyboard1.hideSoftInputFromWindow(seekBarDrawSize.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        break;
                }
                return null;
            });

            HorizontalRTToolbar toolbar1 = new HorizontalRTToolbar(this);
            mRTManager.registerToolbar(null, toolbar1);

            applyPushDownToViews(views, 0.7f);

            startPostponedEnterTransition();
        }).start();
    }

    public void applyPushDownToViews(List<View> views, float amount) {
        for (View view: views) {
            PushDownAnim.setPushDownAnimTo(view)
                    .setScale(MODE_SCALE,
                            amount);
        }
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
        everNoteManagement.holders.clear();

        everThemeHelper.clearOnBack();

        CloseAllButtons();

        CloseAudioVisualizationHelper.getInstance().clearListeners();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (!atHome) {


                if (actualNote.get().getTitle().equals("") && actualNote.get().getContentsAsString().equals("") && actualNote.get().getDrawsAsString().equals("") && actualNote.get().getImagesAsString().equals("") && actualNote.get().getRecordsAsString().equals("")) {
                    everNoteManagement.removeNote(actualNote.get());

                    System.out.println("Note with id = " + actualNote.get().getId() + " deleted. <-- called from OnBackPress in MainActivity, thx future pedro");
                } else {
                    everNoteManagement.updateNote(actualNote.get().getActualPosition(), actualNote.get());
                }
            }
            switchToolbars(true);
            atHome = true;
            newNote = false;
            everNoteManagement.swipeChangeColorRefresh = false;
            note_bottom_bar.setItemActiveIndex(0);
            positionToScroll = actualNote.get().getActualPosition();
            noteScreen.maximize();
            new Handler(Looper.getMainLooper()).post(super::onBackPressed);
        }, 10);

    }

    public void onClick(View v) {

        clearBooleans();

        everNoteManagement.noteIdIncrement++;
        prefs.putInt("lastID", everNoteManagement.noteIdIncrement);
        actualNote = new WeakReference<>(new Note_Model(everNoteManagement.noteIdIncrement, 0, "", "<br>┼", "", "▓┼", "▓┼", "16777215", "▓┼"));
        everNoteManagement.selectedPosition = 0;
        everNoteManagement.addNote(actualNote.get());
        newNote = true;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            NoteEditorFragmentJavaFragment fragment = NoteEditorFragmentJavaFragment.newInstance();
            CardView card =  Objects.requireNonNull(everNoteManagement.noteScreenRecycler.get().findContainingViewHolder(everNoteManagement.noteScreenRecycler.get().getChildAt(0))).itemView.findViewById(R.id.mainCard);
            fragment.setEnterTransition(new Fade());
            noteScreen.setExitTransition(new Fade());
            FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);

            card.setTransitionName("card" + everNoteManagement.noteIdIncrement + 0);//was +1
            names.add(card.getTransitionName());
            transaction.addSharedElement(card, card.getTransitionName());
            transaction.replace(R.id.nav_host_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            atHome = false;
        }, 450);
    }

    public void ShowDrawSizeVisualizer() {
        if (!DrawVisualizerIsShowing) {
            size_visualizer.setVisibility(View.VISIBLE);
            size_visualizer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter));
            ImageSizeView.setVisibility(View.VISIBLE);
            DrawVisualizerIsShowing = true;
        }
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

    public void onItemClickFromNoteScreen(View view, View view2, View view3, View view4, int position, Note_Model actualNote) {

        clearBooleans();

        NoteEditorFragmentJavaFragment fragment = new NoteEditorFragmentJavaFragment();

        everNoteManagement.ID = actualNote.getId();
        this.actualNote = new WeakReference<>(actualNote);
        names.add(view.getTransitionName());
        names.add(view2.getTransitionName());
        names.add(view3.getTransitionName());
        names.add(view4.getTransitionName());
        everNoteManagement.selectedPosition = position;
        everBallsHelper.clearColors();

        fragment.setEnterTransition(new AutoTransition());
        fragment.setExitTransition(new AutoTransition());
        noteScreen.setExitTransition(new AutoTransition());
        noteScreen.setReenterTransition(new AutoTransition());
        FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.addSharedElement(view, view.getTransitionName());
        transaction.addSharedElement(view2, view2.getTransitionName());
        transaction.addSharedElement(view3, view3.getTransitionName());
        transaction.addSharedElement(view4, view4.getTransitionName());
        transaction.hide(noteScreen);
        transaction.add(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        noteScreen.minimize();
        atHome = false;
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mRTManager.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRTManager.onDestroy(isFinishing());
    }

    public void CloseOrOpenToolbarUndoRedo(boolean UndoRedo) {
        if (UndoRedo) {
            Undo.setVisibility(View.VISIBLE);
            Redo.setVisibility(View.VISIBLE);
        } else {
            Undo.setVisibility(View.INVISIBLE);
            Redo.setVisibility(View.INVISIBLE);
        }
    }

    public void CloseOrOpenAudioOptions(boolean close) {
        if (close) {
            animateHeightChange(findViewById(R.id.imageAudioDecoy), 350, audioDecoySize);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                animateHeightChange(AudioOptions, 500, 1);
                animateWidthChange(AudioOptions, 500, 1);
                CloseAudioVisualizationHelper.getInstance().changeState(true);
            }, 350);
           audioOpen = false;
        } else {
            CloseAudioVisualizationHelper.getInstance().changeState(false);
            animateHeightChange(AudioOptions, 500, 350);
            animateWidthChange(AudioOptions, 500, 750);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                audioDecoySize = findViewById(R.id.imageAudioDecoy).getHeight();
                animateHeightChange(findViewById(R.id.imageAudioDecoy), 350, 0);
            }, 500);

            audioOpen = true;
        }
    }

    public void switchToolbars(boolean show, boolean bottomToolbar, boolean showUndoRedo) {

        if (show) {
            System.out.println("show");
            if (bottomToolbar) {
                if (!bottomBarUp) {
                    animateHeightChange(note_bottom_bar, 600, 150);
                    //   note_bottom_bar.setVisibility(View.VISIBLE);
                    bottomBarUp = true;
                }
            } else {
                //  note_bottom_bar.setVisibility(View.GONE);
                animateHeightChange(note_bottom_bar, 600, 1);

                bottomBarUp = false;
            }

            CloseOrOpenToolbarUndoRedo(showUndoRedo);

        } else {
            // note_bottom_bar.setVisibility(View.GONE);
            animateHeightChange(note_bottom_bar, 600, 1);
            CloseOrOpenToolbarUndoRedo(false);
            bottomBarUp = false;
        }
    }

    public void animateHeightChange(View view, int duration, int amount) {
        new Handler(Looper.getMainLooper()).post(() -> {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(), amount);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = val;
            view.setLayoutParams(layoutParams);
        });
        anim.setDuration(duration);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        anim.start();
        });
    }

    public void animateWidthChange(View view, int duration, int amount) {
        new Handler(Looper.getMainLooper()).post(() -> {
            ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredWidth(), amount);
            anim.addUpdateListener(valueAnimator -> {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = val;
                view.setLayoutParams(layoutParams);
            });
            anim.setDuration(duration);
            anim.setInterpolator(new LinearOutSlowInInterpolator());
            anim.start();
        });
    }

    public void CloseOrOpenDrawOptions(int height, boolean close) {


        noteCreator.get().everCreatorHelper.drawFromRecycler = false;

        if (close) {

            if (!actualNote.get().getNoteColor().equals("16777215"))
                everThemeHelper.tintView(noteCreator.get().everCreatorHelper.cardView.get(), Integer.parseInt(actualNote.get().getNoteColor()));

            //    TransitionManager.beginDelayedTransition(cardNoteCreator, new TransitionSet()
            //           .addTransition(new ChangeBounds()));

            ResizeCardViewToWrapContent();

            CloseOrOpenDrawOptions(true);

            scrollScrollView(false);

            CloseOrOpenNoteContents();

            if (!actualNote.get().getNoteColor().equals("16777215")) {
                cardNoteCreator.get().setCardBackgroundColor(Integer.parseInt(actualNote.get().getNoteColor()));
            }


            CloseOrOpenDrawSize(close);

            drawing = false;

            drawOptions = false;

        } else {

            everThemeHelper.tintView(noteCreator.get().everCreatorHelper.cardView.get(), everThemeHelper.defaultTheme);

            CloseOrOpenDrawOptions(false);
            scrollScrollView(true);
            ResizeEverDrawToPrepareNoteToDraw(height);

            cardNoteCreator.get().setCardBackgroundColor(everThemeHelper.defaultTheme);

            CloseOrOpenNoteContents();

            drawing = true;

            drawOptions = true;

            if (pendingColorChange) {
                everThemeHelper.tintView(cardNoteCreator.get(), everThemeHelper.pendingColor);
                pendingColorChange = false;
            }

            // editor.putBoolean("DrawOn", true);
            // editor.apply();
        }
    }

    public void CloseOrOpenDraWOptionsFromRecycler(EverNestedScrollView scroll, RecyclerView recyclerView, boolean close) {


        if (close) {

            animateHeightChange(DrawOptions, 500, 1);
            CloseOrOpenDrawSize(close);
            drawing = false;
            scroll.setCanScroll(true);
         //   recyclerView.suppressLayout(false);

        } else {

            animateHeightChange(DrawOptions, 500, 150);

            switchToolbars(true, false, true);

            drawing = true;
            scroll.setCanScroll(false);
          //  recyclerView.suppressLayout(true);
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

    public void CloseOrOpenDrawOptions(boolean Close) {
        if (paragraphOptions) {
            CloseOrOpenParagraphOptions(true);
            paragraphOptions = false;
        }
        if (importerOptions) {
            CloseOrOpenImporterOptions(true);
            importerOptions = false;
        }
        if (formatOptions) {
            CloseOrOpenFormatOptions(true);
            formatOptions = false;
        }
        if (Close) {
            // animateObject(DrawOptions, "translationY", DCY, 350);
            animateHeightChange(DrawOptions, 500, 1);
        } else {
            //  animateObject(DrawOptions, "translationY", DOY-50, 350);
            animateHeightChange(DrawOptions, 500, 150);
        }
    }

    public void CloseOrOpenFormatOptions(boolean Close) {
        if (paragraphOptions) {
            CloseOrOpenParagraphOptions(true);
            paragraphOptions = false;
        }
        if (importerOptions) {
            CloseOrOpenImporterOptions(true);
            importerOptions = false;
        }
        if (drawOptions) {
            CloseOrOpenDrawOptions(0, true);
            drawOptions = false;
        }
        if (Close) {
            // animateObject(FormatOptions, "translationY", DCY, 350);
            animateHeightChange(FormatOptions, 500, 1);
            scrollScrollView(false);

        } else {
            //  animateObject(FormatOptions, "translationY", DOY, 350);

            animateHeightChange(FormatOptions, 500, 150);
            scrollScrollView(true);


        }
    }

    public void scrollScrollView(boolean up) {

        if (up) {
            if (!isContentUp) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> noteCreator.get().everCreatorHelper.scrollView.get().smoothScrollBy(0, 150), 50);
                isContentUp = true;
            }
        } else {
            if (isContentUp) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> noteCreator.get().everCreatorHelper.scrollView.get().smoothScrollBy(0, -150), 50);
                isContentUp = false;
            }
        }
    }

    public void CloseOrOpenSelectionOptions(boolean Close) {
        if (Close) {
            animateHeightChange(SelectOptions, 500, 1);
        } else {
            everNoteManagement.notesSelected = false;
            animateHeightChange(SelectOptions, 500, 150);
        }
    }

    public void CloseOrOpenParagraphOptions(boolean Close) {
        if (importerOptions) {
            CloseOrOpenImporterOptions(true);
            importerOptions = false;
        }
        if (formatOptions) {
            CloseOrOpenFormatOptions(true);
            formatOptions = false;
        }
        if (drawOptions) {
            CloseOrOpenDrawOptions(0, true);
            drawOptions = false;
        }
        if (Close) {
            //  animateObject(ParagraphOptions, "translationY", DCY, 350);
            animateHeightChange(ParagraphOptions, 500, 1);
            scrollScrollView(false);
        } else {
            //  animateObject(ParagraphOptions, "translationY", DOY, 350);
            animateHeightChange(ParagraphOptions, 500, 150);
            scrollScrollView(true);
        }
    }

    public void CloseOrOpenImporterOptions(boolean Close) {
        if (paragraphOptions) {
            CloseOrOpenParagraphOptions(true);
            paragraphOptions = false;
        }
        if (formatOptions) {
            CloseOrOpenFormatOptions(true);
            formatOptions = false;
        }
        if (drawOptions) {
            CloseOrOpenDrawOptions(0, true);
            drawOptions = false;
        }
        if (Close) {
            //  animateObject(ImporterOptions, "translationY", DCY+50, 350);
            animateHeightChange(ImporterOptions, 500, 1);
            scrollScrollView(false);
        } else {
            // animateObject(ImporterOptions, "translationY", DOY, 350);
            animateHeightChange(ImporterOptions, 500, 200);
            scrollScrollView(true);
        }
    }

    public void CloseAllButtons() {

        if (noteCreator != null) {
            if (noteCreator.get().everCreatorHelper.scrollView.get() != null) {
                noteCreator.get().everCreatorHelper.scrollView.get().smoothScrollTo(0, 0);
            }
        }
        switchToolbars(false, false, false);

        InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(toolbar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (paragraphOptions) {
            CloseOrOpenParagraphOptions(true);
            paragraphOptions = false;
        }
        if (importerOptions) {
            CloseOrOpenImporterOptions(true);
            importerOptions = false;
        }
        if (formatOptions) {
            CloseOrOpenFormatOptions(true);
            formatOptions = false;
        }
        if (drawOptions) {
            CloseOrOpenDrawOptions(true);
            drawOptions = false;
        }

        if (seekBarDrawSize.getVisibility() == View.VISIBLE) {
            CloseOrOpenDrawSize(true);
        }
    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }

    private void CloseOrOpenNoteContents() {

        // EditText TitleTextBox = findViewById(R.id.TitleTextBox);
        //  RecyclerView recyclerViewImage = findViewById(R.id.ImagesRecycler);
        //  RecyclerView textanddrawRecyclerView = findViewById(R.id.TextAndDrawRecyclerView);
        //  EverDraw everDraw = findViewById(R.id.EverDraw);

        if (showNoteContents) {

            title.get().setVisibility(View.VISIBLE);
            imageRecycler.get().setVisibility(View.VISIBLE);
            contentRecycler.get().setVisibility(View.VISIBLE);
            noteCreator.get().everCreatorHelper.everDraw.get().setVisibility(View.GONE);

            showNoteContents = false;

        } else {

            title.get().setVisibility(View.GONE);
            imageRecycler.get().setVisibility(View.GONE);
            contentRecycler.get().setVisibility(View.GONE);
            noteCreator.get().everCreatorHelper.everDraw.get().setVisibility(View.VISIBLE);

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

    public void formatClick(View view) {
        switch (view.getTag().toString()) {
            case "increaseSize":
                if (size < 76) {

                    size++;
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTSIZE, size);
                    // noteCreator.get().everCreatorHelper.activeEditor.get().setFontSize(size);
                }
                break;

            case "decreaseSize":
                if (size > 10) {

                    size--;
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.FONTSIZE, size);
                    // noteCreator.get().everCreatorHelper.activeEditor.get().setFontSize(size);
                }
                break;

            case "changeColor":
                // createPopupMenu(note_bottom_bar, R.layout.color_change_popup, true, "popup", 50, -180);
                everBallsHelper.initEverBallsForeground();
                //  Blurry.with(this).animate(700).animate().sampling(6).onto(findViewById(R.id.include));
                //CloseOrOpenEditorColors(false);
                break;

            case "bold":
                if (bold) {
                    view.setBackgroundTintList(ColorStateList.valueOf(GetColor(R.color.White)));
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BOLD, false);
                    bold = false;
                } else {
                    view.setBackgroundTintList(ColorStateList.valueOf(GetColor(R.color.SkyBlueHighlight)));
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BOLD, true);
                    bold = true;
                }
                //  noteCreator.get().everCreatorHelper.activeEditor.get().setBold();
                break;

            case "italic":
                if (italic) {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.ITALIC, false);
                    italic = false;
                } else {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.ITALIC, true);
                    italic = true;
                }
                // noteCreator.get().everCreatorHelper.activeEditor.get().setItalic();
                break;

            case "underline":
                if (underline) {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.UNDERLINE, false);
                    underline = false;
                } else {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.UNDERLINE, true);
                    underline = true;
                }
                //  noteCreator.get().everCreatorHelper.activeEditor.get().setUnderline();
                break;

            case "striketrough":
                if (striketrough) {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.STRIKETHROUGH, false);
                    striketrough = false;
                } else {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.STRIKETHROUGH, true);
                    striketrough = true;
                }
                //  noteCreator.get().everCreatorHelper.activeEditor.get().setStrikeThrough();
                break;

            case "highlight":
                // createPopupMenu(note_bottom_bar, R.layout.highlight_color_change_popup, true, "popup", 50, -180);
                //  CloseOrOpenEditorHIghlightColors(false);
                everBallsHelper.initEverBallsHighlight();

                //  Blurry.with(this).animate(700).animate().sampling(6).onto(findViewById(R.id.include));

                break;
            case "clearHighlight":
                // noteCreator.get().everCreatorHelper.activeEditor.get().setTextBackgroundColor(Color.WHITE);
                break;
        }
        // popupWindowHelper.dismiss();
    }

    public void paragraphClick(View view) {
        switch (view.getTag().toString()) {
            case "numbers":
                if (numbers) {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.NUMBER, false);
                    numbers = false;
                } else {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.NUMBER, true);
                    numbers = true;
                }
                //     noteCreator.get().everCreatorHelper.activeEditor.get().setNumbers();
                break;

            case "bullets":
                if (bullets) {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BULLET, false);
                    bullets = false;
                } else {
                    noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.BULLET, true);
                    bullets = true;
                }
                //     noteCreator.get().everCreatorHelper.activeEditor.get().setBullets();
                break;

            case "alignLeft":
                noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_NORMAL);
                //    noteCreator.get().everCreatorHelper.activeEditor.get().setAlignLeft();
                break;

            case "alignCenter":
                noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_CENTER);
                //  noteCreator.get().everCreatorHelper.activeEditor.get().setAlignCenter();
                break;

            case "alignRight":
                noteCreator.get().everCreatorHelper.activeEditor.get().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_OPPOSITE);
                //  noteCreator.get().everCreatorHelper.activeEditor.get().setAlignRight();
                break;

        }
    }

    public void clickToCustomize(View view) {
        switch (view.getTag().toString()) {
            case "black":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.Black));
                break;

            case "white":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.White));
                break;

            case "magenta":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.Magenta));
                break;

            case "pink":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.Pink));
                break;

            case "orange":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.Orange));
                break;

            case "blue":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.SkyBlue));
                break;

            case "yellow":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.YellowSun));
                break;

            case "green":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.GrassGreen));
                break;
        }
        Blurry.delete(findViewById(R.id.homeNotesrelative));
        popupWindowHelperColor.dismiss();
    }

    public void animateObject(View view, String effect, int amount, int duration) {
        ObjectAnimator transAnimation2 = ObjectAnimator.ofFloat(view, effect, view.getTranslationY(), amount);
        transAnimation2.setDuration(duration);//set duration
        transAnimation2.setInterpolator(new LinearOutSlowInInterpolator());
        transAnimation2.start();//start animation
    }

    public void swipeItemsListener(View view, Note_Model noteModel) {
        everNoteManagement.selectedPosition = noteModel.getActualPosition();
        everNoteManagement.selectedID = noteModel.getId();
        switch (view.getTag().toString()) {
            case "changeColor":
                createPopupMenu(view, R.layout.swipe_color_change_popup, "dropdown");
                break;

            case "delete":
                deleteNoteDialog(noteModel);
                break;
        }
    }

    public void deleteNoteDialog(Note_Model note) {
        //   blurView(this, 25, 2, findViewById(R.id.homeNotesrelative));

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this note!")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(sDialog -> {
                    sDialog
                            .setTitleText("Deleted!")
                            .setContentText("Your note in position: " + note.getActualPosition() + ", with ID: " + note.getId() + " was deleted.")
                            .setConfirmText("OK")
                            .setConfirmClickListener(sweetAlertDialog -> sDialog.dismissWithAnimation())
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    sDialog.setOnCancelListener(dialog -> Blurry.delete(findViewById(R.id.homeNotesrelative)));
                    sDialog.setOnDismissListener(dialog -> {
                        Blurry.delete(findViewById(R.id.homeNotesrelative));
                        everNoteManagement.removeNote(note);
                    });
                    //  notesModels.remove(deletePosition);

                })
                .show();
    }

    private void createPopupMenu(View view, int layout, String showAs) {


        View popView = LayoutInflater.from(this).inflate(layout, null);
        popupWindowHelperColor = new EverPopup(popView);

        switch (showAs) {
            case "dropdown":
                popupWindowHelperColor.showAsDropDown(view, 0, 0);
                break;

            case "popup":
                popupWindowHelperColor.showAsPopUp(view, 0, 0);
                break;

            case "top":
                popupWindowHelperColor.showFromTop(view);

                break;

            case "bottom":
                popupWindowHelperColor.showFromBottom(view);
                break;
        }
    }

    public void beginDelayedTransition(ViewGroup view) {
        TransitionManager.beginDelayedTransition(view, new TransitionSet()
                .addTransition(new ChangeBounds()));
    }

    public void pushDownOnTouch(View view, MotionEvent event, float pushScale, int duration) {
        int i = event.getAction();
        if (i == MotionEvent.ACTION_DOWN) {
            makeDecisionAnimScale(view,
                    pushScale,
                    duration,
                    new AccelerateDecelerateInterpolator());
        } else if (i == MotionEvent.ACTION_CANCEL
                || i == MotionEvent.ACTION_UP) {
            makeDecisionAnimScale(view,
                    1f,
                    duration,
                    new AccelerateDecelerateInterpolator());
        }
    }

    public void makeDecisionAnimScale(final View view, float pushScale, long duration, TimeInterpolator interpolator) {

        animScale(view, pushScale, duration, interpolator);
    }

    public void animScale(final View view, float scale, long duration, TimeInterpolator interpolator) {
        AnimatorSet scaleAnimSet = new AnimatorSet();
        view.animate().cancel();
        if (scaleAnimSet != null) {
            scaleAnimSet.cancel();
        }

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleX.setInterpolator(interpolator);
        scaleX.setDuration(duration);
        scaleY.setInterpolator(interpolator);
        scaleY.setDuration(duration);

        scaleAnimSet = new AnimatorSet();
        scaleAnimSet
                .play(scaleX)
                .with(scaleY);
        scaleX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        scaleX.addUpdateListener(valueAnimator -> {
            View p = (View) view.getParent();
            if (p != null) p.invalidate();
        });
        scaleAnimSet.start();
    }





    public void registerEditor(RTEditText editor, boolean useRichEditing) {
        mRTManager.unregisterEditor(editor);
        mRTManager.registerEditor(editor, useRichEditing);
    }

    public void switchToolbars(boolean showmain) {
        if (showmain) {
            changeNoteColorButton.setVisibility(View.GONE);
            edit.get().setVisibility(View.VISIBLE);
            Save.setVisibility(View.INVISIBLE);
            Delete.setVisibility(View.INVISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Save.setImageResource(R.drawable.ic_baseline_search_24);
                Delete.setImageResource(R.drawable.ic_baseline_library_books_24);
                Save.setVisibility(View.VISIBLE);
                Delete.setVisibility(View.VISIBLE);
            }, 150);

            Save.setTag("Search");
            Delete.setTag("GridLayout");
            //  findViewById(R.id.note_Creator_ToolbarLayout).setVisibility(View.GONE);
            //   findViewById(R.id.mainScreen_ToolbarLayout).setVisibility(View.VISIBLE);

        } else {
            // findViewById(R.id.mainScreen_ToolbarLayout).setVisibility(View.GONE);
            edit.get().setVisibility(View.GONE);
            changeNoteColorButton.setVisibility(View.VISIBLE);
            Save.setVisibility(View.INVISIBLE);
            Delete.setVisibility(View.INVISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Save.setImageResource(R.drawable.ic_baseline_check_24);
                Delete.setImageResource(R.drawable.clear);
                Save.setVisibility(View.VISIBLE);
                Delete.setVisibility(View.VISIBLE);
            }, 150);
            Save.setTag("Save");
            Delete.setTag("Delete");
            // findViewById(R.id.note_Creator_ToolbarLayout).setVisibility(View.VISIBLE);
        }
    }

    public void changeLayout(View view) {
        if ( everNoteManagement.isGrid) {
            prefs.putBoolean("isGrid", false);
            everNoteManagement.isGrid = false;
        } else {
            prefs.putBoolean("isGrid", true);
            everNoteManagement.isGrid = true;
        }
        noteScreen.init();
    }

    @SuppressLint("SetTextI18n")
    public void searchNotes(View view) {
        TextWatcher a = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchInNotes(s);
            }
        };
        if (edit.get().getTag().equals("search")) {
            everNoteManagement.searching = true;
            edit.get().setFocusable(true);
            edit.get().setFocusableInTouchMode(true);
            edit.get().setText("");
            edit.get().setTag("remove");
            everNoteManagement.searchListSection.clear();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                edit.get().requestFocus();
                edit.get().addTextChangedListener(a);
            }, 350);
        } else {
            everNoteManagement.searching = false;
            edit.get().setFocusable(false);
            edit.get().setFocusableInTouchMode(false);
            edit.get().removeTextChangedListener(a);
            edit.get().setText("EVERMIND");
            edit.get().setTag("search");
            everNoteManagement.searchListSection.clear();
            noteScreen.adapter.get().removeAllSections();
            noteScreen.adapter.get().addSection( everNoteManagement.noteModelSection);
            for (Note_Model note : everNoteManagement.noteModelSection.getData()) {
                    note.setActualPosition(everNoteManagement.noteModelSection.getData().indexOf(note));
                }
            }
        }

    private void searchInNotes(CharSequence what) {
        everNoteManagement.searchListSection = new ListSection<>();
        for (Note_Model note : everNoteManagement.noteModelSection.getData()) {
            if (note.getContentsAsString().contains(what)) {

                everNoteManagement.searchListSection.add(note);
                note.setActualPosition(everNoteManagement.searchListSection.getData().indexOf(note));
            }
        }
        noteScreen.adapter.get().removeAllSections();
        noteScreen.adapter.get().addSection(everNoteManagement.searchListSection);
    }

    public void saveButtonClick(View view) {
        if (view.getTag().equals("Save")) {
            if (noteCreator.get().everCreatorHelper.drawFromRecycler) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    noteCreator.get().everCreatorHelper.SaveBitmapFromDraw(true);
                }
                noteCreator.get().everCreatorHelper.drawFromRecycler = false;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    noteCreator.get().everCreatorHelper.SaveBitmapFromDraw(false);
                }

            }
        }
        if (view.getTag().equals("Search")) {
            searchNotes(view);
        }

    }

    public void deleteButtonClick(View view) {
        if (view.getTag().equals("Delete")) {
            if (drawing) {
                noteCreator.get().everCreatorHelper.everDraw.get().clearCanvas();
                CloseOrOpenDrawOptions(0, true);
                // noteCreator.get().everCreatorHelper.FinalYHeight = 0;
            } else {
                deleteNoteDialog(actualNote.get());
            }
        }
        if (view.getTag().equals("GridLayout")) {
            changeLayout(view);
        }
    }

    public void redoClick(View view) {
        animateObject(view, "rotation", 360, 250);
        if (drawing) {
            noteCreator.get().everCreatorHelper.everDraw.get().redo();
        } else {
            mRTManager.onRedo();
        }
    }

    public void undoClick(View view) {
        animateObject(view, "rotation", -360, 250);
        if (drawing) {
            noteCreator.get().everCreatorHelper.everDraw.get().undo();
        } else {
            mRTManager.onUndo();
        }
    }

    public void changeNoteBalls(View view) {
        everBallsHelper.initEverBallsNoteColor();
    }

    public void changePaintBalls(View view) {
        everBallsHelper.initEverBallsPaintType();
    }

    public void eraserClick(View view) {
        noteCreator.get().everCreatorHelper.everDraw.get().setColor(Color.WHITE);
    }

    private void recordAudio() {
        File directory = new File(getFilesDir().getPath() + "/recordEver/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        audioHelper.configureAudio(directory.getPath() + "/", AudioSource.MIC, AudioChannel.STEREO, AudioSampleRate.HZ_48000, Integer.parseInt(actualNote.get().getNoteColor()));
        //  AndroidAudioRecorder.with(this)
     // //          .setFilePath(record.getPath())
     //           .setColor(Integer.parseInt(everNoteManagement.actualNote.get().getNoteColor()))
     //           .setRequestCode(105)
     //           // Optional
     //           .setSource(AudioSource.MIC)
     //           .setChannel(AudioChannel.STEREO)
     //           .setSampleRate(AudioSampleRate.HZ_48000)
     //           .setAutoStart(true)
      //          .setKeepDisplayOn(true)
                // Start recording
      //          .record();
       }

    public void selectAllClick(View view) {
        everNoteManagement.selectAllNotes();
    }

    public void clickChangeColorSelected(View view) {
        int color;
        switch (view.getTag().toString()) {
            case "black":
                color = (GetColor(R.color.Black));
                everNoteManagement.changeColorSelection(color);
                break;

            case "white":
                color = (GetColor(R.color.White));
                everNoteManagement.changeColorSelection(color);
                break;

            case "magenta":
                color = (GetColor(R.color.Magenta));
                everNoteManagement.changeColorSelection(color);
                break;

            case "pink":
                color = (GetColor(R.color.Pink));
                everNoteManagement.changeColorSelection(color);
                break;

            case "orange":
                color = (GetColor(R.color.Orange));
                everNoteManagement.changeColorSelection(color);
                break;

            case "blue":
                color = (GetColor(R.color.SkyBlue));
                everNoteManagement.changeColorSelection(color);
                break;

            case "yellow":
                color = (GetColor(R.color.YellowSun));
                everNoteManagement.changeColorSelection(color);
                break;

            case "green":
                color = (GetColor(R.color.GrassGreen));
                everNoteManagement.changeColorSelection(color);
                break;

        }
        popupWindowHelperColor.dismiss();
    }

    public void openColorSelectorSelection(View view) {
        createPopupMenu(view, R.layout.selection_color_change_popup, "popup");
    }

    public void deleteSelection(View view) {
        everNoteManagement.handleDeleteSelection();
    }

    public void drawChangeSizeClick(View view) {
            if (drawsize) {
                CloseOrOpenDrawSize(true);
                drawsize = false;
            } else {
                CloseOrOpenDrawSize(false);
                drawsize = true;
            }
    }

    public void drawColorClick(View view) {
        everBallsHelper.initEverBallsDraw();
    }

    public void importerClick(View view) {
        noteCreator.get().importerClick(view);
    }

    private void clearBooleans() {
          showNoteContents = false;
          DrawVisualizerIsShowing = false;
          drawing = false;
          atHome = true;
          newNote = false;
          bottomBarUp = false;
          formatOptions = false;
          paragraphOptions = false;
          importerOptions = false;
          drawOptions = false;
          drawsize = false;
          bold = false;
          italic = false;
          underline = false;
          striketrough = false;
          numbers = false;
          bullets = false;
          isContentUp = false;
          audioOpen = false;
          pendingColorChange = false;
    }
}