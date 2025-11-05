package com.greenledger.app.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.greenledger.app.database.GreenLedgerDatabase;
import com.greenledger.app.database.entities.FarmEntity;
import com.greenledger.app.database.entities.ScheduleEntity;
import com.greenledger.app.database.entities.BusinessPartnerEntity;
import com.greenledger.app.firebase.FirebaseHelper;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SyncManager {
    private static final String TAG = "SyncManager";
    private static volatile SyncManager INSTANCE;
    private final Context context;
    private final GreenLedgerDatabase database;
    private final FirebaseHelper firebaseHelper;
    private final Executor executor;
    private final Gson gson;

    private SyncManager(Context context) {
        this.context = context.getApplicationContext();
        this.database = GreenLedgerDatabase.getInstance(context);
        this.firebaseHelper = FirebaseHelper.getInstance();
        this.executor = Executors.newSingleThreadExecutor();
        this.gson = new Gson();
    }

    public static SyncManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SyncManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SyncManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public void syncData() {
        if (!isNetworkAvailable()) {
            Log.d(TAG, "No network connection available. Skipping sync.");
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        syncPendingData();
        syncFarms(userId);
        syncSchedules(userId);
        syncBusinessPartners(userId);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void syncPendingData() {
        executor.execute(() -> {
            // Sync pending farms
            List<FarmEntity> pendingFarms = database.farmDao().getPendingSyncFarms();
            for (FarmEntity farm : pendingFarms) {
                switch (farm.getSyncStatus()) {
                    case 1: // Pending Upload
                        firebaseHelper.getFarmsRef().child(farm.getFarmId()).setValue(gson.fromJson(farm.getFarmDetails(), Map.class));
                        break;
                    case 2: // Pending Update
                        firebaseHelper.getFarmsRef().child(farm.getFarmId()).updateChildren(gson.fromJson(farm.getFarmDetails(), Map.class));
                        break;
                    case 3: // Pending Delete
                        firebaseHelper.getFarmsRef().child(farm.getFarmId()).removeValue();
                        break;
                }
                database.farmDao().updateSyncStatus(farm.getFarmId(), 0);
            }

            // Sync pending schedules
            List<ScheduleEntity> pendingSchedules = database.scheduleDao().getPendingSyncSchedules();
            for (ScheduleEntity schedule : pendingSchedules) {
                switch (schedule.getSyncStatus()) {
                    case 1:
                        firebaseHelper.getSchedulesRef().child(schedule.getScheduleId()).setValue(gson.fromJson(schedule.getScheduleInfo(), Object.class));
                        break;
                    case 2:
                        firebaseHelper.getSchedulesRef().child(schedule.getScheduleId()).updateChildren(gson.fromJson(schedule.getScheduleInfo(), Map.class));
                        break;
                    case 3:
                        firebaseHelper.getSchedulesRef().child(schedule.getScheduleId()).removeValue();
                        break;
                }
                database.scheduleDao().updateSyncStatus(schedule.getScheduleId(), 0);
            }

            // Sync pending business partners
            List<BusinessPartnerEntity> pendingPartners = database.businessPartnerDao().getPendingSyncPartners();
            for (BusinessPartnerEntity partner : pendingPartners) {
                switch (partner.getSyncStatus()) {
                    case 1:
                        firebaseHelper.getBusinessPartnersRef().child(partner.getPartnerId()).setValue(gson.fromJson(partner.getPartnerInfo(), Object.class));
                        break;
                    case 2:
                        firebaseHelper.getBusinessPartnersRef().child(partner.getPartnerId()).updateChildren(gson.fromJson(partner.getPartnerInfo(), Map.class));
                        break;
                    case 3:
                        firebaseHelper.getBusinessPartnersRef().child(partner.getPartnerId()).removeValue();
                        break;
                }
                database.businessPartnerDao().updateSyncStatus(partner.getPartnerId(), 0);
            }
        });
    }

    private void syncFarms(String userId) {
        firebaseHelper.getFarmsRef().orderByChild("farmerId").equalTo(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    executor.execute(() -> {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            FarmEntity farm = new FarmEntity();
                            farm.setFarmId(snapshot.getKey());
                            farm.setFarmerId(userId);
                            farm.setFarmDetails(gson.toJson(snapshot.getValue()));
                            farm.setLastSync(System.currentTimeMillis());
                            farm.setSyncStatus(0);
                            database.farmDao().insert(farm);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Failed to sync farms: " + databaseError.getMessage());
                }
            });
    }

    private void syncSchedules(String userId) {
        firebaseHelper.getSchedulesRef().orderByChild("farmerId").equalTo(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    executor.execute(() -> {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ScheduleEntity schedule = new ScheduleEntity();
                            schedule.setScheduleId(snapshot.getKey());
                            schedule.setFarmerId(userId);
                            schedule.setScheduleInfo(gson.toJson(snapshot.getValue()));
                            schedule.setLastSync(System.currentTimeMillis());
                            schedule.setSyncStatus(0);
                            database.scheduleDao().insert(schedule);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Failed to sync schedules: " + databaseError.getMessage());
                }
            });
    }

    private void syncBusinessPartners(String userId) {
        firebaseHelper.getBusinessPartnersRef().orderByChild("farmerId").equalTo(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    executor.execute(() -> {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            BusinessPartnerEntity partner = new BusinessPartnerEntity();
                            partner.setPartnerId(snapshot.getKey());
                            partner.setFarmerId(userId);
                            partner.setPartnerInfo(gson.toJson(snapshot.getValue()));
                            partner.setLastSync(System.currentTimeMillis());
                            partner.setSyncStatus(0);
                            database.businessPartnerDao().insert(partner);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Failed to sync business partners: " + databaseError.getMessage());
                }
            });
    }
}
