package com.greenledger.app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.greenledger.app.database.entities.ScheduleEntity;
import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ScheduleEntity schedule);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ScheduleEntity> schedules);

    @Update
    void update(ScheduleEntity schedule);

    @Delete
    void delete(ScheduleEntity schedule);

    @Query("SELECT * FROM schedules WHERE scheduleId = :scheduleId")
    ScheduleEntity getScheduleById(String scheduleId);

    @Query("SELECT * FROM schedules WHERE farmerId = :farmerId")
    List<ScheduleEntity> getSchedulesByFarmerId(String farmerId);

    @Query("SELECT * FROM schedules WHERE farmId = :farmId")
    List<ScheduleEntity> getSchedulesByFarmId(String farmId);

    @Query("DELETE FROM schedules")
    void deleteAll();

    @Query("SELECT * FROM schedules WHERE syncStatus != 0")
    List<ScheduleEntity> getPendingSyncSchedules();

    @Query("UPDATE schedules SET syncStatus = :status WHERE scheduleId = :scheduleId")
    void updateSyncStatus(String scheduleId, int status);

    @Query("SELECT * FROM schedules WHERE lastSync < :timestamp")
    List<ScheduleEntity> getSchedulesToSync(long timestamp);
}
