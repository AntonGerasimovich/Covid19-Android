package com.example.covid19.data.repository

import com.example.covid19.data.entity.CountryEntity
import com.example.covid19.data.entity.CovidCasesEntity
import com.example.covid19.network.api.ApiClient
import com.example.covid19.network.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CovidRepository {
    private val api = ApiClient.getCovidApi()

    fun loadAllCountries(): Flow<Result<List<CountryEntity>>> = flow { emit(api.getCountries()) }

    fun getDataByCountry(countryName: String): Flow<Result<List<CovidCasesEntity>>> = flow { emit(api.getDataByCountry(countryName)) }
}