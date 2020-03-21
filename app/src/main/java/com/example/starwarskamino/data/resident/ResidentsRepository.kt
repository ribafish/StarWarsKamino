package com.example.starwarskamino.data.resident

import com.example.starwarskamino.data.server.StarWarsApi
import com.example.starwarskamino.general.CoroutineContextProvider

class ResidentsRepository(private val api: StarWarsApi, private val contextProvider: CoroutineContextProvider) {
}