package com.example.starwarskamino.data.main.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LikeRequest (planetId:Int) {
    @SerializedName("planet_id")
    @Expose
    val planetId: Int = planetId
}