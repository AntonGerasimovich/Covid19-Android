package com.example.covid19.network.api

import com.example.covid19.network.ResultAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var connectionTimeout = 30
        private lateinit var retrofit: Retrofit
        private lateinit var instance: CovidApi
        private const val SERVER_URL = "https://api.covid19api.com/"

        fun getCovidApi(timeout: Int = connectionTimeout): CovidApi {
            connectionTimeout = timeout
            if (!this::instance.isInitialized) {
                initInstance()
            }
            return instance
        }

        private fun initInstance() {
            val httpClient = OkHttpClient.Builder()
                .readTimeout(connectionTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(connectionTimeout.toLong(), TimeUnit.SECONDS)

            retrofit = Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(ResultAdapterFactory())
                .build()

            instance = retrofit.create(CovidApi::class.java)
        }
    }
}