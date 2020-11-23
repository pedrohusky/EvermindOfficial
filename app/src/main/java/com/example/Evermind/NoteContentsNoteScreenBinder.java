package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.TESTEDITOR.rteditor.RTTextView;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.lang.ref.WeakReference;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class NoteContentsNoteScreenBinder extends ItemBinder<EverLinkedMap, NoteContentsNoteScreenBinder.NoteScreenHolder> {

    private final Context context;
    private WeakReference<CardView> noteScreenCard;
    private WeakReference<RTTextView> everTextView;
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

        //((MainActivity)context).mRTManager.registerEditor(everTextView.get(), true);

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



                everTextView.get().setRichTextEditing(contentHTML);
                everTextView.get().setVisibility(View.VISIBLE);

                if (getAdapterPosition() == Size - 1) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteScreen.startPostponedEnterTransition(), 50);
                }

                if (contentHTML.length() <= 10) {
                    everTextView.get().setTextSize(22);
                }

                if (contentHTML.length() <= 5) {
                    everTextView.get().setTextSize(25);
                }

                if (contentHTML.length() >= 10) {
                    everTextView.get().setTextSize(18);
                }
            } else {
                everTextView.get().setVisibility(View.GONE);
            }
        }


        void setDrawContent(String draw) {

            everImage = new WeakReference<>(itemView.findViewById(R.id.recycler_Image));

            if (new File(draw).exists()) {
                everImage.get().setVisibility(View.VISIBLE);
                Ion.with(everImage.get())
                        .error(R.drawable.ic_baseline_clear_24)
                        .animateLoad(R.anim.grid_new_item_anim)
                        .animateIn(R.anim.grid_new_item_anim)
                        .smartSize(true)
                        .fitXY()
                        .load(draw);
            } else {
               everImage.get().setVisibility(View.GONE);
            }
        }
    }
}