package com.example.starwarskamino.ui.residentDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarskamino.data.resident.ResidentsRepository
import javax.inject.Inject

class ResidentDetailsViewModel @Inject constructor(private val residentsRepository: ResidentsRepository) : ViewModel() {
    fun getResident(id:String) = residentsRepository.getResident(viewModelScope, id, false)
}
