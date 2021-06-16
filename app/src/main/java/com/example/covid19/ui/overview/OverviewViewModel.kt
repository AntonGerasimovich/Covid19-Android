package com.example.covid19.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.data.entity.*
import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.network.backendReceiver.receive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OverviewViewModel(private val repository: CovidRepository) : ViewModel() {
    private val mutableCovidCases: MutableStateFlow<CovidCasesModel> =
        MutableStateFlow(CovidCasesModel("Belarus", 0, 0, 0, "2021-06-06T00:00:00Z"))
    val covidCases: StateFlow<CovidCasesModel> = mutableCovidCases
    val countries: MutableStateFlow<List<CountryModel>> = MutableStateFlow(listOf())
    val selectedCountry: MutableStateFlow<CountryModel> =
        MutableStateFlow(CountryModel("Select a country"))
    val isDarkTheme: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var timestampModel: TimestampModel = TimestampModel.AllTime()
    private var caseType: CaseType = CaseType.NewCases()
    private var rawData: List<CovidCasesModel> = listOf()
    val graphData: MutableStateFlow<List<GraphDataModel>> = MutableStateFlow(listOf())

    fun getCovidCases(country: CountryModel) {
        selectedCountry.value = country
        receive<List<CovidCasesEntity>> {
            bind(repository.getDataByCountry(country.shortName), viewModelScope)
            onReceive = {
                mutableCovidCases.value =
                    if (!it.isNullOrEmpty()) it.last()
                        .mapToModel() else CovidCasesModel(country.name, 0, 0, 0)
                rawData = it.map { entity -> entity.mapToModel() }
                convertRawDataToGraphData()
                null
            }
        }
    }

    fun onTimestampPicked(timestampModel: TimestampModel) {
        this.timestampModel = timestampModel
        convertRawDataToGraphData()
    }

    fun onCaseTypePicked(caseType: CaseType) {
        this.caseType = caseType
        convertRawDataToGraphData()
    }

    private fun convertRawDataToGraphData() {
        val days: Int = when (timestampModel) {
            TimestampModel.Week() -> TimestampModel.Week().days
            TimestampModel.TwoWeeks() -> TimestampModel.TwoWeeks().days
            TimestampModel.Month() -> TimestampModel.Month().days
            else -> 0
        }
        if (rawData.isNotEmpty()) {
            graphData.value = convertTotalDataToDaily(
                (((if (days > 0) {
                    rawData.takeLast(days + 1)
                } else {
                    rawData
                }).reversed() as MutableList<CovidCasesModel>))
            ).map { model -> model.mapToGraphData(caseType) }
        }
    }

    private fun convertTotalDataToDaily(data: MutableList<CovidCasesModel>): List<CovidCasesModel> {
        for (index in data.indices) {
            if (index != data.lastIndex) {
                data[index] = data[index] - data[index + 1]
            }
        }
        data.removeLast()
        return data.reversed()
    }

    fun changeAppTheme(isDark: Boolean) {
        isDarkTheme.value = !isDark
    }
}