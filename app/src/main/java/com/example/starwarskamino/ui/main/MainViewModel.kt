package com.example.starwarskamino.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.starwarskamino.data.main.PlanetRepository
import com.example.starwarskamino.data.server.ApiFactory
import com.example.starwarskamino.general.CoroutineContextProvider

class MainViewModel(private val planetRepository : PlanetRepository) : ViewModel() {

    fun getKamino() = planetRepository.getKaminoPlanet(viewModelScope)
}

object MainViewModelFactory : ViewModelProvider.Factory {
    private val planetRepository = PlanetRepository(ApiFactory.apiaryApi, CoroutineContextProvider())

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(planetRepository) as T
    }
}
