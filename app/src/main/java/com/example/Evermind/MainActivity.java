package com.example.Evermind;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.alimuzaffar.lib.widgets.AnimatedEditText;
import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.RTManager;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTApi;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTMediaFactoryImpl;
import com.example.Evermind.TESTEDITOR.rteditor.api.RTProxyImpl;
import com.example.Evermind.TESTEDITOR.rteditor.effects.Effects;
import com.example.Evermind.everUtils.EverAudioHelper;
import com.example.Evermind.everUtils.EverBallsHelper;
import com.example.Evermind.everUtils.EverBitmapHelper;
import com.example.Evermind.everUtils.EverNoteManagement;
import com.example.Evermind.everUtils.EverPreferences;
import com.example.Evermind.everUtils.EverThemeHelper;
import com.example.Evermind.everUtils.EverViewManagement;
import com.example.Evermind.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.example.Evermind.ui.note_screen.NotesScreen;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.navigation.NavigationView;
import com.koushikdutta.ion.Ion;
import com.thekhaeng.pushdownanim.PushDownAnim;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.blurry.Blurry;
import mva2.adapter.ListSection;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class MainActivity extends AppCompatActivity  {


    //VIEWS //FUNCTIONS
    private WeakReference<NotesScreen> noteScreen;
    private NoteEditorFragmentJavaFragment noteCreator;
    private AnimatedEditText titleBox;
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
    //LISTS
    private ArrayList<String> names = new ArrayList<>();
    //IDK
    private Window everMainWindow;
    private EverPreferences prefs;
    // INT's
    private int size = 30;
    //BOOLEANS

    private boolean atHome = true;
    private boolean newNote = false;
    private boolean smoothColorChange = false;
    private TextWatcher TitleWatcher;
    private TextWatcher searchWatcher;

    private Note_Model actualNote;
    private Handler handler;

    

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        postponeEnterTransition();

        everMainWindow = getWindow();

        prefs = new EverPreferences(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  new Handler(Looper.getMainLooper()).post(() -> {
           // new Thread(() -> {
            //  giphyLibrary = new GiphyLibrary();

        runOnUiThread(this::initializeViews);

        everMainWindow.setStatusBarColor(getColor(R.color.White));
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initializeViews() {
        //new Thread(() -> {
            // a potentially time consuming task


            toolbar = findViewById(R.id.toolbar);

            titleBox = findViewById(R.id.searchBox);
            titleBox.setFocusable(false);
          //  ImageButton fillCanvas = findViewById(R.id.fillCanvas);
          //  fillCanvas.setOnClickListener(v -> noteCreator.everDraw.get().fillColor(Color.GREEN));
            //  OverScrollDecoratorHelper.setUpOverScroll(scrollView1);


        startPostponedEnterTransition();



            new Handler(Looper.myLooper()).post(() -> {
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    WindowInsetsController windowInsetsController = getWindow().getDecorView().getWindowInsetsController(); // get current flag
                    windowInsetsController.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
                }

                AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.firstView).getBackground();
                animationDrawable.setEnterFadeDuration(1000);
                animationDrawable.setExitFadeDuration(1000);
                animationDrawable.start();

                Button startButton = findViewById(R.id.startButton);

                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        everViewManagement.animateHeightChange(v, 700, 15000);
                        everViewManagement.animateWidthChange(v, 700, 15000);
                        new Handler(Looper.myLooper()).postDelayed(() -> {
                            animationDrawable.stop();
                            ((ConstraintLayout)findViewById(R.id.mainConstrainLayout)).removeView(findViewById(R.id.firstView));
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                runOnUiThread(MainActivity.this::startEverNoteManagement);
                            }, 200);
                        }, 800);
                    }
                });

                PushDownAnim.setPushDownAnimTo(startButton).setScale(MODE_SCALE, 0.7f);

            });

        startEverViewManagement();
          //  new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(findViewById(R.id.searchBox)));

       // }).start();
    }

    private void startEverNoteManagement() {
        everNoteManagement = new EverNoteManagement(MainActivity.this);
    }

    public void startEverViewManagement() {
        RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, false));
        mRTManager = new RTManager(rtApi, null, this);
        everBitmapHelper = new EverBitmapHelper(this);
        everBallsHelper = new EverBallsHelper(this);
        audioHelper = new EverAudioHelper(this);
        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Exception error) {

            }
        });

        everThemeHelper = new EverThemeHelper(this);
        everViewManagement = new EverViewManagement(this);
     //   new Handler(Looper.getMainLooper()).post(() -> {
     //   });
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

        everThemeHelper.clearOnBack();

        EverInterfaceHelper.getInstance().clearListeners();

        if (noteCreator != null) {
            if (noteCreator != null) {
                noteCreator.removeColorListenerFromCreatorHelper();
            }
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (!atHome) {

                if (actualNote.getTitle().equals("") && actualNote.getContentsAsString().equals("<br>┼") && actualNote.getDrawsAsString().equals("▓┼") && actualNote.getImagesAsString().equals("▓┼") && actualNote.getRecordsAsString().equals("▓┼")) {
                    everNoteManagement.removeNote(actualNote);
                } else {
                    everNoteManagement.updateNote(actualNote.getActualPosition(), actualNote, true);
                }
            }
            EverInterfaceHelper.getInstance().show();
            everViewManagement.switchToolbars(true);
            atHome = true;
            newNote = false;
            everNoteManagement.setSwipeChangeColorRefresh(false);
            everViewManagement.CloseAllButtons();
            everViewManagement.getBottomBar().setItemActiveIndex(0);
            //positionToScroll = actualNote.getActualPosition();
            clearBooleans();
            super.onBackPressed();
        }, 10);

    }

    public void onClick(View v) {

        if (atHome) {
            clearBooleans();
            atHome = false;

            if (noteCreator == null) {
                noteCreator = new NoteEditorFragmentJavaFragment();
                noteCreator.setEnterTransition(new AutoTransition());
                noteCreator.setExitTransition(new AutoTransition());
                noteScreen.get().setExitTransition(new AutoTransition());
                noteScreen.get().setEnterTransition(new AutoTransition());
                noteScreen.get().setReenterTransition(new AutoTransition());
            }

            everNoteManagement.incrementID();
            prefs.putInt("lastID", everNoteManagement.getNoteIdIncrement());
            actualNote = new Note_Model(everNoteManagement.getNoteIdIncrement(), 0, "", "<br>┼", Calendar.getInstance().getTime().toString(), "", "▓┼", "-1", "▓┼");
            everNoteManagement.setSelectedPosition(0);
            everNoteManagement.addNote(actualNote);
            newNote = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler(Looper.myLooper()).postDelayed(() -> {
                        CardView card = Objects.requireNonNull(everNoteManagement.getNoteScreenRecycler().findContainingViewHolder(everNoteManagement.getNoteScreenRecycler().getChildAt(0))).itemView.findViewById(R.id.mainCard);

                        FragmentTransaction transaction = noteScreen.get().getParentFragmentManager().beginTransaction();
                        transaction.setReorderingAllowed(true);
                        card.setTransitionName("card" + everNoteManagement.getNoteIdIncrement() + "-" + actualNote.getDate());//was +1
                        names.add(card.getTransitionName());
                        transaction.addSharedElement(card, card.getTransitionName());
                        transaction.hide(noteScreen.get());
                        transaction.add(R.id.nav_host_fragment, noteCreator);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }, 550);
                }
            });
        }
    }

    public void OnFocusChangeEditor(boolean focused) {
        if (focused) {
            everViewManagement.switchToolbars(true, true, true);
        }
    }

    public void onItemClickFromNoteScreen(View view, View view2, View view4, int position, Note_Model actualNote) {

        if (atHome) {

            clearBooleans();

            atHome = false;



            if (noteCreator == null) {
                noteCreator = new NoteEditorFragmentJavaFragment();
                noteCreator.setEnterTransition(new AutoTransition());
                noteCreator.setExitTransition(new AutoTransition());
                noteScreen.get().setExitTransition(new AutoTransition());
                noteScreen.get().setReenterTransition(new AutoTransition());
            }

            everNoteManagement.setSelectedID(actualNote.getId());
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

    public void AddDraw(boolean close) {


        noteCreator.setDrawFromRecycler(true);

        if (close) {

          //  scrollScrollView(false);

            everViewManagement.CloseOrOpenDrawOptions(close);

            everViewManagement.setDrawing(false);

            everViewManagement.setDrawOptions(false);

        } else {

            noteCreator.getActiveEditor().clearFocus();
            actualNote.addEverLinkedMap("", this, true);
            noteCreator.setNewDraw(true);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
              //  noteCreator.getScrollView().smoothScrollBy(0, noteCreator.getTextDrawRecycler().getHeight(), 550);
            }, 450);



            everViewManagement.setDrawing(true);

            everViewManagement.setDrawOptions(true);
        }
    }

    private int GetColor(int color) {
        return ResourcesCompat.getColor(getResources(), color, null);
    }

    public void formatClick(View view) {
        switch (view.getTag().toString()) {
            case "increaseSize":
                if (size < 76) {

                    size++;
                    noteCreator.getActiveEditor().applyEffect(Effects.FONTSIZE, size);
                    // noteCreator.activeEditor.setFontSize(size);
                }
                break;

            case "decreaseSize":
                if (size > 10) {

                    size--;
                    noteCreator.getActiveEditor().applyEffect(Effects.FONTSIZE, size);
                    // noteCreator.getActiveEditor().setFontSize(size);
                }
                break;

            case "changeColor":
                // createPopupMenu(note_bottom_bar, R.layout.color_change_popup, true, "popup", 50, -180);
                everBallsHelper.initEverBallsForeground();
                //  Blurry.with(this).animate(700).animate().sampling(6).onto(findViewById(R.id.include));
                //CloseOrOpenEditorColors(false);
                break;

            case "bold":
                if (everViewManagement.isBold()) {
                  //  view.setBackgroundTintList(ColorStateList.valueOf(GetColor(R.color.White)));
                    noteCreator.getActiveEditor().applyEffect(Effects.BOLD, false);
                    everViewManagement.setBold(false);
                } else {
                 //   view.setBackgroundTintList(ColorStateList.valueOf(GetColor(R.color.SkyBlueHighlight)));
                    noteCreator.getActiveEditor().applyEffect(Effects.BOLD, true);
                    everViewManagement.setBold(true);
                }
                //  noteCreator.getActiveEditor().setBold();
                break;

            case "italic":
                if (everViewManagement.isItalic()) {
                    noteCreator.getActiveEditor().applyEffect(Effects.ITALIC, false);
                    everViewManagement.setItalic(false);
                } else {
                    noteCreator.getActiveEditor().applyEffect(Effects.ITALIC, true);
                    everViewManagement.setItalic(true);
                }
                // noteCreator.getActiveEditor().setItalic();
                break;

            case "underline":
                if (everViewManagement.isUnderline()) {
                    noteCreator.getActiveEditor().applyEffect(Effects.UNDERLINE, false);
                    everViewManagement.setUnderline(false);
                } else {
                    noteCreator.getActiveEditor().applyEffect(Effects.UNDERLINE, true);
                    everViewManagement.setUnderline(true);
                }
                //  noteCreator.getActiveEditor().setUnderline();
                break;

            case "striketrough":
                if (everViewManagement.isStriketrough()) {
                    noteCreator.getActiveEditor().applyEffect(Effects.STRIKETHROUGH, false);
                    everViewManagement.setStriketrough(false);
                } else {
                    noteCreator.getActiveEditor().applyEffect(Effects.STRIKETHROUGH, true);
                    everViewManagement.setStriketrough(true);
                }
                //  noteCreator.getActiveEditor().setStrikeThrough();
                break;

            case "highlight":
                // createPopupMenu(note_bottom_bar, R.layout.highlight_color_change_popup, true, "popup", 50, -180);
                //  CloseOrOpenEditorHIghlightColors(false);
                everBallsHelper.initEverBallsHighlight();

                //  Blurry.with(this).animate(700).animate().sampling(6).onto(findViewById(R.id.include));

                break;
            case "clearHighlight":
                // noteCreator.getActiveEditor().get().setTextBackgroundColor(Color.WHITE);
                break;
        }
        // popupWindowHelper.dismiss();
    }

    public void paragraphClick(View view) {
        switch (view.getTag().toString()) {
            case "numbers":
                if (everViewManagement.isNumbers()) {
                    noteCreator.getActiveEditor().applyEffect(Effects.NUMBER, false);
                    everViewManagement.setNumbers(false);
                } else {
                    noteCreator.getActiveEditor().applyEffect(Effects.NUMBER, true);
                    everViewManagement.setNumbers(true);
                }
                //     noteCreator.getActiveEditor().setNumbers();
                break;

            case "bullets":
                if (everViewManagement.isBullets()) {
                    noteCreator.getActiveEditor().applyEffect(Effects.BULLET, false);
                    everViewManagement.setBullets(false);
                } else {
                    noteCreator.getActiveEditor().applyEffect(Effects.BULLET, true);
                    everViewManagement.setBullets(true);
                }
                //     noteCreator.getActiveEditor().setBullets();
                break;

            case "alignLeft":
                noteCreator.getActiveEditor().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_NORMAL);
                //    noteCreator.getActiveEditor().setAlignLeft();
                break;

            case "alignCenter":
                noteCreator.getActiveEditor().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_CENTER);
                //  noteCreator.getActiveEditor().setAlignCenter();
                break;

            case "alignRight":
                noteCreator.getActiveEditor().applyEffect(Effects.ALIGNMENT, Layout.Alignment.ALIGN_OPPOSITE);
                //  noteCreator.getActiveEditor().setAlignRight();
                break;

        }
    }

    public void clickToCustomize(View view) {
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

    public void swipeItemsListener(View view, Note_Model noteModel) {
        everNoteManagement.setSelectedPosition(noteModel.getActualPosition());
        everNoteManagement.setSelectedID(noteModel.getId());
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

    public void registerEditor(RTEditText editor, boolean useRichEditing) {
        mRTManager.unregisterEditor(editor);
        mRTManager.registerEditor(editor, useRichEditing);
    }

    public void changeLayout(View view) {
        if ( everNoteManagement.isGrid()) {
            prefs.putBoolean("isGrid", false);
            everNoteManagement.setGrid(false);
        } else {
            prefs.putBoolean("isGrid", true);
            everNoteManagement.setGrid(true);
        }
        noteScreen.get().setLayoutManager(getEverNoteManagement());
    }

    @SuppressLint("SetTextI18n")
    public void searchNotes(View view) {
        if (titleBox.getTag().equals("search")) {
            searchWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        searchInNotes(s);
                    }, 250);
                }
            };
            everNoteManagement.setSearching(true);
            titleBox.setFocusable(true);
            titleBox.setFocusableInTouchMode(true);
            titleBox.removeTextChangedListener(null);
            titleBox.setText("");
            titleBox.setHint("Ex: Sex");
            titleBox.setTag("remove");
            everNoteManagement.getSearchListSection().clear();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                titleBox.requestFocus();
                titleBox.addTextChangedListener(searchWatcher);
            }, 350);
        } else {
            everNoteManagement.setSearching(false);
            titleBox.setFocusable(false);
            titleBox.setFocusableInTouchMode(false);
            titleBox.removeTextChangedListener(searchWatcher);
            titleBox.setText("EVERMIND");
            titleBox.setTag("search");
            noteScreen.get().getAdapter().removeAllSections();
            noteScreen.get().getAdapter().addSection( everNoteManagement.getNoteModelSection());
            for (Note_Model note : everNoteManagement.getNoteModelSection().getData()) {
                note.setActualPosition(everNoteManagement.getNoteModelSection().getData().indexOf(note));
            }
        }
        everNoteManagement.getSearchListSection().clear();
    }

    private void searchInNotes(CharSequence what) {
        everNoteManagement.setSearchListSection(new ListSection<>());
        for (Note_Model note : everNoteManagement.getNoteModelSection().getData()) {
            if (note.getContentsAsString().contains(what)) {

                everNoteManagement.getSearchListSection().add(note);
                note.setActualPosition(everNoteManagement.getSearchListSection().getData().indexOf(note));
            }
        }
        noteScreen.get().getAdapter().removeAllSections();
        noteScreen.get().getAdapter().addSection(everNoteManagement.getSearchListSection());
    }

    public void saveButtonClick(View view) {
        if (view.getTag().equals("Save")) {
            if (noteCreator.isDrawFromRecycler()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    noteCreator.SaveBitmapFromDraw();
                }
                noteCreator.setDrawFromRecycler(false);
            } else {
                onBackPressed();
            }
        }
        if (view.getTag().equals("Search")) {
            searchNotes(view);
        }

    }

    public void deleteButtonClick(View view) {
        if (view.getTag().equals("Delete")) {
            if (everViewManagement.isDrawing()) {
                noteCreator.getEverDraw().clearCanvas();
                noteCreator.getEverDraw().setVisibility(View.INVISIBLE);
               if (noteCreator.getSavedBitmapPath().equals("")) {
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       noteCreator.onLongPress(noteCreator.drawPosition, true);
                   }
               }
                everViewManagement.CloseOrOpenDrawOptions(true);
                // noteCreator.FinalYHeight = 0;
            } else {
                deleteNoteDialog(actualNote);
            }
        }
        if (view.getTag().equals("GridLayout")) {
            changeLayout(view);
        }
    }

    public void redoClick(View view) {
        everViewManagement.animateObject(view, "rotation", 360, 250);
        if (everViewManagement.isDrawing()) {
            noteCreator.getEverDraw().redo();
        } else {
            mRTManager.onRedo();
        }
    }

    public void undoClick(View view) {
        everViewManagement.animateObject(view, "rotation", -360, 250);
        if (everViewManagement.isDrawing()) {
            noteCreator.getEverDraw().undo();
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
        noteCreator.getEverDraw().setColor(Color.WHITE);
    }

    public void recordAudio() {
        File directory = new File(getFilesDir().getPath() + "/recordEver/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        audioHelper.configureAudio(directory.getPath() + "/", AudioSource.MIC, AudioChannel.STEREO, AudioSampleRate.HZ_48000, Integer.parseInt(actualNote.getNoteColor()));
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
        EverInterfaceHelper.getInstance().changeColor(color);
        popupWindowHelperColor.dismiss();
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

    public void openColorSelectorSelection(View view) {
        createPopupMenu(view, R.layout.selection_color_change_popup, "popup");
    }

    public void deleteSelection(View view) {
        everNoteManagement.handleDeleteSelection();
    }

    public EverAudioHelper getAudioHelper() {
        return audioHelper;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public RTManager getmRTManager() {
        return mRTManager;
    }

    public void drawChangeSizeClick(View view) {
        everViewManagement.drawChangeSizeClick();
    }

    public void drawColorClick(View view) {
        everBallsHelper.initEverBallsDraw();
    }



    public void importerClick(View view) {
        noteCreator.importerClick(view);
    }

    private void clearBooleans() {
        everViewManagement.clearBooleans();
        atHome = true;
        newNote = false;
    }

    public void setNoteScreen(WeakReference<NotesScreen> noteScreen) {
        this.noteScreen = noteScreen;
    }

    public void setTitleWatcher(TextWatcher titleWatcher) {
        TitleWatcher = titleWatcher;
    }

    public EverPreferences getPrefs() {
        return prefs;
    }

    public Window getEverMainWindow() {
        return everMainWindow;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void runOnUI(Runnable runnable) {
        this.runOnUiThread(runnable);
    }

      public WeakReference<NotesScreen> getNoteScreen() {
        return noteScreen;
    }

    public CardView getCardNoteCreator() {
        return noteCreator.binding.cardNoteCreator;
    }

    public NoteEditorFragmentJavaFragment getNoteCreator() {
        return noteCreator;
    }

    public AnimatedEditText getTitleBox() {
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

    public TextWatcher getSearchWatcher() {
        return searchWatcher;
    }

    public Note_Model getActualNote() {
        return actualNote;
    }
}