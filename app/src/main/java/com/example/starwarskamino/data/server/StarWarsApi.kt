package com.example.starwarskamino.data.server

import com.example.starwarskamino.data.main.request.LikeRequest
import com.example.starwarskamino.data.main.response.LikeResponse
import com.example.starwarskamino.data.main.response.PlanetResponse
import com.example.starwarskamino.data.resident.response.ResidentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * The Apiary StarWars Kamino interface that describes the http actions available
 */
interface StarWarsApi {

    @GET("planets/{id}")
    suspend fun getPlanet(@Path(value = "id") planetId: String) : Response<PlanetResponse>

    @GET("residents/{id}")
    suspend fun getResident(@Path(value = "id") residentId: String) : Response<ResidentResponse>

    @POST("planets/{id}/like")
    suspend fun likePlanet(@Path(value = "id") planetId: String, @Body likeRequest: LikeRequest) : Response<LikeResponse>
}