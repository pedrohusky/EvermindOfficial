package com.example.Evermind;
import android.content.SharedPreferences;
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
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Evermind.ui.grid.ui.main.NotesScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.example.Evermind.ui.grid.ui.main.NotesScreen.databaseHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static DataBaseHelper mDatabaseHelper;
    Integer ID;
    public Boolean CloseFormatter = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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



            CloseOrOpenFormatter(true);
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

                    CloseOrOpenFormatter(true);

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

                    ID = NotesScreen.adapter.getItemCount();


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

    private void CloseOrOpenFormatter(Boolean close) {

        Animation fadein = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_formatter);
        CardView format_text = findViewById(R.id.format_selector);

        ImageButton ChangeColor = findViewById(R.id.ChangeColor);
        ImageButton Bold = findViewById(R.id.Bold);
        ImageButton Italic = findViewById(R.id.Italic);
        ImageButton Underline = findViewById(R.id.Underline);
        ImageButton Striketrough = findViewById(R.id.Striketrough);
        ImageButton HighlightText = findViewById(R.id.HighlightText);

        ImageButton Black = findViewById(R.id.black);
        ImageButton Blue = findViewById(R.id.blue);
        ImageButton Purple = findViewById(R.id.purple);
        ImageButton Magenta = findViewById(R.id.magenta);
        ImageButton Orange = findViewById(R.id.orange);
        ImageButton Yellow = findViewById(R.id.yellow);
        ImageButton Green = findViewById(R.id.green);

        ImageButton BlackHighlight = findViewById(R.id.blackhighlight);
        ImageButton BlueHighlight = findViewById(R.id.bluehighlight);
        ImageButton PurpleHighlight = findViewById(R.id.purplehighlight);
        ImageButton MagentaHighlight = findViewById(R.id.magentahighlight);
        ImageButton OrangeHighlight = findViewById(R.id.orangehighlight);
        ImageButton YellowHighlight = findViewById(R.id.yellowhighlight);
        ImageButton GreenHighlight = findViewById(R.id.greenhighlight);

        ImageButton Increase =  findViewById(R.id.IncreaseSize);
        ImageButton Decrease =  findViewById(R.id.DecreaseSize);
        ImageButton Left =  findViewById(R.id.AlignLeft);
        ImageButton Center =  findViewById(R.id.AlignCenter);
        ImageButton Right =  findViewById(R.id.AlignRight);

        if (close) {

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
            Left.setVisibility(View.GONE);
            Center.setVisibility(View.GONE);
            Right.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                format_text.startAnimation(fadeout);

            }, 150);

            CloseFormatter = false;


        } else {

            format_text.setVisibility(View.VISIBLE);

            format_text.startAnimation(fadein);



            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                Increase.setVisibility(View.VISIBLE);
                Decrease.setVisibility(View.VISIBLE);
                Left.setVisibility(View.VISIBLE);
                Center.setVisibility(View.VISIBLE);
                Right.setVisibility(View.VISIBLE);
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

    private void CloseEditorButtonsSaveDelete () {

        ImageButton Delete = findViewById(R.id.Delete);
        ImageButton Save = findViewById(R.id.Save);

            Delete.setVisibility(View.GONE);
            Save.setVisibility(View.GONE);

    }
}