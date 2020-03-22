package com.example.starwarskamino.ui.residentList

import androidx.lifecycle.*
import com.example.starwarskamino.data.resident.ResidentsRepository
import com.example.starwarskamino.data.resident.response.ResidentResponse
import com.example.starwarskamino.data.server.ApiFactory
import com.example.starwarskamino.general.CoroutineContextProvider
import com.example.starwarskamino.general.Result

class ResidentListViewModel(private val residentsRepository: ResidentsRepository) : ViewModel() {

    private val residentIds:MutableLiveData<List<String>> = MutableLiveData(ArrayList())
    // Public LiveData holding a List of resident IDs
    val residentIdsLiveData:LiveData<List<String>> = residentIds

    /**
     * Gets specified resident details and returns LiveData with the result
     * @param id id of resident to get details of
     * @param refresh force a refresh if true, otherwise return cached if possible
     * @return Livedata holding the result with ResidentResponse
     */
    fun getResident(id:String, refresh:Boolean) : LiveData<Result<ResidentResponse>> {
        return residentsRepository.getResident(viewModelScope, id, refresh)
    }

    /**
     * Parses resident urls to resident IDs
     * @param urls Array of resident urls as strings
     */
    fun setResidentUrls(urls:Array<String>) {
        residentIds.value = urls.toList().map{it.trim().removeSuffix("/").substringAfterLast("/")}
    }
}

/**
 * Helper ViewModel Factory to instantiate a ViewModel with constructor parameters
 */
object ResidentListViewModelFactory : ViewModelProvider.Factory {
    private val residentsRepository = ResidentsRepository.getInstance(ApiFactory.api, CoroutineContextProvider())

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ResidentListViewModel(residentsRepository) as T
    }
}