package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.smerkis.weamther.R
import com.smerkis.weamther.activities.MainActivity
import com.smerkis.weamther.viewModels.SplashViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@FlowPreview
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).window.navigationBarColor = 4

        viewModel.errorData.observe(viewLifecycleOwner) { exception ->
            handleErrorCode(exception)
        }

        viewModel.preloadedData.observe(viewLifecycleOwner) {
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToMainFragment(
                    it.first,
                    it.second
                )
            )
        }
    }



}