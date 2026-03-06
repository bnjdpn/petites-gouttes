package com.bnjdpn.petitesgouttes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bnjdpn.petitesgouttes.data.database.AppDatabase
import com.bnjdpn.petitesgouttes.data.database.MilkBagEntity
import com.bnjdpn.petitesgouttes.data.preferences.SettingsDataStore
import com.bnjdpn.petitesgouttes.data.repository.MilkBagRepository
import kotlinx.coroutines.flow.Flow

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MilkBagRepository
    private val settings: SettingsDataStore

    val activeBagCount: Flow<Int>
    val totalActiveVolume: Flow<Int?>
    val nextBagToUse: Flow<MilkBagEntity?>
    val bagsExpiringSoon: Flow<List<MilkBagEntity>>
    val lowStockThreshold: Flow<Int>
    val dailyConsumption: Flow<Int>
    val daycareDays: Flow<Int>

    init {
        val dao = AppDatabase.getInstance(application).milkBagDao()
        repository = MilkBagRepository(dao)
        settings = SettingsDataStore(application)

        activeBagCount = repository.activeBagCount
        totalActiveVolume = repository.totalActiveVolume
        nextBagToUse = repository.nextBagToUse
        bagsExpiringSoon = repository.getBagsExpiringSoon(14)
        lowStockThreshold = settings.lowStockThresholdMl
        dailyConsumption = settings.dailyConsumptionMl
        daycareDays = settings.daycareDaysPerWeek
    }
}
