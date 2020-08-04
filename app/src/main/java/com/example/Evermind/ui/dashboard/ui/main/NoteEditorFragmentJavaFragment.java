package com.example.Evermind.ui.dashboard.ui.main;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.Evermind.DataBaseHelper;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.example.Evermind.SoftInputAssist;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.richeditor.RichEditor;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static android.content.Context.MODE_PRIVATE;

public class NoteEditorFragmentJavaFragment extends Fragment {

    private NoteEditorFragmentMainViewModel mViewModel;

    private DataBaseHelper dataBaseHelper;

    private RichEditor mEditor;

    public Boolean CloseFormatter = false;
    public Boolean CloseParagraph = false;
    public Boolean CloseOpenedColors = false;
    public Boolean CloseOpenedColorsHighlight = false;
    public Boolean DeleteSave = false;

    private SoftInputAssist softInputAssist;

    int size = 4;

    public static NoteEditorFragmentJavaFragment newInstance() {
        return new NoteEditorFragmentJavaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

      //  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        return inflater.inflate(R.layout.fragment_note_creator, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        softInputAssist = new SoftInputAssist(getActivity());

       /////////////////////////////////////////////////////////////////////////// MainActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);


        mEditor = getActivity().findViewById(R.id.ToSaveNoteText);
        mEditor.setEditorHeight(500);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(15, 15, 15, 15);
        //mEditor.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_note);

        ScrollView scrollView = getActivity().findViewById(R.id.scrollview);
        HorizontalScrollView scrollView1 = getActivity().findViewById(R.id.scroll_formatter);
        HorizontalScrollView scrollView2 = getActivity().findViewById(R.id.scroll_paragraph);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView1);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView2);

        EditText TitleTextBox = getActivity().findViewById(R.id.TitleTextBox);
        ImageButton Undo = getActivity().findViewById(R.id.Undo);
        ImageButton Redo = getActivity().findViewById(R.id.Redo);
        ImageButton Delete = getActivity().findViewById(R.id.Delete);
        ImageButton Save = getActivity().findViewById(R.id.Save);

        ImageButton ChangeColor = getActivity().findViewById(R.id.ChangeColor);
        ImageButton HighlightText = getActivity().findViewById(R.id.HighlightText);

        ImageButton Black = getActivity().findViewById(R.id.black);
        ImageButton Blue = getActivity().findViewById(R.id.blue);
        ImageButton Purple = getActivity().findViewById(R.id.purple);
        ImageButton Magenta = getActivity().findViewById(R.id.magenta);
        ImageButton Orange = getActivity().findViewById(R.id.orange);
        ImageButton Yellow = getActivity().findViewById(R.id.yellow);
        ImageButton Green = getActivity().findViewById(R.id.green);

        ImageButton ClearHighlight = getActivity().findViewById(R.id.clearhighlight);
        ImageButton BlackHighlight = getActivity().findViewById(R.id.blackhighlight);
        ImageButton BlueHighlight = getActivity().findViewById(R.id.bluehighlight);
        ImageButton PurpleHighlight = getActivity().findViewById(R.id.purplehighlight);
        ImageButton MagentaHighlight = getActivity().findViewById(R.id.magentahighlight);
        ImageButton OrangeHighlight = getActivity().findViewById(R.id.orangehighlight);
        ImageButton YellowHighlight = getActivity().findViewById(R.id.yellowhighlight);
        ImageButton GreenHighlight = getActivity().findViewById(R.id.greenhighlight);

// Purple.post(() ->
        Black.setOnClickListener(view -> ColorClickedSwitcher("Black", false));

        Blue.setOnClickListener(view -> ColorClickedSwitcher("Blue", false));

        Purple.setOnClickListener(view -> ColorClickedSwitcher("Purple", false));

        Magenta.setOnClickListener(view -> ColorClickedSwitcher("Magenta", false));

        Orange.setOnClickListener(view -> ColorClickedSwitcher("Orange", false));

        Yellow.setOnClickListener(view -> ColorClickedSwitcher("Yellow", false));

        Green.setOnClickListener(view -> ColorClickedSwitcher("Green", false));

        ClearHighlight.setOnClickListener(view -> ColorClickedSwitcher("Clear", true));

        BlackHighlight.setOnClickListener(view -> ColorClickedSwitcher("Black", true));

        BlueHighlight.setOnClickListener(view -> ColorClickedSwitcher("Blue", true));

        PurpleHighlight.setOnClickListener(view -> ColorClickedSwitcher("Purple", true));

        MagentaHighlight.setOnClickListener(view -> ColorClickedSwitcher("Magenta", true));

        OrangeHighlight.setOnClickListener(view -> ColorClickedSwitcher("Orange", true));

        YellowHighlight.setOnClickListener(view -> ColorClickedSwitcher("Yellow", true));

        GreenHighlight.setOnClickListener(view -> ColorClickedSwitcher("Green", true));

        ChangeColor.setOnClickListener(view -> {
            if (CloseOpenedColors) {

                OpenOrCloseColors(false, true);


            } else {

                OpenOrCloseColors(false, false);

            }
        });

        HighlightText.setOnClickListener(view -> {
            if (CloseOpenedColorsHighlight) {

                OpenOrCloseColors(true, true);


            } else {

                OpenOrCloseColors(true, false);

            }
        });


        getActivity().findViewById(R.id.IncreaseSize).setOnClickListener(v -> {


            if (size < 7) {

                size++;
                mEditor.setFontSize(size);
            }

        });

        getActivity().findViewById(R.id.DecreaseSize).setOnClickListener(v -> {


            if (size > 3) {

                size--;
                mEditor.setFontSize(size);
            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id_nav = item.getItemId();


            switch (id_nav) {
                case R.id.nav_formatText:

                    //TODO TO USE LATER THIS CODE TO SWITCH ANIM \/


                    if (CloseFormatter) {

                        CloseOrOpenFormatter(true);


                    } else {

                        CloseOrOpenFormatter(false);

                    }

                    //TODO TO USE LATER THIS CODE TO SWITCH ANIM /\

                    break;

                case R.id.nav_checkbox:

                    if (CloseParagraph) {

                        CloseOrOpenParagraph(true);


                    } else {

                        CloseOrOpenParagraph(false);

                    }


                    break;

                case R.id.nav_quotes:

                    mEditor.setIndent();

                    break;

                case R.id.nav_bullets:

                    mEditor.setBullets();


                    break;

                case R.id.nav_numbers:

                    mEditor.insertImage("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSKT0rZRLOVrqrrZwgVmywEaaHX73EmnYTo0Q&usqp=CAU", "dachshund");


                    break;


                default:
                    return true;
            }
            return true;
        });




        Delete.setOnClickListener(view -> new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {

                            SharedPreferences preferences1 = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
                            int id1 = preferences1.getInt("noteId", -1);

                            dataBaseHelper.deleteNote(id1);

                            onBackPressed(true);
                        }
                )
                .setNegativeButton("No", null)
                .show());

        Save.setOnClickListener(view -> onBackPressed(false));

        Undo.setOnClickListener(v -> mEditor.undo());

        Redo.setOnClickListener(v -> mEditor.redo());

        getActivity().findViewById(R.id.Bold).setOnClickListener(v -> mEditor.setBold());

        getActivity().findViewById(R.id.Italic).setOnClickListener(v -> mEditor.setItalic());

        getActivity().findViewById(R.id.Underscript).setOnClickListener(v -> mEditor.setSubscript());

        getActivity().findViewById(R.id.Superscript).setOnClickListener(v -> mEditor.setSuperscript());

        getActivity().findViewById(R.id.Striketrough).setOnClickListener(v -> mEditor.setStrikeThrough());

        getActivity().findViewById(R.id.Underline).setOnClickListener(v -> mEditor.setUnderline());

        //     getActivity().findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.setHeading(1);
        //          }
        //      });

        //      getActivity().findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
        //        @Override public void onClick(View v) {
        //             mEditor.setHeading(2);
        //         }
        //      });

        ///      getActivity().findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.setHeading(3);
        //            }
        //     });

        //     getActivity().findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
        //         @Override public void onClick(View v) {
        //             mEditor.setHeading(4);
        //        }
        //      });

        //     getActivity().findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
        //         @Override public void onClick(View v) {
        //             mEditor.setHeading(5);
        //        }
        //    });

        //      getActivity().findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.setHeading(6);
        //          }
        //     });


                getActivity().findViewById(R.id.AlignLeft).setOnClickListener(v -> mEditor.setAlignLeft());
                getActivity().findViewById(R.id.AlignCenter).setOnClickListener(v -> mEditor.setAlignCenter());
                getActivity().findViewById(R.id.AlignRight).setOnClickListener(v -> mEditor.setAlignRight());


        //      getActivity().findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
        //                      "dachshund");
        //          }
        //     });

        //     getActivity().findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
        ////         @Override public void onClick(View v) {
        //             mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
        //         }


        dataBaseHelper = new DataBaseHelper(getActivity());


        mViewModel = ViewModelProviders.of(this).get(NoteEditorFragmentMainViewModel.class);


        new Thread(() -> {

            String title = GetTitleFromSharedPreferences();

            new Handler(Looper.getMainLooper()).post(() -> {

                TitleTextBox.setText(title);

            });

        }).start();


        new Thread(() -> {

            String content = GetContentFromSharedPreferences();
            boolean NewNote = GetNewNoteFromSharedPreferences();

            new Handler(Looper.getMainLooper()).post(() -> {

                mEditor.setHtml(content);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    if (NewNote) {
                        mEditor.focusEditor();
                        CardView cardView = getActivity().findViewById(R.id.cardView);
                        TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                                           .addTransition(new ChangeBounds()));

                        mEditor.setEditorHeight(200);

                                   ViewGroup.LayoutParams params = cardView.getLayoutParams();

                                  params.height = 1000;

                                  cardView.setLayoutParams(params);

                        Undo.setVisibility(View.VISIBLE);
                        Redo.setVisibility(View.VISIBLE);
                        Delete.setVisibility(View.VISIBLE);
                        Save.setVisibility(View.VISIBLE);

                        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(mEditor, 0);
                    }

                }, 500);

            });

        }).start();


        mEditor.setOnFocusChangeListener((view, b) -> {
            Animation bottom_nav_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_anim);
            BottomNavigationView bottomNavigationView12 = getActivity().findViewById(R.id.navigation_note);

            CardView cardView = getActivity().findViewById(R.id.cardView);

            if (b) {

                if (mEditor.getHeight() >= 822) {


                        TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                                .addTransition(new ChangeBounds()));

                        ViewGroup.LayoutParams params = cardView.getLayoutParams();

                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                        cardView.setLayoutParams(params);

                }

                new Handler(Looper.getMainLooper()).postDelayed(() -> {



                    bottomNavigationView12.setVisibility(View.VISIBLE);
                    bottomNavigationView12.startAnimation(bottom_nav_anim);

                    DeleteSave = GetDeleteNSaveFromSharedPreferences();

                    if (DeleteSave) {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            Undo.setVisibility(View.VISIBLE);
                            Redo.setVisibility(View.VISIBLE);


                            InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            if (keyboard.isActive()) {

                            } else {
                                keyboard.showSoftInput(mEditor, 0);
                            }

                        }, 200);

                    } else {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            Undo.setVisibility(View.VISIBLE);
                            Redo.setVisibility(View.VISIBLE);
                            Delete.setVisibility(View.VISIBLE);
                            Save.setVisibility(View.VISIBLE);

                        }, 200);

                    }

                    ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, true, false, 0);
                    ApplyChangesToSharedPreferences("UndoRedo", false, "", true, true, false, 0);

                }, 200);

            } else {

                new Handler(Looper.getMainLooper()).post(() -> {

              //      TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
              //              .addTransition(new ChangeBounds()));

                //    ViewGroup.LayoutParams params = cardView.getLayoutParams();

                //    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
               //     cardView.setLayoutParams(params);


                    ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, false, false, 0);
                    ApplyChangesToSharedPreferences("UndoRedo", false, "", true, false, false, 0);

                    Undo.setVisibility(View.GONE);
                    Redo.setVisibility(View.GONE);

                    InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                });
            }
        });


        mEditor.setOnTextChangeListener(text -> new Thread(() -> {

            String transformToHexHTML = replaceRGBColorsWithHex(mEditor.getHtml());

            if (mEditor.getHeight() >= 822) {
                CardView cardView = getActivity().findViewById(R.id.cardView);
                new Handler(Looper.getMainLooper()).post(() -> {

                    TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                            .addTransition(new ChangeBounds()));

                    ViewGroup.LayoutParams params = cardView.getLayoutParams();

                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                    cardView.setLayoutParams(params);
                });
            }


           // focusOnView(scrollView, mEditor);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                dataBaseHelper.editContent(Integer.toString(GetIDFromSharedPreferences()), transformToHexHTML);


            }, 750);

        }).start());

        TitleTextBox.setOnFocusChangeListener((view, b) -> {

            Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_anim_reverse);
            BottomNavigationView bottomNavigationView1 = getActivity().findViewById(R.id.navigation_note);

            if (b) {

                new Handler(Looper.getMainLooper()).post(() -> {

                    InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(TitleTextBox, 0);

                    DeleteSave = GetDeleteNSaveFromSharedPreferences();

                    if (DeleteSave) {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            Undo.setVisibility(View.GONE);
                            Redo.setVisibility(View.GONE);

                        }, 200);


                    } else {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            Delete.setVisibility(View.VISIBLE);
                            Save.setVisibility(View.VISIBLE);

                        }, 200);

                    }

                    ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, true, false, 0);


                    CloseOrOpenFormatter(true);

                    bottomNavigationView1.startAnimation(bottom_nav_anim_reverse);

                   // new Handler(Looper.getMainLooper()).postDelayed(() -> {

                       // bottomNavigationView.setVisibility(View.GONE);

                   // }, 200);

                });

            } else {

                new Handler(Looper.getMainLooper()).post(() -> {

                    InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                   // Delete.setVisibility(View.GONE);
                   // Save.setVisibility(View.GONE);

                    ApplyChangesToSharedPreferences("DeleteNSave", false, "", true, false, false, 0);

                });
            }
        });

    }

    private void OpenOrCloseColors(Boolean highlight, Boolean close) {

        new Thread(() -> {

            ImageButton ChangeColor =  getActivity().findViewById(R.id.ChangeColor);
            ImageButton Bold =  getActivity().findViewById(R.id.Bold);
            ImageButton Italic =  getActivity().findViewById(R.id.Italic);
            ImageButton Underline =  getActivity().findViewById(R.id.Underline);
            ImageButton Striketrough =  getActivity().findViewById(R.id.Striketrough);
            ImageButton HighlightText =  getActivity().findViewById(R.id.HighlightText);

            ImageButton Black =  getActivity().findViewById(R.id.black);
            ImageButton Blue =  getActivity().findViewById(R.id.blue);
            ImageButton Purple =  getActivity().findViewById(R.id.purple);
            ImageButton Magenta =  getActivity().findViewById(R.id.magenta);
            ImageButton Orange =  getActivity().findViewById(R.id.orange);
            ImageButton Yellow =  getActivity().findViewById(R.id.yellow);
            ImageButton Green =  getActivity().findViewById(R.id.green);

            ImageButton ClearHighlight =  getActivity().findViewById(R.id.clearhighlight);
            ImageButton BlackHighlight =  getActivity().findViewById(R.id.blackhighlight);
            ImageButton BlueHighlight =  getActivity().findViewById(R.id.bluehighlight);
            ImageButton PurpleHighlight =  getActivity().findViewById(R.id.purplehighlight);
            ImageButton MagentaHighlight =  getActivity().findViewById(R.id.magentahighlight);
            ImageButton OrangeHighlight =  getActivity().findViewById(R.id.orangehighlight);
            ImageButton YellowHighlight =  getActivity().findViewById(R.id.yellowhighlight);
            ImageButton GreenHighlight =  getActivity().findViewById(R.id.greenhighlight);

            ImageButton Increase =  getActivity().findViewById(R.id.IncreaseSize);
            ImageButton Decrease =  getActivity().findViewById(R.id.DecreaseSize);
            ImageButton Left =  getActivity().findViewById(R.id.AlignLeft);
            ImageButton Center =  getActivity().findViewById(R.id.AlignCenter);
            ImageButton Right =  getActivity().findViewById(R.id.AlignRight);

            if (close) {

                if (highlight) {

                    new Handler(Looper.getMainLooper()).post(() -> {

                        BlackHighlight.setVisibility(View.GONE);
                        BlueHighlight.setVisibility(View.GONE);
                        PurpleHighlight.setVisibility(View.GONE);
                        MagentaHighlight.setVisibility(View.GONE);
                        OrangeHighlight.setVisibility(View.GONE);
                        YellowHighlight.setVisibility(View.GONE);
                        GreenHighlight.setVisibility(View.GONE);
                        ClearHighlight.setVisibility(View.GONE);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            ChangeColor.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Italic.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Underline.setVisibility(View.VISIBLE);
                            Striketrough.setVisibility(View.VISIBLE);
                            HighlightText.setVisibility(View.VISIBLE);

                            Increase.setVisibility(View.VISIBLE);
                            Decrease.setVisibility(View.VISIBLE);
                            Left.setVisibility(View.VISIBLE);
                            Center.setVisibility(View.VISIBLE);
                            Right.setVisibility(View.VISIBLE);
                        }, 200);

                        CloseOpenedColorsHighlight = false;

                    });
                } else {

                    new Handler(Looper.getMainLooper()).post(() -> {

                        Black.setVisibility(View.GONE);
                        Blue.setVisibility(View.GONE);
                        Purple.setVisibility(View.GONE);
                        Magenta.setVisibility(View.GONE);
                        Orange.setVisibility(View.GONE);
                        Yellow.setVisibility(View.GONE);
                        Green.setVisibility(View.GONE);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            //  ChangeColor.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Italic.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Underline.setVisibility(View.VISIBLE);
                            Striketrough.setVisibility(View.VISIBLE);
                            HighlightText.setVisibility(View.VISIBLE);

                            Increase.setVisibility(View.VISIBLE);
                            Decrease.setVisibility(View.VISIBLE);
                            Left.setVisibility(View.VISIBLE);
                            Center.setVisibility(View.VISIBLE);
                            Right.setVisibility(View.VISIBLE);

                        }, 200);

                        CloseOpenedColors = false;

                    });
                }

            } else {
                if (highlight) {
                    new Handler(Looper.getMainLooper()).post(() -> {

                        ChangeColor.setVisibility(View.GONE);
                        Bold.setVisibility(View.GONE);
                        Italic.setVisibility(View.GONE);
                        Bold.setVisibility(View.GONE);
                        Underline.setVisibility(View.GONE);
                        Striketrough.setVisibility(View.GONE);
                        HighlightText.setVisibility(View.GONE);
                        ClearHighlight.setVisibility(View.VISIBLE);

                        Increase.setVisibility(View.GONE);
                        Decrease.setVisibility(View.GONE);
                        Left.setVisibility(View.GONE);
                        Center.setVisibility(View.GONE);
                        Right.setVisibility(View.GONE);



                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            // ChangeColor.setVisibility(View.GONE);

                            BlackHighlight.setVisibility(View.VISIBLE);
                            BlueHighlight.setVisibility(View.VISIBLE);
                            PurpleHighlight.setVisibility(View.VISIBLE);
                            MagentaHighlight.setVisibility(View.VISIBLE);
                            OrangeHighlight.setVisibility(View.VISIBLE);
                            YellowHighlight.setVisibility(View.VISIBLE);
                            GreenHighlight.setVisibility(View.VISIBLE);
                        }, 250);

                        CloseOpenedColorsHighlight = false;
                    });

                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {

                        BlackHighlight.setVisibility(View.GONE);
                        BlueHighlight.setVisibility(View.GONE);
                        PurpleHighlight.setVisibility(View.GONE);
                        MagentaHighlight.setVisibility(View.GONE);
                        OrangeHighlight.setVisibility(View.GONE);
                        YellowHighlight.setVisibility(View.GONE);
                        GreenHighlight.setVisibility(View.GONE);

                        Bold.setVisibility(View.GONE);
                        Italic.setVisibility(View.GONE);
                        Bold.setVisibility(View.GONE);
                        Underline.setVisibility(View.GONE);
                        Striketrough.setVisibility(View.GONE);
                        HighlightText.setVisibility(View.GONE);

                        Increase.setVisibility(View.GONE);
                        Decrease.setVisibility(View.GONE);
                        Left.setVisibility(View.GONE);
                        Center.setVisibility(View.GONE);
                        Right.setVisibility(View.GONE);


                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            // ChangeColor.setVisibility(View.GONE);

                            Black.setVisibility(View.VISIBLE);
                            Blue.setVisibility(View.VISIBLE);
                            Purple.setVisibility(View.VISIBLE);
                            Magenta.setVisibility(View.VISIBLE);
                            Orange.setVisibility(View.VISIBLE);
                            Yellow.setVisibility(View.VISIBLE);
                            Green.setVisibility(View.VISIBLE);
                        }, 250);

                        CloseOpenedColors = false;
                    });
                }
            }

        }).start();
    }
    public void ColorClickedSwitcher (String color, boolean highlight) {

            if (highlight) {

                switch (color) {

                    case "Clear":

                        ImageButton ClearHighlight =  getActivity().findViewById(R.id.clearhighlight);

                        mEditor.setTextBackgroundColor(Color.WHITE);

                        OpenOrCloseColors(true, true);

                        ClearHighlight.setVisibility(View.GONE);

                        break;

                    case "Black":

                            mEditor.setTextBackgroundColor(Color.parseColor("#CCC3FF"));



                       // new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            //mEditor.setTextColor(Color.WHITE);

                       //}, 250);

                        OpenOrCloseColors(true, true);


                        break;

                    case "Blue":


                            mEditor.setTextBackgroundColor(Color.parseColor("#8ECAFF"));

                        OpenOrCloseColors(true, true);


                        break;

                    case "Purple":


                            mEditor.setTextBackgroundColor(Color.parseColor("#FFCCF4"));

                        OpenOrCloseColors(true, true);


                        break;

                    case "Magenta":


                            mEditor.setTextBackgroundColor(Color.parseColor("#B1FFF5"));

                        OpenOrCloseColors(true, true);


                        break;

                    case "Orange":


                            mEditor.setTextBackgroundColor(Color.parseColor("#FFB89C"));

                        OpenOrCloseColors(true, true);


                        break;

                    case "Yellow":

                            mEditor.setTextBackgroundColor(Color.parseColor("#EEFF00"));

                        OpenOrCloseColors(true, true);


                        break;

                    case "Green":

                            mEditor.setTextBackgroundColor(Color.parseColor("#A0FF8A"));

                        OpenOrCloseColors(true, true);


                        break;

                    default:


                        break;
                }
            } else {
                switch (color) {

                    case "Black":

                            mEditor.setTextColor(Color.parseColor("#000000"));


                        OpenOrCloseColors(false, true);


                        break;

                    case "Blue":

                            mEditor.setTextColor(Color.parseColor("#6CCAEF"));

                      //  Blue = !Blue;


                        OpenOrCloseColors(false, true);


                        break;

                    case "Purple":

                            mEditor.setTextColor(Color.parseColor("#c371f9"));

                      //  Purple = !Purple;

                        OpenOrCloseColors(false, true);


                        break;

                    case "Magenta":

                            mEditor.setTextColor(Color.parseColor("#EDF52E66"));

                      //  Magenta = !Magenta;


                        OpenOrCloseColors(false, true);


                        break;

                    case "Orange":


                            mEditor.setTextColor(Color.parseColor("#F9A75D"));

                      //  Orange = !Orange;

                        OpenOrCloseColors(false, true);


                        break;

                    case "Yellow":


                            mEditor.setTextColor(Color.parseColor("#FAF075"));

                       // Yellow = !Yellow;

                        OpenOrCloseColors(false, true);


                        break;

                    case "Green":


                            mEditor.setTextColor(Color.parseColor("#8de791"));

                       // Green = !Green;

                        OpenOrCloseColors(false, true);


                        break;

                    default:


                        break;
                }
            }
    }

    private static String replaceRGBColorsWithHex(String html) {
        // using regular expression to find all occurences of rgb(a,b,c) using
        // capturing groups to get separate numbers.
        Pattern p = Pattern.compile("(rgb\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\))");
        Matcher m = p.matcher(html);

        while (m.find()) {
            // get whole matched rgb(a,b,c) text
            String foundRGBColor = m.group(1);
          //  System.out.println("Found: " + foundRGBColor);

            // get r value
            String rString = m.group(2);
            // get g value
            String gString = m.group(3);
            // get b value
            String bString = m.group(4);

          //  System.out.println(" separated r value: " + rString);
         //   System.out.println(" separated g value: " + gString);
         //   System.out.println(" separated b value: " + bString);

            // converting numbers from string to int
            int rInt = Integer.parseInt(rString);
            int gInt = Integer.parseInt(gString);
            int bInt = Integer.parseInt(bString);

            // converting int to hex value
            String rHex = Integer.toHexString(rInt);
            String gHex = Integer.toHexString(gInt);
            String bHex = Integer.toHexString(bInt);

            // add leading zero if number is small to avoid converting
            // rgb(1,2,3) to rgb(#123)
            String rHexFormatted = String.format("%2s", rHex).replace(" ", "0");
            String gHexFormatted = String.format("%2s", gHex).replace(" ", "0");
            String bHexFormatted = String.format("%2s", bHex).replace(" ", "0");

         //   System.out.println(" converted " + rString + " to hex: " + rHexFormatted);
          //  System.out.println(" converted " + gString + " to hex: " + gHexFormatted);
         //   System.out.println(" converted " + bString + " to hex:" + bHexFormatted);

            // concatenate new color in hex
            String hexColorString = "#" + rHexFormatted + gHexFormatted + bHexFormatted ;

            System.out.println("  replacing " + foundRGBColor + " with " + hexColorString);
            html = html.replaceAll(Pattern.quote(foundRGBColor), hexColorString);
        }
        return html;
    }

    private  void onBackPressed(Boolean delete) {

        new Handler(Looper.getMainLooper()).post(() -> {

            if (delete) {

                CloseOrOpenFormatter(true);


                //Hide nav view \/ \/ \/
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_note);
                Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_anim_reverse);
                bottomNavigationView.startAnimation(bottom_nav_anim_reverse);


                //Hide nav view /\ /\ /\
                ApplyChangesToSharedPreferences("athome", false, "", true, true, false, 0);
                ApplyChangesToSharedPreferences("content", true, "", false, false, false, 0);
            }

            else {


                EditText editText = getActivity().findViewById(R.id.TitleTextBox);

                new Thread(() -> {
                    int id = GetIDFromSharedPreferences();
                    dataBaseHelper.editTitle(Integer.toString(id), editText.getText().toString());

                    //Hide nav view \/ \/ \/
                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_note);
                    Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_anim_reverse);
                    bottomNavigationView.startAnimation(bottom_nav_anim_reverse);

                    ApplyChangesToSharedPreferences("athome", false, "", true, true, false, 0);
                    ApplyChangesToSharedPreferences("content", true, "", false, false, false, 0);


                }).start();

                CloseOrOpenFormatter(true);
                CloseEditorButtonsSaveDelete();
            }
        });
    }

    private void CloseOrOpenFormatter(Boolean close) {

        Animation fadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_formatter);
        CardView format_text = getActivity().findViewById(R.id.format_selector);

        ImageButton ChangeColor = getActivity().findViewById(R.id.ChangeColor);
        ImageButton Bold = getActivity().findViewById(R.id.Bold);
        ImageButton Italic = getActivity().findViewById(R.id.Italic);
        ImageButton Underline = getActivity().findViewById(R.id.Underline);
        ImageButton Striketrough = getActivity().findViewById(R.id.Striketrough);
        ImageButton HighlightText = getActivity().findViewById(R.id.HighlightText);

        ImageButton Black = getActivity().findViewById(R.id.black);
        ImageButton Blue = getActivity().findViewById(R.id.blue);
        ImageButton Purple = getActivity().findViewById(R.id.purple);
        ImageButton Magenta = getActivity().findViewById(R.id.magenta);
        ImageButton Orange = getActivity().findViewById(R.id.orange);
        ImageButton Yellow = getActivity().findViewById(R.id.yellow);
        ImageButton Green = getActivity().findViewById(R.id.green);

        ImageButton BlackHighlight = getActivity().findViewById(R.id.blackhighlight);
        ImageButton BlueHighlight = getActivity().findViewById(R.id.bluehighlight);
        ImageButton PurpleHighlight = getActivity().findViewById(R.id.purplehighlight);
        ImageButton MagentaHighlight = getActivity().findViewById(R.id.magentahighlight);
        ImageButton OrangeHighlight = getActivity().findViewById(R.id.orangehighlight);
        ImageButton YellowHighlight = getActivity().findViewById(R.id.yellowhighlight);
        ImageButton GreenHighlight = getActivity().findViewById(R.id.greenhighlight);

        ImageButton Increase = getActivity().findViewById(R.id.IncreaseSize);
        ImageButton Decrease = getActivity().findViewById(R.id.DecreaseSize);
        ImageButton Superscript = getActivity().findViewById(R.id.Superscript);
        ImageButton Underscript = getActivity().findViewById(R.id.Underscript);

        if (close) {

            ChangeColor.setVisibility(View.GONE);
            Bold.setVisibility(View.GONE);
            Italic.setVisibility(View.GONE);
            Bold.setVisibility(View.GONE);
            Underline.setVisibility(View.GONE);
            Striketrough.setVisibility(View.GONE);
            HighlightText.setVisibility(View.GONE);

            Black.setVisibility(View.GONE);
            Blue.setVisibility(View.GONE);
            Purple.setVisibility(View.GONE);
            Magenta.setVisibility(View.GONE);
            Orange.setVisibility(View.GONE);
            Yellow.setVisibility(View.GONE);
            Green.setVisibility(View.GONE);

            BlackHighlight.setVisibility(View.GONE);
            BlueHighlight.setVisibility(View.GONE);
            PurpleHighlight.setVisibility(View.GONE);
            MagentaHighlight.setVisibility(View.GONE);
            OrangeHighlight.setVisibility(View.GONE);
            YellowHighlight.setVisibility(View.GONE);
            GreenHighlight.setVisibility(View.GONE);

            Increase.setVisibility(View.GONE);
            Decrease.setVisibility(View.GONE);
            Superscript.setVisibility(View.GONE);
            Underscript.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                format_text.startAnimation(fadeout);

            }, 150);

            CloseFormatter = false;


        } else {

            format_text.setVisibility(View.VISIBLE);

            format_text.startAnimation(fadein);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                Increase.setVisibility(View.VISIBLE);
                Decrease.setVisibility(View.VISIBLE);
                Superscript.setVisibility(View.VISIBLE);
                Underscript.setVisibility(View.VISIBLE);
                ChangeColor.setVisibility(View.VISIBLE);
                Bold.setVisibility(View.VISIBLE);
                Italic.setVisibility(View.VISIBLE);
                Bold.setVisibility(View.VISIBLE);
                Underline.setVisibility(View.VISIBLE);
                Striketrough.setVisibility(View.VISIBLE);
                HighlightText.setVisibility(View.VISIBLE);

            }, 150);

            CloseFormatter = true;

        }
    }

    private void CloseOrOpenParagraph(Boolean close) {

        Animation fadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_formatter);
        Animation fadeout = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_formatter);
        CardView format_text = getActivity().findViewById(R.id.format_paragraph);

        ImageButton Bullets = getActivity().findViewById(R.id.Bullets);
        ImageButton Numbers = getActivity().findViewById(R.id.Numbers);
        ImageButton Left = getActivity().findViewById(R.id.AlignLeft);
        ImageButton Center = getActivity().findViewById(R.id.AlignCenter);
        ImageButton Right = getActivity().findViewById(R.id.AlignRight);

        if (close) {

            Bullets.setVisibility(View.GONE);
            Numbers.setVisibility(View.GONE);
            Left.setVisibility(View.GONE);
            Center.setVisibility(View.GONE);
            Right.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                format_text.startAnimation(fadeout);

            }, 150);

            CloseParagraph = false;


        } else {

            format_text.setVisibility(View.VISIBLE);

            format_text.startAnimation(fadein);


            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                Bullets.setVisibility(View.VISIBLE);
                Numbers.setVisibility(View.VISIBLE);
                Left.setVisibility(View.VISIBLE);
                Center.setVisibility(View.VISIBLE);
                Right.setVisibility(View.VISIBLE);

            }, 150);

            CloseParagraph = true;

        }
    }

    private void CloseEditorButtonsSaveDelete () {

        ImageButton Delete = getActivity().findViewById(R.id.Delete);
        ImageButton Save = getActivity().findViewById(R.id.Save);

            Delete.setVisibility(View.GONE);
            Save.setVisibility(View.GONE);

    }

    private final void focusOnView(final ScrollView scroll, final View view) {
        new Handler(Looper.getMainLooper()).post(() -> {
            //FOR HORIZONTAL SCROLL VIEW
            ////      int vLeft = view.getLeft();
            //    int vRight = view.getRight();
            //     int sWidth = scroll.getWidth();
            //     scroll.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);


            int vTop = view.getTop();
            int vBottom = view.getBottom();
            int sHeight = scroll.getBottom();
            //scroll.smoothScrollTo(((vTop + vBottom - sHeight) / 2), 0);
             scroll.smoothScrollTo(0, vTop + vBottom - sHeight / 2);
        });
    }

    private int GetIDFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        return preferences.getInt("noteId", -1);
    }

    private String GetTitleFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        return preferences.getString("title", "");
    }

    private String GetContentFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        return preferences.getString("content", "");
    }

    private Boolean GetNewNoteFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        return preferences.getBoolean("newnote", false);
    }

    private Boolean GetDeleteNSaveFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        return preferences.getBoolean("DeleteNSave", false);
    }

    private void ApplyChangesToSharedPreferences(String name, boolean string, String text, boolean bolean, boolean value, boolean integer, int id) {

        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        if (string) {
            editor.putString(name,text);
            editor.apply();
        }
        if (integer) {
            editor.putInt(name, id);
            editor.apply();
        }
        if (bolean) {
            editor.putBoolean(name, value);
            editor.apply();
        }

    }
}
