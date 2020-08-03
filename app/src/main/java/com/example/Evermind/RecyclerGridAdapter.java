package com.example.Evermind;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sysdata.kt.htmltextview.SDHtmlTextView;

import org.htmlcleaner.HtmlSerializer;

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.ViewHolder> {

    private String[] mData;
    private String[] mTitle;
    private String[] mDate;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;

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
    public void onBindViewHolder(ViewHolder holder, int position) {




            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.myTitleView.setText(mTitle[position]);
            }




            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


                holder.myTextView.setHtmlText(mData[position]);

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

        ViewHolder(View itemView) {
            super(itemView);


                myTextView = itemView.findViewById(R.id.info_text);
                myTitleView = itemView.findViewById(R.id.info_title);
                itemView.setOnClickListener(this);

                //TODO IMPORTANT CODE \/ \/ \/ \/ \/

                myTextView.setOnClickListener(this);

                ////TODO/////////////// /\ /\ /\ /\

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int p = getLayoutPosition();

                    System.out.println(p);

                    return true;// returning true instead of false, works for me
                }
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
    }

}
