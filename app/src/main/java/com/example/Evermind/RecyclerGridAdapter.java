package com.example.Evermind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.ViewHolder> {

    private String[] mData;
    private String[] mTitle;
    private String[] mDate;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerGridAdapter(Context context, String[] data, String[] title, String[] date) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mTitle = title;
        this.mDate = date;
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
    public void onBindViewHolder(RecyclerGridAdapter.ViewHolder holder, int position) {


        holder.myTextView.setText(mData[position]);
        holder.myTitleView.setText(mTitle[position]);

        if (holder.myTitleView.length() > 1) {
            holder.myTitleView.setTextSize(17);
            holder.myTitleView.setTranslationY(-10);
            holder.myTextView.setTextSize(16);
            holder.myTitleView.setPadding(15, 10, 15, 22);
            holder.myTextView.setPadding(13, 20, 13, 20);
        }


        if (holder.myTitleView.length() < 1) {

            holder.myTextView.setTextSize(25);
            holder.myTextView.setPadding(15, 5, 15, 50);
            holder.myTitleView.setPadding(0, 0, 0, 0);
            holder.myTitleView.setTranslationY(5);

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
        TextView myTextView;
        TextView myTitleView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.info_text);
            myTitleView = itemView.findViewById(R.id.info_title);
            itemView.setOnClickListener(this);
        }

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

    void setOnLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onClick(View view);
    }

   //public RecyclerGridAdapter(GetOnClickListenerInterface listener){
       // this.mClickListener = (ItemClickListener) listener;
  // }
}
