package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.koushikdutta.ion.Ion;
import com.sysdata.kt.htmltextview.SDHtmlTextView;

import java.io.File;
import java.lang.ref.WeakReference;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class NoteContentsNoteScreenBinder extends ItemBinder<EverLinkedMap, NoteContentsNoteScreenBinder.NoteScreenHolder> {

    private final Context context;
    private WeakReference<CardView> noteScreenCard;
    private WeakReference<SDHtmlTextView> everTextView;
    private WeakReference<ImageView> everImage;
    private final int Size;

    public NoteContentsNoteScreenBinder(Context context, int size) {
        this.context = context;
        Size = size;
    }


    @Override
    public NoteScreenHolder createViewHolder(ViewGroup parent) {
        return new NoteScreenHolder(inflate(parent, R.layout.recyclerview_note_screen_textview_layout));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof EverLinkedMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bindViewHolder(NoteScreenHolder holder, EverLinkedMap everMap) {

        everTextView = new WeakReference<>(holder.itemView.findViewById(R.id.recycler_everEditor));
        everImage = new WeakReference<>(holder.itemView.findViewById(R.id.recycler_Image));
        noteScreenCard = new WeakReference<>(holder.itemView.findViewById(R.id.card_NoteScreen));

        everTextView.get().setPadding(15, 15, 15, 15);

        //if (((MainActivity) context).noteModelSection.get().getNoteColor().equals("000000")) {
        noteScreenCard.get().setElevation(0);
        // }

        holder.setContentHTML(holder.getItem().getContent());
        holder.setDrawContent(holder.getItem().getDrawLocation());

    }

    class NoteScreenHolder extends ItemViewHolder<EverLinkedMap> {

        @SuppressLint("ClickableViewAccessibility")
        public NoteScreenHolder(View itemView) {
            super(itemView);
            everTextView = new WeakReference<>(itemView.findViewById(R.id.recycler_everEditor));
            everImage = new WeakReference<>(itemView.findViewById(R.id.recycler_Image));
            noteScreenCard = new WeakReference<>(itemView.findViewById(R.id.card_NoteScreen));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML(String contentHTML) {

            everTextView = new WeakReference<>(itemView.findViewById(R.id.recycler_everEditor));

            if (!contentHTML.equals("▓")) {
                everTextView.get().setHtmlText(contentHTML);
                everTextView.get().setVisibility(View.VISIBLE);

                if (getAdapterPosition() == Size - 1) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteScreen.startPostponedEnterTransition(), 25);
                }

                if (contentHTML.length() <= 10) {
                    everTextView.get().setTextSize(19);
                }

                if (contentHTML.length() <= 5) {
                    everTextView.get().setTextSize(22);
                }

                if (contentHTML.length() >= 10) {
                    everTextView.get().setTextSize(16);
                }
            } else {
                everTextView.get().setVisibility(View.GONE);
            }
        }


        void setDrawContent(String draw) {

            everImage = new WeakReference<>(itemView.findViewById(R.id.recycler_Image));

            if (new File(draw).exists()) {
                System.out.println("loaded file = " + draw);
                everImage.get().setVisibility(View.VISIBLE);
                Glide.with(context)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .apply(new RequestOptions().override(468, 700))
                        .error(R.drawable.ic_baseline_clear_24)
                        .transition(GenericTransitionOptions.with(R.anim.grid_new_item_anim))
                        .load(draw).into(everImage.get());
            } else {
               everImage.get().setVisibility(View.GONE);
            }
        }
    }
}