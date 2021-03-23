package com.example.Evermind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Evermind.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.Evermind.databinding.NoteContentWithRecyclerviewVisualizationtestBinding;
import com.example.Evermind.everUtils.EverNoteDiffUtil;
import com.example.Evermind.recycler_models.EverLinkedMap;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class noteModelsAdapter extends RecyclerView.Adapter<noteModelsAdapter.ViewHolder>{

    private final List<Note_Model> noteData = new ArrayList<>();
    private final WeakReference<MainActivity> mainActivity;
    public noteModelsAdapter(Context context, List<Note_Model> noteData) {
        mainActivity = new WeakReference<>(((MainActivity)context));
        this.noteData.addAll(noteData);
    }



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder  implements EverInterfaceHelper.OnHideListener, EverInterfaceHelper.OnEnterDarkMode {
        @NonNull
        private final NoteContentWithRecyclerviewVisualizationtestBinding binding;
        private Note_Model noteModel;
        private  imagesAdapter imageAdapter;
        private noteContentsNoteScreenAdapter adapter;


        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            binding = NoteContentWithRecyclerviewVisualizationtestBinding.bind(view);

            EverInterfaceHelper.getInstance().setDarkModeListeners(this);

            LinearLayoutManager a = new LinearLayoutManager(mainActivity.get());
            a.setItemPrefetchEnabled(true);
            a.setOrientation(RecyclerView.VERTICAL);
            binding.DrawAndTextNoteScreenRecycler.setLayoutManager(a);
            if (adapter == null) {
                adapter = new noteContentsNoteScreenAdapter(new ArrayList<>());
                adapter.setHasStableIds(true);
                imageAdapter = new imagesAdapter(new ArrayList<>(), true);
            }



            binding.DrawAndTextNoteScreenRecycler.setItemAnimator(new customLandingAnimator());
            binding.DrawAndTextNoteScreenRecycler.setAdapter(adapter);

            EverInterfaceHelper.getInstance().setHideListener(this);

            binding.viewToApplyPushDown.setOnLongClickListener(v -> {
                noteModel.setSelected(!noteModel.isSelected());
                if (noteModel.isSelected()) {
                    mainActivity.get().getEverNoteManagement().getSelectedItems().add(noteModel);
                    if (mainActivity.get().getEverNoteManagement().getSelectedItems().size() == 1) {
                        mainActivity.get().getEverViewManagement().switchBottomBars("select");
                    }
                    mainActivity.get().getEverNoteManagement().setPushed(true);
                } else {
                    mainActivity.get().getEverNoteManagement().getSelectedItems().remove(noteModel);
                    mainActivity.get().setSmoothColorChange(true);
                    if (mainActivity.get().getEverNoteManagement().getSelectedItems().size() <= 0) {
                        System.out.println(mainActivity.get().getEverNoteManagement().getSelectedItems().size() + " ");
                        new Handler(Looper.getMainLooper()).postDelayed(() -> mainActivity.get().getEverNoteManagement().setPushed(false), 450);

                        mainActivity.get().getEverViewManagement().switchBottomBars("select");
                    }
                }
                mainActivity.get().getNoteScreen().get().getAdapter().notifyItemChanged(getAdapterPosition());
                return false;
            });

            binding.viewToApplyPushDown.setOnClickListener(v -> {
                //  if (!isnoteModelSelected()) {
                if (!mainActivity.get().getEverNoteManagement().isPushed()) {
                   onItemTouched();
                }
                //   }
            });
            binding.viewToApplyPushDown.setOnTouchListener((View v, MotionEvent event) -> {
                mainActivity.get().getEverViewManagement().pushDownOnTouch(binding.mainCard, event, 0.95f, 150);
                return false;
            });
        }

        public void onItemTouched() {
            mainActivity.get().onItemClickFromNoteScreen(binding.DrawAndTextNoteScreenRecycler, binding.mainCard, binding.imageRecyclerNoteScreen, getAdapterPosition(), noteModel);
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
            //    mainActivity.get().runOnUiThread(() -> {
            if (mainActivity.get().getEverThemeHelper().isDarkMode()) {
                binding.mainCard.setCardBackgroundColor(mainActivity.get().getEverThemeHelper().defaultTheme);
                binding.DrawAndTextNoteScreenRecycler.setBackgroundColor(mainActivity.get().getEverThemeHelper().defaultTheme);
                binding.imageRecyclerNoteScreen.setBackgroundColor(mainActivity.get().getEverThemeHelper().defaultTheme);
                if (noteModel.getNoteColor().equals("-1")) {
                    binding.mainCard.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getEverThemeHelper().defaultTheme));
                }
            }
            setTitle();
            setRecycler();
            setImages();
            //    });

        }

        void setTitle() {
            String title = noteModel.getTitle();
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

        void setRecycler() {

            adapter.updateList(noteModel.getEverLinkedContents(true));

            try {
                binding.DrawAndTextNoteScreenRecycler.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        void setImages() {
            final boolean[] exists = {false};
            mainActivity.get().asyncTask(() -> {
                for (String path : noteModel.getImages()) {
                    if (new File(path).exists()) {
                        exists[0] = true;
                    }
                }
            }, () -> {
                if (exists[0]) {

                    LinearLayoutManager a = new LinearLayoutManager(mainActivity.get());
                    a.setOrientation(RecyclerView.HORIZONTAL);
                    binding.imageRecyclerNoteScreen.setLayoutManager(a);
                    binding.imageRecyclerNoteScreen.setItemAnimator(new LandingAnimator(new LinearOutSlowInInterpolator()));
                    binding.imageRecyclerNoteScreen.setVisibility(View.VISIBLE);
                    binding.imageRecyclerNoteScreen.setAdapter(new AlphaInAnimationAdapter(imageAdapter));
                    imageAdapter.updateImages(noteModel.getImages());

                } else {
                    if (binding.imageRecyclerNoteScreen.getVisibility() == View.VISIBLE) {
                        binding.imageRecyclerNoteScreen.setVisibility(View.GONE);
                    }
                }

                mainActivity.get().getNoteScreen().get().StartPostpone();
            });
        }

        void updateNoteColor(boolean smooth) {
            int color = Integer.parseInt(noteModel.getNoteColor());
            if (binding.mainCard.getCardBackgroundColor() != ColorStateList.valueOf(color) || color == -1) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (smooth) {
                        mainActivity.get().getEverThemeHelper().tintViewAccent(binding.mainCard, color, 0);
                        mainActivity.get().setSmoothColorChange(false);
                    } else {
                        binding.mainCard.setBackgroundTintList(ColorStateList.valueOf(color));
                    }


                });
            }
        }

        @Override
        public void swichDarkMode(int color, boolean isDarkMode) {
            if (noteModel.getNoteColor().equals("-1")) {
                binding.mainCard.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getEverThemeHelper().defaultTheme));
            }
            binding.DrawAndTextNoteScreenRecycler.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getEverThemeHelper().defaultTheme));
            binding.imageRecyclerNoteScreen.setBackgroundTintList(ColorStateList.valueOf(mainActivity.get().getEverThemeHelper().defaultTheme));



            noteModelsAdapter.this.notifyItemChanged(getAdapterPosition());
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list noteModel
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_content_with_recyclerview_visualizationtest, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.noteModel = noteData.get(position);
        String transitionIdentifier = viewHolder.noteModel.getNote_name() + "-" + viewHolder.noteModel.getDate();
        System.out.println("Transition identifier = " + transitionIdentifier);

        if (viewHolder.binding.mainCard.getTransitionName() == null) {
            ViewCompat.setTransitionName(viewHolder.binding.DrawAndTextNoteScreenRecycler, "textRecycler" + transitionIdentifier);
            ViewCompat.setTransitionName(viewHolder.binding.mainCard, "card" + transitionIdentifier);
            ViewCompat.setTransitionName(viewHolder.binding.imageRecyclerNoteScreen, "imageRecycler" + transitionIdentifier);
        } else {
            if (!viewHolder.binding.mainCard.getTransitionName().equals("card" + transitionIdentifier)) {
                ViewCompat.setTransitionName(viewHolder.binding.DrawAndTextNoteScreenRecycler, "textRecycler" + transitionIdentifier);
                ViewCompat.setTransitionName(viewHolder.binding.mainCard, "card" + transitionIdentifier);
                ViewCompat.setTransitionName(viewHolder.binding.imageRecyclerNoteScreen, "imageRecycler" + transitionIdentifier);
            }
        }

        if (viewHolder.noteModel.isSelected()) {
            mainActivity.get().getEverThemeHelper().tintViewAccent(viewHolder.binding.mainCard, Color.RED, 500);
        } else {
            if (viewHolder.binding.mainCard.getBackgroundTintList().getDefaultColor() != Integer.parseInt(viewHolder.noteModel.getNoteColor())) {
                viewHolder.updateNoteColor(mainActivity.get().isSmoothColorChange());
            }
        }

        if (!mainActivity.get().getEverNoteManagement().isPushed()) {
            if (!mainActivity.get().getEverNoteManagement().isSwipeChangeColorRefresh()) {
                viewHolder.initNote();
            }
        }
    }

    public void updateNotesAdapter(List<Note_Model> noteData) {
        final EverNoteDiffUtil diffCallback = new EverNoteDiffUtil(this.noteData, noteData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);


        this.noteData.clear();
        this.noteData.addAll(noteData);
        diffResult.dispatchUpdatesTo(this);
    }



    public void changeLayout() {
        for (int i = 0; i < noteData.size(); i++)
            this.notifyItemChanged(i);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return noteData.size();
    }
}

