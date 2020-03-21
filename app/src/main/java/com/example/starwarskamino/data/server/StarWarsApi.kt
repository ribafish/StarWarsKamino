package com.example.starwarskamino.data.server

import com.example.starwarskamino.data.main.response.PlanetResponse
import com.example.starwarskamino.data.resident.response.ResidentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * The Apiary StarWars Kamino interface that describes the http actions available
 */
interface StarWarsApi {

    @GET("planets/{id}")
    suspend fun getPlanet(@Path(value = "id") planetId: String) : Response<PlanetResponse>

    @GET("residents/{id}")
    suspend fun getResident(@Path(value = "id") residentId: String) : Response<ResidentResponse>
}