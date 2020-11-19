package com.smerkis.weamther.di.modules

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.smerkis.weamther.viewModels.MainViewModel
import com.smerkis.weamther.viewModels.SplashViewModel
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview

@Module
class ViewModelModule {
    @Provides
    fun getSplashViewModel(activity: FragmentActivity): SplashViewModel {
        return ViewModelProvider(activity).get(SplashViewModel::class.java)
    }

    @FlowPreview
    @Provides
    fun getMainViewModel(activity: FragmentActivity): MainViewModel {
        return ViewModelProvider(activity).get(MainViewModel::class.java)
    }
}
