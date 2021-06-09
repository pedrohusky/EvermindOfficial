package com.example.evermemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.evermemo.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.evermemo.TESTEDITOR.rteditor.RTEditText;
import com.example.evermemo.TESTEDITOR.rteditor.RTManager;
import com.example.evermemo.TESTEDITOR.rteditor.api.RTApi;
import com.example.evermemo.TESTEDITOR.rteditor.api.RTMediaFactoryImpl;
import com.example.evermemo.TESTEDITOR.rteditor.api.RTProxyImpl;
import com.example.evermemo.databinding.HomeScreenButtonsBinding;
import com.example.evermemo.everUtils.EverAudioHelper;
import com.example.evermemo.everUtils.EverBallsHelper;
import com.example.evermemo.everUtils.EverBitmapHelper;
import com.example.evermemo.everUtils.EverNoteManagement;
import com.example.evermemo.everUtils.EverPreferences;
import com.example.evermemo.everUtils.EverThemeHelper;
import com.example.evermemo.everUtils.EverViewManagement;
import com.example.evermemo.recycler_models.EverLinkedMap;
import com.example.evermemo.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.example.evermemo.ui.note_screen.NotesScreen;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.tapadoo.alerter.Alerter;
import com.thekhaeng.pushdownanim.PushDownAnim;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.blurry.Blurry;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class MainActivity extends AppCompatActivity {


    //LISTS
    private final ArrayList<String> names = new ArrayList<>();
    //BOOLEANS
    // INT's
    //VIEWS //FUNCTIONS
    private WeakReference<NotesScreen> noteScreen;
    private NoteEditorFragmentJavaFragment noteCreator;
    private EverAnimatedText titleBox;
    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    //HELPERS
    private EverBitmapHelper everBitmapHelper;
    private EverBallsHelper everBallsHelper;
    private EverNoteManagement everNoteManagement;
    private EverPopup popupWindowHelperColor;
    private RTManager mRTManager;
    private EverAudioHelper audioHelper;
    private EverThemeHelper everThemeHelper;
    private EverViewManagement everViewManagement;
    private FirebaseHelper firebaseHelper;
    //IDK
    private Window everMainWindow;
    private EverPreferences prefs;
    private boolean atHome = true;
    private boolean newNote = false;
    private boolean smoothColorChange = false;
    private TextWatcher TitleWatcher;
    private TextWatcher searchWatcher;
    private Note_Model actualNote;
    private HomeScreenButtonsBinding buttonsBinding;
    private Handler handler;
    private Handler handlerUI;
    private boolean alertRunning = false;

    public HomeScreenButtonsBinding getButtonsBinding() {
        return buttonsBinding;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        postponeEnterTransition();

        everMainWindow = getWindow();

        prefs = new EverPreferences(this);

        this.setTheme(R.style.EverNightTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonsBinding = HomeScreenButtonsBinding.bind(findViewById(R.id.mainConstrainLayout));
        handler = new Handler(Looper.myLooper());
        handlerUI = new Handler(Looper.getMainLooper());
        //  new Handler(Looper.getMainLooper()).post(() -> {
        // new Thread(() -> {
        //  giphyLibrary = new GiphyLibrary();

        asyncTask(null, new Runnable() {
            @Override
            public void run() {
                everMainWindow.setStatusBarColor(getColor(R.color.White));
                startEverViewManagement();
                initializeViews();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 23) {
            //  IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                firebaseHelper = new FirebaseHelper(this, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()));
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initializeViews() {
        //new Thread(() -> {
        // a potentially time consuming task


        toolbar = findViewById(R.id.toolbar);

        titleBox = findViewById(R.id.searchBox);
        //   titleBox.setFocusable(false);
        //  ImageButton fillCanvas = findViewById(R.id.fillCanvas);
        //  fillCanvas.setOnClickListener(v -> noteCreator.everDraw.get().fillColor(Color.GREEN));
        //  OverScrollDecoratorHelper.setUpOverScroll(scrollView1);


        startPostponedEnterTransition();


        new Handler(Looper.myLooper()).post(() -> {
            //MAYBE TO REMOVE ALL OF THIS MINUS THE LAST LINE
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayShowTitleEnabled(false);


            //   DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);

            Menu menu_nav=navigationView.getMenu();
            MenuItem item=menu_nav.findItem(R.id.switchDarkMode);
            RelativeLayout relativeLayout = item.getActionView().findViewById(R.id.switchDarkMode);
            SwitchCompat swich =(SwitchCompat) relativeLayout.findViewById(R.id.switchToDarkMode);

            //add listener
            swich.setOnCheckedChangeListener((buttonView, isChecked) -> everThemeHelper.enterDarkMode());

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_note)
                    //  .setDrawerLayout(drawer)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsController windowInsetsController = getWindow().getDecorView().getWindowInsetsController(); // get current flag
                windowInsetsController.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);

                titleBox.setOnClickListener(v -> {
                    if (atHome) {
                        if (!everNoteManagement.isSearching()) {
                            searchNotes();
                        } else {
                            everNoteManagement.setSearching(false);
                        }
                    }
                });
                titleBox.setFocusable(false);
                PushDownAnim.setPushDownAnimTo(titleBox);

            }


            AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.firstView).getBackground();
            animationDrawable.setEnterFadeDuration(2000);
            animationDrawable.setExitFadeDuration(500);
            animationDrawable.start();


            Button startButton = findViewById(R.id.startButton);

            startButton.setOnClickListener(v -> {
                everViewManagement.animateHeightChange(v, 350, 1, null);
                everViewManagement.animateWidthChange(v, 350, 1);
                everViewManagement.animateHeightChange(findViewById(R.id.EvermindBlack), 350, 1, null);
                everViewManagement.animateWidthChange(findViewById(R.id.EvermindBlack), 350, 1);
                getUIHandler().postDelayed(() -> {
                    animationDrawable.stop();
                    ((ConstraintLayout) findViewById(R.id.mainConstrainLayout)).removeView(findViewById(R.id.firstView));
                    getUIHandler().postDelayed(() -> {
                        runOnUiThread(MainActivity.this::startEverNoteManagement);
                    }, 200);
                }, 350);
            });

        });



        //  new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(findViewById(R.id.searchBox)));

        // }).start();
    }


    private void startEverNoteManagement() {
        everNoteManagement = new EverNoteManagement(MainActivity.this);
    }

    public void startEverViewManagement() {
        if (mRTManager == null) {
            RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, false));
            mRTManager = new RTManager(rtApi, null, this);
            everBitmapHelper = new EverBitmapHelper(this);
            everBallsHelper = new EverBallsHelper(this);
            //   audioHelper = new EverAudioHelper(this);
            everThemeHelper = new EverThemeHelper(this);
            everViewManagement = new EverViewManagement(this);
            EverWaveOptions.init(this);
            //   new Handler(Looper.getMainLooper()).post(() -> {
            //   });
        }

    }

    public void applyPushDownToViews(@NonNull List<View> views, float amount) {
        for (View view : views) {
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

    public FirebaseHelper getFirebaseHelper() {
        return firebaseHelper;
    }

    @Override
    public void onBackPressed() {

        if (noteCreator == null) {
            super.onBackPressed();
        } else {
            if (noteCreator.getNoteState() == 1) {
                noteCreator.setNoteState(0);
            } else {

                //   getUIHandler().postDelayed(() -> {

                if (!atHome) {

                    if (actualNote.getTitle().equals("") && actualNote.isContentsEmpty() && actualNote.getImages().size() == 0) {
                        everNoteManagement.removeNote(actualNote);
                        System.out.println("Actual note removed. " + actualNote.toString());
                    } else {
                        everNoteManagement.updateNote(actualNote.getActualPosition(), actualNote, true);
                    }
                }
                firebaseHelper.updateNoteCount();
                EverInterfaceHelper.getInstance().show();
                everViewManagement.switchToolbars(true);
                atHome = true;
                newNote = false;
                everNoteManagement.setSwipeChangeColorRefresh(false);
                everViewManagement.CloseAllButtons();
                everViewManagement.getBottomBar().setItemActiveIndex(0);
                clearBooleans();
                super.onBackPressed();

                if (noteCreator != null) {
                    noteCreator.removeColorListenerFromCreatorHelper();
                }
                EverInterfaceHelper.getInstance().changeAccentColor(everThemeHelper.defaultTheme);
                everThemeHelper.tintSystemBarsAccent(everThemeHelper.defaultTheme, 500);
                EverInterfaceHelper.getInstance().clearListeners();

                //  }, 15);

            }
        }
    }

    public void onClick(View v) {

        if (atHome) {
            clearBooleans();

            everNoteManagement.incrementID();
            prefs.putInt("lastID", everNoteManagement.getNoteIdIncrement());
            everNoteManagement.setSelectedPosition(0);
            firebaseHelper.addNoteToFirebase();
            newNote = true;
            runOnUiThread(() -> getUIHandler().postDelayed(() -> {
                noteModelsAdapter.ViewHolder holder = (noteModelsAdapter.ViewHolder) everNoteManagement.getNoteScreenRecycler().findContainingViewHolder(everNoteManagement.getNoteScreenRecycler().getChildAt(0));

                if (holder != null) {
                    holder.onItemTouched();
                    atHome = false;
                }
            }, 1000));
        }
    }

    public void onItemClickFromNoteScreen(@NonNull View view, @NonNull View view2, @NonNull View view4, int position, @NonNull Note_Model actualNote) {

        if (atHome) {

            clearBooleans();

            atHome = false;


            if (noteCreator == null) {
                noteCreator = new NoteEditorFragmentJavaFragment();
                noteCreator.setEnterTransition(new EverAutoTransition());
                noteCreator.setExitTransition(new EverAutoTransition());
                noteCreator.setReenterTransition(new EverAutoTransition());
                noteScreen.get().setExitTransition(new EverAutoTransition());
                noteScreen.get().setReenterTransition(new EverAutoTransition());
            }

            //  everNoteManagement.setSelectedID(actualNote.getId());
            this.actualNote = actualNote;
            names.add(view.getTransitionName());
            names.add(view2.getTransitionName());
            names.add(view4.getTransitionName());
            everNoteManagement.setSelectedPosition(position);
            everBallsHelper.clearColors();

            FragmentTransaction transaction = noteScreen.get().getParentFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.addSharedElement(view, view.getTransitionName());
            transaction.addSharedElement(view2, view2.getTransitionName());
            transaction.addSharedElement(view4, view4.getTransitionName());
            transaction.add(R.id.nav_host_fragment, noteCreator);
            transaction.addToBackStack(noteScreen.get().getTag());
            transaction.hide(noteScreen.get());
            transaction.commit();
            //  noteScreen.postpone();
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getmRTManager() != null)
            mRTManager.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRTManager.onDestroy(isFinishing());
    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }

    public void clickToCustomize(@NonNull View view) {
        switch (view.getTag().toString()) {
            case "black":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.Black), null);
                break;

            case "white":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.White), null);
                break;

            case "magenta":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.Magenta), null);
                break;

            case "pink":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.Pink), null);
                break;

            case "orange":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.Orange), null);
                break;

            case "blue":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.SkyBlue), null);
                break;

            case "yellow":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.YellowSun), null);
                break;

            case "green":
                everNoteManagement.ChangeNoteColor(GetColor(R.color.GrassGreen), null);
                break;
        }
        Blurry.delete(findViewById(R.id.homeNotesrelative));
        popupWindowHelperColor.dismiss();
    }

    public void swipeItemsListener(@NonNull View view, @NonNull Note_Model noteModel) {
        everNoteManagement.setSelectedPosition(noteModel.getActualPosition());
        // everNoteManagement.setSelectedID(noteModel.getId());
        smoothColorChange = true;
        switch (view.getTag().toString()) {
            case "changeColor":
                createPopupMenu(view, R.layout.swipe_color_change_popup, "dropdown");
                break;

            case "delete":
                deleteNoteDialog(noteModel);
                break;
        }
    }

    public void deleteNoteDialog(@NonNull Note_Model note) {
        //   blurView(this, 25, 2, findViewById(R.id.homeNotesrelative));

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this note!")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(sDialog -> {
                    sDialog
                            .setTitleText("Deleted!")
                            .setContentText("Your note in position: " + note.getActualPosition() + ", with ID: " + note.getNote_name() + " was deleted.")
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

    public void registerEditor(@NonNull RTEditText editor, boolean useRichEditing) {
        mRTManager.unregisterEditor(editor);
        mRTManager.registerEditor(editor, useRichEditing);
    }

    public void changeLayout(View view) {
        if (everNoteManagement.isGrid()) {
            prefs.putBoolean("isGrid", false);
            everNoteManagement.setGrid(false);
            firebaseHelper.setIsGrid(false);
        } else {
            prefs.putBoolean("isGrid", true);
            everNoteManagement.setGrid(true);
            firebaseHelper.setIsGrid(true);
        }
        noteScreen.get().setLayoutManager(firebaseHelper);
    }

    public void searchNotes() {
        if (titleBox.getTag().equals("search")) {
            searchWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(@NonNull Editable s) {
                    titleBox.postDelayed(() -> searchInNotes(s, "content"), 250);
                }
            };
            everNoteManagement.setSearching(true);
            titleBox.setFocusable(true);
            titleBox.setFocusableInTouchMode(true);
            titleBox.removeTextChangedListener(null);
            titleBox.setText("");
            titleBox.setHint("Ex: Homework");
            titleBox.setTag("remove");
            everNoteManagement.getSearchListSection().clear();
            titleBox.postDelayed(() -> {
                titleBox.requestFocus();
                titleBox.addTextChangedListener(searchWatcher);
            }, 350);
        } else {
            everNoteManagement.setSearching(false);
            titleBox.setFocusable(false);
            titleBox.setFocusableInTouchMode(false);
            titleBox.removeTextChangedListener(searchWatcher);
            titleBox.setText("EVERMEMO");
            titleBox.setTag("search");
            noteScreen.get().getAdapter().updateNotesAdapter(everNoteManagement.getNoteModelSection());
            for (Note_Model note : everNoteManagement.getNoteModelSection()) {
                note.setActualPosition(everNoteManagement.getNoteModelSection().indexOf(note));
            }
            InputMethodManager keyboard1 = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            keyboard1.hideSoftInputFromWindow(titleBox.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        everNoteManagement.getSearchListSection().clear();
    }

    private void searchInNotes(@NonNull CharSequence what, String whatToSearchFor) {

        everNoteManagement.setSearchListSection(new ArrayList<>());
        boolean contains = false;
        for (Note_Model note : everNoteManagement.getNoteModelSection()) {
            switch (whatToSearchFor) {
                case "title":
                    if (note.getTitle().contains(what))
                        contains = true;
                    break;

                case "content":
                    for (EverLinkedMap s : note.getEverLinkedContents(false)) {
                        if (s.getContentByType(s.getType()).contains(what)) {
                            contains = true;
                            break;
                        }
                    }
                    break;

                case "date":
                    if (note.getDate().contains(what))
                        contains = true;
                    break;

                case "color":
                    if (note.getNoteColor().contains(what))
                        contains = true;
                    break;
            }
            if (contains) {
                everNoteManagement.getSearchListSection().add(note);
                note.setActualPosition(everNoteManagement.getSearchListSection().indexOf(note));
            }
        }
        noteScreen.get().getAdapter().updateNotesAdapter(everNoteManagement.getSearchListSection());
    }


    public void recordAudio() {
        File directory = new File(getFilesDir().getPath() + "/recordEver/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        audioHelper.configureAudio(directory.getPath() + "/", AudioSource.MIC, AudioChannel.MONO, AudioSampleRate.HZ_48000, Integer.parseInt(actualNote.getNoteColor()));
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

    public void clickChangeColorSelected(@NonNull View view) {
        int color = -1;
        switch (view.getTag().toString()) {
            case "black":
                color = (GetColor(R.color.Black));
                break;

            case "white":
                color = (GetColor(R.color.White));
                break;

            case "magenta":
                color = (GetColor(R.color.Magenta));
                break;

            case "pink":
                color = (GetColor(R.color.Pink));
                break;

            case "orange":
                color = (GetColor(R.color.Orange));
                break;

            case "blue":
                color = (GetColor(R.color.SkyBlue));
                break;

            case "yellow":
                color = (GetColor(R.color.YellowSun));
                break;

            case "green":
                color = (GetColor(R.color.GrassGreen));
                break;

        }
        everNoteManagement.changeColorSelection(color);
        EverInterfaceHelper.getInstance().changeAccentColor(color);
        popupWindowHelperColor.dismiss();
    }

    private void createPopupMenu(@NonNull View view, int layout, @NonNull String showAs) {


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

    public void openColorSelectorSelection(@NonNull View view) {
        createPopupMenu(view, R.layout.selection_color_change_popup, "popup");
    }

    public void deleteSelection(View view) {
        everNoteManagement.handleDeleteSelection();
    }

    public EverAudioHelper getAudioHelper() {
        return audioHelper;
    }

    public void setAudioHelper(EverAudioHelper AudioHelper) {
        audioHelper = AudioHelper;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public RTManager getmRTManager() {
        return mRTManager;
    }


    public void importerClick(@NonNull View view) {
        noteCreator.importerClick(view);
    }

    private void clearBooleans() {
        everViewManagement.clearBooleans();
        atHome = true;
        newNote = false;
    }

    public EverPreferences getPrefs() {
        return prefs;
    }

    public Window getEverMainWindow() {
        return everMainWindow;
    }

    @NonNull
    public ArrayList<String> getNames() {
        return names;
    }

    public WeakReference<NotesScreen> getNoteScreen() {
        return noteScreen;
    }

    public void setNoteScreen(WeakReference<NotesScreen> noteScreen) {
        this.noteScreen = noteScreen;
    }

    @NonNull
    public CardView getCardNoteCreator() {
        return noteCreator.binding.cardNoteCreator;
    }

    public NoteEditorFragmentJavaFragment getNoteCreator() {
        return noteCreator;
    }

    public void setNoteCreator(NoteEditorFragmentJavaFragment noteCreator) {
        this.noteCreator = noteCreator;
    }

    public EverAnimatedText getTitleBox() {
        return titleBox;
    }

    public EverBitmapHelper getEverBitmapHelper() {
        return everBitmapHelper;
    }

    public EverBallsHelper getEverBallsHelper() {
        return everBallsHelper;
    }

    public EverNoteManagement getEverNoteManagement() {
        return everNoteManagement;
    }

    public EverThemeHelper getEverThemeHelper() {
        return everThemeHelper;
    }

    public EverViewManagement getEverViewManagement() {
        return everViewManagement;
    }

    public boolean isAtHome() {
        return atHome;
    }

    public boolean isSmoothColorChange() {
        return smoothColorChange;
    }

    public void setSmoothColorChange(boolean smoothColorChange) {
        this.smoothColorChange = smoothColorChange;
    }

    public boolean isNewNote() {
        return newNote;
    }

    public TextWatcher getTitleWatcher() {
        return TitleWatcher;
    }

    public void setTitleWatcher(TextWatcher titleWatcher) {
        TitleWatcher = titleWatcher;
    }

    public TextWatcher getSearchWatcher() {
        return searchWatcher;
    }

    public Note_Model getActualNote() {
        return actualNote;
    }

    public void setActualNote(Note_Model actualNote) {
        this.actualNote = actualNote;
    }

    public Handler getUIHandler() {
        return handlerUI;
    }

    public Handler getHandler() {
        return handler;
    }

    public void asyncTask(Runnable background, Runnable UIThread) {
        if (background == null) {
            new Handler(Looper.getMainLooper()).post(UIThread);
        } else {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                background.run();
                handler.post(UIThread);
            });
            executor.shutdown();
        }

    }

    public void sendAlert(String title, String text, Drawable drawable) {

        if (!alertRunning) {

            alertRunning = true;
            Alerter.create(this)
                    .setTitle(title)
                    .setText(text)
                    .setDuration(1250)
                    .setIcon(drawable)
                    .enableIconPulse(true)
                    .enableClickAnimation(true)
                    .setIconColorFilter(0) // Optional - Removes white tint
                    .enableVibration(true)
                    .show();

            handler.postDelayed(() -> alertRunning = false, 1250);
        }

    }

    public String getLocalFileDirectory() {
        return getDir("imageDir", Context.MODE_PRIVATE).getPath() + "/";
    }

}