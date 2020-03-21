package com.example.starwarskamino.data.main.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PlanetResponse {
    var name: String? = null
    @SerializedName("rotation_period")
    @Expose
    var rotationPeriod: String? = null
    @SerializedName("orbital_period")
    @Expose
    var orbitalPeriod: String? = null
    @SerializedName("diameter")
    @Expose
    var diameter: String? = null
    @SerializedName("climate")
    @Expose
    var climate: String? = null
    @SerializedName("gravity")
    @Expose
    var gravity: String? = null
    @SerializedName("terrain")
    @Expose
    var terrain: String? = null
    @SerializedName("surface_water")
    @Expose
    var surfaceWater: String? = null
    @SerializedName("population")
    @Expose
    var population: String? = null
    @SerializedName("residents")
    @Expose
    var residents: List<String>? = null
    @SerializedName("created")
    @Expose
    var created: String? = null
    @SerializedName("edited")
    @Expose
    var edited: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
    @SerializedName("likes")
    @Expose
    var likes: Int? = null
}