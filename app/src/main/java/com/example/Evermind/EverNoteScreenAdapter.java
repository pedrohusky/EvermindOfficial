package com.example.Evermind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.recycler_models.Content;
import com.example.Evermind.recycler_models.Draw;
import com.example.Evermind.recycler_models.Item;
import com.sysdata.kt.htmltextview.SDHtmlTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class EverNoteScreenAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Item> itemList;

    private static EverNoteScreenAdapter.ItemClickListener mClickListener;

    private AdapterView.OnItemLongClickListener mLongClick;

    public EverNoteScreenAdapter(List<Item> items, EverDataBase dataBase) {

        itemList = items;

        System.out.println("ItemList size = " + items.size());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            return new ContentViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recyclerview_note_screen_textview_layout,
                            parent,
                            false
                    )
            );
        } else {
            return new DrawViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recyclerview_note_screen_image_layout,
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == 0) {

            Content content = (Content) itemList.get(position).getObject();
            ((ContentViewHolder)holder).setContentHTML(content);

        } else {
            Draw draw = (Draw) itemList.get(position).getObject();
            ((DrawViewHolder)holder).setDrawContent(draw);

        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }





    static class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SDHtmlTextView everTextView;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            everTextView = itemView.findViewById(R.id.recycler_everEditor);

            everTextView.setPadding(8, 15, 15, 8);

            //everTextView.setTextSize(22);

            itemView.setOnClickListener(this);

            //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            everTextView.setOnClickListener(this);

            everTextView.setOnLongClickListener(view -> {
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

        void setContentHTML(Content contentHTML) {

                everTextView.setHtmlText(contentHTML.getContent());

            if (contentHTML.getContent().length() <= 10) {
                everTextView.setTextSize(19);
            }

            if (contentHTML.getContent().length() <= 5) {
                everTextView.setTextSize(22);
            }

            if (contentHTML.getContent().length() >= 10) {
                everTextView.setTextSize(12);
            }

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    static class DrawViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView everDraw;

        public DrawViewHolder(@NonNull View itemView) {
            super(itemView);
            everDraw = itemView.findViewById(R.id.recycler_imageView);
            itemView.setOnClickListener(this);

            //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            everDraw.setOnClickListener(this);

            everDraw.setOnLongClickListener(view -> {
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

        void setDrawContent(Draw draw) {

            Bitmap bitmap = BitmapFactory.decodeFile(draw.getFileLocation());

            everDraw.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public static void setClickListener(EverNoteScreenAdapter.ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
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
