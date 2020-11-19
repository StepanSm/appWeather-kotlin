package com.smerkis.weamther

import android.app.Application
import com.smerkis.weamther.di.AppComponent
import com.smerkis.weamther.di.DaggerAppComponent
import com.smerkis.weamther.di.ViewModelSubComponent
import com.smerkis.weamther.di.WorkerSubComponent
import io.paperdb.Paper
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MyApp : Application() {

    val appName: String
        get() = resources.getString(R.string.app_name)

    private lateinit var appComponent: AppComponent
    private lateinit var viewModelSubComponent: ViewModelSubComponent
    private lateinit var workerSubComponent: WorkerSubComponent
    fun getAppComponent(): AppComponent {
        return appComponent
    }

    fun getViewModelSubComponent(): ViewModelSubComponent {
        return viewModelSubComponent
    }

    fun getWorkerComponent(): WorkerSubComponent {
        return workerSubComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Paper.init(this)

        appComponent = DaggerAppComponent.builder().application(this).build()
        viewModelSubComponent = appComponent.viewModelSubComponentBuilder().build()
        workerSubComponent = appComponent.workerSubComponentBuilder().build()





    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}