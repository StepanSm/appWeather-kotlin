package com.smerkis.weamther.di.modules

import com.smerkis.weamther.MyApp
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: MyApp) {
    @Provides
    internal fun appProvide(): MyApp = app
}