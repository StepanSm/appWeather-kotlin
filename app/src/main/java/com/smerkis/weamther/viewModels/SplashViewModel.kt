package com.smerkis.weamther.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.image.ImageRepo
import com.smerkis.weamther.repository.weather.WeatherRepo
import com.smerkis.weamther.worker.WeatherBus
import com.squareup.otto.Subscribe
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class SplashViewModel : AbstractViewModel(), KoinComponent {

    private val weatherRepo: WeatherRepo by inject()
    private val imageRepo: ImageRepo by inject()

    val preloadedData: MutableLiveData<Pair<WeatherInfo, String>> by lazy { MutableLiveData() }

    init {
        Log.d("WeatherWorker", "ViewModel register")
        WeatherBus.instance.register(this)
    }

    @FlowPreview
    @Subscribe
    fun onWeatherUpdated(weather: WeatherInfo) {
        WeatherBus.instance.unregister(this@SplashViewModel)

        viewModelScope.launch {
            imageRepo.downloadImage(weather.name).catch {
                errorData.postValue(it)
            }.collect { url ->
                preloadedData.postValue(Pair(weather, url))
            }
        }
    }

    @Subscribe
    fun onError(err: Throwable) {
        Log.d("WeatherWorker", "onError")
        errorData.value = err
    }
}

