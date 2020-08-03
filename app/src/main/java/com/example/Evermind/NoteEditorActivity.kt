package com.example.Evermind

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.Evermind.ui.grid.ui.main.NotesScreen
import com.google.android.material.bottomnavigation.BottomNavigationView


class NoteEditorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        //val note_content: TextView = findViewById(R.id.ToSaveNoteText)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.nav_checkbox
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_save -> {

                    val sharedPreferences = applicationContext.getSharedPreferences("com.example.Evermind", Context.MODE_PRIVATE)
                    val noteid: Int = intent.getIntExtra("noteId", -1)


                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Delete Note")
                    builder.setMessage("Do you want to delete this note?")
                    builder.setPositiveButton("Yes"){dialog, which ->

                        NotesScreen.notes.removeAt(noteid)
                        val set: HashSet<String> = HashSet(NotesScreen.notes)
                        sharedPreferences.edit().putStringSet("notes", set).apply();


                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                    }
                    builder.setNegativeButton("No"){dialog,which ->

                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
            true
        }
    }

        fun onSave(view: View) {


            val noteid: Int = intent.getIntExtra("noteId", -1)
            val textwrote: String = intent.getStringExtra("WhatWasWrote").toString()
            val boolean: Boolean = intent.getBooleanExtra("addnote", false)


            if (boolean == true) {

                val intent = Intent(this, MainActivity::class.java)
              //  intent.putExtra("notes", this.ToSaveNoteText.text.toString())
                intent.putExtra("noteId", noteid)
              //  NotesScreen.notes.add(ToSaveNoteText.text.toString())

                val sharedPreferences = applicationContext.getSharedPreferences("com.example.Evermind", Context.MODE_PRIVATE)
                val set: HashSet<String> = HashSet(NotesScreen.notes)
                sharedPreferences.edit().putStringSet("notes", set).apply();



                startActivity(intent) }

            else {


                val intent = Intent(this, MainActivity::class.java)
               // intent.putExtra("notes", this.ToSaveNoteText.text.toString())
                intent.putExtra("noteId", noteid)
              //  NotesScreen.notes.set(noteid, ToSaveNoteText.text.toString())


                val sharedPreferences = applicationContext.getSharedPreferences("com.example.Evermind", Context.MODE_PRIVATE)
                val set: HashSet<String> = HashSet(NotesScreen.notes)
                sharedPreferences.edit().putStringSet("notes", set).apply();



                startActivity(intent)

            }


        }

    override fun onBackPressed() {
        //val returnIntent = Intent(this, MainActivity::class.java)
        //returnIntent.putExtra("hasBackPressed", true)
        //setResult(Activity.RESULT_OK, returnIntent)
        //startActivity(returnIntent)

        //Toast.makeText(this, "oi", Toast.LENGTH_SHORT).show()

        //val navController = findNavController(R.id.nav_host_fragment)
        //navController.navigate(R.id.action_nav_note_to_nav_home)
    }



        //val navController = findNavController(R.id.nav_host_fragment)
        //navController.navigate(R.id.nav_grid)

    }