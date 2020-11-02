package com.example.Evermind;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.stfalcon.imageviewer.StfalconImageViewer;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;
import pl.droidsonroids.gif.GifImageView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ImagesBinder extends ItemBinder<String, ImagesBinder.ImageModelViewHolder> {

    private final Context context;
    private final boolean fromNoteScreen;
    private final int size;

    public ImagesBinder(Context context, int size, boolean fromNoteScreen) {
        this.fromNoteScreen = fromNoteScreen;
        this.context = context;
        this.size = size;
    }


    @Override
    public ImageModelViewHolder createViewHolder(ViewGroup parent) {
        return new ImageModelViewHolder(inflate(parent, R.layout.images_imagerecycler));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof String;
    }

    @Override
    public void bindViewHolder(ImageModelViewHolder holder, String path) {
        if (fromNoteScreen) {

            if (holder.getAdapterPosition() == 0 && size <= 1) {
                if (path.length() > 1) {
                    Ion.with(holder.myImageView)
                            .error(R.drawable.ic_baseline_clear_24)
                            .animateLoad(R.anim.grid_new_item_anim)
                            .animateIn(R.anim.grid_new_item_anim)
                            .smartSize(true)
                            .centerCrop()
                            .load(path).success(value -> {
                        //   if (position == getItemCount() - 1) {
                        //   ((MainActivity) context).noteScreen.StartPostpone();
                        //  }
                    }); // was position // was position

                    ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

                    params.height = 450;
                    params.width = MATCH_PARENT;

                    holder.myImageView.setLayoutParams(params);

                }

            } else {
                if (path.length() > 1) {
                    Ion.with(holder.myImageView)
                            .error(R.drawable.ic_baseline_clear_24)
                            .animateLoad(R.anim.grid_new_item_anim)
                            .animateIn(R.anim.grid_new_item_anim)
                            .smartSize(true)
                            .centerCrop()
                            .load(path).success(value -> {
                         if (holder.getLayoutPosition() == size - 1) {
                           ((MainActivity) context).noteScreen.StartPostpone();
                         }
                    });

                    ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

                    params.height = 350;
                    params.width = 350;

                    holder.myImageView.setLayoutParams(params);
                }
            }


        } else {

            if ((holder.getAdapterPosition() % 2) == 0 && (holder.getAdapterPosition() + 1) == size) {

                if (!path.equals("")) {
                    Ion.with(holder.myImageView)
                            .error(R.drawable.ic_baseline_clear_24)
                            .animateLoad(R.anim.grid_new_item_anim)
                            .animateIn(R.anim.grid_new_item_anim)
                            .load(path).success(value -> {
                           if (holder.getLayoutPosition() == size - 1) {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteCreator.startPostponeTransition(), 25);
                          }
                    }); // was position

                    ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();


                    params.height = 800;
                    params.width = 1300;


                    holder.myImageView.setLayoutParams(params);
                }
            } else {

                if (!path.equals("")) {
                    Ion.with(holder.myImageView)
                            .error(R.drawable.ic_baseline_clear_24)
                            .animateLoad(R.anim.grid_new_item_anim)
                            .animateIn(R.anim.grid_new_item_anim)
                            .smartSize(true)
                            .centerCrop()
                            .load(path).success(value -> {
                         if (holder.getLayoutPosition() == size - 1) {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteCreator.startPostponeTransition(), 25);
                         }
                    });
                }
            }
        }

    }

    class ImageModelViewHolder extends ItemViewHolder<String> {

        GifImageView myImageView;

        public ImageModelViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.recyclerImage);

            //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            myImageView.setOnClickListener(view ->
                    new StfalconImageViewer.Builder<>(context, ((MainActivity) context).noteModelSection.get(getAdapterPosition()).getImages(), (imageView, image) ->
                            Ion.with(imageView)
                                    .error(R.drawable.ic_baseline_clear_24)
                                    .animateLoad(R.anim.grid_new_item_anim)
                                    .animateIn(R.anim.grid_new_item_anim)
                                    .load(image)).show(true).setCurrentPosition(getLayoutPosition()));



        }
        ///////////////////////
    }

}