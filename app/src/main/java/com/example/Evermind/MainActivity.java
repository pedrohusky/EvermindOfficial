package com.example.Evermind;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.alimuzaffar.lib.widgets.AnimatedEditText;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.RTManager;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTApi;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTMediaFactoryImpl;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTProxyImpl;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effects;
import com.example.Evermind.TESTEDITOR.toolbar.HorizontalRTToolbar;
import com.example.Evermind.TESTEDITOR.toolbar.RTToolbarImageButton;
import com.example.Evermind.everUtils.EverPreferences;
import com.example.Evermind.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.example.Evermind.ui.note_screen.NotesScreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.navigation.NavigationView;
import com.thekhaeng.pushdownanim.PushDownAnim;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.alterac.blurkit.BlurLayout;
import jp.wasabeef.blurry.Blurry;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;
import mva2.adapter.internal.ItemMetaData;
import mva2.adapter.util.OnSelectionChangedListener;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class MainActivity extends AppCompatActivity {

    //VIEWS//FUNCTIONS
    public EverDataBase mDatabaseEver;
    public NotesScreen noteScreen;
    public WeakReference<Note_Model> actualNote;
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
    public ImageButton changeNotecolorButton;
    private ImageButton DrawChangeColor;
    private ImageButton DrawChangeSize;
    public CardView ImporterOptions;
    public CardView ParagraphOptions;
    public CardView FormatOptions;
    public CardView SelectOptions;
    private CardView DrawOptions;
    private CardView size_visualizer;
    public SmoothBottomBar note_bottom_bar;
    public EverCircularColorSelect metaColors;
    public WeakReference<CardView> cardNoteCreator;
    public WeakReference<NoteEditorFragmentJavaFragment> noteCreator;
    public WeakReference<RecyclerView> contentRecycler;
    public WeakReference<EditText> title;
    public WeakReference<RecyclerView> imageRecycler;
    public WeakReference<MultiViewAdapter> adapter;
    public WeakReference<RecyclerView> recyclertest;
    WeakReference<AnimatedEditText> edit;
    public EverRecordAudio recorder;
    private AppBarConfiguration mAppBarConfiguration;
    private ImageView ImageSizeView;
    private Toolbar toolbar;
    private EverPopup popupWindowHelperColor;
    private EverPopup popupWindowHelper;
    public RTManager mRTManager;
    public BlurLayout blur;
    //LISTS
    public ArrayList<String> names = new ArrayList<>();
    private int[] colors = new int[0];
    private ArrayList<Drawable> bkg = new ArrayList<>();
    private int[] colorsHighlight = new int[0];
    public ListSection<Note_Model> noteModelSection = new ListSection<>();
    public ListSection<Note_Model> searchListSection = new ListSection<>();
    //IDK
    public Animation bottom_nav_anim;
    public Animation bottom_nav_anim_reverse;
    public Window everMainWindow;
    public EverPreferences prefs;
    // INT's
    public int ID;
    public int selectedPosition;
    public int selectedID;
    public int noteIdIncrement = 0;
    public int metaColorNoteColor;
    public int metaColorDraw;
    public int metaColorHighlightColor;
    public int metaColorForeGroundColor;
    public int defaultColor;
    private int actualColor;
    private int size = 4;
    //BOOLEANS
    public boolean swipeChangeColorRefresh = false;
    public boolean showNoteContents = false;
    public boolean DrawVisualizerIsShowing = false;
    public boolean DrawOn = false;
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
    public boolean pushed = false;
    public boolean isGrid = true;
    public boolean searching = false;
    private boolean notesSelected = false;

    public ListSection<Note_Model> allSelected = new ListSection<>();
    public ListSection<Note_Model> selectedItems = new ListSection<>();


    public List<NoteModelBinder.NoteModelViewHolder> holders = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        postponeEnterTransition();

        mDatabaseEver = new EverDataBase(this);

        everMainWindow = getWindow();


        RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, false));
        mRTManager = new RTManager(rtApi, savedInstanceState);


        prefs = new EverPreferences(this);

        //TODO: optimize performance a little more and make sure everything that we changed is working as intended

        if (getIntent().hasExtra("notes")) {
            //   notesModels = (ArrayList<Note_Model>) getIntent().getSerializableExtra("notes");
            noteModelSection.addAll((ArrayList<Note_Model>) getIntent().getSerializableExtra("notes"));
            noteScreen = new NotesScreen();
            if (noteModelSection.size() > 0) {
                noteIdIncrement = prefs.getInt("lastID", 0);
                // noteIdIncrement = noteModelSection.get(0).getId();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper()).post(() -> {

            //  giphyLibrary = new GiphyLibrary();

            if (!Fresco.hasBeenInitialized())
                Fresco.initialize(this);

        });

        atHome = true;

        isGrid = prefs.getBoolean("isGrid", true);

        adapter = new WeakReference<>(new MultiViewAdapter());

        adapter.get().registerItemBinders(new NoteModelBinder(this));
        adapter.get().addSection(noteModelSection);

        new Thread(() -> {
            // a potentially time consuming task
            //    RTToolbarImageButton changecolorToolbar = findViewById(R.id.toolbar_bold);
            //   RTToolbarImageButton changeFontColorToolbar = findViewById(R.id.toolbar_bold);
            ////    RTToolbarImageButton increaseSizetoolbar = findViewById(R.id.toolbar_bold);
            //    RTToolbarImageButton fontTypeToolbar = findViewById(R.id.toolbar_bold);
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
            //scrollView1 = findViewById(R.id.scroll_draw);
            changeNotecolorButton = findViewById(R.id.changeNotecolorButton);
            selectionChangeColor = findViewById(R.id.selectChangeColor);
            selectionDelete = findViewById(R.id.selectDelete);
            edit = new WeakReference<>(findViewById(R.id.searchBox));
            edit.get().setFocusable(false);
            blur = findViewById(R.id.viewblur);
            ImageButton fillCanvas = findViewById(R.id.fillCanvas);
            fillCanvas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteCreator.get().everDraw.get().fillColor(Color.GREEN);
                }
            });
            //  OverScrollDecoratorHelper.setUpOverScroll(scrollView1);

            metaColors = findViewById(R.id.metaBallMenu);
            int[] a = new int[8];
            for (int value : a) {
                bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));
            }


            PushDownAnim.setPushDownAnimTo(selectionChangeColor)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(selectionDelete)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(DrawChangeColor)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(DrawChangeSize)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(changeColor)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Highlight)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(IncreaseSize)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(DecreaseSize)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Bold)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Italic)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Underline)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(StrikeThrough)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Undo)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Redo)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Save)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Delete)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Bullets)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Numbers)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(AlignCenter)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(AlignLeft)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(AlignRight)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(GooglePhotos)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Gallery)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(Files)
                    .setScale(MODE_SCALE,
                            0.7f);
            PushDownAnim.setPushDownAnimTo(changeNotecolorButton)
                    .setScale(MODE_SCALE,
                            0.7f);

            seekBarDrawSize.setOnTouchListener(new SeekBar.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
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
                }
            });


            defaultColor = getColor(R.color.White);
            actualColor = getColor(R.color.White);


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

            DrawChangeColor.setOnClickListener(view -> initEverBalls("draw"));

            DrawChangeSize.setOnClickListener(v -> {
                if (drawsize) {
                    CloseOrOpenDrawSize(true);
                    drawsize = false;
                } else {
                    CloseOrOpenDrawSize(false);
                    drawsize = true;
                }
            });


            note_bottom_bar.setOnItemReselected(new Function1<Integer, Unit>() {
                @Override
                public Unit invoke(Integer integer) {
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
                            try {
                                recorder.stop();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 4:
                            break;

                        case 5:
                            if (drawOptions) {
                                CloseOrOpenDrawOptions(0, true);
                            } else {
                                CloseOrOpenDrawOptions(1400, false);
                                noteCreator.get().everDraw = new WeakReference<>(findViewById(R.id.EverDraw));
                                InputMethodManager keyboard1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                keyboard1.hideSoftInputFromWindow(seekBarDrawSize.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            break;
                    }
                    return null;
                }
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

                        recordAudio();
                        break;

                    case 4:
                        break;

                    case 5:
                        if (drawOptions) {
                            CloseOrOpenDrawOptions(0, true);
                        } else {
                            CloseOrOpenDrawOptions(1400, false);
                            noteCreator.get().everDraw = new WeakReference<>(findViewById(R.id.EverDraw));
                            InputMethodManager keyboard1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            keyboard1.hideSoftInputFromWindow(seekBarDrawSize.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        break;
                }
                return null;
            });

            HorizontalRTToolbar toolbar1 = new HorizontalRTToolbar(this);
            mRTManager.registerToolbar(null, toolbar1);

            startPostponedEnterTransition();
        }).start();
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
        holders.clear();
        //  if (!actualNote.getNoteColor().equals("000000")) {
        if (actualColor != getColor(R.color.White)) {
            //  tintSystemBars(defaultToolbarColor);
            tintSystemBars(getColor(R.color.White), 500);
        }
        //  }
        new Handler(Looper.getMainLooper()).postDelayed(this::setLightStatusBar, 250);

        CloseAllButtons();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (!atHome) {


                if (actualNote.get().getTitle().equals("") && actualNote.get().getContent().equals("") && actualNote.get().getDrawLocation().equals("") && actualNote.get().getImageURLS().equals("")) {
                    removeNote(actualNote.get());

                    System.out.println("Note with id = " + actualNote.get().getId() + " deleted. <-- called from OnBackPress in MainActivity, thx future pedro");
                } else {
                    updateNote(actualNote.get().getActualPosition(), actualNote.get());
                }
            }
            switchToolbars(true);
            atHome = true;
            newNote = false;
            swipeChangeColorRefresh = false;
            new Handler(Looper.getMainLooper()).post(super::onBackPressed);
        }, 10);

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

    public @NonNull
    Bitmap createBitmapFromView(@NonNull View view, int width, int height) {

        Bitmap bitmap = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(GetColor(R.color.White));
        Drawable background = view.getBackground();

        if (background != null) {
            background.draw(canvas);
        }
        view.draw(canvas);

        return bitmap;
    }

    public void onClick(View v) {

        noteIdIncrement++;
        prefs.putInt("lastID", noteIdIncrement);
        actualNote = new WeakReference<>(new Note_Model(noteIdIncrement, 0, "", "", "", "", "", "000000"));
        selectedPosition = 0;
        addNote(actualNote.get());
        atHome = false;
        newNote = true;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            NoteEditorFragmentJavaFragment fragment = NoteEditorFragmentJavaFragment.newInstance();
            CardView card = recyclertest.get().findContainingViewHolder(recyclertest.get().getChildAt(0)).itemView.findViewById(R.id.mainCard);
            fragment.setEnterTransition(new Fade());
            noteScreen.setExitTransition(new Fade());
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

        NoteEditorFragmentJavaFragment fragment = new NoteEditorFragmentJavaFragment();

        this.ID = actualNote.getId();
        this.actualNote = new WeakReference<>(actualNote);
        atHome = false;
        newNote = false;
        names.add(view.getTransitionName());
        names.add(view2.getTransitionName());
        names.add(view3.getTransitionName());
        names.add(view4.getTransitionName());
        selectedPosition = position;
        metaColorNoteColor = GetColor(R.color.White);
        metaColorDraw = GetColor(R.color.White);
        metaColorForeGroundColor = GetColor(R.color.Black);
        metaColorHighlightColor = GetColor(R.color.White);

        fragment.setEnterTransition(new Fade());
        noteScreen.setExitTransition(new Fade());
        FragmentTransaction transaction = noteScreen.getParentFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.addSharedElement(view, view.getTransitionName());
        transaction.addSharedElement(view2, view2.getTransitionName());
        transaction.addSharedElement(view3, view3.getTransitionName());
        transaction.addSharedElement(view4, view4.getTransitionName());
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(noteScreen.getTag());
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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


        noteCreator.get().drawFromRecycler = false;

        if (close) {

            if (!actualNote.get().getNoteColor().equals("000000"))
                tintView(noteCreator.get().cardView.get(), Integer.parseInt(actualNote.get().getNoteColor()));

            //    TransitionManager.beginDelayedTransition(cardNoteCreator, new TransitionSet()
            //           .addTransition(new ChangeBounds()));

            ResizeCardViewToWrapContent();

            CloseOrOpenDrawOptions(true);

            scrollScrollView(false);

            CloseOrOpenNoteContents();

            if (!actualNote.get().getNoteColor().equals("000000")) {
                cardNoteCreator.get().setCardBackgroundColor(Integer.parseInt(actualNote.get().getNoteColor()));
            }


            CloseOrOpenDrawSize(close);

            DrawOn = false;

            drawOptions = false;

        } else {

            tintView(noteCreator.get().cardView.get(), defaultColor);

            CloseOrOpenDrawOptions(false);
            scrollScrollView(true);
            ResizeEverDrawToPrepareNoteToDraw(height);

            cardNoteCreator.get().setCardBackgroundColor(defaultColor);

            CloseOrOpenNoteContents();

            DrawOn = true;

            drawOptions = true;

            // editor.putBoolean("DrawOn", true);
            // editor.apply();
        }
    }

    public void CloseOrOpenDraWOptionsFromRecycler(EverNestedScrollView scroll, RecyclerView recyclerView, boolean close) {


        if (close) {

            animateHeightChange(DrawOptions, 500, 1);
            CloseOrOpenDrawSize(close);
            DrawOn = false;
            scroll.setCanScroll(true);
            recyclerView.suppressLayout(false);

        } else {

            animateHeightChange(DrawOptions, 500, 150);

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
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    noteCreator.get().scrollView.get().smoothScrollBy(0, 150);
                }, 50);
                isContentUp = true;
            }
        } else {
            if (isContentUp) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    noteCreator.get().scrollView.get().smoothScrollBy(0, -150);
                }, 50);
                isContentUp = false;
            }
        }
    }

    public void CloseOrOpenSelectionOptions(boolean Close) {
        if (Close) {
            animateHeightChange(SelectOptions, 500, 1);
        } else {
            notesSelected = false;
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
            if (noteCreator.get().scrollView.get() != null) {
                noteCreator.get().scrollView.get().smoothScrollTo(0, 0);
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
            noteCreator.get().everDraw.get().setVisibility(View.GONE);

            showNoteContents = false;

        } else {

            title.get().setVisibility(View.GONE);
            imageRecycler.get().setVisibility(View.GONE);
            contentRecycler.get().setVisibility(View.GONE);
            noteCreator.get().everDraw.get().setVisibility(View.VISIBLE);

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
                if (size < 76) {

                    size++;
                    noteCreator.get().activeEditor.get().applyEffect(Effects.FONTSIZE, size);
                    // noteCreator.get().activeEditor.get().setFontSize(size);
                }
                break;

            case "decreaseSize":
                if (size > 12) {

                    size--;
                    noteCreator.get().activeEditor.get().applyEffect(Effects.FONTSIZE, size);
                    // noteCreator.get().activeEditor.get().setFontSize(size);
                }
                break;

            case "changeColor":
                // createPopupMenu(note_bottom_bar, R.layout.color_change_popup, true, "popup", 50, -180);
                initEverBalls("foreground");
                //  Blurry.with(this).animate(700).animate().sampling(6).onto(findViewById(R.id.include));
                //CloseOrOpenEditorColors(false);
                break;

            case "bold":
                if (bold) {
                    view.setBackgroundTintList(ColorStateList.valueOf(GetColor(R.color.White)));
                    noteCreator.get().activeEditor.get().applyEffect(Effects.BOLD, false);
                    bold = false;
                } else {
                    view.setBackgroundTintList(ColorStateList.valueOf(GetColor(R.color.SkyBlueHighlight)));
                    noteCreator.get().activeEditor.get().applyEffect(Effects.BOLD, true);
                    bold = true;
                }
                //  noteCreator.get().activeEditor.get().setBold();
                break;

            case "italic":
                if (italic) {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.ITALIC, false);
                    italic = false;
                } else {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.ITALIC, true);
                    italic = true;
                }
                // noteCreator.get().activeEditor.get().setItalic();
                break;

            case "underline":
                if (underline) {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.UNDERLINE, false);
                    underline = false;
                } else {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.UNDERLINE, true);
                    underline = true;
                }
                //  noteCreator.get().activeEditor.get().setUnderline();
                break;

            case "striketrough":
                if (striketrough) {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.STRIKETHROUGH, false);
                    striketrough = false;
                } else {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.STRIKETHROUGH, true);
                    striketrough = true;
                }
                //  noteCreator.get().activeEditor.get().setStrikeThrough();
                break;

            case "highlight":
                // createPopupMenu(note_bottom_bar, R.layout.highlight_color_change_popup, true, "popup", 50, -180);
                //  CloseOrOpenEditorHIghlightColors(false);
                initEverBalls("highlight");

                //  Blurry.with(this).animate(700).animate().sampling(6).onto(findViewById(R.id.include));

                break;
            case "clearHighlight":
                // noteCreator.get().activeEditor.get().setTextBackgroundColor(Color.WHITE);
                break;
        }
        // popupWindowHelper.dismiss();
    }

    public void paragraphClick(View view) {
        switch (view.getTag().toString()) {
            case "numbers":
                if (numbers) {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.NUMBER, false);
                    numbers = false;
                } else {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.NUMBER, true);
                    numbers = true;
                }
                //     noteCreator.get().activeEditor.get().setNumbers();
                break;

            case "bullets":
                if (bullets) {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.BULLET, false);
                    bullets = false;
                } else {
                    noteCreator.get().activeEditor.get().applyEffect(Effects.BULLET, true);
                    bullets = true;
                }
                //     noteCreator.get().activeEditor.get().setBullets();
                break;

            case "alignLeft":
                noteCreator.get().activeEditor.get().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_NORMAL);
                //    noteCreator.get().activeEditor.get().setAlignLeft();
                break;

            case "alignCenter":
                noteCreator.get().activeEditor.get().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_CENTER);
                //  noteCreator.get().activeEditor.get().setAlignCenter();
                break;

            case "alignRight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_OPPOSITE);
                //  noteCreator.get().activeEditor.get().setAlignRight();
                break;

        }
    }

    public void importerClick(View view) {
        noteCreator.get().importerClick(view, popupWindowHelper);
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
        if (searchListSection.size() > 0) {
            searchListSection.get(selectedPosition).setNoteColor(String.valueOf(color));
            updateNote(selectedPosition, searchListSection.get(selectedPosition));
        } else {
            noteModelSection.get(selectedPosition).setNoteColor(String.valueOf(color));
            updateNote(selectedPosition, noteModelSection.get(selectedPosition));
        }


    }

    public void tintSystemBars(int color, int duration) {

        setDarkStatusBar();

        // Initial colors of each system bar.
        final int statusBarColor = everMainWindow.getStatusBarColor();
        final int buttonsColor = actualColor;
        // final int toolbarColor = everMainWindow.getStatusBarColor();

        // Desired final colors of each bar.
        final int buttonToColor;
        if (color == defaultColor) {
            buttonToColor = R.color.Black;
        } else {
            buttonToColor = color;
        }
        final int statusBarToColor = color;
        //  final int toolbarToColor = color;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
            anim.addUpdateListener(animation -> {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = blendColors(statusBarColor, statusBarToColor, position);
                int buttonsfinalColor = blendColors(buttonsColor, buttonToColor, position);
                everMainWindow.setStatusBarColor(blended);

                // Apply blended color to the ActionBar.
                //   blended = blendColors(toolbarColor, toolbarToColor, position);
                //  ColorDrawable background = new ColorDrawable(blended);
                //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
                Save.setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                Delete.setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                Undo.setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                Redo.setImageTintList(ColorStateList.valueOf(buttonsfinalColor));
                note_bottom_bar.setBarIndicatorColor(blended);
                cardNoteCreator.get().setBackgroundTintList(ColorStateList.valueOf(blended));
            });
            anim.setInterpolator(new LinearOutSlowInInterpolator());
            anim.setDuration(duration).start();
            actualColor = color;
        }, 15);
    }

    public void tintView(View view, int color) {

        // Initial colors of each system bar.
        final int statusBarColor = view.getBackgroundTintList().getDefaultColor();
        // final int toolbarColor = everMainWindow.getStatusBarColor();

        final int metacolor = metaColors.getMainButtonColor();
        // Desired final colors of each bar.
        final int statusBarToColor = color;
        //  final int toolbarToColor = color;


        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(animation -> {
            // Use animation position to blend colors.
            float position = animation.getAnimatedFraction();

            // Apply blended color to the status bar.
            int blended = blendColors(statusBarColor, statusBarToColor, position);
            int blended1 = blendColors(metacolor, statusBarToColor, position);
            // Apply blended color to the ActionBar.
            //   blended = blendColors(toolbarColor, toolbarToColor, position);
            //  ColorDrawable background = new ColorDrawable(blended);
            //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
            if (view == note_bottom_bar) {
                note_bottom_bar.setBarIndicatorColor(blended);
            } else if (view == metaColors) {
                metaColors.setMainButtonColor(blended1);
            } else {
                view.setBackgroundTintList(ColorStateList.valueOf(blended));
            }
        });
        anim.setInterpolator(new LinearOutSlowInInterpolator());
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

            cardNoteCreator.get().setTranslationY(blended);
        });
        anim.setInterpolator(new LinearOutSlowInInterpolator());
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            transAnimation2.setInterpolator(new LinearOutSlowInInterpolator());
        }
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
                        removeNote(note);
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
        swipeChangeColorRefresh = true;
        if (searchListSection.size() > 0) {
            new Handler(Looper.getMainLooper()).post(() -> searchListSection.set(p, note));

        } else {
            if (noteModelSection.size() > p) {
                new Handler(Looper.getMainLooper()).post(() -> noteModelSection.set(p, note));

            }
        }

        if (note != null) {
                mDatabaseEver.setNoteModel(String.valueOf(note.getId()), note.getTitle(), note.getContent(), note.getDrawLocation(), note.getImageURLS(), note.getNoteColor());
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> swipeChangeColorRefresh = false, 10);
        //adapter.notifyItemChanged(p);
    }

    public void removeNote(Note_Model note) {
        deleteDrawsAndImagesInsideNote(note);
        mDatabaseEver.deleteNote(note.getId());
        new Handler(Looper.getMainLooper()).post(() -> {
            noteModelSection.remove(note.getActualPosition());
            for (int position = 0; position < noteModelSection.size(); position++) {
                noteModelSection.get(position).setActualPosition(position);
            }
        });
    }

    public void addNote(Note_Model newNote) {
        //TODO FIX ANIMATION WHEN ADDING NOTE WHEN CREATING TO DO THE SHARED ELEMENT TRANSITION AND TRY TO USE ADAPTER.NOTIFIYITEMATPOSITION
        mDatabaseEver.AddNoteContent("", "");
        noteModelSection.add(0, newNote);
        recyclertest.get().smoothScrollToPosition(0);
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
        scaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                View p = (View) view.getParent();
                if (p != null) p.invalidate();
            }
        });
        scaleAnimSet.start();
    }

    private void deleteDrawsAndImagesInsideNote(Note_Model note) {
        for (String path : note.getImages()) {
            File f = new File(path);
            if (f.exists()) {
                System.out.println("Image at = " + f.getPath() + " Deleted.");
                f.delete();
            }
        }
        for (String path : note.getDraws()) {
            File f = new File(path);
            if (f.exists()) {
                System.out.println("Draw at = " + f.getPath() + " Deleted.");
                f.delete();
            }
        }
    }

    public void deleteSelection(View view) {
        new Thread(() -> {
        if (allSelected.size() > 0) {
            List<Note_Model> list = allSelected.getData();
            for (Note_Model note : list) {
                removeNote(note);
            }
        } else {
            List<Note_Model> list = selectedItems.getData();
            if (list.size() > 0) {
                for (Note_Model note : list) {
                    removeNote(note);
                }
                selectedItems.clear();
            }
        }

        CloseOrOpenSelectionOptions(true);
        new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
        }).start();
    }

    public void openColorSelectorSelection(View view) {
        createPopupMenu(view, R.layout.selection_color_change_popup, true, "popup", 0, 0);
    }

    public void changeColorSelection(int color) {
        new Thread(() -> {
        if (allSelected.size() > 0) {
            List<Note_Model> list = allSelected.getData();
            for (Note_Model note : list) {
                System.out.println(note.getActualPosition());
                selectedPosition = note.getActualPosition();
                selectedID = note.getId();
                ChangeNoteColor(color);
                new Handler(Looper.getMainLooper()).post(() -> noteScreen.adapter.get().notifyItemChanged(note.getActualPosition()));
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                deselectItems(allSelected);
            });

        } else {
            List<Note_Model> list = selectedItems.getData();
            if (list.size() > 0) {
                for (Note_Model note : list) {
                    System.out.println(note.getActualPosition());
                    selectedPosition = note.getActualPosition();
                    selectedID = note.getId();
                    ChangeNoteColor(color);
                    note.setSelected(false);
                    new Handler(Looper.getMainLooper()).post(() -> noteScreen.adapter.get().notifyItemChanged(note.getActualPosition()));
                }
                selectedItems.clear();
            }
        }

        new Handler(Looper.getMainLooper()).post(() -> CloseOrOpenSelectionOptions(true));
        new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
        }).start();
    }

    public void clickChangeColorSelected(View view) {
        int color;
        switch (view.getTag().toString()) {
            case "black":
                color = (GetColor(R.color.Black));
                changeColorSelection(color);
                break;

            case "white":
                color = (GetColor(R.color.White));
                changeColorSelection(color);
                break;

            case "magenta":
                color = (GetColor(R.color.Magenta));
                changeColorSelection(color);
                break;

            case "pink":
                color = (GetColor(R.color.Pink));
                changeColorSelection(color);
                break;

            case "orange":
                color = (GetColor(R.color.Orange));
                changeColorSelection(color);
                break;

            case "blue":
                color = (GetColor(R.color.SkyBlue));
                changeColorSelection(color);
                break;

            case "yellow":
                color = (GetColor(R.color.YellowSun));
                changeColorSelection(color);
                break;

            case "green":
                color = (GetColor(R.color.GrassGreen));
                changeColorSelection(color);
                break;

        }
        popupWindowHelperColor.dismiss();
    }

    public void registerEditor(RTEditText editor, boolean useRichEditing) {
        mRTManager.unregisterEditor(editor);
        mRTManager.registerEditor(editor, useRichEditing);
    }

    public void switchToolbars(boolean showmain) {
        if (showmain) {
            changeNotecolorButton.setVisibility(View.GONE);
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
            changeNotecolorButton.setVisibility(View.VISIBLE);
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
        if (isGrid) {
            prefs.putBoolean("isGrid", false);
            isGrid = false;
        } else {
            prefs.putBoolean("isGrid", true);
            isGrid = true;
        }
        noteScreen.init();
    }

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
            searching = true;
            edit.get().setFocusable(true);
            edit.get().setFocusableInTouchMode(true);
            edit.get().setText("");
            edit.get().setTag("remove");
            searchListSection.clear();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                edit.get().requestFocus();
                edit.get().addTextChangedListener(a);
            }, 350);
        } else {
            searching = false;
            edit.get().setFocusable(false);
            edit.get().setFocusableInTouchMode(false);
            edit.get().removeTextChangedListener(a);
            edit.get().setText("EVERMIND");
            edit.get().setTag("search");
            searchListSection.clear();
            noteScreen.adapter.get().removeAllSections();
            noteScreen.adapter.get().addSection(noteModelSection);
        }
    }

    private void searchInNotes(CharSequence what) {
        searchListSection = new ListSection<>();
        for (Note_Model note : noteModelSection.getData()) {
            if (note.getContent().contains(what)) {

                searchListSection.add(note);
                note.setActualPosition(searchListSection.getData().indexOf(note));
            }
        }
        noteScreen.adapter.get().removeAllSections();
        noteScreen.adapter.get().addSection(searchListSection);
    }

    public void saveButtonClick(View view) {
        if (view.getTag().equals("Save")) {
            if (noteCreator.get().drawFromRecycler) {

                int finalY = noteCreator.get().FinalYHeight;

                Bitmap toResizeBitmap = noteCreator.get().everDraw.get().getBitmap(Color.TRANSPARENT);

                if (toResizeBitmap.getHeight() >= finalY + 75) {
                    noteCreator.get().newDrawedbitmap = new WeakReference<>(Bitmap.createBitmap(toResizeBitmap, 0, 0, noteCreator.get().everDraw.get().getWidth(), noteCreator.get().everDraw.get().getHeight()));
                } else {
                    noteCreator.get().newDrawedbitmap = new WeakReference<>(Bitmap.createBitmap(toResizeBitmap, 0, 0, noteCreator.get().everDraw.get().getWidth(), noteCreator.get().everDraw.get().getHeight()));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    noteCreator.get().SaveBitmapFromDraw(true);
                }
                noteCreator.get().drawFromRecycler = false;
            } else {

                // String content = actualNote.get().getContent();

                // mainActivity.get().mDatabaseEver.editContent(String.valueOf(actualNote.get().getId()), content + "┼");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    noteCreator.get().SaveBitmapFromDraw(false);
                }

            }
        }
        if (view.getTag().equals("Search")) {
            searchNotes(view);
        }

    }

    public void deleteButtonClick(View view) {
        if (view.getTag().equals("Delete")) {
            if (DrawOn) {
                noteCreator.get().everDraw.get().clearCanvas();
                CloseOrOpenDrawOptions(0, true);
                // noteCreator.get().FinalYHeight = 0;
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
        if (DrawOn) {
            noteCreator.get().everDraw.get().redo();
        } else {
            mRTManager.onRedo();
        }
    }

    public void undoClick(View view) {
        animateObject(view, "rotation", -360, 250);
        if (DrawOn) {
            noteCreator.get().everDraw.get().undo();
        } else {
            mRTManager.onUndo();
        }
    }

    public void changeNoteBalls(View view) {
        initEverBalls("noteColor");
    }

    public void changePaintBalls(View view) {
        initEverBalls("paintType");
    }

    public void eraserClick(View view) {
        noteCreator.get().everDraw.get().setColor(Color.WHITE);
    }

    private void recordAudio() {
        //animateHeightChange(recordMic, 500, 200);

        File record = new File(getFilesDir().getPath() + "/recordEver/" + "recordN" + System.currentTimeMillis() + ".3gp");
        if (!record.exists()) {
            record.mkdirs();
        }
        System.out.println("dir = " + record.getPath());
        EverRecordAudio r = new EverRecordAudio(record.getPath());
        try {
            r.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        r.recorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                System.out.println("aaaaxaas432537378645342354 oi meu cu buceta");
            }
        });
    }

    private void initEverBalls(String name) {

        switch (name) {
            case "noteColor":
                colors = getResources().getIntArray(R.array.noteColor);
                metaColors.setAdapter(new meatadapter(colors, bkg));
                metaColors.init(true, false, metaColorNoteColor);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:

                            tintSystemBars(colors[0], 1000);
                            tintView(metaColors, colors[0]);
                            metaColorNoteColor = colors[0];
                            actualNote.get().setNoteColor(String.valueOf(colors[0]));
                            metaColors.toggleMenu(true, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.Black));
                            break;
                        case 1:

                            tintSystemBars(colors[1], 1000);
                            tintView(metaColors, colors[1]);
                            metaColorNoteColor = colors[1];
                            actualNote.get().setNoteColor(String.valueOf(colors[1]));
                            metaColors.toggleMenu(true, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.White));
                            break;
                        case 2:

                            tintSystemBars(colors[2], 1000);
                            tintView(metaColors, colors[2]);
                            metaColorNoteColor = colors[2];
                            actualNote.get().setNoteColor(String.valueOf(colors[2]));
                            metaColors.toggleMenu(true, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.Magenta));
                            break;
                        case 3:
                            tintSystemBars(colors[3], 1000);
                            tintView(metaColors, colors[3]);
                            metaColorNoteColor = colors[3];
                            actualNote.get().setNoteColor(String.valueOf(colors[3]));
                            metaColors.toggleMenu(true, false);


                            //  noteCreator.get().activeEditor.get().setTextColorR.color.Pink));
                            break;
                        case 4:

                            tintSystemBars(colors[4], 1000);
                            tintView(metaColors, colors[4]);
                            metaColorNoteColor = colors[4];
                            actualNote.get().setNoteColor(String.valueOf(colors[4]));
                            metaColors.toggleMenu(true, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.Orange));
                            break;
                        case 5:

                            tintSystemBars(colors[5], 1000);
                            tintView(metaColors, colors[5]);
                            metaColorNoteColor = colors[5];
                            actualNote.get().setNoteColor(String.valueOf(colors[5]));
                            metaColors.toggleMenu(true, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.SkyBlue));
                            break;
                        case 6:

                            tintSystemBars(colors[6], 1000);
                            tintView(metaColors, colors[6]);
                            metaColorNoteColor = colors[6];
                            actualNote.get().setNoteColor(String.valueOf(colors[6]));
                            metaColors.toggleMenu(true, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.YellowSun));
                            break;
                        case 7:

                            tintSystemBars(colors[7], 1000);
                            tintView(metaColors, colors[7]);
                            metaColorNoteColor = colors[7];
                            actualNote.get().setNoteColor(String.valueOf(colors[7]));
                            metaColors.toggleMenu(true, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.GrassGreen));
                            break;
                    }

                    return null;
                });
                break;
            case "highlight":
                colors = getResources().getIntArray(R.array.highlight);
                metaColors.setAdapter(new meatadapter(colors, bkg));
                metaColors.init(false, true, metaColorHighlightColor);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:

                            metaColorHighlightColor = colors[0];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, colors[0]);
                            tintView(metaColors, colors[0]);
                            metaColors.toggleMenu(false, true);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.Black));
                            break;
                        case 1:
                            metaColorHighlightColor = colors[1];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, colors[1]);
                            tintView(metaColors, colors[1]);
                            metaColors.toggleMenu(false, true);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.White));
                            break;
                        case 2:

                            noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, colors[2]);
                            metaColorHighlightColor = colors[2];
                            tintView(metaColors, colors[2]);
                            metaColors.toggleMenu(false, true);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.Magenta));
                            break;
                        case 3:

                            tintView(metaColors, colors[3]);
                            metaColorHighlightColor = colors[3];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, colors[3]);
                            metaColors.toggleMenu(false, true);


                            //  noteCreator.get().activeEditor.get().setTextColorR.color.Pink));
                            break;
                        case 4:

                            noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, colors[4]);
                            metaColorHighlightColor = colors[4];
                            tintView(metaColors, colors[4]);
                            metaColors.toggleMenu(false, true);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.Orange));
                            break;
                        case 5:

                            metaColorHighlightColor = colors[5];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, colors[5]);
                            tintView(metaColors, colors[5]);
                            metaColors.toggleMenu(false, true);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.SkyBlue));
                            break;
                        case 6:

                            metaColorHighlightColor = colors[6];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, colors[6]);
                            tintView(metaColors, colors[6]);
                            metaColors.toggleMenu(false, true);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.YellowSun));
                            break;
                        case 7:

                            metaColorHighlightColor = colors[7];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, colors[7]);
                            tintView(metaColors, colors[7]);
                            metaColors.toggleMenu(false, true);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.GrassGreen));
                            break;
                    }

                    return null;
                });
                break;

            case "foreground":
                colors = getResources().getIntArray(R.array.foreground);
                metaColors.setAdapter(new meatadapter(colors, bkg));
                metaColors.init(false, false, metaColorForeGroundColor);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:
                            metaColorForeGroundColor = colors[0];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[0]);
                            tintView(metaColors, colors[0]);
                            metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.Black));
                            break;
                        case 1:
                            metaColorForeGroundColor = colors[1];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[1]);
                            tintView(metaColors, colors[1]);
                            metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.White));
                            break;
                        case 2:
                            metaColorForeGroundColor = colors[2];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[2]);
                            tintView(metaColors, colors[2]);
                            metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.Magenta));
                            break;
                        case 3:
                            //   tintView(metaColors, GetColor(R.color.Orange));
                            metaColorForeGroundColor = colors[3];
                            tintView(metaColors, colors[3]);
                            noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[3]);
                            metaColors.toggleMenu(false, false);


                            //  noteCreator.get().activeEditor.get().setTextColorR.color.Pink));
                            break;
                        case 4:
                            metaColorForeGroundColor = colors[4];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[4]);
                            tintView(metaColors, colors[4]);
                            metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.Orange));
                            break;
                        case 5:
                            metaColorForeGroundColor = colors[5];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[5]);
                            tintView(metaColors, colors[5]);
                            metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.SkyBlue));
                            break;
                        case 6:
                            metaColorForeGroundColor = colors[6];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[6]);
                            tintView(metaColors, colors[6]);
                            metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.YellowSun));
                            break;
                        case 7:
                            tintView(metaColors, colors[7]);
                            metaColorForeGroundColor = colors[7];
                            noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, colors[7]);
                            metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.GrassGreen));
                            break;
                    }

                    return null;
                });
                break;

            case "draw":
                colors = getResources().getIntArray(R.array.foreground);
                metaColors.setAdapter(new meatadapter(colors, bkg));
                metaColors.init(false, false, metaColorDraw);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:
                            metaColorDraw = colors[0];
                            noteCreator.get().everDraw.get().setColor(colors[0]);
                            tintView(metaColors, colors[0]);
                            metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.Black));
                            break;
                        case 1:
                            metaColorDraw = colors[1];
                            noteCreator.get().everDraw.get().setColor(colors[1]);
                            tintView(metaColors, colors[1]);
                            metaColors.toggleMenu(false, false);
                            //  noteCreator.get().activeEditor.get().setTextColorR.color.White));
                            break;
                        case 2:

                            metaColorDraw = colors[2];
                            noteCreator.get().everDraw.get().setColor(colors[2]);
                            tintView(metaColors, colors[2]);
                            metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.Magenta));
                            break;
                        case 3:

                            //   tintView(metaColors, GetColor(R.color.Orange));
                            metaColorDraw = colors[3];
                            tintView(metaColors, colors[3]);
                            noteCreator.get().everDraw.get().setColor(colors[3]);
                            metaColors.toggleMenu(false, false);


                            //  noteCreator.get().activeEditor.get().setTextColorR.color.Pink));
                            break;
                        case 4:
                            metaColorDraw = colors[4];
                            noteCreator.get().everDraw.get().setColor(colors[4]);
                            tintView(metaColors, colors[4]);
                            metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.Orange));
                            break;
                        case 5:

                            metaColorDraw = colors[5];
                            noteCreator.get().everDraw.get().setColor(colors[5]);
                            tintView(metaColors, colors[5]);
                            metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColorR.color.SkyBlue));
                            break;
                        case 6:

                            metaColorDraw = colors[6];
                            noteCreator.get().everDraw.get().setColor(colors[6]);
                            tintView(metaColors, colors[6]);
                            metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.YellowSun));
                            break;
                        case 7:

                            tintView(metaColors, colors[7]);
                            metaColorDraw = colors[7];
                            noteCreator.get().everDraw.get().setColor(colors[7]);
                            metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColorR.color.GrassGreen));
                            break;
                    }

                    return null;
                });
                break;

            case "paintType":
                colors = getResources().getIntArray(R.array.noteColor);
                metaColors.setAdapter(new meatadapter(colors, bkg));
                metaColors.init(false, false, metaColorDraw);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:
                            noteCreator.get().everDraw.get().paintType("stroke");


                            break;
                        case 1:
                            noteCreator.get().everDraw.get().paintType("fill");
                            break;
                        case 2:

                            noteCreator.get().everDraw.get().paintType("fillStroke");
                            break;
                    }

                    return null;
                });
                break;
        }

    }
    public void selectAllClick(View view) {
        selectAllNotes();
    }
    public void selectAllNotes() {
        if (notesSelected) {
            deselectItems(allSelected);
            notesSelected = false;
        } else {
            if (searchListSection.size() > 0) {
                allSelected.addAll(searchListSection.getData());
            } else {
                allSelected.addAll(noteModelSection.getData());
            }
            selectItems(allSelected);
            notesSelected = true;
        }
    }
    private void selectItems(ListSection<Note_Model> notes) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (int i = 0; i < notes.size(); i++) {
                notes.get(i).setSelected(true);
                noteScreen.adapter.get().notifyItemChanged(i);
            }
        });
    }

    private void deselectItems(ListSection<Note_Model> notes) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (int i = 0; i < notes.size(); i++) {
                notes.get(i).setSelected(false);
                noteScreen.adapter.get().notifyItemChanged(i);
            }

                notes.clear();
                selectedItems.clear();

        });
    }
}

