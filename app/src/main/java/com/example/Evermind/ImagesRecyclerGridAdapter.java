package com.example.Evermind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.koushikdutta.ion.Ion;


import pl.droidsonroids.gif.GifImageView;

public class ImagesRecyclerGridAdapter extends RecyclerView.Adapter<ImagesRecyclerGridAdapter.ViewHolder>  {

    private String[] mImageURLs;
    private Integer mID;
    private int count;
    private String[] SplittedURLs;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;

    // data is passed into the constructor
    public ImagesRecyclerGridAdapter(Context context, String ImageURLs, Integer ID, int countURLs) {


        SplittedURLs = ImageURLs.replaceAll("[\\[\\](){}]","").split("┼");

            this.mInflater = LayoutInflater.from(context);
            this.mImageURLs = SplittedURLs;
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

        if (position == 0 && getItemCount() <= 1) {
            Ion.with(holder.myImageView)
                    .error(R.drawable.ic_baseline_clear_24)
                    .animateLoad(R.anim.grid_new_item_anim)
                    .animateIn(R.anim.grid_new_item_anim)
                    .load(mImageURLs[position]); // was position

            ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

            params.height = 1300;
            params.width = 1300;

            holder.myImageView.setLayoutParams(params);

        }


           Ion.with(holder.myImageView)
                   .error(R.drawable.ic_baseline_clear_24)
                   .animateLoad(R.anim.grid_new_item_anim)
                   .animateIn(R.anim.grid_new_item_anim)
                   .smartSize(true)
                   .load(mImageURLs[position]);


        System.out.println("POSITION WICH LOADED = " + position +  "   LAST STEP LOADED = " + mImageURLs[position]);

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
