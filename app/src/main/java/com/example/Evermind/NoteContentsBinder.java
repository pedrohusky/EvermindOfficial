package com.example.Evermind;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.state.Dimension;

import com.daimajia.swipe.SwipeLayout;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.koushikdutta.ion.Ion;
import com.sysdata.kt.htmltextview.SDHtmlTextView;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.richeditor.RichEditor;
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_STATIC_DP;

public class NoteContentsBinder extends ItemBinder<EverLinkedMap, NoteContentsBinder.NoteCreatorHolder> {

    private final ArrayList<String> array = new ArrayList<>();
    private final Context context;
    private final int Size;
    public int SelectedDrawPosition;
    private RichEditor ActiveEditor;
    private int ActiveEditorPosition;
    private List<EverLinkedMap> arrayToAdd;
    private int FinalYHeight;
    private RichEditor everEditor;
    private ImageView everDrawImage;
    private SDHtmlTextView editorDecoy;
    private CardView cardDrawRecycler;
    private CardView cardEditorRecycler;

    private SwipeLayout swipe;
    //TODO FIX THHIS SHIT WHEN UPDATING NOTE AND ADDING A NEW NOTE !!!!!!!!!!!

    public NoteContentsBinder(Context context, int size) {
        this.context = context;
        Size = size;
    }


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
        swipe = holder.itemView.findViewById(R.id.testSwipe);
        everEditor = holder.itemView.findViewById(R.id.evermindEditor);
        everEditor.setPaddingRelative(8, 8, 8, 8);
        everEditor.setEditorFontSize(22);
        holder.setContentHTML(everMap.getContent());
        holder.setDrawContent(everMap.getDrawLocation());
        swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipe.setClickToClose(true);

        //  swipe.addDrag(SwipeLayout.DragEdge.Bottom, holder.itemView.findViewById(R.id.bkg));


//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)


        swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
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
            arrayToAdd = ((MainActivity) context).noteCreator.list.getData();
            for (EverLinkedMap strg : arrayToAdd) {
                System.out.println("Splitted string is = " + strg.getContent());
               // if (!strg.getContent().equals("")) {
                    if (strg.getContent().endsWith("┼")) {
                        array.add(strg.getContent());
                    } else {
                        array.add(strg.getContent() + "┼");
                    }
              //  }
            }
            //   arrayToAdd = null;

            if (ActiveEditorPosition > array.size()) {
                array.add(ActiveEditor.getHtml() + "┼");
            } else {
                if (ActiveEditor.getHtml().endsWith("<br>")) {
                    array.set(ActiveEditorPosition, ActiveEditor.getHtml().substring(0, ActiveEditor.getHtml().length() - 4) + "┼");
                } else {
                    array.set(ActiveEditorPosition, ActiveEditor.getHtml() + "┼");
                }
            }


            String joined_arrayString = String.join("", array);

            System.out.println("Updated content = " + joined_arrayString);

            // ((MainActivity) context).mDatabaseEver.editContent(Integer.toString(ID), replaceRGBColorsWithHex(joined_arrayString));
            //  ((MainActivity) context).notesModels.get(Position).setContent(replaceRGBColorsWithHex(joined_arrayString));
            ((MainActivity) context).noteCreator.actualNote.setContent(replaceRGBColorsWithHex(joined_arrayString));
            ((MainActivity) context).noteCreator.list.get(ActiveEditorPosition).setContent(ActiveEditor.getHtml());
            ((MainActivity) context).updateNote(((MainActivity) context).noteCreator.actualNote.getActualPosition(), ((MainActivity) context).noteCreator.actualNote);
          //  for (int i = 0; i < array.size(); i++) {
          ////      EverLinkedMap ever = new EverLinkedMap(arrayToAdd.get(i).getContent(), arrayToAdd.get(i).getDrawLocation());
           //     ever.setContent(array.get(i));
           //     arrayToAdd.set(i, ever);
           // }
            //arrayToAdd = array.toArray(new String[0]);
        }).start();
    }

    class NoteCreatorHolder extends ItemViewHolder<EverLinkedMap> implements View.OnClickListener, View.OnLongClickListener {

        @SuppressLint("ClickableViewAccessibility")
        public NoteCreatorHolder(View itemView) {
            super(itemView);
            everEditor = itemView.findViewById(R.id.evermindEditor);
            everDrawImage = itemView.findViewById(R.id.recycler_imageView);
            editorDecoy = itemView.findViewById(R.id.editorDecoy);
            cardDrawRecycler = itemView.findViewById(R.id.cardDrawRecycler);
            cardEditorRecycler = itemView.findViewById(R.id.cardEditorRecycler);
            swipe = itemView.findViewById(R.id.testSwipe);
            everEditor.setOnInitialLoadListener(isReady -> {
                if (getLayoutPosition() == Size - 1) {
                    if (!((MainActivity) context).noteCreator.hasImages) {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteCreator.startPostponeTransition(), 25);
                    }
                }
            });

            PushDownAnim.setPushDownAnimTo( itemView.findViewById(R.id.imageButton))
        .setScale( MODE_SCALE,
                0.7f )
                    .setOnClickListener( new View.OnClickListener(){
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick( View view ){
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
                                view.setBackgroundTintList(ColorStateList.valueOf(blended));
                            });

                            anim.setDuration(500).start();
                        }
                    } );

            everDrawImage.setOnTouchListener((v, event) -> {
                ((MainActivity)context).pushDownOnTouch(itemView.findViewById(R.id.cardDrawRecycler), event, 0.85f, 50);
                return false;
            });


            everDrawImage.setOnClickListener(this);
            //itemView.setOnClickListener(this);

            itemView.findViewById(R.id.everDraw).setOnTouchListener((view, motionEvent) -> {

                ((MainActivity) context).noteCreator.textanddrawRecyclerView.suppressLayout(true);
                ((SwipeLayout)itemView.findViewById(R.id.testSwipe)).setSwipeEnabled(false);

                int y = (int) motionEvent.getY();

                if (y >= FinalYHeight) {
                    FinalYHeight = y;
                    ((MainActivity) context).noteCreator.FinalYHeight = y;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    if (y >= everDrawImage.getHeight() - 75) {

                        new Handler(Looper.getMainLooper()).post(() -> {

                            ((MainActivity) context).noteCreator.textanddrawRecyclerView.suppressLayout(false);

                            TransitionManager.beginDelayedTransition(((MainActivity) context).cardNoteCreator, new TransitionSet()
                                    .addTransition(new ChangeBounds()));

                            ViewGroup.LayoutParams params = itemView.findViewById(R.id.everDraw).getLayoutParams();

                            params.height = FinalYHeight + 100;

                            itemView.findViewById(R.id.everDraw).setLayoutParams(params);

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                ((SwipeLayout)itemView.findViewById(R.id.testSwipe)).setSwipeEnabled(true);
                                ((MainActivity) context).noteCreator.textanddrawRecyclerView.suppressLayout(true);
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

            if (contentHTML.equals("▓")) {
                System.out.println("Position = " + getLayoutPosition() + " GONE1" + "content os =" + contentHTML);
            } else if (contentHTML.equals("<br>") && getLayoutPosition() != 0 && getLayoutPosition() != Size - 1) {
                System.out.println("Position = " + getLayoutPosition() + " GONE2");

            } else {

                cardEditorRecycler.setVisibility(View.VISIBLE);
                editorDecoy.setVisibility(View.VISIBLE);
                editorDecoy.setHtmlText(contentHTML);
                everEditor.setHtml(contentHTML);
                everEditor.setVisibility(View.VISIBLE);
            }

            everEditor.setOnFocusChangeListener((view, b) -> {
                ActiveEditor = itemView.findViewById(R.id.evermindEditor);
                ((MainActivity) context).noteCreator.activeEditor = ActiveEditor;
                ((MainActivity) context).noteCreator.activeEditorPosition = getLayoutPosition();
                ActiveEditorPosition = getLayoutPosition();
                ((MainActivity) context).OnFocusChangeEditor(b);
            });

            everEditor.setOnTextChangeListener(text -> updateContents());
        }

        void setDrawContent(String draw) {

            if (!draw.equals("")) {

                if (!draw.equals("▓")) {

                    Ion.with(everDrawImage)
                            .error(R.drawable.ic_baseline_clear_24)
                            .animateLoad(R.anim.grid_new_item_anim)
                            .animateIn(R.anim.grid_new_item_anim)
                            .smartSize(true)
                            .centerCrop()
                            .load(draw);

                    swipe.setVisibility(View.VISIBLE);
                    cardDrawRecycler.setVisibility(View.VISIBLE);
                    everDrawImage.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onClick(View view) {



            InputMethodManager keyboard = (InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
            if (keyboard.isActive()) {
                keyboard.hideSoftInputFromWindow(everDrawImage.getWindowToken(), 0);
            }


            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                FinalYHeight = 0;

                SelectedDrawPosition = getLayoutPosition();

                ImageView imageView = itemView.findViewById(R.id.recycler_imageView);
                EverDraw draw = itemView.findViewById(R.id.everDraw);

                assert draw != null;
                ViewGroup.LayoutParams params = draw.getLayoutParams();
                params.height = imageView.getHeight();

                ((MainActivity)context).beginDelayedTransition(cardDrawRecycler);

                draw.setLayoutParams(params);

                Bitmap b = Ion.with(imageView).getBitmap();

                ((MainActivity) context).noteCreator.onItemClickTemporaryINHERE_REMOVE_LATER(draw, SelectedDrawPosition, b, getItem().getDrawLocation());

            }, 250);

          }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}