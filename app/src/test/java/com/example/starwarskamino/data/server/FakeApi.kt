package com.example.starwarskamino.data.server

import com.example.starwarskamino.data.main.request.LikeRequest
import com.example.starwarskamino.data.main.response.LikeResponse
import com.example.starwarskamino.data.main.response.PlanetResponse
import com.example.starwarskamino.data.resident.response.ResidentResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response

class FakeApi : StarWarsApi {
    private lateinit var planetResponse : Response<PlanetResponse>
    private lateinit var residentResponse : Response<ResidentResponse>
    private lateinit var likeResponse : Response<LikeResponse>

    init {
        createErrorResponse(Type.Planet, 999)
        createErrorResponse(Type.Resident, 999)
        createErrorResponse(Type.Like, 999)
    }


    override suspend fun getPlanet(planetId: String): Response<PlanetResponse> {
        return planetResponse
    }

    override suspend fun getResident(residentId: String): Response<ResidentResponse> {
        return residentResponse
    }

    override suspend fun likePlanet(
        planetId: String,
        likeRequest: LikeRequest
    ): Response<LikeResponse> {
        return likeResponse
    }

    fun createErrorResponse(type : Type, code : Int) {
        val body = ResponseBody.Companion.create("application/json; charset=utf-8".toMediaTypeOrNull(), "{\"error\":\"mock_error\",\"error_description\":\"Mock error\",\"code\":400}")
        when(type) {
            Type.Like -> likeResponse = Response.error(code, body)
            Type.Planet -> planetResponse = Response.error(code, body)
            Type.Resident -> residentResponse = Response.error(code, body)
        }
    }

    fun createSuccessResponse(type : Type, data : Any) {
        when(type) {
            Type.Like -> likeResponse = Response.success(data as LikeResponse)
            Type.Planet -> planetResponse = Response.success(data as PlanetResponse)
            Type.Resident -> residentResponse = Response.success(data as ResidentResponse)
        }
    }

    enum class Type {
        Planet,
        Resident,
        Like
    }
}