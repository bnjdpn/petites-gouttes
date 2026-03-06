package com.bnjdpn.petitesgouttes.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MilkBagDao {

    @Query("SELECT * FROM milk_bag WHERE removedFromFreezer = 0 ORDER BY pumpDate ASC")
    fun getActiveBags(): Flow<List<MilkBagEntity>>

    @Query("SELECT * FROM milk_bag WHERE removedFromFreezer = 1 ORDER BY removalDate DESC")
    fun getRemovedBags(): Flow<List<MilkBagEntity>>

    @Query("SELECT * FROM milk_bag ORDER BY pumpDate ASC")
    fun getAllBags(): Flow<List<MilkBagEntity>>

    @Query("SELECT * FROM milk_bag WHERE removedFromFreezer = 0 ORDER BY pumpDate ASC LIMIT 1")
    fun getNextBagToUse(): Flow<MilkBagEntity?>

    @Query("SELECT * FROM milk_bag WHERE id = :id")
    suspend fun getBagById(id: Long): MilkBagEntity?

    @Query("SELECT SUM(volumeMl) FROM milk_bag WHERE removedFromFreezer = 0")
    fun getTotalActiveVolume(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM milk_bag WHERE removedFromFreezer = 0")
    fun getActiveBagCount(): Flow<Int>

    @Query("SELECT * FROM milk_bag WHERE removedFromFreezer = 0 AND expiryDate < :thresholdDate ORDER BY expiryDate ASC")
    fun getBagsExpiringSoon(thresholdDate: Long): Flow<List<MilkBagEntity>>

    @Query("SELECT * FROM milk_bag WHERE pumpDate >= :startDate AND pumpDate < :endDate ORDER BY pumpDate ASC")
    fun getBagsByPumpDateRange(startDate: Long, endDate: Long): Flow<List<MilkBagEntity>>

    @Insert
    suspend fun insert(bag: MilkBagEntity): Long

    @Update
    suspend fun update(bag: MilkBagEntity)

    @Delete
    suspend fun delete(bag: MilkBagEntity)

    @Query("UPDATE milk_bag SET removedFromFreezer = 1, removalDate = :removalDate WHERE id = :id")
    suspend fun markAsRemoved(id: Long, removalDate: Long)

    @Query("UPDATE milk_bag SET removedFromFreezer = 0, removalDate = NULL WHERE id = :id")
    suspend fun markAsActive(id: Long)
}
