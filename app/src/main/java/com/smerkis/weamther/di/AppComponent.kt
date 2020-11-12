package com.smerkis.weamther.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.SplashActivity
import com.smerkis.weamther.SplashViewModel
import com.smerkis.weamther.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Component(
    modules = [
        ApiFactoryModule::class,
        WeatherRepoModule::class,
        ImageRepoModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(appModule: MyApp): Builder
    }

    fun viewModelSubComponentBuilder(): ViewModelSubComponent.Builder
    fun activitySubComponentBuilder(): ActivitySubComponent.Builder
}

@Subcomponent
interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }

    fun inject(vModel: SplashViewModel)

}

@Subcomponent(modules = [ActivityModule::class])
interface ActivitySubComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun with(activity: AppCompatActivity): Builder
        fun build(): ActivitySubComponent
    }

    fun inject(splashActivity: SplashActivity)

}
