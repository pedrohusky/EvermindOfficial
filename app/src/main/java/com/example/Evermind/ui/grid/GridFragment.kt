package com.example.Evermind.ui.grid.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.Evermind.R
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
        return root
    }
}