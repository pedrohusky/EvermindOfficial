package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.daimajia.swipe.SwipeLayout;
import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.databinding.MainViewNoteCreatorRecyclerBinding;
import com.example.Evermind.databinding.NoteContentWithRecyclerviewVisualizationtestBinding;
import com.example.Evermind.recycler_models.EverLinkedMap;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.lang.ref.WeakReference;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class NoteModelBinder extends ItemBinder<Note_Model, NoteModelBinder.NoteModelViewHolder> {

    private final Context context;
    private final WeakReference<MainActivity> mainActivity;
    private final EverLinkedMap e;

    public NoteModelBinder(Context context) {
        this.context = context;
        mainActivity = new WeakReference<>(((MainActivity) context));
        e = new EverLinkedMap(true);
    }


    @Override
    public NoteModelViewHolder createViewHolder(ViewGroup parent) {
        return new NoteModelViewHolder(inflate(parent, R.layout.note_content_with_recyclerview_visualizationtest));
    }


    @Override
    public boolean canBindData(Object item) {
        return item instanceof Note_Model;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bindViewHolder(NoteModelViewHolder holder, Note_Model item) {

        String transitionIdentifier = item.getId() + "-" + item.getDate();

        if (holder.binding.mainCard.getTransitionName() == null) {
            ViewCompat.setTransitionName(holder.binding.DrawAndTextNoteScreenRecycler, "textRecycler" + transitionIdentifier);
            ViewCompat.setTransitionName(holder.binding.mainCard, "card" + transitionIdentifier);
            ViewCompat.setTransitionName(holder.binding.imageRecyclerNoteScreen, "imageRecycler" + transitionIdentifier);
        } else {
            if (!holder.binding.mainCard.getTransitionName().equals("card" + transitionIdentifier)) {
                ViewCompat.setTransitionName(holder.binding.DrawAndTextNoteScreenRecycler, "textRecycler" + transitionIdentifier);
                ViewCompat.setTransitionName(holder.binding.mainCard, "card" + transitionIdentifier);
                ViewCompat.setTransitionName(holder.binding.imageRecyclerNoteScreen, "imageRecycler" + transitionIdentifier);
            }
        }

        if (item.isSelected()) {
            mainActivity.get().getEverThemeHelper().tintView(holder.binding.mainCard, Color.RED, 0);
        } else {
            if (holder.binding.mainCard.getBackgroundTintList().getDefaultColor() != Integer.parseInt(item.getNoteColor())) {
                holder.updateNoteColor(mainActivity.get().isSmoothColorChange());
            }
        }

        if (!mainActivity.get().getEverNoteManagement().isPushed()) {
            if (!mainActivity.get().getEverNoteManagement().isSwipeChangeColorRefresh()) {
                holder.initNote();
            }
        }
    }

    public class NoteModelViewHolder extends ItemViewHolder<Note_Model> implements EverInterfaceHelper.OnHideListener {

        private final ListSection<String> listImages = new ListSection<>();
        private final ListSection<EverLinkedMap> listContents = new ListSection<>();
        private ImagesBinder imagesBinder;
        private Note_Model lastProcessedNote;

        private final NoteContentWithRecyclerviewVisualizationtestBinding binding;

        @SuppressLint("ClickableViewAccessibility")
        public NoteModelViewHolder(View itemView) {
            super(itemView);

            binding = NoteContentWithRecyclerviewVisualizationtestBinding.bind(itemView);

        }

        @Override
        public void hide() {
            itemView.setVisibility(View.GONE);
        }

        @Override
        public void show() {
            itemView.setVisibility(View.VISIBLE);
        }

        @SuppressLint("ClickableViewAccessibility")
        void initNote() {
            setTitle();
            setRecycler();
            setImages();
        }

        void setTitle() {
            String title = getItem().getTitle();
            if (!title.equals("")) {

                binding.infoTitle.setText(title);

                ViewGroup.LayoutParams params = binding.infoTitle.getLayoutParams();

                params.height = WRAP_CONTENT;

                binding.infoTitle.setLayoutParams(params);
                binding.infoTitle.setPadding(10, 5, 10, 5);
            } else {
                ViewGroup.LayoutParams params = binding.infoTitle.getLayoutParams();

                binding.infoTitle.setText("");
                binding.infoTitle.setPadding(0, 0, 0, 0);

                params.height = 50;

                binding.infoTitle.setLayoutParams(params);
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        void setRecycler() {
            if (binding.DrawAndTextNoteScreenRecycler.getAdapter() == null) {
                Runnable r = () -> {
                    LinearLayoutManager a = new LinearLayoutManager(context);
                    a.setOrientation(RecyclerView.VERTICAL);
                    a.setInitialPrefetchItemCount(3);
                    //   binding.DrawAndTextNoteScreenRecycler.setHasFixedSize(true);
                    binding.DrawAndTextNoteScreenRecycler.setLayoutManager(a);
                    MultiViewAdapter adapter = new MultiViewAdapter();
                    listContents.add(e);
                    listContents.add(e);
                    listContents.add(e);
                    binding.DrawAndTextNoteScreenRecycler.setItemAnimator(new LandingAnimator(new LinearOutSlowInInterpolator()));
                    adapter.registerItemBinders(new NoteContentsNoteScreenBinder(context));
                    binding.DrawAndTextNoteScreenRecycler.setAdapter(new ScaleInAnimationAdapter(adapter));
                    updateList();
                    adapter.addSection(listContents);
                };
                mainActivity.get().runOnUiThread(r);
                new Handler(Looper.myLooper()).post(() -> {
                    binding.changeColorSwipe.setOnClickListener(v -> ((MainActivity) context).swipeItemsListener(v, getItem()));
                    binding.deleteSwipe.setOnClickListener(v -> ((MainActivity) context).swipeItemsListener(v, getItem()));

                    binding.testSwipe.setShowMode(SwipeLayout.ShowMode.PullOut);
                    binding.testSwipe.setClickToClose(true);


                    PushDownAnim.setPushDownAnimTo(binding.deleteSwipe)
                            .setScale(MODE_SCALE,
                                    0.7f);
                    PushDownAnim.setPushDownAnimTo(binding.changeColorSwipe)
                            .setScale(MODE_SCALE,
                                    0.7f);

                    EverInterfaceHelper.getInstance().setHideListener(this);


                    binding.viewToApplyPushDown.setOnLongClickListener(v -> {
                        getItem().setSelected(!getItem().isSelected());
                        if (getItem().isSelected()) {
                            mainActivity.get().getEverNoteManagement().getSelectedItems().add(getItem());
                            mainActivity.get().getEverViewManagement().CloseOrOpenSelectionOptions(false);
                            mainActivity.get().getEverNoteManagement().setPushed(true);
                        } else {
                            mainActivity.get().getEverNoteManagement().getSelectedItems().remove(mainActivity.get().getEverNoteManagement().getSelectedItems().getData().indexOf(getItem()));
                            mainActivity.get().setSmoothColorChange(true);
                            if (mainActivity.get().getEverNoteManagement().getSelectedItems().size() <= 0) {
                                System.out.println(mainActivity.get().getEverNoteManagement().getSelectedItems().size() + " " + isItemSelected());
                                new Handler(Looper.getMainLooper()).postDelayed(() -> mainActivity.get().getEverNoteManagement().setPushed(false), 450);
                                mainActivity.get().getEverViewManagement().CloseOrOpenSelectionOptions(true);
                            }
                        }
                        mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(getAdapterPosition());
                        return false;
                    });

                    binding.viewToApplyPushDown.setOnClickListener(v -> {
                        if (!isItemSelected()) {
                            if (!mainActivity.get().getEverNoteManagement().isPushed()) {
                                new Handler(Looper.getMainLooper()).postDelayed(() -> ((MainActivity) context).onItemClickFromNoteScreen(binding.DrawAndTextNoteScreenRecycler, binding.mainCard, binding.imageRecyclerNoteScreen, getAdapterPosition(), getItem()), 40);
                            }
                        }
                    });

                    binding.viewToApplyPushDown.setOnTouchListener((View v, MotionEvent event) -> {
                        mainActivity.get().getEverViewManagement().pushDownOnTouch(binding.mainCard, event, 0.85f, 50);
                        return false;
                    });
                });

            } else {
                mainActivity.get().runOnUiThread(this::updateList);
            }
        }

        void setImages() {
            List<String> images = getItem().getImages();
            if (images.size() > 0) {
               // if (!getItem().getImages().get(0).equals("") && !getItem().getImages().get(0).equals("▓")) {

                    if (binding.imageRecyclerNoteScreen.getAdapter() == null) {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);
                        binding.imageRecyclerNoteScreen.setLayoutManager(staggeredGridLayoutManager);

                        MultiViewAdapter imageAdapter = new MultiViewAdapter();
                        listImages.addAll(images);
                        imageAdapter.addSection(listImages);
                        imagesBinder = new ImagesBinder(context, true);
                        imagesBinder.size = images.size();
                        imageAdapter.registerItemBinders(imagesBinder);
                        binding.imageRecyclerNoteScreen.setItemAnimator(new LandingAnimator(new LinearOutSlowInInterpolator()));
                        binding.imageRecyclerNoteScreen.setVisibility(View.VISIBLE);
                        binding.imageRecyclerNoteScreen.setAdapter(new AlphaInAnimationAdapter(imageAdapter));
                        System.out.println("Image adapter set at = " + getAdapterPosition());
                    } else {
                        if (!listImages.getData().equals(images)) {
                            binding.imageRecyclerNoteScreen.setVisibility(View.VISIBLE);
                            imagesBinder.size = getItem().getImages().size();
                            listImages.clear();
                            listImages.addAll(images);
                            System.out.println("Image adapter turned visible at = " + getAdapterPosition());
                        } else {
                            binding.imageRecyclerNoteScreen.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    if (binding.imageRecyclerNoteScreen.getVisibility() == View.VISIBLE) {
                        binding.imageRecyclerNoteScreen.setVisibility(View.GONE);
                        System.out.println("Image adapter gone at = " + getAdapterPosition());
                    }
                }

                //   EverScrollDecorator.setUpOverScroll(myRecyclerView.get(), EverScrollDecorator.ORIENTATION_HORIZONTAL);


            mainActivity.get().getNoteScreen().get().StartPostpone();
        }

        void updateNoteColor(boolean smooth) {
            int color = Integer.parseInt(getItem().getNoteColor());
            if (binding.mainCard.getCardBackgroundColor() != ColorStateList.valueOf(color) || color == -1) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (smooth) {
                        mainActivity.get().getEverThemeHelper().tintView(binding.mainCard, color, 0);
                        mainActivity.get().setSmoothColorChange(false);
                    } else {
                        binding.mainCard.setBackgroundTintList(ColorStateList.valueOf(color));
                    }

                    for (int i = 0; i < listContents.size(); i++) {
                        if (!listContents.get(i).getRecord().equals("▓")) {
                            View view = binding.DrawAndTextNoteScreenRecycler.getChildAt(i);
                            if (view != null) {
                                View cardView = view.findViewById(R.id.card_everNoteScreenPlayer);
                                if (cardView != null) {
                                    mainActivity.get().getEverThemeHelper().tintView(cardView, color, 0);
                                }
                            }
                        }
                    }
                });
            }
        }

        void updateList() {
            if (getItem() == lastProcessedNote) {
                for (int i = 0; i < 3; i++) {
                    if (getItem().everLinkedContents.size() >= i + 1) {
                        if (!listContents.getData().get(i).equals(getItem().everLinkedContents.getData().get(i))) {
                            listContents.set(i, getItem().everLinkedContents.get(i));
                        }
                    } else {
                        listContents.set(i, e);
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    if (getItem().everLinkedContents.size() >= i + 1) {
                            listContents.set(i, getItem().everLinkedContents.get(i));
                    } else {
                        listContents.set(i, e);
                    }
                }
                lastProcessedNote = getItem();
            }

        }
    }
}