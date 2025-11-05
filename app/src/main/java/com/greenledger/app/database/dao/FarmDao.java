package com.greenledger.app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.greenledger.app.database.entities.FarmEntity;
import java.util.List;

@Dao
public interface FarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FarmEntity farm);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FarmEntity> farms);

    @Update
    void update(FarmEntity farm);

    @Delete
    void delete(FarmEntity farm);

    @Query("SELECT * FROM farms WHERE farmId = :farmId")
    FarmEntity getFarmById(String farmId);

    @Query("SELECT * FROM farms WHERE farmerId = :farmerId")
    List<FarmEntity> getFarmsByFarmerId(String farmerId);

    @Query("DELETE FROM farms")
    void deleteAll();

    @Query("SELECT * FROM farms WHERE syncStatus != 0")
    List<FarmEntity> getPendingSyncFarms();

    @Query("UPDATE farms SET syncStatus = :status WHERE farmId = :farmId")
    void updateSyncStatus(String farmId, int status);

    @Query("SELECT * FROM farms WHERE lastSync < :timestamp")
    List<FarmEntity> getFarmsToSync(long timestamp);
}
