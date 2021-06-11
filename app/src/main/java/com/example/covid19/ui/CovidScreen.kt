package com.example.covid19.ui

import java.lang.IllegalArgumentException

sealed class CovidScreen {
    data class Overview(val name: String = "Overview"): CovidScreen()
    data class Symptoms(val name: String = "Symptoms"): CovidScreen()

    companion object {
        fun fromRoute(route: String?): CovidScreen =
            when (route?.substringBefore("/")) {
                Overview().name -> Overview()
                Symptoms().name -> Symptoms()
                null -> Overview()
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}