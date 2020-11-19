package com.example.Evermind;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
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
        int ID = holder.getItem().getId();




        myTitleView = new WeakReference<>(holder.itemView.findViewById(R.id.info_title));
        myCardView = new WeakReference<>(holder.itemView.findViewById(R.id.mainCard));
        myRecyclerView = new WeakReference<>(holder.itemView.findViewById(R.id.RecyclerNoteScren));
        textRecycler = new WeakReference<>(holder.itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler));
        WeakReference<View> view = new WeakReference<>(holder.itemView.findViewById(R.id.view2));
        WeakReference<SwipeLayout> swipe = new WeakReference<>(holder.itemView.findViewById(R.id.testSwipe));
        ViewCompat.setTransitionName(textRecycler.get(), "textRecycler" + ID);
        ViewCompat.setTransitionName(myCardView.get(), "card" + ID);
        ViewCompat.setTransitionName(myRecyclerView.get(), "imageRecycler" + ID);
        ViewCompat.setTransitionName(myTitleView.get(), "title" + ID);

        if (holder.isItemSelected()) {
            mainActivity.get().tintView(myCardView.get(), Color.RED);
        } else {
            if (!holder.getItem().getNoteColor().equals("000000")) {
                mainActivity.get().tintView(myCardView.get(), Integer.parseInt(holder.getItem().getNoteColor()));
            } else {
                mainActivity.get().tintView(myCardView.get(), Color.WHITE);
            }

            if (mainActivity.get().searching) {
                if (!mainActivity.get().pushed) {
                    if (!mainActivity.get().swipeChangeColorRefresh) {
                        init(holder.getItem());
                    }
                }

            }

            if (!mainActivity.get().pushed) {
                if (!mainActivity.get().swipeChangeColorRefresh) {
                    init(holder.getItem());
                }
            }
        }

        WeakReference<ImageButton> delete = new WeakReference<>(holder.itemView.findViewById(R.id.deleteSwipe));
        WeakReference<ImageButton> changeColor = new WeakReference<>(holder.itemView.findViewById(R.id.changeColorSwipe));

        changeColor.get().setOnClickListener(v -> ((MainActivity) context).swipeItemsListener(v, holder.getItem()));
        delete.get().setOnClickListener(v -> ((MainActivity) context).swipeItemsListener(v, holder.getItem()));
        view.get().setOnLongClickListener(v -> {
            holder.toggleItemSelection();
            if (holder.isItemSelected()) {
                mainActivity.get().CloseOrOpenSelectionOptions(false);
                mainActivity.get().pushed = true;
            } else {
                if (mainActivity.get().noteModelSection.getSelectedItems().size() == 0) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> mainActivity.get().pushed = false, 450);
                    mainActivity.get().CloseOrOpenSelectionOptions(true);
                }
            }
            return false;
        });

        view.get().setOnClickListener(v -> {
            if (!holder.isItemSelected()) {
                if (!mainActivity.get().pushed) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).onItemClickFromNoteScreen(holder.itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler), holder.itemView.findViewById(R.id.mainCard), holder.itemView.findViewById(R.id.info_title), holder.itemView.findViewById(R.id.RecyclerNoteScren), holder.getAdapterPosition(), holder.getItem()), 40);
                }
            }
        });

        view.get().setOnTouchListener((v, event) -> {
            mainActivity.get().pushDownOnTouch(holder.itemView.findViewById(R.id.mainCard), event, 0.85f, 50);
            return false;
        });

        swipe.get().setShowMode(SwipeLayout.ShowMode.PullOut);
        swipe.get().setClickToClose(true);
     // if (holder.getAdapterPosition() == mainActivity.get().noteModelSection.getData().size()-1) {

        new Handler(Looper.getMainLooper()).postDelayed(() -> mainActivity.get().noteScreen.StartPostpone(), 25);

      //  }

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

        textRecycler.get().setItemAnimator(new LandingAnimator(new EvershootInterpolator(1f)));
        textRecycler.get().setAdapter(new AlphaInAnimationAdapter(adptr));

    }

    class NoteModelViewHolder extends ItemViewHolder<Note_Model> {

        @SuppressLint("ClickableViewAccessibility")
        public NoteModelViewHolder(View itemView) {
            super(itemView);

            WeakReference<ImageButton> delete = new WeakReference<>(itemView.findViewById(R.id.deleteSwipe));
            WeakReference<ImageButton> changeColor = new WeakReference<>(itemView.findViewById(R.id.changeColorSwipe));

            myTitleView = new WeakReference<>(itemView.findViewById(R.id.info_title));
            myCardView = new WeakReference<>(itemView.findViewById(R.id.mainCard));
            myRecyclerView = new WeakReference<>(itemView.findViewById(R.id.RecyclerNoteScren));
            textRecycler = new WeakReference<>(itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler));



            PushDownAnim.setPushDownAnimTo(delete.get())
                    .setScale( MODE_SCALE,
                            0.7f );
            PushDownAnim.setPushDownAnimTo(changeColor.get())
                    .setScale( MODE_SCALE,
                            0.7f );
        }
    }
    private void init(Note_Model note) {
        SetupNoteEditorRecycler(note);

        myTitleView.get().setText(note.getTitle());

        if (note.getTitle().length() < 1) {
            //   TransitionManager.beginDelayedTransition(holder.itemView.findViewById(R.id.mainCard), new TransitionSet()
            //          .addTransition(new ChangeBounds()));
            myTitleView.get().setVisibility(View.GONE);

        }

        if (note.getImages().size() > 0) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);

            myRecyclerView.get().setLayoutManager(staggeredGridLayoutManager);

            MultiViewAdapter adapter = new MultiViewAdapter();
            ListSection<String> list = new ListSection<>();
            list.addAll(note.getImages());
            adapter.addSection(list);
            adapter.registerItemBinders(new ImagesBinder(context, list.size(), true));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                myRecyclerView.get().setItemAnimator(new LandingAnimator(new EvershootInterpolator(1f)));
            }
            myRecyclerView.get().setAdapter(new AlphaInAnimationAdapter(adapter));

            //   EverScrollDecorator.setUpOverScroll(myRecyclerView.get(), EverScrollDecorator.ORIENTATION_HORIZONTAL);

        } else {
            myRecyclerView.get().setVisibility(View.GONE);
        }

    }
}