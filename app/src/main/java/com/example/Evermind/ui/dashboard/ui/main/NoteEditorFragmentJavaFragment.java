package com.example.Evermind.ui.dashboard.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.Evermind.ImagesRecyclerGridAdapter;
import com.example.Evermind.MainActivity;
import com.example.Evermind.Note_Model;
import com.example.Evermind.R;
import com.example.Evermind.everUtils.EverNoteCreatorHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class NoteEditorFragmentJavaFragment extends Fragment implements ImagesRecyclerGridAdapter.ItemClickListener {

    public WeakReference<MainActivity> mainActivity;
    public EverNoteCreatorHelper everCreatorHelper;

    public static NoteEditorFragmentJavaFragment newInstance() {
        return new NoteEditorFragmentJavaFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        postponeEnterTransition();

        setSharedElementEnterTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));
        setSharedElementReturnTransition(TransitionInflater.from(requireActivity()).inflateTransition(R.transition.ever_transition));

        return inflater.inflate(R.layout.fragment_note_creator, container, false);


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        everCreatorHelper = new EverNoteCreatorHelper(requireActivity());

        mainActivity = new WeakReference<>(((MainActivity)requireActivity()));

        mainActivity.get().noteCreator = new WeakReference<>(this);

        everCreatorHelper.init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        WeakReference<Note_Model> actualNote = mainActivity.get().actualNote;

        // new Thread(() -> {

        if (mainActivity.get().DrawVisualizerIsShowing) {

            if (resultCode != RESULT_OK) {

                Uri gif = Objects.requireNonNull(data).getData();

                mainActivity.get().everBitmapHelper.SaveImageAndAddToDatabase(gif);
                //reloadImages();
            }
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(Objects.requireNonNull(data));
            if (resultUri != null) {
                mainActivity.get().everBitmapHelper.SaveImageAndAddToDatabase(resultUri);

            }
            // reloadImages();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(Objects.requireNonNull(data));
            Objects.requireNonNull(cropError).printStackTrace();
        } else {
            if (requestCode != RESULT_OK) {

                File directory = mainActivity.get().getDir("imageDir", Context.MODE_PRIVATE);

                Uri uri = Uri.fromFile(new File(directory, "EverImage" + Calendar.getInstance().getTimeInMillis() + ".jpg"));

                if (data != null) {

                    UCrop.Options options = new UCrop.Options();
                    if (!actualNote.get().getNoteColor().equals("16777215")) {
                        options.setToolbarColor(Integer.parseInt(actualNote.get().getNoteColor()));
                        options.setStatusBarColor(Integer.parseInt(actualNote.get().getNoteColor()));
                        options.setCropFrameColor(Integer.parseInt(actualNote.get().getNoteColor()));
                        options.setCropGridCornerColor(Integer.parseInt(actualNote.get().getNoteColor()));
                        options.setActiveControlsWidgetColor(Integer.parseInt(actualNote.get().getNoteColor()));
                    }
                    UCrop.of(Objects.requireNonNull(data.getData()), uri)
                            .withOptions(options)
                            .start(requireActivity(), this);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
        //  }).start();
    }

    public void importerClick(View view) {
        switch (view.getTag().toString()) {
            case "gallery":
                // TODO
                break;

            case "googlePhotos":
                Intent intentGooglePhotos = new Intent();
                intentGooglePhotos.setAction(Intent.ACTION_PICK);
                intentGooglePhotos.setType("image/*");
                intentGooglePhotos.setPackage("com.google.android.apps.photos");
                startActivityForResult(intentGooglePhotos, 101);
                break;

            case "files":
                Intent intentFiles = new Intent();
                intentFiles.setType("image/*");
                intentFiles.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentFiles, "Select Picture"), 101);
                break;
        }

    }

    public void startPostponeTransition() {
        startPostponedEnterTransition();
    }

    @Override
    public void onImageLongPress(View view, int position) {

    }
}
