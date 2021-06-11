package com.example.covid19.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19.data.entity.Country
import com.example.covid19.data.entity.CovidCases
import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.network.backendReceiver.receive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OverviewViewModel(private val repository: CovidRepository) : ViewModel() {
    private val covidCases_: MutableStateFlow<CovidCases> =
        MutableStateFlow(CovidCases("Belarus", 0, 0, 0, "2021-06-06T00:00:00Z"))
    val covidCases: StateFlow<CovidCases> = covidCases_
    val countries: MutableStateFlow<List<Country>> = MutableStateFlow(listOf())
    val selectedCountry: MutableStateFlow<Country> = MutableStateFlow(Country("Select a country"))
    val isDarkTheme: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun getCovidCases(country: Country) {
        selectedCountry.value = country
        receive<List<CovidCases>> {
            bind(repository.getDataByCountry(country.shortName), viewModelScope)
            onReceive = {
                covidCases_.value = if (!it.isNullOrEmpty()) it.last() else CovidCases(country.name, 0, 0, 0)
                null
            }
        }
    }

    fun changeAppTheme(isDark: Boolean) {
        isDarkTheme.value = !isDark
    }
}