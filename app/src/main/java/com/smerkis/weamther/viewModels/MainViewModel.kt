package com.smerkis.weamther.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.model.ApiForecast
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.forecast.ForecastRepo
import com.smerkis.weamther.repository.image.ImageRepo
import com.smerkis.weamther.repository.weather.WeatherRepo
import com.smerkis.weamther.worker.WeatherBus
import com.squareup.otto.Subscribe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
@FlowPreview
class MainViewModel : AbstractViewModel(), KoinComponent {

    private val weatherRepo: WeatherRepo by inject()
    private val imageRepo: ImageRepo by inject()
    private val forecastRepo: ForecastRepo by inject()

    val weatherInfo: MutableLiveData<WeatherInfo> by lazy { MutableLiveData<WeatherInfo>() }
    val imageCity: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val forecast: MutableLiveData<ApiForecast> by lazy { MutableLiveData<ApiForecast>() }

    init {
        WeatherBus.instance.register(this)
    }

    fun loadForecast() {
        viewModelScope.launch {
            weatherRepo.loadCity().flatMapConcat { city ->
                forecastRepo.downloadForecast(city)
            }.catch {
                errorData.value = it
            }.collect {
                forecast.value = it
            }
        }
    }

    fun searchCity(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.loadWeather(city).catch {
                errorData.postValue(it)
            }.collect {
                weatherInfo.postValue(it)
            }
        }
    }

    fun downloadNewImage() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.loadCity().flatMapConcat { city ->
                imageRepo.downloadImage(city)
            }.catch {
                errorData.postValue(it)
            }.collect {
                imageCity.postValue(it)
            }
        }
    }

    @Subscribe
    fun getWeather(weather: WeatherInfo) {
        Log.d("WeatherWorker", "MainViewModel weather loaded")
        weatherInfo.postValue(weather)
    }

    @Subscribe
    fun onError(err: Throwable) {
        Log.d("WeatherWorker", "onError")
        errorData.value = err
    }

    override fun onCleared() {
        super.onCleared()
        WeatherBus.instance.unregister(this)
    }
}
