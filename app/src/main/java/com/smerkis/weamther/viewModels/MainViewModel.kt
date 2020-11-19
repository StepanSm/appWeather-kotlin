package com.smerkis.weamther.viewModels

import android.graphics.Bitmap
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.components.CELSIUM
import com.smerkis.weamther.model.ApiForecast
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.image.ImageRepo
import com.smerkis.weamther.repository.weather.WeatherRepo
import com.smerkis.weamther.worker.WeatherBus
import com.squareup.otto.Subscribe
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
@FlowPreview
class MainViewModel : AbstractViewModel(), KoinComponent {

    private val weatherRepo: WeatherRepo by inject()
    private val imageRepo: ImageRepo by inject()

    val isPhotoLoaded = ObservableField(true)
    val weatherInfo: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val forecast: MutableLiveData<ApiForecast> by lazy { MutableLiveData<ApiForecast>() }
    val errorData: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }
    val imageCityData: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }

    init {
        WeatherBus.instance.register(this)
    }

    fun loadForecast() {
        viewModelScope.launch {
            weatherRepo.loadCity().flatMapConcat { city ->
                weatherRepo.getForecast(city)
            }.catch {
                errorData.value = it
            }.collect {
                forecast.value = it
            }
        }
    }

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

    fun downloadPhoto() {
        isPhotoLoaded.set(false)
        viewModelScope.launch {
            weatherRepo.loadCity().flatMapConcat { city ->
                imageRepo.downloadImage(city)
            }.catch {
                errorData.postValue(it)
            }.collect {
                imageCityData.postValue(it)
                isPhotoLoaded.set(true)
            }
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
