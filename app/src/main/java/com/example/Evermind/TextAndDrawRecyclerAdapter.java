package com.example.Evermind;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sysdata.kt.htmltextview.SDHtmlTextView;

public class TextAndDrawRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private String[] mData;
    private Integer[] mIds;
    public static String[] title;
    public static Integer[] id;
    public  Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;
    private EverDataBase everDataBase;

    // data is passed into the constructor
    public TextAndDrawRecyclerAdapter(Context context, String[] data, Integer[] ids, EverDataBase dataBase) {

        this.everDataBase = dataBase;

        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.mIds = ids;

    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_content_visualization, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    // binds the data to the TextView in each cell

    @Override
    public int getItemViewType(int position) {
     //   if (TextUtils.isEmpty(employees.get(position).getEmail())) {
      //      return TYPE_CALL;

      //  } else {
            return 1;
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
        ImageView myImageView;
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
