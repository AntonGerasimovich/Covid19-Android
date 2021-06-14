package com.example.covid19.network.api

import com.example.covid19.network.ResultAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(loggingInterceptor)
            }

            retrofit = Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(httpClient.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(ResultAdapterFactory())
                .build()

            instance = retrofit.create(CovidApi::class.java)
        }
    }
}