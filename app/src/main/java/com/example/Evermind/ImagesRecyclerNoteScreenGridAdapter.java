package com.example.Evermind;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.ui.grid.ui.main.NotesScreen;
import com.koushikdutta.ion.Ion;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.Arrays;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import pl.droidsonroids.gif.GifImageView;

public class ImagesRecyclerNoteScreenGridAdapter extends RecyclerView.Adapter<ImagesRecyclerNoteScreenGridAdapter.ViewHolder>  {

    private String[] mImageURLs;
    private Integer mID;
    private int count;
    private String[] SplittedURLs;
    private int realID;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;
    private Context context;
    private PopupWindowHelper popupWindowHelper;

    // data is passed into the constructor
    public ImagesRecyclerNoteScreenGridAdapter(Context context, String ImageURLs, Integer ID, int countURLs) {


        SplittedURLs = ImageURLs.replaceAll("[\\[\\](){}]","").trim().split("┼");

            this.mInflater = LayoutInflater.from(context);
            this.mImageURLs = SplittedURLs;
            this.mID = ID;
            this.count = countURLs;
            this.context = context;
    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.images_imagerecycler_notescreen, parent, false);


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

            params.height = 600;
            params.width = 600;

            holder.myImageView.setLayoutParams(params);
        }

        Ion.with(holder.myImageView)
                .error(R.drawable.ic_baseline_clear_24)
                .animateLoad(R.anim.grid_new_item_anim)
                .animateIn(R.anim.grid_new_item_anim)
                .smartSize(true)
                .load(mImageURLs[position]); // was position



        System.out.println("NOTE SCREEN POSITION WICH LOADED = " + position +  "   LAST STEP LOADED = " + mImageURLs[position]);

        }





    // total number of cells
    @Override
    public int getItemCount() {
        return count;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

            myImageView.setOnClickListener(view -> {

                ((MainActivity)context).OpenNoteFromImage(view, mID);
            });

           // myImageView.setOnClickListener(this);

            myImageView.setOnLongClickListener(view -> {
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
