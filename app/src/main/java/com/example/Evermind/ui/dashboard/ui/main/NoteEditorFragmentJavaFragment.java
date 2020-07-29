package com.example.Evermind.ui.dashboard.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Evermind.DataBaseHelper;
import com.example.Evermind.R;
import com.tuyenmonkey.textdecorator.TextDecorator;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class NoteEditorFragmentJavaFragment extends Fragment {

    private NoteEditorFragmentMainViewModel mViewModel;

    private SpannableStringBuilder mSpannableStringBuilder;

    private DataBaseHelper dataBaseHelper;

    private int flag = Spannable.SPAN_INCLUSIVE_INCLUSIVE;

    private int start;
    private int finish;
    private String color;
    private ForegroundColorSpan fcs;
    private Editable text;

    public static NoteEditorFragmentJavaFragment newInstance() {
        return new NoteEditorFragmentJavaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_creator, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NoteEditorFragmentMainViewModel.class);

        String content = this.getArguments().getString("Content");
        String title = this.getArguments().getString("title");
        Integer id = this.getArguments().getInt("noteId");

        dataBaseHelper = new DataBaseHelper(getActivity());


        EditText content_editText = getActivity().findViewById(R.id.ToSaveNoteText);

        EditText title_editText = getActivity().findViewById(R.id.myEditText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            title_editText.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content_editText.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
        }

        content_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //content_editText.removeTextChangedListener(this);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID",MODE_PRIVATE);
                 color = preferences.getString("color", "#000000");
                 fcs = new ForegroundColorSpan(Color.parseColor(color));
                 //text = content_editText.getText();
            }

            @Override
            public void afterTextChanged(Editable editable) {

                content_editText.removeTextChangedListener(this);
                start = content_editText.length() - 1;
                finish = content_editText.length();



                editable.setSpan(fcs, start, finish, flag);
                content_editText.setSelection(content_editText.getText().length()); //this is to move the cursor position
                dataBaseHelper.editNote(id.toString(), Html.toHtml(title_editText.getText(), Html.FROM_HTML_MODE_COMPACT), Html.toHtml(content_editText.getText(), Html.FROM_HTML_MODE_COMPACT));

                content_editText.addTextChangedListener(this);
                    //dataBaseHelper.editNote(id.toString(), title_text, content_text);
                }






        });

        title_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String content_text = Html.toHtml(content_editText.getText(), Html.FROM_HTML_MODE_COMPACT);
                String title_text = Html.toHtml(title_editText.getText(), Html.FROM_HTML_MODE_COMPACT);

                if (title_text.equals(title_text)) {

                } else {

                    dataBaseHelper.editNote(id.toString(), title_text, content_text);

                }

            }
        });


    }

}