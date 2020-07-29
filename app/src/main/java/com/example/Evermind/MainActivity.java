package com.example.Evermind;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.Evermind.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.example.Evermind.ui.grid.ui.main.NotesScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.snackbar.Snackbar;
import com.tuyenmonkey.textdecorator.TextDecorator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import kotlinx.coroutines.Delay;

import static com.example.Evermind.ui.grid.ui.main.NotesScreen.databaseHelper;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static DataBaseHelper mDatabaseHelper;
    RecyclerGridAdapter adapter;
    Integer ID;
    public Boolean CloseFormatter = false;
    public Boolean CloseColorSelector = false;
    public Boolean Bold = false;
    public Boolean Italic = false;
    public Boolean Underline = false;
    public Boolean Strikethrough = false;
    public Integer TextSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);


        BottomNavigationView bottomNavigationView1 = findViewById(R.id.navigation_note);
        bottomNavigationView1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Animation fadein  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_button_colors);
                Animation fadeout  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_button_colors);
                CardView format_text = findViewById(R.id.format_selector);
                CardView cardView = findViewById(R.id.color_selector);

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
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                SharedPreferences preferences = getSharedPreferences("DeleteNoteID",MODE_PRIVATE);
                                                int id = preferences.getInt("noteId", -1);

                                                Toast.makeText(MainActivity.this, Integer.toString(id), Toast.LENGTH_SHORT).show();


                                                mDatabaseHelper.deleteNote(id);

                                                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                                                navController.navigate(R.id.action_nav_note_to_nav_home);
                                            }
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
            return true; }
        });


        mDatabaseHelper = new DataBaseHelper(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show(); }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //TODO THIS IS TO MAKE SURE COLOR SELECTOR WONT CLICK BEHIND IT \/

        CardView cardView = findViewById(R.id.color_selector);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        CardView format_text = findViewById(R.id.format_selector);
        format_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
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

        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorClickedSwitcher("Black");
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorClickedSwitcher("Blue");
            }
        });

        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorClickedSwitcher("Purple");
            }
        });

        magenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorClickedSwitcher("Magenta");
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorClickedSwitcher("Orange");
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorClickedSwitcher("Yellow");
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorClickedSwitcher("Green");
            }
        });

        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorClickedSwitcher("White");
            }
        });

        ImageButton bold = findViewById(R.id.Bold);
        ImageButton italic = findViewById(R.id.Italic);
        ImageButton underline = findViewById(R.id.Underline);
        ImageButton strikethrough = findViewById(R.id.Striketrough);
        ImageButton change_color = findViewById(R.id.ChangeColor);
        ImageButton text_size = findViewById(R.id.TextSize);
        ImageButton highlight_text = findViewById(R.id.HighlightText);


        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormatTextSwitcher("Bold");

            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormatTextSwitcher("Italic");

            }
        });

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormatTextSwitcher("Underline");

            }
        });

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormatTextSwitcher("Striketrough");
            }
        });

        change_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormatTextSwitcher("ChangeColor");

            }
        });

        text_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormatTextSwitcher("TextSize");

            }
        });

        highlight_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormatTextSwitcher("HighlightText");

            }
        });
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
        editText.setVisibility(View.GONE);

        //ArrayList<Integer> array = databaseHelper.getIDFromDatabase();
       // Integer id = array;
       // if (databaseHelper.getIDFromDatabase()


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
        bottomNavigationView.setVisibility(View.GONE);

        super.onBackPressed();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

   //     if (item.getItemId() == android.R.id.home) {

     //           EditText editText = findViewById(R.id.myEditText);
    //            editText.setVisibility(View.GONE);

     //       BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
    //        bottomNavigationView.setVisibility(View.GONE);


      //          NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
     //           navController.navigate(R.id.action_nav_note_to_nav_home);
     //           return true; }

        return super.onOptionsItemSelected(item); }

    public void onClick(View v) {


        AddContent("", "");

       ArrayList<Integer> arrayList = databaseHelper.getIDFromDatabase();

       if (arrayList.isEmpty() != true) {

           ID = arrayList.get(arrayList.size()-1);

           String waswrote = "";
           Fragment fragment = new NoteEditorFragmentJavaFragment();
           Bundle bundle = new Bundle();
           bundle.putInt("noteId", ID);
           bundle.putString("Content", waswrote);
           bundle.putBoolean("addnote", true);
           bundle.putString("title", "");

           fragment.setArguments(bundle);

           NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
           navController.navigate(R.id.action_nav_home_to_nav_note, bundle);


       } else {

        String waswrote = "";

           NotesScreen.adapter.notifyDataSetChanged();

           ID = NotesScreen.adapter.getItemCount();

        Fragment fragment = new NoteEditorFragmentJavaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("noteId", ID);
        bundle.putString("Content", waswrote);
        bundle.putBoolean("addnote", true);
        bundle.putString("title", "");

        fragment.setArguments(bundle);

           NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
           navController.navigate(R.id.action_nav_home_to_nav_note, bundle);


       }


        EditText editText = findViewById(R.id.myEditText);
        editText.setVisibility(View.VISIBLE);


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
        bottomNavigationView.setVisibility(View.VISIBLE);

        SharedPreferences preferences= this.getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("noteId", ID);
        editor.commit();


    }



    public void AddContent(String title, String content) {

        boolean insertData = mDatabaseHelper.AddNoteContent(title, content);

        toastMessage(content);
        if (insertData) {
            toastMessage("Data successfully inserted");
        } else {
            toastMessage("Error: Data not inserted");
        }

    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        //Note_Model note_model = (Note_Model) getParent().getI
    }
    
    public void ColorClickedSwitcher (String color) {

        EditText editText = findViewById(R.id.ToSaveNoteText);
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        SpannableString spannableString = new SpannableString(editText.getText());
        SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();

        Animation fadeout  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_button_colors);
        CardView cardView = findViewById(R.id.color_selector);

        switch (color) {

            case "Black":


                editor.putString("color", "#000000");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                cardView.startAnimation(fadeout);


            break;

            case "Blue":


                editor.putString("color", "#6CCAEF");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#6CCAEF")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                cardView.startAnimation(fadeout);



                break;

            case "Purple":


                editor.putString("color", "#c371f9");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#c371f9")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                cardView.startAnimation(fadeout);



                break;

            case "Magenta":

                editor.putString("color", "#EDF52E66");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#EDF52E66")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                cardView.startAnimation(fadeout);



                break;

            case "Orange":


                editor.putString("color", "#F9A75D");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#F9A75D")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                cardView.startAnimation(fadeout);



                break;

            case "Yellow":


                editor.putString("color", "#FAF075");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FAF075")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                cardView.startAnimation(fadeout);



                break;

            case "Green":


                editor.putString("color", "#8de791");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8de791")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                cardView.startAnimation(fadeout);



                break;

            case "White":


                editor.putString("color","#FFFFFF");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                cardView.startAnimation(fadeout);



                break;

            default:
                break;
        }
    }
    public void FormatTextSwitcher (String functionName) {

        EditText editText = findViewById(R.id.ToSaveNoteText);
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        SpannableString spannableString = new SpannableString(editText.getText());
        SharedPreferences preferences = getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Animation fadein  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in_button_colors);
        Animation fadeout  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out_button_colors);
        CardView format_text = findViewById(R.id.format_selector);
        CardView cardView = findViewById(R.id.color_selector);

        switch (functionName) {

            case "Bold":

                editor.putInt("format", Typeface.BOLD);
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                break;

            case "Italic":

                editor.putInt("format", Typeface.ITALIC);
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new StyleSpan(Typeface.ITALIC), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                break;

            case "Underline":

                editor.putBoolean("Underline", true);
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                break;

            case "Striketrough":

                editor.putBoolean("Striketrough", true);
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new UnderlineSpan(), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                break;

            case "TextSize":


                if(TextSize == 0) {
                    editor.putInt("TextSize", 1);
                    editor.commit();

                    if (selectionStart + selectionEnd != 0) {

                        spannableString.setSpan(new RelativeSizeSpan(1), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannableString, TextView.BufferType.SPANNABLE); }
                }

                if  (TextSize == 1) {
                editor.putInt("TextSize", 1);
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new RelativeSizeSpan(1), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }
                }
                if (TextSize == 2) {
                    editor.putInt("TextSize", 1);
                    editor.commit();

                    if (selectionStart + selectionEnd != 0) {

                        spannableString.setSpan(new RelativeSizeSpan(1), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannableString, TextView.BufferType.SPANNABLE); }
                    TextSize = 0;
                }

                TextSize = TextSize ++;



                editor.putInt("TextSize", 5);
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new RelativeSizeSpan(5), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                break;

            case "ChangeColor":


                if(CloseColorSelector) {
                    cardView.setVisibility(View.GONE);

                    cardView.startAnimation(fadeout);
                } else {
                    cardView.setVisibility(View.VISIBLE);

                    cardView.startAnimation(fadein);
                }
                CloseColorSelector = !CloseColorSelector;

                break;

            case "HighlightText":

                editor.putString("format", "BackgroundColorSpan");
                editor.commit();

                if (selectionStart + selectionEnd != 0) {

                    spannableString.setSpan(new BackgroundColorSpan(Color.MAGENTA), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    editText.setText(spannableString, TextView.BufferType.SPANNABLE); }

                break;

            default:
                break;
        }
    }
}