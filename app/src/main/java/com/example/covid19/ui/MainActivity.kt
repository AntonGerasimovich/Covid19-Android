package com.example.covid19.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.covid19.ui.overview.OverviewScreen
import com.example.covid19.ui.symptoms.SymptomsScreen
import com.example.covid19.ui.theme.CovidTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CovidApp()
        }
    }
}

@Composable
fun CovidApp() {
    CovidTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = CovidScreen.Overview.name
        ) {
            composable(CovidScreen.Overview.name) {
                OverviewScreen(onMenuClick = {
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