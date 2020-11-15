package com.smerkis.weamther.viewModels

import android.annotation.SuppressLint
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
import java.lang.Exception
import javax.inject.Inject

class SplashViewModel : AbstractViewModel() {

    @Inject
    lateinit var imageRepo: ImageRepo

    @Inject
    lateinit var weatherRepo: WeatherRepo

    val imageUrlData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val weatherInfoData: MutableLiveData<WeatherInfo> by lazy { MutableLiveData<WeatherInfo>() }
    val errorData: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }


    init {
        Log.d("WeatherWorker", "ViewModel register")
        WeatherBus.instance.register(this)
    }

    @FlowPreview
    @Subscribe
    @SuppressLint("CheckResult")
    fun onWeatherUpdated(weather: WeatherInfo) {

        viewModelScope.launch {

            weatherRepo.saveCity(weather.name).flatMapConcat {
                Log.d("SplashViewModel ", "loadCityName")

                WeatherBus.instance.unregister(this@SplashViewModel)

                weatherRepo.saveWeather(weather.name, weather).map {

                    if (it) {
                        weatherInfoData.value = weather
                        Log.d("SplashViewModel", "weather saved")
                    } else {
                        errorData.value =  Exception("save weather error")
                    }
                }
            }.catch {
                errorData.value =  Exception("onWeatherUpdated error")
                Log.d("SplashViewModel", "error")

            }.collect ()
        }
    }

    @Subscribe
    fun onError(err: Throwable) {
        Log.d("WeatherWorker", "onError")
        errorData.value = err
    }
}

