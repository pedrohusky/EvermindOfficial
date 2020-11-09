package com.example.Evermind;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.util.TimeUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.swipe.SwipeLayout;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTEditable;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTFormat;
import com.example.Evermind.TESTEDITOR.rteditor.api.format.RTText;
import com.example.Evermind.TESTEDITOR.rteditor.api.media.RTImage;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.richeditor.RichEditor;
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

import static com.example.Evermind.TESTEDITOR.rteditor.media.crop.RotateBitmap.TAG;

public class NoteContentsBinder extends ItemBinder<EverLinkedMap, NoteContentsBinder.NoteCreatorHolder> {

    private final ArrayList<String> array = new ArrayList<>();
    private final Context context;
    private final int Size;
    public int SelectedDrawPosition;
    private WeakReference<RTEditText> ActiveEditor;
    private int ActiveEditorPosition;
    private List<EverLinkedMap> arrayToAdd;
    private int FinalYHeight;
    private WeakReference<RTEditText> everEditor;
    private WeakReference<ImageView> everDrawImage;
    private WeakReference<CardView> cardDrawRecycler;
    private WeakReference<CardView> cardEditorRecycler;
    private WeakReference<SwipeLayout> swipe;
    private WeakReference<EverDraw> draws;
    private boolean action = false;
    private int h = 0;

    public NoteContentsBinder(Context context, int size) {
        this.context = context;
        Size = size;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public NoteCreatorHolder createViewHolder(ViewGroup parent) {
        return new NoteCreatorHolder(inflate(parent, R.layout.aa));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof EverLinkedMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bindViewHolder(NoteCreatorHolder holder, EverLinkedMap everMap) {
        swipe = new WeakReference<>(holder.itemView.findViewById(R.id.testSwipe));
        everEditor = new WeakReference<>(holder.itemView.findViewById(R.id.evermindEditor));
        everEditor.get().setPaddingRelative(8, 8, 8, 8);
       // everEditor.get().setEditorFontSize(22);
        everEditor.get().setSpannableFactory(new Spannable.Factory(){
            @Override
            public Spannable newSpannable(CharSequence source) {
                return (Spannable) source;
            }
        });


        ((MainActivity) context).mRTManager.registerEditor(everEditor.get(), true);
        holder.setContentHTML(everMap.getContent());
        holder.setDrawContent(everMap.getDrawLocation());
        swipe.get().setShowMode(SwipeLayout.ShowMode.PullOut);
        swipe.get().setClickToClose(true);
        if (holder.getAdapterPosition() == Size-1) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                ((MainActivity) context).noteCreator.get().startPostponeTransition(); }, 75);

        }
    }

    private String replaceRGBColorsWithHex(String html) {
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
            int rInt = Integer.parseInt(Objects.requireNonNull(rString));
            int gInt = Integer.parseInt(Objects.requireNonNull(gString));
            int bInt = Integer.parseInt(Objects.requireNonNull(bString));

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
            String hexColorString = "#" + rHexFormatted + gHexFormatted + bHexFormatted;

            if (foundRGBColor != null) {
                html = html.replaceAll(Pattern.quote(foundRGBColor), hexColorString);
            }
        }
        return html;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateContents() {
         new Thread(() -> {
            array.clear();
            arrayToAdd = ((MainActivity) context).noteCreator.get().list.getData();
            for (EverLinkedMap strg : arrayToAdd) {
               // System.out.println("Splitted string is = " + strg.getContent());
               // if (!strg.getContent().equals("")) {
                    if (strg.getContent().endsWith("┼")) {
                        array.add(strg.getContent());
                    } else {
                        array.add(strg.getContent() + "┼");
                    }
              //  }
            }
            //   arrayToAdd = null;
            String text = "";
            if (ActiveEditor != null) {
               text = ActiveEditor.get().getText(RTFormat.HTML);
                if (ActiveEditorPosition > array.size()) {
                    array.add(text + "┼");
                } else {
                    if (text.endsWith("<br>")) {
                        array.set(ActiveEditorPosition, text.substring(0, text.length() - 4) + "┼");
                    } else {
                        array.set(ActiveEditorPosition, text + "┼");
                    }
                }

                String joined_arrayString = String.join("", array);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    //TODO MBAYBE WE CCAN REMOVE REPLACE RGB COLOR WIT HEXT BECAUSE ITS NOT FUNCTIONAL ANYMORE replaceRGBColorsWithHex(joined_arrayString)
                    ((MainActivity) context).noteCreator.get().actualNote.get().setContent(joined_arrayString);
                    ((MainActivity) context).noteCreator.get().list.get(ActiveEditorPosition).setContent(ActiveEditor.get().getText(RTFormat.HTML));
                    ((MainActivity) context).updateNote(((MainActivity) context).noteCreator.get().actualNote.get().getActualPosition(), ((MainActivity) context).noteCreator.get().actualNote.get());
                }, 1000);
            }
        }).start();
    }

    class NoteCreatorHolder extends ItemViewHolder<EverLinkedMap> implements View.OnClickListener, View.OnLongClickListener {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("ClickableViewAccessibility")
        public NoteCreatorHolder(View itemView) {
            super(itemView);
            everEditor = new WeakReference<>(itemView.findViewById(R.id.evermindEditor));
            everDrawImage = new WeakReference<>(itemView.findViewById(R.id.recycler_imageView));
        //    editorDecoy = itemView.findViewById(R.id.editorDecoy);
            cardDrawRecycler = new WeakReference<>(itemView.findViewById(R.id.cardDrawRecycler));
            cardEditorRecycler = new WeakReference<>(itemView.findViewById(R.id.cardEditorRecycler));
            swipe = new WeakReference<>(itemView.findViewById(R.id.testSwipe));
            ImageButton delete = itemView.findViewById(R.id.deleteFromRecycler);
            everEditor.get().setTextSize(22);
         //   editorDecoy.setVisibility(View.GONE);


            delete.setOnClickListener(v -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ((MainActivity)context).noteCreator.get().onLongPress(v, getAdapterPosition());
                }
            }, 350));

            delete.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!action) {
                        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
                        int color = ((MainActivity)context).defaultToolbarColor;
                        anim.addUpdateListener(animation -> {
                            // Use animation position to blend colors.
                            float position = animation.getAnimatedFraction();

                            // Apply blended color to the status bar.

                            int blended = ((MainActivity)context).blendColors(color, context.getColor(R.color.Magenta), position);

                            // Apply blended color to the ActionBar.
                            //   blended = blendColors(toolbarColor, toolbarToColor, position);
                            //  ColorDrawable background = new ColorDrawable(blended);
                            //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
                            itemView.findViewById(R.id.deleteFromRecycler).setBackgroundTintList(ColorStateList.valueOf(blended));
                        });

                        anim.setDuration(250).start();
                        action = true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
                    anim.addUpdateListener(animation -> {
                        // Use animation position to blend colors.
                        float position = animation.getAnimatedFraction();

                        // Apply blended color to the status bar.

                        int blended = ((MainActivity)context).blendColors(context.getColor(R.color.Magenta), context.getColor(R.color.GrassGreen), position);

                        // Apply blended color to the ActionBar.
                        //   blended = blendColors(toolbarColor, toolbarToColor, position);
                        //  ColorDrawable background = new ColorDrawable(blended);
                        //  Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(background);
                        itemView.findViewById(R.id.deleteFromRecycler).setBackgroundTintList(ColorStateList.valueOf(blended));
                    });

                    anim.setDuration(250).start();
                    action = false;
                }
                ((MainActivity)context).pushDownOnTouch(v, event, 0.7f, 50);
                return false;
            });

            everDrawImage.get().setOnTouchListener((v, event) -> {
                ((MainActivity)context).pushDownOnTouch(itemView.findViewById(R.id.cardDrawRecycler), event, 0.85f, 50);
                return false;
            });


            everDrawImage.get().setOnClickListener(this);
            //itemView.setOnClickListener(this);

            itemView.findViewById(R.id.everDraw).setOnTouchListener((view, motionEvent) -> {

                ((MainActivity) context).noteCreator.get().textanddrawRecyclerView.get().suppressLayout(true);
                ((SwipeLayout)itemView.findViewById(R.id.testSwipe)).setSwipeEnabled(false);

                int y = (int) motionEvent.getY();

                if (y >= FinalYHeight) {
                    FinalYHeight = y;
                    ((MainActivity) context).noteCreator.get().FinalYHeight = y;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (y >= everDrawImage.get().getHeight() - 75) {

                        new Handler(Looper.getMainLooper()).post(() -> {

                            ((MainActivity) context).noteCreator.get().textanddrawRecyclerView.get().suppressLayout(false);

                            TransitionManager.beginDelayedTransition(((MainActivity) context).cardNoteCreator.get(), new TransitionSet()
                                    .addTransition(new ChangeBounds()));

                            ViewGroup.LayoutParams params = itemView.findViewById(R.id.everDraw).getLayoutParams();

                            params.height = FinalYHeight + 100;

                            itemView.findViewById(R.id.everDraw).setLayoutParams(params);

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                ((SwipeLayout)itemView.findViewById(R.id.testSwipe)).setSwipeEnabled(true);
                                ((MainActivity) context).noteCreator.get().textanddrawRecyclerView.get().suppressLayout(true);
                            }, 150);

                        });

                        return false;
                    }
                }
                return false;
            });


            itemView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                System.out.println(p);

                return true;// returning true instead of false, works for me
            });
            //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            // everEditor.setOnClickListener(this);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML(String contentHTML) {

            everEditor = new WeakReference<>(itemView.findViewById(R.id.evermindEditor));
            cardEditorRecycler = new WeakReference<>(itemView.findViewById(R.id.cardEditorRecycler));

            if (contentHTML.equals("▓")) {
                cardEditorRecycler.get().setVisibility(View.GONE);
                everEditor.get().setVisibility(View.GONE);
                System.out.println("Position = " + getLayoutPosition() + " GONE1" + "content os =" + contentHTML);
            } else if (contentHTML.equals("<br>") && getLayoutPosition() != 0 && getLayoutPosition() != Size - 1) {
                System.out.println("Position = " + getLayoutPosition() + " GONE2");

            } else {

                cardEditorRecycler.get().setVisibility(View.VISIBLE);
                everEditor.get().setRichTextEditing(true, contentHTML);
               everEditor.get().setVisibility(View.VISIBLE);
            }


            everEditor.get().setOnRichContentListener((RTEditText.OnRichContentListener) (contentUri, description) -> {
                if (description.getMimeTypeCount() > 0 && contentUri != null) {
                    final String fileExtension = MimeTypeMap.getSingleton()
                            .getExtensionFromMimeType(description.getMimeType(0));
                    final String filename = System.currentTimeMillis() + "." + fileExtension;
                    File richContentFile = new File(((MainActivity)context).getFilesDir(), filename);
                    if (!writeToFileFromContentUri(richContentFile, contentUri)) {
                        System.out.println("Cant Write File");
                    } else {
                        System.out.println("FAILED");
                    }
                    ((MainActivity)context).mRTManager.insertImage(ActiveEditor.get(), new RTImage() {
                        @Override
                        public String getFilePath(RTFormat format) {
                            return richContentFile.getPath();
                        }

                        @Override
                        public String getFileName() {
                            return filename;
                        }

                        @Override
                        public String getFileExtension() {
                            return fileExtension;
                        }

                        @Override
                        public boolean exists() {
                            return richContentFile.exists();
                        }

                        @Override
                        public void remove() {
                            richContentFile.delete();
                        }

                        @Override
                        public int getWidth() {
                            return 0;
                        }

                        @Override
                        public int getHeight() {
                            return 0;
                        }

                        @Override
                        public long getSize() {
                            return 0;
                        }
                    });
                    }
                });





            everEditor.get().setOnFocusChangeListener((view, b) -> {
                everEditor = new WeakReference<>(itemView.findViewById(R.id.evermindEditor));
                ActiveEditor = everEditor;

                ((MainActivity) context).mRTManager.registerEditor(everEditor.get(), true);
                ((MainActivity) context).noteCreator.get().activeEditor = ActiveEditor;
                ((MainActivity) context).noteCreator.get().activeEditorPosition = getLayoutPosition();
                ActiveEditorPosition = getLayoutPosition();
                ((MainActivity) context).OnFocusChangeEditor(b);
            });

           // everEditor.get().setOnInitialLoadListener(isReady -> {
             //   if (isReady) {
              //      if (getLayoutPosition() == Size-1 ) {
                       ///////////////////////////// ((MainActivity)context).beginDelayedTransition(((MainActivity)context).findViewById(R.id.card_note_creator));
                //        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            //   if (!((MainActivity) context).noteCreator.get().hasImages) {

                   //         Toast.makeText(context, "p = " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
                    //        ((MainActivity) context).noteCreator.get().startPostponeTransition();
                            //   }


                      //  }, 300);

                    //}
                //}
           // });

            everEditor.get().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    System.out.println(everEditor.get().getText(RTFormat.HTML));
                }

                @Override
                public void afterTextChanged(Editable s) {
                    updateContents();
                }
            });
            //everEditor.get().setOnTextChangeListener(text -> updateContents());
        }

        void setDrawContent(String draw) {
            everDrawImage = new WeakReference<>(itemView.findViewById(R.id.recycler_imageView));
            cardDrawRecycler = new WeakReference<>(itemView.findViewById(R.id.cardDrawRecycler));
            swipe = new WeakReference<>(itemView.findViewById(R.id.testSwipe));

            if (new File(draw).exists()) {
                swipe.get().setVisibility(View.VISIBLE);
                cardDrawRecycler.get().setVisibility(View.VISIBLE);
                everDrawImage.get().setVisibility(View.VISIBLE);

                Ion.with(everDrawImage.get())
                        .error(R.drawable.ic_baseline_clear_24)
                        .animateLoad(R.anim.grid_new_item_anim)
                        .animateIn(R.anim.grid_new_item_anim)
                        .smartSize(true)
                        .centerCrop()
                        .load(draw);


            } else {
                swipe.get().setVisibility(View.GONE);
                cardDrawRecycler.get().setVisibility(View.GONE);
                everDrawImage.get().setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {



            InputMethodManager keyboard = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (keyboard.isActive()) {
                keyboard.hideSoftInputFromWindow(everDrawImage.get().getWindowToken(), 0);
            }


            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                FinalYHeight = 0;

                SelectedDrawPosition = getLayoutPosition();

                ImageView imageView = itemView.findViewById(R.id.recycler_imageView);
                EverDraw draw = itemView.findViewById(R.id.everDraw);

                assert draw != null;
                ViewGroup.LayoutParams params = draw.getLayoutParams();
                params.height = imageView.getHeight();

                ((MainActivity)context).beginDelayedTransition(cardDrawRecycler.get());

                draw.setLayoutParams(params);

                Bitmap b = Ion.with(imageView).getBitmap();

                ((MainActivity) context).noteCreator.get().onItemClickTemporaryINHERE_REMOVE_LATER(draw, SelectedDrawPosition, b, getItem().getDrawLocation());

            }, 250);

          }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public boolean writeToFileFromContentUri(File file, Uri uri) {
        if (file == null || uri == null) return false;
        try {
            InputStream stream = ((MainActivity)context).getContentResolver().openInputStream(uri);
            OutputStream output = new FileOutputStream(file);
            if (stream == null) return false;
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = stream.read(buffer)) != -1) output.write(buffer, 0, read);
            output.flush();
            output.close();
            stream.close();
            System.out.println("File created succesffuly. At: " + file.getPath());
            return true;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Couldn't open stream: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException on stream: " + e.getMessage());
        }
        return false;
    }
}