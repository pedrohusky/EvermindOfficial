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
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.Evermind.DataBaseHelper;
import com.example.Evermind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;

import static android.content.Context.MODE_PRIVATE;
import static android.text.Spanned.SPAN_COMPOSING;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static com.example.Evermind.ui.grid.ui.main.NotesScreen.databaseHelper;

public class NoteEditorFragmentJavaFragment extends Fragment {

    private NoteEditorFragmentMainViewModel mViewModel;

    private DataBaseHelper dataBaseHelper;

    private String text;

    private RichEditor mEditor;

    boolean Black = false;
    boolean Blue = false;
    boolean Purple = false;
    boolean Magenta = false;
    boolean Orange = false;
    boolean Yellow = false;
    boolean Green = false;
    boolean BlackH = false;
    boolean BlueH = false;
    boolean PurpleH = false;
    boolean MagentaH = false;
    boolean OrangeH = false;
    boolean YellowH = false;
    boolean GreenH = false;

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

        new Thread(() -> {

            mEditor = (RichEditor) getActivity().findViewById(R.id.ToSaveNoteText);
            mEditor.setEditorHeight(500);
            mEditor.setEditorFontSize(22);
            mEditor.setPadding(10, 10, 10, 10);

            ImageButton ChangeColor =  getActivity().findViewById(R.id.ChangeColor);
            ImageButton HighlightText =  getActivity().findViewById(R.id.HighlightText);
            ImageButton TextSize =  getActivity().findViewById(R.id.TextSize);

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


            Black.setOnClickListener(view -> Black.post(() ->  ColorClickedSwitcher("Black", false)));

            Blue.setOnClickListener(view -> Blue.post(() -> ColorClickedSwitcher("Blue", false)));

            Purple.setOnClickListener(view -> Purple.post(() -> ColorClickedSwitcher("Purple", false)));

            Magenta.setOnClickListener(view -> Magenta.post(() -> ColorClickedSwitcher("Magenta", false)));

            Orange.setOnClickListener(view -> Orange.post(() -> ColorClickedSwitcher("Orange", false)));

            Yellow.setOnClickListener(view -> Yellow.post(() -> ColorClickedSwitcher("Yellow", false)));

            Green.setOnClickListener(view -> Green.post(() -> ColorClickedSwitcher("Green", false)));

            ClearHighlight.setOnClickListener(view -> ClearHighlight.post(() -> ColorClickedSwitcher("Clear", true)));

            BlackHighlight.setOnClickListener(view -> Black.post(() -> ColorClickedSwitcher("Black", true)));

            BlueHighlight.setOnClickListener(view -> Blue.post(() -> ColorClickedSwitcher("Blue", true)));

            PurpleHighlight.setOnClickListener(view -> Purple.post(() -> ColorClickedSwitcher("Purple", true)));

            MagentaHighlight.setOnClickListener(view -> Magenta.post(() -> ColorClickedSwitcher("Magenta", true)));

            OrangeHighlight.setOnClickListener(view -> Orange.post(() -> ColorClickedSwitcher("Orange", true)));

            YellowHighlight.setOnClickListener(view -> Yellow.post(() -> ColorClickedSwitcher("Yellow", true)));

            GreenHighlight.setOnClickListener(view -> Green.post(() -> ColorClickedSwitcher("Green", true)));

            ChangeColor.setOnClickListener(view -> OpenColors(false));

            HighlightText.setOnClickListener(view ->  OpenColors(true));

            TextSize.setOnClickListener(view -> mEditor.setFontSize(7));




       //     getActivity().findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
         //       @Override public void onClick(View v) {
       //             mEditor.undo();
       //         }
        //    });

         //   getActivity().findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
        //        @Override public void onClick(View v) {
       //             mEditor.redo();
        //        }
        //    });

            getActivity().findViewById(R.id.Bold).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.setBold();
                }
            });

            getActivity().findViewById(R.id.Italic).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
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
                @Override public void onClick(View v) {
                    mEditor.setStrikeThrough();
                }
            });

            getActivity().findViewById(R.id.Underline).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
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

       //     getActivity().findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
       //         @Override public void onClick(View v) {
       //             mEditor.setAlignLeft();
      //          }
      //      });

       //     getActivity().findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
       //         @Override public void onClick(View v) {
      //              mEditor.setAlignCenter();
      //          }
       //     });

    //        getActivity().findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
     //           @Override public void onClick(View v) {
      //              mEditor.setAlignRight();
     //           }
     //       });

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


     ///////   EditText content_editText = getActivity().findViewById(R.id.ToSaveNoteText);

        EditText title_editText = getActivity().findViewById(R.id.myEditText);

        dataBaseHelper = new DataBaseHelper(getActivity());


    //    notes = databaseHelper.getContentsFromDatabase();
     //   titles = databaseHelper.getTitlesFromDatabase();



      //  String title_text = titles.get(id);
      //  String content_text = notes.get(id);

        mViewModel = ViewModelProviders.of(this).get(NoteEditorFragmentMainViewModel.class);


            new Thread(() -> {

                String title = preferences.getString("title", "");

                new Handler(Looper.getMainLooper()).post(() -> {

                        title_editText.setText(title);

                });
            }).start();


            new Thread(() -> {

                String content = preferences.getString("content", "");

              //  Spanned content_html = null;
             ///   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
             //       TextHtml = Html.fromHtml(content, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH);
             //   }

        new Handler(Looper.getMainLooper()).post(() -> {

                mEditor.setHtml(content);

        });
            }).start();


            mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
                @Override
                public void onTextChange(String text) {
                    new Thread(() -> {

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            dataBaseHelper.editContent(Integer.toString(id), mEditor.getHtml());

                        }, 1000);

                    }).start();
                }
            });

        }).start();
    }

    private void OpenColors(Boolean highlight) {

        new Thread(() -> {

            ImageButton Textsize =  getActivity().findViewById(R.id.TextSize);
            ImageButton ChangeColor =  getActivity().findViewById(R.id.ChangeColor);
            ImageButton Bold =  getActivity().findViewById(R.id.Bold);
            ImageButton Italic =  getActivity().findViewById(R.id.Italic);
            ImageButton Underline =  getActivity().findViewById(R.id.Underline);
            ImageButton Striketrough =  getActivity().findViewById(R.id.Striketrough);
            //ImageButton Subscript = findViewById(R.id.Subscript);
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

            if (highlight) {
                new Handler(Looper.getMainLooper()).post(() -> {

                    Textsize.setVisibility(View.GONE);
                    ChangeColor.setVisibility(View.GONE);
                    Bold.setVisibility(View.GONE);
                    Italic.setVisibility(View.GONE);
                    Bold.setVisibility(View.GONE);
                    Underline.setVisibility(View.GONE);
                    Striketrough.setVisibility(View.GONE);
                    HighlightText.setVisibility(View.GONE);
                    ClearHighlight.setVisibility(View.VISIBLE);



                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Textsize.setVisibility(View.GONE);
                        // ChangeColor.setVisibility(View.GONE);

                        BlackHighlight.setVisibility(View.VISIBLE);
                        BlueHighlight.setVisibility(View.VISIBLE);
                        PurpleHighlight.setVisibility(View.VISIBLE);
                        MagentaHighlight.setVisibility(View.VISIBLE);
                        OrangeHighlight.setVisibility(View.VISIBLE);
                        YellowHighlight.setVisibility(View.VISIBLE);
                        GreenHighlight.setVisibility(View.VISIBLE);
                    }, 250);
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

                    Textsize.setVisibility(View.GONE);
                    Bold.setVisibility(View.GONE);
                    Italic.setVisibility(View.GONE);
                    Bold.setVisibility(View.GONE);
                    Underline.setVisibility(View.GONE);
                    Striketrough.setVisibility(View.GONE);
                    HighlightText.setVisibility(View.GONE);


                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Textsize.setVisibility(View.GONE);
                        // ChangeColor.setVisibility(View.GONE);

                        Black.setVisibility(View.VISIBLE);
                        Blue.setVisibility(View.VISIBLE);
                        Purple.setVisibility(View.VISIBLE);
                        Magenta.setVisibility(View.VISIBLE);
                        Orange.setVisibility(View.VISIBLE);
                        Yellow.setVisibility(View.VISIBLE);
                        Green.setVisibility(View.VISIBLE);
                    }, 250);
                });
            }
        }).start();
    }

    private void CloseOpenedColors(Boolean highlight) {

        new Thread(() -> {

            ImageButton Textsize =  getActivity().findViewById(R.id.TextSize);
            ImageButton ChangeColor =  getActivity().findViewById(R.id.ChangeColor);
            ImageButton Bold =  getActivity().findViewById(R.id.Bold);
            ImageButton Italic =  getActivity().findViewById(R.id.Italic);
            ImageButton Underline =  getActivity().findViewById(R.id.Underline);
            ImageButton Striketrough =  getActivity().findViewById(R.id.Striketrough);
            //ImageButton Subscript = findViewById(R.id.Subscript);
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
                        Textsize.setVisibility(View.VISIBLE);
                        ChangeColor.setVisibility(View.VISIBLE);
                        Bold.setVisibility(View.VISIBLE);
                        Italic.setVisibility(View.VISIBLE);
                        Bold.setVisibility(View.VISIBLE);
                        Underline.setVisibility(View.VISIBLE);
                        Striketrough.setVisibility(View.VISIBLE);
                        HighlightText.setVisibility(View.VISIBLE);
                    }, 200);

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
                        Textsize.setVisibility(View.VISIBLE);
                        //  ChangeColor.setVisibility(View.VISIBLE);
                        Bold.setVisibility(View.VISIBLE);
                        Italic.setVisibility(View.VISIBLE);
                        Bold.setVisibility(View.VISIBLE);
                        Underline.setVisibility(View.VISIBLE);
                        Striketrough.setVisibility(View.VISIBLE);
                        HighlightText.setVisibility(View.VISIBLE);
                    }, 200);

                });
            }

        }).start();
    }

    public void ColorClickedSwitcher (String color, boolean highlight) {

            if (highlight) {

                switch (color) {

                    case "Clear":

                        ImageButton ClearHighlight =  getActivity().findViewById(R.id.clearhighlight);

                        mEditor.removeFormat();

                        CloseOpenedColors(true);

                        ClearHighlight.setVisibility(View.GONE);

                        break;

                    case "Black":


                            mEditor.setTextBackgroundColor(Color.BLACK);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {

                            mEditor.setTextColor(Color.WHITE);

                        }, 1350);

                        CloseOpenedColors(true);


                        break;

                    case "Blue":


                            mEditor.setTextBackgroundColor(Color.parseColor("#89DDFF"));



                        CloseOpenedColors(true);


                        break;

                    case "Purple":


                            mEditor.setTextBackgroundColor(Color.parseColor("#FFCCF4"));


                        CloseOpenedColors(true);


                        break;

                    case "Magenta":


                            mEditor.setTextBackgroundColor(Color.parseColor("#BFFFF7"));



                        CloseOpenedColors(true);


                        break;

                    case "Orange":


                            mEditor.setTextBackgroundColor(Color.parseColor("#FFB89C"));


                        CloseOpenedColors(true);


                        break;

                    case "Yellow":

                            mEditor.setTextBackgroundColor(Color.parseColor("#EEFF00"));


                        CloseOpenedColors(true);


                        break;

                    case "Green":

                            mEditor.setTextBackgroundColor(Color.parseColor("#A0FF8A"));

                        CloseOpenedColors(true);


                        break;

                    default:


                        break;
                }
            } else {
                switch (color) {

                    case "Black":

                            mEditor.setTextColor(Color.parseColor("#000000"));


                        CloseOpenedColors(false);


                        break;

                    case "Blue":

                            mEditor.setTextColor(Color.parseColor("#6CCAEF"));

                        Blue = !Blue;


                        CloseOpenedColors(false);


                        break;

                    case "Purple":

                            mEditor.setTextColor(Color.parseColor("#c371f9"));

                        Purple = !Purple;

                        CloseOpenedColors(false);


                        break;

                    case "Magenta":

                            mEditor.setTextColor(Color.parseColor("#EDF52E66"));

                        Magenta = !Magenta;


                        CloseOpenedColors(false);


                        break;

                    case "Orange":


                            mEditor.setTextColor(Color.parseColor("#F9A75D"));

                        Orange = !Orange;

                        CloseOpenedColors(false);


                        break;

                    case "Yellow":


                            mEditor.setTextColor(Color.parseColor("#FAF075"));

                        Yellow = !Yellow;

                        CloseOpenedColors(false);


                        break;

                    case "Green":


                            mEditor.setTextColor(Color.parseColor("#8de791"));

                        Green = !Green;

                        CloseOpenedColors(false);


                        break;

                    default:


                        break;
                }
            }
    }
}