package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smerkis.weamther.*
import com.smerkis.weamther.databinding.FragmentSplashBinding
import com.smerkis.weamther.viewModels.SplashViewModel
import isdigital.errorhandler.ErrorHandler
import kotlinx.coroutines.FlowPreview
import java.lang.Exception
import javax.inject.Inject

@FlowPreview
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    @Inject
    lateinit var viewModel: SplashViewModel
    private val binding: FragmentSplashBinding by viewBinding(FragmentSplashBinding::bind)

    override fun initDi() {
        MyApp.instance.getAppComponent()
            .activitySubComponentBuilder()
            .with(activity as FragmentActivity)
            .build()
            .inject(this)

        MyApp.instance.getViewModelSubComponent().inject(viewModel)
    }


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