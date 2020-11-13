package com.smerkis.weamther.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.image.ImageRepo
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel : AbstractViewModel() {

    @Inject
    lateinit var imageRepo: ImageRepo

    @Inject
    lateinit var weatherRepo: WeatherRepo

    val imageUrlData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val weatherInfoData: MutableLiveData<WeatherInfo> by lazy { MutableLiveData<WeatherInfo>() }
    val errorData: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }

    @SuppressLint("CheckResult")
    fun loadCityName() {
        println()
        viewModelScope.launch {
            weatherRepo.loadCity()
                .catch {
                    errorData.value = it }
                .collect { city ->
                    weatherRepo.downloadWeather(city)
                        .catch {
                            errorData.value = it }
                        .collect { weather ->
                            weatherRepo.saveWeather(city, weather)
                                .catch {
                                    errorData.value = it }
                            imageRepo.getRandomPhotoUrl(city)
                                .catch {
                                    errorData.value = it }
                                .collect { imageUrl ->
                                    imageUrlData.value = imageUrl
                                }
                            weatherInfoData.value = weather
                        }
                }
        }

    }


}

