package com.smerkis.weamther.fragments

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.smerkis.weamther.BR
import com.smerkis.weamther.R
import com.smerkis.weamther.activities.MainActivity
import com.smerkis.weamther.databinding.FragmentMainBinding
import com.smerkis.weamther.viewModels.MainViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@FlowPreview
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModel()
    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)
    private val args by navArgs<MainFragmentArgs>()
    private val forecastAdapter = ForecastsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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

        binding.toolbarCityImage.setImageBitmap(args.image)
        binding.iconWeather.load("http://openweathermap.org/img/wn/${args.weather.weather[0].icon}@2x.png")
        binding.iconWeather.setOnClickListener {
            showShortToast(args.weather.weather[0].description)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToHistoryFragment())
        }

        val menu = binding.actionMenu.menu
        (activity as MainActivity).menuInflater.inflate(R.menu.maim_menu, menu)

        initRecycler()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.maim_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }


    override fun onStart() {
        super.onStart()
        viewModel.loadForecast()
    }

    private fun initRecycler() {
        binding.recyclerView.adapter = forecastAdapter
    }

}