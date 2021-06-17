package com.example.covid19.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CovidCasesModel(
    var country: String = "",
    var infected: Int = 0,
    var recovered: Int = 0,
    var dead: Int = 0,
    var date: String = ""
) : Parcelable {
    operator fun minus(covidCasesModel: CovidCasesModel): CovidCasesModel = CovidCasesModel(
        this.country,
        this.infected - covidCasesModel.infected,
        this.recovered - covidCasesModel.recovered,
        this.dead - covidCasesModel.dead,
        this.date
    )
}

fun CovidCasesModel.mapToEntity() = CovidCasesEntity(country, infected, recovered, dead, date)