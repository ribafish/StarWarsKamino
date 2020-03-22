package com.example.starwarskamino.data.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.starwarskamino.data.main.response.PlanetResponse
import com.example.starwarskamino.data.server.StarWarsApi
import com.example.starwarskamino.general.CoroutineContextProvider
import com.example.starwarskamino.general.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException

class PlanetRepository(private val api:StarWarsApi, private val contextProvider: CoroutineContextProvider) {
    private val result : MutableLiveData<Result<PlanetResponse>> = MutableLiveData()

    /**
     * Helper function for emitting the Result via the main thread
     */
    private suspend fun emitResult(result: Result<PlanetResponse>) {
        withContext(contextProvider.Main) {
            this@PlanetRepository.result.value = result
        }
    }

    fun getKaminoPlanet(scope: CoroutineScope) : LiveData<Result<PlanetResponse>> {
        result.value = Result.Loading
        scope.launch {
            withContext(contextProvider.IO) {
                try {
                    val response = api.getPlanet("10")
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            emitResult(Result.Success(response.body()!!))   // We've checked that body isn't null
                        } else {
                            emitResult(Result.Error("Network call returned empty"))
                        }
                    } else {
                        emitResult(Result.Error(response.errorBody().toString()))
                    }
                } catch (e : UnknownHostException) {
                    emitResult(Result.Error("Can't connect to host. Check internet connectivity"))
                } catch (e : Exception) {
                    emitResult(Result.Error(e.toString()))
                }
            }
        }
        return result
    }
}