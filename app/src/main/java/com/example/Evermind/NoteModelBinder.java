package com.example.Evermind;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.daimajia.swipe.SwipeLayout;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.example.Evermind.ui.dashboard.ui.main.NoteEditorFragmentJavaFragment;
import com.thekhaeng.pushdownanim.PushDownAnim;

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
    private RecyclerView textRecycler;
    private TextView myTitleView;
    private RecyclerView myRecyclerView;
    private CardView myCardView;
    private SwipeLayout swipe;

    public NoteModelBinder(Context context) {
        this.context = context;
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

        myTitleView = holder.itemView.findViewById(R.id.info_title);
        myCardView = holder.itemView.findViewById(R.id.mainCard);
        myRecyclerView = holder.itemView.findViewById(R.id.RecyclerNoteScren);
        textRecycler = holder.itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler);

        ViewCompat.setTransitionName(textRecycler, "textRecycler" + ID);
        ViewCompat.setTransitionName(myCardView, "card" + ID);
        ViewCompat.setTransitionName(myRecyclerView, "imageRecycler" + ID);
        ViewCompat.setTransitionName(myTitleView, "title" + ID);

        if (!item.getNoteColor().equals("000000")) {
            myCardView.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(item.getNoteColor())));
        }

        SetupNoteEditorRecycler(item);

        myTitleView.setText(item.getTitle());

        if (item.getTitle().length() < 1) {
            //   TransitionManager.beginDelayedTransition(holder.itemView.findViewById(R.id.mainCard), new TransitionSet()
            //          .addTransition(new ChangeBounds()));
            myTitleView.setVisibility(View.GONE);

        }

        if (item.getImageURLS().length() > 1) {

            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);

            myRecyclerView.setLayoutManager(staggeredGridLayoutManager);

            MultiViewAdapter adapter = new MultiViewAdapter();
            ListSection<String> list = new ListSection<>();
            list.addAll(item.getImages());
            adapter.addSection(list);
            adapter.registerItemBinders(new ImagesBinder(context, list.size(), true));
            myRecyclerView.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
            myRecyclerView.setAdapter(new AlphaInAnimationAdapter(adapter));

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

    private void SetupNoteEditorRecycler(Note_Model item) {

        ArrayList<EverLinkedMap> items = new ArrayList<>();

        int i;

        List<String> bitmaps;
        List<String> content_split;

        bitmaps = item.getDraws();
        content_split = item.getContents();


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

        for (i = 0; i <= size; i++) {
            items.add(new EverLinkedMap(content_split.get(i), bitmaps.get(i)));
            //  items.add(new EverLinkedMap("", bitmaps.get(i)));
        }

        LinearLayoutManager a = new LinearLayoutManager(context);
        textRecycler.setLayoutManager(a);

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
        adptr.registerItemBinders(new NoteContentsNoteScreenBinder(context, items.size(), myTitleView));
        adptr.addSection(list);
        textRecycler.setItemAnimator(new LandingAnimator(new OvershootInterpolator(1f)));
        textRecycler.setAdapter(new AlphaInAnimationAdapter(adptr));

    }

    class NoteModelViewHolder extends ItemViewHolder<Note_Model> {

        @SuppressLint("ClickableViewAccessibility")
        public NoteModelViewHolder(View itemView) {
            super(itemView);

            ImageButton delete = itemView.findViewById(R.id.deleteSwipe);
            ImageButton changeColor = itemView.findViewById(R.id.changeColorSwipe);
            View view = itemView.findViewById(R.id.view2);

            myTitleView = itemView.findViewById(R.id.info_title);
            myCardView = itemView.findViewById(R.id.mainCard);
            myRecyclerView = itemView.findViewById(R.id.RecyclerNoteScren);
            textRecycler = itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler);
            swipe = itemView.findViewById(R.id.testSwipe);
            changeColor.setOnClickListener(v -> ((MainActivity) context).swipeItemsListener(v, getItem()));
            delete.setOnClickListener(v -> ((MainActivity) context).swipeItemsListener(v, getItem()));

           view.setOnClickListener(v -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
               ((MainActivity) context).onItemClickFromNoteScreen(itemView.findViewById(R.id.DrawAndTextNoteScreenRecycler), itemView.findViewById(R.id.mainCard), itemView.findViewById(R.id.info_title), itemView.findViewById(R.id.RecyclerNoteScren), getAdapterPosition(), getItem());
           }, 40));
           view.setOnTouchListener((v, event) -> {
               ((MainActivity)context).pushDownOnTouch(itemView.findViewById(R.id.mainCard), event, 0.85f, 50);
               return false;
           });

            PushDownAnim.setPushDownAnimTo(delete)
                    .setScale( MODE_SCALE,
                            0.7f );
            PushDownAnim.setPushDownAnimTo(changeColor)
                    .setScale( MODE_SCALE,
                            0.7f );
        }
    }
}