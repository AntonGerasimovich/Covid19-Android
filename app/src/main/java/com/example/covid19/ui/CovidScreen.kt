package com.example.covid19.ui

import java.lang.IllegalArgumentException

enum class CovidScreen {
    Overview(),
    Symptoms();

    companion object {
        fun fromRoute(route: String?): CovidScreen =
            when (route?.substringBefore("/")) {
                Overview.name -> Overview
                Symptoms.name -> Symptoms
                null -> Overview
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}