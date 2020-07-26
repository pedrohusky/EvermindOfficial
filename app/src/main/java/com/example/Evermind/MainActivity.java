package com.example.Evermind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.Evermind.R;
import com.example.Evermind.ui.dashboard.NoteEditorFragment;
import com.example.Evermind.ui.grid.ui.main.NotesScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;

import static com.example.Evermind.ui.grid.ui.main.NotesScreen.ids;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DataBaseHelper mDatabaseHelper;
    RecyclerGridAdapter adapter;

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

                switch (id) {
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_save:

                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are you sure?")
                                .setMessage("Do you want to delete this note?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                SharedPreferences preferences=getSharedPreferences("DeleteNoteID",MODE_PRIVATE);
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

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
        bottomNavigationView.setVisibility(View.GONE);

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

                EditText editText = findViewById(R.id.myEditText);
                editText.setVisibility(View.GONE);

            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
            bottomNavigationView.setVisibility(View.GONE);

                Bundle bundle = new Bundle();
                bundle.putBoolean("AntiGoBack", true);

                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_note_to_nav_home, bundle);
                return true; }

        return super.onOptionsItemSelected(item); }

    public void onClick(View v) {

        Integer itemcount = NotesScreen.adapter.getItemCount();
        String waswrote = "";

        Fragment fragment = new NoteEditorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("noteId", itemcount);
        bundle.putString("Content", waswrote);
        bundle.putBoolean("addnote", true);
        bundle.putString("title", "Create a new Title");
        fragment.setArguments(bundle);

        EditText editText = findViewById(R.id.myEditText);
        editText.setVisibility(View.VISIBLE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_note);
        bottomNavigationView.setVisibility(View.VISIBLE);

        Toast.makeText(this, itemcount.toString(), Toast.LENGTH_SHORT).show();

        //AddContent("Create a new title", "Override this");


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_home_to_nav_note, bundle);
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
}