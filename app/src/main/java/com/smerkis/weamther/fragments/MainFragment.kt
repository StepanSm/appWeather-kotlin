package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.R
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.viewModels.MainViewModel
import javax.inject.Inject

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(R.layout.fragment_main) {
    override fun initDi() {
        MyApp.instance.getAppComponent()
            .activitySubComponentBuilder()
            .with(navigator as FragmentActivity)
            .build()
            .inject(this)
    }

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onStart() {
        super.onStart()
        MyApp.instance.getViewModelSubComponent().inject(viewModel)
        binding.viewModel = viewModel
        viewModel.weatherDummie.observe(viewLifecycleOwner) {
            binding.text.text = it
        }

        viewModel.load()
    }
}