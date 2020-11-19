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

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class MainActivity extends AppCompatActivity {

    public EverDataBase mDatabaseEver;
    public Integer ID;
    public WeakReference<Note_Model> actualNote;
    public Boolean showNoteContents = false;
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
    public CardView ImporterOptions;
    public CardView ParagraphOptions;
    public CardView FormatOptions;
    public CardView SelectOptions;
    public SmoothBottomBar note_bottom_bar;
    public Animation bottom_nav_anim;
    public Animation bottom_nav_anim_reverse;
    public Boolean DrawVisualizerIsShowing = false;
    public EverCircularColorSelect metaColors;
    public Boolean DrawOn = false;
    public NotesScreen noteScreen;
    public boolean atHome = true;
    public boolean newNote = false;
    public int selectedPosition;
    public int selectedID;
    public WeakReference<CardView> cardNoteCreator;
    public WeakReference<NoteEditorFragmentJavaFragment> noteCreator;
    public WeakReference<RecyclerView> contentRecycler;
    public WeakReference<EditText> title;
    public WeakReference<RecyclerView> imageRecycler;
    public ArrayList<String> names = new ArrayList<>();
    public int noteIdIncrement = 0;
    public Window everMainWindow;
    public ImageButton changeNotecolorButton;
    public ListSection<Note_Model> noteModelSection = new ListSection<>();
    WeakReference<AnimatedEditText> edit ;
    public WeakReference<MultiViewAdapter> adapter ;
    public WeakReference<RecyclerView> recyclertest;
    private AppBarConfiguration mAppBarConfiguration;
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
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    private boolean formatOptions = false;
    private boolean paragraphOptions = false;
    private boolean importerOptions = false;
    private boolean drawOptions = false;
    private boolean drawsize = false;
    public boolean pushed = false;
    public RTManager mRTManager;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean striketrough = false;
    private boolean numbers = false;
    private boolean bullets = false;
    private final int DOY = 0;
    private final int DCY = 350;
    public boolean isGrid = true;
    public boolean searching = false;
    private int actualColor;
    public BlurLayout blur;
    public int metaColorNoteColor;
    public int metaColorDraw;
    public int metaColorHighlightColor;
    public int metaColorForeGroundColor;
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Drawable> bkg = new ArrayList<>();
    private ArrayList<Integer> colorsHighlight = new ArrayList<>();
    public ListSection<Note_Model> searchListSection = new ListSection<>();
    public EverRecordAudio recorder;
    private int searchBoxWidth;

    public boolean swipeChangeColorRefresh = false;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ClickableViewAccessibility", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabaseEver = new EverDataBase(this);

        everMainWindow = getWindow();



        RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, false));
        mRTManager = new RTManager(rtApi, savedInstanceState);


        preferences = getApplicationContext().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        editor = preferences.edit();

        //TODO: optimize performance a little more and make sure everything that we changed is working as intended

        if (getIntent().hasExtra("notes")) {
            //   notesModels = (ArrayList<Note_Model>) getIntent().getSerializableExtra("notes");
            noteModelSection.addAll((ArrayList<Note_Model>) getIntent().getSerializableExtra("notes"));
            noteScreen = new NotesScreen();
            if (noteModelSection.size() > 0) {
                noteIdIncrement = preferences.getInt("lastID", 0);
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

        isGrid = preferences.getBoolean("isGrid", true);

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
        colorsHighlight.add(getColor(R.color.White));
        colorsHighlight.add(getColor(R.color.YellowSunHighlight));
        colorsHighlight.add(getColor(R.color.OrangeHighlight));
        colorsHighlight.add(getColor(R.color.PinkHighlight));
        colorsHighlight.add(getColor(R.color.PurpleHighlight));
        colorsHighlight.add(getColor(R.color.MagentaHighlight));
        colorsHighlight.add(getColor(R.color.SkyBlueHighlight));
        colorsHighlight.add(getColor(R.color.GrassGreenHighlight));
        colors.add(getColor(R.color.White));
        colors.add(getColor(R.color.Black));
        colors.add(getColor(R.color.YellowSun));
        colors.add(getColor(R.color.Orange));
        colors.add(getColor(R.color.Magenta));
        colors.add(getColor(R.color.Pink));
        colors.add(getColor(R.color.SkyBlue));
        colors.add(getColor(R.color.GrassGreen));
        bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));
        bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));
        bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));
        bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));
        bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));
        bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));
        bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));
        bkg.add(ResourcesCompat.getDrawable(getResources(), R.drawable.circle_colors, null));


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

            searchBoxWidth = edit.get().getMeasuredWidth();
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

        //  if (!actualNote.getNoteColor().equals("000000")) {
        if (actualColor != defaultToolbarColor) {
          //  tintSystemBars(defaultToolbarColor);
            tintSystemBars(defaultToolbarColor , 500);
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

    public void onClick(View v) {

        noteIdIncrement++;
        editor.putInt("lastID", noteIdIncrement);
        editor.apply();
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new Fade());
            noteScreen.setExitTransition(new Fade());
        }
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
    }

    public void animateWidthChange(View view, int duration, int amount) {
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
    }

    public void CloseOrOpenDrawOptions(int height, boolean close) {


        noteCreator.get().drawFromRecycler = false;

        if (close) {

           if (!actualNote.get().getNoteColor().equals("000000"))  tintView(noteCreator.get().cardView.get(), Integer.parseInt(actualNote.get().getNoteColor()));

            //    TransitionManager.beginDelayedTransition(cardNoteCreator, new TransitionSet()
            //           .addTransition(new ChangeBounds()));

            ResizeCardViewToWrapContent();

            CloseOrOpenDrawOptions(true);

            CloseOrOpenNoteContents();

            if (!actualNote.get().getNoteColor().equals("000000")) {
                cardNoteCreator.get().setCardBackgroundColor(Integer.parseInt(actualNote.get().getNoteColor()));
            }


            CloseOrOpenDrawSize(close);

            DrawOn = false;

            drawOptions = false;

        } else {

            tintView(noteCreator.get().cardView.get(), defaultToolbarColor);

            CloseOrOpenDrawOptions(false);

            ResizeEverDrawToPrepareNoteToDraw(height);

            cardNoteCreator.get().setCardBackgroundColor(defaultToolbarColor);

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
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                noteCreator.get().scrollView.get().smoothScrollBy(0, -150);
            }, 50);

        } else {
          //  animateObject(FormatOptions, "translationY", DOY, 350);

            animateHeightChange(FormatOptions, 500, 150);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                noteCreator.get().scrollView.get().smoothScrollBy(0, 150);
            }, 50);


        }
    }

    public void CloseOrOpenSelectionOptions(boolean Close) {
        if (Close) {
           animateHeightChange(SelectOptions, 500, 1);
        } else {
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
        } else {
          //  animateObject(ParagraphOptions, "translationY", DOY, 350);
            animateHeightChange(ParagraphOptions, 500, 150);
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
        } else {
           // animateObject(ImporterOptions, "translationY", DOY, 350);
            animateHeightChange(ImporterOptions, 500, 200);
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
        if (color == defaultToolbarColor) {
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
           } else if (view == metaColors){
               System.out.println("a");
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
            searchListSection.set(p, note);
        } else {
            if (noteModelSection.size() > p) {
                noteModelSection.set(p, note);
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
        noteModelSection.remove(note.getActualPosition());
        for (int position = 0; position < noteModelSection.size(); position++) {
            noteModelSection.get(position).setActualPosition(position);
        }
        // adapter.notifyItemRemoved(p);
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
        for (String path: note.getImages()) {
            File f = new File(path);
            if (f.exists()) {
                System.out.println("Image at = " + f.getPath() + " Deleted.");
                f.delete();
            }
        }
        for (String path: note.getDraws()) {
            File f = new File(path);
            if (f.exists()) {
                System.out.println("Draw at = " + f.getPath() + " Deleted.");
                f.delete();
            }
        }
    }

    public void deleteSelection(View view) {
        List<Note_Model> list = noteModelSection.getSelectedItems();
        if (list.size() > 0) {
            for (Note_Model note : list) {
                deleteDrawsAndImagesInsideNote(note);
                removeNote(note);
            }
            noteModelSection.clearSelections();
        } else {
            List<Note_Model> list2 = searchListSection.getSelectedItems();
            if (list2.size() > 0) {
                for (Note_Model note : list2) {
                    deleteDrawsAndImagesInsideNote(note);
                    removeNote(note);
                }
                searchListSection.clearSelections();
            }
        }


        CloseOrOpenSelectionOptions(true);
        new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
    }

    public void openColorSelectorSelection(View view) {
        createPopupMenu(view, R.layout.selection_color_change_popup, true, "popup", 0, 0);
    }

    public void changeColorSelection(int color) {
        List<Note_Model> list = noteModelSection.getSelectedItems();
        if (list.size() > 0) {
            for (Note_Model note : list) {
                selectedPosition = note.getActualPosition();
                selectedID = note.getId();
                ChangeNoteColor(color);
            }
            noteModelSection.clearSelections();
        } else {
            List<Note_Model> list2 = searchListSection.getSelectedItems();
            if (list2.size() > 0) {
                for (Note_Model note : list2) {
                    selectedPosition = note.getActualPosition();
                    selectedID = note.getId();
                    ChangeNoteColor(color);
                }
            }
            searchListSection.clearSelections();
        }

        CloseOrOpenSelectionOptions(true);
        new Handler(Looper.getMainLooper()).postDelayed(() -> pushed = false, 250);
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
            System.out.println(searchBoxWidth);
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
           editor.putBoolean("isGrid", false);
           editor.apply();
           isGrid = false;
       } else {
           editor.putBoolean("isGrid", true);
           editor.apply();
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
        } if (view.getTag().equals("Search")) {
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
        } if (view.getTag().equals("GridLayout")) {
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

            File record = new File(getFilesDir().getPath() + "/recordEver/" + "recordN"+System.currentTimeMillis()+".3gp");
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
                metaColors.setAdapter(new meatadapter(colors, bkg));
                metaColors.init(true, false, metaColorNoteColor);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:

                                tintSystemBars(GetColor(R.color.White), 1000);
                                tintView(metaColors, GetColor (R.color.White));
                                metaColorNoteColor = (GetColor(R.color.White));
                                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.White)));
                                metaColors.toggleMenu(true, false);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Black));
                            break;
                        case 1:

                                tintSystemBars(GetColor(R.color.Black), 1000);
                                tintView(metaColors, GetColor(R.color.Black));
                                metaColorNoteColor = (GetColor(R.color.Black));
                                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.Black)));
                                metaColors.toggleMenu(true, false);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.White));
                            break;
                        case 2:

                                tintSystemBars(GetColor(R.color.YellowSun), 1000);
                                tintView(metaColors, GetColor(R.color.YellowSun));
                                metaColorNoteColor = (GetColor(R.color.YellowSun));
                                actualNote.get().setNoteColor(String.valueOf(R.color.YellowSun));
                                metaColors.toggleMenu(true, false);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Magenta));
                            break;
                        case 3:
                                tintSystemBars(GetColor(R.color.Orange), 1000);
                                tintView(metaColors, GetColor (R.color.Orange));
                                metaColorNoteColor = (GetColor(R.color.Orange));
                                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.Orange)));
                                metaColors.toggleMenu(true, false);


                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Pink));
                            break;
                        case 4:

                                tintSystemBars(GetColor(R.color.Magenta), 1000);
                                tintView(metaColors, GetColor (R.color.Magenta));
                                metaColorNoteColor = (GetColor(R.color.Magenta));
                                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.Magenta)));
                                metaColors.toggleMenu(true, false);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Orange));
                            break;
                        case 5:

                                tintSystemBars(GetColor(R.color.Pink), 1000);
                                tintView(metaColors, GetColor(R.color.Pink));
                                metaColorNoteColor = (GetColor(R.color.Pink));
                                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.Pink)));
                                metaColors.toggleMenu(true, false);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.SkyBlue));
                            break;
                        case 6:

                                tintSystemBars(GetColor(R.color.SkyBlue), 1000);
                                tintView(metaColors, GetColor (R.color.SkyBlue));
                                metaColorNoteColor = (GetColor(R.color.SkyBlue));
                                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.SkyBlue)));
                                metaColors.toggleMenu(true, false);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.YellowSun));
                            break;
                        case 7:

                                tintSystemBars(GetColor(R.color.GrassGreen), 1000);
                                tintView(metaColors, GetColor (R.color.GrassGreen));
                                metaColorNoteColor = GetColor (R.color.GrassGreen);
                                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.GrassGreen)));
                                metaColors.toggleMenu(true, false);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.GrassGreen));
                            break;
                    }

                    return null;
                });
                break;
            case "highlight":
                metaColors.setAdapter(new meatadapter(colorsHighlight, bkg));
                metaColors.init(false, true, metaColorHighlightColor);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:

                                metaColorHighlightColor =(GetColor(R.color.White));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.White));
                                tintView(metaColors, GetColor (R.color.White));
                                metaColors.toggleMenu(false, true);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Black));
                            break;
                        case 1:
                                metaColorHighlightColor =(GetColor(R.color.YellowSunHighlight));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.YellowSunHighlight));
                                tintView(metaColors, GetColor(R.color.YellowSunHighlight));
                                metaColors.toggleMenu(false, true);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.White));
                            break;
                        case 2:

                                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.OrangeHighlight));
                                metaColorHighlightColor = (GetColor(R.color.OrangeHighlight));
                                tintView(metaColors, GetColor(R.color.OrangeHighlight));
                                metaColors.toggleMenu(false, true);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Magenta));
                            break;
                        case 3:

                                tintView(metaColors, GetColor (R.color.PinkHighlight));
                                metaColorHighlightColor = ( GetColor(R.color.PinkHighlight));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.PinkHighlight));
                                metaColors.toggleMenu(false, true);


                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Pink));
                            break;
                        case 4:

                                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.PurpleHighlight));
                                metaColorHighlightColor = (GetColor(R.color.PurpleHighlight));
                                tintView(metaColors, GetColor (R.color.PurpleHighlight));
                                metaColors.toggleMenu(false, true);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Orange));
                            break;
                        case 5:

                                metaColorHighlightColor = (GetColor(R.color.MagentaHighlight));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.MagentaHighlight));
                                tintView(metaColors, GetColor (R.color.MagentaHighlight));
                                metaColors.toggleMenu(false, true);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.SkyBlue));
                            break;
                        case 6:

                                metaColorHighlightColor = (GetColor(R.color.SkyBlueHighlight));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.SkyBlueHighlight));
                                tintView(metaColors, GetColor(R.color.SkyBlueHighlight));
                                metaColors.toggleMenu(false, true);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.YellowSun));
                            break;
                        case 7:

                                metaColorHighlightColor = (GetColor(R.color.GrassGreenHighlight));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.GrassGreenHighlight));
                                tintView(metaColors, GetColor(R.color.GrassGreenHighlight));
                                metaColors.toggleMenu(false, true);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.GrassGreen));
                            break;
                    }

                    return null;
                });
                break;
                
            case "foreground":
                metaColors.setAdapter(new meatadapter(colors, bkg));
                metaColors.init(false, false, metaColorForeGroundColor);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:
                                metaColorForeGroundColor = (GetColor(R.color.White));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.White));
                                tintView(metaColors, GetColor (R.color.White));
                                metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Black));
                            break;
                        case 1:
                                metaColorForeGroundColor = (GetColor(R.color.Black));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.Black));
                                tintView(metaColors, GetColor(R.color.Black));
                                metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.White));
                            break;
                        case 2:
                                metaColorForeGroundColor = (GetColor(R.color.YellowSun));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.YellowSun));
                                tintView(metaColors, GetColor(R.color.YellowSun));
                                metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Magenta));
                            break;
                        case 3:
                                //   tintView(metaColors, GetColor(R.color.Orange));
                                metaColorForeGroundColor = (GetColor(R.color.Orange));
                                tintView(metaColors, GetColor (R.color.Orange));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.Orange));
                                metaColors.toggleMenu(false, false);


                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Pink));
                            break;
                        case 4:
                                metaColorForeGroundColor = (GetColor(R.color.Magenta));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.Magenta));
                                tintView(metaColors, GetColor (R.color.Magenta));
                                metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Orange));
                            break;
                        case 5:
                                metaColorForeGroundColor = (GetColor(R.color.Pink));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.Pink));
                                tintView(metaColors, GetColor(R.color.Pink));
                                metaColors.toggleMenu(false, false);

                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.SkyBlue));
                            break;
                        case 6:
                                metaColorForeGroundColor = (GetColor(R.color.SkyBlue));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.SkyBlue));
                                tintView(metaColors, GetColor (R.color.SkyBlue));
                                metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.YellowSun));
                            break;
                        case 7:
                                tintView(metaColors, GetColor (R.color.GrassGreen));
                                metaColorForeGroundColor = (GetColor(R.color.GrassGreen));
                                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.GrassGreen));
                                metaColors.toggleMenu(false, false);

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.GrassGreen));
                            break;
                    }

                    return null;
                });
                break;
                
            case "draw":
                metaColors.setAdapter(new meatadapter(colors, bkg));
                metaColors.init(false, false, metaColorDraw);
                metaColors.setOnItemSelectedListener(integer -> {
                    switch (integer) {
                        case 0:
                                metaColorDraw = (GetColor(R.color.White));
                                noteCreator.get().everDraw.get().setColor(GetColor(R.color.White));
                                tintView(metaColors, GetColor (R.color.White));
                                metaColors.toggleMenu(false, false);
                            
                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Black));
                            break;
                        case 1:
                            metaColorDraw = (GetColor(R.color.Black));
                                noteCreator.get().everDraw.get().setColor(GetColor(R.color.Black));
                                tintView(metaColors, GetColor(R.color.Black));
                                metaColors.toggleMenu(false, false);
                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.White));
                            break;
                        case 2:

                            metaColorDraw = (GetColor(R.color.YellowSun));
                                noteCreator.get().everDraw.get().setColor(GetColor(R.color.YellowSun));
                                tintView(metaColors, GetColor(R.color.YellowSun));
                                metaColors.toggleMenu(false, false);
                            
                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Magenta));
                            break;
                        case 3:
                          
                                //   tintView(metaColors, GetColor(R.color.Orange));
                            metaColorDraw = (GetColor(R.color.Orange));
                                tintView(metaColors, GetColor (R.color.Orange));
                                noteCreator.get().everDraw.get().setColor(GetColor(R.color.Orange));
                                metaColors.toggleMenu(false, false);
                            

                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Pink));
                            break;
                        case 4:
                            metaColorDraw = (GetColor(R.color.Magenta));
                                noteCreator.get().everDraw.get().setColor(GetColor(R.color.Magenta));
                                tintView(metaColors, GetColor (R.color.Magenta));
                                metaColors.toggleMenu(false, false);
                            
                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Orange));
                            break;
                        case 5:

                            metaColorDraw = (GetColor(R.color.Pink));
                                noteCreator.get().everDraw.get().setColor(GetColor(R.color.Pink));
                                tintView(metaColors, GetColor(R.color.Pink));
                                metaColors.toggleMenu(false, false);
                            
                            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.SkyBlue));
                            break;
                        case 6:

                            metaColorDraw = (GetColor(R.color.SkyBlue));
                                noteCreator.get().everDraw.get().setColor(GetColor(R.color.SkyBlue));
                                tintView(metaColors, GetColor (R.color.SkyBlue));
                                metaColors.toggleMenu(false, false);
                            
                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.YellowSun));
                            break;
                        case 7:
                           
                                tintView(metaColors, GetColor (R.color.GrassGreen));
                            metaColorDraw = (GetColor(R.color.GrassGreen));
                                noteCreator.get().everDraw.get().setColor(GetColor(R.color.GrassGreen));
                                metaColors.toggleMenu(false, false);
                            
                            //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.GrassGreen));
                            break;
                    }

                    return null;
                });
                break;

            case "paintType":
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
}

