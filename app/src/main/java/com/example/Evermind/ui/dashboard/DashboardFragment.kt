package com.example.Evermind.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.Evermind.R
import com.example.Evermind.Test
import com.example.Evermind.Test.adapter
import com.example.Evermind.Test.notes
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //READ INTENT AND SET NOTE ID AND SET TEXT
        val intent = getActivity()?.getIntent();
        val text = intent?.getStringExtra("WhatWasWrote")
        val noteId = intent?.getIntExtra("noteId", -1)
        //Toast.makeText(this.context, noteId.toString(), Toast.LENGTH_SHORT).show()


        //Get EditText from Note and set to what was wrote
        val texto = requireView().findViewById(R.id.ToSaveNoteText) as EditText

        //Set text to what was wrote
        texto.setText(text.toString())


        //SAVE IF HIT BACK
        //texto.doOnTextChanged { text, start, before, count ->
           // if (noteId != null) {
             //   Toast.makeText(this.context, texto.text.toString(), Toast.LENGTH_SHORT).show()
              //  notes.set(noteId, texto.text.toString())
               // adapter.notifyDataSetChanged()
            }
        }
