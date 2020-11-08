package com.example.Evermind;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
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
                    Glide.with(context)
                            .asBitmap()
                            .error(R.drawable.ic_baseline_clear_24)
                            .transition(GenericTransitionOptions.with(R.anim.grid_new_item_anim))
                            .load(path)
                            .into(holder.myImageView);

                    ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

                    params.height = 450;
                    params.width = MATCH_PARENT;

                    holder.myImageView.setLayoutParams(params);

                }

            } else {
                if (path.length() > 1) {
                    Glide.with(context)
                            .asBitmap()
                            .error(R.drawable.ic_baseline_clear_24)
                            .transition(GenericTransitionOptions.with(R.anim.grid_new_item_anim))
                            .load(path)
                            .into(holder.myImageView);

                    ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

                    params.height = 350;
                    params.width = 350;

                    holder.myImageView.setLayoutParams(params);
                }
            }


        } else {

            if ((holder.getAdapterPosition() % 2) == 0 && (holder.getAdapterPosition() + 1) == ((MainActivity)context).noteCreator.get().listImages.size()) {

                if (!path.equals("")) {
                    Glide.with(context)
                            .asBitmap()
                            .error(R.drawable.ic_baseline_clear_24)
                            .transition(GenericTransitionOptions.with(R.anim.grid_new_item_anim))
                            .load(path)
                            .into(holder.myImageView);


                    ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();


                    params.height = 800;
                    params.width = 1300;


                    holder.myImageView.setLayoutParams(params);
                }
            } else {

                if (!path.equals("")) {
                    Glide.with(context)
                            .asBitmap()
                            .error(R.drawable.ic_baseline_clear_24)
                            .transition(GenericTransitionOptions.with(R.anim.grid_new_item_anim))
                            .load(path)
                            .into(holder.myImageView);

                    }
                }
            }
        }



    class ImageModelViewHolder extends ItemViewHolder<String> {

        GifImageView myImageView;

        public ImageModelViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.recyclerImage);

            myImageView.setOnClickListener(view ->
                    new StfalconImageViewer.Builder<>(context, ((MainActivity) context).noteModelSection.get(((MainActivity) context).actualNote.get().getActualPosition()).getImages(), (imageView, image) ->
                            Glide.with(context)
                                    .asBitmap()
                                    .transition(GenericTransitionOptions.with(R.anim.grid_new_item_anim))
                                    .error(R.drawable.ic_baseline_clear_24)
                                    .load(image)
                                    .into(imageView)).show(true).setCurrentPosition(getAdapterPosition()));




        }
    }

}