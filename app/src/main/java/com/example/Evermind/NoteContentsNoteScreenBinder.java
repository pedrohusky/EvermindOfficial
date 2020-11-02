package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.example.Evermind.recycler_models.EverLinkedMap;
import com.koushikdutta.ion.Ion;
import com.sysdata.kt.htmltextview.SDHtmlTextView;

import java.io.File;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class NoteContentsNoteScreenBinder extends ItemBinder<EverLinkedMap, NoteContentsNoteScreenBinder.NoteScreenHolder> {

    private final Context context;
    private CardView noteScreenCard;
    private SDHtmlTextView everTextView;
    private ImageView everImage;
    private int Size;
    private TextView textView;

    public NoteContentsNoteScreenBinder(Context context, int size, TextView title) {
        this.context = context;
        Size = size;
        textView = title;
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

        everTextView.setPadding(15, 15, 15, 15);

        //if (((MainActivity) context).noteModelSection.get().getNoteColor().equals("000000")) {
            noteScreenCard.setElevation(0);
       // }

        holder.setContentHTML(everMap.getContent());
        holder.setDrawContent(everMap.getDrawLocation());

    }

    class NoteScreenHolder extends ItemViewHolder<EverLinkedMap> {

        @SuppressLint("ClickableViewAccessibility")
        public NoteScreenHolder(View itemView) {
            super(itemView);
            everTextView = itemView.findViewById(R.id.recycler_everEditor);
            everImage = itemView.findViewById(R.id.recycler_Image);
            noteScreenCard = itemView.findViewById(R.id.card_NoteScreen);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setContentHTML(String contentHTML) {

            if (!contentHTML.equals("▓")) {
                everTextView.setHtmlText(contentHTML);
                everTextView.setVisibility(View.VISIBLE);

                if (getAdapterPosition() == Size - 1) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).noteScreen.startPostponedEnterTransition(), 25);
                }

                if (contentHTML.length() <= 10) {
                    everTextView.setTextSize(19);
                }

                if (contentHTML.length() <= 5) {
                    everTextView.setTextSize(22);
                }

                if (contentHTML.length() >= 10) {
                    everTextView.setTextSize(16);
                }
            }
        }


        void setDrawContent(String draw) {

            if (!draw.equals("")) {

                if (!draw.equals("┼")) {

                    if (new File(draw).exists()) {
                        everImage.setVisibility(View.VISIBLE);
                        Ion.with(everImage)
                                .error(R.drawable.ic_baseline_clear_24)
                                .animateLoad(R.anim.grid_new_item_anim)
                                .animateIn(R.anim.grid_new_item_anim)
                                .centerCrop()
                                .smartSize(true)
                                .load(draw);
                    }
                }
            }
        }
    }
}