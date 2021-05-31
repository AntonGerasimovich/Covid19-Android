package com.example.covid19.ui.viewmodels.base

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {

    val navigationCommands: MutableSharedFlow<NavigationCommand> = MutableSharedFlow(0, 1)

    fun navigate(directions: NavDirections) {
        navigationCommands.tryEmit(NavigationCommand.To(directions))
    }

    sealed class NavigationCommand {
        data class To(val directions: NavDirections) : NavigationCommand()
        object Back : NavigationCommand()
        data class BackTo(val destinationId: Int) : NavigationCommand()
        object ToRoot : NavigationCommand()
    }
}