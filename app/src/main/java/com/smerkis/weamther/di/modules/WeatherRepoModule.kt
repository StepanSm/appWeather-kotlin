package com.smerkis.weamther.di.modules

import com.smerkis.weamther.api.ApiFactory
import com.smerkis.weamther.repository.PaperWeatherRepo
import com.smerkis.weamther.repository.WeatherRepo
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module(includes = [ApiFactoryModule::class])
class WeatherRepoModule {

    @Singleton
    @Provides
    fun getWeatherRepo(@Named("paper") repo: WeatherRepo): WeatherRepo = repo

    @Singleton
    @Named("paper")
    @Provides
    fun paperWeatherRepo(apiFactory: ApiFactory): WeatherRepo = PaperWeatherRepo(apiFactory)

}