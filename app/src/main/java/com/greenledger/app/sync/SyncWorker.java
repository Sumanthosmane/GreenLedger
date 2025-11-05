package com.greenledger.app.sync;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import java.util.concurrent.TimeUnit;

public class SyncWorker extends Worker {
    private static final String SYNC_WORK_NAME = "data_sync_work";

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        SyncManager.getInstance(getApplicationContext()).syncData();
        return Result.success();
    }

    public static void scheduleSyncWork(Context context) {
        // Create network constraints
        Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

        // Create periodic work request (minimum interval is 15 minutes)
        PeriodicWorkRequest syncWorkRequest = new PeriodicWorkRequest.Builder(
                SyncWorker.class,
                15,
                TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build();

        // Enqueue the work request
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                SYNC_WORK_NAME,
                androidx.work.ExistingPeriodicWorkPolicy.REPLACE,
                syncWorkRequest
            );
    }
}
