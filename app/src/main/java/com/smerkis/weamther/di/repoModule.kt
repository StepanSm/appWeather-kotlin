package com.smerkis.weamther.di

import com.smerkis.weamther.repository.image.ImageRepo
import com.smerkis.weamther.repository.image.PaperImageRepo
import com.smerkis.weamther.repository.weather.PaperWeatherRepo
import com.smerkis.weamther.repository.weather.WeatherRepo
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.bind
import org.koin.dsl.module

@FlowPreview
val repositoryModule = module {
    single { PaperWeatherRepo(get()) } bind (WeatherRepo::class)
    single { PaperImageRepo(get()) } bind (ImageRepo::class)
}