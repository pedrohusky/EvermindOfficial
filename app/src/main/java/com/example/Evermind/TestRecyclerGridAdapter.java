package com.example.Evermind;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.recycler_models.Content;
import com.example.Evermind.recycler_models.Draw;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.Item;
import com.sysdata.kt.htmltextview.SDHtmlTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRecyclerGridAdapter extends RecyclerView.Adapter<TestRecyclerGridAdapter.ViewHolder> {

    private String[] mData;
    private String[] mTitle;
    private String[] mDate;
    private Integer[] mIds;
    private static Integer[] id;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;
    private EverDataBase mEverDataBase;
    private ImagesRecyclerNoteScreenGridAdapter adapter;
    private static EverNoteScreenAdapter everNoteScreenAdapter;
    private List<Item> items;
    private List<String> bitmaps;
    private int i;
    private RecyclerView textanddrawRecyclerView;

    // data is passed into the constructor
    public TestRecyclerGridAdapter(Context contexts, String[] data, String[] title, String[] date, Integer[] ids, EverDataBase database) {

        mEverDataBase = database;
        context = contexts;
        this.mData = data;
        this.mTitle = title;
        this.mDate = date;
        this.mIds = ids;
       this.mInflater = LayoutInflater.from(context);

    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_content_with_recyclerview_visualization, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        holder.myTitleView.setText(mTitle[position]);

        SetupNoteEditorRecycler(position);

        String imagesURLs = mEverDataBase.getImageURLFromDatabaseWithID(mIds[position]);

        if (imagesURLs.length() > 0) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);

            holder.myRecyclerView.setLayoutManager(staggeredGridLayoutManager);

            adapter = new ImagesRecyclerNoteScreenGridAdapter(context, imagesURLs, position, imagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);
            holder.myRecyclerView.setAdapter(adapter);

        }


        if (holder.myTitleView.length() < 1) {
            ViewGroup.LayoutParams params = holder.myTitleView.getLayoutParams();

            params.height = 65;

            holder.myTitleView.setLayoutParams(params);
           // holder.myTextView.setTextSize(18);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.myTitleView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c371f9")));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.myTitleView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCCF4")));
            }
        }

    }


    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;

    }


    public void setClickListener(AdapterView.OnItemClickListener onItemClickListener) {
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView myTitleView;
        RecyclerView myRecyclerView;


        ViewHolder(View itemView) {
            super(itemView);

            myTitleView = itemView.findViewById(R.id.info_title);

            myRecyclerView = itemView.findViewById(R.id.RecyclerNoteScren);

            textanddrawRecyclerView = itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler);

            itemView.setOnClickListener(this);

            //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            myRecyclerView.setOnClickListener(this);

            myRecyclerView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)
                    mClickListener.onLongPress(view, p);
                System.out.println(p);
                return false;
            });

            textanddrawRecyclerView.setOnClickListener(this);

            textanddrawRecyclerView.setOnLongClickListener(view -> {
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
        ///////////////////////

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
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

    private void SetupNoteEditorRecycler(int position) {

        items = new ArrayList<>();
        bitmaps = new ArrayList<>();
        i = 0;

        String[] html = mEverDataBase.getBackgroundFromDatabaseWithID(mIds[position]).replaceAll("[\\[\\](){}]", "").trim().split("┼");

        bitmaps.addAll(Arrays.asList(html));

        System.out.println("Note at position " + position + ", and the amount of bitmaps is = " + bitmaps.size());

        String[] strings = mEverDataBase.getContentsFromDatabaseWithID(mIds[position]).replaceAll("[\\[\\](){}]", "").split("┼");

        if (html.length == 0 && strings.length == 0) {
            Content content = new Content("");
            items.add(new Item(0, content));
            System.out.println("Content added = " + content + " at position: " + position);
        }

        if (strings.length == 0 && html.length >= 1) {

            Draw draw1 = new Draw(bitmaps.get(i));
            items.add(new Item(1, draw1));
            System.out.println("Draw added = " + draw1 + " at position: " + position);
            i++;
            if (i >= strings.length) {
                Content content = new Content("");
                items.add(new Item(0, content));
                System.out.println("Content added = " + content + " at position: " + position);
            }
        } else {

            for (String text : strings) {
                Content content1 = new Content(text);
                items.add(new Item(0, content1));
                System.out.println("Content added = " + content1 + " at position: " + position);

                if (i <= bitmaps.size() - 1) {
                    Draw draw1 = new Draw(bitmaps.get(i));
                    items.add(new Item(1, draw1));
                    System.out.println("Draw added = " + draw1 + " at position: " + position);
                    i++;
                    if (i >= strings.length) {
                        Content content = new Content("");
                        items.add(new Item(0, content));
                        System.out.println("Content added = " + content + " at position: " + position);
                    }
                }
            }
        }


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        textanddrawRecyclerView.setLayoutManager(staggeredGridLayoutManager);

        everNoteScreenAdapter = new EverNoteScreenAdapter(items, mEverDataBase);

        textanddrawRecyclerView.setAdapter(everNoteScreenAdapter);

        EverNoteScreenAdapter.setClickListener((EverNoteScreenAdapter.ItemClickListener) mClickListener);
    }
}
