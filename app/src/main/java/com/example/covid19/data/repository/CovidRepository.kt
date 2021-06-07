package com.example.covid19.data.repository

import com.example.covid19.data.entity.Country
import com.example.covid19.data.entity.CovidCases
import com.example.covid19.network.api.ApiClient
import com.example.covid19.network.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CovidRepository {
    private val api = ApiClient.getCovidApi()

    fun loadAllCountries(): Flow<Result<List<Country>>> = flow { emit(api.getCountries()) }

    fun getDataByCountry(countryName: String): Flow<Result<List<CovidCases>>> = flow { emit(api.getDataByCountry(countryName)) }
}