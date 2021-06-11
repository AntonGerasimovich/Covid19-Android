package com.example.covid19.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.covid19.ui.overview.OverviewScreen
import com.example.covid19.ui.overview.OverviewViewModel
import com.example.covid19.ui.symptoms.SymptomsScreen
import com.example.covid19.ui.theme.CovidTheme
import com.example.covid19.utils.CountryManager
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val overviewViewModel by inject<OverviewViewModel>()
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
            startDestination = CovidScreen.Overview.name
        ) {
            composable(CovidScreen.Overview.name) {
                OverviewScreen(overviewViewModel, onMenuClick = {
                    navController.navigate(CovidScreen.Symptoms.name)
                })
            }
            composable(CovidScreen.Symptoms.name) {
                SymptomsScreen(onMenuClick = {
                    navController.navigate(CovidScreen.Overview.name)
                })
            }
        }
    }
}