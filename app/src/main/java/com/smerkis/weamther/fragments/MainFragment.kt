package com.smerkis.weamther.fragments

import androidx.fragment.app.FragmentActivity
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.R
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.viewModels.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
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
        text
        MyApp.instance.getViewModelSubComponent().inject(viewModel)
        binding.viewModel = viewModel
        viewModel.weatherInfo.observe(viewLifecycleOwner) {
            binding.text.text = it
        }

        viewModel.load()


    }
}