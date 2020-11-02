package com.example.Evermind.recycler_models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Evermind.EverDraw;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import com.koushikdutta.ion.Ion;
import com.sysdata.kt.htmltextview.SDHtmlTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.richeditor.RichEditor;

public class EverAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CardView noteScreenCard;
    private RichEditor ActiveEditor;
    private int ActiveEditorPosition;
    private ArrayList<EverLinkedMap> itemList;
    private final int ID;
    private final int Position;
    private final ArrayList<String> array = new ArrayList<>();
    private final Context context;
    private List<String> arrayToAdd;
    private EverDraw everDraw;
    public int SelectedDrawPosition;
    private final ArrayList<EverDraw> everDraws;
    private final ArrayList<ImageView> images = new ArrayList<>();
    private int FinalYHeight;
    private final CardView cardView;
    private final RecyclerView recyclerView;
    private RichEditor everEditor;
    private ImageView everDrawImage;
    private SDHtmlTextView editorDecoy;
    private ItemClickListener mClickListener;
    private final boolean fromNoteScreen;
   // private final RecyclerView imageRecyclerView;
    private final TextView textView;
    private SDHtmlTextView everTextView;
    private ImageView everImage;

    public EverAdapter(Context context, ArrayList<EverLinkedMap> items, int id, int position, @Nullable List<String> toAdd, CardView card, RecyclerView recyclerView, @Nullable RecyclerView recyclerView2, @Nullable TextView title, boolean fromNoteScreen) {

        itemList = items;
        ID = id;
        Position = position;
        this.context = context;
        arrayToAdd = toAdd;
        cardView = card;
      //  imageRecyclerView = recyclerView2;
        textView = title;
        this.recyclerView = recyclerView;
        this.fromNoteScreen = fromNoteScreen;
        FinalYHeight = 0;
        everDraws = new ArrayList<>();
        images.clear();
        everDraws.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (fromNoteScreen) {
            return new ViewViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recyclerview_note_screen_textview_layout,
                            parent,
                            false
                    )
            );
        } else {

            return new ContentViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.testrecyclerview_editor_layout,
                            parent,
                            false
                    )
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (fromNoteScreen) {
            everTextView.setPadding(15, 15, 15, 15);

            if (((MainActivity)context).noteModelSection.get(Position).getNoteColor().equals("000000")) {
                noteScreenCard.setElevation(0);
            }

            ((ViewViewHolder) holder).setContentHTML(itemList.get(position).getContent());
            ((ViewViewHolder) holder).setDrawContent(itemList.get(position).getDrawLocation());
        } else {

            everEditor.setPaddingRelative(8, 8, 8, 8);
            everEditor.setEditorFontSize(22);
            ((ContentViewHolder) holder).setContentHTML(itemList.get(position).getContent());
            ((ContentViewHolder) holder).setDrawContent(itemList.get(position).getDrawLocation());
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // return itemList.get(position).getType();
        return position;
    }

    class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        @SuppressLint("ClickableViewAccessibility")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            everEditor = itemView.findViewById(R.id.evermindEditor);
            everDrawImage = itemView.findViewById(R.id.recycler_imageView);
            everDraw = itemView.findViewById(R.id.everDraw);
            editorDecoy = itemView.findViewById(R.id.editorDecoy);

            everEditor.setOnInitialLoadListener(isReady -> {
                if (getLayoutPosition() == getItemCount() - 1) {
                    if (!((MainActivity)context).noteCreator.hasImages) {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteCreator.startPostponeTransition(), 25);
                    }
                }
            });

            itemView.setOnClickListener(this);

            everDraw.setOnTouchListener((view, motionEvent) -> {

                recyclerView.suppressLayout(true);

                int y = (int) motionEvent.getY();

                if (y >= FinalYHeight) {
                    FinalYHeight = y;
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {


                    System.out.println("Final Height =" + FinalYHeight);


                    if (y >= everDrawImage.getHeight() - 75) {

                        new Handler(Looper.getMainLooper()).post(() -> {

                            recyclerView.suppressLayout(false);

                            TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
                                    .addTransition(new ChangeBounds()));

                            ViewGroup.LayoutParams params = everDraw.getLayoutParams();

                            params.height = FinalYHeight + 100;

                            everDraw.setLayoutParams(params);

                            new Handler(Looper.getMainLooper()).postDelayed(() -> recyclerView.suppressLayout(true), 150);

                        });

                        return false;
                    }
                }
                return false;
            });


            itemView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)
                    mClickListener.onLongPress(view, p);
                System.out.println(p);

                return true;// returning true instead of false, works for me
            });
            //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            // everEditor.setOnClickListener(this);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML(String contentHTML) {

            if (contentHTML.equals("▓")) {
                System.out.println("Position = " + getLayoutPosition() + " GONE1");
            } else if (contentHTML.equals("<br>") && getLayoutPosition() != 0 && getLayoutPosition() != itemList.size() - 1) {
                System.out.println("Position = " + getLayoutPosition() + " GONE2");

            } else {

                editorDecoy.setVisibility(View.VISIBLE);
                editorDecoy.setHtmlText(contentHTML);
                everEditor.setHtml(contentHTML);
                everEditor.setVisibility(View.VISIBLE);
            }

            everEditor.setOnFocusChangeListener((view, b) -> {
                ActiveEditor = ((RichEditor) view);
                ActiveEditorPosition = getLayoutPosition();
                ((MainActivity) context).OnFocusChangeEditor(b);
            });

            everEditor.setOnTextChangeListener(text -> updateContents());
        }


        void setDrawContent(String draw) {

            if (!draw.equals("")) {

                everDraws.add(everDraw);

                if (!draw.equals("▓")) {

                    Ion.with(everDrawImage)
                            .error(R.drawable.ic_baseline_clear_24)
                            .animateLoad(R.anim.grid_new_item_anim)
                            .animateIn(R.anim.grid_new_item_anim)
                            .smartSize(true)
                            .centerCrop()
                            .load(draw);

                    everDrawImage.setVisibility(View.VISIBLE);
                    images.add(everDrawImage);
                }
            }

        }

        @Override
        public void onClick(View view) {

            if (everDraw != null && everDraw.getVisibility() == View.VISIBLE) {
                //  everDraw.setVisibility(View.GONE);

                System.out.println("Draw gone.");
            }

            FinalYHeight = 0;

            SelectedDrawPosition = getLayoutPosition();
            everDraw = everDraws.get(SelectedDrawPosition);
            System.out.println("amount = " + everDraws.size() + "And they are = " + everDraw.toString());


            ViewGroup.LayoutParams params = everDraw.getLayoutParams();

            params.height = getDrawBitmap(getSelectedDrawPosition()).getHeight();

            everDraw.setLayoutParams(params);

            everDraw.setVisibility(View.VISIBLE);

            System.out.println("select draw hight= " + everDraw.getHeight() + "everdraw height = " + everDrawImage.getMeasuredHeight());

            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    class ViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        @SuppressLint("ClickableViewAccessibility")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public ViewViewHolder(@NonNull View itemView) {
            super(itemView);

            everTextView = itemView.findViewById(R.id.recycler_everEditor);
            everImage = itemView.findViewById(R.id.recycler_Image);
            noteScreenCard = itemView.findViewById(R.id.card_NoteScreen);

            everTextView.setOnClickListener(view -> textView.callOnClick());
            everImage.setOnClickListener(view -> textView.callOnClick());


        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML(String contentHTML) {

            if (!contentHTML.equals("▓")) {
                everTextView.setHtmlText(contentHTML);
                everTextView.setVisibility(View.VISIBLE);

                if (Position == ((MainActivity)context).noteModelSection.size()-1) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteScreen.startPostponedEnterTransition(), 25);
                }

                if (contentHTML.length() <= 10) {
                    everTextView.setTextSize(19);
                }

                if (contentHTML.length() <= 5) {
                    everTextView.setTextSize(22);
                }

                if (contentHTML.length() >= 10) {
                    everTextView.setTextSize(16);
                }
            }
        }


        void setDrawContent(String draw) {

            if (!draw.equals("")) {

                if (!draw.equals("┼")) {

                    if (new File(draw).exists()) {
                        everImage.setVisibility(View.VISIBLE);
                        Ion.with(everImage)
                                .error(R.drawable.ic_baseline_clear_24)
                                .animateLoad(R.anim.grid_new_item_anim)
                                .animateIn(R.anim.grid_new_item_anim)
                                .centerCrop()
                                .smartSize(true)
                                .load(draw);
                    }
                }
            }
        }

        @Override
        public void onClick(View view) {

            if (everDraw != null && everDraw.getVisibility() == View.VISIBLE) {
                //  everDraw.setVisibility(View.GONE);

                System.out.println("Draw gone.");
            }

            FinalYHeight = 0;

            SelectedDrawPosition = getLayoutPosition();
            everDraw = everDraws.get(SelectedDrawPosition);
            System.out.println("amount = " + everDraws.size() + "And they are = " + everDraw.toString());


            ViewGroup.LayoutParams params = everDraw.getLayoutParams();

            params.height = getDrawBitmap(getSelectedDrawPosition()).getHeight();

            everDraw.setLayoutParams(params);

            everDraw.setVisibility(View.VISIBLE);

            System.out.println("select draw hight= " + everDraw.getHeight() + "everdraw height = " + everDrawImage.getMeasuredHeight());

            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
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


    public Bitmap getDrawBitmap(int position) {
        return Ion.with(images.get(position)).getBitmap();
    }


    public String getImagePath(int position) {

        return itemList.get(position).getDrawLocation();
    }

    public RichEditor GetActiveEditor() {
        return ActiveEditor;
    }

    // allows clicks events to be caught
    public void setClickListener(EverAdapter.ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onLongPress(View view, int position);
    }

    public void UpdateAdapter(ArrayList<EverLinkedMap> item, List<String> toAdd, boolean notifyChange, boolean removed, boolean add, int position) {

        array.clear();

        // updateList(item);

        itemList = item;

        arrayToAdd = toAdd;

        if (notifyChange) {
            if (removed) {
                if (position == itemList.size() - 1) {
                    recyclerView.removeViewAt(position);
                } else {
                    System.out.println("position is = " + position);
                    notifyItemRemoved(position);
                    recyclerView.removeViewAt(position + 1);

                    recyclerView.removeViewAt(position);
                    notifyItemChanged(position);
                }
            } else {
                recyclerView.removeViewAt(position);
                notifyItemChanged(position);
                System.out.println("item notified at position " + position);
            }
        } else if (add) {
            recyclerView.removeViewAt(position - 1);
            notifyItemInserted(position - 1);
        } else {
            notifyItemChanged(itemList.size() - 1);
        }


    }

    public EverDraw getSelectedDraw(int position) {
        return everDraws.get(position);
    }

    public int getSelectedDrawPosition() {
        return SelectedDrawPosition;
    }

    public int getFinalYHeight() {
        return FinalYHeight;
    }

    public void setFinalYHeight(int height) {
        FinalYHeight = height;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateContents() {
        new Thread(() -> {
        for (String strg : arrayToAdd) {
            if (!strg.equals("")) {
                if (strg.endsWith("┼")) {
                    array.add(strg);
                } else {
                    array.add(strg + "┼");
                }
            }
        }
        arrayToAdd = null;

        if (ActiveEditorPosition > array.size() - 1) {
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
            ((MainActivity)context).updateNote(((MainActivity) context).noteCreator.actualNote.getActualPosition(),  ((MainActivity) context).noteCreator.actualNote);
        arrayToAdd = array;
        array.clear();
    }).start();
    }
}

  //  public void insertdata(ArrayList<EverLinkedMap> insertList){
 //       EverDiffUtil diffUtilCallback = new EverDiffUtil(itemList,insertList);
 //       DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
 //       itemList.addAll(insertList);
  //      diffResult.dispatchUpdatesTo(this);

  //      //TODO TRY TO ADD DATA WITHOUT DUPLICATE AND REMOVE STATIC FROM ALL OF THIS ADAPTER
  //  }

  //  public void updateList(ArrayList<EverLinkedMap> newList){
  //      EverDiffUtil diffUtilCallback = new EverDiffUtil(itemList,newList);
  //      DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
  //      itemList.clear();
  //      itemList.addAll(newList);
  //      diffResult.dispatchUpdatesTo(this);
  //  }}

