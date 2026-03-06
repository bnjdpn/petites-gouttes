package com.bnjdpn.petitesgouttes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milk_bag")
data class MilkBagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val volumeMl: Int,
    val pumpDate: Long,
    val expiryDate: Long,
    val removedFromFreezer: Boolean = false,
    val removalDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
