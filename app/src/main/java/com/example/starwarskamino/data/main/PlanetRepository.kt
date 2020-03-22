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
import java.lang.Exception
import java.net.UnknownHostException

class PlanetRepository private constructor(private val api:StarWarsApi, private val contextProvider: CoroutineContextProvider) {

    companion object {
        private var instance:PlanetRepository? = null
        fun getInstance(api: StarWarsApi, contextProvider: CoroutineContextProvider):PlanetRepository {
            if (instance == null) {
                instance = PlanetRepository(api, contextProvider)
            }
            return instance as PlanetRepository
        }
    }


    private val resultPlanet : MutableLiveData<Result<PlanetResponse>> = MutableLiveData(Result.Loading)
    private val resultLike : MutableLiveData<Result<LikeResponse>> = MutableLiveData(Result.Loading)

    fun getResultLike() : LiveData<Result<LikeResponse>> = resultLike

    fun getPlanet(scope: CoroutineScope, planetId:Int) : LiveData<Result<PlanetResponse>> {
        scope.launch {
            withContext(contextProvider.IO) {
                try {
                    val response = api.getPlanet(planetId.toString())
                    emitResultPlanet(Result.Loading)
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            emitResultPlanet(Result.Success(response.body()!!))   // We've checked that body isn't null
                        } else {
                            emitResultPlanet(Result.Error("Network call returned empty"))
                        }
                    } else {
                        emitResultPlanet(Result.Error(response.errorBody().toString()))
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

    fun likePlanet(scope: CoroutineScope, planetId:Int) : LiveData<Result<LikeResponse>>  {
        scope.launch {
            withContext(contextProvider.IO) {
                try {
                    val response = api.likePlanet(planetId.toString(), LikeRequest(planetId))
                    emitResultLike(Result.Loading)
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            emitResultLike(Result.Success(response.body()!!))   // We've checked that body isn't null
                        } else {
                            emitResultLike(Result.Error("Network call returned empty"))
                        }
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
     */
    private suspend fun emitResultPlanet(result: Result<PlanetResponse>) {
        withContext(contextProvider.Main) {
            this@PlanetRepository.resultPlanet.value = result
        }
    }

    private suspend fun emitResultLike(result: Result<LikeResponse>) {
        withContext(contextProvider.Main) {
            this@PlanetRepository.resultLike.value = result
        }
    }
}