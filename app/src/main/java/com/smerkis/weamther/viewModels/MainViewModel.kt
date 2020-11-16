package com.smerkis.weamther.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.components.CELSIUM
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.weather.WeatherRepo
import com.smerkis.weamther.worker.WeatherBus
import com.squareup.otto.Subscribe
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : AbstractViewModel() {

    @Inject
    lateinit var weatherRepo: WeatherRepo

    val weatherInfo: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorData: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }

    init {
        WeatherBus.instance.register(this)
    }

    @FlowPreview
    fun load() {
        viewModelScope.launch {

            weatherRepo.loadCity().flatMapConcat { city ->
                weatherRepo.loadWeather(city).map {
                    Log.d("MainViewModel", "weather loaded")
                    val message = "city: ${it?.name}\ntemp: ${it?.main?.temp}$CELSIUM\n ${
                        it?.weather?.get(0)?.description
                    }"
                    weatherInfo.value = message
                }
            }.catch {
                errorData.value = it
                Log.d("MainViewModel", "error here")
            }.collect()
        }
    }


    @Subscribe
    fun getWeather(weather: WeatherInfo) {
        Log.d("WeatherWorker", "MainViewModel weather loaded")
    }

    override fun onCleared() {
        super.onCleared()
        WeatherBus.instance.unregister(this)
    }
}
