package com.smerkis.weamther.di.modules

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.smerkis.weamther.viewModels.MainViewModel
import com.smerkis.weamther.viewModels.SplashViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {
    @Provides
    fun getSplashViewModel(activity: FragmentActivity): SplashViewModel {
        return ViewModelProvider(activity).get(SplashViewModel::class.java)
    }

    @Provides
    fun getMainViewModel(activity: FragmentActivity): MainViewModel {
        return ViewModelProvider(activity).get(MainViewModel::class.java)
    }
}
