package com.greenledger.app.utils;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class StorageHelper {
    private static StorageHelper instance;
    private final FirebaseStorage storage;

    private StorageHelper() {
        storage = FirebaseStorage.getInstance();
    }

    public static synchronized StorageHelper getInstance() {
        if (instance == null) {
            instance = new StorageHelper();
        }
        return instance;
    }

    public Task<Uri> uploadCropImage(String farmId, String cropId, Uri imageUri) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String path = String.format("crops/%s/%s/images/%s", farmId, cropId, fileName);
        StorageReference ref = storage.getReference().child(path);

        return ref.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() != null) {
                            throw task.getException();
                        }
                    }
                    return ref.getDownloadUrl();
                });
    }

    public void deleteCropImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            storage.getReferenceFromUrl(imageUrl).delete();
        }
    }

    public Task<Uri> uploadFarmImage(String farmId, Uri imageUri) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String path = String.format("farms/%s/images/%s", farmId, fileName);
        StorageReference ref = storage.getReference().child(path);

        return ref.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() != null) {
                            throw task.getException();
                        }
                    }
                    return ref.getDownloadUrl();
                });
    }

    public void deleteFarmImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            storage.getReferenceFromUrl(imageUrl).delete();
        }
    }

    public Task<Uri> uploadLandImage(String farmId, String landId, Uri imageUri) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        String path = String.format("farms/%s/lands/%s/images/%s", farmId, landId, fileName);
        StorageReference ref = storage.getReference().child(path);

        return ref.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() != null) {
                            throw task.getException();
                        }
                    }
                    return ref.getDownloadUrl();
                });
    }

    public void deleteLandImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            storage.getReferenceFromUrl(imageUrl).delete();
        }
    }

    public Task<Uri> uploadProfileImage(String userId, Uri imageUri) {
        String fileName = "profile.jpg";
        String path = String.format("users/%s/profile/%s", userId, fileName);
        StorageReference ref = storage.getReference().child(path);

        return ref.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        if (task.getException() != null) {
                            throw task.getException();
                        }
                    }
                    return ref.getDownloadUrl();
                });
    }

    public void deleteProfileImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            storage.getReferenceFromUrl(imageUrl).delete();
        }
    }
}
