package com.smerkis.weamther.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.model.WeatherInfo
import com.smerkis.weamther.repository.ErrorHandler
import com.smerkis.weamther.repository.Result
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
                .collect { cityResult ->
                    when (cityResult) {
                        is Result.Success -> weatherRepo.downloadWeather(cityResult.data).collect {resultWeather ->
                            when(resultWeather){
                                is Result.Success -> weatherRepo.saveWeather(cityResult.data, resultWeather.data).collect { resultSaveWeather ->
                                    when(resultSaveWeather){
                                        is Result.Success -> imageRepo.getRandomPhotoUrl(cityResult.data).collect { photoUrl ->
                                            when(photoUrl){
                                                is Result.Success -> imageUrlData.value = photoUrl.data
                                                is Result.Error -> errorData.value = photoUrl.exception
                                            }
                                        }
                                        is Result.Error -> errorData.value = resultSaveWeather.exception
                                    }
                                }
                                is Result.Error -> errorData.value = resultWeather.exception
                            }
                        }
                        is Result.Error -> errorData.value = cityResult.exception
                    }
                }
        }

    }


}

