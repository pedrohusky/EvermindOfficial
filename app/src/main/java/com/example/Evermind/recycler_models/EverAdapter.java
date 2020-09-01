package com.example.Evermind.recycler_models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Evermind.EverDataBase;
import com.example.Evermind.EverDraw;
import com.example.Evermind.EvermindEditor;
import com.example.Evermind.MainActivity;
import com.example.Evermind.R;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EverAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static EvermindEditor WichLayoutIsActive;
    private static int ActiveEditorPosition;
    private static List<Item> itemList;
    private static int ID;
    private static EverDataBase everDataBase;
    private static String contents_text;
    private static String contents;
    private static String[] contents_array;
    private static ArrayList<String> array = new ArrayList<>();
    private static Context context;
    private static RelativeLayout relativeLayout;
    private static ImageView imageView;
    private static String[] arrayToAdd;
    private static EverDraw selectedDraw;
    private static int SelectedDrawPosition;
    private static ArrayList<EverDraw> everDraws = new ArrayList<>();

    private static EverAdapter.ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;

    public EverAdapter(Context context, List<Item> items, int id, EverDataBase dataBase, String strings, String[] toAdd) {

        itemList = items;
        ID = id;
        everDataBase = dataBase;
        contents = strings;
        EverAdapter.context = context;
        arrayToAdd = toAdd;

        everDraws = new ArrayList<>();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            return new ContentViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recyclerview_editor_layout,
                            parent,
                            false
                    )
            );
        } else {
            return new DrawViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recyclerview_image_layout,
                            parent,
                            false
                    )
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        everDraws.add(selectedDraw);

        if (getItemViewType(position) == 0) {

            Content content = (Content) itemList.get(position).getObject();
            ((ContentViewHolder)holder).setContentHTML(content);


        } else {
            Draw draw = (Draw) itemList.get(position).getObject();
            ((DrawViewHolder)holder).setDrawContent(draw);

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }





    static class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private EvermindEditor everEditor;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

                everEditor = itemView.findViewById(R.id.recycler_everEditor);
                relativeLayout = itemView.findViewById(R.id.editorLinearLayout);
                imageView = itemView.findViewById(R.id.editorImage);

                everEditor.setPadding(8, 15, 15, 8);
                everEditor.setEditorFontSize(22);

                itemView.setOnClickListener(this);

                //TODO IMPORTANT CODE \/ \/ \/ \/ \/

                everEditor.setOnClickListener(this);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML(Content contentHTML) {


                everEditor.setHtml(contentHTML.getContent());


                if (everEditor.getHtml().equals("")) {

                    if (getLayoutPosition() != 0) {
                        imageView.setVisibility(View.GONE);
                        everEditor.setVisibility(View.GONE);
                        System.out.println("Position = " + getLayoutPosition() + " GONE");
                    }
                    if (itemList.size() - 1 == getLayoutPosition()) {
                        everEditor.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        System.out.println("Position = " + getLayoutPosition() + " inicially was gone but turned visibler");
                    }
                }


            everEditor.setOnFocusChangeListener((view, b) -> {

                WichLayoutIsActive = ((EvermindEditor) view);
                ActiveEditorPosition = getLayoutPosition() / 2;


                ((MainActivity) context).OnFocusChangeEditor(b);
            });

            everEditor.setOnTextChangeListener(text -> {

                new Thread(() -> {

                    for (String splitted : arrayToAdd) {

                        if (splitted.endsWith("┼")) {
                            array.add(splitted);
                        } else {
                            array.add(splitted + "┼");
                        }
                    }

                    contents_text = GetActiveEditor().getHtml();

                    if (ActiveEditorPosition >= array.size()) {
                        array.add("");
                    }

                    if (GetActiveEditor().getHtml().equals("")) {
                        array.set(ActiveEditorPosition, GetActiveEditor().getHtml());
                    } else {
                        array.set(ActiveEditorPosition, GetActiveEditor().getHtml() + "┼");
                    }

                    contents_array = array.toArray(new String[0]);



                    String joined_arrayString = String.join("", contents_array);

                    everDataBase.editContent(Integer.toString(ID), replaceRGBColorsWithHex(joined_arrayString));


                    contents = joined_arrayString;

                    arrayToAdd = array.toArray(new String[0]);

                    array.clear();
                }).start();
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    static class DrawViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView everDraw;

        public DrawViewHolder(@NonNull View itemView) {
            super(itemView);
                everDraw = itemView.findViewById(R.id.recycler_imageView);
                selectedDraw = itemView.findViewById(R.id.draw_imageLayout);

                itemView.setOnClickListener(this);





                //TODO IMPORTANT CODE \/ \/ \/ \/ \/


                everDraw.setOnClickListener(this);

                everDraw.setOnLongClickListener(view -> {
                    int p = getLayoutPosition();

                    if (mClickListener != null)
                        mClickListener.onLongPress(view, p);
                    System.out.println(p);
                    return false;
                });


                ////TODO/////////////// /\ /\ /\ /\

                itemView.setOnLongClickListener(view -> {
                    int p = getLayoutPosition();

                    if (mClickListener != null)
                        mClickListener.onLongPress(view, p);
                    System.out.println(p);

                    return true;// returning true instead of false, works for me
                });
        }

        void setDrawContent(Draw draw) {

            Bitmap bitmap = BitmapFactory.decodeFile(draw.getFileLocation());

            everDraw.setImageBitmap(bitmap);

        }

        @Override
        public void onClick(View view) {

            SelectedDrawPosition = getLayoutPosition();
            selectedDraw = everDraws.get(SelectedDrawPosition);
            System.out.println(selectedDraw.toString());

            selectedDraw.setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams params = selectedDraw.getLayoutParams();

            params.height = everDraw.getHeight();

            selectedDraw.setLayoutParams(params);
            System.out.println("DEU");
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
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
            String hexColorString = "#" + rHexFormatted + gHexFormatted + bHexFormatted;

            if (foundRGBColor != null) {
                html = html.replaceAll(Pattern.quote(foundRGBColor), hexColorString);
            }
        }
        return html;
    }

    public static EvermindEditor GetActiveEditor() {
        return WichLayoutIsActive;
    }

    // allows clicks events to be caught
    public static void setClickListener(EverAdapter.ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public void setOnLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mLongClick = onItemLongClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onClick(View view);

        void onLongPress(View view, int position);
    }

    public void growDraw(int position) {

    }

    public void UpdateAdapter(List<Item> item, String content, String[] toAdd) {

            contents = content;

            itemList = item;

            arrayToAdd = toAdd;

            notifyDataSetChanged();
    }

    public static String GetContents() {
        return contents;
    }
}
