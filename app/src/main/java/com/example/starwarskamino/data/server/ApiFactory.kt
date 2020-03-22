package com.example.starwarskamino.data.server

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A factory that generates the reotrofit object used to connect to the mocky server
 */
object ApiFactory {
    const val baseUrl = "https://private-anon-f3af761773-starwars2.apiary-mock.com/"

    private val logger = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        }
    }
    private val client = OkHttpClient().newBuilder().addInterceptor(logger).build()

    private fun retrofit() : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiaryApi : StarWarsApi = retrofit()
        .create(StarWarsApi::class.java)

}
