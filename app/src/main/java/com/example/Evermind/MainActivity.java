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
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
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

import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.RTManager;
import com.example.Evermind.TESTEDITOR.rteditor.RTToolbarListener;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTApi;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTMediaFactoryImpl;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTProxyImpl;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effect;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effects;
import com.example.Evermind.TESTEDITOR.rteditor.spans.RTSpan;
import com.example.Evermind.TESTEDITOR.toolbar.HorizontalRTToolbar;
import com.example.Evermind.TESTEDITOR.toolbar.RTToolbarImageButton;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.example.Evermind.ui.note_screen.NotesScreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.koushikdutta.ion.Ion;
import com.thekhaeng.pushdownanim.PushDownAnim;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.blurry.Blurry;
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
    public ImageButton BlackDraw;
    public ImageButton BlueDraw;
    public ImageButton PurpleDraw;
    public ImageButton MagentaDraw;
    public ImageButton OrangeDraw;
    public ImageButton YellowDraw;
    public ImageButton GreenDraw;
    public ImageButton BlackHighlight;
    public ImageButton BlueHighlight;
    public ImageButton PurpleHighlight;
    public ImageButton MagentaHighlight;
    public ImageButton OrangeHighlight;
    public ImageButton YellowHighlight;
    public ImageButton GreenHighlight;
    public ImageButton Black;
    public ImageButton Blue;
    public ImageButton Purple;
    public ImageButton Magenta;
    public ImageButton Orange;
    public ImageButton Yellow;
    public ImageButton Green;
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
    private Switch switchtest;
    public ImageButton ClearHighlight;
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
    public WeakReference<CardView> cardNoteCreator;
    public WeakReference<NoteEditorFragmentJavaFragment> noteCreator;
    public WeakReference<RecyclerView> contentRecycler;
    public WeakReference<EditText> title;
    public WeakReference<RecyclerView> imageRecycler;
    //   public ArrayList<Note_Model> notesModels = new ArrayList<>();
    public ArrayList<String> names = new ArrayList<>();
    public int noteIdIncrement = 0;
    public EverAdapter everAdapter;
    public Window everMainWindow;
    public ImageButton drawColor;
    public ImageButton drawSize;
    public ListSection<Note_Model> noteModelSection = new ListSection<>();
    WeakReference<EditText> edit ;
    public WeakReference<MultiViewAdapter> adapter = new WeakReference<>(new MultiViewAdapter());
    public WeakReference<RecyclerView> recyclertest;
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
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    private boolean formatOptions = false;
    private boolean paragraphOptions = false;
    private boolean importerOptions = false;
    private boolean drawOptions = false;
    private boolean drawsize = false;
    public boolean pushed = true;
    public RTManager mRTManager;
    private boolean bold = false;
    private boolean italic = false;
    private boolean underline = false;
    private boolean striketrough = false;
    private boolean numbers = false;
    private boolean bullets = false;
    private final int DOY = 0;
    private final int DCY = 350;
    private boolean switchToolbars = false;
    public boolean isGrid = true;
    public boolean searching = false;
    public ListSection<Note_Model> newList = new ListSection<>();


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
        adapter.get().registerItemBinders(new NoteModelBinder(this));
        adapter.get().addSection(noteModelSection);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper()).post(() -> {

            //  giphyLibrary = new GiphyLibrary();

            Fresco.initialize(this);

        });

        atHome = true;

        isGrid = preferences.getBoolean("isGrid", true);

        //   new Thread(() -> {
        // a potentially time consuming task
     //    RTToolbarImageButton changecolorToolbar = findViewById(R.id.toolbar_bold);
      //   RTToolbarImageButton changeFontColorToolbar = findViewById(R.id.toolbar_bold);
     ////    RTToolbarImageButton increaseSizetoolbar = findViewById(R.id.toolbar_bold);
     //    RTToolbarImageButton fontTypeToolbar = findViewById(R.id.toolbar_bold);
        note_bottom_bar = findViewById(R.id.note_bottom_bar);
        bottom_nav_anim = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim);
        bottom_nav_anim_reverse = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim_reverse);
        DrawChangeColor = findViewById(R.id.DrawChangeColor);
        DrawChangeSize = findViewById(R.id.DrawChangeSize);
        size_visualizer = findViewById(R.id.draw_sizeVisualizerCardView);
        ImageSizeView = findViewById(R.id.draw_size_visualizer);
        seekBarDrawSize = findViewById(R.id.draw_size_seekbar);
        BlackDraw = findViewById(R.id.Drawblack);
        BlueDraw = findViewById(R.id.Drawblue);
        PurpleDraw = findViewById(R.id.Drawpurple);
        MagentaDraw = findViewById(R.id.Drawmagenta);
        OrangeDraw = findViewById(R.id.Draworange);
        YellowDraw = findViewById(R.id.Drawyellow);
        GreenDraw = findViewById(R.id.Drawgreen);
        BlackHighlight = findViewById(R.id.blackhighlight);
        BlueHighlight = findViewById(R.id.bluehighlight);
        PurpleHighlight = findViewById(R.id.purplehighlight);
        MagentaHighlight = findViewById(R.id.magentahighlight);
        OrangeHighlight = findViewById(R.id.orangehighlight);
        YellowHighlight = findViewById(R.id.yellowhighlight);
        GreenHighlight = findViewById(R.id.greenhighlight);
        Black = findViewById(R.id.black);
        Blue = findViewById(R.id.blue);
        Purple = findViewById(R.id.purple);
        Magenta = findViewById(R.id.magenta);
        Orange = findViewById(R.id.orange);
        Yellow = findViewById(R.id.yellow);
        Green = findViewById(R.id.green);
        IncreaseSize = findViewById(R.id.IncreaseSize1);
        DecreaseSize = findViewById(R.id.DecreaseSize1);
        DrawOptions = findViewById(R.id.draw_options);
        FormatOptions = findViewById(R.id.format_selectors);
        ClearHighlight = findViewById(R.id.clearhighlight);
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
        drawColor = findViewById(R.id.drawColor);
        selectionChangeColor = findViewById(R.id.selectChangeColor);
        selectionDelete = findViewById(R.id.selectDelete);
        edit = new WeakReference<>(findViewById(R.id.searchBox));
        edit.get().setFocusable(false);
      //  OverScrollDecoratorHelper.setUpOverScroll(scrollView1);


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

        DrawChangeSize.setOnClickListener(v -> {
            if (drawsize) {
                CloseOrOpenDrawSize(true);
                drawsize = false;
            } else {
                CloseOrOpenDrawSize(false);
                drawsize = true;
            }
        });

        drawColor.setOnClickListener(v -> createPopupMenu(v, R.layout.draw_options_popup, true, "dropdown", 0, 0));

        note_bottom_bar.setOnNavigationItemSelectedListener(item -> {
            int id_nav = item.getItemId();


            switch (id_nav) {
                case R.id.nav_formatText:
                    if (formatOptions) {
                        CloseOrOpenFormatOptions(true);
                        formatOptions = false;
                    } else {
                        CloseOrOpenFormatOptions(false);
                        formatOptions = true;
                    }
                    break;

                case R.id.nav_paragraph:
                    if (paragraphOptions) {
                        CloseOrOpenParagraphOptions(true);
                        paragraphOptions = false;
                    } else {
                        CloseOrOpenParagraphOptions(false);
                        paragraphOptions = true;
                    }
                    break;

                case R.id.nav_checkbox:
                    if (importerOptions) {
                        CloseOrOpenImporterOptions(true);
                        importerOptions = false;
                    } else {
                        CloseOrOpenImporterOptions(false);
                        importerOptions = true;
                    }
                    break;

                case R.id.nav_bullets:
                    break;

                case R.id.nav_draw:
                    if (drawOptions) {
                        CloseOrOpenDrawOptions(0, true);
                    } else {
                        CloseOrOpenDrawOptions(1400, false);
                        noteCreator.get().everDraw = new WeakReference<>(findViewById(R.id.EverDraw));
                        InputMethodManager keyboard1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        keyboard1.hideSoftInputFromWindow(seekBarDrawSize.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                default:
                    return true;
            }
            return true;
        });
        HorizontalRTToolbar toolbar1 = new HorizontalRTToolbar(this);
        mRTManager.registerToolbar(null, toolbar1);
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
          //  tintSystemBars(defaultToolbarColor);
            tintView(toolbar, defaultToolbarColor);
            tintView(note_bottom_bar, defaultToolbarColor);
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
            new Handler(Looper.getMainLooper()).post(super::onBackPressed);
        }, 100);

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
                    note_bottom_bar.setVisibility(View.VISIBLE);
                    bottomBarUp = true;
                }
            } else {
                note_bottom_bar.setVisibility(View.GONE);
                bottomBarUp = false;
            }

            CloseOrOpenToolbarUndoRedo(!showUndoRedo);

        } else {
            note_bottom_bar.setVisibility(View.GONE);
            toolbarDown = false;
            bottomBarUp = false;
        }
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

            CloseOrOpenDrawColors(close);

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

            animateObject(DrawOptions, "translationY", DCY, 350);
            CloseOrOpenDrawColors(close);
            CloseOrOpenDrawSize(close);
            DrawOn = false;
            scroll.setCanScroll(true);
            recyclerView.suppressLayout(false);

        } else {

            animateObject(DrawOptions, "translationY", DOY, 350);

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
            animateObject(DrawOptions, "translationY", DCY, 350);
        } else {
            animateObject(DrawOptions, "translationY", DOY-50, 350);
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
            animateObject(FormatOptions, "translationY", DCY, 350);
        } else {
            animateObject(FormatOptions, "translationY", DOY, 350);
        }
    }

    public void CloseOrOpenSelectionOptions(boolean Close) {
        if (Close) {
            animateObject(SelectOptions, "translationY", DCY, 350);
        } else {
            animateObject(SelectOptions, "translationY", DOY, 350);
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
            animateObject(ParagraphOptions, "translationY", DCY, 350);
        } else {
            animateObject(ParagraphOptions, "translationY", DOY, 350);
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
            animateObject(ImporterOptions, "translationY", DCY+50, 350);
        } else {
            animateObject(ImporterOptions, "translationY", DOY, 350);
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

    public void CloseOrOpenEditorColors(boolean CloseOpenedEditorColors) {

        if (CloseOpenedEditorColors) {

            Black.setVisibility(View.GONE);
            Blue.setVisibility(View.GONE);
            Purple.setVisibility(View.GONE);
            Magenta.setVisibility(View.GONE);
            Orange.setVisibility(View.GONE);
            Yellow.setVisibility(View.GONE);
            Green.setVisibility(View.GONE);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                changeColor.setVisibility(View.VISIBLE);
                Bold.setVisibility(View.VISIBLE);
                Italic.setVisibility(View.VISIBLE);
                Underline.setVisibility(View.VISIBLE);
                StrikeThrough.setVisibility(View.VISIBLE);
                Highlight.setVisibility(View.VISIBLE);
                IncreaseSize.setVisibility(View.VISIBLE);
                DecreaseSize.setVisibility(View.VISIBLE);


            }, 150);

        } else {

            changeColor.setVisibility(View.GONE);
            Bold.setVisibility(View.GONE);
            Italic.setVisibility(View.GONE);
            Underline.setVisibility(View.GONE);
            StrikeThrough.setVisibility(View.GONE);
            Highlight.setVisibility(View.GONE);
            IncreaseSize.setVisibility(View.GONE);
            DecreaseSize.setVisibility(View.GONE);

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
        }


    }

    public void CloseOrOpenEditorHIghlightColors(boolean CloseOpenedEditorHighlightColors) {

        if (CloseOpenedEditorHighlightColors) {

            BlackHighlight.setVisibility(View.GONE);
            BlueHighlight.setVisibility(View.GONE);
            PurpleHighlight.setVisibility(View.GONE);
            MagentaHighlight.setVisibility(View.GONE);
            OrangeHighlight.setVisibility(View.GONE);
            YellowHighlight.setVisibility(View.GONE);
            GreenHighlight.setVisibility(View.GONE);
            ClearHighlight.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                changeColor.setVisibility(View.VISIBLE);
                Bold.setVisibility(View.VISIBLE);
                Italic.setVisibility(View.VISIBLE);
                Underline.setVisibility(View.VISIBLE);
                StrikeThrough.setVisibility(View.VISIBLE);
                Highlight.setVisibility(View.VISIBLE);
                IncreaseSize.setVisibility(View.VISIBLE);
                DecreaseSize.setVisibility(View.VISIBLE);

            }, 150);

        } else {

            changeColor.setVisibility(View.GONE);
            Bold.setVisibility(View.GONE);
            IncreaseSize.setVisibility(View.GONE);
            DecreaseSize.setVisibility(View.GONE);
            Italic.setVisibility(View.GONE);
            Underline.setVisibility(View.GONE);
            StrikeThrough.setVisibility(View.GONE);
            Highlight.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // ChangeColor.setVisibility(View.GONE);

                BlackHighlight.setVisibility(View.VISIBLE);
                BlueHighlight.setVisibility(View.VISIBLE);
                PurpleHighlight.setVisibility(View.VISIBLE);
                MagentaHighlight.setVisibility(View.VISIBLE);
                OrangeHighlight.setVisibility(View.VISIBLE);
                YellowHighlight.setVisibility(View.VISIBLE);
                GreenHighlight.setVisibility(View.VISIBLE);
                ClearHighlight.setVisibility(View.VISIBLE);


            }, 150);
        }


    }

    public void CloseAllButtons() {

        if (noteCreator.get().scrollView.get() != null) {
            noteCreator.get().scrollView.get().smoothScrollTo(0, 0);
        }

        if (bottomBarUp) {
            switchToolbars(false, false, false);
        } else {
            if (toolbarDown) {
                switchToolbars(false, false, false);
            }
        }

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
                CloseOrOpenEditorColors(false);
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
                CloseOrOpenEditorHIghlightColors(false);
                break;
            case "clearHighlight":
               // noteCreator.get().activeEditor.get().setTextBackgroundColor(Color.WHITE);
                break;
        }
        // popupWindowHelper.dismiss();
    }

    public void colorChangeClick(View view) {
        switch (view.getTag().toString()) {
            case "black":

                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.Black));
              //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Black));
                break;
            case "white":
                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.White));
              //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.White));
                break;
            case "magenta":
                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.Magenta));
            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Magenta));
                break;
            case "purple":
                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.Pink));
              //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Pink));
                break;
            case "orange":
                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.Orange));
            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.Orange));
                break;
            case "blue":
                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.SkyBlue));
            //    noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.SkyBlue));
                break;
            case "yellow":
                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.YellowSun));
              //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.YellowSun));
                break;
            case "green":
                noteCreator.get().activeEditor.get().applyEffect(Effects.FONTCOLOR, GetColor(R.color.GrassGreen));
              //  noteCreator.get().activeEditor.get().setTextColor(GetColor(R.color.GrassGreen));
                break;
        }
        CloseOrOpenEditorColors(true);
    }

    public void noteColorChange(View view) {

        switch (view.getTag().toString()) {
            case "black":
                tintSystemBars(GetColor(R.color.Black));
                actualNote.get().setNoteColor(String.valueOf(R.color.Black));
                break;

            case "white":
                tintSystemBars(GetColor(R.color.White));
                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.White)));
                break;

            case "magenta":
                tintSystemBars(GetColor(R.color.Magenta));
                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.Magenta)));
                break;

            case "purple":
                tintSystemBars(GetColor(R.color.Pink));
                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.Pink)));
                break;

            case "orange":
                tintSystemBars(GetColor(R.color.Orange));
                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.Orange)));
                break;

            case "blue":
                tintSystemBars(GetColor(R.color.SkyBlue));
                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.SkyBlue)));
                break;

            case "yellow":
                tintSystemBars(GetColor(R.color.YellowSun));
                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.YellowSun)));
                break;

            case "green":
                tintSystemBars(GetColor(R.color.GrassGreen));
                actualNote.get().setNoteColor(String.valueOf(GetColor(R.color.GrassGreen)));
                break;
        }
    }

    public void drawColorChange(View view) {

        switch (view.getTag().toString()) {
            case "black":
                noteCreator.get().everDraw.get().setColor(GetColor(R.color.Black));
                break;

            case "white":
                noteCreator.get().everDraw.get().setColor(GetColor(R.color.White));
                break;

            case "magenta":
                noteCreator.get().everDraw.get().setColor(GetColor(R.color.Magenta));
                break;

            case "purple":
                noteCreator.get().everDraw.get().setColor(GetColor(R.color.Pink));
                break;

            case "orange":
                noteCreator.get().everDraw.get().setColor(GetColor(R.color.Orange));
                break;

            case "blue":
                noteCreator.get().everDraw.get().setColor(GetColor(R.color.SkyBlue));
                break;

            case "yellow":
                noteCreator.get().everDraw.get().setColor(GetColor(R.color.YellowSun));
                break;

            case "green":
                noteCreator.get().everDraw.get().setColor(GetColor(R.color.GrassGreen));
                break;
        }
        CloseOrOpenDrawColors(true);
    }

    public void highlightColorChangeClick(View view) {
        switch (view.getTag().toString()) {
            case "blackHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.Black));
             //   noteCreator.get().activeEditor.get().setTextBackgroundColor(GetColor(R.color.Black));
                break;

            case "whiteHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.White));
             //   noteCreator.get().activeEditor.get().setTextBackgroundColor(GetColor(R.color.White));
                break;

            case "magentaHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.Magenta));
             //   noteCreator.get().activeEditor.get().setTextBackgroundColor(GetColor(R.color.Magenta));
                break;

            case "purpleHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.Pink));
                //noteCreator.get().activeEditor.get().setTextBackgroundColor(GetColor(R.color.Pink));
                break;

            case "orangeHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.Orange));
            //    noteCreator.get().activeEditor.get().setTextBackgroundColor(GetColor(R.color.Orange));
                break;

            case "blueHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.SkyBlue));
            //    noteCreator.get().activeEditor.get().setTextBackgroundColor(GetColor(R.color.SkyBlue));
                break;

            case "yellowHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.YellowSun));
             //   noteCreator.get().activeEditor.get().setTextBackgroundColor(GetColor(R.color.YellowSun));
                break;

            case "greenHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.GrassGreen));
             //   noteCreator.get().activeEditor.get().setTextBackgroundColor(GetColor(R.color.GrassGreen));
                break;

            case "clearHighlight":
                noteCreator.get().activeEditor.get().applyEffect(Effects.BGCOLOR, GetColor(R.color.White));
            //    noteCreator.get().activeEditor.get().setTextBackgroundColor(Color.WHITE);
                break;

        }
        CloseOrOpenEditorHIghlightColors(true);
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
        noteModelSection.get(selectedPosition).setNoteColor(String.valueOf(color));
        updateNote(selectedPosition, noteModelSection.get(selectedPosition));
    }

    public void ClearIonCache() {
        new Thread(() -> {
                Ion.getDefault(this).getBitmapCache().clear();
                Ion.getDefault(this).getCache().clear();
                ////  Glide.get(this).clearMemory();
                //  Glide.get(this).clearDiskCache();
        }).start();
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
            cardNoteCreator.get().setBackgroundTintList(ColorStateList.valueOf(blended));
        });

        anim.setDuration(500).start();
    }

    public void tintView(View view, int color) {

        setDarkStatusBar();

        // Initial colors of each system bar.
        final int statusBarColor = view.getBackgroundTintList().getDefaultColor();
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
            // Apply blended color to the ActionBar.
            //   blended = blendColors(toolbarColor, toolbarToColor, position);
            //  ColorDrawable background = new ColorDrawable(blended);
            //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
            view.setBackgroundTintList(ColorStateList.valueOf(blended));
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

            cardNoteCreator.get().setTranslationY(blended);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            transAnimation2.setInterpolator(new OvershootInterpolator(1f));
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
        if (noteModelSection.size() > p) {
            noteModelSection.set(p, note);
        }
        if (note != null) {
            mDatabaseEver.setNoteModel(String.valueOf(note.getId()), note.getTitle(), note.getContent(), note.getDrawLocation(), note.getImageURLS(), note.getNoteColor());
        }
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
        for (Note_Model note : list) {
           deleteDrawsAndImagesInsideNote(note);
            removeNote(note);
        }
        noteModelSection.clearSelections();
        CloseOrOpenSelectionOptions(true);
        pushed = true;
    }

    public void openColorSelectorSelection(View view) {
        createPopupMenu(view, R.layout.selection_color_change_popup, true, "popup", 0, 0);
    }

    public void changeColorSelection(int color) {
        List<Note_Model> list = noteModelSection.getSelectedItems();
        for (Note_Model note : list) {
            selectedPosition = note.getActualPosition();
            selectedID = note.getId();
            ChangeNoteColor(color);
        }
        noteModelSection.clearSelections();
        CloseOrOpenSelectionOptions(true);
        pushed = true;
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
            findViewById(R.id.note_Creator_ToolbarLayout).setVisibility(View.GONE);
            findViewById(R.id.mainScreen_ToolbarLayout).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.mainScreen_ToolbarLayout).setVisibility(View.GONE);
            findViewById(R.id.note_Creator_ToolbarLayout).setVisibility(View.VISIBLE);
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
        if (view.getTag().equals("search")) {
            searching = true;
            edit.get().setFocusable(true);
            edit.get().setFocusableInTouchMode(true);
            edit.get().setText("");
            edit.get().requestFocus();
            view.setTag("remove");
            edit.get().addTextChangedListener(a);
        } else {
            searching = false;
            edit.get().setFocusable(false);
            edit.get().setFocusableInTouchMode(false);
            edit.get().removeTextChangedListener(a);
            edit.get().setText("EVERMIND");
            view.setTag("search");
            noteScreen.adapter.get().removeAllSections();
            noteScreen.adapter.get().addSection(noteModelSection);
        }
    }

    private void searchInNotes(CharSequence what) {
        newList = new ListSection<>();
        for (Note_Model note : noteModelSection.getData()) {
            if (note.getContent().contains(what)) {
                    newList.add(note);
            }
        }
        noteScreen.adapter.get().removeAllSections();
        noteScreen.adapter.get().addSection(newList);
    }
}

