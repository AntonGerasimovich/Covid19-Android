package com.example.covid19.data.entity

import com.squareup.moshi.Json

data class CountryEntity(
    @field:Json(name = "Country")
    val name: String,
    @field:Json(name = "Slug")
    val shortName: String = ""
)

fun CountryEntity.mapToModel() = CountryModel(name, shortName)