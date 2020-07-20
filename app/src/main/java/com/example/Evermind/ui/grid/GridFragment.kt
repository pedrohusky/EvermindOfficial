package com.example.Evermind.ui.grid.grid

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.Evermind.R
import com.example.Evermind.Test
import com.example.Evermind.ui.slideshow.GridViewModel


class GridFragment : Fragment() {

    private lateinit var gridViewModel: GridViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        gridViewModel =
            ViewModelProviders.of(this).get(GridViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_item_listbkup, container, false)
        val add_button = root.findViewById(R.id.button) as Button

        //val path = this.context?.getFilesDir()
        //val notedirectory = File(path, "Notes")
        //val file = File(notedirectory, "SavedNotes.txt")
        //val inputAsString = FileInputStream(file).bufferedReader().use { it.readText() }
        //Toast.makeText(this.context, inputAsString, Toast.LENGTH_SHORT).show()

        add_button.setOnClickListener{


            val myIntent = Intent(activity, Test::class.java)
            startActivity(myIntent)

        }
        return root
    }
}
