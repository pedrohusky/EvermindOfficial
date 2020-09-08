package com.example.Evermind;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.recycler_models.Content;
import com.example.Evermind.recycler_models.Draw;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.example.Evermind.recycler_models.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class RecyclerGridAdapterNoteScreen extends RecyclerView.Adapter<RecyclerGridAdapterNoteScreen.ViewHolder> {

    private String[] mData;
    private String[] mTitle;
    private String[] mDate;
    private Integer[] mIds;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;
    private RecyclerView textanddrawRecyclerView;

    public RecyclerGridAdapterNoteScreen(Context contexts, String[] data, String[] title, String[] date, Integer[] ids) {

        context = contexts;
        this.mData = data;
        this.mTitle = title;
        this.mDate = date;
        this.mIds = ids;
       this.mInflater = LayoutInflater.from(context);

    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_content_with_recyclerview_visualization, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        holder.myTitleView.setText(mTitle[position]);

        SetupNoteEditorRecycler(position);

        String imagesURLs = ((MainActivity) context).mDatabaseEver.getImageURLFromDatabaseWithID(mIds[position]);

        if (imagesURLs.length() > 0) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);

            holder.myRecyclerView.setLayoutManager(staggeredGridLayoutManager);

            ImagesRecyclerNoteScreenGridAdapter adapter = new ImagesRecyclerNoteScreenGridAdapter(context, imagesURLs, position, imagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);
            holder.myRecyclerView.setAdapter(adapter);

            OverScrollDecoratorHelper.setUpOverScroll(holder.myRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        }


        if (holder.myTitleView.length() < 1) {
            ViewGroup.LayoutParams params = holder.myTitleView.getLayoutParams();

            params.height = 65;

            holder.myTitleView.setLayoutParams(params);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.myTitleView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c371f9")));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.myTitleView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCCF4")));
            }
        }

    }


    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView myTitleView;
        RecyclerView myRecyclerView;


        ViewHolder(View itemView) {
            super(itemView);

            myTitleView = itemView.findViewById(R.id.info_title);

            myRecyclerView = itemView.findViewById(R.id.RecyclerNoteScren);

            textanddrawRecyclerView = itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler);

            itemView.setOnClickListener(this);

            myRecyclerView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)
                    mClickListener.onLongPress(view, p);
                System.out.println(p);
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

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public void setClickListener(ItemClickListener itemClickListener) {

        this.mClickListener = itemClickListener;

    }

    public interface ItemClickListener {

        void onItemClick(View view, int position);

        void onLongPress(View view, int position);
    }

    private String IfContentIsBiggerReturnNothing(int content, int bitmap, List<String> string, Integer i) {
        if (content > bitmap && i == content - 1) {
            return "";
        }
        return string.get(i);
    }

    private void SetupNoteEditorRecycler(int position) {

        List<EverLinkedMap> items = new ArrayList<>();
        int i = 0;

        String[] html = ((MainActivity) context).mDatabaseEver.getBackgroundFromDatabaseWithID(mIds[position]).split("┼");

        List<String> bitmaps = new ArrayList<>(Arrays.asList(html));

        String[] strings = ((MainActivity) context).mDatabaseEver.getContentsFromDatabaseWithID(mIds[position]).split("┼");

        List<String> contentsSplitted = new ArrayList<>(Arrays.asList(strings));

        System.out.println(contentsSplitted.toString());

        if (contentsSplitted.size() != bitmaps.size()) {
            for (i = contentsSplitted.size(); i < bitmaps.size(); i++) {
                contentsSplitted.add(contentsSplitted.size(), "▓");
                System.out.println("added = " + i + " times.");
            }
        }
        if (bitmaps.size() != contentsSplitted.size()) {
            for (i = bitmaps.size(); i < contentsSplitted.size(); i++) {
                bitmaps.add(bitmaps.size(), "");
                System.out.println("added bit = " + i + " times.");
            }
        }

        int size = bitmaps.size() - 1;

        for (i = 0; i <= size ; i++) {
            items.add(new EverLinkedMap(contentsSplitted.get(i), IfContentIsBiggerReturnNothing(contentsSplitted.size(), bitmaps.size(), bitmaps, i)));
        }

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

            textanddrawRecyclerView.setLayoutManager(staggeredGridLayoutManager);

            textanddrawRecyclerView.setAdapter( new EverNoteScreenAdapter(items, context,mIds[position]));

    }
}
