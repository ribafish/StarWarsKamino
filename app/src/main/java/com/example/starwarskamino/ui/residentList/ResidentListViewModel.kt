package com.example.starwarskamino.ui.residentList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.starwarskamino.data.resident.ResidentsRepository
import com.example.starwarskamino.data.server.ApiFactory
import com.example.starwarskamino.general.CoroutineContextProvider

class ResidentListViewModel(private val residentsRepository: ResidentsRepository) : ViewModel() {
    // TODO: Implement the ViewModel
}

object ResidentListViewModelFactory : ViewModelProvider.Factory {
    private val residentsRepository = ResidentsRepository(ApiFactory.apiaryApi, CoroutineContextProvider())

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ResidentListViewModel(residentsRepository) as T
    }
}