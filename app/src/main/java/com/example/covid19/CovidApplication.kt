package com.example.covid19

import android.app.Application
import android.content.Context
import com.example.covid19.di.appModule
import com.example.covid19.di.viewModelModule
import com.example.covid19.utils.CountryManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CovidApplication: Application() {

    companion object {
        lateinit var app: CovidApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        app = this
    }

    @DelicateCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CovidApplication)
            modules(appModule, viewModelModule)
        }
        init()
    }

    @DelicateCoroutinesApi
    private fun init() {
        CountryManager.loadAllCountries(GlobalScope)
    }
}