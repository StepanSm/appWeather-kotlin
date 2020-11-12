package com.smerkis.weamther.di.modules

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.smerkis.weamther.viewModels.SplashViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {
    @Provides
    fun getSplashViewModel(activity: AppCompatActivity): SplashViewModel {
        return ViewModelProvider(activity).get(SplashViewModel::class.java)
    }
}
