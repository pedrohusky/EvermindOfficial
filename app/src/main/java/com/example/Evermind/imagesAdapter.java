package com.example.Evermind;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.example.Evermind.databinding.MainViewNotescreenRecyclerBinding;
import com.example.Evermind.everUtils.EverImageDiffUtil;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class imagesAdapter extends RecyclerView.Adapter<imagesAdapter.ViewHolder> {

    private final List<String> images = new ArrayList<>();

    private final boolean fromNoteScreen;

    public imagesAdapter(List<String> images, boolean fromNoteScreen) {
       this.fromNoteScreen = fromNoteScreen;
        this.images.addAll(images);
    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private String imagePath;
        ImageView myImageView;
        Note_Model note;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            myImageView = itemView.findViewById(R.id.recyclerImage);

            if (!fromNoteScreen) {
                myImageView.setOnLongClickListener(v -> {
                    ((MainActivity) view.getContext()).getNoteCreator().onImageLongPress(v, getAdapterPosition());
                    return false;
                });
                PushDownAnim.setPushDownAnimTo(myImageView);
            }
            myImageView.setOnClickListener(v -> new Handler(Looper.getMainLooper()).post(() -> {
                if (note == null) {
                    for (Note_Model n : ((MainActivity)v.getContext()).getEverNoteManagement().getNoteModelSection()) {
                        if (n.getImages().contains(imagePath)) {
                            note = n;
                        }
                    }
                }
                new StfalconImageViewer.Builder<>(view.getContext(), note.getImages(), (imageView, image) ->
                        Glide.with(view.getContext())
                                .asBitmap()
                                .transition(GenericTransitionOptions.with(R.anim.grid_new_item_anim))
                                .error(R.drawable.ic_baseline_clear_24)
                                .load(image)
                                .into(imageView)).show(true).setCurrentPosition(getAdapterPosition());
            }));
        }
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.images_imagerecycler, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.imagePath = images.get(position);

        if (fromNoteScreen) {
            if (viewHolder.getAdapterPosition() == 0 && getItemCount() <= 1) {
                if (viewHolder.imagePath.length() > 1) {


                    ViewGroup.LayoutParams params = viewHolder.myImageView.getLayoutParams();

                    params.height = 450;
                    params.width = MATCH_PARENT;

                    Glide.with(viewHolder.myImageView).asBitmap().load(viewHolder.imagePath).centerCrop().into(viewHolder.myImageView);



                }

            } else {
                if (viewHolder.imagePath.length() > 1) {

                    Glide.with(viewHolder.myImageView).asBitmap().load(viewHolder.imagePath).centerCrop().into(viewHolder.myImageView);




                }
            }


        } else {



            ViewGroup.LayoutParams params = viewHolder.myImageView.getLayoutParams();

            params.height = MATCH_PARENT;
           params.width = 1052 / (((MainActivity)viewHolder.itemView.getContext()).getActualNote().getImages().size());



            Glide.with(viewHolder.myImageView).asBitmap().load(viewHolder.imagePath).into(viewHolder.myImageView);





        }
    }

    public void updateImages(List<String> images) {
        final EverImageDiffUtil diffCallback = new EverImageDiffUtil(this.images, images);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.images.clear();
        this.images.addAll(images);
        diffResult.dispatchUpdatesTo(this);
    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return images.size();
    }
}

