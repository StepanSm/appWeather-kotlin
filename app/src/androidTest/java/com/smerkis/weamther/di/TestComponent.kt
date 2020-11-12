package com.smerkis.weamther.di

import com.smerkis.weamther.WeatherRepoInstrumentalTest
import com.smerkis.weamther.di.modules.ApiFactoryModule
import com.smerkis.weamther.di.modules.AppModule
import com.smerkis.weamther.di.modules.WeatherRepoModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApiFactoryModule::class,
        WeatherRepoModule::class]
)
interface TestComponent {
    fun inject(test: WeatherRepoInstrumentalTest)
}