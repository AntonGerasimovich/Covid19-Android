package com.example.covid19.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryModel(
    var name: String = "",
    var shortName: String = ""
): Parcelable

fun CountryModel.mapToEntity() = CountryEntity(name, shortName)