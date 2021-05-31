package com.example.covid19.ui.viewmodels

import com.example.covid19.data.repository.CovidRepository
import com.example.covid19.ui.viewmodels.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AboutCovidViewModel(private val repository: CovidRepository): BaseViewModel() {

}