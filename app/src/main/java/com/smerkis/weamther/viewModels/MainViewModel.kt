package com.smerkis.weamther.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.components.CELSIUM
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel : AbstractViewModel() {

    @Inject
    lateinit var weatherRepo: WeatherRepo

    val weatherDummie: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorDate: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }

    fun load() {
        viewModelScope.launch {
            weatherRepo.loadCity().catch { errorDate.value = it }
                .collect { city ->
                    weatherRepo.loadWeather(city)
                        .catch { errorDate.value = it }
                        .collect {
                            val message = "city: ${it?.name}\ntemp: ${it?.main?.temp}$CELSIUM\n ${
                                it?.weather?.get(0)?.description
                            }"
                            weatherDummie.value = message
                        }
                }
        }
    }
}
