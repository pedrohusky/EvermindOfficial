package com.example.Evermind;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
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
import java.util.LinkedHashMap;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class RecyclerGridAdapterNoteScreen extends RecyclerView.Adapter<RecyclerGridAdapterNoteScreen.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;
    private RecyclerView textanddrawRecyclerView;
    private TextView myTitleView;
    private LinearLayout myLinearLayout;
    private RecyclerView myRecyclerView;
    private CardView myCardView;
    private ArrayList<Note_Model> models = new ArrayList<>();

    public RecyclerGridAdapterNoteScreen(Context contexts, ArrayList<Note_Model> noteModels) {

        context = contexts;
        this.models = noteModels;
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

        ViewCompat.setTransitionName(textanddrawRecyclerView, "textRecycler"+position);
        ViewCompat.setTransitionName(myCardView, "card"+position);
        ViewCompat.setTransitionName(myRecyclerView, "imageRecycler"+position);
        ViewCompat.setTransitionName(myTitleView, "title"+position);

        myTitleView.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(models.get(position).getNoteColor())));

        SetupNoteEditorRecycler(position);

        myTitleView.setText(models.get(position).getTitle());

      //  textanddrawRecyclerView.setBackgroundColor(Integer.parseInt(models.get(position).getNoteColor()));

        String imagesURLs = models.get(position).getImageURLS();



        if (imagesURLs.length() > 0) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);

            myRecyclerView.setLayoutManager(staggeredGridLayoutManager);

          //  ImagesRecyclerNoteScreenGridAdapter adapter = new ImagesRecyclerNoteScreenGridAdapter(context, imagesURLs, position, imagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length);
            myRecyclerView.setAdapter(new ImagesRecyclerNoteScreenGridAdapter(context, imagesURLs, position, imagesURLs.replaceAll("[\\[\\](){}]", "").split("┼").length));

            OverScrollDecoratorHelper.setUpOverScroll(myRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        }


        if (myTitleView.length() < 1) {
            ViewGroup.LayoutParams params = myTitleView.getLayoutParams();

            params.height = 65;

            myTitleView.setLayoutParams(params);


        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return models.size();

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ViewHolder(View itemView) {
            super(itemView);

            myTitleView = itemView.findViewById(R.id.info_title);

            myCardView = itemView.findViewById(R.id.mainCard);

            myRecyclerView = itemView.findViewById(R.id.RecyclerNoteScren);

            textanddrawRecyclerView = itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
               // Toast.makeText(context, "adapter position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                mClickListener.onItemClick(textanddrawRecyclerView, myCardView, textanddrawRecyclerView, getAdapterPosition());
        }

    }

    public void setClickListener(ItemClickListener itemClickListener) {

        this.mClickListener = itemClickListener;

    }

    public interface ItemClickListener {

        void onItemClick(RecyclerView view, CardView view2, View view4, int position);

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

        String[] html = models.get(position).getDrawLocation().split("┼");

        List<String> bitmaps = new ArrayList<>(Arrays.asList(html));

        String[] strings = models.get(position).getContent().split("┼");

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

            EverNoteScreenAdapter adapter = new EverNoteScreenAdapter(items, context, models.get(position).getId(), position, textanddrawRecyclerView, myCardView, myRecyclerView, myTitleView);

           //
            textanddrawRecyclerView.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
            textanddrawRecyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));

    }
    public void update(int position, ArrayList<Note_Model> nmodels) {
        models = nmodels;
        notifyItemChanged(position, models.get(position).getNoteColor());
    }
}
