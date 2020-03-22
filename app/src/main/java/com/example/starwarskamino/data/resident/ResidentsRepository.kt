package com.example.starwarskamino.data.resident

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.starwarskamino.data.resident.response.ResidentResponse
import com.example.starwarskamino.data.server.StarWarsApi
import com.example.starwarskamino.general.CoroutineContextProvider
import com.example.starwarskamino.general.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException

class ResidentsRepository private constructor(private val api: StarWarsApi, private val contextProvider: CoroutineContextProvider) {
    private val result : MutableLiveData<Result<ResidentResponse>> = MutableLiveData(Result.Loading)
    // Cached data
    private val residents : MutableLiveData<HashMap<String,ResidentResponse>> = MutableLiveData(HashMap())
    val cachedResidentsLiveData : LiveData<HashMap<String,ResidentResponse>> = residents

    companion object {
        private var instance:ResidentsRepository? = null
        fun getInstance(api: StarWarsApi, contextProvider: CoroutineContextProvider):ResidentsRepository {
            if (instance == null) {
                instance = ResidentsRepository(api, contextProvider)
            }
            return instance as ResidentsRepository
        }
    }

    fun getResident(scope: CoroutineScope, id:String, refresh: Boolean): LiveData<Result<ResidentResponse>> {
        val found:Boolean = residents.value?.containsKey(id) ?: false
        if (!found || refresh) {
            scope.launch {
                withContext(contextProvider.IO) {
                    getResident(id)
                }
            }
        } else if (found) {
            result.value = Result.Success(residents.value?.get(id) ?: error(""))
        }

        return result
    }

    private suspend fun getResident(id:String) {
        try {
            val response = api.getResident(id)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val residentResponse = response.body()!!
                    emitResult(Result.Success(residentResponse))   // We've checked that body isn't null
                    withContext(contextProvider.Main) {
                        residents.value?.set(id, residentResponse)
                    }
                } else {
                    emitResult(Result.Error("Network call returned empty"))
                }
            } else {
                emitResult(Result.Error(response.errorBody().toString()))
            }
        } catch (e: UnknownHostException) {
            emitResult(Result.Error("Can't connect to host. Check internet connectivity"))
        } catch (e: Exception) {
            emitResult(Result.Error(e.toString()))
        }
    }


    /**
     * Helper function for emitting the Result via the main thread
     */
    private suspend fun emitResult(result: Result<ResidentResponse>) {
        withContext(contextProvider.Main) {
            this@ResidentsRepository.result.value = result
        }
    }







}