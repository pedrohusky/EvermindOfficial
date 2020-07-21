package com.example.Evermind

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*


class NoteActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)


            val navView: BottomNavigationView = findViewById(R.id.nav_view)

            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)


            //navView.setOnNavigationItemSelectedListener { item ->
            //  when (item.itemId) {
            //    R.id.navigation_save -> {val intent = Intent(this, MainActivity::class.java)
            //      startActivity(intent)
            //}
            //}
            //true
        }

        fun onSave(view: View) {
            //val saved_text = getSharedPreferences("NoteSaved",Context.MODE_PRIVATE)
            //val note_saved = ToSaveNoteText.text.toString()
            /////note_saved.putString("NotesWrote", ToSaveNoteText.text.toString())
            /////note_saved.commit()

            //val path = this.getFilesDir()
            //val notedirectory = File(path, "Notes")
            //notedirectory.mkdirs()
            //val file = File(notedirectory, "SavedNotes.txt")
            //file.appendText("Note: ")
            //file.appendText(note_saved)
            //file.appendText("/")
            //val inputAsString = FileInputStream(file).bufferedReader().use { it.readText() }
            //Toast.makeText(this, inputAsString, Toast.LENGTH_SHORT).show()


            // val i = intent
            //val notes: ArrayList<String>? = i.getSerializableExtra("notes") as ArrayList<String>?
            //if (notes != null) {
            //   notes.add(ToSaveNoteText.text.toString())
            //   Toast.makeText(this, notes.toString(), Toast.LENGTH_LONG).show()

            val noteid: Int = intent.getIntExtra("noteId", -1)
            val boolean: Boolean = intent.getBooleanExtra("addnote", false)


            if (boolean == true) {

                val intent = Intent(this, Test::class.java)
                intent.putExtra("notes", this.ToSaveNoteText.text.toString())
                intent.putExtra("noteId", noteid)
                Test.notes.add(noteid, ToSaveNoteText.text.toString())

                val sharedPreferences = applicationContext.getSharedPreferences("com.example.Evermind", Context.MODE_PRIVATE)
                val set: HashSet<String> = HashSet(Test.notes)
               sharedPreferences.edit().putStringSet("notes", set).apply();



                startActivity(intent)


            } else {

                //val newnote = Test.adapter.getItemId(+1)


                val intent = Intent(this, Test::class.java)
                intent.putExtra("notes", this.ToSaveNoteText.text.toString())
                intent.putExtra("noteId", noteid)
                Test.notes.set(noteid, ToSaveNoteText.text.toString())
                val sharedPreferences = applicationContext.getSharedPreferences("com.example.Evermind", Context.MODE_PRIVATE)
                val set: HashSet<String> = HashSet(Test.notes)
                sharedPreferences.edit().putStringSet("notes", set).apply();







                startActivity(intent)

            }

        }


        //val navController = findNavController(R.id.nav_host_fragment)
        //navController.navigate(R.id.nav_grid)

    }