package com.example.starwarskamino.data.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.starwarskamino.data.main.request.LikeRequest
import com.example.starwarskamino.data.main.response.LikeResponse
import com.example.starwarskamino.data.main.response.PlanetResponse
import com.example.starwarskamino.data.server.StarWarsApi
import com.example.starwarskamino.general.CoroutineContextProvider
import com.example.starwarskamino.general.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.TestOnly
import timber.log.Timber
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject

class PlanetRepository @Inject constructor(private val api:StarWarsApi, private val contextProvider: CoroutineContextProvider) {

    // Make the PlanetRepository a singleton, so that the last result and data is cached. This would be better solved using Room
    companion object {
        private var instance:PlanetRepository? = null
        fun getInstance(api: StarWarsApi, contextProvider: CoroutineContextProvider):PlanetRepository {
            if (instance == null) {
                instance = PlanetRepository(api, contextProvider)
            }
            return instance as PlanetRepository
        }
        @TestOnly
        fun destroyInstance() {
            instance = null
        }
    }


    private val resultPlanet : MutableLiveData<Result<PlanetResponse>> = MutableLiveData(Result.Loading)
    private val resultLike : MutableLiveData<Result<LikeResponse>> = MutableLiveData(Result.Loading)

    /**
     * Returns the LiveData holding the result of like request
     * @return LiveData holding the result of like request
     */
    fun getResultLike() : LiveData<Result<LikeResponse>> = resultLike

    /**
     * Starts the get planet data and returns LiveData holding the result of get planet data request
     * @param scope coroutine scope, which is used to start the coroutine
     * @param planetId id of the planet to get the data for
     * @return LiveData holding the result of get planet data request
     */
    fun getPlanet(scope: CoroutineScope, planetId:Int) : LiveData<Result<PlanetResponse>> {
        scope.launch {
            // Change context to IO
            withContext(contextProvider.IO) {
                try {
                    // Emit the loading state
                    emitResultPlanet(Result.Loading)
                    // Issue the request - blocking the coroutine until it finishes
                    val response = api.getPlanet(planetId.toString())
                    if (response.isSuccessful) {
                        response.body()?.let {
                            emitResultPlanet(Result.Success(it))
                        } ?: emitResultPlanet(Result.Error("Network call returned empty"))
                    } else {
                        val msg = response.errorBody()?.string()
                        emitResultPlanet(Result.Error("Error ${response.code()}: $msg"))
                    }
                } catch (e : UnknownHostException) {
                    emitResultPlanet(Result.Error("Can't connect to host. Check internet connectivity"))
                } catch (e : Exception) {
                    emitResultPlanet(Result.Error(e.toString()))
                }
            }
        }
        return resultPlanet
    }

    /**
     * Starts the request to like tha planet
     * @param scope coroutine scope, which is used to start the coroutine
     * @param planetId id of the planet to get the data for
     * @return LiveData holding the result of like request
     */
    fun likePlanet(scope: CoroutineScope, planetId:Int) : LiveData<Result<LikeResponse>>  {
        scope.launch {
            withContext(contextProvider.IO) {
                try {
                    // Emit the loading state
                    emitResultLike(Result.Loading)
                    // Issue the request - blocking the coroutine until it finishes.
                    val response = api.likePlanet(planetId.toString(), LikeRequest(planetId))
                    if (response.isSuccessful) {
                        response.body()?.let {
                            emitResultLike(Result.Success(it))
                        } ?: emitResultLike(Result.Error("Network call returned empty"))
                    } else {
                        emitResultLike(Result.Error(response.errorBody().toString()))
                    }
                } catch (e : UnknownHostException) {
                    emitResultLike(Result.Error("Can't connect to host. Check internet connectivity"))
                } catch (e : Exception) {
                    emitResultLike(Result.Error(e.toString()))
                }
            }
        }
        return resultLike
    }



    /**
     * Helper function for emitting the Result via the main thread
     * @param result PlanetResponse result to assign to MutableLiveData on the main thread
     */
    private suspend fun emitResultPlanet(result: Result<PlanetResponse>) {
        withContext(contextProvider.Main) {
            this@PlanetRepository.resultPlanet.value = result
        }
    }

    /**
     * Helper function for emitting the Result via the main thread
     * @param result LikeResponse result to assign to MutableLiveData on the main thread
     */
    private suspend fun emitResultLike(result: Result<LikeResponse>) {
        withContext(contextProvider.Main) {
            this@PlanetRepository.resultLike.value = result
        }
    }
}