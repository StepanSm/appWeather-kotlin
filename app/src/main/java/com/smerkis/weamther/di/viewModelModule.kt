package com.smerkis.weamther.di

import com.smerkis.weamther.viewModels.HistoryViewModel
import com.smerkis.weamther.viewModels.MainViewModel
import com.smerkis.weamther.viewModels.SplashViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@FlowPreview
@OptIn(KoinApiExtension::class)
val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { MainViewModel() }
    viewModel { HistoryViewModel() }
}