package com.bnjdpn.petitesgouttes.data.repository

import com.bnjdpn.petitesgouttes.data.database.MilkBagDao
import com.bnjdpn.petitesgouttes.data.database.MilkBagEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class MilkBagRepository(private val dao: MilkBagDao) {

    val activeBags: Flow<List<MilkBagEntity>> = dao.getActiveBags()
    val removedBags: Flow<List<MilkBagEntity>> = dao.getRemovedBags()
    val allBags: Flow<List<MilkBagEntity>> = dao.getAllBags()
    val nextBagToUse: Flow<MilkBagEntity?> = dao.getNextBagToUse()
    val totalActiveVolume: Flow<Int?> = dao.getTotalActiveVolume()
    val activeBagCount: Flow<Int> = dao.getActiveBagCount()

    fun getBagsExpiringSoon(withinDays: Int = 14): Flow<List<MilkBagEntity>> {
        val threshold = LocalDate.now().plusDays(withinDays.toLong())
            .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return dao.getBagsExpiringSoon(threshold)
    }

    fun getBagsByPumpDateRange(startDate: Long, endDate: Long): Flow<List<MilkBagEntity>> {
        return dao.getBagsByPumpDateRange(startDate, endDate)
    }

    suspend fun getBagById(id: Long): MilkBagEntity? = dao.getBagById(id)

    suspend fun addBag(volumeMl: Int, pumpDate: LocalDate): Long {
        val pumpDateMillis = pumpDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val expiryDate = pumpDate.plusMonths(4)
        val expiryDateMillis = expiryDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val bag = MilkBagEntity(
            volumeMl = volumeMl,
            pumpDate = pumpDateMillis,
            expiryDate = expiryDateMillis
        )
        return dao.insert(bag)
    }

    suspend fun updateBag(id: Long, volumeMl: Int, pumpDate: LocalDate) {
        val existing = dao.getBagById(id) ?: return
        val pumpDateMillis = pumpDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val expiryDate = pumpDate.plusMonths(4)
        val expiryDateMillis = expiryDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        dao.update(existing.copy(
            volumeMl = volumeMl,
            pumpDate = pumpDateMillis,
            expiryDate = expiryDateMillis
        ))
    }

    suspend fun removeBag(id: Long) {
        dao.markAsRemoved(id, System.currentTimeMillis())
    }

    suspend fun restoreBag(id: Long) {
        dao.markAsActive(id)
    }

    suspend fun deleteBag(bag: MilkBagEntity) {
        dao.delete(bag)
    }
}
