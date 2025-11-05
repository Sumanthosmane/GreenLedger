package com.greenledger.app.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.greenledger.app.database.entities.BusinessPartnerEntity;
import java.util.List;

@Dao
public interface BusinessPartnerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BusinessPartnerEntity partner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BusinessPartnerEntity> partners);

    @Update
    void update(BusinessPartnerEntity partner);

    @Delete
    void delete(BusinessPartnerEntity partner);

    @Query("SELECT * FROM business_partners WHERE partnerId = :partnerId")
    BusinessPartnerEntity getPartnerById(String partnerId);

    @Query("SELECT * FROM business_partners WHERE farmerId = :farmerId")
    List<BusinessPartnerEntity> getPartnersByFarmerId(String farmerId);

    @Query("DELETE FROM business_partners")
    void deleteAll();

    @Query("SELECT * FROM business_partners WHERE syncStatus != 0")
    List<BusinessPartnerEntity> getPendingSyncPartners();

    @Query("UPDATE business_partners SET syncStatus = :status WHERE partnerId = :partnerId")
    void updateSyncStatus(String partnerId, int status);

    @Query("SELECT * FROM business_partners WHERE lastSync < :timestamp")
    List<BusinessPartnerEntity> getPartnersToSync(long timestamp);
}
