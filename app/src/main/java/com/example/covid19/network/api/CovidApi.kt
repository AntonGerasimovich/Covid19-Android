package com.example.covid19.network.api

import com.example.covid19.data.entity.Country
import com.example.covid19.data.entity.CovidCases
import com.example.covid19.network.result.Result
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidApi {
    @GET("countries")
    suspend fun getCountries(): Result<List<Country>>

    @GET("total/country/{country}")
    suspend fun getDataByCountry(@Path("country") countryName: String): Result<List<CovidCases>>
}