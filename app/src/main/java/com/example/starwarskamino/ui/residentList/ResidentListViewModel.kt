package com.example.starwarskamino.ui.residentList

import androidx.lifecycle.*
import com.example.starwarskamino.data.resident.ResidentsRepository
import com.example.starwarskamino.data.resident.response.ResidentResponse
import com.example.starwarskamino.data.server.ApiFactory
import com.example.starwarskamino.general.CoroutineContextProvider
import com.example.starwarskamino.general.Result
import timber.log.Timber

class ResidentListViewModel(private val residentsRepository: ResidentsRepository) : ViewModel() {

    private val residentIds:MutableLiveData<List<String>> = MutableLiveData(ArrayList())
    val residentIdsLiveData:LiveData<List<String>> = residentIds

    fun getResident(residentUrl:String, refresh:Boolean) : LiveData<Result<ResidentResponse>> {
        val id = residentUrl.trim().removePrefix(ApiFactory.baseUrl).removePrefix("residents/").removeSurrounding("/")
        return residentsRepository.getResident(viewModelScope, id, refresh)
    }

    fun setResidentUrls(urls:Array<String>) {
        residentIds.value = urls.toList().map{it.trim().removeSuffix("/").substringAfterLast("/")}
    }
}

object ResidentListViewModelFactory : ViewModelProvider.Factory {
    private val residentsRepository = ResidentsRepository.getInstance(ApiFactory.apiaryApi, CoroutineContextProvider())

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ResidentListViewModel(residentsRepository) as T
    }
}