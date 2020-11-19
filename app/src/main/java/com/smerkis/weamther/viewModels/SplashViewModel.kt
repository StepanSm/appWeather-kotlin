package com.smerkis.weamther.viewModels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.image.ImageRepo
import com.smerkis.weamther.repository.weather.WeatherRepo
import com.smerkis.weamther.worker.WeatherBus
import com.squareup.otto.Subscribe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class SplashViewModel : AbstractViewModel(), KoinComponent {

    private val weatherRepo: WeatherRepo by inject()
    private val imageRepo: ImageRepo by inject()

    val preloadedData: MutableLiveData<Pair<WeatherInfo, Bitmap>> by lazy { MutableLiveData() }
    val errorData: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }

    init {
        Log.d("WeatherWorker", "ViewModel register")
        WeatherBus.instance.register(this)
    }

    @FlowPreview
    @Subscribe
    fun onWeatherUpdated(weather: WeatherInfo) {

        viewModelScope.launch {
            Log.d("SplashViewModel", "onWeatherUpdated start")

            val cacheWeather = weatherRepo.saveCity(weather.name).flatMapConcat {
                withContext(Dispatchers.Main) {
                    WeatherBus.instance.unregister(this@SplashViewModel)
                }
                weatherRepo.saveWeather(weather.name, weather)
            }

            val image = imageRepo.downloadImage(weather.name)

            val zipped = cacheWeather.zip(image) { _, bitmap ->
                Pair(weather, bitmap)
            }

            zipped.catch {
                errorData.postValue(it)
            }.flowOn(Dispatchers.IO).collect {
                preloadedData.postValue(it)
            }
        }
    }

    @Subscribe
    fun onError(err: Throwable) {
        Log.d("WeatherWorker", "onError")
        errorData.value = err
    }
}

