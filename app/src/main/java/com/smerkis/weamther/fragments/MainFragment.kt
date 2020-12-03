package com.smerkis.weamther.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.smerkis.weamther.R
import com.smerkis.weamther.activities.MainActivity
import com.smerkis.weamther.components.WEATHER_ICON
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.getDateTime
import com.smerkis.weamther.getTemperature
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.viewModels.MainViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@FlowPreview
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val vModel: MainViewModel by viewModel()
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val args by navArgs<MainFragmentArgs>()
    private val forecastAdapter = ForecastsAdapter()
    private var _weather: WeatherInfo? = null
    private val weather get() = _weather!!

    companion object {
        const val SELECTED_CITY = "city_ch"
        const val MAIN_FRAGMENT = "main_frag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(MAIN_FRAGMENT) { _, bundle ->
            val city = bundle.getString(SELECTED_CITY)
            if (city != null) {
                vModel.searchCity(city)
            }
        }
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _weather = args.weather



        createToolbar(binding.tb, shouldSetUpBtn = false)
        setObserver()
        fillWidget()
        setListeners()
    }

    private fun setObserver() {

        vModel.apply {
            imageCity.observe(viewLifecycleOwner) {
                binding.cityImage.load(it)
                photoIsLoaded(false)
            }
            weatherInfo.observe(viewLifecycleOwner) {
                _weather = it
                downloadNewImage()
                vModel.loadForecast()
                fillWidget()
            }

            forecast.observe(viewLifecycleOwner) { forecast ->
                forecastAdapter.submitList(forecast.list)
            }

            errorData.observe(viewLifecycleOwner) {
                handleErrorCode(it)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.main_menu, menu)
    }

    private fun fillWidget() {

        binding.apply {

            cityImage.load(args.imagePath)
            recyclerView.adapter = forecastAdapter
            cityName.text = weather.name

            currentWeather.apply {
                temp.text = getTemperature(weather.main.temp)
                WEATHER_ICON[weather.weather[0].icon]?.let { iconWeather.load(it) }
                range.text = weather.main.getRange()
                pressureData.text = weather.main.getPressure()
                windData.text = weather.wind.getSpeed()
                humidityData.text = weather.main.getHumidity()
                dateTime.text = getDateTime(weather.dt.toLong())
            }
        }
        vModel.loadForecast()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.go_to_history -> findNavController().navigate(MainFragmentDirections.actionMainFragmentToHistoryFragment())
            R.id.refresh -> vModel.searchCity(weather.name)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setListeners() {
        binding.apply {
            fab.setOnClickListener {
                searchCity()
            }
            currentWeather.iconWeather.setOnClickListener { showShortToast(weather.weather[0].description) }
            cityImage.setOnClickListener {
                vModel.downloadNewImage()
                photoIsLoaded(true)
            }
        }
    }

    private fun searchCity() {
        val et = EditText(context)
        et.textSize = 20F
        showInfoDialog(
            title = "Search city",
            message = "Enter the name of the city",
            view = et
        ) {
            vModel.searchCity(et.text.toString())
        }
    }

    private fun photoIsLoaded(loaded: Boolean) {
        if (loaded) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }

    }
}