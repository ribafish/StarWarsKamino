package com.example.starwarskamino.data.main.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LikeResponse {
    @SerializedName("planet_id")
    @Expose
    var planetId: Int? = null
    @SerializedName("likes ")
    @Expose
    var likes: Int? = null
}