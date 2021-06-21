package com.example.covid19.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.covid19.ui.overview.OverviewScreen
import com.example.covid19.ui.overview.OverviewViewModel
import com.example.covid19.ui.symptoms.SymptomsScreen
import com.example.covid19.ui.theme.CovidTheme
import com.example.covid19.utils.CountryManager
import com.example.covid19.utils.CovidWorker
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val overviewViewModel by inject<OverviewViewModel>()
        overviewViewModel.countryLiveUpdates.onEach {
            if (it.name.isNotEmpty()) {
                val updateRequest = PeriodicWorkRequestBuilder<CovidWorker>(3, TimeUnit.HOURS)
                    .build()
                with(WorkManager.getInstance(applicationContext)) {
                    enqueue(updateRequest)
                }
            }
        }.launchIn(overviewViewModel.viewModelScope)
        CountryManager.onCountriesLoaded = {
            val countries = it.sortedBy { country -> country.name }
            overviewViewModel.getCovidCases(countries.first())
            overviewViewModel.countries.value = countries
            overviewViewModel.selectedCountry.value = countries.first()
        }
        setContent {
            CovidApp(overviewViewModel)
        }
    }
}

@Composable
fun CovidApp(overviewViewModel: OverviewViewModel) {
    val isDarkTheme = overviewViewModel.isDarkTheme.collectAsState()
    CovidTheme(isDarkTheme.value) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = CovidScreen.Overview().name
        ) {
            composable(CovidScreen.Overview().name) {
                OverviewScreen(overviewViewModel, onMenuClick = {
                    navController.navigate(CovidScreen.Symptoms().name)
                })
            }
            composable(CovidScreen.Symptoms().name) {
                SymptomsScreen(overviewViewModel, onMenuClick = {
                    navController.navigate(CovidScreen.Overview().name)
                })
            }
        }
    }
}