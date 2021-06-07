package com.example.covid19.data.entity

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("Country")
    val name: String,
    @SerializedName("Slug")
    val shortName: String = ""
)