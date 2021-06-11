package com.example.covid19.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.data.entity.CountryModel
import com.example.covid19.data.entity.CovidCasesEntity
import com.example.covid19.data.entity.CovidCasesModel
import com.example.covid19.data.entity.mapToModel
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

    fun getCovidCases(country: CountryModel) {
        selectedCountry.value = country
        receive<List<CovidCasesEntity>> {
            bind(repository.getDataByCountry(country.shortName), viewModelScope)
            onReceive = {
                mutableCovidCases.value =
                    if (!it.isNullOrEmpty()) it.last().mapToModel() else CovidCasesModel(country.name, 0, 0, 0)
                null
            }
        }
    }

    fun changeAppTheme(isDark: Boolean) {
        isDarkTheme.value = !isDark
    }
}