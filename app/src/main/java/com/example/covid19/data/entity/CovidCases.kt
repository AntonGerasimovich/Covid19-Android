package com.example.covid19.data.entity

import com.google.gson.annotations.SerializedName

data class CovidCases(
    @SerializedName("Country")
    var country: String = "",
    @SerializedName("Confirmed")
    var infected: Int = 0,
    @SerializedName("Recovered")
    var recovered: Int = 0,
    @SerializedName("Deaths")
    var dead: Int = 0,
    @SerializedName("Date")
    var date: String = ""
)
