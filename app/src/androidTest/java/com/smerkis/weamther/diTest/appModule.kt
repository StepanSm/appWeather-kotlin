package com.smerkis.weamther.diTest

import com.smerkis.weamther.MyApp
import kotlinx.coroutines.FlowPreview
import org.koin.dsl.module

@FlowPreview
fun appModule() = module {
    single { MyApp.instance }
}