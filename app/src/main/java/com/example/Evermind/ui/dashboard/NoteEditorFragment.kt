package com.example.Evermind.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.Evermind.DataBaseHelper
import com.example.Evermind.R


class NoteEditorFragment : Fragment() {

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


        if (arguments != null) {
            val text = requireArguments().getString("Content")
            val newnote = requireArguments().getBoolean("addnote")
            val noteId = requireArguments().getInt("noteId")
            val title_text = requireArguments().getString("title")

            val texto = requireView().findViewById(R.id.ToSaveNoteText) as EditText
            val title = activity?.findViewById(R.id.myEditText) as EditText

            //Set text to what was wrote
            texto.setText(text.toString())
            title.setText(title_text.toString())


            //SAVE IF HIT BACK
            val content = requireView().findViewById(R.id.ToSaveNoteText) as EditText
            content.doAfterTextChanged {

                val dbhelper: DataBaseHelper
                dbhelper = DataBaseHelper(activity)

                val noteId = requireArguments().getInt("noteId")
                val title_content = title.text.toString()

                if (title_content.equals("Create a new title")) {
                    title.setText("")
                }


                dbhelper.editNote(noteId.toString(), title_content, content.text.toString())

                if (title_text.equals(title_text) != true) {
                    title.doAfterTextChanged {

                        val dbhelper: DataBaseHelper
                        dbhelper = DataBaseHelper(activity)

                        val noteId = requireArguments().getInt("noteId")
                        val content = requireView().findViewById(R.id.ToSaveNoteText) as EditText
                        val title_content = title.text.toString()

                        if (title_content.equals("Create a new title")) {
                            title.setText("")
                        }


                        dbhelper.editNote(noteId.toString(), title_content, content.text.toString())
                    }
                }
            }
        }
    }
}







