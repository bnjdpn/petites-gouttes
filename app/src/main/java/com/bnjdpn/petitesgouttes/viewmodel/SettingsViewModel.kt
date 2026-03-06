package com.bnjdpn.petitesgouttes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bnjdpn.petitesgouttes.data.preferences.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settings = SettingsDataStore(application)

    val lowStockThreshold: Flow<Int> = settings.lowStockThresholdMl
    val dailyConsumption: Flow<Int> = settings.dailyConsumptionMl
    val daycareDays: Flow<Int> = settings.daycareDaysPerWeek

    fun updateLowStockThreshold(value: Int) {
        viewModelScope.launch {
            settings.setLowStockThreshold(value)
        }
    }

    fun updateDailyConsumption(value: Int) {
        viewModelScope.launch {
            settings.setDailyConsumption(value)
        }
    }

    fun updateDaycareDays(value: Int) {
        viewModelScope.launch {
            settings.setDaycareDaysPerWeek(value)
        }
    }
}
