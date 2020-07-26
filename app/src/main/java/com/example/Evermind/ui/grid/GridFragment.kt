package com.example.Evermind.ui.grid.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.Evermind.R
import com.example.Evermind.ui.slideshow.GridViewModel


class GridFragment : Fragment() {

    private lateinit var gridViewModel: GridViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val root = inflater.inflate(R.layout.grid_notes_home_screen, container, false)


        //val add_button = root.findViewById(R.id.button) as Button
        //val myIntent = Intent(activity, Test::class.java)

        //startActivity(myIntent)

        //val path = this.context?.getFilesDir()
        //val notedirectory = File(path, "Notes")
        //val file = File(notedirectory, "SavedNotes.txt")
        //val inputAsString = FileInputStream(file).bufferedReader().use { it.readText() }
        //Toast.makeText(this.context, inputAsString, Toast.LENGTH_SHORT).show()

        //add_button.setOnClickListener{


            //val myIntent = Intent(activity, Test::class.java)
          //  startActivity(myIntent)


        return root
    }
    }

