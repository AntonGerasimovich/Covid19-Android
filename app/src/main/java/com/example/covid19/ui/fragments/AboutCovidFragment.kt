package com.example.covid19.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.covid19.R
import com.example.covid19.databinding.FragmentAboutCovidBinding
import com.example.covid19.ui.fragments.base.BaseFragment
import com.example.covid19.ui.viewmodels.AboutCovidViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class AboutCovidFragment: BaseFragment<AboutCovidViewModel>(R.layout.fragment_about_covid) {
    private val binding: FragmentAboutCovidBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun initFlowCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {

        }
    }

    override fun initListeners() {
    }

    override fun createViewModel(): AboutCovidViewModel {
        val model: AboutCovidViewModel by inject()
        return model
    }

}