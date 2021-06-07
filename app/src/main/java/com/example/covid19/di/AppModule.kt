package com.example.covid19.di

import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.ui.overview.OverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}
val viewModelModule = module {
    fun provideRepository() = CovidRepository()
    single { provideRepository() }
    viewModel { OverviewViewModel(get()) }
}
