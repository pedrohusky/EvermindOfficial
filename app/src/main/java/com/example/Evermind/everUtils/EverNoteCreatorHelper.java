package com.example.Evermind.everUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.Evermind.EverDraw;
import com.example.Evermind.EverNestedScrollView;
import com.example.Evermind.EverPopup;
import com.example.Evermind.EvershootInterpolator;
import com.example.Evermind.ImagesBinder;
import com.example.Evermind.MainActivity;
import com.example.Evermind.NoteContentsBinder;
import com.example.Evermind.R;
import com.example.Evermind.TESTEDITOR.rteditor.RTEditText;
import com.example.Evermind.recycler_models.EverLinkedMap;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import me.everything.android.ui.overscroll.HorizontalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.StaticOverScrollDecorAdapter;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

public class EverNoteCreatorHelper {
    private final WeakReference<MainActivity> mainActivity;
    public WeakReference<RecyclerView> textDrawRecycler;
    public WeakReference<EverDraw> everDraw;
    public int FinalYHeight;
    public WeakReference<RTEditText> activeEditor;
    public int activeEditorPosition = -1;
    public WeakReference<EverNestedScrollView> scrollView;
    public WeakReference<EditText> TitleTextBox;
    public WeakReference<CardView> cardView;
    public WeakReference<MultiViewAdapter> adptr ;
    public ListSection<EverLinkedMap> everLinkedContents = new ListSection<>();
    public ListSection<String> listImages;
    private EverDraw drawToRemove;
    public int drawPosition = 0;
    private String savedBitmapPath;
    private WeakReference<RecyclerView> recyclerViewImage;
    public boolean drawFromRecycler = false;
    public boolean hasImages = false;
    public boolean hasDraws = false;
    public  int cardWidth;
    public int cardHeight;
    private CardView card;

    public EverNoteCreatorHelper(Context context) {
        mainActivity = new WeakReference<>(((MainActivity)context));
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init() {

        mainActivity.get().switchToolbars(false);

        if (mainActivity.get().actualNote.get().getImages().size() > 0) {
            hasImages = true;
        }
        if (mainActivity.get().actualNote.get().getDraws().size() > 0) {
            hasDraws = true;
        }

        everDraw = new WeakReference<>(mainActivity.get().findViewById(R.id.EverDraw));
        // scrollView = new WeakReference<>(mainActivity.get().findViewById(R.id.scrollview));
        TitleTextBox = new WeakReference<>(mainActivity.get().findViewById(R.id.TitleTextBox));
        textDrawRecycler = new WeakReference<>(mainActivity.get().findViewById(R.id.TextAndDrawRecyclerView));
        cardView = new WeakReference<>(mainActivity.get().findViewById(R.id.card_note_creator));
        scrollView = new WeakReference<>(mainActivity.get().findViewById(R.id.nestedScroll));
        mainActivity.get().actualNote.get().getEverContents();
        everLinkedContents = mainActivity.get().actualNote.get().everLinkedContents;

        LinearLayoutManager a = new LinearLayoutManager(mainActivity.get());
        textDrawRecycler.get().setLayoutManager(a);
        adptr = new WeakReference<>(new MultiViewAdapter());
        adptr.get().registerItemBinders(new NoteContentsBinder(mainActivity.get()));
        adptr.get().addSection(everLinkedContents);
        textDrawRecycler.get().setItemAnimator(new LandingAnimator(new EvershootInterpolator(1F)));
        textDrawRecycler.get().setAdapter(new AlphaInAnimationAdapter(adptr.get()));
        mainActivity.get().contentRecycler = textDrawRecycler;
        mainActivity.get().cardNoteCreator = cardView;
       // SetupNoteEditorRecycler();
        recyclerViewImage = new WeakReference<>(mainActivity.get().findViewById(R.id.ImagesRecycler));
        cardView.get().requestFocus();


        if (!mainActivity.get().names.isEmpty()) {
            if (mainActivity.get().newNote) {
                cardView.get().setTransitionName(mainActivity.get().names.get(0));
            } else {
                textDrawRecycler.get().setTransitionName(mainActivity.get().names.get(0));
                cardView.get().setTransitionName(mainActivity.get().names.get(1));
                TitleTextBox.get().setTransitionName(mainActivity.get().names.get(2));
                recyclerViewImage.get().setTransitionName(mainActivity.get().names.get(3));
            }
            mainActivity.get().names.clear();
        }

        if (mainActivity.get().actualNote != null) {
            TitleTextBox.get().setText(mainActivity.get().actualNote.get().getTitle());
        }
        mainActivity.get().title = TitleTextBox;

        reloadImages();

        new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(TitleTextBox.get()));

        boolean NewNote = mainActivity.get().newNote;

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (NewNote) {

                mainActivity.get().switchToolbars(true, true, false);
            }

        }, 750);

        //  EverScrollDecorator.setUpOverScroll(scrollView.get());
        everDraw.get().setOnTouchListener((view, motionEvent) -> {

            scrollView.get().setCanScroll(false);

            int y = (int) motionEvent.getY();

            if (y >= FinalYHeight) {
                FinalYHeight = y;
            }

            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                if (y >= everDraw.get().getHeight() - 75) {

                    new Handler(Looper.getMainLooper()).post(() -> {


                        TransitionManager.beginDelayedTransition(cardView.get(), new TransitionSet()
                                .addTransition(new ChangeBounds()));

                        ViewGroup.LayoutParams params = everDraw.get().getLayoutParams();

                        params.height = FinalYHeight + 100;

                        everDraw.get().setLayoutParams(params);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> scrollView.get().setCanScroll(true), 150);

                    });

                    return false;
                }
            }
            return false;
        });

        mainActivity.get().seekBarDrawSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                everDraw.get().setStrokeWidth((float) (i*1.1));

                if (!mainActivity.get().DrawVisualizerIsShowing) {
                    mainActivity.get().ShowDrawSizeVisualizer();
                }
                mainActivity.get().ModifyDrawSizeVisualizer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        TitleTextBox.get().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                //new Handler(Looper.getMainLooper()).postDelayed(() -> {
                mainActivity.get().switchToolbars(true, false, false);
                // }, 400);
            }
        });
        // TransitionManager.beginDelayedTransition(cardView, new TransitionSet()
        //         .addTransition(new ChangeBounds()));


        if (!mainActivity.get().actualNote.get().getNoteColor().equals("16777215")) {
            mainActivity.get().everThemeHelper.tintSystemBars(Integer.parseInt(mainActivity.get().actualNote.get().getNoteColor()), 500);
            mainActivity.get().everBallsHelper.metaColorNoteColor = Integer.parseInt(mainActivity.get().actualNote.get().getNoteColor());
        }

        // }).start();
    }

    private void onBackPressed() {
        mainActivity.get().actualNote.get().setTitle(TitleTextBox.get().getText().toString());
        mainActivity.get().onBackPressed();
    }

    public void onDrawClick(EverDraw view, int position, String path, CardView swipe) {

        // everAdapter.setFinalYHeight(0);
        if (drawToRemove != null) {
            drawToRemove.setVisibility(View.GONE);
        }
        this.card = swipe;
        drawToRemove = view;
        savedBitmapPath = path;
        drawPosition = position;
        System.out.println("Selected position of draw is =" + drawPosition);
        drawFromRecycler = true;
        this.everDraw = new WeakReference<>(view);
        mainActivity.get().CloseOrOpenDraWOptionsFromRecycler(scrollView.get(), textDrawRecycler.get(), false);
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onImageLongPress(View view, int position) {
        EverPopup popupWindowHelper;
        View popView;
        popView = LayoutInflater.from(mainActivity.get()).inflate(R.layout.options_attached, null);
        popupWindowHelper = new EverPopup(popView);
        popupWindowHelper.showAsDropDown(view, 350, -25);
        ImageButton imageView = popView.findViewById(R.id.DeleteAttached);

        imageView.setOnClickListener(view1 -> {
            removeImage(position);
            popupWindowHelper.dismiss();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onLongPress( int position) {

        String finalS;
        List<String> htmls = mainActivity.get().actualNote.get().getDraws();
        List<String> strings = mainActivity.get().actualNote.get().getContents();
        List<String> arrayList = new ArrayList<>();
        List<String> stringsList = new ArrayList<>();
        String selectedDrawPath = everLinkedContents.get(position).getDrawLocation();
        String toReplace = "";
        int positionToReplace = 0;

        for (String s : strings) {
            if (s.equals(strings.get(position))) {
                toReplace = strings.get(position);
                positionToReplace = position;
            } else {
                stringsList.add(s + "┼");
            }
        }
        if (everLinkedContents.get(position + 1).getContent().equals("▓")) {
            finalS = toReplace + "┼";
        } else {
            finalS = toReplace + "<br>" + everLinkedContents.get(position + 1).getContent() + "┼";
        }

        if (stringsList.size() > positionToReplace) {
            stringsList.set(positionToReplace, finalS);
        } else {
            stringsList.add(finalS);
        }

        for (String html : htmls) {
            if (!html.equals(selectedDrawPath)) {
                arrayList.add(html + "┼");
            } else {
                File file = new File(selectedDrawPath);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("Image deleted");
                    }
                }
            }
        }

      //  String joined_arrayString = String.join("", stringsList);
      //  String joined_arrayPath = String.join("", arrayList);
        mainActivity.get().actualNote.get().setDraws(arrayList);
        mainActivity.get().actualNote.get().setContents(stringsList);
      //////////  mainActivity.get().everNoteManagement.updateNote(actualNote.get().getActualPosition(), actualNote.get());
       // EverLinkedMap e = new EverLinkedMap(newString, list.get(position).getDrawLocation(), list.get(position).getRecord());
        updateNoteContent(position, finalS.replaceAll("┼", ""));
        stringsList.clear();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveBitmapFromDraw(boolean fromRecycler) {
        textDrawRecycler.get().suppressLayout(false);
        if (fromRecycler) {
            if (mainActivity.get().drawing) {

                if (!everDraw.get().getMPaths().isEmpty()) {

                    mainActivity.get().everBitmapHelper.UpdateBitmapToFile(mainActivity.get().everBitmapHelper.createBitmapFromView(card), savedBitmapPath);

                    everDraw.get().clearCanvas();

                    FinalYHeight = 0;

                }
                mainActivity.get().CloseOrOpenDraWOptionsFromRecycler(scrollView.get(), textDrawRecycler.get(), true);
            } else {
                onBackPressed();
            }
        } else {

            if (mainActivity.get().drawing) {

                Bitmap bitmap = everDraw.get().getBitmap(Color.WHITE);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), FinalYHeight + 75);

                if (!everDraw.get().getMPaths().isEmpty()) {

                    mainActivity.get().CloseOrOpenDrawOptions(0, true);


                    //WAS POST DELAYED WITH 250 MS
                    new Handler(Looper.getMainLooper()).post(() -> mainActivity.get().everBitmapHelper.TransformBitmapToFile(resizedBitmap));

                    everDraw.get().clearCanvas();

                    FinalYHeight = 0;

                } else {

                    mainActivity.get().CloseOrOpenDrawOptions(0, true);

                    FinalYHeight = 0;
                }
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    bitmap.recycle();
                    resizedBitmap.recycle();
                }, 250);

            } else {
                onBackPressed();
            }
        }
    }



    public void reloadImages() {

        if (mainActivity.get().actualNote.get().getImages().size() > 0) {

            if (new File(mainActivity.get().actualNote.get().getImages().get(0)).exists()) {
                StaggeredGridLayoutManager staggeredGridLayoutManagerImage = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

                recyclerViewImage.get().setLayoutManager(staggeredGridLayoutManagerImage);

                MultiViewAdapter adapter = new MultiViewAdapter();
                listImages = new ListSection<>();

                listImages.addAll(mainActivity.get().actualNote.get().getImages());
                adapter.addSection(listImages);
                adapter.registerItemBinders(new ImagesBinder(mainActivity.get(), 0, false));
                recyclerViewImage.get().setItemAnimator(new LandingAnimator(new EvershootInterpolator(1f)));
                recyclerViewImage.get().setAdapter(new AlphaInAnimationAdapter(adapter));

                recyclerViewImage.get().setVisibility(View.VISIBLE);

            }

        } else {
            recyclerViewImage.get().setVisibility(View.GONE);
        }
        mainActivity.get().imageRecycler = recyclerViewImage;
    }

    public void updateDraw(int p, String drawLocation) {
        mainActivity.get().beginDelayedTransition(cardView.get());
        everLinkedContents.get(p).setDrawLocation(drawLocation);
        adptr.get().notifyItemChanged(p);
    }

    public void updateNoteContent(int p, String content) {
        View currentFocus = mainActivity.get().getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        mainActivity.get().beginDelayedTransition(cardView.get());
        removeNote(p);
        EverLinkedMap e = everLinkedContents.get(p).get();
        e.setContent(content);
        everLinkedContents.set(p, e);
    }

    public void removeNote(int p) {
        everLinkedContents.remove(p);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void removeImage(int p) {
        List<String> toRemove = new ArrayList<>();
        for (String paths : listImages.getData()) {
            if (paths.equals(listImages.get(p))) {
                File file = new File(paths);
                if (file.exists()) {
                    if (file.delete()){
                        System.out.println("Image deleted at " + p);
                    }
                }
            } else {
                toRemove.add(paths + "┼");
            }
        }
        mainActivity.get().everNoteManagement.mDatabaseEver.editImages(String.valueOf(mainActivity.get().actualNote.get().getId()), String.join("", toRemove));
        mainActivity.get().actualNote.get().setImageURLS(String.join("", toRemove));
        listImages.remove(p);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addImage(String path) {
        //  new Handler(Looper.getMainLooper()).post(() -> {
        if (recyclerViewImage.get().getVisibility() == View.GONE) {
            recyclerViewImage.get().setVisibility(View.VISIBLE);
        }
        mainActivity.get().beginDelayedTransition(cardView.get());
        if (listImages == null) {
            listImages = new ListSection<>();
        }
        listImages.add(path);
        if (listImages.size() != 1) {
            recyclerViewImage.get().removeViewAt(listImages.size() - 2);
        }

        //   actualNote.get().setImageURLS(file.toString() + "┼" + imagesUrls.replaceAll("[\\[\\](){}]", ""));
        //  actualNote.get().getImages().add(file.toString());
        mainActivity.get().actualNote.get().setImages(listImages.getData());
        //   actualNote.get().setImageURLS(file.toString() + "┼" + actualNote.get().getImageURLS());
        // actualNote.get().setImageURLS(file.toString() + "┼" + imagesUrls);
        //   noteModelSection.get(position).setImageURLS(file.toString() + "┼" + imagesUrls.replaceAll("[\\[\\](){}]", ""));
        // mDatabaseEver.insertImageToDatabase(String.valueOf(ID), file.toString(), imagesUrls.replaceAll("[\\[\\](){}]", ""));
     //   mainActivity.get().everNoteManagement.updateNote(actualNote.get().getActualPosition(), actualNote.get());

        reloadImages();
        // });

    }
}
