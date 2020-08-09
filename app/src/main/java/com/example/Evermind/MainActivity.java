package com.example.Evermind;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.stfalcon.frescoimageviewer.ImageViewer;
import java.util.ArrayList;


import static com.example.Evermind.ui.note_screen.NotesScreen.databaseHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    public static DataBaseHelper mDatabaseHelper;

    Integer ID;

    public Boolean CloseFormatter = false;
    public Boolean CloseParagraph = false;
    public Boolean CloseImporter = false;

    public static String[] URL;
    public static String[] Data;
    public static String[] Title;
    public static String[] Date;
    public static int[] Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getIntent().getIntArrayExtra("id");
        this.getIntent().getStringArrayExtra("title");
        this.getIntent().getStringArrayExtra("content");
        this.getIntent().getStringArrayExtra("date");
        this.getIntent().getStringArrayExtra("url");

        Id =  this.getIntent().getIntArrayExtra("id");

        Title =  this.getIntent().getStringArrayExtra("title");

        Data =  this.getIntent().getStringArrayExtra("content");

        Date =  this.getIntent().getStringArrayExtra("date");

        URL =  this.getIntent().getStringArrayExtra("url");

        //////////CLEAR SHAREDPREFS

        SharedPreferences preferences = this.getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.putBoolean("athome", true);
        editor.apply();

        //////////CLEAR SHAREDPREFS

        new Thread(() -> {
            // a potentially time consuming task

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            BottomNavigationView bottomNavigationView1 = findViewById(R.id.navigation_note);

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            CardView format_text = findViewById(R.id.format_selector);


            mDatabaseHelper = new DataBaseHelper(this);


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


            //TODO THIS IS TO MAKE SURE COLOR SELECTOR WONT CLICK BEHIND IT \/


            format_text.setOnClickListener(view -> {

            });

            //TODO THIS IS TO MAKE SURE COLOR SELECTOR WONT CLICK BEHIND IT /\

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

        EditText editText = findViewById(R.id.TitleTextBox);

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

            if (!preferences.getBoolean("athome", false)) {
                new Thread(() -> {
                    int id = preferences.getInt("noteId", -1);
                    mDatabaseHelper.editTitle(Integer.toString(id), editText.getText().toString());

                }).start();
            }



           CloseAllButtons();
            CloseEditorButtonsSaveDelete();


            //Hide nav view \/ \/ \/
            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
            Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim_reverse);
            bottomNavigationView.startAnimation(bottom_nav_anim_reverse);

            //Hide nav view /\ /\ /\

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("athome", true);
            editor.putString("content", "");
            editor.apply();

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        boolean athome = preferences.getBoolean("athome", true);
        SharedPreferences.Editor editor = preferences.edit();

        if (athome) {
            return super.onOptionsItemSelected(item);
        } else {

            new Thread(() -> {
                // a potentially time consuming task
                EditText title_editText = this.findViewById(R.id.TitleTextBox);
                int id = preferences.getInt("noteId", -1);

                mDatabaseHelper.editTitle(Integer.toString(id), title_editText.getText().toString());

                // TODO  /////////////////////////////////////////////////////

              //  new Handler(Looper.getMainLooper()).postDelayed(() -> {

            //        mDatabaseHelper.editTitle(Integer.toString(id), title_editText.getText().toString());

            //    }, 250);

                // TODO /////////////////////////////////////////////////////////////

                new Handler(Looper.getMainLooper()).post(() -> {

                    //Hide nav view \/ \/ \/
                    BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
                    Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(this, R.anim.translate_up_anim_reverse);
                    bottomNavigationView.startAnimation(bottom_nav_anim_reverse);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {


                        bottomNavigationView.setVisibility(View.GONE);

                    }, 350);
                    //Hide nav view /\ /\ /\

                    CloseAllButtons();

                });

                editor.putBoolean("athome", true);
                editor.apply();

            }).start();

            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            navController.navigate(R.id.action_nav_note_to_nav_home);


            return true;
        }

    }

    public void onClick(View v) {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();


        new Thread(() -> {
            // a potentially time consuming task

            mDatabaseHelper.AddNoteContent("", "");

            new Handler(Looper.getMainLooper()).post(() -> {

                ArrayList<Integer> arrayList = databaseHelper.getIDFromDatabase();

                if (!arrayList.isEmpty()) {

                    ID = arrayList.get(arrayList.size() - 1);

                    editor.putInt("noteId", ID);
                    editor.putString("title", "");
                    editor.putString("content", "");
                    editor.putBoolean("athome", false);
                    editor.putBoolean("newnote", true);
                    editor.putBoolean("BlackHighlight?", false);
                    editor.putBoolean("DeleteNSave", false);
                    editor.putBoolean("UndoRedo", false);
                    editor.apply();

                    BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
                    bottomNavigationView.setVisibility(View.VISIBLE);


                } else {

//                    ID = NotesScreen.adapter.getItemCount();
                    ID = 1;


                    editor.putInt("noteId", ID);
                    editor.putString("title", "");
                    editor.putString("content", "");
                    editor.putBoolean("athome", false);
                    editor.putBoolean("newnote", true);
                    editor.putBoolean("BlackHighlight?", false);
                    editor.putBoolean("DeleteNSave", false);
                    editor.putBoolean("UndoRedo", false);
                    editor.apply();

                    EditText editText = findViewById(R.id.TitleTextBox);
                    editText.setVisibility(View.VISIBLE);

                    BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }

                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_home_to_nav_note);

            });

        }).start();
    }

    private void CloseOrOpenFormatter() {

        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);
        CardView format_text = this.findViewById(R.id.format_selector);

        ImageButton Draw = this.findViewById(R.id.Draw);
        ImageButton ChangeColor = this.findViewById(R.id.ChangeColor);
        ImageButton Bold = this.findViewById(R.id.Bold);
        ImageButton Italic = this.findViewById(R.id.Italic);
        ImageButton Underline = this.findViewById(R.id.Underline);
        ImageButton Striketrough = this.findViewById(R.id.Striketrough);
        ImageButton HighlightText = this.findViewById(R.id.HighlightText);

        ImageButton Black = this.findViewById(R.id.black);
        ImageButton Blue = this.findViewById(R.id.blue);
        ImageButton Purple = this.findViewById(R.id.purple);
        ImageButton Magenta = this.findViewById(R.id.magenta);
        ImageButton Orange = this.findViewById(R.id.orange);
        ImageButton Yellow = this.findViewById(R.id.yellow);
        ImageButton Green = this.findViewById(R.id.green);

        ImageButton BlackHighlight = this.findViewById(R.id.blackhighlight);
        ImageButton BlueHighlight = this.findViewById(R.id.bluehighlight);
        ImageButton PurpleHighlight = this.findViewById(R.id.purplehighlight);
        ImageButton MagentaHighlight = this.findViewById(R.id.magentahighlight);
        ImageButton OrangeHighlight = this.findViewById(R.id.orangehighlight);
        ImageButton YellowHighlight = this.findViewById(R.id.yellowhighlight);
        ImageButton GreenHighlight = this.findViewById(R.id.greenhighlight);

        ImageButton Increase = this.findViewById(R.id.IncreaseSize);
        ImageButton Decrease = this.findViewById(R.id.DecreaseSize);

        if (CloseFormatter) {

            Draw.setVisibility(View.GONE);
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
                Draw.setVisibility(View.VISIBLE);
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

        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);
        CardView format_text = this.findViewById(R.id.format_paragraph);

        ImageButton Bullets = this.findViewById(R.id.Bullets);
        ImageButton Numbers = this.findViewById(R.id.Numbers);
        ImageButton Left = this.findViewById(R.id.AlignLeft);
        ImageView spacing = this.findViewById(R.id.imageView2);
        ImageButton Center = this.findViewById(R.id.AlignCenter);
        ImageButton Right = this.findViewById(R.id.AlignRight);

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

        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out_formatter);
        CardView importer = this.findViewById(R.id.import_options);

        ImageButton Google = this.findViewById(R.id.GooglePhotos);
        ImageButton Gallery = this.findViewById(R.id.Gallery);
        ImageButton Files = this.findViewById(R.id.Files);

        if (CloseImporter) {

            Google.setVisibility(View.GONE);
            Gallery.setVisibility(View.GONE);
            Files.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                importer.startAnimation(fadeout);
                importer.setVisibility(View.GONE);

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
    }

    private void CloseEditorButtonsSaveDelete () {

        ImageButton Delete = findViewById(R.id.Delete);
        ImageButton Save = findViewById(R.id.Save);

            Delete.setVisibility(View.GONE);
            Save.setVisibility(View.GONE);

    }

    public void OpenNoteFromImage(int position, Uri[] uri) {

        new Handler(Looper.getMainLooper()).post(() -> {

            new ImageViewer.Builder(this, uri)
                    .setStartPosition(position)
                    .show();

        });
    }
}