package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.daimajia.swipe.SwipeLayout;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class NoteModelBinder extends ItemBinder<Note_Model, NoteModelBinder.NoteModelViewHolder> {

    private final Context context;
    private WeakReference<RecyclerView> textRecycler;
    private WeakReference<TextView> myTitleView;
    private WeakReference<RecyclerView> myRecyclerView;
    private WeakReference<CardView> myCardView;
    private final WeakReference<MainActivity> mainActivity;

    public NoteModelBinder(Context context) {
        this.context = context;
        mainActivity = new WeakReference<>(((MainActivity)context));
    }


    @Override
    public NoteModelViewHolder createViewHolder(ViewGroup parent) {
        return new NoteModelViewHolder(inflate(parent, R.layout.note_content_with_recyclerview_visualizationtest));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof Note_Model;
    }

    @Override
    public void bindViewHolder(NoteModelViewHolder holder, Note_Model item) {
        int ID = item.getId();




        myTitleView = new WeakReference<>(holder.itemView.findViewById(R.id.info_title));
        myCardView = new WeakReference<>(holder.itemView.findViewById(R.id.mainCard));
        myRecyclerView = new WeakReference<>(holder.itemView.findViewById(R.id.RecyclerNoteScren));
        textRecycler = new WeakReference<>(holder.itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler));
        WeakReference<SwipeLayout> swipe = new WeakReference<>(holder.itemView.findViewById(R.id.testSwipe));

        ViewCompat.setTransitionName(textRecycler.get(), "textRecycler" + ID);
        ViewCompat.setTransitionName(myCardView.get(), "card" + ID);
        ViewCompat.setTransitionName(myRecyclerView.get(), "imageRecycler" + ID);
        ViewCompat.setTransitionName(myTitleView.get(), "title" + ID);



        if (holder.isItemSelected()) {
            myCardView.get().setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        } else {
            if (!holder.getItem().getNoteColor().equals("000000")) {
                myCardView.get().setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(holder.getItem().getNoteColor())));
            } else {
                myCardView.get().setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }

        SetupNoteEditorRecycler(holder.getItem());

        myTitleView.get().setText(holder.getItem().getTitle());

        if (holder.getItem().getTitle().length() < 1) {
            //   TransitionManager.beginDelayedTransition(holder.itemView.findViewById(R.id.mainCard), new TransitionSet()
            //          .addTransition(new ChangeBounds()));
            myTitleView.get().setVisibility(View.GONE);

        }

        if (holder.getItem().getImages().size() > 0) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);

            myRecyclerView.get().setLayoutManager(staggeredGridLayoutManager);

            MultiViewAdapter adapter = new MultiViewAdapter();
            ListSection<String> list = new ListSection<>();
            list.addAll(holder.getItem().getImages());
            adapter.addSection(list);
            adapter.registerItemBinders(new ImagesBinder(context, list.size(), true));
            myRecyclerView.get().setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
            myRecyclerView.get().setAdapter(new AlphaInAnimationAdapter(adapter));

            OverScrollDecoratorHelper.setUpOverScroll(myRecyclerView.get(), OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        } else {
            myRecyclerView.get().setVisibility(View.GONE);
        }

        swipe.get().setShowMode(SwipeLayout.ShowMode.PullOut);
        swipe.get().setClickToClose(true);
    }

    private void SetupNoteEditorRecycler(Note_Model item) {

        ArrayList<EverLinkedMap> items = new ArrayList<>();

        int i;

        List<String> bitmaps;
        List<String> content_split;

        bitmaps = item.getDraws();
        content_split = item.getContents();


        if (content_split.size() != bitmaps.size()) {
            for (i = content_split.size(); i < bitmaps.size(); i++) {
                content_split.add("▓");
           }
        }
        if (bitmaps.size() != content_split.size()) {
            for (i = bitmaps.size(); i < content_split.size(); i++) {
                bitmaps.add("");
            }
        }

        int size = bitmaps.size();

        for (i = 0; i < size; i++) {
            items.add(new EverLinkedMap(content_split.get(i), bitmaps.get(i)));
        }

        LinearLayoutManager a = new LinearLayoutManager(context);
        textRecycler.get().setLayoutManager(a);

        MultiViewAdapter adptr = new MultiViewAdapter();
        ListSection<EverLinkedMap> list = new ListSection<>();
        if (items.size() > 0) {
            list.add(items.get(0));
        }
        if (items.size() > 1) {
            list.add(items.get(1));
        }
        if (items.size() > 2) {
            list.add(items.get(2));
        }
      //  list.addAll(items);
        adptr.registerItemBinders(new NoteContentsNoteScreenBinder(context, items.size()));
        adptr.addSection(list);

        textRecycler.get().setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
        textRecycler.get().setAdapter(new AlphaInAnimationAdapter(adptr));

    }

    class NoteModelViewHolder extends ItemViewHolder<Note_Model> {

        @SuppressLint("ClickableViewAccessibility")
        public NoteModelViewHolder(View itemView) {
            super(itemView);

            WeakReference<ImageButton> delete = new WeakReference<>(itemView.findViewById(R.id.deleteSwipe));
            WeakReference<ImageButton> changeColor = new WeakReference<>(itemView.findViewById(R.id.changeColorSwipe));
            WeakReference<View> view = new WeakReference<>(itemView.findViewById(R.id.view2));
            myTitleView = new WeakReference<>(itemView.findViewById(R.id.info_title));
            myCardView = new WeakReference<>(itemView.findViewById(R.id.mainCard));
            myRecyclerView = new WeakReference<>(itemView.findViewById(R.id.RecyclerNoteScren));
            textRecycler = new WeakReference<>(itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler));

            changeColor.get().setOnClickListener(v -> ((MainActivity) context).swipeItemsListener(v, getItem()));
            delete.get().setOnClickListener(v -> ((MainActivity) context).swipeItemsListener(v, getItem()));
            view.get().setOnLongClickListener(v -> {
                toggleItemSelection();
               if (isItemSelected()) {
                   mainActivity.get().CloseOrOpenSelectionOptions(false);
                   mainActivity.get().pushed = false;
               } else {
                   if (mainActivity.get().noteModelSection.getSelectedItems().size() == 0) {
                       new Handler(Looper.getMainLooper()).postDelayed(() -> mainActivity.get().pushed = true, 500);
                       mainActivity.get().CloseOrOpenSelectionOptions(true);
                   }
               }
                return false;
            });

            view.get().setOnClickListener(v -> {
                if (!isItemSelected()) {
                    if (mainActivity.get().pushed) {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).onItemClickFromNoteScreen(itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler), itemView.findViewById(R.id.mainCard), itemView.findViewById(R.id.info_title), itemView.findViewById(R.id.RecyclerNoteScren), getAdapterPosition(), getItem()), 40);
                    }
                }
            });

            view.get().setOnTouchListener((v, event) -> {
               mainActivity.get().pushDownOnTouch(itemView.findViewById(R.id.mainCard), event, 0.85f, 50);
               return false;
           });

            PushDownAnim.setPushDownAnimTo(delete.get())
                    .setScale( MODE_SCALE,
                            0.7f );
            PushDownAnim.setPushDownAnimTo(changeColor.get())
                    .setScale( MODE_SCALE,
                            0.7f );
        }
    }
}