package com.example.Evermind;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.Evermind.recycler_models.EverAdapter;
import com.example.Evermind.recycler_models.EverLinkedMap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class RecyclerGridAdapterNoteScreen extends RecyclerSwipeAdapter<RecyclerGridAdapterNoteScreen.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private AdapterView.OnItemLongClickListener mLongClick;
    private RecyclerView textRecycler;
    private TextView myTitleView;
    private RecyclerView myRecyclerView;
    private CardView myCardView;
    private ArrayList<Note_Model> models;
    private LinearLayout linearLayout;
    private SwipeLayout swipe;
    private ImageButton delete;
    private ImageButton changeColor;

    public RecyclerGridAdapterNoteScreen(Context contexts, ArrayList<Note_Model> noteModels) {

        context = contexts;
        models = noteModels;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.note_content_with_recyclerview_visualizationtest, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        int ID = models.get(position).getId();

        myTitleView =holder.itemView.findViewById(R.id.info_title);
        myCardView = holder.itemView.findViewById(R.id.mainCard);
        myRecyclerView = holder.itemView.findViewById(R.id.RecyclerNoteScren);
        textRecycler = holder.itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler);

        if (position == getItemCount()-1) {
            //((MainActivity)context).noteScreen.StartPostpone();
        }

        ViewCompat.setTransitionName(textRecycler, "textRecycler"+ID);
        ViewCompat.setTransitionName(myCardView, "card"+ID);
        ViewCompat.setTransitionName(myRecyclerView, "imageRecycler"+ID);
        ViewCompat.setTransitionName(myTitleView, "title"+ID);

        if (!models.get(position).getNoteColor().equals("000000")) {
            myCardView.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(models.get(position).getNoteColor())));
        }

        SetupNoteEditorRecycler(position);

        myTitleView.setText(models.get(position).getTitle());

        if (models.get(position).getTitle().length() < 1) {
            TransitionManager.beginDelayedTransition(myCardView, new TransitionSet()
                    .addTransition(new ChangeBounds()));
           myTitleView.setVisibility(View.GONE);

        }

        if (models.get(position).getImages().size() > 0) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);

            myRecyclerView.setLayoutManager(staggeredGridLayoutManager);

           // myRecyclerView.setAdapter(new ImagesRecyclerGridAdapter(context, models.get(position).getImages(), true));

            OverScrollDecoratorHelper.setUpOverScroll(myRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        } else {
            myRecyclerView.setVisibility(View.GONE);
        }

        //TODO TRY TO MAKE SWIPE FROM TOP <3
//set show mode.
        swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipe.setClickToClose(true);

      //  swipe.addDrag(SwipeLayout.DragEdge.Bottom, holder.itemView.findViewById(R.id.bkg));


//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)


        swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

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


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.testSwipe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View itemView) {
            super(itemView);

            myTitleView = itemView.findViewById(R.id.info_title);
            myCardView = itemView.findViewById(R.id.mainCard);
            myRecyclerView = itemView.findViewById(R.id.RecyclerNoteScren);
            textRecycler = itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler);
            linearLayout = itemView.findViewById(R.id.bkg);
            swipe = itemView.findViewById(R.id.testSwipe);
            delete = itemView.findViewById(R.id.deleteSwipe);
            changeColor = itemView.findViewById(R.id.changeColorSwipe);

            myCardView.setOnClickListener(v -> myTitleView.callOnClick());
         //   myTitleView.setOnClickListener(v -> ((MainActivity) context).onItemClickFromNoteScreen(itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler), itemView.findViewById(R.id.mainCard), itemView.findViewById(R.id.info_title), itemView.findViewById(R.id.RecyclerNoteScren), getAdapterPosition(), models.get(getAdapterPosition())));
            changeColor.setOnClickListener(v -> ((MainActivity)context).swipeItemsListener(v, models.get(getAdapterPosition())));
            delete.setOnClickListener(v -> ((MainActivity)context).swipeItemsListener(v, models.get(getAdapterPosition())));

        }

    }

    private void SetupNoteEditorRecycler(int position) {

        ArrayList<EverLinkedMap> items = new ArrayList<>();

        int i;

        List<String> bitmaps;
        List<String> content_split;

        bitmaps = models.get(position).getDraws();
        content_split = models.get(position).getContents();



        if (content_split.size() != bitmaps.size()) {
            for (i = content_split.size(); i < bitmaps.size(); i++) {
                content_split.add(content_split.size(), "▓");
                System.out.println("added = " + i + " times.");
            }
        }
        if (bitmaps.size() != content_split.size()) {
            for (i = bitmaps.size(); i < content_split.size(); i++) {
                bitmaps.add(bitmaps.size(), "");
                System.out.println("added bit = " + i + " times.");
            }
        }

        int size = bitmaps.size() - 1;

        for (i = 0; i <= size ; i++) {
            items.add(new EverLinkedMap(content_split.get(i), bitmaps.get(i)));
        }

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

            textRecycler.setLayoutManager(staggeredGridLayoutManager);

            EverAdapter adapter = new EverAdapter(context, items, models.get(position).getId(), position, null, myCardView, textRecycler, myRecyclerView, myTitleView, true);

            textRecycler.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
            textRecycler.setAdapter(new AlphaInAnimationAdapter(adapter));

    }
}
