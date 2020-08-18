package com.example.Evermind;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TextAndDrawRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] mData;
    private String[] mBitmap;
    public static Integer[] id;
    public Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private AdapterView.OnItemLongClickListener mLongClick;

    private String[] SplittedURLs;

    private String[] SplittedFiles;

    private EvermindEditor editorContent;
    private ImageView imageButton;

    public TextAndDrawRecyclerAdapter(Context context, String content, String files) {

        SplittedURLs = content.replaceAll("[\\[\\](){}]", "").trim().split("┼");


        SplittedFiles = files.replaceAll("[\\[\\](){}]", "").trim().split("┼");

        this.mInflater = LayoutInflater.from(context);
        this.mData = SplittedURLs;
        this.mBitmap = SplittedFiles;
        this.context = context;

    }


    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;

            if (viewType == 1) {
                view = LayoutInflater.from(context).inflate(R.layout.recyclerview_editor_layout, viewGroup, false);
                return new EditorViewHolder(view);

            } else {
                    view = LayoutInflater.from(context).inflate(R.layout.recyclerview_image_layout, viewGroup, false);
                    return new DrawViewHolder(view);
            }
        }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        System.out.println("Type = " + position + " Item Count = " + getItemCount());

            if (getItemViewType(position) == 1) {

                if (position == mData.length && position != 0) {

                    setEditorText("");

                } else {

                    if (mData.length > 0 ) {

                        setEditorText(mData[position]);
                    }
                }
        }

                if (getItemViewType(position) == 2) {

                    if (position == mBitmap.length && position != 0) {

                        //Bitmap bitmap = BitmapFactory.decodeFile(mBitmap[position - 1]);

                        //setDrawImage(bitmap);

                    } else {

                        if (mBitmap.length > 0) {
                            Bitmap bitmap = BitmapFactory.decodeFile(mBitmap[position]);

                            setDrawImage(bitmap);
                        }

                    }
                }
            }

    private void setEditorText(String html) {
        editorContent.setHtml(html);
        editorContent.setEditorHeight(20);
        editorContent.setEditorFontSize(22);
        editorContent.setBackgroundColor(Color.TRANSPARENT);
        editorContent.setPadding(15, 15, 15, 15);
    }
    private void setDrawImage(Bitmap bitmap) {
        imageButton.setImageBitmap(bitmap);
    }

    @Override
    public int getItemViewType(int position) {

            if (position % 2 == 0) {

                return 1;

            } else {

                return 2;

            }
    }

    @Override
    public int getItemCount() {
        return mData.length + mBitmap.length;
    }


    public void setClickListener(AdapterView.OnItemClickListener onItemClickListener) {
    }

    class EditorViewHolder extends RecyclerView.ViewHolder {

        EditorViewHolder(@NonNull View itemView) {
            super(itemView);
            editorContent = itemView.findViewById(R.id.recycler_everEditor);
        }
    }
    class DrawViewHolder extends RecyclerView.ViewHolder {


        DrawViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.recycler_imageView);
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EvermindEditor myEditor;
        ImageView myImageView;



        ViewHolder(View itemView) {
            super(itemView);


            myImageView = itemView.findViewById(R.id.info_text);
                myEditor = itemView.findViewById(R.id.info_title);
                itemView.setOnClickListener(this);

                //TODO IMPORTANT CODE \/ \/ \/ \/ \/

            myImageView.setOnClickListener(this);

            myImageView.setOnLongClickListener(view -> {
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

                return true;
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public String getItem(int id) {
        return mData[id];
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

   public void setOnLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mLongClick = onItemLongClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onClick(View view);

        void onLongPress(View view, int position);
    }
}
