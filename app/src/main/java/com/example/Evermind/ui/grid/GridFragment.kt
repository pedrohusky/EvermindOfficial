package com.example.Evermind.ui.grid.grid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.Evermind.NoteActivity
import com.example.Evermind.R
import com.example.Evermind.StartupActivity
import com.example.Evermind.ui.dashboard.DashboardFragment
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

        val root = inflater.inflate(R.layout.fragment_item_list, container, false)
        val add_button = root.findViewById(R.id.button) as Button

        add_button.setOnClickListener{
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putLong(getString(R.string.NotesWrote), )
                commit()
            }

            val myIntent = Intent(activity, NoteActivity::class.java)
            startActivity(myIntent)
        }
        return root
    }
}
