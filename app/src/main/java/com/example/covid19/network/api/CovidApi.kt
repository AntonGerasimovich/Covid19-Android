package com.example.covid19.network.api

import com.example.covid19.data.entity.CountryEntity
import com.example.covid19.data.entity.CovidCasesEntity
import com.example.covid19.network.result.Result
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidApi {
    @GET("countries")
    suspend fun getCountries(): Result<List<CountryEntity>>

    @GET("total/country/{country}")
    suspend fun getDataByCountry(@Path("country") countryName: String): Result<List<CovidCasesEntity>>
}