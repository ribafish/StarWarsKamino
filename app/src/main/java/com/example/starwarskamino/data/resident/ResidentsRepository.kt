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
import org.jetbrains.annotations.TestOnly
import java.lang.Exception
import java.net.UnknownHostException

class ResidentsRepository private constructor(private val api: StarWarsApi, private val contextProvider: CoroutineContextProvider) {
    private val result : MutableLiveData<Result<ResidentResponse>> = MutableLiveData(Result.Loading)
    // Cached data
    private val residents : MutableLiveData<HashMap<String,ResidentResponse>> = MutableLiveData(HashMap())

    // Singleton, so that we can cache the data as the user is moving between screens
    companion object {
        private var instance:ResidentsRepository? = null
        fun getInstance(api: StarWarsApi, contextProvider: CoroutineContextProvider):ResidentsRepository {
            if (instance == null) {
                instance = ResidentsRepository(api, contextProvider)
            }
            return instance as ResidentsRepository
        }
        @TestOnly
        fun destroyInstance() {
            instance = null
        }
    }

    /**
     * Starts the get resident details request and returns the LiveData with result
     * @param scope coroutine scope, which is used to start the coroutine
     * @param id id of the resident to get the data for
     * @return LiveData holding the result of the request
     */
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

    /**
     * Helper coroutine function to get the resident
     * @param id id of the resident to get the data for
     */
    private suspend fun getResident(id:String) {
        try {
            // Emit the loading state
            emitResult(Result.Loading)
            // Issue the request - blocking the coroutine until it finishes.
            val response = api.getResident(id)
            if (response.isSuccessful) {
                response.body()?.let { residentResponse ->
                    emitResult(Result.Success(residentResponse))
                    // cache the residentResponse using the main thread
                    withContext(contextProvider.Main) {
                        residents.value?.set(id, residentResponse)
                    }
                } ?: emitResult(Result.Error("Network call returned empty"))
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