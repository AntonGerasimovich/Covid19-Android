package com.example.covid19.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CovidCasesModel (
    var country: String = "",
    var infected: Int = 0,
    var recovered: Int = 0,
    var dead: Int = 0,
    var date: String = ""
): Parcelable

fun CovidCasesModel.mapToEntity() = CovidCasesEntity(country, infected, recovered, dead, date)