package com.smerkis.weamther.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryViewModel : AbstractViewModel(), KoinComponent {

    private val weatherRepo: WeatherRepo by inject()
    val cities: MutableLiveData<List<String>> by lazy { MutableLiveData<List<String>>() }

    fun loadHistory() {

        viewModelScope.launch {
            weatherRepo.loadWeatherHistory().catch {
                errorData.postValue(it)
            }.collect {
                val keys = ArrayList<String>()
                keys.addAll(it.keys)
                cities.postValue(keys)
            }
        }
    }

    fun deleteItemHistory(city: String) {
        viewModelScope.launch {
            weatherRepo.deleteItemHistory(city).catch {
                errorData.postValue(it)
            }.collect {
                val keys = ArrayList<String>()
                keys.addAll(it.keys)
                cities.postValue(keys)
            }
        }

    }
}