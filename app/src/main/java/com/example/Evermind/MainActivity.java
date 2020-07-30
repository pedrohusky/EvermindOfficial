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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import static com.example.Evermind.ui.grid.ui.main.NotesScreen.databaseHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static DataBaseHelper mDatabaseHelper;
    Integer ID;
    public Boolean CloseFormatter = false;
    public Boolean CloseColorSelector = false;
    public Integer TextSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);


        BottomNavigationView bottomNavigationView1 = findViewById(R.id.navigation_note);
        bottomNavigationView1.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Animation fadein  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_button_colors);
            Animation fadeout  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_button_colors);
            CardView format_text = findViewById(R.id.format_selector);
           // CardView cardView = findViewById(R.id.color_selector);

            switch (id) {
                case R.id.navigation_home:

                    //TODO TO USE LATER THIS CODE TO SWITCH ANIM \/

                    if(CloseFormatter) {
                        format_text.setVisibility(View.GONE);

                        format_text.startAnimation(fadeout);
                    } else {
                        format_text.setVisibility(View.VISIBLE);

                        format_text.startAnimation(fadein);
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

                                SharedPreferences preferences = getSharedPreferences("DeleteNoteID",MODE_PRIVATE);
                                int id1 = preferences.getInt("noteId", -1);

                                Toast.makeText(MainActivity.this, Integer.toString(id1), Toast.LENGTH_SHORT).show();


                                mDatabaseHelper.deleteNote(id1);

                                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                                navController.navigate(R.id.action_nav_note_to_nav_home);
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
        return true; });


        mDatabaseHelper = new DataBaseHelper(this);

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show());


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

        CardView cardView = findViewById(R.id.color_selector);
        cardView.setOnClickListener(view -> {

        });

        HorizontalScrollView format_text = findViewById(R.id.scroll);
        format_text.setOnClickListener(view -> {

        });

        //TODO THIS IS TO MAKE SURE COLOR SELECTOR WONT CLICK BEHIND IT /\

        ImageButton black = findViewById(R.id.Black);
        ImageButton blue = findViewById(R.id.Blue);
        ImageButton purple = findViewById(R.id.Purple);
        ImageButton magenta = findViewById(R.id.Magenta);
        ImageButton orange = findViewById(R.id.Orange);
        ImageButton yellow = findViewById(R.id.Yellow);
        ImageButton green = findViewById(R.id.Green);
        ImageButton white = findViewById(R.id.White);
////////////////////////////////////////////////////
        ImageButton Black = findViewById(R.id.black);
        ImageButton Blue = findViewById(R.id.blue);
        ImageButton Purple = findViewById(R.id.purple);
        ImageButton Magenta = findViewById(R.id.magenta);
        ImageButton Orange = findViewById(R.id.orange);
        ImageButton Yellow = findViewById(R.id.yellow);
        ImageButton Green = findViewById(R.id.green);
        ImageButton White = findViewById(R.id.white);
/////////////////////////////////////////////////////
        black.setOnClickListener(view -> black.post(() -> ColorClickedSwitcher("Black")));

        blue.setOnClickListener(view -> blue.post(() -> ColorClickedSwitcher("Blue")));

        purple.setOnClickListener(view -> purple.post(() -> ColorClickedSwitcher("Purple")));

        magenta.setOnClickListener(view -> magenta.post(() -> ColorClickedSwitcher("Blue")));

        orange.setOnClickListener(view -> orange.post(() -> ColorClickedSwitcher("Orange")));

        yellow.setOnClickListener(view -> yellow.post(() -> ColorClickedSwitcher("Yellow")));

        green.setOnClickListener(view -> green.post(() -> ColorClickedSwitcher("Green")));

        white.setOnClickListener(view -> white.post(() -> ColorClickedSwitcher("White")));

        /////////////////////////////////////////////////////


        Black.setOnClickListener(view -> black.post(() -> ColorClickedSwitcher("Black")));

        Blue.setOnClickListener(view -> blue.post(() -> ColorClickedSwitcher("Blue")));

        Purple.setOnClickListener(view -> purple.post(() -> ColorClickedSwitcher("Purple")));

        Magenta.setOnClickListener(view -> magenta.post(() -> ColorClickedSwitcher("Blue")));

        Orange.setOnClickListener(view -> orange.post(() -> ColorClickedSwitcher("Orange")));

        Yellow.setOnClickListener(view -> yellow.post(() -> ColorClickedSwitcher("Yellow")));

        Green.setOnClickListener(view -> green.post(() -> ColorClickedSwitcher("Green")));

        White.setOnClickListener(view -> white.post(() -> ColorClickedSwitcher("White")));


        ///////////////////////////////////////

        ImageButton bold = findViewById(R.id.Bold);
        ImageButton italic = findViewById(R.id.Italic);
        ImageButton underline = findViewById(R.id.Underline);
        ImageButton strikethrough = findViewById(R.id.Striketrough);
        ImageButton change_color = findViewById(R.id.ChangeColor);
        ImageButton text_size = findViewById(R.id.TextSize);
        ImageButton highlight_text = findViewById(R.id.HighlightText);


        bold.setOnClickListener(view -> FormatTextSwitcher("Bold"));

        italic.setOnClickListener(view -> FormatTextSwitcher("Italic"));

        underline.setOnClickListener(view -> FormatTextSwitcher("Underline"));

        strikethrough.setOnClickListener(view -> FormatTextSwitcher("Striketrough"));

        change_color.setOnClickListener(view -> FormatTextSwitcher("ChangeColor"));

        text_size.setOnClickListener(view -> FormatTextSwitcher("TextSize"));

        highlight_text.setOnClickListener(view -> FormatTextSwitcher("HighlightText"));
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

            editText.setVisibility(View.GONE);



            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
            bottomNavigationView.setVisibility(View.GONE);

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
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
            return super.onOptionsItemSelected(item); }
        else {

            new Thread(() -> {
                // a potentially time consuming task

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


            return true; }

        }

    public void onClick(View v) {


        new Thread(() -> {
            // a potentially time consuming task
            mDatabaseHelper.AddNoteContent("", "");
        }).start();

        ArrayList<Integer> arrayList = databaseHelper.getIDFromDatabase();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        if (!arrayList.isEmpty()) {

            ID = arrayList.get(arrayList.size() - 1);

            editor.putInt("noteId", ID);

            EditText editText = findViewById(R.id.myEditText);
            editText.setVisibility(View.VISIBLE);


            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
            bottomNavigationView.setVisibility(View.VISIBLE);


        } else {


            // NotesScreen.adapter.notifyDataSetChanged();

            ID = NotesScreen.adapter.getItemCount();

            editor.putInt("noteId", ID);
        }

        editor.putString("title", "");
        editor.putString("content", "");
        editor.putBoolean("athome", false);
        editor.apply();

        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_home_to_nav_note);

    }
    
    public void ColorClickedSwitcher (String color) {

        new Thread(() -> {
            // a potentially time consuming task

            EditText editText = findViewById(R.id.ToSaveNoteText);
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();
            SpannableStringBuilder spannableString = new SpannableStringBuilder(editText.getEditableText());
            SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();

            Animation fadeout  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_button_colors);
            Animation format_out_color  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.format_color_selector_anim);
            CardView cardView = findViewById(R.id.color_selector);

            CloseColorSelector = false;

            switch (color) {

                case "Black":


                    editor.putString("color", "#000000");
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }
                    });

                    cardView.startAnimation(fadeout);


                    break;

                case "Blue":


                    editor.putString("color", "#6CCAEF");
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#6CCAEF")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }
                    });

                    cardView.startAnimation(fadeout);



                    break;

                case "Purple":

                    editor.putString("color", "#c371f9");
                    editor.apply();


                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#c371f9")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }
                    });

                    cardView.startAnimation(fadeout);



                    break;

                case "Magenta":

                    editor.putString("color", "#EDF52E66");
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#EDF52E66")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }
                    });

                    cardView.startAnimation(fadeout);



                    break;

                case "Orange":


                    editor.putString("color", "#F9A75D");
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#F9A75D")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }
                    });

                    cardView.startAnimation(fadeout);



                    break;

                case "Yellow":


                    editor.putString("color", "#FAF075");
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FAF075")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }
                    });

                    cardView.startAnimation(fadeout);



                    break;

                case "Green":


                    editor.putString("color", "#8de791");
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8de791")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }
                    });

                    cardView.startAnimation(fadeout);



                    break;

                case "White":


                    editor.putString("color","#FFFFFF");
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }
                    });

                    cardView.startAnimation(fadeout);


                    break;

                default:


                    break;
            }
        }).start();
    }
    public void FormatTextSwitcher (String functionName) {

        new Thread(() -> {
            // a potentially time consuming task

            EditText editText = findViewById(R.id.ToSaveNoteText);
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();
            SpannableString spannableString = new SpannableString(editText.getText());
            SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            Animation fadein = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_button_colors);
            Animation fadeout = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_button_colors);
            Animation format_in_color  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.format_color_selector_anim);
            CardView cardView = findViewById(R.id.color_selector);

            ImageButton Black = findViewById(R.id.black);
            ImageButton Blue = findViewById(R.id.blue);
            ImageButton Purple = findViewById(R.id.purple);
            ImageButton Magenta = findViewById(R.id.magenta);
            ImageButton Orange = findViewById(R.id.orange);
            ImageButton Yellow = findViewById(R.id.yellow);
            ImageButton Green = findViewById(R.id.green);
            ImageButton White = findViewById(R.id.white);

            switch (functionName) {

                case "Bold":

                    editor.putBoolean("Bold", true);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString);
                        }
                    });

                    break;

                case "Italic":

                    editor.putBoolean("Italic", true);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new StyleSpan(Typeface.ITALIC), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString);
                        }
                    });

                    break;

                case "Underline":

                    editor.putBoolean("Underline", true);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString);
                        }

                    });

                    break;

                case "Striketrough":

                    editor.putBoolean("Striketrough", true);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new StrikethroughSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString);
                        }

                    });

                    break;

                case "TextSize":

                    new Handler(Looper.getMainLooper()).post(() -> {

                        if (TextSize == 0) {
                            editor.putInt("TextSize", 1);
                            editor.apply();

                            if (selectionStart + selectionEnd != 0) {

                                spannableString.setSpan(new RelativeSizeSpan(1), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannableString);
                            }
                        }

                        if (TextSize == 1) {
                            editor.putInt("TextSize", 2);
                            editor.apply();

                            if (selectionStart + selectionEnd != 0) {

                                spannableString.setSpan(new RelativeSizeSpan(2), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannableString);
                            }
                        }
                        if (TextSize == 2) {
                            editor.putInt("TextSize", 3);
                            editor.apply();

                            if (selectionStart + selectionEnd != 0) {

                                spannableString.setSpan(new RelativeSizeSpan(3), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannableString);
                            }
                            TextSize = 0;
                        }

                        TextSize = TextSize++;

                    });

                    break;

                case "ChangeColor":

                    new Handler(Looper.getMainLooper()).post(() -> {

                        if (CloseColorSelector) {
                            cardView.setVisibility(View.GONE);

                            cardView.startAnimation(fadeout);
                        } else {
                            cardView.setVisibility(View.VISIBLE);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms

                                }
                            }, 5000);
                            Black.setVisibility(View.VISIBLE);
                                    Blue.setVisibility(View.VISIBLE);
                            Purple.setVisibility(View.VISIBLE);
                                    Magenta.setVisibility(View.VISIBLE);
                            Yellow.setVisibility(View.VISIBLE);
                                    Green.setVisibility(View.VISIBLE);
                            White.setVisibility(View.VISIBLE);
                                    Orange.setVisibility(View.VISIBLE);

                            Black.startAnimation(format_in_color);
                            Blue.startAnimation(format_in_color);
                            Purple.startAnimation(format_in_color);
                            Magenta.startAnimation(format_in_color);
                            Yellow.startAnimation(format_in_color);
                            Green.startAnimation(format_in_color);
                            White.startAnimation(format_in_color);
                            Orange.startAnimation(format_in_color);

                            cardView.startAnimation(fadein);
                        }
                        CloseColorSelector = !CloseColorSelector;
                    });


                    break;

                case "HighlightText":

                    new Handler(Looper.getMainLooper()).post(() -> {

                        editor.putBoolean("format", true);
                        editor.apply();
                        String color = preferences.getString("color", "#000000");

                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new BackgroundColorSpan(Color.parseColor(color)), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString);
                        }

                    });

                    break;

                default:
                    break;
            }
        }).start();
    }
}