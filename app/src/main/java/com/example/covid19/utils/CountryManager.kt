package com.example.covid19.utils

import com.example.covid19.data.entity.CountryEntity
import com.example.covid19.data.entity.CountryModel
import com.example.covid19.data.entity.mapToModel
import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.network.backendReceiver.receive
import kotlinx.coroutines.CoroutineScope
import org.koin.java.KoinJavaComponent.inject

object CountryManager {
    private val covidRepository: CovidRepository by inject(CovidRepository::class.java)
    private var countries: List<CountryModel> = listOf()
    var onCountriesLoaded: (List<CountryModel>) -> Unit = {}

    fun loadAllCountries(scope: CoroutineScope) {
        receive<List<CountryEntity>> {
            bind(covidRepository.loadAllCountries(), scope)
            onReceive = {
                countries = it.map { countryEntity -> countryEntity.mapToModel() }
                onCountriesLoaded(countries)
            }
        }
    }
}