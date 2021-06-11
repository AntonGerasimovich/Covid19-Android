package com.example.covid19.data.entity

import com.squareup.moshi.Json

data class CovidCasesEntity(
    @field:Json(name = "Country")
    var country: String = "",
    @field:Json(name = "Confirmed")
    var infected: Int = 0,
    @field:Json(name = "Recovered")
    var recovered: Int = 0,
    @field:Json(name = "Deaths")
    var dead: Int = 0,
    @field:Json(name = "Date")
    var date: String = ""
)

fun CovidCasesEntity.mapToModel() = CovidCasesModel(country, infected, recovered, dead, date)
