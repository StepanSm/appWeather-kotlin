package com.smerkis.weamther.fragments

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.R
import com.smerkis.weamther.activities.Layout
import com.smerkis.weamther.databinding.FragmentSplashBinding
import com.smerkis.weamther.viewModels.SplashViewModel
import javax.inject.Inject


class SplashFragment :
    BaseFragment<SplashViewModel, FragmentSplashBinding>(R.layout.fragment_splash) {


    override fun initDi() {
        MyApp.instance.getAppComponent()
            .activitySubComponentBuilder()
            .with(navigator as FragmentActivity)
            .build()
            .inject(this)

        MyApp.instance.getViewModelSubComponent().inject(viewModel)
    }

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = viewModel
        viewModel.errorData.observe(viewLifecycleOwner) { exception ->
            showInfoDialog("ERROR", exception.message)
            navigator.closeApp()
        }

        viewModel.imageUrlData.observe(viewLifecycleOwner) { url ->

        }

        viewModel.weatherInfoData.observe(viewLifecycleOwner) {
            navigator.navigateTo(Layout.MAIN)
        }

        binding.viewModel?.loadCityName()

        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())

    }


}