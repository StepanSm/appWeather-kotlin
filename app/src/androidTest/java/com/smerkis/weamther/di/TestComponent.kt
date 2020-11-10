package com.smerkis.weamther.di

import com.smerkis.weamther.ModuleTest
import com.smerkis.weamther.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface TestComponent {
    fun inject(test:ModuleTest)
}