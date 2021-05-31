package com.example.covid19.ui.fragments.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.covid19.R
import com.example.covid19.ui.viewmodels.base.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes contentLayoutId: Int) :
    Fragment(contentLayoutId) {
    protected lateinit var model: VM
    private lateinit var snackbar: Snackbar
    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = createViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorCarbonBlack))
        }

        navController = findNavController()
        model.navigationCommands.onEach { command ->
            when (command) {
                is BaseViewModel.NavigationCommand.To -> navController.navigate(command.directions)
                is BaseViewModel.NavigationCommand.BackTo -> navController.popBackStack(
                    command.destinationId,
                    false
                )
                is BaseViewModel.NavigationCommand.Back -> navController.popBackStack()
                is BaseViewModel.NavigationCommand.ToRoot -> navController.popBackStack(
                    R.id.covidCasesFragment,
                    false
                )
            }
        }.launchIn(model.viewModelScope)
        initListeners()
        initFlowCollectors()
    }

    fun showSnackBar(message: String, period: Int = Snackbar.LENGTH_SHORT) {
        snackbar.dismiss()
        snackbar.duration = period
        snackbar.setText(message)
        snackbar.show()
    }

    abstract fun initFlowCollectors()

    abstract fun initListeners()

    abstract fun createViewModel(): VM
}