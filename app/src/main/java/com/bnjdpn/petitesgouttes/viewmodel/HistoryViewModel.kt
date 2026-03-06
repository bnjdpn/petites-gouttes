package com.bnjdpn.petitesgouttes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bnjdpn.petitesgouttes.data.database.AppDatabase
import com.bnjdpn.petitesgouttes.data.database.MilkBagEntity
import com.bnjdpn.petitesgouttes.data.repository.MilkBagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MilkBagRepository

    val removedBags: Flow<List<MilkBagEntity>>

    init {
        val dao = AppDatabase.getInstance(application).milkBagDao()
        repository = MilkBagRepository(dao)
        removedBags = repository.removedBags
    }

    fun restoreBag(id: Long) {
        viewModelScope.launch {
            repository.restoreBag(id)
        }
    }

    fun deleteBag(bag: MilkBagEntity) {
        viewModelScope.launch {
            repository.deleteBag(bag)
        }
    }
}
