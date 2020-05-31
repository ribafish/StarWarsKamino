package com.example.starwarskamino.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarskamino.data.main.PlanetRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val planetRepository : PlanetRepository) : ViewModel() {

    fun getKamino() = planetRepository.getPlanet(viewModelScope, 10)

    fun getLikeKamino() = planetRepository.getResultLike()
    fun likeKamino() = planetRepository.likePlanet(viewModelScope, 10)
}

