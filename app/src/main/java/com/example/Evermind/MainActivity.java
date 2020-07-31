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
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

import static com.example.Evermind.ui.grid.ui.main.NotesScreen.databaseHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static DataBaseHelper mDatabaseHelper;
    Integer ID;
    public Boolean CloseFormatter = false;
    public Boolean CloseColorSelector = false;
    public Boolean bolder = false;
    public Boolean italicer = false;
    public Boolean underliner = false;
    public Boolean striker = false;
    public Boolean highlighter = false;
    public Integer TextSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            // a potentially time consuming task

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            BottomNavigationView bottomNavigationView1 = findViewById(R.id.navigation_note);

            Animation fadein  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_button_colors);
            Animation fadeout  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_button_colors);
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

            new Handler(Looper.getMainLooper()).post(() -> {

                bottomNavigationView1.setOnNavigationItemSelectedListener(item -> {
                    int id = item.getItemId();


                    switch (id) {
                        case R.id.navigation_home:

                            //TODO TO USE LATER THIS CODE TO SWITCH ANIM \/



                            if(CloseFormatter) {

                                format_text.setVisibility(View.GONE);

                                format_text.startAnimation(fadeout);

                                new Handler(Looper.getMainLooper()).post(() -> {

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

                                });

                            } else {

                                format_text.setVisibility(View.VISIBLE);

                                format_text.startAnimation(fadein);

                                new Handler(Looper.getMainLooper()).post(() -> {

                                    Textsize.setVisibility(View.VISIBLE);
                                    ChangeColor.setVisibility(View.VISIBLE);
                                    Bold.setVisibility(View.VISIBLE);
                                    Italic.setVisibility(View.VISIBLE);
                                    Bold.setVisibility(View.VISIBLE);
                                    Underline.setVisibility(View.VISIBLE);
                                    Striketrough.setVisibility(View.VISIBLE);
                                    HighlightText.setVisibility(View.VISIBLE);

                                });

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


            Black.setOnClickListener(view -> Black.post(() -> ColorClickedSwitcher("Black")));

            Blue.setOnClickListener(view -> Blue.post(() -> ColorClickedSwitcher("Blue")));

            Purple.setOnClickListener(view -> Purple.post(() -> ColorClickedSwitcher("Purple")));

            Magenta.setOnClickListener(view -> Magenta.post(() -> ColorClickedSwitcher("Magenta")));

            Orange.setOnClickListener(view -> Orange.post(() -> ColorClickedSwitcher("Orange")));

            Yellow.setOnClickListener(view -> Yellow.post(() -> ColorClickedSwitcher("Yellow")));

            Green.setOnClickListener(view -> Green.post(() -> ColorClickedSwitcher("Green")));


            ///////////////////////////////////////


            Bold.setOnClickListener(view -> FormatTextSwitcher("Bold"));

            Italic.setOnClickListener(view -> FormatTextSwitcher("Italic"));

            Underline.setOnClickListener(view -> FormatTextSwitcher("Underline"));

            Striketrough.setOnClickListener(view -> FormatTextSwitcher("Striketrough"));

            ChangeColor.setOnClickListener(view -> FormatTextSwitcher("ChangeColor"));

            Textsize.setOnClickListener(view -> FormatTextSwitcher("TextSize"));

            HighlightText.setOnClickListener(view -> FormatTextSwitcher("HighlightText"));

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
                    editor.putBoolean("athome", false);
                    editor.apply();

                    EditText editText = findViewById(R.id.myEditText);
                    editText.setVisibility(View.VISIBLE);

                    BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
                    bottomNavigationView.setVisibility(View.VISIBLE);


                } else {

                 //   ID = NotesScreen.adapter.getItemCount();


                    editor.putInt("noteId", 1);
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
    
    public void ColorClickedSwitcher (String color) {

        new Thread(() -> {
            // a potentially time consuming task

            EditText editText = findViewById(R.id.ToSaveNoteText);
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();
            SpannableStringBuilder spannableString = new SpannableStringBuilder(editText.getEditableText());
            SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();

            CloseColorSelector = false;

            switch (color) {

                case "Black":


                    editor.putString("color", "#000000");
                    editor.apply();

                    CloseOpenedColors();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }

                       // cardView.startAnimation(fadeout);

                    });


                    break;

                case "Blue":


                    editor.putString("color", "#6CCAEF");
                    editor.apply();

                    CloseOpenedColors();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#6CCAEF")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }

                       // cardView.startAnimation(fadeout);

                    });



                    break;

                case "Purple":

                    editor.putString("color", "#c371f9");
                    editor.apply();

                    CloseOpenedColors();


                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#c371f9")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }

                       // cardView.startAnimation(fadeout);

                    });



                    break;

                case "Magenta":

                    editor.putString("color", "#EDF52E66");
                    editor.apply();

                    CloseOpenedColors();


                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#EDF52E66")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }

                       // cardView.startAnimation(fadeout);
                    });


                    break;

                case "Orange":


                    editor.putString("color", "#F9A75D");
                    editor.apply();

                    CloseOpenedColors();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#F9A75D")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }

                      //  cardView.startAnimation(fadeout);

                    });



                    break;

                case "Yellow":


                    editor.putString("color", "#FAF075");
                    editor.apply();

                    CloseOpenedColors();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FAF075")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }

                      //  cardView.startAnimation(fadeout);

                    });



                    break;

                case "Green":


                    editor.putString("color", "#8de791");
                    editor.apply();

                    CloseOpenedColors();

                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (selectionStart + selectionEnd != 0) {

                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8de791")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.setText(spannableString); }

                     //   cardView.startAnimation(fadeout);

                    });


                    break;

                default:


                    break;
            }
        }).start();
    }
    public void FormatTextSwitcher (String functionName) {

        EditText editText = findViewById(R.id.ToSaveNoteText);
        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());

        new Thread(() -> {
            // a potentially time consuming task

            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();

            SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            ImageButton Black = findViewById(R.id.black);
            ImageButton Blue = findViewById(R.id.blue);
            ImageButton Purple = findViewById(R.id.purple);
            ImageButton Magenta = findViewById(R.id.magenta);
            ImageButton Orange = findViewById(R.id.orange);
            ImageButton Yellow = findViewById(R.id.yellow);
            ImageButton Green = findViewById(R.id.green);

            ImageButton Textsize = findViewById(R.id.TextSize);
            ImageButton ChangeColor = findViewById(R.id.ChangeColor);
            ImageButton Bold = findViewById(R.id.Bold);
            ImageButton Italic = findViewById(R.id.Italic);
            ImageButton Underline = findViewById(R.id.Underline);
            ImageButton Striketrough = findViewById(R.id.Striketrough);
            //ImageButton Subscript = findViewById(R.id.Subscript);
            ImageButton HighlightText = findViewById(R.id.HighlightText);

            switch (functionName) {

                case "Bold":

                    editor.putBoolean("Bold", true);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {

                            if(bolder) {

                                if (selectionStart + selectionEnd != 0) {

                                    spannable.setSpan(new StyleSpan(Typeface.NORMAL), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    editText.setText(spannable);
                                    editText.setSelection(0);
                                } else {

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                    Bold.setSelected(false);

                                }, 50); }


                            } else {

                                if (selectionStart + selectionEnd != 0) {

                                    spannable.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    editText.setText(spannable);
                                    editText.setSelection(0);
                                } else {

                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                        Bold.setSelected(true);

                                    }, 50); }

                            bolder = !bolder;
                        }
                    });

                    break;

                case "Italic":

                    editor.putBoolean("Italic", true);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {


                        if(italicer) {

                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new StyleSpan(Typeface.NORMAL), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                                editText.setSelection(0);
                            } else {

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                Italic.setSelected(false);

                            }, 50); }

                        } else {
                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new StyleSpan(Typeface.ITALIC), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                                editText.setSelection(0);
                            } else {

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                    Italic.setSelected(true);

                                }, 50); }
                        }
                        italicer = !italicer;
                    });

                    break;

                case "Underline":

                    editor.putBoolean("Underline", true);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {

                        if(underliner) {

                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new StyleSpan(Typeface.NORMAL), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                                editText.setSelection(0);
                            } else {

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                Underline.setSelected(false);

                            }, 50); }

                        } else {
                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                                editText.setSelection(0);
                            } else {

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                    Underline.setSelected(true);

                                }, 50); }
                        }
                        underliner = !underliner;

                    });

                    break;

                case "Striketrough":

                    editor.putBoolean("Striketrough", true);
                    editor.apply();

                    new Handler(Looper.getMainLooper()).post(() -> {

                        if(striker) {

                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new  StyleSpan(Typeface.NORMAL), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                                editText.setSelection(0);
                            } else {

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                Striketrough.setSelected(false);

                            }, 50); }

                        } else {
                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new StrikethroughSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                                editText.setSelection(0);
                            } else {

                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                    Striketrough.setSelected(true);

                                }, 50); }
                        }
                        striker = !striker;

                    });

                    break;

                case "TextSize":

                    new Handler(Looper.getMainLooper()).post(() -> {

                        if (TextSize == 0) {
                            editor.putInt("TextSize", 1);
                            editor.apply();

                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new RelativeSizeSpan(1), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                            }
                        }

                        if (TextSize == 1) {
                            editor.putInt("TextSize", 2);
                            editor.apply();

                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new RelativeSizeSpan(2), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                            }
                        }
                        if (TextSize == 2) {
                            editor.putInt("TextSize", 3);
                            editor.apply();

                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new RelativeSizeSpan(3), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                            }
                            TextSize = 0;
                        }

                        TextSize = TextSize++;

                    });

                    break;

                case "ChangeColor":

                    new Handler(Looper.getMainLooper()).post(() -> {

                        Textsize.setVisibility(View.GONE);
                        Bold.setVisibility(View.GONE);
                        Italic.setVisibility(View.GONE);
                        Bold.setVisibility(View.GONE);
                        Underline.setVisibility(View.GONE);
                        Striketrough.setVisibility(View.GONE);
                        HighlightText.setVisibility(View.GONE);


                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Textsize.setVisibility(View.GONE);
                           // ChangeColor.setVisibility(View.GONE);

                            Black.setVisibility(View.VISIBLE);
                            Blue.setVisibility(View.VISIBLE);
                            Purple.setVisibility(View.VISIBLE);
                            Magenta.setVisibility(View.VISIBLE);
                            Orange.setVisibility(View.VISIBLE);
                            Yellow.setVisibility(View.VISIBLE);
                            Green.setVisibility(View.VISIBLE);
                        }, 250);

                    });


                    break;

                case "HighlightText":

                    editor.putBoolean("HighlightText", true);
                    editor.apply();
                    String color = preferences.getString("color", "#000000");

                    new Handler(Looper.getMainLooper()).post(() -> {


                        if(highlighter) {

                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new BackgroundColorSpan(Color.parseColor("#000000")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                                editText.setSelection(0);
                            } else {

                                editor.putBoolean("HighlightText", false);
                                editor.apply();

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                HighlightText.setSelected(false);

                            }, 50); }


                        } else {
                            if (selectionStart + selectionEnd != 0) {

                                spannable.setSpan(new BackgroundColorSpan(Color.parseColor(color)), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                editText.setText(spannable);
                                editText.setSelection(0);
                            } else {
                                editor.putBoolean("HighlightText", true);
                                editor.apply();
                                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                                    HighlightText.setSelected(true);

                                }, 50); }
                        }
                        highlighter = !highlighter;

                    });

                    break;

                default:
                    break;
            }
        }).start();
    }

    private void CloseOpenedColors() {

        new Thread(() -> {

            ImageButton Black = findViewById(R.id.black);
            ImageButton Blue = findViewById(R.id.blue);
            ImageButton Purple = findViewById(R.id.purple);
            ImageButton Magenta = findViewById(R.id.magenta);
            ImageButton Orange = findViewById(R.id.orange);
            ImageButton Yellow = findViewById(R.id.yellow);
            ImageButton Green = findViewById(R.id.green);

            ImageButton Textsize = findViewById(R.id.TextSize);
            ImageButton ChangeColor = findViewById(R.id.ChangeColor);
            ImageButton Bold = findViewById(R.id.Bold);
            ImageButton Italic = findViewById(R.id.Italic);
            ImageButton Underline = findViewById(R.id.Underline);
            ImageButton Striketrough = findViewById(R.id.Striketrough);
            //ImageButton Subscript = findViewById(R.id.Subscript);
            ImageButton HighlightText = findViewById(R.id.HighlightText);

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

        }).start();
    }
}