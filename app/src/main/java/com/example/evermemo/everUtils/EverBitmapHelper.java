package com.example.evermemo.everUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.evermemo.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class EverBitmapHelper {

    @NonNull
    private final WeakReference<MainActivity> mainActivity;

    public EverBitmapHelper(Context context) {
        mainActivity = new WeakReference<>(((MainActivity) context));
    }

    public @NonNull
    Bitmap createBitmapFromView(@NonNull View view, int size) {
        int finalSize = size + 100;
        if (size == 0 || size < view.getHeight() && !mainActivity.get().getNoteCreator().isNewDraw()) {
            finalSize = view.getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                finalSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void TransformBitmapToFile(@NonNull Bitmap bitmap) {
        File file = CreateBitmap(bitmap, "EverDraw" + System.currentTimeMillis(), ".jpg");
        mainActivity.get().getActualNote().addEverLinkedMap(file.toString(), mainActivity.get(), true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void UpdateBitmapToFile(@NonNull Bitmap bitmap, @NonNull int position) {
        String path = "";
        if (mainActivity.get().getActualNote().getEverLinkedContents(true).size() > position) {
            path = mainActivity.get().getActualNote().getEverLinkedContents(false).get(position).getDrawLocation();
        }
        File toDelete = new File(path);
        if (toDelete.exists()) {
            if (toDelete.delete()) {
                mainActivity.get().getFirebaseHelper().deleteFile(toDelete, 0);
                System.out.println("Old draw deleted when updating.");
                //   mainActivity.get().clearIonCache(toDelete.getPath());
            }
        }

        String fileName = "<>" +
                bitmap.getHeight() +
                "<>" +
                bitmap.getWidth() +
                "<>" +
                mainActivity.get().getActualNote().getNote_name() +
                "_" +
                mainActivity.get().getActualNote().getEverLinkedContents(false).size() +
                "_" + System.currentTimeMillis();


        String finalPath = mainActivity.get().getLocalFileDirectory() + fileName;


        File withXY = UpdateFile(bitmap, finalPath);


        //  File file = UpdateFile(bitmap, finalPath);

        mainActivity.get().getFirebaseHelper().putFile(withXY, 0, position, null, null);
        mainActivity.get().getActualNote().getEverLinkedContents(false).get(position).setDrawLocation(withXY.getPath());
        mainActivity.get().getNoteCreator().updateDrawAtPosition(position, mainActivity.get().getActualNote().getEverLinkedContents(false).get(position).getDrawLocation());
        //  mainActivity.get().everNoteManagement.updateNote(actualNote.get().getActualPosition(), actualNote.get());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveImageAndAddToDatabase(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mainActivity.get().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = CreateBitmap(bitmap, "EverImage", ".jpg");
        if (file.exists()) {
            mainActivity.get().getNoteCreator().addImage(file.toString());
        }
    }

    @NonNull
    public File CreateBitmap(@NonNull Bitmap bitmap, String fileName, String fileType) {

        File directory = mainActivity.get().getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, fileName + System.currentTimeMillis() + fileType);

        if (!file.exists()) {
            Log.d("path", file.toString());
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, Objects.requireNonNull(fos));
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

    @NonNull
    public File UpdateFile(@NonNull Bitmap bitmap, String path) {
        String format = "";
        if (!path.endsWith(".jpg")) {
            format = ".jpg";
        }
        File file = new File(path + format);

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
