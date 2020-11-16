package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.R
import com.smerkis.weamther.components.ICONS
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.viewModels.MainViewModel
import javax.inject.Inject

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(R.layout.fragment_main) {

    override val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val args by navArgs<MainFragmentArgs>()


    override fun initDi() {
        MyApp.instance.getAppComponent()
            .activitySubComponentBuilder()
            .with(navigator as FragmentActivity)
            .build()
            .inject(this)
    }

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyApp.instance.getViewModelSubComponent().inject(viewModel)
        binding.toolbarCityImage.setImageBitmap(args.image)
        binding.title.text = "${args.weather.name}\n${args.weather.main.temp}"
        ICONS[args.weather.weather[0].description]?.let { binding.toolbarImage.load(it) }
    }
}