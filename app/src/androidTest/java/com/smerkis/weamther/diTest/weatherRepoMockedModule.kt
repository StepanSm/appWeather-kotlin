package com.smerkis.weamther.diTest

import com.smerkis.weamther.repository.weather.PaperWeatherRepo
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.bind
import org.koin.dsl.module


@FlowPreview
fun weatherRepoMockedModule() = module {
    single { PaperWeatherRepo(get()) } bind (WeatherRepo::class)

}
