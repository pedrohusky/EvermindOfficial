package com.example.Evermind.ui.dashboard.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.Evermind.DataBaseHelper;
import com.example.Evermind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.richeditor.RichEditor;

import static android.content.Context.MODE_PRIVATE;

public class NoteEditorFragmentJavaFragment extends Fragment {

    private NoteEditorFragmentMainViewModel mViewModel;

    private DataBaseHelper dataBaseHelper;

    private String text;

    private RichEditor mEditor;

    public Boolean CloseOpenedColors = false;
    public Boolean CloseOpenedColorsHighlight = false;
    public Boolean CloseOpenedText = false;

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

        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        int id = preferences.getInt("noteId", -1);

        mEditor = (RichEditor) getActivity().findViewById(R.id.ToSaveNoteText);
        mEditor.setEditorHeight(500);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(15, 15, 15, 15);

        EditText TitleTextBox = getActivity().findViewById(R.id.TitleTextBox);

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


        getActivity().findViewById(R.id.IncreaseSize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (size < 7) {

                    size++;
                    mEditor.setFontSize(size);
                }

            }
        });

        getActivity().findViewById(R.id.DecreaseSize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (size > 3) {

                    size--;
                    mEditor.setFontSize(size);
                }
            }
        });

             getActivity().findViewById(R.id.Undo).setOnClickListener(new View.OnClickListener() {
               @Override public void onClick(View v) {
                     mEditor.undo();
                 }
            });

           getActivity().findViewById(R.id.Redo).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                     mEditor.redo();
                }
            });

        getActivity().findViewById(R.id.Bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        getActivity().findViewById(R.id.Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        //   getActivity().findViewById(R.id.TODO).setOnClickListener(new View.OnClickListener() {
        //      @Override public void onClick(View v) {
        //          mEditor.setSubscript();
        //       }
        //   });

        //     getActivity().findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
        //         @Override public void onClick(View v) {
        //             mEditor.setSuperscript();
        //        }
        //     });

        getActivity().findViewById(R.id.Striketrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        getActivity().findViewById(R.id.Underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

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

        //        getActivity().findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
        //            @Override public void onClick(View v) {
        //              mEditor.setIndent();
        //          }
        //      });

        //     getActivity().findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
        //         @Override public void onClick(View v) {
        //             mEditor.setOutdent();
        //         }
        //     });

        getActivity().findViewById(R.id.AlignLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        getActivity().findViewById(R.id.AlignCenter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        getActivity().findViewById(R.id.AlignRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        //       getActivity().findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.setBlockquote();
        //          }
        //      });

        //      getActivity().findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
        //            @Override public void onClick(View v) {
        //               mEditor.setBullets();
        //           }
        //       });

        //      getActivity().findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
        //          @Override public void onClick(View v) {
        //              mEditor.setNumbers();
        //          }
        //      });

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
        //     });
        //     getActivity().findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
        //         @Override public void onClick(View v) {
        //            mEditor.insertTodo();
        //        }
        //    });


        dataBaseHelper = new DataBaseHelper(getActivity());


        mViewModel = ViewModelProviders.of(this).get(NoteEditorFragmentMainViewModel.class);


        new Thread(() -> {

            String title = preferences.getString("title", "");

            new Handler(Looper.getMainLooper()).post(() -> {

                TitleTextBox.setText(title);

            });

        }).start();


        new Thread(() -> {

            String content = preferences.getString("content", "");
            boolean NewNote = preferences.getBoolean("newnote", false);

            new Handler(Looper.getMainLooper()).post(() -> {

                mEditor.setHtml(content);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {

                    if (NewNote) {
                        mEditor.focusEditor();

                        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(mEditor, 0);
                    }

                }, 500);

            });

        }).start();

        mEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        Animation bottom_nav_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_anim);
                        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_note);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        bottomNavigationView.startAnimation(bottom_nav_anim);

                    }, 250);

                } else {

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    }, 100);
                }
            }
        });


        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {

                new Thread(() -> {

                    String transformToHexHTML = replaceRGBColorsWithHex(mEditor.getHtml());

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        dataBaseHelper.editContent(Integer.toString(id), transformToHexHTML);

                    }, 750);

                }).start();
            }
        });

        TitleTextBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    new Handler(Looper.getMainLooper()).post(() -> {

                        Animation bottom_nav_anim_reverse = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_up_anim_reverse);
                        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_note);
                        bottomNavigationView.startAnimation(bottom_nav_anim_reverse);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            bottomNavigationView.setVisibility(View.GONE);

                        }, 300);

                    });

                } else {

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        keyboard.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    }, 100);
                }
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

            SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

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

                        editor.putBoolean("WhiteHighlight?", false);
                        editor.putBoolean("BlackHighlight?", false);
                        editor.apply();

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

        SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

            if (highlight) {

                switch (color) {

                    case "Clear":

                        ImageButton ClearHighlight =  getActivity().findViewById(R.id.clearhighlight);

                        //mEditor.removeFormat();
                        mEditor.setTextBackgroundColor(Color.WHITE);

                        editor.putBoolean("BlackHighlight?", false);
                        editor.putBoolean("WhiteHighlight?", true);
                        editor.apply();

                        OpenOrCloseColors(true, true);

                        ClearHighlight.setVisibility(View.GONE);

                        break;

                    case "Black":

                            mEditor.setTextBackgroundColor(Color.parseColor("#CCC3FF"));

                        editor.putBoolean("BlackHighlight?", true);
                        editor.putBoolean("WhiteHighlight?", false);
                        editor.apply();

                       // new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            //mEditor.setTextColor(Color.WHITE);

                       //}, 250);

                        OpenOrCloseColors(true, true);


                        break;

                    case "Blue":


                            mEditor.setTextBackgroundColor(Color.parseColor("#8ECAFF"));

                        editor.putBoolean("BlackHighlight?", false);
                        editor.putBoolean("WhiteHighlight?", false);
                        editor.apply();



                        OpenOrCloseColors(true, true);


                        break;

                    case "Purple":


                            mEditor.setTextBackgroundColor(Color.parseColor("#FFCCF4"));

                        editor.putBoolean("BlackHighlight?", false);
                        editor.putBoolean("WhiteHighlight?", false);
                        editor.apply();


                        OpenOrCloseColors(true, true);


                        break;

                    case "Magenta":


                            mEditor.setTextBackgroundColor(Color.parseColor("#B1FFF5"));

                        editor.putBoolean("BlackHighlight?", false);
                        editor.putBoolean("WhiteHighlight?", false);
                        editor.apply();



                        OpenOrCloseColors(true, true);


                        break;

                    case "Orange":


                            mEditor.setTextBackgroundColor(Color.parseColor("#FFB89C"));

                        editor.putBoolean("BlackHighlight?", false);
                        editor.putBoolean("WhiteHighlight?", false);
                        editor.apply();


                        OpenOrCloseColors(true, true);


                        break;

                    case "Yellow":

                            mEditor.setTextBackgroundColor(Color.parseColor("#EEFF00"));

                        editor.putBoolean("BlackHighlight?", false);
                        editor.putBoolean("WhiteHighlight?", false);
                        editor.apply();


                        OpenOrCloseColors(true, true);


                        break;

                    case "Green":

                            mEditor.setTextBackgroundColor(Color.parseColor("#A0FF8A"));

                        editor.putBoolean("BlackHighlight?", false);
                        editor.putBoolean("WhiteHighlight?", false);
                        editor.apply();

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

    private void OpenOrCloseTextFormatSize(Boolean close) {

        new Thread(() -> {

            ImageButton ChangeColor =  getActivity().findViewById(R.id.ChangeColor);
            ImageButton Bold =  getActivity().findViewById(R.id.Bold);
            ImageButton Italic =  getActivity().findViewById(R.id.Italic);
            ImageButton Underline =  getActivity().findViewById(R.id.Underline);
            ImageButton Striketrough =  getActivity().findViewById(R.id.Striketrough);
            ImageButton HighlightText =  getActivity().findViewById(R.id.HighlightText);

            ImageButton Increase =  getActivity().findViewById(R.id.IncreaseSize);
            ImageButton Decrease =  getActivity().findViewById(R.id.DecreaseSize);
            ImageButton Left =  getActivity().findViewById(R.id.AlignLeft);
            ImageButton Center =  getActivity().findViewById(R.id.AlignCenter);
            ImageButton Right =  getActivity().findViewById(R.id.AlignRight);



            SharedPreferences preferences = getActivity().getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            if (close) {

                    new Handler(Looper.getMainLooper()).post(() -> {

                        Increase.setVisibility(View.GONE);
                        Decrease.setVisibility(View.GONE);
                        Left.setVisibility(View.GONE);
                        Center.setVisibility(View.GONE);
                        Right.setVisibility(View.GONE);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            ChangeColor.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Italic.setVisibility(View.VISIBLE);
                            Bold.setVisibility(View.VISIBLE);
                            Underline.setVisibility(View.VISIBLE);
                            Striketrough.setVisibility(View.VISIBLE);
                            HighlightText.setVisibility(View.VISIBLE);
                        }, 200);

                        CloseOpenedText = false;

                    });


            } else {

                new Handler(Looper.getMainLooper()).post(() -> {

                    ChangeColor.setVisibility(View.GONE);
                    Bold.setVisibility(View.GONE);
                    Italic.setVisibility(View.GONE);
                    Bold.setVisibility(View.GONE);
                    Underline.setVisibility(View.GONE);
                    Striketrough.setVisibility(View.GONE);
                    HighlightText.setVisibility(View.GONE);


                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        Increase.setVisibility(View.VISIBLE);
                        Decrease.setVisibility(View.VISIBLE);
                        Left.setVisibility(View.VISIBLE);
                        Center.setVisibility(View.VISIBLE);
                        Right.setVisibility(View.VISIBLE);
                    }, 250);

                    CloseOpenedText = false;
                });
            }

        }).start();
    }
}
