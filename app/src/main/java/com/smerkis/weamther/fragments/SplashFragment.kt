package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smerkis.weamther.MyErrorHandler
import com.smerkis.weamther.R
import com.smerkis.weamther.databinding.FragmentSplashBinding
import com.smerkis.weamther.viewModels.SplashViewModel
import isdigital.errorhandler.ErrorHandler
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

@FlowPreview
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModel()
    private val binding: FragmentSplashBinding by viewBinding(FragmentSplashBinding::bind)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorData.observe(viewLifecycleOwner) { exception ->
            errorHandler(exception)
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

    private fun errorHandler(e: Throwable) {
        MyErrorHandler.errorHandler
            .on(MyErrorHandler.OFFLINE_CODE) { throwable: Throwable, errorHandler: ErrorHandler ->
                showLongToast(getString(R.string.network_connect))
                binding.progressCircular.visibility = View.INVISIBLE
            }.handle(e)
    }


}