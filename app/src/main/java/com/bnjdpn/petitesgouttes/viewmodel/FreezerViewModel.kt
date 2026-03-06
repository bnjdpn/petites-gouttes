package com.bnjdpn.petitesgouttes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bnjdpn.petitesgouttes.data.database.AppDatabase
import com.bnjdpn.petitesgouttes.data.database.MilkBagEntity
import com.bnjdpn.petitesgouttes.data.repository.MilkBagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

enum class SortOrder { DATE_ASC, DATE_DESC, VOLUME_ASC, VOLUME_DESC }

class FreezerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MilkBagRepository

    val activeBags: Flow<List<MilkBagEntity>>

    private val _sortOrder = MutableStateFlow(SortOrder.DATE_ASC)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    init {
        val dao = AppDatabase.getInstance(application).milkBagDao()
        repository = MilkBagRepository(dao)
        activeBags = repository.activeBags
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun sortBags(bags: List<MilkBagEntity>, order: SortOrder): List<MilkBagEntity> {
        return when (order) {
            SortOrder.DATE_ASC -> bags.sortedBy { it.pumpDate }
            SortOrder.DATE_DESC -> bags.sortedByDescending { it.pumpDate }
            SortOrder.VOLUME_ASC -> bags.sortedBy { it.volumeMl }
            SortOrder.VOLUME_DESC -> bags.sortedByDescending { it.volumeMl }
        }
    }

    fun removeBag(id: Long) {
        viewModelScope.launch {
            repository.removeBag(id)
        }
    }

    fun deleteBag(bag: MilkBagEntity) {
        viewModelScope.launch {
            repository.deleteBag(bag)
        }
    }
}
