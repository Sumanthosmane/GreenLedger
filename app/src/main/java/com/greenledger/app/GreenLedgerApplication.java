package com.greenledger.app;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class GreenLedgerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }
}
