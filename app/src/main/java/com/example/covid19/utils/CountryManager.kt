package com.example.covid19.utils

import com.example.covid19.data.entity.Country
import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.network.backendReceiver.receive
import kotlinx.coroutines.CoroutineScope
import org.koin.java.KoinJavaComponent.inject

object CountryManager {
    private val covidRepository: CovidRepository by inject(CovidRepository::class.java)
    var countries: List<Country> = listOf()
    var onCountriesLoaded: (List<Country>) -> Unit = {}

    fun loadAllCountries(scope: CoroutineScope) {
        receive<List<Country>> {
            bind(covidRepository.loadAllCountries(), scope)
            onReceive = {
                countries = it
                onCountriesLoaded(countries)
            }
        }
    }
}