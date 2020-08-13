package com.example.Evermind;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.koushikdutta.ion.Ion;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import pl.droidsonroids.gif.GifImageView;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ImagesRecyclerNoteScreenGridAdapter extends RecyclerView.Adapter<ImagesRecyclerNoteScreenGridAdapter.ViewHolder>  {

    private String[] mImageURLs;
    private Integer mID;
    private int count;
    private String[] SplittedURLs;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;
    private Context context;

    public static ArrayList<Uri> uris = new ArrayList<>();
    public static Uri[] uri;


    // data is passed into the constructor
    public ImagesRecyclerNoteScreenGridAdapter(Context context, String ImageURLs, Integer ID, int countURLs) {

        new Thread(() -> {

        SplittedURLs = ImageURLs.replaceAll("[\\[\\](){}]","").trim().split("┼");

            this.mInflater = LayoutInflater.from(context);
            this.mImageURLs = SplittedURLs;
            this.mID = ID;
            this.count = countURLs;
            this.context = context;

        }).start();
    }


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.images_imagerecycler_notescreen, parent, false);


        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {


        if (position == 0 && getItemCount() <= 1) {
            Ion.with(holder.myImageView)
                    .error(R.drawable.ic_baseline_clear_24)
                    .animateLoad(R.anim.grid_new_item_anim)
                    .animateIn(R.anim.grid_new_item_anim)
                    .smartSize(true)
                    .centerCrop()
                    .load(mImageURLs[position]); // was position

            ViewGroup.LayoutParams params = holder.myImageView.getLayoutParams();

            params.height = 600;
            params.width = MATCH_PARENT;

            holder.myImageView.setLayoutParams(params);
        }


        Ion.with(holder.myImageView)
                .error(R.drawable.ic_baseline_clear_24)
                .animateLoad(R.anim.grid_new_item_anim)
                .animateIn(R.anim.grid_new_item_anim)
                .smartSize(true)
                .centerCrop()
                .load(mImageURLs[position]); // was position



        }





    // total number of cells
    @Override
    public int getItemCount() {
        return count;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void setClickListener(AdapterView.OnItemClickListener onItemClickListener) {
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        GifImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);



                myImageView = itemView.findViewById(R.id.recyclerImage);
                itemView.setOnClickListener(this);

                //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            myImageView.setOnClickListener(view -> {

                new Thread(() -> {

                for (String item: mImageURLs) {

                    File file = new File(item);
                    uris.add(Uri.fromFile(file));

                }

                uri = uris.toArray(new Uri[0]);

                    new Handler(Looper.getMainLooper()).post(() -> {

                        new ImageViewer.Builder(context, uri)
                                .setStartPosition(getLayoutPosition())
                                .show();
                    });

                uris.clear();

            }).start();

            });


           // myImageView.setOnClickListener(this);

            myImageView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)
                    mClickListener.onLongPress(view, p);
                System.out.println(p);
                return false;
            });

                ////TODO/////////////// /\ /\ /\ /\

            itemView.setOnLongClickListener(view -> {
                int p = getLayoutPosition();

                if (mClickListener != null)
                    mClickListener.onLongPress(view, p);
                System.out.println(p);

                return true;// returning true instead of false, works for me
            });
        }
        ///////////////////////

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

    }



    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

   public void setOnLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mLongClick = onItemLongClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onClick(View view);

        void onLongPress(View view, int position);
    }

}
