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
import com.smerkis.weamther.components.ICONS
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.viewModels.MainViewModel
import kotlinx.coroutines.FlowPreview
import okhttp3.internal.addHeaderLenient
import javax.inject.Inject

@FlowPreview
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(R.layout.fragment_main) {

    @Inject
    lateinit var viewModel: MainViewModel
    override val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val args by navArgs<MainFragmentArgs>()
    private val forecastAdapter = ForecastsAdapter()


    override fun initDi() {
        MyApp.instance.getAppComponent()
            .activitySubComponentBuilder()
            .with(navigator as FragmentActivity)
            .build()
            .inject(this)
    }


    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        viewModel.imageCityData.observe(viewLifecycleOwner) {
            binding.toolbarCityImage.setImageBitmap(it)
            binding.progressBar.visibility = View.GONE

        }

        viewModel.forecast.observe(viewLifecycleOwner) {
            forecastAdapter.submitList(it.list)
        }

        binding.setVariable(BR.weather, args.weather)

        MyApp.instance.getViewModelSubComponent().inject(viewModel)
        binding.toolbarCityImage.setImageBitmap(args.image)
        ICONS[args.weather.weather[0].description]?.let { binding.iconWeather.load(it) }
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