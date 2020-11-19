package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.smerkis.weamther.BR
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.R
import com.smerkis.weamther.activities.MainActivity
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.viewModels.MainViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
class MainFragment : BaseFragment(R.layout.fragment_main) {

    @Inject
    lateinit var viewModel: MainViewModel
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val args by navArgs<MainFragmentArgs>()
    private val forecastAdapter = ForecastsAdapter()

    override fun initDi() {
        MyApp.instance.getAppComponent()
            .activitySubComponentBuilder()
            .with(activity as FragmentActivity)
            .build()
            .inject(this)
    }


    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        viewModel.imageCityData.observe(viewLifecycleOwner) {
            binding.toolbarCityImage.setImageBitmap(it)
        }

        viewModel.forecast.observe(viewLifecycleOwner) {
            forecastAdapter.submitList(it.list)
        }

        binding.setVariable(BR.weather, args.weather)

        MyApp.instance.getViewModelSubComponent().inject(viewModel)
        binding.toolbarCityImage.setImageBitmap(args.image)
        binding.iconWeather.load("http://openweathermap.org/img/wn/${args.weather.weather[0].icon}@2x.png")
        binding.iconWeather.setOnClickListener {
            showShortToast(args.weather.weather[0].description)
        }

        initRecycler()
    }


    override fun onStart() {
        super.onStart()
        viewModel.loadForecast()
    }

    private fun initRecycler() {
        binding.recyclerView.adapter = forecastAdapter
    }

}