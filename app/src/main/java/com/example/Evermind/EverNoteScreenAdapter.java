package com.example.Evermind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Evermind.recycler_models.Content;
import com.example.Evermind.recycler_models.Draw;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.example.Evermind.recycler_models.Item;
import com.sysdata.kt.htmltextview.SDHtmlTextView;
import java.util.List;

public class EverNoteScreenAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EverLinkedMap> itemList;

    private EverNoteScreenAdapter.ItemClickListener mClickListener;

    private AdapterView.OnItemLongClickListener mLongClick;

    private Context context;

    private int ID;

    public EverNoteScreenAdapter(List<EverLinkedMap> items, Context contexts, int id) {

        itemList = items;

        context = contexts;

        ID = id;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ContentViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.recyclerview_note_screen_textview_layout,
                            parent,
                            false
                    )
            );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ((ContentViewHolder)holder).setContentHTML(itemList.get(position).getContent());
            ((ContentViewHolder)holder).setDrawContent(itemList.get(position).getDrawLocation());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

     class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SDHtmlTextView everTextView;
        private ImageView everImage;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            everTextView = itemView.findViewById(R.id.recycler_everEditor);
            everImage = itemView.findViewById(R.id.recycler_Image);

            everTextView.setPadding(15, 15, 15, 25);

            //everTextView.setTextSize(22);

            everTextView.setOnClickListener(view -> { ((MainActivity) context).onItemClickFromRecyclerAtNotescreen(view, ID); });
            everImage.setOnClickListener(view -> { ((MainActivity) context).onItemClickFromRecyclerAtNotescreen(view, ID); });

           // TODO TO REMOVE -> itemView.setOnClickListener(this);

            everTextView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)
                    mClickListener.onLongPress(view, p);
                System.out.println("XHUPAKI = " + p);
                return false;
            });

            itemView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)
                    mClickListener.onLongPress(view, p);
                System.out.println(p);

                return true;// returning true instead of false, works for me
            });

        }

         void setDrawContent(String draw) {

             Bitmap bitmap = BitmapFactory.decodeFile(draw);

             everImage.setImageBitmap(bitmap);
         }

        void setContentHTML(String contentHTML) {

            if (contentHTML.equals("▓")) {
                everTextView.setVisibility(View.GONE);
            } else {
                everTextView.setHtmlText(contentHTML);
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

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onLongPress(View view, int position);
    }
}
