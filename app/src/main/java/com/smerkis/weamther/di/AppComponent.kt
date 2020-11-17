package com.smerkis.weamther.di

import androidx.fragment.app.FragmentActivity
import com.smerkis.weamther.MyApp
import com.smerkis.weamther.di.modules.*
import com.smerkis.weamther.worker.WeatherWorker
import com.smerkis.weamther.fragments.MainFragment
import com.smerkis.weamther.fragments.SplashFragment
import com.smerkis.weamther.viewModels.MainViewModel
import com.smerkis.weamther.viewModels.SplashViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@Component(
    modules = [
        ApiFactoryModule::class,
        WeatherRepoModule::class,
        ImageRepoModule::class,
    ]
)
@Singleton
@FlowPreview
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(appModule: MyApp): Builder
    }

    fun viewModelSubComponentBuilder(): ViewModelSubComponent.Builder
    fun activitySubComponentBuilder(): ActivitySubComponent.Builder
    fun workerSubComponentBuilder(): WorkerSubComponent.Builder
}

@Subcomponent
@FlowPreview
interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }

    fun inject(vModel: SplashViewModel)
    fun inject(vModel: MainViewModel)
}

@FlowPreview
@Subcomponent(modules = [ActivityModule::class])
interface ActivitySubComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun with(activity: FragmentActivity): Builder
        fun build(): ActivitySubComponent
    }

    fun inject(splashActivity: SplashFragment)
    fun inject(mainFragment: MainFragment)
}

@Subcomponent
interface WorkerSubComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): WorkerSubComponent
    }

    fun inject(worker: WeatherWorker)
}