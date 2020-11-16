package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.R
import com.smerkis.weamther.activities.Layout
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.databinding.FragmentSplashBinding
import com.smerkis.weamther.showInfoDialog
import com.smerkis.weamther.viewModels.SplashViewModel
import javax.inject.Inject


class SplashFragment :
    BaseFragment<SplashViewModel, FragmentSplashBinding>(R.layout.fragment_splash) {

    override val binding: FragmentSplashBinding by viewBinding(FragmentSplashBinding::bind)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorData.observe(viewLifecycleOwner) { exception ->
            showInfoDialog("ERROR", exception.message)
            navigator.closeApp()
        }

        viewModel.preloadedData.observe(viewLifecycleOwner) {
            navigator.navigateTo(
                SplashFragmentDirections.actionSplashFragmentToMainFragment(
                    it.first,
                    it.second
                )
            )
        }
    }



}