package com.greenledger.app.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private final DatabaseReference database;

    private FirebaseHelper() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public DatabaseReference getSchedulesRef() {
        return database.child("schedules");
    }

    public DatabaseReference getBusinessPartnersRef() {
        return database.child("businessPartners");
    }

    public DatabaseReference getFarmsRef() {
        return database.child("farms");
    }

    public DatabaseReference getCropsRef() {
        return database.child("crops");
    }

    public DatabaseReference getStorageRef() {
        return database.child("storage");
    }

    // Other reference methods...
}
