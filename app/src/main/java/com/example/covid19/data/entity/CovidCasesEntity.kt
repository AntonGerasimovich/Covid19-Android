package com.example.covid19.data.entity

import androidx.annotation.StringRes
import com.example.covid19.R
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

sealed class CaseType {
    data class NewCases(@StringRes val name: Int = R.string.new_cases) : CaseType()
    data class LethalCases(@StringRes val name: Int = R.string.lethal) : CaseType()

    companion object {
        val dropdownContentCaseType = arrayListOf(
            NewCases().name,
            LethalCases().name
        )

        fun getCaseType(@StringRes name: Int) = when (name) {
            NewCases().name -> NewCases()
            LethalCases().name -> LethalCases()
            else -> NewCases()
        }
    }
}
