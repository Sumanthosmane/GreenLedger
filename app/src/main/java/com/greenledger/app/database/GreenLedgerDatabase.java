package com.greenledger.app.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.greenledger.app.database.dao.UserDao;
import com.greenledger.app.database.dao.FarmDao;
import com.greenledger.app.database.dao.ScheduleDao;
import com.greenledger.app.database.dao.BusinessPartnerDao;
import com.greenledger.app.database.entities.UserEntity;
import com.greenledger.app.database.entities.FarmEntity;
import com.greenledger.app.database.entities.ScheduleEntity;
import com.greenledger.app.database.entities.BusinessPartnerEntity;

@Database(
    entities = {
        UserEntity.class,
        FarmEntity.class,
        ScheduleEntity.class,
        BusinessPartnerEntity.class
    },
    version = 1,
    exportSchema = false
)
public abstract class GreenLedgerDatabase extends RoomDatabase {
    private static volatile GreenLedgerDatabase INSTANCE;
    private static final String DATABASE_NAME = "greenledger_db";

    public abstract UserDao userDao();
    public abstract FarmDao farmDao();
    public abstract ScheduleDao scheduleDao();
    public abstract BusinessPartnerDao businessPartnerDao();

    public static GreenLedgerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GreenLedgerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            GreenLedgerDatabase.class,
                            DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}
