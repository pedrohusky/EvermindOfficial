package com.example.Evermind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Evermind.recycler_models.Content;
import com.example.Evermind.recycler_models.Draw;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.example.Evermind.recycler_models.Item;
import com.koushikdutta.ion.Ion;
import com.sysdata.kt.htmltextview.SDHtmlTextView;

import java.io.File;
import java.util.List;

public class EverNoteScreenAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EverLinkedMap> itemList;

    private EverNoteScreenAdapter.ItemClickListener mClickListener;

    private AdapterView.OnItemLongClickListener mLongClick;

    private RecyclerView recyclerView;

    private RecyclerView imageRecyclerView;

    private CardView cardView;

    private TextView textView;

    private SDHtmlTextView everTextView;
    private ImageView everImage;

    private Context context;

    private int ID;
    private int actualPosition;

    public EverNoteScreenAdapter(List<EverLinkedMap> items, Context contexts, int id, int position, RecyclerView recyclerView, CardView view, RecyclerView recyclerView2, TextView title) {

        itemList = items;

        context = contexts;

        ID = id;

        this.actualPosition = position;

        this.recyclerView = recyclerView;

        cardView = view;

        imageRecyclerView = recyclerView2;

        textView = title;





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

        everTextView.setPadding(15, 15, 15, 15);

        everTextView.setTransitionName("htmltext"+position);

            ((ContentViewHolder)holder).setContentHTML(itemList.get(position).getContent());
            ((ContentViewHolder)holder).setDrawContent(itemList.get(position).getDrawLocation());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

     class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            everTextView = itemView.findViewById(R.id.recycler_everEditor);
            everImage = itemView.findViewById(R.id.recycler_Image);

         //   everTextView.setBackgroundColor(Integer.parseInt(((MainActivity)context).notesModels.get(actualPosition).getNoteColor()));
            recyclerView.setOnClickListener(view -> { ((MainActivity) context).onItemClickFromRecyclerAtNotescreen(recyclerView, cardView, textView, imageRecyclerView, everTextView,  actualPosition, ID); });
            everTextView.setOnClickListener(view -> { recyclerView.callOnClick(); });
            textView.setOnClickListener(view -> { recyclerView.callOnClick(); });
            everImage.setOnClickListener(view -> { recyclerView.callOnClick(); });

            everImage.setOnLongClickListener(view -> { ((MainActivity) context).OpenCustomizationPopup(textView, ID, actualPosition); return false; });
            everTextView.setOnLongClickListener(view -> { ((MainActivity) context).OpenCustomizationPopup(textView, ID, actualPosition); return false; });
            textView.setOnLongClickListener(view -> { ((MainActivity) context).OpenCustomizationPopup(textView, ID, actualPosition); return false; });

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
                                .load(draw); // was position
                        //  everImage.setImageBitmap(bitmap);
                    }
                }
            }
         }

        void setContentHTML(String contentHTML) {

            if (!contentHTML.equals("▓")) {
                everTextView.setHtmlText(contentHTML);
                everTextView.setVisibility(View.VISIBLE);

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

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onLongPress(View view, View view2, int position);
    }
}
