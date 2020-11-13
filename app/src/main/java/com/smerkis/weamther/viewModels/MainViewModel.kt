package com.smerkis.weamther.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.smerkis.weamther.components.CELSIUM
import com.smerkis.weamther.repository.Result
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Error
import javax.inject.Inject

class MainViewModel : AbstractViewModel() {

    @Inject
    lateinit var weatherRepo: WeatherRepo


    val weatherDummie: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorDate: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }

    fun load() {
        viewModelScope.launch {
            weatherRepo.loadCity()
                .collect { resultCity ->
                    when (resultCity) {
                        is Result.Success -> weatherRepo.loadWeather(resultCity.data)
                            .collect { weatherInfo ->
                                when (weatherInfo) {
                                    is Result.Success -> {
                                        val message =
                                            "city: ${weatherInfo.data?.name}\ntemp: ${weatherInfo.data?.main?.temp}$CELSIUM\n ${
                                                weatherInfo.data?.weather?.get(0)?.description
                                            }"
                                        weatherDummie.value = message
                                    }
                                    is Result.Error -> errorDate.value = weatherInfo.exception
                                }
                            }
                        is Result.Error -> errorDate.value = resultCity.exception
                    }
                }
        }
    }
}
