package com.example.starwarskamino.ui.residentDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.starwarskamino.data.resident.ResidentsRepository
import com.example.starwarskamino.data.server.ApiFactory
import com.example.starwarskamino.general.CoroutineContextProvider

class ResidentDetailsViewModel(private val residentsRepository: ResidentsRepository) : ViewModel() {
    fun getResident(id:String) = residentsRepository.getResident(viewModelScope, id, false)
}

/**
 * Helper ViewModel Factory to instantiate a ViewModel with constructor parameters
 */
object ResidentListViewModelFactory : ViewModelProvider.Factory {
    private val residentsRepository = ResidentsRepository.getInstance(ApiFactory.api, CoroutineContextProvider())

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ResidentDetailsViewModel(residentsRepository) as T
    }
}
