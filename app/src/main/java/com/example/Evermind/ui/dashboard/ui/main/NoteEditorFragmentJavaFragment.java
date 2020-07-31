package com.example.Evermind.ui.dashboard.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Evermind.DataBaseHelper;
import com.example.Evermind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.text.Spanned.SPAN_COMPOSING;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static com.example.Evermind.ui.grid.ui.main.NotesScreen.databaseHelper;

public class NoteEditorFragmentJavaFragment extends Fragment {

    private NoteEditorFragmentMainViewModel mViewModel;

    private DataBaseHelper dataBaseHelper;

    private int start;
    private int finish;
    private String color;
    private Boolean Bold;
    private Boolean Italic;
    private Boolean Underline;
    private Boolean Striketrough;
    private Boolean SeT_subscript;
    private Integer TexTSize;
    private Boolean HighlightText;

    private ForegroundColorSpan fcs;
    private UnderlineSpan under;
    private StyleSpan bold;
    private StyleSpan italic;
    private StrikethroughSpan strike;
    private RelativeSizeSpan textsiz;
    private BackgroundColorSpan background;
    private SubscriptSpan subscriptspan;

    private Editable text;

    //Notes Array \/
    public static ArrayList<String> notes = new ArrayList<>();
    //Notes Array /\
    public static ArrayList<String> titles = new ArrayList<>();

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

        //new Thread(() -> {

        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        int id = preferences.getInt("noteId", -1) - 1;

        EditText content_editText = getActivity().findViewById(R.id.ToSaveNoteText);

        EditText title_editText = getActivity().findViewById(R.id.myEditText);

        dataBaseHelper = new DataBaseHelper(getActivity());


        notes = databaseHelper.getContentsFromDatabase();
        titles = databaseHelper.getTitlesFromDatabase();



        String title_text = titles.get(id);
        String content_text = notes.get(id);

        mViewModel = ViewModelProviders.of(this).get(NoteEditorFragmentMainViewModel.class);


        new Handler(Looper.getMainLooper()).post(() -> {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                title_editText.setText(Html.fromHtml(title_text, Html.FROM_HTML_MODE_COMPACT));
            }

        });

        new Handler(Looper.getMainLooper()).post(() -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                content_editText.setText(Html.fromHtml(content_text, Html.FROM_HTML_MODE_COMPACT));
            }

        });

            content_editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        SharedPreferences preferences1 = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
                        color = preferences1.getString("color", "#000000");
                        Bold = preferences1.getBoolean("Bold", false);
                        Italic = preferences1.getBoolean("Italic", false);
                        Underline = preferences1.getBoolean("Underline", false);
                        Striketrough = preferences1.getBoolean("Striketrough", false);
                        TexTSize = preferences1.getInt("TextSize", 1);
                        HighlightText = preferences1.getBoolean("HighlightText", false);
                        SeT_subscript = preferences1.getBoolean("Subscript", false);

                        fcs = new ForegroundColorSpan(Color.parseColor(color));
                        if (Underline) {
                            under = new UnderlineSpan();
                        }

                        if (Bold) {
                            bold = new StyleSpan(Typeface.BOLD);
                        }

                        if (Italic) {
                            italic = new StyleSpan(Typeface.ITALIC);
                        }

                        if (Striketrough) {
                            strike = new StrikethroughSpan();
                        }

                        if (TexTSize > 1) {
                            textsiz = new RelativeSizeSpan(TexTSize);
                        }

                        if (HighlightText) {
                            background = new BackgroundColorSpan(Color.parseColor(color));
                        }

                        if (SeT_subscript) {
                            subscriptspan = new SubscriptSpan();
                        }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    if (content_editText.length() != 0) {
                        start = content_editText.length() - 1;
                    } else {
                        start = 0;
                    }

                    finish = content_editText.length();

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    SpannableStringBuilder spannable = new SpannableStringBuilder(editable);



                    spannable.setSpan(fcs, start, finish, SPAN_EXCLUSIVE_EXCLUSIVE);




                    if (Underline) {


                        spannable.setSpan(under, start, finish, SPAN_EXCLUSIVE_EXCLUSIVE);


                    }

                    if (Bold) {


                        spannable.setSpan(bold, start, finish, SPAN_EXCLUSIVE_EXCLUSIVE);


                    }

                    if (Italic) {


                        spannable.setSpan(italic, start, finish, SPAN_EXCLUSIVE_EXCLUSIVE);


                    }

                    if (Striketrough) {


                        spannable.setSpan(strike, start, finish, SPAN_EXCLUSIVE_EXCLUSIVE);


                    }

                    if (TexTSize > 1) {


                        spannable.setSpan(textsiz, start, finish, SPAN_EXCLUSIVE_EXCLUSIVE);


                    }

                    if (HighlightText) {


                        spannable.setSpan(background, start, finish, SPAN_EXCLUSIVE_EXCLUSIVE);


                    }

                    if (SeT_subscript) {


                        spannable.setSpan(subscriptspan, start, finish, SPAN_EXCLUSIVE_EXCLUSIVE);


                    }


                    content_editText.setSelection(finish); //this is to move the cursor position


                    new Thread(() -> {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            text = content_editText.getEditableText();

                           dataBaseHelper.editContent(Integer.toString(id+1), Html.toHtml(text, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE));
                        }

                        fcs = null;
                        under = null;
                        bold = null;
                        italic = null;
                        strike = null;
                        textsiz = null;
                        background = null;
                        subscriptspan = null;

                    }).start();
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

                    String title_text1 = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        title_text1 = Html.toHtml(title_editText.getEditableText(), Html.FROM_HTML_MODE_COMPACT);
                    }

                    if (title_text1.equals(title_text1)) {

                    } else {

                        dataBaseHelper.editTitle(Integer.toString(id), title_text1);

                    }

                }
            });

       // }).start();
    }
}