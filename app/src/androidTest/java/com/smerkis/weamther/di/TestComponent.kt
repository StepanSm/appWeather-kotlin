package com.smerkis.weamther.di

import com.smerkis.weamther.ImageRepoInstrumentalTest
import com.smerkis.weamther.WeatherRepoInstrumentalTest
import com.smerkis.weamther.di.modules.ApiFactoryModule
import com.smerkis.weamther.di.modules.ImageRepoModule
import com.smerkis.weamther.di.modules.WeatherRepoModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApiFactoryModule::class,
        ImageRepoModule::class,
        WeatherRepoModule::class]
)
interface TestComponent {
    fun inject(test: WeatherRepoInstrumentalTest)
    fun inject(test: ImageRepoInstrumentalTest)
}