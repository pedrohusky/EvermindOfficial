package com.example.Evermind;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sysdata.kt.htmltextview.SDHtmlTextView;

import java.util.ArrayList;

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.ViewHolder>  {

    private String[] mData;
    private String[] mTitle;
    private String[] mDate;
    private Integer[] mIds;
    public static String[] title;
    public static Integer[] id;
    public  Context context;
    private String[] SplittedURLs;
    private String[] urls;

    private RecyclerView mRecyclerView;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;
    private EverDataBase mEverDataBase;
    private ImagesRecyclerNoteScreenGridAdapter adapter;

    // data is passed into the constructor
    public RecyclerGridAdapter(Context context, String[] data, String[] title, String[] date, Integer[] ids, String[] ImageURL) {



        mEverDataBase = new EverDataBase(context);

        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mTitle = title;
        this.mDate = date;
        this.context = context;
        this.mIds = ids;
        this.urls = ImageURL;

    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_content_visualization, parent, false);


        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String imagesURLs = mEverDataBase.getImageURLFromDatabaseWithID(mIds[position]);

        if (!mEverDataBase.getBackgroundFromDatabaseWithID(mIds[position]).equals("┼")) {
            Bitmap bitmap = BitmapFactory.decodeFile(mEverDataBase.getBackgroundFromDatabaseWithID(mIds[position]));

            if (bitmap != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

                holder.myTextView.setBackground(bitmapDrawable);
                holder.myTextView.setGravity(Gravity.START);
                holder.myTextView.setPadding(4, 4 ,4 ,4);
            }
        }

        if (imagesURLs.length() > 0) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);

            holder.myRecyclerView.setLayoutManager(staggeredGridLayoutManager);

            adapter = new ImagesRecyclerNoteScreenGridAdapter(context, imagesURLs, position, imagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);
            holder.myRecyclerView.setAdapter(adapter);



        }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.myTitleView.setText(mTitle[position]);
            }

            if (holder.myTitleView.length() < 1) {
                ViewGroup.LayoutParams params = holder.myTitleView.getLayoutParams();

                params.height = 65;

                holder.myTitleView.setLayoutParams(params);
                holder.myTextView.setTextSize(18);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.myTitleView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c371f9")));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.myTitleView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCCF4")));
                }
            }




            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


                holder.myTextView.setHtmlText(mData[position]);

            }

            if (holder.myTextView.length() < 1) {

                ViewGroup.LayoutParams params = holder.myTextView.getLayoutParams();

                params.height = 0;

                holder.myTextView.setLayoutParams(params);

                ViewGroup.LayoutParams params2 = holder.myTextView.getLayoutParams();

                params2.height = 0;

                holder.myTextView.setLayoutParams(params2);
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

        SDHtmlTextView myTextView;
        TextView myTitleView;
        RecyclerView myRecyclerView;
        Activity mActivity;
        LinearLayout myLinearLayout;



        ViewHolder(View itemView) {
            super(itemView);


                myTextView = itemView.findViewById(R.id.info_text);
                myTitleView = itemView.findViewById(R.id.info_title);
                myRecyclerView = itemView.findViewById(R.id.RecyclerNoteScren);
                itemView.setOnClickListener(this);

                //TODO IMPORTANT CODE \/ \/ \/ \/ \/

                myTextView.setOnClickListener(this);

                myTextView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)
                    mClickListener.onLongPress(view, p);
                System.out.println(p);
                return false;
            });

                myRecyclerView.setOnClickListener(this);

            myRecyclerView.setOnLongClickListener(view -> {
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

}
