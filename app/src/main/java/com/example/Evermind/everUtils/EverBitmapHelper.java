package com.example.Evermind.everUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.Evermind.MainActivity;
import com.example.Evermind.Note_Model;
import com.example.Evermind.recycler_models.EverLinkedMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import mva2.adapter.ListSection;

public class EverBitmapHelper {

    private final WeakReference<MainActivity> mainActivity;

    public EverBitmapHelper(Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
    }

    public @NonNull
    Bitmap createBitmapFromView(@NonNull View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Drawable background = view.getBackground();

        if (background != null) {
            background.draw(canvas);
        }
        view.draw(canvas);

        return bitmap;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void TransformBitmapToFile(Bitmap bitmap) {
        File file = CreateBitmap(bitmap, "EverDraw"+ System.currentTimeMillis(), ".jpg");
        mainActivity.get().actualNote.get().addDraw(file.toString(), mainActivity.get());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void UpdateBitmapToFile(Bitmap bitmap, String savedBitmapPath) {

        File toDelete = new File(savedBitmapPath);
        if (toDelete.exists()) {
            if (toDelete.delete()) {
                System.out.println("Old draw deleted when updating.");
            }
        }

        String s = savedBitmapPath.substring(0, savedBitmapPath.length() - 17);

        List<String> strings = mainActivity.get().actualNote.get().getDraws();

        strings.set(strings.indexOf(savedBitmapPath), UpdateFile(bitmap, s).toString());

        for (int p = 0; p < strings.size(); p++) {
            if (!strings.get(p).equals("")) {
                strings.set(p, strings.get(p) + "┼");
            }
        }
        String editDraw = strings.toString().replaceAll("[\\[\\](){}]", "").replaceAll(",", "").replaceAll(" ", "");
        mainActivity.get().actualNote.get().setDrawLocation(editDraw);
      //  mainActivity.get().everNoteManagement.updateNote(actualNote.get().getActualPosition(), actualNote.get());
        int drawPosition = mainActivity.get().noteCreator.get().everCreatorHelper.drawPosition;
        mainActivity.get().noteCreator.get().everCreatorHelper.updateDraw(drawPosition, mainActivity.get().actualNote.get().getDraws().get(drawPosition));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveImageAndAddToDatabase(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mainActivity.get().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file =  CreateBitmap(bitmap, "EverImage", ".jpg");
       if (file.exists()) {
           mainActivity.get().noteCreator.get().everCreatorHelper.addImage(file.toString());
       }
    }

    public File CreateBitmap(Bitmap bitmap, String fileName, String fileType) {

        File directory = mainActivity.get().getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, fileName + Calendar.getInstance().getTimeInMillis() + fileType);

        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(bitmap).compress(Bitmap.CompressFormat.JPEG, 100, Objects.requireNonNull(fos));
            try {
                Objects.requireNonNull(fos).flush();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmap.recycle();
        return file;
    }

    public File UpdateFile(Bitmap bitmap, String path) {
        File file = new File(path + System.currentTimeMillis() + ".jpg");

        Log.d("path", file.toString());
        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
