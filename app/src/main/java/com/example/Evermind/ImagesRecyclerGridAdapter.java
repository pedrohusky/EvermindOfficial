package com.example.Evermind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.koushikdutta.ion.Ion;

import pl.droidsonroids.gif.GifImageView;

public class ImagesRecyclerGridAdapter extends RecyclerView.Adapter<ImagesRecyclerGridAdapter.ViewHolder>  {

    private String[] mImageURLs;
    private Integer mID;
    private int count;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;

    // data is passed into the constructor
    public ImagesRecyclerGridAdapter(Context context, String[] ImageURLs, Integer ID, int countURLs) {


        this.mInflater = LayoutInflater.from(context);
        this.mImageURLs = ImageURLs;
        this.mID = ID;
        this.count = countURLs;
    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.images_imagerecycler, parent, false);


        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       // if (position == 0 && count == 0) {
       //     StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
      //       layoutParams.setFullSpan(true);
      //  }

        if (!mImageURLs[position].equals("")) {
                System.out.println(holder.myImageView.toString());
                Ion.with(holder.myImageView)
                        .error(R.drawable.ic_baseline_clear_24)
                        .animateLoad(R.anim.translate_up_anim)
                        .animateIn(R.anim.fade_in_formatter)
                        .smartSize(true)
                        .load(mImageURLs[position]);
            }
        }



    // total number of cells
    @Override
    public int getItemCount() {
        return count;

    }


    public void setClickListener(AdapterView.OnItemClickListener onItemClickListener) {
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        GifImageView myImageView;
        LinearLayout myLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);



                myImageView = itemView.findViewById(R.id.recyclerImage);
                itemView.setOnClickListener(this);

                //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            myImageView.setOnClickListener(this);

            myImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int p = getLayoutPosition();

                        if (mClickListener != null)
                            mClickListener.onLongPress(view, p);
                        System.out.println(p);
                        return false;
                    }
                });

                ////TODO/////////////// /\ /\ /\ /\

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int p = getLayoutPosition();

                    if (mClickListener != null)
                        mClickListener.onLongPress(view, p);
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
