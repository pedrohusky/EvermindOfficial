package com.example.Evermind.recycler_models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Evermind.EverDataBase;
import com.example.Evermind.EverDraw;
import com.example.Evermind.EvermindEditor;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EverAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static EvermindEditor ActiveEditor;
    private static int ActiveEditorPosition;
    private static List<EverLinkedMap> itemList;
    private static int ID;
    private static int Position;
    private static EverDataBase everDataBase;
    private static String contents;
    private static ArrayList<String> array = new ArrayList<>();
    private static Context context;
    private static ImageView imageView;
    private static String[] arrayToAdd;
    private static EverDraw everDraw;
    public static int SelectedDrawPosition;
    private static ArrayList<EverDraw> everDraws = new ArrayList<>();
    private static ArrayList<ImageView> images = new ArrayList<>();
    private static int FinalYHeight;
    private static CardView cardView;
    private static ArrayList<EvermindEditor> editors = new ArrayList<>();
    private static RecyclerView recyclerView;
    private static EvermindEditor everEditor;
    private static ImageView everDrawImage;
    private static EverAdapter.ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;

    public EverAdapter(Context context, List<EverLinkedMap> items, int id, int position, EverDataBase dataBase, String strings, String[] toAdd, CardView card, RecyclerView recyclerView) {

        itemList = items;
        ID = id;
        Position = position;
        everDataBase = dataBase;
        contents = strings;
        EverAdapter.context = context;
        arrayToAdd = toAdd;
        cardView = card;
        EverAdapter.recyclerView = recyclerView;
        FinalYHeight = 0;
        everDraws = new ArrayList<>();
        editors = new ArrayList<>();
        images.clear();
        everDraws.clear();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ContentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.testrecyclerview_editor_layout,
                        parent,
                        false
                )
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

     //   everEditor.setEditorBackgroundColor(Integer.parseInt(((MainActivity)context).notesModels.get(Position).getNoteColor()));
        everEditor.setPadding(8, 15, 15, 8);
        everEditor.setEditorHeight(20);
        everEditor.setEditorFontSize(22);
        ((ContentViewHolder)holder).setContentHTML(itemList.get(position).getContent());
        ((ContentViewHolder)holder).setDrawContent(itemList.get(position).getDrawLocation());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // return itemList.get(position).getType();
        return 1;
    }


    static class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {



        @SuppressLint("ClickableViewAccessibility")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            everEditor = itemView.findViewById(R.id.recycler_everEditor);
            imageView = itemView.findViewById(R.id.editorImage);
            everDrawImage = itemView.findViewById(R.id.recycler_imageView);
            everDraw = itemView.findViewById(R.id.everDraw);



           // everEditor.setEditorBackgroundColor(Color.LTGRAY);

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

            everEditor.setOnClickListener(this);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML(String contentHTML) {

            editors.add(everEditor);

            if (contentHTML.equals("▓")) {

                System.out.println("Position = " + getLayoutPosition() + " GONE1");
            } else if (contentHTML.equals("<br>") && getLayoutPosition() != 0 && getLayoutPosition() != itemList.size()-1){

                System.out.println("Position = " + getLayoutPosition() + " GONE2");

            } else {
                everEditor.setHtml(contentHTML);
                everEditor.setVisibility(View.VISIBLE);
                //imageView.setVisibility(View.GONE);
            }


            everEditor.setOnFocusChangeListener((view, b) -> {

                ActiveEditor = ((EvermindEditor) view);
                ActiveEditorPosition = getLayoutPosition();


                ((MainActivity) context).OnFocusChangeEditor(b);
            });

            everEditor.setOnTextChangeListener(text -> {

               // new Thread(() -> {

                    System.out.println("Arrays = " +Arrays.toString(arrayToAdd));

                    updateContents();
               // }).start();
            });
        }



        void setDrawContent(String draw) {



            if (!draw.equals("")) {

                everDraws.add(everDraw);

                if (!draw.equals("▓")) {

                    Bitmap bitmap = BitmapFactory.decodeFile(draw);

                  //  Ion.with(everDraw)
                  //          .error(R.drawable.ic_baseline_clear_24)
                   //         .animateLoad(R.anim.grid_new_item_anim)
                   //         .animateIn(R.anim.grid_new_item_anim)
                   //         .smartSize(true)
                   //        .centerCrop()
                    //        .load(draw); // was position

                    everDrawImage.setVisibility(View.VISIBLE);

                   // selectedDraw.setVisibility(View.VISIBLE);

                    everDrawImage.setImageBitmap(bitmap);



                    images.add(everDrawImage);
                } else if (getLayoutPosition() != 0 && getLayoutPosition() != itemList.size()){
                    System.out.println("GONE BY DRAW FUNCTION 1");
                }
            } else if (getLayoutPosition() != 0 && getLayoutPosition() != itemList.size() - 1){
                System.out.println("GONE BY DRAW FUNCTION 2");
            }
            //selectedDraw = null;

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
            System.out.println("amount = " + everDraws.size() + "And they are = " +  everDraw.toString());



            ViewGroup.LayoutParams params = everDraw.getLayoutParams();

            params.height = images.get(getSelectedDrawPosition()).getHeight();

            everDraw.setLayoutParams(params);

            everDraw.setVisibility(View.VISIBLE);

            System.out.println("select draw hight= "+ everDraw.getHeight()+ "everdraw height = " + everDrawImage.getMeasuredHeight());

            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
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

    public static  BitmapDrawable getDrawBitmap(int position) {
        return (BitmapDrawable) images.get(position).getDrawable();
    }



    public static  String getImagePath(int position) {

        return itemList.get(position).getDrawLocation();
    }

    public static EvermindEditor GetActiveEditor() {
        return ActiveEditor;
    }

    // allows clicks events to be caught
    public static void setClickListener(EverAdapter.ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onLongPress(View view, int position);
    }

    public void UpdateAdapter(List<EverLinkedMap> item, String content, String[] toAdd, boolean notifyChange, boolean removed, boolean add, int position) {

        array.clear();

        contents = content;

        itemList = item;

        arrayToAdd = toAdd;

        if (notifyChange) {
            if (removed) {
                if (position == itemList.size()-1) {
                    recyclerView.removeViewAt(position);
                } else {
                    System.out.println("position is = " + position);
                    notifyItemRemoved(position);
                    recyclerView.removeViewAt(position+1);

                    recyclerView.removeViewAt(position);
                    notifyItemChanged(position);
                }
            } else {
                recyclerView.removeViewAt(position);
                notifyItemChanged(position);
                System.out.println("item notified at position " + position);
            }
        } else if (add) {
           recyclerView.removeViewAt(position-1);
            notifyItemInserted(position-1);
            } else {
                notifyItemChanged(itemList.size()-1);
        }


    }

    public static String GetContents() {
        return contents;
    }

    public static EverDraw getSelectedDraw(int position) {
        return everDraws.get(position);
    }
    public static int getSelectedDrawPosition() {
        return SelectedDrawPosition;
    }

    public static int getFinalYHeight() {
        return FinalYHeight;
    }

    public static void setFinalYHeight(int height) { FinalYHeight = height; }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void updateContents() {

        for (String strg: arrayToAdd) {
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
            array.set(ActiveEditorPosition, ActiveEditor.getHtml() + "┼");
        }



        String joined_arrayString = String.join("", array);

        System.out.println("Updated content = " + joined_arrayString);

        everDataBase.editContent(Integer.toString(ID), replaceRGBColorsWithHex(joined_arrayString));
        ((MainActivity)context).notesModels.get(Position).setContent(replaceRGBColorsWithHex(joined_arrayString));
        contents = joined_arrayString;
        arrayToAdd = array.toArray(new String[0]);
        array.clear();
    }
}
