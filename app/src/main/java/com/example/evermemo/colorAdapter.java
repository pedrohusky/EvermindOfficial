package com.example.evermemo;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.evermemo.everUtils.EverImageDiffUtil;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class colorAdapter extends RecyclerView.Adapter<colorAdapter.ViewHolder> {

    private final List<Integer> colors = new ArrayList<>();

    public colorAdapter(List<Integer> colors) {
        this.colors.addAll(colors);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.color_recycler, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.color = colors.get(position);
    }

    public void addColor(int color) {
        int p = getItemCount();
        colors.add(color);
        notifyItemInserted(p);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return colors.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myImageView;
        private int color;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            myImageView = itemView.findViewById(R.id.recyclerImage);

            myImageView.setImageTintList(ColorStateList.valueOf(color));
        }
    }
}

