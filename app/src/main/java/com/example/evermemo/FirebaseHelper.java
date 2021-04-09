package com.example.evermemo;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.evermemo.EverAudioVisualizerHandlers.EverInterfaceHelper;
import com.example.evermemo.everUtils.EverNoteManagement;
import com.example.evermemo.recycler_models.EverLinkedMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FirebaseHelper {

    private final FirebaseFirestore db;
    private final StorageReference storage;
    private final String userPath;
    private final String mainPath;
    private final String DRAW_PATH;
    private final String RECORD_PATH;
    private final String IMAGE_PATH;
    private final int draw = 0;
    private final int record = 1;
    private final int image = 2;
    private final WeakReference<MainActivity> mainActivity;
    private final EverNoteManagement noteManagement;
    StorageReference storageRef;
    StorageReference drawFolder;
    StorageReference recordFolder;
    StorageReference imageFolder;
    private QuerySnapshot notesDb;
    private userSettings uSettings;

    public FirebaseHelper(Context context, FirebaseUser user) {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        mainActivity = new WeakReference<>(((MainActivity) context));
        noteManagement = mainActivity.get().getEverNoteManagement();
        mainPath = "users";
        userPath = "User" + "_" + user.getUid();
        DRAW_PATH = userPath + "/draws/";
        RECORD_PATH = userPath + "/records/";
        IMAGE_PATH = userPath + "/images/";
        storageRef = storage.getStorage().getReference();
        drawFolder = storageRef.child(DRAW_PATH);
        recordFolder = storageRef.child(RECORD_PATH);
        imageFolder = storageRef.child(IMAGE_PATH);
        VerifyIfIsNewUser(user);
    }

    public userSettings getUserSettings() {
        return uSettings;
    }

    public void updateUserSettings() {
        setDocument("", uSettings, null, null, null);
    }

    public void setIsGrid(boolean isGrid) {
        uSettings.setGrid(isGrid);
        updateUserSettings();
    }

    public void setNoteCount(int count) {
        uSettings.setNoteCount(count);
        updateUserSettings();
    }

    public void setDarkMode(boolean darkMode) {
        uSettings.setDarkMode(darkMode);
        updateUserSettings();
    }

    public void VerifyIfIsNewUser(FirebaseUser user) {
        getDocument()
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!Objects.requireNonNull(task.getResult()).getData().containsKey("name")) {
                            HashMap<String, Object> userData = new HashMap<>();
                            userData.put("name", user.getDisplayName());
                            userData.put("last_log_in", Calendar.getInstance().getTime());
                            userData.put("note_count", 0);
                            userData.put("isGrid", false);
                            userData.put("darMode", false);

                            setDocument("", userData, null, null, null);
                        } else {
                            uSettings = task.getResult().toObject(userSettings.class);
                            if (uSettings != null) {
                                uSettings.setDate(Calendar.getInstance().getTime().toString());
                            }
                        }


                        getAllNotesFromDatabase();
                    }
                });
    }

    private int getLastNoteName() {
        int size;
        if (noteManagement.getNoteModelSection().size() > 1) {
            size = Integer.parseInt(noteManagement.getNoteModelSection().get(0).getNote_name().substring(5));
        } else {
            size = uSettings.getNoteCount();
        }
        return size;
    }

    public void addNoteToFirebase() {
        int finalSize = getLastNoteName() + 1;
        Note_Model note = createNoteDocument(true, "note" + "_" + finalSize, 0, "", -1, new ArrayList<>());
        mainActivity.get().setActualNote(note);
        note.setActualPosition(noteManagement.getNoteModelSection().size());
        setDocumentCollection("notes", "note" + "_" + finalSize, note, new Runnable() {
            @Override
            public void run() {
                noteManagement.addNote(note);
                updateNoteCount();
            }
        }, null, null);
    }


    public void updateNoteCount() {
        setNoteCount(noteManagement.getNoteModelSection().size());
    }

    public Note_Model createNoteDocument(boolean empty, String name, int position, String title, int color, List<EverLinkedMap> contentData) {
        Note_Model note;
        if (empty) {

            List<EverLinkedMap> aaa = new ArrayList<>();
            aaa.add(new EverLinkedMap("<br>", "▓", "▓", -1));

            note = new Note_Model(
                    name,
                    position,
                    "",
                    Calendar.getInstance().getTime().toString(),
                    "-1",
                    aaa
            );
        } else {
            note = new Note_Model(
                    name,
                    position,
                    title,
                    Calendar.getInstance().getTime().toString(),
                    String.valueOf(color),
                    contentData
            );
        }
        return note;
    }

    public void getAllNotesFromDatabase() {

        getDocument().collection("notes").get().addOnCompleteListener(task -> {
            notesDb = task.getResult();
            noteManagement.getNoteModelSection().clear();
            int p = 0;
            QuerySnapshot documents = task.getResult();
            for (int i = documents.size() - 1; i > -1; i--) {
                DocumentSnapshot document = documents.getDocuments().get(i);
                Note_Model note = transformToNoteModel(document);
                note.setActualPosition(p);
                noteManagement.getNoteModelSection().add(note);
                p++;
            }


            mainActivity.get().getNoteScreen().get().init(noteManagement, this);
            setNoteCount(noteManagement.getNoteModelSection().size());
            //    mainActivity.get().getNoteScreen().get().getAdapter().updateNotesAdapter( noteManagement.getNoteModelSection());

        });
    }

    public CollectionReference getCollection() {
        return db.collection(mainPath);
    }

    public DocumentReference getDocument() {
        return db.collection(mainPath).document(userPath);
    }

    public void setDocument(String path, Object data, Runnable success, Runnable fail, Runnable complete) {
        getDocument().set(data)
                .addOnSuccessListener(unused -> {
                    if (success != null)
                        success.run();
                })
                .addOnFailureListener(e -> {
                    if (fail != null)
                        fail.run();
                })
                .addOnCompleteListener(task -> {
                    if (complete != null)
                        complete.run();
                });
    }

    public void setDocumentCollection(String path, String name, Object data, Runnable success, Runnable fail, Runnable complete) {
        getDocument().collection(path).document(name).set(data)
                .addOnSuccessListener(unused -> {
                    if (success != null)
                        success.run();
                })
                .addOnFailureListener(e -> {
                    if (fail != null)
                        fail.run();
                })
                .addOnCompleteListener(task -> {
                    if (complete != null)
                        complete.run();
                });
    }

    public void getDocument(String path, Runnable success, Runnable fail, Runnable complete) {
        db.collection(userPath).document(path).get()
                .addOnSuccessListener(unused -> {
                    if (success != null)
                        success.run();
                })
                .addOnFailureListener(e -> {
                    if (fail != null)
                        fail.run();
                })
                .addOnCompleteListener(task -> {
                    if (complete != null)
                        complete.run();
                });
    }

    public void getCollection(Runnable success, Runnable fail, Runnable complete) {
        db.collection(userPath).get()
                .addOnSuccessListener(unused -> {
                    if (success != null)
                        success.run();
                })
                .addOnFailureListener(e -> {
                    if (fail != null)
                        fail.run();
                })
                .addOnCompleteListener(task -> {
                    if (complete != null)
                        complete.run();
                });
    }

    public String getUserPath() {
        return userPath;
    }

    public StorageReference getStorage(int type) {
        StorageReference storageReference = null;
        switch (type) {
            case draw:
                storageReference = drawFolder;
                break;
            case record:
                storageReference = recordFolder;
                break;
            case image:
                storageReference = imageFolder;
                break;
        }
        return storageReference;
    }

    public void getFileFromFirebase(String path, int type, int position) {
        if (path.length() > 1) {
            StorageReference storageReference = getStorage(type);

            StorageReference fileRef = storageReference.child(path);
            File localFile = new File(mainActivity.get().getLocalFileDirectory() + path);
            fileRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                mainActivity.get().sendAlert("File Downloaded", "File: " + localFile.getName() + "downloaded successfully.", ContextCompat.getDrawable(mainActivity.get(), R.drawable.success_circle));
                EverInterfaceHelper.getInstance().sendDownloadToListeners(localFile, position, path);
            }).addOnFailureListener(e -> mainActivity.get().sendAlert("Download failed", "File: " + localFile.getName() + "downloaded failed.", ContextCompat.getDrawable(mainActivity.get(), R.drawable.error_center_x)));
        }
    }

    public String putFile(File file, int type, int position, Runnable success, Runnable fail) {
        StorageReference storageReference = null;
        switch (type) {
            case draw:
                storageReference = drawFolder;
                storageReference = storageReference.child(file.getName());
                break;
            case record:
                storageReference = recordFolder;
                storageReference = storageReference.child(file.getName());
                break;
            case image:
                storageReference = imageFolder;
                storageReference = storageReference.child(file.getName());
                break;
        }

        if (storageReference != null) {
            storageReference.putFile(Uri.fromFile(file)).addOnSuccessListener(taskSnapshot -> {
                if (success != null)
                    success.run();
                mainActivity.get().sendAlert("New File Added", "File: " + file.getName() + "added successfully.", ContextCompat.getDrawable(mainActivity.get(), R.drawable.success_circle));
                //  getFileFromFirebase(file.getName(), 0, position);
            }).addOnFailureListener(e -> {
                if (fail != null)
                    fail.run();
                mainActivity.get().sendAlert("New File Failed", "File: " + file.getName() + "failed while saving.", ContextCompat.getDrawable(mainActivity.get(), R.drawable.error_center_x));
            });
        }

        return Objects.requireNonNull(storageReference).getPath();
    }

    public boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public Note_Model transformToNoteModel(DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(Note_Model.class);
    }

    public void deleteNote(Note_Model note) {
        getDocument().collection("notes").document(note.getNote_name()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                updateNoteCount();
                mainActivity.get().sendAlert(note.getNote_name(), "Deleted successfully.", ContextCompat.getDrawable(mainActivity.get(), R.drawable.success_circle));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mainActivity.get().sendAlert(note.getNote_name(), "Error while deleting note. Maybe wrong path.", ContextCompat.getDrawable(mainActivity.get(), R.drawable.error_center_x));
            }
        });
    }

    public void deleteFile(File file, int type) {
        StorageReference storageRef = storage.getStorage().getReference();
        switch (type) {
            case draw:
                storageRef = drawFolder;
                storageRef = storageRef.child(file.getName());
                break;
            case record:
                storageRef = recordFolder;
                storageRef = storageRef.child(file.getName());
                break;
            case image:
                storageRef = imageFolder;
                storageRef = storageRef.child(file.getName());
                break;
        }

        storageRef.delete().addOnSuccessListener(aVoid -> mainActivity.get().sendAlert("File", "Deleted successfully.", ContextCompat.getDrawable(mainActivity.get(), R.drawable.success_circle))).addOnFailureListener(exception -> mainActivity.get().sendAlert("File", "Error. File is still there. Maybe wrong path.", ContextCompat.getDrawable(mainActivity.get(), R.drawable.error_center_x)));

    }
}
