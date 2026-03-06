package com.bnjdpn.petitesgouttes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bnjdpn.petitesgouttes.data.database.AppDatabase
import com.bnjdpn.petitesgouttes.data.database.MilkBagEntity
import com.bnjdpn.petitesgouttes.data.repository.MilkBagRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class DailyVolume(val date: LocalDate, val totalMl: Int)
data class WeeklyVolume(val weekStart: LocalDate, val totalMl: Int)

class StatsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MilkBagRepository

    val allBags: Flow<List<MilkBagEntity>>

    init {
        val dao = AppDatabase.getInstance(application).milkBagDao()
        repository = MilkBagRepository(dao)
        allBags = repository.allBags
    }

    fun getDailyVolumes(bags: List<MilkBagEntity>, days: Int = 30): List<DailyVolume> {
        val today = LocalDate.now()
        val startDate = today.minusDays(days.toLong() - 1)
        val zone = ZoneId.systemDefault()

        val volumeByDate = bags
            .map { bag ->
                val date = Instant.ofEpochMilli(bag.pumpDate).atZone(zone).toLocalDate()
                date to bag.volumeMl
            }
            .filter { (date, _) -> !date.isBefore(startDate) && !date.isAfter(today) }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.sum() }

        return (0L until days).map { offset ->
            val date = startDate.plusDays(offset)
            DailyVolume(date, volumeByDate[date] ?: 0)
        }
    }

    fun getWeeklyVolumes(bags: List<MilkBagEntity>, weeks: Int = 12): List<WeeklyVolume> {
        val today = LocalDate.now()
        val zone = ZoneId.systemDefault()

        return (0 until weeks).map { weekOffset ->
            val weekEnd = today.minusWeeks(weekOffset.toLong())
            val weekStart = weekEnd.minusDays(6)
            val startMillis = weekStart.atStartOfDay(zone).toInstant().toEpochMilli()
            val endMillis = weekEnd.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()

            val total = bags
                .filter { it.pumpDate >= startMillis && it.pumpDate < endMillis }
                .sumOf { it.volumeMl }

            WeeklyVolume(weekStart, total)
        }.reversed()
    }

    fun getAverageDailyVolume(bags: List<MilkBagEntity>, days: Int): Double {
        val today = LocalDate.now()
        val startDate = today.minusDays(days.toLong() - 1)
        val zone = ZoneId.systemDefault()
        val startMillis = startDate.atStartOfDay(zone).toInstant().toEpochMilli()
        val endMillis = today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()

        val total = bags
            .filter { it.pumpDate >= startMillis && it.pumpDate < endMillis }
            .sumOf { it.volumeMl }

        return total.toDouble() / days
    }

    fun getMonthlyTotal(bags: List<MilkBagEntity>, monthsAgo: Int = 0): Int {
        val today = LocalDate.now()
        val month = today.minusMonths(monthsAgo.toLong())
        val monthStart = month.withDayOfMonth(1)
        val monthEnd = monthStart.plusMonths(1)
        val zone = ZoneId.systemDefault()
        val startMillis = monthStart.atStartOfDay(zone).toInstant().toEpochMilli()
        val endMillis = monthEnd.atStartOfDay(zone).toInstant().toEpochMilli()

        return bags
            .filter { it.pumpDate >= startMillis && it.pumpDate < endMillis }
            .sumOf { it.volumeMl }
    }
}
