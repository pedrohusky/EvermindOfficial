package com.example.Evermind;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
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
import com.example.Evermind.ui.grid.ui.main.NotesScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;

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

            CardView format_text = findViewById(R.id.format_selector);

            new Handler(Looper.getMainLooper()).post(() -> {

                bottomNavigationView1.setOnNavigationItemSelectedListener(item -> {
                    int id = item.getItemId();


                    switch (id) {
                        case R.id.navigation_home:

                            //TODO TO USE LATER THIS CODE TO SWITCH ANIM \/


                            if (CloseFormatter) {

                                CloseOrOpenFormatter(true);


                            } else {

                                CloseOrOpenFormatter(false);

                            }
                            CloseFormatter = !CloseFormatter;

                            //TODO TO USE LATER THIS CODE TO SWITCH ANIM /\

                            break;

                        case R.id.navigation_dashboard:

                            //  TODO


                            break;

                        case R.id.navigation_save:

                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Are you sure?")
                                    .setMessage("Do you want to delete this note?")
                                    .setPositiveButton("Yes", (dialogInterface, i) -> {

                                                SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
                                                int id1 = preferences.getInt("noteId", -1);

                                                mDatabaseHelper.deleteNote(id1);

                                        onBackPressed();

                                                //NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                                        // navController.navigate(R.id.action_nav_note_to_nav_home);
                                            }
                                    )
                                    .setNegativeButton("No", null)
                                    .show();

                            break;

                        case R.id.navigation_notifications:

                            Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();

                            break;


                        default:
                            return true;
                    }
                    return true;
                });


            });

            mDatabaseHelper = new DataBaseHelper(this);


            //  DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        EditText editText = findViewById(R.id.myEditText);

        editText.post(() -> {

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

            new Thread(() -> {
                EditText title_editText = this.findViewById(R.id.myEditText);
                int id = preferences.getInt("noteId", -1);
                mDatabaseHelper.editTitle(Integer.toString(id), title_editText.getText().toString());

            }).start();

            editText.setVisibility(View.GONE);

            CloseOrOpenFormatter(true);

            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
            bottomNavigationView.setVisibility(View.GONE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("athome", true);
            editor.apply();
        });

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
                EditText title_editText = this.findViewById(R.id.myEditText);
                int id = preferences.getInt("noteId", -1);


                // TODO  /////////////////////////////////////////////////////

                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    mDatabaseHelper.editTitle(Integer.toString(id), title_editText.getText().toString());

                }, 250);

                // TODO /////////////////////////////////////////////////////////////


                CloseOrOpenFormatter(true);

                new Handler(Looper.getMainLooper()).post(() -> {
                    EditText editText = findViewById(R.id.myEditText);
                    editText.setVisibility(View.GONE);

                    BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
                    bottomNavigationView.setVisibility(View.GONE);

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
                    editor.apply();

                    EditText editText = findViewById(R.id.myEditText);
                    editText.setVisibility(View.VISIBLE);

                    BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
                    bottomNavigationView.setVisibility(View.VISIBLE);


                } else {

                    ID = NotesScreen.adapter.getItemCount();


                    editor.putInt("noteId", ID);
                    editor.putString("title", "");
                    editor.putString("content", "");
                    editor.putBoolean("athome", false);
                    editor.apply();

                    EditText editText = findViewById(R.id.myEditText);
                    editText.setVisibility(View.VISIBLE);

                    BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }

                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_home_to_nav_note);

            });

        }).start();
    }

    private void CloseOpenedColors(Boolean highlight) {

        new Thread(() -> {

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

            ImageButton Textsize = findViewById(R.id.TextSize);
            ImageButton ChangeColor = findViewById(R.id.ChangeColor);
            ImageButton Bold = findViewById(R.id.Bold);
            ImageButton Italic = findViewById(R.id.Italic);
            ImageButton Underline = findViewById(R.id.Underline);
            ImageButton Striketrough = findViewById(R.id.Striketrough);
            //ImageButton Subscript = findViewById(R.id.Subscript);
            ImageButton HighlightText = findViewById(R.id.HighlightText);

            if (highlight) {
                new Handler(Looper.getMainLooper()).post(() -> {

                    BlackHighlight.setVisibility(View.GONE);
                    BlueHighlight.setVisibility(View.GONE);
                    PurpleHighlight.setVisibility(View.GONE);
                    MagentaHighlight.setVisibility(View.GONE);
                    OrangeHighlight.setVisibility(View.GONE);
                    YellowHighlight.setVisibility(View.GONE);
                    GreenHighlight.setVisibility(View.GONE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Textsize.setVisibility(View.VISIBLE);
                        ChangeColor.setVisibility(View.VISIBLE);
                        Bold.setVisibility(View.VISIBLE);
                        Italic.setVisibility(View.VISIBLE);
                        Bold.setVisibility(View.VISIBLE);
                        Underline.setVisibility(View.VISIBLE);
                        Striketrough.setVisibility(View.VISIBLE);
                    }, 200);

                });
            } else {

                new Handler(Looper.getMainLooper()).post(() -> {

                    Black.setVisibility(View.GONE);
                    Blue.setVisibility(View.GONE);
                    Purple.setVisibility(View.GONE);
                    Magenta.setVisibility(View.GONE);
                    Orange.setVisibility(View.GONE);
                    Yellow.setVisibility(View.GONE);
                    Green.setVisibility(View.GONE);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Textsize.setVisibility(View.VISIBLE);
                        //  ChangeColor.setVisibility(View.VISIBLE);
                        Bold.setVisibility(View.VISIBLE);
                        Italic.setVisibility(View.VISIBLE);
                        Bold.setVisibility(View.VISIBLE);
                        Underline.setVisibility(View.VISIBLE);
                        Striketrough.setVisibility(View.VISIBLE);
                        HighlightText.setVisibility(View.VISIBLE);
                    }, 200);

                });
            }

        }).start();
    }

    private void CloseOrOpenFormatter(Boolean close) {

        //new Thread(() -> {

        Animation fadein = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_button_colors);
        Animation fadeout = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_button_colors);
        CardView format_text = findViewById(R.id.format_selector);

        ImageButton Textsize = findViewById(R.id.TextSize);
        ImageButton ChangeColor = findViewById(R.id.ChangeColor);
        ImageButton Bold = findViewById(R.id.Bold);
        ImageButton Italic = findViewById(R.id.Italic);
        ImageButton Underline = findViewById(R.id.Underline);
        ImageButton Striketrough = findViewById(R.id.Striketrough);
        //ImageButton Subscript = findViewById(R.id.Subscript);
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

        if (close) {

            //new Handler(Looper.getMainLooper()).post(() -> {


            format_text.startAnimation(fadeout);

            Textsize.setVisibility(View.GONE);
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

            //format_text.setVisibility(View.GONE);

            // });

        } else {

            //new Handler(Looper.getMainLooper()).post(() -> {

            format_text.setVisibility(View.VISIBLE);

            format_text.startAnimation(fadein);

            Textsize.setVisibility(View.VISIBLE);
            ChangeColor.setVisibility(View.VISIBLE);
            Bold.setVisibility(View.VISIBLE);
            Italic.setVisibility(View.VISIBLE);
            Bold.setVisibility(View.VISIBLE);
            Underline.setVisibility(View.VISIBLE);
            Striketrough.setVisibility(View.VISIBLE);
            HighlightText.setVisibility(View.VISIBLE);

            // });
        }
        //}).start();
    }
}