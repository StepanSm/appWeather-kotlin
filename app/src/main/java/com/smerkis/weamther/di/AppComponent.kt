package com.smerkis.weamther.di

import com.smerkis.weamther.di.modules.ApiFactoryModule
import com.smerkis.weamther.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApiFactoryModule::class]
)
interface AppComponent {
}
