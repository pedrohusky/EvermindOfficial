package com.example.Evermind;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.Ion;
import com.stfalcon.imageviewer.StfalconImageViewer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import pl.droidsonroids.gif.GifImageView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ImagesRecyclerGridAdapter extends RecyclerView.Adapter<ImagesRecyclerGridAdapter.ViewHolder>  {

    private final ArrayList<String> mImageURLs;
    private final Context context;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final boolean fromNoteScreen;

    // data is passed into the constructor
    public ImagesRecyclerGridAdapter(Context context, ArrayList<String> ImageURLs, boolean fromNoteScreen) {

        this.fromNoteScreen = fromNoteScreen;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mImageURLs = ImageURLs;

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
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        if (fromNoteScreen) {

            if (position == 0 && getItemCount() <= 1) {
                if (mImageURLs.get(position).length() > 1) {
                Ion.with(holder.myImageView)
                        .error(R.drawable.ic_baseline_clear_24)
                        .animateLoad(R.anim.grid_new_item_anim)
                        .animateIn(R.anim.grid_new_item_anim)
                        .smartSize(true)
                        .centerCrop()
                        .load(mImageURLs.get(position)).success(value -> {
                    if (position == getItemCount() - 1) {
                     //   ((MainActivity) context).noteScreen.StartPostpone();
                    }
                }); // was position // was position

                ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

                params.height = 450;
                params.width = MATCH_PARENT;

                holder.myImageView.setLayoutParams(params);

            }

            } else {
               if (mImageURLs.get(position).length() > 1) {
                   Ion.with(holder.myImageView)
                           .error(R.drawable.ic_baseline_clear_24)
                           .animateLoad(R.anim.grid_new_item_anim)
                           .animateIn(R.anim.grid_new_item_anim)
                           .smartSize(true)
                           .centerCrop()
                           .load(mImageURLs.get(position)).success(value -> {
                       if (position == getItemCount() - 1) {
                        //   ((MainActivity) context).noteScreen.StartPostpone();
                       }
                   }); // was position // was position

                   ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

                   params.height = 350;
                   params.width = 350;

                   holder.myImageView.setLayoutParams(params);
               }
            }



        } else {

            if ((position % 2) == 0 && (position + 1) == getItemCount()) {

                if (!mImageURLs.get(position).equals("")) {
                    Ion.with(holder.myImageView)
                            .error(R.drawable.ic_baseline_clear_24)
                            .animateLoad(R.anim.grid_new_item_anim)
                            .animateIn(R.anim.grid_new_item_anim)
                            .load(mImageURLs.get(position)).success(value -> {
                        if (position == getItemCount() - 1) {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteCreator.startPostponeTransition(), 25);
                        }
                    }); // was position

                    ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();


                    params.height = 800;
                    params.width = 1300;


                    holder.myImageView.setLayoutParams(params);
                }
            }

            else {

                if (!mImageURLs.get(position).equals("")) {
                    Ion.with(holder.myImageView)
                            .error(R.drawable.ic_baseline_clear_24)
                            .animateLoad(R.anim.grid_new_item_anim)
                            .animateIn(R.anim.grid_new_item_anim)
                            .smartSize(true)
                            .centerCrop()
                            .load(mImageURLs.get(position)).success(value -> {
                        if (position == getItemCount() - 1) {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteCreator.startPostponeTransition(), 25);
                        }
                    });
                }
            }
        }

        }

    // total number of cells
    @Override
    public int getItemCount() {
        return mImageURLs.size();

    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{

        GifImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);

                myImageView = itemView.findViewById(R.id.recyclerImage);

                //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            myImageView.setOnClickListener(view ->
                    new StfalconImageViewer.Builder<>(context, mImageURLs, (imageView, image) ->
                    Ion.with(imageView)
                    .error(R.drawable.ic_baseline_clear_24)
                    .animateLoad(R.anim.grid_new_item_anim)
                    .animateIn(R.anim.grid_new_item_anim)
                    .load(image)).show(true).setCurrentPosition(getLayoutPosition()));



            itemView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)

                    mClickListener.onImageLongPress(view, p);
                //popupWindow(view, p);
                System.out.println(p);

                return true;// returning true instead of false, works for me
            });
        }
        ///////////////////////

    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onImageLongPress(View view, int position);
    }

    public void updateAdapter(String urls, int position) {
        mImageURLs.clear();
        Collections.addAll(mImageURLs, urls.split("┼"));
        notifyItemRemoved(position);

    }
}
