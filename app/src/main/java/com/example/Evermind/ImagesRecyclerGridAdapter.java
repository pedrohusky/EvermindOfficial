package com.example.Evermind;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.koushikdutta.ion.Ion;
import com.stfalcon.frescoimageviewer.ImageViewer;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import cn.xm.weidongjian.popuphelper.PopupWindowHelper;
import pl.droidsonroids.gif.GifImageView;

public class ImagesRecyclerGridAdapter extends RecyclerView.Adapter<ImagesRecyclerGridAdapter.ViewHolder>  {

    private String[] mImageURLs;
    private Integer mID;
    private int count;
    private String[] SplittedURLs;
    private Context context;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;

    public static ArrayList<Uri> uris = new ArrayList<>();
    public static Uri[] uri;

    // data is passed into the constructor
    public ImagesRecyclerGridAdapter(Context context, String ImageURLs, Integer ID, int countURLs) {

        new Thread(() -> {

        SplittedURLs = ImageURLs.replaceAll("[\\[\\](){}]","").split("┼");

        this.context = context;

            this.mInflater = LayoutInflater.from(context);
            this.mImageURLs = SplittedURLs;
            this.mID = ID;
            this.count = countURLs;

    }).start();
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

        if ((position % 2) == 0 && (position + 1) == getItemCount()) {

            if (!mImageURLs[position].equals("")) {
                Ion.with(holder.myImageView)
                        .error(R.drawable.ic_baseline_clear_24)
                        .animateLoad(R.anim.grid_new_item_anim)
                        .animateIn(R.anim.grid_new_item_anim)
                        .load(mImageURLs[position]); // was position

                ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

                params.height = 800;
                params.width = 1300;

                holder.myImageView.setLayoutParams(params);
            }
        }

        else {

            if (!mImageURLs[position].equals("")) {
                Ion.with(holder.myImageView)
                        .error(R.drawable.ic_baseline_clear_24)
                        .animateLoad(R.anim.grid_new_item_anim)
                        .animateIn(R.anim.grid_new_item_anim)
                        .smartSize(true)
                        .centerCrop()
                        .load(mImageURLs[position]); // was position
            }
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

        ViewHolder(View itemView) {
            super(itemView);



                myImageView = itemView.findViewById(R.id.recyclerImage);
                itemView.setOnClickListener(this);

                //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            myImageView.setOnClickListener(view -> {

                        new Thread(() -> {

                            for (String item : mImageURLs) {

                                File file = new File(item);
                                uris.add(Uri.fromFile(file));

                            }

                            uri = uris.toArray(new Uri[0]);

                            new Handler(Looper.getMainLooper()).post(() -> {

                                new ImageViewer.Builder(view.getContext(), uri)
                                        .setStartPosition(getLayoutPosition())
                                        .show();
                            });

                            uris.clear();

                        }).start();
                    });



                ////TODO/////////////// /\ /\ /\ /\

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public boolean onLongClick(View view) {
                    int p = getLayoutPosition();

                    if (mClickListener != null)

                        mClickListener.onImageLongPress(view, p);
                    //popupWindow(view, p);
                    System.out.println(p);

                    return true;// returning true instead of false, works for me
                }
            });
        }
        ///////////////////////

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onImageClick(view, getAdapterPosition());
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
        void onImageClick(View view, int position);

        void onImageLongPress(View view, int position);
    }
    public void updateAdapter(String urls, int position) {
        SplittedURLs = urls.split("┼");
        mImageURLs = SplittedURLs;
        count = SplittedURLs.length;
        System.out.println("URLS IS =" + urls + "mImage is = " + Arrays.toString(mImageURLs));

            notifyItemRemoved(position);

            if (SplittedURLs.length < 1) {

            }

         //   if (position == 0) {
          //      notifyDataSetChanged();
          //  }

    }
}
