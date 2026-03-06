package com.bnjdpn.petitesgouttes.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        val LOW_STOCK_THRESHOLD_ML = intPreferencesKey("low_stock_threshold_ml")
        val DAILY_CONSUMPTION_ML = intPreferencesKey("daily_consumption_ml")
        val DAYCARE_DAYS_PER_WEEK = intPreferencesKey("daycare_days_per_week")
    }

    val lowStockThresholdMl: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[LOW_STOCK_THRESHOLD_ML] ?: 1500
    }

    val dailyConsumptionMl: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[DAILY_CONSUMPTION_ML] ?: 300
    }

    val daycareDaysPerWeek: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[DAYCARE_DAYS_PER_WEEK] ?: 5
    }

    suspend fun setLowStockThreshold(value: Int) {
        context.dataStore.edit { it[LOW_STOCK_THRESHOLD_ML] = value }
    }

    suspend fun setDailyConsumption(value: Int) {
        context.dataStore.edit { it[DAILY_CONSUMPTION_ML] = value }
    }

    suspend fun setDaycareDaysPerWeek(value: Int) {
        context.dataStore.edit { it[DAYCARE_DAYS_PER_WEEK] = value }
    }
}
